package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.NotificationsApp;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.database.Queries;
import com.faaya.fernandoaranaandrade.demo.notifications.Util;
import com.faaya.fernandoaranaandrade.demo.utils.HourUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.faaya.fernandoaranaandrade.demo.EditProyectActivity.DATE_REGEX;

public class EditTaskActivity extends AppCompatActivity {

    public static final String FROM_ACTIVITY = "FROM_ACTIVITY";

    Spinner typesSpinner;

    Spinner proyectSpinner;

    Queries queries;

    EditText nameTaskEditText;

    Button dateEndTaskButton;

    Button realDataTaskButton;

    Button notificationButton;

    Button buttonDelete;

    EditText commentsTaskEditText;

    private TaskApp taskApp;

    Long idTipoCurrentIntent;

    String rangoCurrentIntent;

    private CheckBox checkBoxEditTask;

    long idProyectCurrentIntent;

    Button hourButton;

    ImageButton cleanDate, cleanHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queries = new Queries(this);
        Intent intent = getIntent();
        setTask(intent);
        setContentView(R.layout.activity_edit_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        typesSpinner = findViewById(R.id.proyectsSpinnerAll);
        proyectSpinner = findViewById(R.id.proyectSpinner);
        nameTaskEditText = findViewById(R.id.nameTaskEditText);
        dateEndTaskButton = findViewById(R.id.buttonEstimateDate);
        realDataTaskButton = findViewById(R.id.buttonRealDate);
        hourButton = findViewById(R.id.buttonHour);
        cleanDate = findViewById(R.id.imageButtonCleanDate);
        cleanHour = findViewById(R.id.imageViewCleanHour);
        commentsTaskEditText = findViewById(R.id.commentsTaskEditText);
        notificationButton = findViewById(R.id.button7);
        commentsTaskEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        List<TaskType> typesValues = queries.getAllTaskTypes();
        typesValues.add(new TaskType(""));
        typesSpinner.setAdapter(new ArrayAdapter<TaskType>(this, R.layout.spinner18, typesValues));
        typesSpinner.setSelection(typesValues.size() - 1);
        final List<Proyect> proyects = queries.getAllProyects();
        proyects.add(new Proyect(""));
        proyectSpinner.setAdapter(new ArrayAdapter<Proyect>(this, R.layout.spinner18, proyects));

        idTipoCurrentIntent = intent.getLongExtra(TaskEnum.ID_TYPE.toString(), 0);
        rangoCurrentIntent = intent.getStringExtra(TaskEnum.RANGO_TIEMPO.toString());
        idProyectCurrentIntent = intent.getLongExtra(TaskEnum.ID_PROYECT.toString(), 0);
        checkBoxEditTask = findViewById(R.id.checkBoxEditTask);
        fillData(typesValues, proyects);
        if (isNew()) {
            buttonDelete = findViewById(R.id.buttonDelete);
            buttonDelete.setVisibility(View.INVISIBLE);
        }

        typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fillTaskType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        proyectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fillProyectId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        nameTaskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fillName();
            }
        });
        dateEndTaskButton = findViewById(R.id.buttonEstimateDate);
        commentsTaskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fillComments();
            }
        });
        checkBoxEditTask.setText(R.string.finish);
        checkBoxEditTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    finishTask();
                } else {
                    unfinishTask();
                }
            }
        });
    }

    private void unfinishTask() {
        realDataTaskButton.setText(getString(R.string.setDate));
        realDataTaskButton.setVisibility(View.INVISIBLE);
        taskApp.setRealDate(null);
    }

    private void finishTask() {
        realDataTaskButton.setVisibility(View.VISIBLE);
        Date finishDate = new Date();
        String date = DateEnum.dateSimpleDateFormat.format(finishDate);
        realDataTaskButton.setText(date);
        taskApp.setRealDate(finishDate.getTime());
    }

    private void fillData(List<TaskType> typesValues, List<Proyect> proyects) {
        proyectSpinner.setSelection(proyectSpinner.getAdapter().getCount() - 1);
        if(taskApp.getName() != null){
            nameTaskEditText.setText(taskApp.getName());
        }
        if(taskApp.getDateEnd() != null && taskApp.getDateEnd() != 0){
            Date date = new Date(taskApp.getDateEnd());
            dateEndTaskButton.setText(DateEnum.dateSimpleDateFormat.format(date));
            if(!isBegingDate()){
                hourButton.setText(DateEnum.hourSimpleDateFormat.format(date));
            }
            fillCleanButtons();
        } else {
            hourButton.setVisibility(View.INVISIBLE);
            fillCleanButtons();
        }
        if (taskApp.getRealDate() != null && taskApp.getRealDate().longValue() != 0) {
            checkBoxEditTask.setChecked(true);
            realDataTaskButton.setText(DateEnum.dateSimpleDateFormat.format(new Date(taskApp.getRealDate())));
        } else {
            realDataTaskButton.setVisibility(View.INVISIBLE);
        }
        if(taskApp.getComments() != null){
            commentsTaskEditText.setText(taskApp.getComments());
        }
        if(taskApp.getIdType() == null){
            fillTypeData(typesValues, idTipoCurrentIntent);
        } else {
            fillTypeData(typesValues, taskApp.getIdType());
        }
        if(taskApp.getProyectId() == null){
            fillProyectData(proyects, idProyectCurrentIntent);
        } else {
            fillProyectData(proyects, taskApp.getProyectId());
        }
        if (taskApp.getDateNotification() != null){
            notificationButton.setText(taskApp.getDateNotification());
        }

    }

    private void fillCleanButtons() {
        if(hasEndDate(taskApp.getDateEnd())){
            cleanHour.setVisibility(View.VISIBLE);
            if(isBegingDate()){
                cleanHour.setVisibility(View.INVISIBLE);
            } else {
                cleanHour.setVisibility(View.VISIBLE);
            }
            cleanDate.setVisibility(View.VISIBLE);
        } else {
            cleanHour.setVisibility(View.INVISIBLE);
            cleanDate.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isBegingDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(taskApp.getDateEnd());
        return calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0;
    }

    private boolean hasEndDate(Long dateEnd) {
        return dateEnd != null && dateEnd.longValue() != 0;
    }

    private void fillProyectData(List<Proyect> proyects, Long idProyect) {
        if (idProyect != null && idProyect.longValue() != 0) {
            for (int index = 0; index < proyects.size(); index++) {
                if (proyects.get(index).getId() != null && proyects.get(index).getId().equals(idProyect)) {
                    proyectSpinner.setSelection(index);
                }
            }
        }
    }

    private void fillTypeData(List<TaskType> typesValues, Long idType) {
        if (idType != 0) {
            for (int index = 0; index < typesValues.size(); index++) {
                if (typesValues.get(index).getId() != null && typesValues.get(index).getId().equals(idType)) {
                    typesSpinner.setSelection(index);
                }
            }
        }
    }

    private boolean isNew() {
        return taskApp.getId() == null || taskApp.getId().longValue() == 0;
    }

    public void delete(View view) {
        FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(getString(R.string.eliminar_definitivamente));
        editNameDialogFragment.setOkAction(new OkAction() {
            @Override
            public void doAction() {
                queries.deleteTask(taskApp.getId());
                queries.deleteNotificationByIdTask(taskApp.getId());
                exit();
            }
        });
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    private void save() {
        try {
            if (isCorrectData()) {
                saveDataAndNotification();
                showMessage(getString(R.string.Tarea) + " " + taskApp.getName().toUpperCase() + " " + getString(R.string.guardada));
                exit();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void saveDataAndNotification() throws ParseException {
        fillTaskType();
        fillProyectId();
        fillName();
        fillDateEnd();
        fillComments();
        queries.saveOrUpdateTaskApp(taskApp);

        Long idTask = taskApp.getId();
        if(idTask == null){
            idTask = queries.getByIdProyectAndName(taskApp.getProyectId(), taskApp.getName()).getId();
        }
        if(isNotificationActive()){
            Long alarmTime = HourUtils.getCalendar(taskApp.getDateNotification());
            queries.saveUpdateOrDeleteNotifications(true, new NotificationsApp(alarmTime, idTask));
            Util.scheduleNotification(this,queries,alarmTime);
            System.out.println("Fecha notificación:" + new Date(alarmTime));
        } else {
            queries.deleteNotificationByIdTask(idTask);
        }

    }

    private boolean isNotificationActive() {
        return taskApp.getActiveNotification() != null && taskApp.getActiveNotification().equals(SettingsEnum.ON.toString());
    }

    private void fillComments() {
        taskApp.setComments(commentsTaskEditText.getText().toString());
    }

    private void fillDateEnd() {
        try {
            if (!dateEndTaskButton.getText().toString().equalsIgnoreCase(getString(R.string.setDate))) {
                Date dateEnd = DateEnum.dateSimpleDateFormat.parse(dateEndTaskButton.getText().toString());
                String hourString = hourButton.getText().toString();
                if(hourString.equalsIgnoreCase(getString(R.string.setHour))){
                    taskApp.setDateEnd(dateEnd.getTime());
                } else {
                    Date hourDate = DateEnum.hourSimpleDateFormat.parse(hourString);
                    Calendar hourCalendar = Calendar.getInstance();
                    hourCalendar.setTime(hourDate);
                    Calendar dateCalendar = Calendar.getInstance();
                    dateCalendar.setTime(dateEnd);
                    dateCalendar.set(Calendar.HOUR_OF_DAY, hourCalendar.get(Calendar.HOUR_OF_DAY));
                    dateCalendar.set(Calendar.MINUTE, hourCalendar.get(Calendar.MINUTE));
                    System.out.println(dateCalendar.getTime());
                    taskApp.setDateEnd(dateCalendar.getTimeInMillis());
                }
                hourButton.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillName() {
        taskApp.setName(nameTaskEditText.getText().toString());
    }

    private void fillProyectId() {
        taskApp.setProyectId(((Proyect) proyectSpinner.getSelectedItem()).getId());
    }

    private void fillTaskType() {
        taskApp.setIdType(((TaskType) typesSpinner.getSelectedItem()).getId());
    }

    private boolean isCorrectData() {
        if (typesSpinner.getSelectedItem() == null || typesSpinner.getSelectedItem().toString().isEmpty()) {
            showMessage(getString(R.string.No_se_ha_seleccionado_un_tipo_de_tarea));
            return false;
        }
        if (proyectSpinner.getSelectedItem() == null || ((Proyect) proyectSpinner.getSelectedItem()).getName().isEmpty()) {
            showMessage(getString(R.string.No_se_ha_seleccionado_un_proyecto));
            return false;
        }
        if (nameTaskEditText.getText() == null || nameTaskEditText.getText().toString().isEmpty()) {
            showMessage(getString(R.string.El_nombre_no_puede_estar_vacio));
            return false;
        }
        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void exit() {
        Intent newIntent = new Intent(this, MainActivity.class);
        String fromActivity = getIntent().getStringExtra(FROM_ACTIVITY);
        System.out.println("from:" + fromActivity);
        if(fromActivity != null){
            if(fromActivity.contains("AllTasksActivity")){
                newIntent = new Intent(this, AllTasksActivity.class);
                fillIBackIntent(newIntent);
            }
            if(fromActivity.contains("TaskListProyectActivity")){
                newIntent = new Intent(this, TaskListProyectActivity.class);
                newIntent.putExtra(TaskEnum.ID_PROYECT.toString(), idProyectCurrentIntent);
                Serializable serializable = getIntent().getSerializableExtra(TaskListProyectActivity.FILTER_BEAN);
                newIntent.putExtra(TaskListProyectActivity.FILTER_BEAN, serializable);
            }
            if(fromActivity.contains("CalendarActivity")){
                newIntent = new Intent(this, CalendarActivity.class);
            }
        }
        startActivity(newIntent);
        finish();
    }

    private void fillIBackIntent(Intent newIntent) {
        if (idTipoCurrentIntent != null && idTipoCurrentIntent.longValue() != 0) {
            newIntent.putExtra(TaskEnum.ID_TYPE.toString(), idTipoCurrentIntent);
        }
        if (rangoCurrentIntent != null && !rangoCurrentIntent.isEmpty()) {
            newIntent.putExtra(TaskEnum.RANGO_TIEMPO.toString(), rangoCurrentIntent);
        }
        if (idProyectCurrentIntent != 0) {
            newIntent.putExtra(TaskEnum.ID_PROYECT.toString(), idProyectCurrentIntent);
        }
    }

    private void setTask(Intent intent) {
        Serializable taskAppExtra = intent.getSerializableExtra(EditSemaforoTaskActivity.TASK);
        if (taskAppExtra == null) {
            Long id = intent.getLongExtra(MainActivity.ID_TASK, 0);
            if (id == 0) {
                taskApp = new TaskApp();
                taskApp.setWhiteSemaforo(queries.getValueByProperty(SettingsEnum.WHITE_SEMAFORO));
                taskApp.setYellowSemaforo(queries.getValueByProperty(SettingsEnum.YELLOW_SEMAFORO));
                taskApp.setOrangeSemaforo(queries.getValueByProperty(SettingsEnum.ORANGE_SEMAFORO));
                taskApp.setRedSemaforo(queries.getValueByProperty(SettingsEnum.RED_SEMAFORO));
                taskApp.setActiveSemaforo(queries.getValueByProperty(SettingsEnum.ACTIVE));
                taskApp.setRealSemaforo(queries.getValueByProperty(SettingsEnum.REAL_SEMAFORO));
                taskApp.setActiveNotification(SettingsEnum.OFF.toString());
            } else {
                taskApp = queries.getByIdTask(id);
                if(taskApp == null){ //tarea eliminada (posiblemente viene de una notificacion)
                    showMessage(getString(R.string.notExitTask));
                    exit();
                }
            }
            Serializable serializable = intent.getSerializableExtra(TaskEnum.END_DAY.toString());
            if (serializable != null) {
                Calendar calendar = (Calendar) intent.getSerializableExtra(TaskEnum.END_DAY.toString());
                dateEndTaskButton.setText(DateEnum.dateSimpleDateFormat.format(calendar.getTime()));
            }
        } else {
            taskApp = (TaskApp) taskAppExtra;
        }
    }

    public void set_hour(View view) {
        String textButton = hourButton.getText().toString();
        HourDialogFragment dialogFragment = HourDialogFragment.newInstance(" ", textButton);
        dialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                hourButton.setText(DateEnum.hourSimpleDateFormat.format(calendar.getTime()));
                fillDateEnd();
                fillCleanButtons();
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "fragment_edit_hour");
    }

    public void set_date(View view) {
        String textButton = null;
        if (dateEndTaskButton.getText().toString().matches(DATE_REGEX)) {
            textButton = dateEndTaskButton.getText().toString();
        }
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(" ", textButton);
        dateDialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                dateEndTaskButton.setText(DateEnum.dateSimpleDateFormat.format(calendar.getTime()));
                fillDateEnd();
                fillCleanButtons();
            }
        });
        dateDialogFragment.show(getSupportFragmentManager(), "fragment_edit_date");
    }

    public void set_date_real(View view) {
        String textButton = null;
        if (realDataTaskButton.getText().toString().matches(DATE_REGEX)) {
            textButton = realDataTaskButton.getText().toString();
        }
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(" ", textButton);
        dateDialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                try {
                    realDataTaskButton.setText(DateEnum.dateSimpleDateFormat.format(calendar.getTime()));
                    taskApp.setRealDate(calendar.getTimeInMillis());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        dateDialogFragment.show(getSupportFragmentManager(), "fragment_edit_date");
    }

    public void clean_date(View view) {
        taskApp.setDateEnd(null);
        dateEndTaskButton.setText(getString(R.string.setDate));
        hourButton.setText(getString(R.string.setHour));
        hourButton.setVisibility(View.INVISIBLE);
        fillCleanButtons();
    }

    public void clean_hour(View view) {
        hourButton.setText(getString(R.string.setHour));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(taskApp.getDateEnd());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        taskApp.setDateEnd(calendar.getTimeInMillis());
        fillCleanButtons();
    }

    public void edit_semaforo() {
        Intent newIntent = new Intent(this, EditSemaforoTaskActivity.class);
        newIntent.putExtra(EditSemaforoTaskActivity.TASK, taskApp);
        newIntent.putExtra(EditTaskActivity.FROM_ACTIVITY, getIntent().getStringExtra(FROM_ACTIVITY));
        Serializable serializable = getIntent().getSerializableExtra(TaskListProyectActivity.FILTER_BEAN);
        newIntent.putExtra(TaskListProyectActivity.FILTER_BEAN, serializable);
        fillIBackIntent(newIntent);
        startActivity(newIntent);
        finish();
    }

    public void edit_notification(View view) {
        Intent newIntent = new Intent(this, NotificationsSettingsTaskActivity.class);
        newIntent.putExtra(NotificationsSettingsTaskActivity.TASK, taskApp);
        newIntent.putExtra(EditTaskActivity.FROM_ACTIVITY, getIntent().getStringExtra(FROM_ACTIVITY));
        Serializable serializable = getIntent().getSerializableExtra(TaskListProyectActivity.FILTER_BEAN);
        newIntent.putExtra(TaskListProyectActivity.FILTER_BEAN, serializable);
        fillIBackIntent(newIntent);
        startActivity(newIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exit();
                return true;
            case R.id.saveTool:
                save();
                return true;
            case R.id.colorTool:
                edit_semaforo();
                return true;
            case R.id.finishTool:
                taskApp.setRealDate(new Date().getTime());
                save();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(taskApp != null && taskApp.getId() != null && (taskApp.getRealDate() == null || taskApp.getRealDate() == 0)){
            getMenuInflater().inflate(R.menu.tool_bar_edit_task, menu);
        } else {
            getMenuInflater().inflate(R.menu.tool_bar_edit_task_sin_finish, menu);
        }
        return true;
    }
}

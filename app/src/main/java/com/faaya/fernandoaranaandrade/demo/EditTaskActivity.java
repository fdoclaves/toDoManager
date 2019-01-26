package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.NotificationsApp;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.database.Queries;
import com.faaya.fernandoaranaandrade.demo.utils.HourUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.faaya.fernandoaranaandrade.demo.EditProyectActivity.DATE_REGEX;

public class EditTaskActivity extends AppCompatActivity {

    private static final long MIN_PERIOD_MILLIS = 15 * 60 * 1000L; // 15'

    private static final long MIN_FLEX_MILLIS = 5 * 60 * 1000L; // 5'

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    Spinner typesSpinner;

    Spinner proyectSpinner;

    Queries queries;

    EditText nameTaskEditText;

    Button dateEndTaskButton;

    Button realDataTaskButton;

    EditText commentsTaskEditText;

    private TaskApp taskApp;

    Long idTipoCurrentIntent;

    String rangoCurrentIntent;

    long idProyectCurrentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        queries = new Queries(this);
        typesSpinner = findViewById(R.id.proyectsSpinnerAll);
        proyectSpinner = findViewById(R.id.proyectSpinner);
        nameTaskEditText = findViewById(R.id.nameTaskEditText);
        dateEndTaskButton = findViewById(R.id.buttonEstimateDate);
        realDataTaskButton = findViewById(R.id.buttonRealDate);
        commentsTaskEditText = findViewById(R.id.commentsTaskEditText);
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
        Intent intent = getIntent();
        setTask(intent);
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
        fillData(typesValues, proyects);
        if (isNew()) {
            ImageButton deleteTaskEditImageButton = findViewById(R.id.deleteTaskEditImageButton);
            deleteTaskEditImageButton.setEnabled(false);
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

    }

    private void fillData(List<TaskType> typesValues, List<Proyect> proyects) {
        proyectSpinner.setSelection(proyectSpinner.getAdapter().getCount() - 1);
        if(taskApp.getName() != null){
            nameTaskEditText.setText(taskApp.getName());
        }
        if(taskApp.getDateEnd() != null){
            dateEndTaskButton.setText(simpleDateFormat.format(new Date(taskApp.getDateEnd())));
        }
        if (taskApp.getRealDate() != null && taskApp.getRealDate().longValue() != 0) {
            realDataTaskButton.setText(simpleDateFormat.format(new Date(taskApp.getRealDate())));
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
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(" Eliminar definitivamente ");
        editNameDialogFragment.setAlgo(new OkAction() {
            @Override
            public void doAction() {
                queries.deleteTask(taskApp.getId());
                exit();
            }
        });
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    public void save(View view) {
        try {
            if (isCorrectData()) {
                saveData();
                showMessage("Tarea guardada");
                exit();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void deleteDate(View view) {
        realDataTaskButton.setText(getString(R.string.addDate));
    }

    private void saveData() throws ParseException {
        fillTaskType();
        fillProyectId();
        fillName();
        fillDateEnd();
        fillRealDate();
        fillComments();
        queries.saveOrUpdateTaskApp(taskApp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(taskApp.getDateEnd());
        Calendar alarmTime = HourUtils.setCalendar(taskApp.getDateNotification(), calendar);
        boolean active = false;
        if(taskApp.getActiveNotification().equals(SettingsEnum.ON.toString()) && taskApp.getRealDate() == null){
            active = true;
        }
        Long id = taskApp.getId();
        if(id == null){
            id = queries.getByIdProyectAndName(taskApp.getProyectId(), taskApp.getName()).getId();
        }
        queries.saveUpdateOrDeleteNotifications(active, new NotificationsApp(alarmTime.getTimeInMillis(), id));
    }

    private void fillComments() {
        taskApp.setComments(commentsTaskEditText.getText().toString());
    }

    private void fillRealDate() {
        try {
            if (isValidDate(realDataTaskButton)) {
                Date realDate = simpleDateFormat.parse(realDataTaskButton.getText().toString());
                taskApp.setRealDate(realDate.getTime());
            } else {
                taskApp.setRealDate(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillDateEnd() {
        try {
            if (isValidDate(dateEndTaskButton)) {
                Date dateEnd = simpleDateFormat.parse(dateEndTaskButton.getText().toString());
                taskApp.setDateEnd(dateEnd.getTime());
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
            showMessage("No se ha seleccionado un tipo de tarea");
            return false;
        }
        if (proyectSpinner.getSelectedItem() == null || ((Proyect) proyectSpinner.getSelectedItem()).getName().isEmpty()) {
            showMessage("No se ha seleccionado un proyecto");
            return false;
        }
        if (nameTaskEditText.getText() == null || nameTaskEditText.getText().toString().isEmpty()) {
            showMessage("El Nombre no puede estar vacio");
            return false;
        }
        if (!isValidDate(dateEndTaskButton)) {
            showMessage("No se a configurado la fecha estimada");
            return false;
        }
        return true;
    }

    private boolean isValidDate(Button button) {
        return button.getText().toString().matches(DATE_REGEX);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void exit() {
        System.out.println(idTipoCurrentIntent);
        Intent newIntent = new Intent(this, AllTasksActivity.class);
        if (idTipoCurrentIntent != null && idTipoCurrentIntent.longValue() != 0) {
            newIntent.putExtra(TaskEnum.ID_TYPE.toString(), idTipoCurrentIntent);
        }
        if (rangoCurrentIntent != null && !rangoCurrentIntent.isEmpty()) {
            newIntent.putExtra(TaskEnum.RANGO_TIEMPO.toString(), rangoCurrentIntent);
        }
        if (idProyectCurrentIntent != 0) {
            newIntent.putExtra(TaskEnum.ID_PROYECT.toString(), idProyectCurrentIntent);
        }
        startActivity(newIntent);
        finish();
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
                taskApp.setActiveNotification(queries.getValueByProperty(SettingsEnum.ACTIVE_NOTIFICTION));
                taskApp.setDateNotification(queries.getValueByProperty(SettingsEnum.DATE_NOTIFICATION));
            } else {
                taskApp = queries.getByIdTask(id);
            }
            Serializable serializable = intent.getSerializableExtra(TaskEnum.END_DAY.toString());
            if (serializable != null) {
                Calendar calendar = (Calendar) intent.getSerializableExtra(TaskEnum.END_DAY.toString());
                dateEndTaskButton.setText(simpleDateFormat.format(calendar.getTime()));
            }
        } else {
            taskApp = (TaskApp) taskAppExtra;
        }
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
                dateEndTaskButton.setText(simpleDateFormat.format(calendar.getTime()));
                fillDateEnd();
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
                realDataTaskButton.setText(simpleDateFormat.format(calendar.getTime()));
                fillRealDate();
            }
        });
        dateDialogFragment.show(getSupportFragmentManager(), "fragment_edit_date");
    }

    public void edit_semaforo(View view) {
        Intent intent = new Intent(this, EditSemaforoTaskActivity.class);
        intent.putExtra(EditSemaforoTaskActivity.TASK, taskApp);
        startActivity(intent);
    }

    public void edit_notification(View view) {
        Intent intent = new Intent(this, NotificationsSettingsTaskActivity.class);
        intent.putExtra(NotificationsSettingsTaskActivity.TASK, taskApp);
        startActivity(intent);
    }
}

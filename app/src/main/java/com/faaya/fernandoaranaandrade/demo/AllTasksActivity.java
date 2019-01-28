package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.adapters.TaskAppAdapter;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AllTasksActivity extends AppCompatActivity {

    Spinner typeSpinner, proyectSpinner, rangeTimeSpinner;

    private Queries queries;

    ListView listView;

    List<TaskApp> taskToday = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_all_task);
        load();
    }

    private void load() {
        queries = new Queries(this);
        listView = findViewById(R.id.list_all_task);
        typeSpinner = findViewById(R.id.typeSpinner);
        List<TaskType> typesValues = new ArrayList<TaskType>();
        typesValues.add(new TaskType(TaskEnum.TODO.toString()));
        typesValues.addAll(queries.getAllTaskTypes());
        typeSpinner.setAdapter(new ArrayAdapter<TaskType>(this, R.layout.spinner18, typesValues));
        proyectSpinner = findViewById(R.id.proyectsSpinnerAll);
        List<Proyect> allProyects = new ArrayList<Proyect>();
        allProyects.add(new Proyect(getString(R.string.TODOS)));
        allProyects.addAll(queries.getAllProyects());
        proyectSpinner.setAdapter(new ArrayAdapter<Proyect>(this, R.layout.spinner18, allProyects));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToEditActivity(taskToday.get(position).getId());
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                FragmentManager fm = getSupportFragmentManager();
                EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(getString(R.string.eliminar_definitivamente));
                editNameDialogFragment.setOkAction(new OkAction() {
                    @Override
                    public void doAction() {
                        Long idTask = taskToday.get(position).getId();
                        queries.deleteTask(idTask);
                        queries.deleteNotificationByIdTask(idTask);
                        filter();
                    }
                });
                editNameDialogFragment.show(fm, "fragment_edit_name");
                return true;
            }
        });
        proyectSpinner.setSelection(0);
        rangeTimeSpinner = findViewById(R.id.rangeTimeSpinner);
        String[] rangeTimeValues = {getString(R.string.TODO), getString(R.string.HOY), getString(R.string.SEMANA), getString(R.string.MES)};
        rangeTimeSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner18, rangeTimeValues));
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        };
        rangeTimeSpinner.setOnItemSelectedListener(listener);
        proyectSpinner.setOnItemSelectedListener(listener);
        typeSpinner.setOnItemSelectedListener(listener);
        typeSpinner.setSelection(0);
        Intent intent = getIntent();
        fillData(intent, typesValues, allProyects, rangeTimeValues);
        filter();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditTask();
            }
        });
    }

    public void goToEditTask(){
        Intent intent = new Intent(this, EditTaskActivity.class);
        fillExtra(intent);
        startActivity(intent);
    }

    private void filter() {
        Proyect selectedProyect = (Proyect) proyectSpinner.getSelectedItem();
        String selectedRange = (String) rangeTimeSpinner.getSelectedItem();
        TaskType selectedTaskType = (TaskType) typeSpinner.getSelectedItem();
        Long idProyect = null;
        Long startRange = null;
        Long endRange = null;
        Long idTaskType = null;
        if (!selectedProyect.getName().equals(getString(R.string.TODOS))) {
            idProyect = selectedProyect.getId();
        }
        if (!selectedTaskType.getName().equals(getString(R.string.TODO))) {
            idTaskType = selectedTaskType.getId();
        }
        if (!selectedRange.equals(getString(R.string.TODO))) {
            startRange = getCalendarToday().getTimeInMillis();
        }
        if(selectedRange.equals(getString(R.string.HOY))){
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            calendarEnd.set(Calendar.MILLISECOND, 999);
            endRange = calendarEnd.getTimeInMillis();
        }
        if(selectedRange.equals(getString(R.string.SEMANA))){
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            calendarEnd.set(Calendar.MILLISECOND, 999);
            calendarEnd.set(Calendar.DAY_OF_WEEK, calendarEnd.getActualMaximum(Calendar.DAY_OF_WEEK));
            endRange = calendarEnd.getTimeInMillis();
        }
        if(selectedRange.equals(getString(R.string.MES))){
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            calendarEnd.set(Calendar.MILLISECOND, 999);
            calendarEnd.set(Calendar.DAY_OF_MONTH,calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
            endRange = calendarEnd.getTimeInMillis();
        }
        fillProyectSpinnerWithData(queries.selectTaskByIdProyectEndDateAndType(idProyect, startRange, endRange, idTaskType));
    }

    @NonNull
    private Calendar getCalendarToday() {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);
        return calendarStart;
    }

    private void fillProyectSpinnerWithData(List<TaskApp> taskApps) {
        taskToday.clear();
        taskToday.addAll(taskApps);
        listView.setAdapter(new TaskAppAdapter(this, taskApps));
    }

    private void fillData(Intent intent, List<TaskType> typesValues, List<Proyect> proyects, String[] rangeTimeValues) {
        Long id = intent.getLongExtra(TaskEnum.ID_TYPE.toString(), 0);
        if (id != 0) {
            for (int index = 0; index < typesValues.size(); index++) {
                if (typesValues.get(index).getId() != null && typesValues.get(index).getId().equals(id)) {
                    typeSpinner.setSelection(index);
                }
            }
        }

        Long idProyect = intent.getLongExtra(TaskEnum.ID_PROYECT.toString(), 0);
        if (idProyect != null && idProyect.longValue() != 0) {
            for (int index = 0; index < proyects.size(); index++) {
                if (proyects.get(index).getId() != null && proyects.get(index).getId().equals(idProyect)) {
                    proyectSpinner.setSelection(index);
                }
            }
        }

        String timeRange = intent.getStringExtra(TaskEnum.RANGO_TIEMPO.toString());
        if (timeRange != null && !timeRange.isEmpty()) {
            for (int index = 0; index < rangeTimeValues.length; index++) {
                if (rangeTimeValues[index].equals(timeRange)) {
                    rangeTimeSpinner.setSelection(index);
                }
            }
        }
    }

    private void goToEditActivity(long id) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(MainActivity.ID_TASK, id);
        fillExtra(intent);
        startActivity(intent);
    }

    private void fillExtra(Intent intent) {
        if (!typeSpinner.getSelectedItem().toString().equals(TaskEnum.TODO.toString())) {
            intent.putExtra(TaskEnum.ID_TYPE.toString(), ((TaskType) typeSpinner.getSelectedItem()).getId());
        }
        Proyect proyect = (Proyect) proyectSpinner.getSelectedItem();
        if (!proyect.getName().equals(getString(R.string.TODOS))) {
            intent.putExtra(TaskEnum.ID_PROYECT.toString(), proyect.getId());
        }
        if (!rangeTimeSpinner.getSelectedItem().toString().equals(TaskEnum.TODO.toString())) {
            intent.putExtra(TaskEnum.RANGO_TIEMPO.toString(), (String) rangeTimeSpinner.getSelectedItem());
        }
    }
}

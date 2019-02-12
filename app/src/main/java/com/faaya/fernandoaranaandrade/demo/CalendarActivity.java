package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskEnum;
import com.faaya.fernandoaranaandrade.demo.adapters.TaskAppAdapter;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private int year, month, dayOfMonth;

    private ListView listView;

    List<TaskApp> taskList = new ArrayList<>();

    private Queries queries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                CalendarActivity.this.year = year;
                CalendarActivity.this.month = month;
                CalendarActivity.this.dayOfMonth = dayOfMonth;
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Long endRangeDateStart = getStartDay(calendar);
                Long endRangeDateFinish = getEndDay(calendar);
                fillProyectSpinnerWithData(queries.selectTaskByIdProyectEndDateAndType(null, endRangeDateStart, endRangeDateFinish, null));

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditActivity(year, month, dayOfMonth, view);
            }
        });

        listView = findViewById(R.id.listViewCalendar);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToEditActivity(taskList.get(position).getId());
            }
        });
    }

    private void fillProyectSpinnerWithData(List<TaskApp> taskApps) {
        taskList.clear();
        taskList.addAll(taskApps);
        listView.setAdapter(new TaskAppAdapter(this, taskList));
    }

    private Long getEndDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    private Long getStartDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private void goToEditActivity(long id) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(MainActivity.ID_TASK, id);
        intent.putExtra(EditTaskActivity.FROM_ACTIVITY, this.getClass().getName());
        //fillExtra(intent);
        startActivity(intent);
    }

    private void goToEditActivity(int year, int month, int dayOfMonth, View view) {
        if (queries.getCountProyect() <= 0) {
            Snackbar.make(view, getString(R.string.youNeedToCreateAProjectBefore), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        if (queries.getCountTask() <= 0) {
            Snackbar.make(view, getString(R.string.youNeedToCreateAHaveCategories), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        Intent intent = new Intent(this, EditTaskActivity.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        intent.putExtra(TaskEnum.END_DAY.toString(), calendar);
        intent.putExtra(EditTaskActivity.FROM_ACTIVITY, this.getClass().getName());
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        queries = new Queries(this);
        Long endRangeDateStart = getStartDay(calendar);
        Long endRangeDateFinish = getEndDay(calendar);
        fillProyectSpinnerWithData(queries.selectTaskByIdProyectEndDateAndType(null, endRangeDateStart, endRangeDateFinish, null));
    }

}

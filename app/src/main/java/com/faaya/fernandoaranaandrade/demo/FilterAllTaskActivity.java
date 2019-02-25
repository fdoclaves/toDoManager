package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.faaya.fernandoaranaandrade.demo.Beans.AllTareasBean;
import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.adapters.CheckProjectAdapter;
import com.faaya.fernandoaranaandrade.demo.adapters.CheckTypeTaskAppAdapter;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterAllTaskActivity extends AppCompatActivity {

    private Spinner spinnerFinished;
    private AllTareasBean allTareasBean;
    private Button categoriesAllButton;
    private Button categoriesNoneButton;
    private ListView listViewCategoriesCheckTypes;
    private Queries queries;
    private Button projectsAllButton;
    private Button projectsNoneButton;
    private ListView listViewProjectsCheckTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_all_task);
        queries = new Queries(this);
        fillAllTareasBean();
        spinnerFinished = findViewById(R.id.spinnerFinished);
        final String[] rangeTimeValues = {getString(R.string.all), getString(R.string.ToDo), getString(R.string.done)};
        spinnerFinished.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner18, rangeTimeValues));
        spinnerFinished.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                allTareasBean.setUnfinish(rangeTimeValues[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        categoriesAllButton = findViewById(R.id.buttonAll);
        categoriesAllButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                allTareasBean.getCheckedCurrent().clear();
                allTareasBean.getCheckedCurrent().addAll(allTareasBean.getAllTaskType());
                listViewCategoriesCheckTypes.setAdapter(new CheckTypeTaskAppAdapter(v.getContext(), allTareasBean.getAllTaskType(), allTareasBean.getCheckedCurrent()));
            }
        });
        categoriesNoneButton = findViewById(R.id.buttonNone);
        categoriesNoneButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                allTareasBean.getCheckedCurrent().clear();
                listViewCategoriesCheckTypes.setAdapter(new CheckTypeTaskAppAdapter(v.getContext(), allTareasBean.getAllTaskType(), allTareasBean.getCheckedCurrent()));
            }
        });

        listViewCategoriesCheckTypes = findViewById(R.id.listViewCheckTypes);
        listViewCategoriesCheckTypes.setAdapter(new CheckTypeTaskAppAdapter(this, allTareasBean.getAllTaskType(), allTareasBean.getCheckedCurrent()));

        listViewCategoriesCheckTypes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        projectsAllButton = findViewById(R.id.buttonAllProjects);
        projectsAllButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                allTareasBean.getProjectsCheckedCurrent().clear();
                allTareasBean.getProjectsCheckedCurrent().addAll(allTareasBean.getAllProjects());
                listViewProjectsCheckTypes.setAdapter(new CheckProjectAdapter(v.getContext(), allTareasBean.getAllProjects(), allTareasBean.getProjectsCheckedCurrent()));
            }
        });
        projectsNoneButton = findViewById(R.id.buttonNoneProjects);
        projectsNoneButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                allTareasBean.getProjectsCheckedCurrent().clear();
                listViewProjectsCheckTypes.setAdapter(new CheckProjectAdapter(v.getContext(), allTareasBean.getAllProjects(), allTareasBean.getProjectsCheckedCurrent()));
            }
        });

        listViewProjectsCheckTypes = findViewById(R.id.listViewCheckTypesProjects);
        listViewProjectsCheckTypes.setAdapter(new CheckProjectAdapter(this, allTareasBean.getAllProjects(), allTareasBean.getProjectsCheckedCurrent()));
        listViewProjectsCheckTypes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        fillScreenByData(rangeTimeValues);
    }

    private void fillAllTareasBean() {
        Serializable extra = getIntent().getSerializableExtra(AllTasksActivity.FILTER);
        if (extra == null) {
            allTareasBean = new AllTareasBean();
            List<TaskType> allTaskTypes = queries.getAllTaskTypes();
            allTareasBean.setCheckedBefore(allTaskTypes);
            List<TaskType> otherList = new ArrayList<>();
            otherList.addAll(allTaskTypes);
            allTareasBean.setAllTaskType(otherList);
            allTareasBean.setCheckedCurrent(new ArrayList<TaskType>());
            allTareasBean.getCheckedCurrent().addAll(allTareasBean.getCheckedBefore());
            List<Proyect> allProyects = queries.getAllProyects();
            allTareasBean.setProjectCheckedBefore(allProyects);
            allTareasBean.setAllProjects(allProyects);
            allTareasBean.setProjectsCheckedCurrent(new ArrayList<Proyect>());
            allTareasBean.getProjectsCheckedCurrent().addAll(allTareasBean.getProjectCheckedBefore());
        } else {
            allTareasBean = (AllTareasBean) extra;
        }
    }

    private void fillScreenByData(String[] rangeTimeValues) {
        if (allTareasBean.getUnfinish() != null) {
            for (int i = 0; i < rangeTimeValues.length; i++) {
                if (rangeTimeValues[i].equalsIgnoreCase(allTareasBean.getUnfinish())) {
                    spinnerFinished.setSelection(i);
                    break;
                }
            }
        }
        if (allTareasBean.getEndDate() != null && allTareasBean.getStartDate() != null) {
            Button rangeButton = findViewById(R.id.button2);
            String startDate = DateEnum.dateSimpleDateFormat.format(allTareasBean.getStartDate().getTime());
            String endDate = DateEnum.dateSimpleDateFormat.format(allTareasBean.getEndDate().getTime());
            String text = startDate + " - " + endDate;
            rangeButton.setText(text);
        }
    }

    public void range(View view) {
        Intent newIntent = new Intent(this, RangeDatesActivity.class);
        newIntent.putExtra(AllTasksActivity.FILTER, allTareasBean);
        startActivity(newIntent);
    }

    public void cancel(View view) {
        finish();
    }

    public void filter(View view) {
        Intent newIntent = new Intent(this, AllTasksActivity.class);
        newIntent.putExtra(AllTasksActivity.FILTER, allTareasBean);
        startActivity(newIntent);
    }
}

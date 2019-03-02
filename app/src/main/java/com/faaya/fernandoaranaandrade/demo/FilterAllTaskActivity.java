package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import com.faaya.fernandoaranaandrade.demo.Beans.AllTareasBean;
import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.adapters.CheckProjectAdapter;
import com.faaya.fernandoaranaandrade.demo.adapters.CheckTypeTaskAppAdapter;
import com.faaya.fernandoaranaandrade.demo.database.Queries;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FilterAllTaskActivity extends AppCompatActivity {
    private AllTareasBean allTareasBean;
    private Button categoriesAllButton;
    private Button categoriesNoneButton;
    private ListView listViewCategoriesCheckTypes;
    private ListView listViewProjectsCheckTypes;
    private Button projectsAllButton;
    private Button projectsNoneButton;
    private Queries queries;
    private Spinner spinnerFinished;

    class C03732 implements OnClickListener {
        C03732() {
        }

        public void onClick(View view) {
            FilterAllTaskActivity.this.allTareasBean.getCheckedCurrent().clear();
            FilterAllTaskActivity.this.allTareasBean.getCheckedCurrent().addAll(FilterAllTaskActivity.this.allTareasBean.getAllTaskType());
            FilterAllTaskActivity.this.listViewCategoriesCheckTypes.setAdapter(new CheckTypeTaskAppAdapter(view.getContext(), FilterAllTaskActivity.this.allTareasBean.getAllTaskType(), FilterAllTaskActivity.this.allTareasBean.getCheckedCurrent()));
        }
    }

    class C03743 implements OnClickListener {
        C03743() {
        }

        public void onClick(View view) {
            FilterAllTaskActivity.this.allTareasBean.getCheckedCurrent().clear();
            FilterAllTaskActivity.this.listViewCategoriesCheckTypes.setAdapter(new CheckTypeTaskAppAdapter(view.getContext(), FilterAllTaskActivity.this.allTareasBean.getAllTaskType(), FilterAllTaskActivity.this.allTareasBean.getCheckedCurrent()));
        }
    }

    class C03765 implements OnClickListener {
        C03765() {
        }

        public void onClick(View view) {
            FilterAllTaskActivity.this.allTareasBean.getProjectsCheckedCurrent().clear();
            FilterAllTaskActivity.this.allTareasBean.getProjectsCheckedCurrent().addAll(FilterAllTaskActivity.this.allTareasBean.getAllProjects());
            FilterAllTaskActivity.this.listViewProjectsCheckTypes.setAdapter(new CheckProjectAdapter(view.getContext(), FilterAllTaskActivity.this.allTareasBean.getAllProjects(), FilterAllTaskActivity.this.allTareasBean.getProjectsCheckedCurrent()));
        }
    }

    class C03776 implements OnClickListener {
        C03776() {
        }

        public void onClick(View view) {
            FilterAllTaskActivity.this.allTareasBean.getProjectsCheckedCurrent().clear();
            FilterAllTaskActivity.this.listViewProjectsCheckTypes.setAdapter(new CheckProjectAdapter(view.getContext(), FilterAllTaskActivity.this.allTareasBean.getAllProjects(), FilterAllTaskActivity.this.allTareasBean.getProjectsCheckedCurrent()));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_filter_all_task);
        this.queries = new Queries(this);
        fillAllTareasBean();
        this.spinnerFinished = (Spinner) findViewById(R.id.spinnerFinished);
        final String[] valuesUnfinish = new String[]{getString(R.string.all), getString(R.string.ToDo), getString(R.string.done)};
        this.spinnerFinished.setAdapter(new ArrayAdapter(this, R.layout.spinner18, valuesUnfinish));
        this.spinnerFinished.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                FilterAllTaskActivity.this.allTareasBean.setUnfinish(valuesUnfinish[i]);
            }
        });
        this.categoriesAllButton = (Button) findViewById(R.id.buttonAll);
        this.categoriesAllButton.setOnClickListener(new C03732());
        this.categoriesNoneButton = (Button) findViewById(R.id.buttonNone);
        this.categoriesNoneButton.setOnClickListener(new C03743());
        this.listViewCategoriesCheckTypes = (ListView) findViewById(R.id.listViewCheckTypes);
        this.listViewCategoriesCheckTypes.setAdapter(new CheckTypeTaskAppAdapter(this, this.allTareasBean.getAllTaskType(), this.allTareasBean.getCheckedCurrent()));
        this.projectsAllButton = (Button) findViewById(R.id.buttonAllProjects);
        this.projectsAllButton.setOnClickListener(new C03765());
        this.projectsNoneButton = (Button) findViewById(R.id.buttonNoneProjects);
        this.projectsNoneButton.setOnClickListener(new C03776());
        this.listViewProjectsCheckTypes = (ListView) findViewById(R.id.listViewCheckTypesProjects);
        this.listViewProjectsCheckTypes.setAdapter(new CheckProjectAdapter(this, this.allTareasBean.getAllProjects(), this.allTareasBean.getProjectsCheckedCurrent()));
        fillScreenByData(valuesUnfinish);
    }

    private void fillAllTareasBean() {
        Serializable serializableExtra = getIntent().getSerializableExtra(AllTasksActivity.FILTER);
        if (serializableExtra == null) {
            this.allTareasBean = new AllTareasBean();
            List<TaskType> allTaskTypes = this.queries.getAllTaskTypes();
            this.allTareasBean.setCheckedBefore(allTaskTypes);
            List arrayList = new ArrayList();
            arrayList.addAll(allTaskTypes);
            this.allTareasBean.setAllTaskType(arrayList);
            this.allTareasBean.setCheckedCurrent(new ArrayList());
            this.allTareasBean.getCheckedCurrent().addAll(this.allTareasBean.getCheckedBefore());
            List allProyects = this.queries.getAllProyects();
            this.allTareasBean.setProjectCheckedBefore(allProyects);
            this.allTareasBean.setAllProjects(allProyects);
            this.allTareasBean.setProjectsCheckedCurrent(new ArrayList());
            this.allTareasBean.getProjectsCheckedCurrent().addAll(this.allTareasBean.getProjectCheckedBefore());
            return;
        }
        this.allTareasBean = (AllTareasBean) serializableExtra;
    }

    private void fillScreenByData(String[] valuesUnfinish) {
        if (this.allTareasBean.getUnfinish() != null) {
            for (int i = 0; i < valuesUnfinish.length; i++) {
                if (valuesUnfinish[i].equalsIgnoreCase(this.allTareasBean.getUnfinish())) {
                    this.spinnerFinished.setSelection(i);
                    break;
                }
            }
        }
        if (this.allTareasBean.getEndDate() != null && this.allTareasBean.getStartDate() != null) {
            Button button = findViewById(R.id.button2);
            String format = DateEnum.dateSimpleDateFormat.format(this.allTareasBean.getStartDate().getTime());
            String format2 = DateEnum.dateSimpleDateFormat.format(this.allTareasBean.getEndDate().getTime());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(format);
            stringBuilder.append(" - ");
            stringBuilder.append(format2);
            button.setText(stringBuilder.toString());
        }
    }

    public void range(View view) {
        Intent newIntent = new Intent(this, RangeDatesActivity.class);
        newIntent.putExtra(AllTasksActivity.FILTER, this.allTareasBean);
        startActivity(newIntent);
        finish();
    }

    public void cancel(View view) {
        Intent newIntent = new Intent(this, FilterAllTaskActivity.class);
        startActivity(newIntent);
        finish();
    }

    public void filter(View view) {
        Intent newIntent  = new Intent(this, AllTasksActivity.class);
        newIntent.putExtra(AllTasksActivity.FILTER, this.allTareasBean);
        startActivity(newIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent newIntent  = new Intent(this, AllTasksActivity.class);
        startActivity(newIntent);
        finish();
    }
}

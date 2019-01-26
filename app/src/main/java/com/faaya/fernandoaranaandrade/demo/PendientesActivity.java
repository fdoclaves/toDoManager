package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskAppAdapter;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PendientesActivity extends AppCompatActivity {

    public static String TODAY = "TODAY";

    private Queries queries;

    ListView listView;

    List<TaskApp> taskToday = new ArrayList<>();

    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendientes);
        queries = new Queries(this);
        aSwitch = findViewById(R.id.switchPendientesDia);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filter();
            }

        });
        listView = findViewById(R.id.listAllPendientes);
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
                EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(" Eliminar definitivamente ");
                editNameDialogFragment.setAlgo(new OkAction() {
                    @Override
                    public void doAction() {
                        queries.deleteTask(taskToday.get(position).getId());
                        filter();
                    }
                });
                editNameDialogFragment.show(fm, "fragment_edit_name");
                return true;
            }
        });
        Intent intent = getIntent();
        fillData(intent);
        filter();
    }

    private void filter() {
        List<TaskApp> allTask;
        if(aSwitch.isChecked()){
            allTask = queries.selectTaskByIdProyectEndDateAndType(null, getStartDate(), getEndDate(), null);
        } else {
            allTask = queries.getAllPendientesTask(getStartDate());
        }
        taskToday.clear();
        taskToday.addAll(allTask);
        listView.setAdapter(new TaskAppAdapter(this, allTask));
    }

    private long getEndDate() {
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);
        return calendarEnd.getTimeInMillis();
    }


    @NonNull
    private Long getStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private void fillData(Intent intent) {
        aSwitch.setChecked(intent.getBooleanExtra(TODAY, false));
    }

    private void goToEditActivity(long id) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(MainActivity.ID_TASK, id);
        startActivity(intent);
    }
}

package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.adapters.TaskAppAdapter;
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
        Intent intent = getIntent();
        fillData(intent);
    }

    private void filter() {
        List<TaskApp> allTask;
        if(aSwitch.isChecked()){
            allTask = queries.selectTaskByIdProyectEndDateAndType(null, getStartDate(), getEndDate(), (TaskType) null, true);
        } else {
            allTask = queries.getAllPendientesTask(getStartDate());
        }
        taskToday.clear();
        taskToday.addAll(allTask);
        listView.setAdapter(new TaskAppAdapter(this, allTask, false));
        if(taskToday.size() == 0){
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, getString(R.string.ThereAreNoPending), Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {
            String message = getString(R.string.hayTareas);
            showMessage(String.format(message, taskToday.size()));
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume(){
        super.onResume();
        filter();
    }
}

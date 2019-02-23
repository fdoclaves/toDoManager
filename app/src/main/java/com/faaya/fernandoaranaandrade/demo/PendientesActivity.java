package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.PendientesBean;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.adapters.TaskAppAdapter;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PendientesActivity extends AppCompatActivity {

    public static final String FILTER_BEAN = "FILTER_BEAN";
    public static String TODAY = "TODAY";

    private Queries queries;

    ListView listView;

    List<TaskApp> taskToday = new ArrayList<>();

    Switch switchChecked;

    Spinner spinnerFilterOrder;

    private String orderBy;

    private String[] orderFilters = new String[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendientes);
        queries = new Queries(this);
        switchChecked = findViewById(R.id.switchPendientesDia);
        spinnerFilterOrder = findViewById(R.id.spinnerFilterOrder);
        orderFilters[0]=getString(R.string.creationUp);
        orderFilters[1]=getString(R.string.creationDown);
        orderFilters[2]=getString(R.string.tagUp);
        orderFilters[3]=getString(R.string.tagDown);
        orderFilters[4]=getString(R.string.dateUp);
        orderFilters[5]=getString(R.string.dateDown);
        orderFilters[6]=getString(R.string.finish_order);
        orderFilters[7]=getString(R.string.finish_order_Down);
        spinnerFilterOrder.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner18, orderFilters));
        spinnerFilterOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                orderBy = (String) spinnerFilterOrder.getSelectedItem();
                filter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
        switchChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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
        if(orderBy != null){
            for (int index = 0; index < orderFilters.length; index++) {
                if(orderFilters[index].equals(orderBy)){
                    spinnerFilterOrder.setSelection(index);
                    break;
                }
            }
        }
        fillData(intent);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditActivity(null);
            }
        });
    }

    private void filter() {
        List<TaskApp> allTask;
        if(switchChecked.isChecked()){
            allTask = queries.selectTaskByIdProyectEndDateAndType(null, getStartDate(), getEndDate(), (TaskType) null, true, orderBy);
        } else {
            allTask = queries.getAllPendientesTask(getStartDate(), orderBy);
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
        switchChecked.setChecked(intent.getBooleanExtra(TODAY, false));
        Serializable serializable = intent.getSerializableExtra(FILTER_BEAN);
        if(serializable != null){
            fillFromSerializable(serializable);
        }
    }

    private void goToEditActivity(Long id) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        if(id != null){
            intent.putExtra(MainActivity.ID_TASK, id);
        }
        intent.putExtra(EditTaskActivity.FROM_ACTIVITY, this.getClass().getName());
        intent.putExtra(FILTER_BEAN, buildSerializable());
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        filter();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putSerializable(FILTER_BEAN, buildSerializable());
        super.onSaveInstanceState(savedInstanceState);
    }

    private Serializable buildSerializable() {
        return new PendientesBean(switchChecked.isChecked(), orderBy);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            fillFromSerializable(savedInstanceState.getSerializable(FILTER_BEAN));
        }
    }

    private void fillFromSerializable(Serializable serializable) {
        PendientesBean pendientesBean = (PendientesBean) serializable;
        switchChecked.setChecked(pendientesBean.getToday());
        orderBy = pendientesBean.getOrderBy();
        for (int i = 0; i < orderFilters.length; i++) {
            if(orderFilters[i].equalsIgnoreCase(orderBy)){
                spinnerFilterOrder.setSelection(i);
                break;
            }
        }
    }
}

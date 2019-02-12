package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskListProyectBean;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.adapters.TaskAppAdapter;
import com.faaya.fernandoaranaandrade.demo.database.Queries;
import android.support.v7.widget.Toolbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskListProyectActivity extends AppCompatActivity {

    public static final String ID_PROYECT = "ID_PROYECT";
    public static final String FILTER_BEAN = "FILTER_BEAN";
    private long idProyect;
    private Queries queries;
    private Proyect proyect;
    private List<TaskType> checked;
    private Boolean unfishedTask;
    private List<TaskType> allTaskType;
    private Integer countAllTask;

    ListView listView;
    List<TaskApp> allTask = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_all_task_list_proyect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent currentIntent = getIntent();
        idProyect = currentIntent.getLongExtra(ID_PROYECT, 0);
        queries = new Queries(this);
        allTaskType = queries.getAllTaskTypes();
        if(checked == null){
            checked = new ArrayList<>();
        }
        proyect = queries.getByIdProyect(idProyect);
        if(proyect == null){
            setTitle(getString(R.string.proyect).toUpperCase());
        } else {
            setTitle(proyect.getName().toUpperCase());
        }
        listView = findViewById(R.id.listViewEditTaskProyect);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToEditActivity(allTask.get(position).getId());
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
                        Long idTask = allTask.get(position).getId();
                        queries.deleteTask(idTask);
                        queries.deleteNotificationByIdTask(idTask);
                        filter(checked, unfishedTask);
                        showMessage(getString(R.string.task_delete));
                    }
                });
                editNameDialogFragment.show(fm, "fragment_edit_name");
                return true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditTask(view);
            }
        });
        Serializable serializable = currentIntent.getSerializableExtra(FILTER_BEAN);
        if(serializable != null && serializable instanceof TaskListProyectBean){
            TaskListProyectBean taskListProyectBean = (TaskListProyectBean) serializable;
            checked = taskListProyectBean.getChecked();
            unfishedTask = taskListProyectBean.getUnfishedTask();
        }
    }

    private void goToEditTask(View view){
        if(allTaskType.size() <= 1){
            Snackbar.make(view, getString(R.string.youNeedToCreateAHaveCategories), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(TaskEnum.ID_PROYECT.toString(), proyect.getId());
        intent.putExtra(EditTaskActivity.FROM_ACTIVITY, this.getClass().getName());
        startActivity(intent);
    }

    private void filter(List<TaskType> checked, Boolean unfishedTask) {
        List<TaskApp> taskApps = queries.selectTaskByIdProyectEndDateAndType(idProyect, null, null, checked, unfishedTask);
        fillProyectSpinnerWithData(taskApps);
        if(taskApps.size() == 0){
            View view = findViewById(android.R.id.content);
            Snackbar.make(view, getString(R.string.ThereAreNoTasks), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
    }

    private void fillProyectSpinnerWithData(List<TaskApp> taskApps) {
        allTask.clear();
        allTask.addAll(taskApps);
        listView.setAdapter(new TaskAppAdapter(this, taskApps));
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void goToEditActivity(long id) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(MainActivity.ID_TASK, id);
        intent.putExtra(EditTaskActivity.FROM_ACTIVITY, this.getClass().getName());
        intent.putExtra(TaskEnum.ID_PROYECT.toString(), proyect.getId());
        intent.putExtra(FILTER_BEAN, new TaskListProyectBean(checked, unfishedTask));
        startActivity(intent);
    }

    private void filterList(){
        if(countAllTask == 0){
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, getString(R.string.ThereAreNoTasksToFilter), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
            FragmentManager fm = getSupportFragmentManager();
            FilterTaskDialogFragment filterTaskDialogFragment = FilterTaskDialogFragment.newInstance(getString(R.string.eliminar_definitivamente));
            filterTaskDialogFragment.setData(new OkActionFilter() {
                @Override
                public void doAction(List<TaskType> checkedfilter, boolean unfishedTaskfilter) {
                    checked = checkedfilter;
                    unfishedTask = unfishedTaskfilter;
                    filter(checked, unfishedTask);
                }
            }, checked, unfishedTask, allTaskType);
            filterTaskDialogFragment.show(fm, "fragment_edit_name");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar_edit_proyect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent newIntent = new Intent(this, MainActivity.class);
                startActivity(newIntent);
                finish();
                return true;
            case R.id.editProyectTool:
                Intent intent = new Intent(this, EditProyectActivity.class);
                intent.putExtra(ID_PROYECT, idProyect);
                startActivity(intent);
                finish();
                return true;
            case R.id.filterEditProyectTool:
                filterList();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        filter(checked, unfishedTask);
        if(countAllTask == null){
            countAllTask = allTask.size();
        }
    }
}

package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.adapters.TaskAppAdapter;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.util.ArrayList;
import java.util.List;

public class TaskListProyectActivity extends AppCompatActivity {

    public static final String ID_PROYECT = "ID_PROYECT";
    private long idProyect;
    private Queries queries;
    private Proyect proyect;
    private List<TaskType> checked;
    private Boolean unfishedTask;
    private List<TaskType> allTaskType;
    private ImageButton imageButton;

    ListView listView;
    List<TaskApp> allTask = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list_proyect);
        idProyect = getIntent().getLongExtra(ID_PROYECT, 0);
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
        filter(checked, unfishedTask);
        if(allTask.size() == 0){
            imageButton = findViewById(R.id.button10);
            imageButton.setVisibility(View.INVISIBLE);
        }
    }

    private void filter(List<TaskType> checked, Boolean unfishedTask) {
        List<TaskApp> taskApps = queries.selectTaskByIdProyectEndDateAndType(idProyect, null, null, checked, unfishedTask);
        fillProyectSpinnerWithData(taskApps);
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
        startActivity(intent);
    }

    public void edit_proyect(View view) {
        Intent intent = new Intent(this, EditProyectActivity.class);
        intent.putExtra(ID_PROYECT, idProyect);
        startActivity(intent);
    }

    public void filterList(View view){
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
}

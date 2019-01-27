package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.adapters.TaskTypeAdapter;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.util.List;

public class ListTaskTypeActivity extends AppCompatActivity {

    private ListView listView;
    private Queries queries;
    private List<TaskType> allTaskTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task_type);
        listView = findViewById(R.id.allTaskType);
        queries = new Queries(this);
        allTaskTypes = queries.getAllTaskTypes();
        fillList(allTaskTypes);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToEditTaskType(allTaskTypes.get(position));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                FragmentManager fm = getSupportFragmentManager();
                EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(" Se eliminaran TODAS la tareas de este tipo ");
                editNameDialogFragment.setOkAction(new OkAction() {
                    @Override
                    public void doAction() {
                        Long idTaskType = allTaskTypes.get(position).getId();
                        queries.deleteNotificationByTaskType(idTaskType);
                        queries.deleteTaskType(idTaskType);
                        queries.deleteTaskByTaskType(idTaskType);
                        allTaskTypes = queries.getAllTaskTypes();
                        fillList(allTaskTypes);
                    }
                });
                editNameDialogFragment.show(fm, "fragment_delete_task_type");
                return true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditTaskType(null);
            }
        });
    }

    private void fillList(List<TaskType> allTaskTypes) {
        listView.setAdapter(new TaskTypeAdapter(this, allTaskTypes));
    }

    private void goToEditTaskType(TaskType taskType) {
        Intent intent = new Intent(this, EditTaskTypeActivity.class);
        if(taskType != null){
            intent.putExtra(TaskType.ID_TASK_TYPE,taskType);
        }
        startActivity(intent);
    }
}

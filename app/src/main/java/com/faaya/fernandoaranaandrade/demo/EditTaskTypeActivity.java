package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

public class EditTaskTypeActivity extends AppCompatActivity {

    private Queries queries;

    private EditText nameEditText;

    private TaskType taskType;

    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_type);
        queries = new Queries(this);
        nameEditText = findViewById(R.id.editTextAddTaskType);
        Intent intent = getIntent();
        if (intent.getSerializableExtra(TaskType.ID_TASK_TYPE) != null) {
            taskType = (TaskType) intent.getSerializableExtra(TaskType.ID_TASK_TYPE);
            nameEditText.setText(taskType.getName());
        } else {
            taskType = new TaskType();
            deleteButton = findViewById(R.id.imageButtonDeleteTypeTask);
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveTaskType(View view) {
        if (nameEditText.getText() != null && !nameEditText.getText().toString().isEmpty()) {
            taskType.setName(nameEditText.getText().toString());
            queries.saveOrUpdate(taskType);
            showMessage(getString(R.string.se_guardo_correctamente));
            exit();
        } else {
            showMessage(getString(R.string.el_nombre_no_es_valido));
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void deleteTaskType(View view) {
        Integer count = queries.getCountTaskByTypeTask(taskType.getId());
        FragmentManager fm = getSupportFragmentManager();
        String title = getString(R.string.alertDeleteCategory);
        String format = String.format(title, count);
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(format);
        editNameDialogFragment.setOkAction(new OkAction() {
            @Override
            public void doAction() {
                queries.deleteNotificationByTaskType(taskType.getId());
                queries.deleteTaskType(taskType.getId());
                queries.deleteTaskByTaskType(taskType.getId());
                exit();
            }
        });
        editNameDialogFragment.show(fm, "fragment_edit_task_type");
    }

    private void exit() {
        Intent intent = new Intent(this, ListTaskTypeActivity.class);
        startActivity(intent);
        finish();
    }
}

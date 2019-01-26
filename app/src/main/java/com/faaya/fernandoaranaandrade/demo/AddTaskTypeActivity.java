package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

public class AddTaskTypeActivity extends AppCompatActivity {

    private Queries queries;

    private EditText nameEditText;

    private TaskType taskType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_type);
        queries = new Queries(this);
        nameEditText = findViewById(R.id.editTextAddTaskType);
        Intent intent = getIntent();
        if (intent.getSerializableExtra(TaskType.ID_TASK_TYPE) != null) {
            taskType = (TaskType) intent.getSerializableExtra(TaskType.ID_TASK_TYPE);
            nameEditText.setText(taskType.getName());
        } else {
            taskType = new TaskType();
        }
    }

    public void saveTaskType(View view) {
        if (nameEditText.getText() != null && !nameEditText.getText().toString().isEmpty()) {
            taskType.setName(nameEditText.getText().toString());
            queries.saveOrUpdate(taskType);
            showMessage("Se guardo correctamente");
            goToSettinsActivity();
        } else {
            showMessage("El nombre no es válido");
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void deleteTaskType(View view) {
        //se eliminaran las tareas
        if (taskType.getId() == null) {
            Snackbar.make(view, "Debe ser guardado antes", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(" Se eliminaran TODAS la tareas de este tipo ");
            editNameDialogFragment.setAlgo(new OkAction() {
                @Override
                public void doAction() {
                    queries.deleteTaskType(taskType.getId());
                    queries.deleteTaskByTaskType(taskType.getId());
                    goToSettinsActivity();
                }
            });
            editNameDialogFragment.show(fm, "fragment_edit_name");
        }
    }

    private void goToSettinsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}

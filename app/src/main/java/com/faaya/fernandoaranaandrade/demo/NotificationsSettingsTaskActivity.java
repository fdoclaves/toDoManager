package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotificationsSettingsTaskActivity extends AppCompatActivity {

    public static final String REGEX = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9] ((AM)|(PM))$$";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
    public static final String TASK = "TASK";

    Button hourButton;
    Switch aSwitch;
    private ImageButton imageButton;
    private TaskApp taskApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_settings);
        imageButton = findViewById(R.id.imageButtonNotificationSave);
        imageButton.setImageResource(R.drawable.ic_keyboard_backspace);
        imageButton.setVisibility(View.INVISIBLE);
        hourButton = findViewById(R.id.notifacionButton);
        aSwitch = findViewById(R.id.switchNotification);
        taskApp = (TaskApp) getIntent().getSerializableExtra(TASK);
        fillSwitch();
        fillHourButton();
    }

    private void fillHourButton() {
        String dateNotification = taskApp.getDateNotification();
        if (dateNotification != null) {
            hourButton.setText(dateNotification);
        }
    }

    private void fillSwitch() {
        String active = taskApp.getActiveNotification();
        if (active != null && active.equals(SettingsEnum.ON.toString())) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }
    }

    public void set_hour(View view) {
        String textButton = null;
        if (hourButton.getText().toString().matches(REGEX)) {
            textButton = hourButton.getText().toString();
        }
        HourDialogFragment dialogFragment = HourDialogFragment.newInstance(" ", textButton);
        dialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                hourButton.setText(simpleDateFormat.format(calendar.getTime()));
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "fragment_edit_hour");
    }

    public void save(View view) {
        saveData();
    }

    private void saveData() {
        if (aSwitch.isChecked()) {
            taskApp.setActiveNotification(SettingsEnum.ON.toString());
        } else {
            taskApp.setActiveNotification(SettingsEnum.OFF.toString());
        }
        taskApp.setDateNotification(hourButton.getText().toString());

        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(TASK, taskApp);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

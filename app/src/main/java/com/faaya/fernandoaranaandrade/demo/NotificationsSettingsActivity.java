package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotificationsSettingsActivity extends AppCompatActivity {

    public static final String REGEX = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9] ((AM)|(PM))$$";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

    Button hourButton;
    Switch aSwitch;
    private Queries queries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_settings);
        hourButton = findViewById(R.id.notifacionButton);
        queries = new Queries(this);
        aSwitch = findViewById(R.id.switchNotification);
        String activeNotification = queries.getValueByProperty(SettingsEnum.ACTIVE_NOTIFICTION);
        aSwitch.setChecked(activeNotification.equals(SettingsEnum.ON.toString()));
        String hourNotification = queries.getValueByProperty(SettingsEnum.DATE_NOTIFICATION);
        hourButton.setText(hourNotification);
    }

    public void set_hour(View view) {
        String textButton = null;
        if(hourButton.getText().toString().matches(REGEX)){
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
        if (aSwitch.isChecked()) {
            queries.saveProperty(SettingsEnum.ACTIVE_NOTIFICTION, SettingsEnum.ON.toString());
        } else {
            queries.saveProperty(SettingsEnum.ACTIVE_NOTIFICTION, SettingsEnum.OFF.toString());
        }
        queries.saveProperty(SettingsEnum.DATE_NOTIFICATION, hourButton.getText().toString());

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}

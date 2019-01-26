package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void go_to_about_me(View view) {
        Intent intent = new Intent(this, AboutMeActivity.class);
        startActivity(intent);
    }

    public void go_to_semaforo(View view) {
        Intent intent = new Intent(this, EditSemaforoActivity.class);
        startActivity(intent);
    }

    public void goToAddTaskType(View view) {
        Intent intent = new Intent(this, ListTaskTypeActivity.class);
        startActivity(intent);
    }

    public void goToNotificationSettings(View view) {
        Intent intent = new Intent(this, NotificationsSettingsActivity.class);
        startActivity(intent);
    }
}

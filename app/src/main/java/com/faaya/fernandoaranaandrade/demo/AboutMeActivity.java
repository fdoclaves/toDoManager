package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AboutMeActivity extends AppCompatActivity {

    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
    }

    public void go_url(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_web)));
        startActivity(intent);
    }

    public void go_monitor(View view) {
        count++;
        if(count==5){
            count = 1;
            Intent intent = new Intent(this, MonitorActivity.class);
            startActivity(intent);
        }
    }
}

package com.faaya.fernandoaranaandrade.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;

import com.faaya.fernandoaranaandrade.demo.database.Queries;

public class PendientesActivity extends AppCompatActivity {

    private Queries queries;

    private CheckBox redCheckBox;

    private CheckBox yellowCheckBox;

    private CheckBox greenCheckBox;

    private CheckBox orangeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendientes);
        queries = new Queries(this);
        redCheckBox = findViewById(R.id.checkBoxRed);
        yellowCheckBox = findViewById(R.id.checkBoxYellow);
        greenCheckBox = findViewById(R.id.checkBoxGreen);
        orangeCheckBox = findViewById(R.id.checkBoxOrange);
    }
}

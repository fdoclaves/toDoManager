package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.faaya.fernandoaranaandrade.demo.Beans.AllTareasBean;
import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;

import java.util.Calendar;

public class RangeDatesActivity extends AppCompatActivity {

    private AllTareasBean allTareasBean;
    private Button startButton, endButton;
    private Switch switchRangeDates;
    private ConstraintLayout layoutRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_dates);
        allTareasBean = (AllTareasBean) getIntent().getSerializableExtra(AllTasksActivity.FILTER);
        startButton = findViewById(R.id.buttonStartDate);
        endButton = findViewById(R.id.buttonEndDate);
        layoutRange = findViewById(R.id.layoutRange);
        switchRangeDates = findViewById(R.id.switchRangeDates);
        switchRangeDates.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layoutRange.setVisibility(View.VISIBLE);
                } else {
                    layoutRange.setVisibility(View.INVISIBLE);
                    allTareasBean.setEndDate(null);
                    allTareasBean.setStartDate(null);
                    startButton.setText(getString(R.string.setDate));
                    endButton.setText(getString(R.string.setDate));
                }
            }
        });
        fillData();
    }

    private void fillData() {
        Calendar endDate = allTareasBean.getEndDate();
        Calendar startDate = allTareasBean.getStartDate();
        if (endDate != null && startDate != null) {
            switchRangeDates.setChecked(true);
            startButton.setText(DateEnum.dateSimpleDateFormat.format(allTareasBean.getStartDate().getTime()));
            endButton.setText(DateEnum.dateSimpleDateFormat.format(allTareasBean.getEndDate().getTime()));
        } else {
            switchRangeDates.setChecked(false);
            layoutRange.setVisibility(View.INVISIBLE);
        }
    }

    public void endDate(View view) {
        String textButton = null;
        if (!endButton.getText().toString().equalsIgnoreCase(getString(R.string.setDate))) {
            textButton = endButton.getText().toString();
        }
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(" ", textButton);
        dateDialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                String format = DateEnum.dateSimpleDateFormat.format(calendar.getTime());
                endButton.setText(format);
                allTareasBean.setEndDate(calendar);
            }
        });
        dateDialogFragment.show(getSupportFragmentManager(), "fragment_edit_date");
    }

    public void startDate(View view) {
        String textButton = null;
        if (!startButton.getText().toString().equalsIgnoreCase(getString(R.string.setDate))) {
            textButton = startButton.getText().toString();
        }
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(" ", textButton);
        dateDialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                String format = DateEnum.dateSimpleDateFormat.format(calendar.getTime());
                startButton.setText(format);
                allTareasBean.setStartDate(calendar);
            }
        });
        dateDialogFragment.show(getSupportFragmentManager(), "fragment_edit_date");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exit() {
        if (switchRangeDates.isChecked()) {
            View view = findViewById(android.R.id.content);
            if (endButton.getText().toString().equalsIgnoreCase(getString(R.string.setDate))) {
                Snackbar.make(view, getString(R.string.youNeedToSetDate), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return;
            }
            if (startButton.getText().toString().equalsIgnoreCase(getString(R.string.setDate))) {
                Snackbar.make(view, getString(R.string.youNeedToSetDate), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return;
            }
        }
        Intent newIntent = new Intent(this, FilterAllTaskActivity.class);
        newIntent.putExtra(AllTasksActivity.FILTER, allTareasBean);
        startActivity(newIntent);
    }
}

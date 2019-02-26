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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import com.faaya.fernandoaranaandrade.demo.Beans.AllTareasBean;
import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;
import java.util.Calendar;

public class RangeDatesActivity extends AppCompatActivity {
    private AllTareasBean allTareasBean;
    private Button endButton;
    private ConstraintLayout layoutRange;
    private Button startButton;
    private Switch switchRangeDates;

    class C04001 implements OnCheckedChangeListener {
        C04001() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                RangeDatesActivity.this.layoutRange.setVisibility(View.VISIBLE);
                return;
            }
            RangeDatesActivity.this.layoutRange.setVisibility(View.INVISIBLE);
            RangeDatesActivity.this.allTareasBean.setEndDate(null);
            RangeDatesActivity.this.allTareasBean.setStartDate(null);
            RangeDatesActivity.this.startButton.setText(RangeDatesActivity.this.getString(R.string.setDate));
            RangeDatesActivity.this.endButton.setText(RangeDatesActivity.this.getString(R.string.setDate));
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_range_dates);
        this.allTareasBean = (AllTareasBean) getIntent().getSerializableExtra(AllTasksActivity.FILTER);
        this.startButton = (Button) findViewById(R.id.buttonStartDate);
        this.endButton = (Button) findViewById(R.id.buttonEndDate);
        this.layoutRange = (ConstraintLayout) findViewById(R.id.layoutRange);
        this.switchRangeDates = (Switch) findViewById(R.id.switchRangeDates);
        this.switchRangeDates.setOnCheckedChangeListener(new C04001());
        fillData();
    }

    private void fillData() {
        Calendar endDate = this.allTareasBean.getEndDate();
        Calendar startDate = this.allTareasBean.getStartDate();
        if (endDate == null || startDate == null) {
            this.switchRangeDates.setChecked(false);
            this.layoutRange.setVisibility(View.INVISIBLE);
            return;
        }
        this.switchRangeDates.setChecked(true);
        this.startButton.setText(DateEnum.dateSimpleDateFormat.format(this.allTareasBean.getStartDate().getTime()));
        this.endButton.setText(DateEnum.dateSimpleDateFormat.format(this.allTareasBean.getEndDate().getTime()));
    }

    public void endDate(View view) {
        String textButton = null;
        if (endButton.getText().toString().matches(DateEnum.DATE_REGEX)) {
            textButton = endButton.getText().toString();
        }
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(" ", textButton);
        dateDialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                RangeDatesActivity.this.endButton.setText(DateEnum.dateSimpleDateFormat.format(calendar.getTime()));
                RangeDatesActivity.this.allTareasBean.setEndDate(calendar);
            }
        });
        dateDialogFragment.show(getSupportFragmentManager(), "fragment_edit_date");
    }

    public void startDate(View view) {
        String textButton = null;
        if (startButton.getText().toString().matches(DateEnum.DATE_REGEX)) {
            textButton = startButton.getText().toString();
        }
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(" ", textButton);
        dateDialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                RangeDatesActivity.this.startButton.setText(DateEnum.dateSimpleDateFormat.format(calendar.getTime()));
                RangeDatesActivity.this.allTareasBean.setStartDate(calendar);
            }
        });
        dateDialogFragment.show(getSupportFragmentManager(), "fragment_edit_date");
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        exit();
        return true;
    }

    private void exit() {
        if (this.switchRangeDates.isChecked()) {
            View parentLayout = findViewById(android.R.id.content);
            if (this.endButton.getText().toString().equalsIgnoreCase(getString(R.string.setDate))) {
                Snackbar.make(parentLayout, getString(R.string.youNeedToSetDate), 0).setAction((CharSequence) "Action", null).show();
                return;
            } else if (this.startButton.getText().toString().equalsIgnoreCase(getString(R.string.setDate))) {
                Snackbar.make(parentLayout, getString(R.string.youNeedToSetDate), 0).setAction((CharSequence) "Action", null).show();
                return;
            }
        }
        Intent intent = new Intent(this, FilterAllTaskActivity.class);
        intent.putExtra(AllTasksActivity.FILTER, this.allTareasBean);
        startActivity(intent);
    }
}

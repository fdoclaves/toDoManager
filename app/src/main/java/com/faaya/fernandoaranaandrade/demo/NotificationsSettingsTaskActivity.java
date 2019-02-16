package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskEnum;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class NotificationsSettingsTaskActivity extends AppCompatActivity {

    public static final String TASK = "TASK";

    Button hourButton;
    Switch aSwitch;
    private TaskApp taskApp;
    private Button buttonDateNotification;
    private String estimatedDate;
    private String specifyDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_settings_task);
        estimatedDate = getString(R.string.estimatedDate);
        specifyDate = getString(R.string.specifyDate);
        hourButton = findViewById(R.id.notifacionButton);
        buttonDateNotification = findViewById(R.id.buttonDateNotification);
        aSwitch = findViewById(R.id.switchNotification);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hourButton.setVisibility(View.VISIBLE);
                    buttonDateNotification.setVisibility(View.VISIBLE);
                    if(taskApp.getDateEnd() != null && taskApp.getDateEnd() != 0){
                        Date date = new Date(taskApp.getDateEnd());
                        buttonDateNotification.setText(DateEnum.dateSimpleDateFormat.format(date));
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(taskApp.getDateEnd());
                        if(calendar.get(Calendar.HOUR_OF_DAY) != 0 && calendar.get(Calendar.MINUTE) != 0){
                            hourButton.setText(DateEnum.hourSimpleDateFormat.format(date));
                        }
                    }
                } else {
                    hourButton.setVisibility(View.INVISIBLE);
                    buttonDateNotification.setVisibility(View.INVISIBLE);
                }
            }
        });
        taskApp = (TaskApp) getIntent().getSerializableExtra(TASK);
        if (isActive()) {
            aSwitch.setChecked(true);
            fillHourButton();
            fillDateButton();
        } else {
            aSwitch.setChecked(false);
            hourButton.setVisibility(View.INVISIBLE);
            buttonDateNotification.setVisibility(View.INVISIBLE);
        }
    }

    private void fillDateButton() {
        String dateNotification = taskApp.getDateNotification();
        if (dateNotification == null || !dateNotification.matches(DateEnum.FULL_DATE_REGEX)) {
            buttonDateNotification.setVisibility(View.INVISIBLE);
        } else {
            try {
                String date = DateEnum.dateSimpleDateFormat.format(DateEnum.fullDateSimpleDateFormat.parse(dateNotification));
                buttonDateNotification.setText(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fillHourButton() {
        try {
            String dateNotification = taskApp.getDateNotification();
            if (dateNotification != null) {
                if (dateNotification.matches(DateEnum.FULL_DATE_REGEX)) {
                    Date date = DateEnum.fullDateSimpleDateFormat.parse(dateNotification);
                    dateNotification = DateEnum.hourSimpleDateFormat.format(date);
                }
                hourButton.setText(dateNotification);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean isActive() {
        String active = taskApp.getActiveNotification();
        return active != null && active.equals(SettingsEnum.ON.toString());
    }

    public void set_hour(View view) {
        String textButton = hourButton.getText().toString();
        HourDialogFragment dialogFragment = HourDialogFragment.newInstance(" ", textButton);
        dialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                hourButton.setText(DateEnum.hourSimpleDateFormat.format(calendar.getTime()));
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "fragment_edit_hour");
    }

    public void set_date(View view) {
        String textButton = null;
        if (buttonDateNotification.getText().toString().matches(DateEnum.DATE_REGEX)) {
            textButton = buttonDateNotification.getText().toString();
        }
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(" ", textButton);
        dateDialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                buttonDateNotification.setText(DateEnum.dateSimpleDateFormat.format(calendar.getTime()));
            }
        });
        dateDialogFragment.show(getSupportFragmentManager(), "fragment_edit_date");
    }

    public void save(View view) {
        saveData(view);
    }

    private void saveData(View view) {
        try {
            if (aSwitch.isChecked()) {
                if (hourButton.getText().toString().equalsIgnoreCase(getString(R.string.setHour))) {
                    Snackbar.make(view, getString(R.string.youNeedToSetHour), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }
                if (buttonDateNotification.getText().toString().equalsIgnoreCase(getString(R.string.setDate))) {
                    Snackbar.make(view, getString(R.string.youNeedToSetDate), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }
                taskApp.setActiveNotification(SettingsEnum.ON.toString());
                taskApp.setDateNotification(hourButton.getText().toString());
                Calendar calendarHour = Calendar.getInstance();
                calendarHour.setTime(DateEnum.hourSimpleDateFormat.parse(hourButton.getText().toString()));
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.setTime(DateEnum.dateSimpleDateFormat.parse(buttonDateNotification.getText().toString()));
                Calendar calendarFull = Calendar.getInstance();
                calendarFull.set(Calendar.DAY_OF_MONTH, calendarDate.get(Calendar.DAY_OF_MONTH));
                calendarFull.set(Calendar.MONTH, calendarDate.get(Calendar.MONTH));
                calendarFull.set(Calendar.YEAR, calendarDate.get(Calendar.YEAR));
                calendarFull.set(Calendar.HOUR_OF_DAY, calendarHour.get(Calendar.HOUR_OF_DAY));
                calendarFull.set(Calendar.MINUTE, calendarHour.get(Calendar.MINUTE));
                String dateNotification = DateEnum.fullDateSimpleDateFormat.format(calendarFull.getTime());
                System.out.println(dateNotification);
                taskApp.setDateNotification(dateNotification);
            } else {
                taskApp.setActiveNotification(SettingsEnum.OFF.toString());
                taskApp.setDateNotification(null);
            }
            Intent newIntent = new Intent(this, EditTaskActivity.class);
            newIntent.putExtra(TASK, taskApp);
            Serializable serializable = getIntent().getSerializableExtra(TaskListProyectActivity.FILTER_BEAN);
            newIntent.putExtra(TaskListProyectActivity.FILTER_BEAN, serializable);
            fillIBackIntent(newIntent, getIntent());
            startActivity(newIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillIBackIntent(final Intent newIntent, final Intent currentIntent) {
        Long idTipoCurrentIntent = currentIntent.getLongExtra(TaskEnum.ID_TYPE.toString(), 0);
        String rangoCurrentIntent = currentIntent.getStringExtra(TaskEnum.RANGO_TIEMPO.toString());
        Long idProyectCurrentIntent = currentIntent.getLongExtra(TaskEnum.ID_PROYECT.toString(), 0);
        if (idTipoCurrentIntent != null && idTipoCurrentIntent.longValue() != 0) {
            newIntent.putExtra(TaskEnum.ID_TYPE.toString(), idTipoCurrentIntent);
        }
        if (rangoCurrentIntent != null && !rangoCurrentIntent.isEmpty()) {
            newIntent.putExtra(TaskEnum.RANGO_TIEMPO.toString(), rangoCurrentIntent);
        }
        if (idProyectCurrentIntent != 0) {
            newIntent.putExtra(TaskEnum.ID_PROYECT.toString(), idProyectCurrentIntent);
        }
        String fromActivity = currentIntent.getStringExtra(EditTaskActivity.FROM_ACTIVITY);
        if (fromActivity != null) {
            newIntent.putExtra(EditTaskActivity.FROM_ACTIVITY, fromActivity);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                View parentLayout = findViewById(android.R.id.content);
                saveData(parentLayout);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

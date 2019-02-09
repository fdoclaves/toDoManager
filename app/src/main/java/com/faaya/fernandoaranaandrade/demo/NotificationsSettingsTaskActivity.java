package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskEnum;
import com.faaya.fernandoaranaandrade.demo.utils.HourUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class NotificationsSettingsTaskActivity extends AppCompatActivity {

    public static final String TASK = "TASK";

    Button hourButton;
    Switch aSwitch;
    private ImageButton imageButton;
    private TaskApp taskApp;
    private Button buttonDateNotification;
    private Spinner spinnerNotification;
    private String estimatedDate;
    private String specifyDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_settings_task);
        estimatedDate = getString(R.string.estimatedDate);
        specifyDate = getString(R.string.specifyDate);
        spinnerNotification = findViewById(R.id.spinnerNotification);
        imageButton = findViewById(R.id.imageButtonNotificationSave);
        imageButton.setImageResource(R.drawable.ic_keyboard_backspace);
        imageButton.setVisibility(View.INVISIBLE);
        hourButton = findViewById(R.id.notifacionButton);
        buttonDateNotification = findViewById(R.id.buttonDateNotification);
        aSwitch = findViewById(R.id.switchNotification);
        taskApp = (TaskApp) getIntent().getSerializableExtra(TASK);
        final String[] rangeTimeValues = {estimatedDate, getString(R.string.specifyDate)};
        spinnerNotification.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner18, rangeTimeValues));
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (rangeTimeValues[position].equals(estimatedDate)) {
                    buttonDateNotification.setVisibility(View.INVISIBLE);
                } else {
                    buttonDateNotification.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        };
        spinnerNotification.setOnItemSelectedListener(listener);
        fillSwitch();
        fillHourButton();
        fillSpinner(rangeTimeValues);
        fillDateButton();
    }

    private void fillDateButton() {
        String dateNotification = taskApp.getDateNotification();
        if (dateNotification == null || !dateNotification.matches(DateEnum.FULL_DATE_REGEX)) {
            buttonDateNotification.setVisibility(View.INVISIBLE);
        } else {
            try{
                String date = DateEnum.dateSimpleDateFormat.format(DateEnum.fullDateSimpleDateFormat.parse(dateNotification));
                buttonDateNotification.setText(date);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void fillSpinner(String[] rangeTimeValues) {
        if (taskApp.getDateNotification() != null && taskApp.getDateNotification().matches(DateEnum.FULL_DATE_REGEX)) {
            for (int i = 0; i < rangeTimeValues.length; i++) {
                if (rangeTimeValues[i].equals(specifyDate)) {
                    spinnerNotification.setSelection(i);
                }
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
        if (hourButton.getText().toString().matches(DateEnum.HOUR_REGEX)) {
            textButton = hourButton.getText().toString();
        }
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
        String selectedItem = (String) spinnerNotification.getSelectedItem();
        if (selectedItem.equals(specifyDate) && !buttonDateNotification.getText().toString().matches(DateEnum.DATE_REGEX)) {
            Snackbar.make(view, getString(R.string.youNeedToSetDate), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        if (aSwitch.isChecked()) {
            taskApp.setActiveNotification(SettingsEnum.ON.toString());
        } else {
            taskApp.setActiveNotification(SettingsEnum.OFF.toString());
        }
        if (selectedItem.equals(estimatedDate)) {
            taskApp.setDateNotification(hourButton.getText().toString());
        }
        if(selectedItem.equals(specifyDate)){
            try {
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
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(TASK, taskApp);
        fillIBackIntent(intent, getIntent());
        startActivity(intent);
        finish();
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

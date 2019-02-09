package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsSettingsActivity extends AppCompatActivity {

    public static final String REGEX = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9] ((AM)|(PM))$$";
    public static final String HORAS = "H";
    public static final String MINUTOS = "M";
    public static final String _5MINUTOS = "5M";
    public static final String _15MINUTOS = "15M";
    public static final String _30MINUTOS = "30M";
    public static final String _1HORA = "1H";
    public static final String _24HORAS = "24H";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

    private Button hourButton;
    private Switch aSwitch;
    private Queries queries;
    private Spinner tiempoSpinner;
    Map<String, String> map = new HashMap<>();
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add(getString(R.string._5minutos));
        list.add(getString(R.string._15minutos));
        list.add(getString(R.string._30minutos));
        list.add(getString(R.string._1Hora));
        list.add(getString(R.string._24Horas));
        map.put(list.get(0),_5MINUTOS);
        map.put(list.get(1),_15MINUTOS);
        map.put(list.get(2),_30MINUTOS);
        map.put(list.get(3),_1HORA);
        map.put(list.get(4), _24HORAS);
        setContentView(R.layout.activity_notifications_settings);
        hourButton = findViewById(R.id.notifacionButtonGeneral);
        queries = new Queries(this);
        aSwitch = findViewById(R.id.switchNotificationGeneral);
        String activeNotification = queries.getValueByProperty(SettingsEnum.ACTIVE_NOTIFICTION);
        aSwitch.setChecked(activeNotification.equals(SettingsEnum.ON.toString()));
        String hourNotification = queries.getValueByProperty(SettingsEnum.DATE_NOTIFICATION);
        hourButton.setText(hourNotification);
        tiempoSpinner = findViewById(R.id.spinnerHorasMinutosGeneral);
        tiempoSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner18, list));
        fillSnooze();
    }

    private void fillSnooze() {
        String timeSnooze = queries.getValueByProperty(SettingsEnum.TIME_SNOOZE);
        for (Map.Entry<String, String> pair : map.entrySet()) {
            if(pair.getValue().equals(timeSnooze)){
                for (int i = 0; i < list.size(); i++) {
                   if(list.get(i).equals(pair.getKey())){
                       tiempoSpinner.setSelection(i);
                   }
                }
            }
        }
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
        queries.saveProperty(SettingsEnum.TIME_SNOOZE, map.get(tiempoSpinner.getSelectedItem()));
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}

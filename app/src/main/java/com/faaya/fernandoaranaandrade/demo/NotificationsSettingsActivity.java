package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.NotificationsApp;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsSettingsActivity extends AppCompatActivity {

    public static final String HORAS = "H";
    public static final String MINUTOS = "M";
    public static final String _5MINUTOS = "5M";
    public static final String _15MINUTOS = "15M";
    public static final String _30MINUTOS = "30M";
    public static final String _1HORA = "1H";
    public static final String _24HORAS = "24H";

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
        queries = new Queries(this);
        tiempoSpinner = findViewById(R.id.spinnerHorasMinutosGeneral);
        tiempoSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner18, list));
        fillSnooze();
        fillNextNotification();
    }

    private void fillNextNotification() {
        TextView textViewNextNotifacion = findViewById(R.id.textViewNextNotifacion);
        NotificationsApp firstNotification = queries.getFirstNotification();
        if(firstNotification == null){
            textViewNextNotifacion.setVisibility(View.INVISIBLE);
            TextView textViewNextNotificationTitule = findViewById(R.id.textViewNextNotificationTitule);
            textViewNextNotificationTitule.setVisibility(View.INVISIBLE);
        } else {
            String format = DateEnum.fullDateSimpleDateFormat.format(new Date(firstNotification.getDate()));
            textViewNextNotifacion.setText(format);
        }
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

    public void save(View view) {
        queries.saveProperty(SettingsEnum.TIME_SNOOZE, map.get(tiempoSpinner.getSelectedItem()));
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}

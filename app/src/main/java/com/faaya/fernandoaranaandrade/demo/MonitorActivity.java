package com.faaya.fernandoaranaandrade.demo;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.faaya.fernandoaranaandrade.demo.Beans.NotificationsApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.database.Queries;
import com.faaya.fernandoaranaandrade.demo.notifications.NotificationServiceUpgrade;

import java.util.Date;
import java.util.List;

public class MonitorActivity extends AppCompatActivity {

    private TextView textView1;

    private TextView textView2;

    private TextView textView3;

    private TextView textView4;

    private TextView textView5;

    private Queries queries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        textView1 = findViewById(R.id.textViewInfo1);
        textView2 = findViewById(R.id.textViewInfo2);
        textView3 = findViewById(R.id.textViewInfo3);
        textView4 = findViewById(R.id.textViewInfo4);
        textView5 = findViewById(R.id.textViewInfo5);
        queries = new Queries(this);
        fill();
    }

    private void fill() {
        textView1.setText("Servicio activo:" + isMyServiceRunning(NotificationServiceUpgrade.class));
        List<NotificationsApp> allNotification = queries.getAllNotification();
        textView2.setText("Total de notificaciónes:"+ allNotification.size());
        textView3.setText("Tiempo actual:" + new Date(System.currentTimeMillis()));
        StringBuffer stringBuffer = new StringBuffer("");
        for (NotificationsApp notificationsApp : allNotification) {
            stringBuffer.append(new Date(notificationsApp.getDate()) + " - " + notificationsApp.getIdTask() + "\n");
        }
        if(allNotification.size()==0){
            stringBuffer.append("Sin notificaciones pendientes");
        }
        textView4.setText(stringBuffer.toString());

        StringBuffer stringBuffer1 = new StringBuffer("");
        for (TaskApp taskApp : queries.getAllTask()) {
            stringBuffer1.append(taskApp.getName() + " - " +taskApp.getId() + "\n");
        }
        textView5.setText(stringBuffer1.toString());
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void refresh(View view) {
        fill();
    }
}

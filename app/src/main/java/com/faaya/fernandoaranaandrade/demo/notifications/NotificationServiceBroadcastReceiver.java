package com.faaya.fernandoaranaandrade.demo.notifications;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.faaya.fernandoaranaandrade.demo.Beans.NotificationsApp;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.util.Calendar;

public class NotificationServiceBroadcastReceiver extends BroadcastReceiver {

    public static String SNOOZE = "SNOOZE_ACTION";
    public static String ID_TASK = "ID_TASK";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (snooze(intent)) {
            Long idTask = null;
            try {
                idTask = intent.getLongExtra(ID_TASK, 0);
                Queries queries = new Queries(context);
                queries.insertNotifications(new NotificationsApp(getNewDate(queries), idTask));
            } catch (Exception e) {
                Log.e("RECEIVER", "Error:", e);
            } finally {
                if (idTask != null) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                    manager.cancel(idTask.intValue());
                }
            }
        } else {
            boolean myServiceRunning = isMyServiceRunning(NotificationServiceUpgrade.class, context);
            System.out.println("+++++++++++++++++++" + myServiceRunning + "+++++++++++++++++++++++");
            if (!myServiceRunning) {
                Util.scheduleJob(context);
            }
        }
    }

    private Long getNewDate(Queries queries) {
        Calendar calendar = Calendar.getInstance();
        String timeSnooze = queries.getValueByProperty(SettingsEnum.TIME_SNOOZE);
        int field = Calendar.MINUTE;
        if(timeSnooze.contains("H")){
            field = Calendar.HOUR;
        }
        String stringWait = timeSnooze.replace("M","").replace("H","");
        calendar.add(field, Integer.parseInt(stringWait));
        return calendar.getTimeInMillis();
    }

    private boolean snooze(Intent intent) {
        if (intent == null) {
            return false;
        }
        return intent.getAction().equals(SNOOZE);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
package com.faaya.fernandoaranaandrade.demo.notifications;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.faaya.fernandoaranaandrade.demo.Beans.NotificationsApp;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.R;
import com.faaya.fernandoaranaandrade.demo.database.Queries;
import java.util.Calendar;

public class NotificationServiceBroadcastReceiver extends BroadcastReceiver {

    private static final String NOTIFICATION = "NOTIFICATION";
    public static String SNOOZE = "SNOOZE_ACTION";
    public static String ID_TASK = "ID_TASK";

    @Override
    public void onReceive(Context context, Intent intent) {
        Queries queries = new Queries(context);
        if (snooze(intent)) {
            Long idTask = Long.parseLong(intent.getAction().replace(SNOOZE,""));
            Log.i(NOTIFICATION, "SNOOZE, idTask:" + idTask);
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.cancel(idTask.intValue());
            Long newDate = getNewDate(queries);
            queries.saveUpdateOrDeleteNotifications(true, new NotificationsApp(newDate, idTask));
            Util.scheduleNotification(context, queries, newDate);
        } else {
            String titleAction = context.getString(R.string.snoozeAction);
            searchNotificationToShow(context, queries,titleAction);
            Util.scheduleNotification(context, queries);
        }
    }

    private void searchNotificationToShow(Context context, Queries queries, String titleAction) {
        try {
            for (NotificationsApp notificationsApp : queries.getNotificationToShow(System.currentTimeMillis())) {
                Log.i(NOTIFICATION, "IdTask:" + notificationsApp.getIdTask());
                long idTask = 0l;
                try {
                    idTask = notificationsApp.getIdTask();
                    TaskApp taskApp = queries.getByIdTask(idTask);
                    if(taskApp != null){
                        Proyect proyect = queries.getByIdProyect(taskApp.getProyectId());
                        Util.showNotifications(context, proyect, taskApp, titleAction);
                    }
                } catch (Exception e) {
                    Log.e(NOTIFICATION, "idTaskError:" + idTask, e);
                } finally {
                    queries.deleteNotificationByIdTask(idTask);
                }
            }
        }   catch (Exception e) {
            Log.e(NOTIFICATION, "error:", e);
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
        if (intent == null || intent.getAction() == null) {
            return false;
        }
        return intent.getAction().contains(SNOOZE);
    }
}
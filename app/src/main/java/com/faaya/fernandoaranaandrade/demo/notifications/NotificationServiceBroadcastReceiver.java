package com.faaya.fernandoaranaandrade.demo.notifications;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.NotificationsApp;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.R;
import com.faaya.fernandoaranaandrade.demo.database.Queries;
import java.util.Calendar;
import java.util.Date;

public class NotificationServiceBroadcastReceiver extends BroadcastReceiver {

    private static final String NOTIFICATION = "NOTIFICATION";
    private static String ACTION = "_ACTION";
    public static String SNOOZE = "SNOOZE" + ACTION;
    public static String FINISH = "FINISH" + ACTION;

    @Override
    public void onReceive(Context context, Intent intent) {
        Queries queries = new Queries(context);
        if (action(intent)) {
            if (intent.getAction().contains(SNOOZE)) {
                Long idTask = Long.parseLong(intent.getAction().replace(SNOOZE,""));
                Log.i(NOTIFICATION, "SNOOZE, idTask:" + idTask);
                NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                manager.cancel(idTask.intValue());
                Long newDate = getNewDate(queries);
                queries.saveUpdateOrDeleteNotifications(true, new NotificationsApp(newDate, idTask));
                Util.scheduleNotification(context, queries, newDate);
                TaskApp taskApp = queries.getByIdTask(idTask);
                String format = DateEnum.fullDateSimpleDateFormat.format(new Date(newDate));
                taskApp.setDateNotification(format);
                queries.saveOrUpdateTaskApp(taskApp);
                Toast.makeText(context, context.getString(R.string.snoozeAction), Toast.LENGTH_SHORT).show();
            }
            if(intent.getAction().contains(FINISH)){
                Long idTask = Long.parseLong(intent.getAction().replace(FINISH,""));
                Log.i(NOTIFICATION, "FINISH, idTask:" + idTask);
                NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                manager.cancel(idTask.intValue());
                TaskApp taskApp = queries.getByIdTask(idTask);
                taskApp.setRealDate(new Date().getTime());
                queries.saveOrUpdateTaskApp(taskApp);
            }
        } else {
            String titleActionSnooze = context.getString(R.string.snoozeAction);
            String titleActionFinish = context.getString(R.string.FINISH);
            searchNotificationToShow(context, queries,titleActionSnooze, titleActionFinish);
            Util.scheduleNotification(context, queries);
        }
    }

    private void searchNotificationToShow(Context context, Queries queries, String titleActionSnooze, String titleActionFinish) {
        try {
            for (NotificationsApp notificationsApp : queries.getNotificationToShow(System.currentTimeMillis())) {
                Log.i(NOTIFICATION, "IdTask:" + notificationsApp.getIdTask());
                long idTask = 0l;
                try {
                    idTask = notificationsApp.getIdTask();
                    TaskApp taskApp = queries.getByIdTask(idTask);
                    if(taskApp != null){
                        Proyect proyect = queries.getByIdProyect(taskApp.getProyectId());
                        Util.showNotifications(context, proyect, taskApp, titleActionSnooze, titleActionFinish);
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

    private boolean action(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return false;
        }
        return intent.getAction().contains(ACTION);
    }
}
package com.faaya.fernandoaranaandrade.demo.notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.EditTaskActivity;
import com.faaya.fernandoaranaandrade.demo.MainActivity;
import com.faaya.fernandoaranaandrade.demo.R;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

public class Util {

    public static final String TO_DO_MANAGER_CHANNEL = "ToDoManager Channel";
    public static String CHANNEL_ID = "CHANNEL_TODOMANAGER";

    public static void showNotifications(Context context, Proyect proyect, TaskApp taskApp, String titleActionSnooze, String titleActionFinish) {
        Long idTask = taskApp.getId();
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra(MainActivity.ID_TASK, taskApp.getId());
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setContentTitle(proyect.getName())
                .setContentText(taskApp.getName())
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.todomanager512)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.todomanager512, titleActionSnooze, getSnoozeIntent(context, idTask))
                .addAction(R.drawable.todomanager512, titleActionFinish, getFinishIntent(context, idTask))
                //.setColor(context.getResources().getColor(R.color.colorYellow))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    TO_DO_MANAGER_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }

        manager.notify(idTask.intValue(), builder.build());
    }

    private static PendingIntent getFinishIntent(Context context, Long idTask) {
        Intent intent = new Intent(context, NotificationServiceBroadcastReceiver.class);
        intent.setAction(NotificationServiceBroadcastReceiver.FINISH + idTask);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private static PendingIntent getSnoozeIntent(Context context, Long idTask) {
        Intent snoozeIntent = new Intent(context, NotificationServiceBroadcastReceiver.class);
        snoozeIntent.setAction(NotificationServiceBroadcastReceiver.SNOOZE + idTask);
        return PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);
    }

    public static void scheduleNotification(Context context, Queries queries, Long alarmTime) {
        Long minorNotificationTime = queries.getMinorNotificationTime();
        if(minorNotificationTime == null || minorNotificationTime > alarmTime){
            sheduleAlarmManager(context, alarmTime);
        } else {
            if(minorNotificationTime > 0){
                sheduleAlarmManager(context, minorNotificationTime);
            }
        }
    }

    public static void scheduleNotification(Context context, Queries queries) {
        Long minorNotificationTime = queries.getMinorNotificationTime();
        if(minorNotificationTime > 0){
            sheduleAlarmManager(context, minorNotificationTime);
        }
    }

    private static void sheduleAlarmManager(Context context, Long minorTime) {
            Intent intent  = new Intent(context, NotificationServiceBroadcastReceiver.class);
            PendingIntent broadcast = PendingIntent.getBroadcast(context, 101, intent,  PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager manager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            manager.setExact(AlarmManager.RTC_WAKEUP, minorTime, broadcast);
    }
}
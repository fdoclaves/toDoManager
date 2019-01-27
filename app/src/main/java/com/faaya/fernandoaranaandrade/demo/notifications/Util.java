package com.faaya.fernandoaranaandrade.demo.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.EditTaskActivity;
import com.faaya.fernandoaranaandrade.demo.MainActivity;
import com.faaya.fernandoaranaandrade.demo.R;

public class Util {

    public static final String TO_DO_MANAGER_CHANNEL = "ToDoManager Channel";
    public static String CHANNEL_ID = "CHANNEL_TODOMANAGER";

    // schedule the start of the service every 10 - 30 seconds
    public static void scheduleJob(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ComponentName serviceComponent = new ComponentName(context, NotificationServiceUpgrade.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
            builder.setMinimumLatency(1 * 1000); // wait at least
            builder.setOverrideDeadline(3 * 1000); // maximum delay
            //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            //builder.setRequiresDeviceIdle(true); // device should be idle
            //builder.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());
        }
    }

    public static void showNotifications(Context context, Proyect proyect, TaskApp taskApp, String title_action) {
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra(MainActivity.ID_TASK, taskApp.getId());
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent snoozeIntent = new Intent(context, NotificationServiceBroadcastReceiver.class);
        snoozeIntent.setAction(NotificationServiceBroadcastReceiver.SNOOZE);
        snoozeIntent.putExtra(NotificationServiceBroadcastReceiver.ID_TASK, taskApp.getId().longValue());
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);

        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setContentTitle(proyect.getName())
                .setContentText(taskApp.getName())
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.todomanager512)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.todomanager512, title_action, snoozePendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    TO_DO_MANAGER_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }

        manager.notify(taskApp.getId().intValue(), builder.build());
    }

}
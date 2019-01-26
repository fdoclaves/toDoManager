package com.faaya.fernandoaranaandrade.demo.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.faaya.fernandoaranaandrade.demo.Beans.NotificationsApp;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.EditTaskActivity;
import com.faaya.fernandoaranaandrade.demo.MainActivity;
import com.faaya.fernandoaranaandrade.demo.R;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationServiceUpgrade extends JobService {
    public static String CHANNEL_ID = "CHANNEL_TODOMANAGER";
    public int counter = 0;
    private Queries queries;

    private Timer timer;
    private TimerTask timerTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        queries = new Queries(this);
        System.out.println("start");
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                work();
            }
        };
        timer.schedule(timerTask, 2 * 60 * 1000L, 1 * 60 * 1000L);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        System.out.println("Job cancelled before being completed.");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        System.out.println("destruido");
        //Intent broadcastIntent = new Intent(this, NotificationServiceReceiver.class);
        //sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    private void work() {
        Log.i("NOTIFICATION", "Time ++++  " + (counter++));
        //System.out.println("allNotifications:" + queries.getAllNotification().size());
        NotificationsApp notificationsApp = queries.getNotificationToShow(System.currentTimeMillis());
        if (notificationsApp != null) {
            Log.i("NOTIFICATION", notificationsApp.getIdTask().toString());
            TaskApp taskApp = queries.getByIdTask(notificationsApp.getIdTask());
            Proyect proyect = queries.getByIdProyect(taskApp.getProyectId());
            showNotifications(taskApp.getName(), proyect.getName(), taskApp.getId());
            queries.deleteNotificationByIdTask(notificationsApp.getIdTask());
        }
    }

    private void showNotifications(final String title, final String text, Long id) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(MainActivity.ID_TASK, id);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.todomanager512)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "ToDoManager Channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }

        manager.notify(id.intValue(), builder.build());
    }

}





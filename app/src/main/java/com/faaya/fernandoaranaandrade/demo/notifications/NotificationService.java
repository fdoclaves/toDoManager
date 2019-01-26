package com.faaya.fernandoaranaandrade.demo.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.faaya.fernandoaranaandrade.demo.Beans.NotificationsApp;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.EditTaskActivity;
import com.faaya.fernandoaranaandrade.demo.MainActivity;
import com.faaya.fernandoaranaandrade.demo.R;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    public static final int MAX_TO_WAIT = 30;
    public int counter = 0;
    private Timer timer;
    private TimerTask timerTask;
    private Queries queries;
    private Integer timeWithoutAsk = 0;

    public NotificationService(Context applicationContext) {
        super();
    }

    public NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        queries = new Queries(this);
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                work();
            }
        };
        timer.schedule(timerTask, 2 * 60 * 1000L, 1 * 60 * 1000L);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stoptimertask();
        Intent broadcastIntent = new Intent(this, NotificationServiceRestarterBroadcastReceiver.class);
        sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    private void work() {
        Log.i("NOTIFICATION", "Time ++++  " + (counter++));
        long time = System.currentTimeMillis();
        if (true || search()) {
            NotificationsApp notificationsApp = queries.getNotificationToShow(time);
            if (notificationsApp != null) {
                TaskApp taskApp = queries.getByIdTask(notificationsApp.getIdTask());
                Proyect proyect = queries.getByIdProyect(taskApp.getProyectId());
                showNotifications(taskApp.getName(), proyect.getName(), taskApp.getId());
                queries.deleteNotificationByIdTask(notificationsApp.getIdTask());
            }
        }
    }

    private boolean search() {
        if(isSleepTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))){
            if (timeWithoutAsk < 0) {
                timeWithoutAsk = MAX_TO_WAIT;
                return true;
            } else {
                timeWithoutAsk--;
                return false;
            }
        }
        return true;
    }

    private boolean isSleepTime(int hourOfDay) {
        return hourOfDay > 20 || hourOfDay < 6;
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void showNotifications(final String title, final String text, Long id) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(MainActivity.ID_TASK, id);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.todomanager512)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        manager.notify(id.intValue(), builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
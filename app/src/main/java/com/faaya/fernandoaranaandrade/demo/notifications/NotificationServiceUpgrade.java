package com.faaya.fernandoaranaandrade.demo.notifications;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.faaya.fernandoaranaandrade.demo.Beans.NotificationsApp;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.R;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationServiceUpgrade extends JobService {
    public static final String NOTIFICATION = "NOTIFICATION";
    private Queries queries;

    private Timer timer;
    private TimerTask timerTask;
    private String titleAction;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        System.out.println("Start JobService.");
        queries = new Queries(this);
        titleAction = getString(R.string.snoozeAction);
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                work(titleAction);
            }
        };
        timer.schedule(timerTask, 2 * 60 * 1000L, 1 * 60 * 1000L);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        System.out.println("JobService cancelled.");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        System.out.println("JobService destroy.");
        //Intent broadcastIntent = new Intent(this, NotificationServiceBroadcastReceiver.class);
        //sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    private void work(String titleAction) {
        try {
            List<NotificationsApp> notificationsApps = queries.getNotificationToShow(System.currentTimeMillis());
            Log.i(NOTIFICATION, "" + notificationsApps.size());
            for (NotificationsApp notificationsApp : notificationsApps) {
                long idTask = 0l;
                try {
                    idTask = notificationsApp.getIdTask();
                    TaskApp taskApp = queries.getByIdTask(idTask);
                    if(taskApp != null){
                        Proyect proyect = queries.getByIdProyect(taskApp.getProyectId());
                        Util.showNotifications(this, proyect, taskApp, titleAction);
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
    }





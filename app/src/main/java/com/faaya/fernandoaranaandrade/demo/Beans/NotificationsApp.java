package com.faaya.fernandoaranaandrade.demo.Beans;

import android.database.Cursor;

import java.io.Serializable;

public class NotificationsApp implements Serializable {

    private Long date;

    private Long idTask;

    public NotificationsApp(){

    }

    public NotificationsApp(Long date, Long idTask){
        setDate(date);
        setIdTask(idTask);
    }

    public NotificationsApp(Cursor cursor){
        setDate(cursor.getLong(0));
        setIdTask(cursor.getLong(1));
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getIdTask() {
        return idTask;
    }

    public void setIdTask(Long idTask) {
        this.idTask = idTask;
    }
}

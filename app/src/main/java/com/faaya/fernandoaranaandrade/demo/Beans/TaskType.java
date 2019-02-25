package com.faaya.fernandoaranaandrade.demo.Beans;

import android.database.Cursor;

import java.io.Serializable;

public class TaskType implements Serializable {
    public static String ID_TASK_TYPE = "ID_TASK_TYPE";
    private String name;
    private Long id;

    public TaskType() {

    }

    public TaskType(String name) {
        this.name = name;
    }

    public TaskType(Cursor cursor) {
        setId(cursor.getLong(0));
        setName(cursor.getString(1));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        return name;
    }
}

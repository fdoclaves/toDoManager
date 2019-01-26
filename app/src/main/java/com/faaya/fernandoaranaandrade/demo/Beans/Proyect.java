package com.faaya.fernandoaranaandrade.demo.Beans;

import android.database.Cursor;

public class Proyect {

    private Long id;
    private String name;
    private long start;
    private Long time;
    private String range;

    public Proyect(){

    }

    public Proyect(final Cursor cursor) {
        setId(cursor.getLong(0));
        setName(cursor.getString(1));
        setStart(cursor.getLong(2));
        setTime(cursor.getLong(3));
        setRange(cursor.getString(4));
    }

    public Proyect(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String toString() {
        return name;
    }

    }

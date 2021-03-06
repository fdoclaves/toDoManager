package com.faaya.fernandoaranaandrade.demo.Beans;

import android.database.Cursor;

import java.io.Serializable;

public class Proyect implements Serializable {

    private Long id;
    private String name;
    private Long start;
    private Long time;
    private String range;
    private Long endDate;

    public Proyect(){

    }

    public Proyect(final Cursor cursor) {
        setId(cursor.getLong(0));
        setName(cursor.getString(1));
        setStart(cursor.getLong(2));
        setTime(cursor.getLong(3));
        setRange(cursor.getString(4));
        setEndDate(cursor.getLong(5));
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

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
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

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }
}

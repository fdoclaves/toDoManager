package com.faaya.fernandoaranaandrade.demo.Beans;

import android.database.Cursor;

public class SettingsApp {
    private String value;
    private Long id;
    private String key;

    public SettingsApp(Cursor cursor) {
        setId(cursor.getLong(0));
        setKey(cursor.getString(1));
        setValue(cursor.getString(2));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

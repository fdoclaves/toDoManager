package com.faaya.fernandoaranaandrade.demo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.R;

public class DataBase extends SQLiteOpenHelper {

    public static final String ID_TYPE = "ID_TYPE";
    public static final String END_DATE = "END_DATE";
    public static final String REAL_DATE = "REAL_DATE";
    public static final String ID_PROYECT = "ID_PROYECT";
    public static final String COMMENTS = "COMMENTS";
    public static final String TASK_TABLE = "TASKS";
    public static final String SETTINGS_TABLE = "SETTINGS";
    public static String NAME = "NAME";
    public static String START = "START";
    public static String TIME = "TIME";
    public static String RANGE = "RANGE";
    public static String PROYECT_TABLE = "OBRA";
    public static String TASK_TYPE_TABLE = "TASK_TYPE";
    public static String SCHEMA = "FER";
    public static String KEYWORD = "KEYWORD";
    public static String VALUE = "VALUE";
    public static String WHITE_SEMAFORO = "WHITE_SEMAFORO";
    public static String YELLOW_SEMAFORO = "YELLOW_SEMAFORO";
    public static String ORANGE_SEMAFORO = "ORANGE_SEMAFORO";
    public static String RED_SEMAFORO = "RED_SEMAFORO";
    public static String REAL_SEMAFORO = "REAL_SEMAFORO";
    public static String ACTIVE_SEMAFORO = "ACTIVE_SEMAFORO";
    public static String ACTIVE_NOTIFICATION="ACTIVE_NOTIFICATION";
    public static String DATE_NOTIFICATION="DATE_NOTIFICATION";
    public static String NOTIFICATIONS_TABLE = "NOTIFICATIONS";
    public static String ID_TASK = "ID_TASK";

    String proyectTable = "CREATE TABLE OBRA(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, START INT, TIME INT, RANGE TEXT)";

    String taskTable = "CREATE TABLE TASKS(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, ID_TYPE INT, ID_PROYECT INT, END_DATE INT,REAL_DATE INT,COMMENTS TEXT, WHITE_SEMAFORO TEXT, YELLOW_SEMAFORO TEXT, ORANGE_SEMAFORO TEXT,RED_SEMAFORO TEXT, REAL_SEMAFORO TEXT, ACTIVE_SEMAFORO TEXT, ACTIVE_NOTIFICATION TEXT, DATE_NOTIFICATION TEXT)";

    String settingsTable = "CREATE TABLE SETTINGS(ID INTEGER PRIMARY KEY AUTOINCREMENT, KEYWORD TEXT, VALUE TEXT)";

    String taskTypeTable = "CREATE TABLE TASK_TYPE(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)";

    String notificationsTable = "CREATE TABLE NOTIFICATIONS(DATE_NOTIFICATION INT, ID_TASK INT)";
    private String task1;
    private String task2;


    public DataBase(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        task1 = context.getString(R.string.pendientes);
        task2 = context.getString(R.string.activities);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(proyectTable);
        db.execSQL(taskTable);
        db.execSQL(settingsTable);
        db.execSQL(taskTypeTable);
        db.execSQL(notificationsTable);
        db.execSQL("INSERT INTO TASK_TYPE(NAME) VALUES ('" + task1 + "')");
        db.execSQL("INSERT INTO TASK_TYPE(NAME) VALUES ('" + task2 + "')");
        db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.WHITE_SEMAFORO.toString() +"','10-DB')");
        db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.YELLOW_SEMAFORO.toString() +"','5-DB')");
        db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.ORANGE_SEMAFORO.toString() +"','3-DB')");
        db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.RED_SEMAFORO.toString() +"','1-DB')");
        db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.ACTIVE.toString() +"','" + SettingsEnum.ON.toString() +"')");
        db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.REAL_SEMAFORO.toString() +"','" + SettingsEnum.VERDE.toString() +"')");
        db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.ACTIVE_NOTIFICTION.toString() +"','" + SettingsEnum.ON.toString() +"')");
        db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.DATE_NOTIFICATION.toString() +"','09:00 AM')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.ACTIVE_NOTIFICTION.toString() +"','" + SettingsEnum.ON.toString() +"')");
                db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.DATE_NOTIFICATION.toString() +"','09:00 AM')");
                db.execSQL("ALTER TABLE TASKS ADD COLUMN ACTIVE_NOTIFICATION TEXT DEFAULT '" + SettingsEnum.ON.toString() + "'");
                db.execSQL("ALTER TABLE TASKS ADD COLUMN DATE_NOTIFICATION TEXT DEFAULT '09:00 AM'");
                db.execSQL(notificationsTable);
        }
    }
}

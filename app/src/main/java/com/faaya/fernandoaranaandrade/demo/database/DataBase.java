package com.faaya.fernandoaranaandrade.demo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.R;

import java.util.Calendar;

public class DataBase extends SQLiteOpenHelper {

    public static final String ID_TYPE = "ID_TYPE";
    public static final String END_DATE = "END_DATE";
    public static final String REAL_DATE = "REAL_DATE";
    public static final String ID_PROYECT = "ID_PROYECT";
    public static final String COMMENTS = "COMMENTS";
    public static final String TASK_TABLE = "TASKS";
    public static final String SETTINGS_TABLE = "SETTINGS";
    public static final String ID = "ID";
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
    public static String UNFINISH_SEMAFORO = "UNFINISH_SEMAFORO";

    String proyectTable = "CREATE TABLE OBRA(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, START INT, TIME INT, RANGE TEXT, END_DATE INT)";

    String taskTable = "CREATE TABLE TASKS(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, ID_TYPE INT, ID_PROYECT INT, END_DATE INT,REAL_DATE INT,COMMENTS TEXT, WHITE_SEMAFORO TEXT, YELLOW_SEMAFORO TEXT, ORANGE_SEMAFORO TEXT,RED_SEMAFORO TEXT, REAL_SEMAFORO TEXT, ACTIVE_SEMAFORO TEXT, ACTIVE_NOTIFICATION TEXT, DATE_NOTIFICATION TEXT)";

    String settingsTable = "CREATE TABLE SETTINGS(ID INTEGER PRIMARY KEY AUTOINCREMENT, KEYWORD TEXT, VALUE TEXT)";

    String taskTypeTable = "CREATE TABLE TASK_TYPE(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)";

    String notificationsTable = "CREATE TABLE NOTIFICATIONS(DATE_NOTIFICATION INT, ID_TASK INT)";
    private String task1;
    private String task2;
    private String verde;
    private String moths, weeks;
    private String red;

    public DataBase(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        task1 = context.getString(R.string.pendientes);
        task2 = context.getString(R.string.activities);
        verde = context.getString(R.string.green);
        red = context.getString(R.string.red);
        moths = context.getString(R.string.MESES);
        weeks = context.getString(R.string.SEMANAS);
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
        db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.REAL_SEMAFORO.toString() +"','" + verde +"')");
        db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.TIME_SNOOZE.toString() +"','15M')");
        db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.UNFINISH_SEMAFORO.toString() +"','" + red + "')");
        db.execSQL("ALTER TABLE TASKS ADD COLUMN UNFINISH_SEMAFORO TEXT DEFAULT '" + red + "'");
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
            case 2:
                db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.TIME_SNOOZE.toString() +"','15M')");
            case 3:
                db.execSQL("DELETE FROM SETTINGS WHERE KEYWORD = '" + SettingsEnum.ACTIVE_NOTIFICTION.toString() +"'");
                db.execSQL("DELETE FROM SETTINGS WHERE KEYWORD = '" + SettingsEnum.DATE_NOTIFICATION.toString() +"'");
                db.execSQL("ALTER TABLE OBRA ADD COLUMN END_DATE INT");
                changeMonths(db);
            case 4:
                db.execSQL("INSERT INTO SETTINGS(KEYWORD,VALUE) VALUES ('" + SettingsEnum.UNFINISH_SEMAFORO.toString() +"','" + red + "')");
                db.execSQL("ALTER TABLE TASKS ADD COLUMN UNFINISH_SEMAFORO TEXT DEFAULT '" + red + "'");
                changeAlams(db);
        }
    }

    private void changeMonths(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + PROYECT_TABLE, new String[0]);
        if (cursor.moveToFirst()) {
            do {
                Proyect proyect = new Proyect(cursor);
                String range = proyect.getRange();
                if(range.equalsIgnoreCase(moths)){
                    range = weeks;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put(RANGE, range);
                String[] values = new String[1];
                values[0] = proyect.getId().toString();
                db.update(PROYECT_TABLE, contentValues, "ID = ?", values);
            } while (cursor.moveToNext());
        }
    }

    private void changeAlams(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TASK_TABLE, new String[0]);
        if (cursor.moveToFirst()) {
            do {
                TaskApp taskApp = new TaskApp(cursor);
                String dateNotification = null;
                try {
                    Calendar fullCalendar = Calendar.getInstance();
                    fullCalendar.setTimeInMillis(taskApp.getDateEnd());
                    Calendar alarmCalendar = Calendar.getInstance();
                    alarmCalendar.setTime(DateEnum.hourSimpleDateFormat.parse(taskApp.getDateNotification()));
                    fullCalendar.set(Calendar.HOUR_OF_DAY, alarmCalendar.get(Calendar.HOUR_OF_DAY));
                    fullCalendar.set(Calendar.MINUTE, alarmCalendar.get(Calendar.MINUTE));
                    dateNotification = DateEnum.fullDateSimpleDateFormat.format(fullCalendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put(DATE_NOTIFICATION, dateNotification);
                String[] values = new String[1];
                values[0] = taskApp.getId().toString();
                db.update(TASK_TABLE, contentValues, "ID = ?", values);
            } while (cursor.moveToNext());
        }
    }
}

package com.faaya.fernandoaranaandrade.demo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.faaya.fernandoaranaandrade.demo.Beans.NotificationsApp;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsApp;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Queries {

    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public Queries(Context context) {
        this.context = context;
        DataBase dataBase = new DataBase(context, DataBase.SCHEMA, null, 6);
        sqLiteDatabase = dataBase.getWritableDatabase();
    }

    public List<Proyect> getAllProyects() {
        return selectProyect("SELECT * FROM " + DataBase.PROYECT_TABLE);
    }

    @NonNull
    private List<Proyect> selectProyect(String sql, String... args) {
        Cursor cursor = sqLiteDatabase.rawQuery(sql, args);
        final List<Proyect> proyects = new ArrayList<Proyect>();
        if (cursor.moveToFirst()) {
            do {
                proyects.add(new Proyect(cursor));
            } while (cursor.moveToNext());
        }
        return proyects;
    }

    public void saveOrUpdateProyect(Proyect proyect) {
        if (proyect.getId() == null || proyect.getId() == 0) {
            saveProyect(proyect);
        } else {
            updateProyect(proyect);
        }
    }

    public void updateProyect(final Proyect proyect) {
        String[] ids = new String[1];
        ids[0] = proyect.getId().toString();
        sqLiteDatabase.update(DataBase.PROYECT_TABLE, fillProyectContentValues(proyect), "ID = ?", ids);
    }

    @NonNull
    private ContentValues fillProyectContentValues(Proyect proyect) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.NAME, proyect.getName());
        contentValues.put(DataBase.RANGE, proyect.getRange());
        contentValues.put(DataBase.START, proyect.getStart());
        contentValues.put(DataBase.TIME, proyect.getTime());
        contentValues.put(DataBase.END_DATE, proyect.getEndDate());
        return contentValues;
    }

    public void saveProyect(final Proyect proyect) {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.insert(DataBase.PROYECT_TABLE, null, fillProyectContentValues(proyect));
        }
    }

    public Proyect getByIdProyect(Long idProyect) {
        List<Proyect> proyects = selectProyect("SELECT * FROM " + DataBase.PROYECT_TABLE + " WHERE ID = ?", idProyect.toString());
        if (proyects.size() == 0) {
            return null;
        }
        return proyects.get(0);
    }

    public void deleteProyect(Long id) {
        if (id != null) {
            String[] ids = new String[1];
            ids[0] = id.toString();
            sqLiteDatabase.delete(DataBase.PROYECT_TABLE, "id=?", ids);
        }
    }

    public void saveOrUpdateTaskApp(TaskApp taskApp) {
        System.out.println("id" + taskApp.getId());
        if (taskApp.getId() == null || taskApp.getId() == 0) {
            System.out.println("save");
            saveOrUpdate(taskApp);
        } else {
            System.out.println("update");
            updateTask(taskApp);
        }
    }

    private void updateTask(TaskApp taskApp) {
        String[] ids = new String[1];
        ids[0] = taskApp.getId().toString();
        sqLiteDatabase.update(DataBase.TASK_TABLE, getTaskContentValues(taskApp), "ID = ?", ids);
    }

    @NonNull
    private ContentValues getTaskContentValues(TaskApp taskApp) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.ID_TYPE, taskApp.getIdType());
        contentValues.put(DataBase.NAME, taskApp.getName());
        contentValues.put(DataBase.END_DATE, taskApp.getDateEnd());
        contentValues.put(DataBase.REAL_DATE, taskApp.getRealDate());
        contentValues.put(DataBase.ID_PROYECT, taskApp.getProyectId());
        contentValues.put(DataBase.COMMENTS, taskApp.getComments());
        contentValues.put(DataBase.WHITE_SEMAFORO, taskApp.getWhiteSemaforo());
        contentValues.put(DataBase.YELLOW_SEMAFORO, taskApp.getYellowSemaforo());
        contentValues.put(DataBase.ORANGE_SEMAFORO, taskApp.getOrangeSemaforo());
        contentValues.put(DataBase.RED_SEMAFORO, taskApp.getRedSemaforo());
        contentValues.put(DataBase.REAL_SEMAFORO, taskApp.getRealSemaforo());
        contentValues.put(DataBase.ACTIVE_SEMAFORO, taskApp.getActiveSemaforo());
        contentValues.put(DataBase.ACTIVE_NOTIFICATION, taskApp.getActiveNotification());
        contentValues.put(DataBase.DATE_NOTIFICATION, taskApp.getDateNotification());
        contentValues.put(DataBase.UNFINISH_SEMAFORO, taskApp.getUnfinishSemaforo());
        return contentValues;
    }

    private void saveOrUpdate(TaskApp taskApp) {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.insert(DataBase.TASK_TABLE, null, getTaskContentValues(taskApp));
        }
    }

    public TaskApp getByIdTask(Long id) {
        List<TaskApp> taskApps = selectTask("SELECT * FROM " + DataBase.TASK_TABLE + " WHERE ID = ?", id.toString());
        if (taskApps.size() == 0) {
            return null;
        }
        return taskApps.get(0);
    }

    public TaskApp getByIdProyectAndName(Long proyectId, String name) {
        List<TaskApp> taskApps = selectTask("SELECT * FROM " + DataBase.TASK_TABLE + " WHERE ID_PROYECT = ? and NAME = ? order by ID desc", proyectId.toString(), name);
        if (taskApps.size() == 0) {
            return null;
        }
        return taskApps.get(0);
    }

    @NonNull
    private List<TaskApp> selectTask(String sql, String... args) {
        Cursor cursor = sqLiteDatabase.rawQuery(sql, args);
        final List<TaskApp> proyects = new ArrayList<TaskApp>();
        if (cursor.moveToFirst()) {
            do {
                proyects.add(new TaskApp(cursor));
            } while (cursor.moveToNext());
        }
        return proyects;
    }

    public List<TaskApp> getAllTask() {
        return selectTask("SELECT * FROM " + DataBase.TASK_TABLE);
    }

    public List<TaskApp> getAllPendientesTask(Long time, String orderBy) {
        StringBuffer query = new StringBuffer("SELECT * FROM " + DataBase.TASK_TABLE + " WHERE (END_DATE <= ? or END_DATE is null or END_DATE = 0) and (REAL_DATE is null or REAL_DATE = 0) ");
        taskOrderBy(orderBy, query);
        System.out.println(query.toString());
        return selectTask(query.toString(), time.toString());
    }

    public void deleteTask(Long id) {
        if (id != null) {
            String[] ids = new String[1];
            ids[0] = id.toString();
            sqLiteDatabase.delete(DataBase.TASK_TABLE, "id=?", ids);
        }
    }

    public List<TaskApp> selectTaskByIdProyectEndDateAndType(Long idProyect, Long endRangeDateStart, Long endRangeDateFinish, Long idTaskType, String order) {
        TaskType taskType = null;
        if (idTaskType != null) {
            taskType = getTaskTypeById(idTaskType);
        }
        return selectTaskByIdProyectEndDateAndType(idProyect, endRangeDateStart, endRangeDateFinish, taskType, null, order);
    }

    public List<TaskApp> selectTaskByIdProyectEndDateAndType(Long idProyect, Long endRangeDateStart, Long endRangeDateFinish, TaskType taskType, Boolean onlyPendientes, String order) {
        List<TaskType> ids = null;
        if (taskType != null) {
            ids = new ArrayList<>();
            ids.add(taskType);
        }
        return selectTaskByIdProyectEndDateAndType(idProyect, endRangeDateStart, endRangeDateFinish, ids, onlyPendientes, order);
    }

    public List<TaskApp> selectTaskByIdProyectEndDateAndType(Long idProyect, Long endRangeDateStart, Long endRangeDateFinish, List<TaskType> idTaskTypes, Boolean unfinish, String order) {
        StringBuffer query = new StringBuffer("SELECT * FROM " + DataBase.TASK_TABLE);
        List<String> values = new ArrayList<>();
        boolean useFilter = false;
        if (idProyect != null || endRangeDateStart != null || idTaskTypes != null || endRangeDateFinish != null || unfinish != null) {
            query.append(" WHERE ");
        }
        if (idProyect != null) {
            query.append(DataBase.ID_PROYECT + " = ? ");
            values.add(idProyect.toString());
            useFilter = true;
        }
        if (endRangeDateStart != null) {
            if (useFilter) {
                query.append(" AND ");
            }
            query.append(DataBase.END_DATE + " >= ? ");
            values.add(endRangeDateStart.toString());
            useFilter = true;
        }
        if (endRangeDateFinish != null) {
            if (useFilter) {
                query.append(" AND ");
            }
            query.append(DataBase.END_DATE + " <= ? ");
            values.add(endRangeDateFinish.toString());
            useFilter = true;
        }
        if (idTaskTypes != null) {
            if (useFilter) {
                query.append(" AND ");
            }
            query.append(DataBase.ID_TYPE + " in (");
            for (int i = 0; i < idTaskTypes.size(); i++) {
                if (i != 0) {
                    query.append(", ");
                }
                query.append("" + idTaskTypes.get(i).getId());
            }
            query.append(") ");
            useFilter = true;
        }
        if (unfinish != null) {
            if (useFilter) {
                query.append(" AND ");
            }
            if (unfinish) {
                query.append("(" + DataBase.REAL_DATE + " is null or " + DataBase.REAL_DATE + " = 0)");
            } else {
                query.append("(" + DataBase.REAL_DATE + " is not null and " + DataBase.REAL_DATE + " != 0)");
            }
            useFilter = true;
        }
        taskOrderBy(order, query);
        System.out.println(query);
        String[] valueArray = new String[values.size()];
        for (int index = 0; index < values.size(); index++) {
            valueArray[index] = values.get(index);
        }
        return selectTask(query.toString(), valueArray);
    }

    private void taskOrderBy(String order, StringBuffer query) {
        if (order != null && !order.isEmpty()) {
            if (order.equals(context.getString(R.string.creationUp))) {
                query.append("");
            }
            if (order.equals(context.getString(R.string.dateUp))) {
                query.append(" order by " + DataBase.END_DATE);
            }
            if (order.equals(context.getString(R.string.tagUp))) {
                query.append(" order by " + DataBase.ID_TYPE);
            }
            if (order.equals(context.getString(R.string.finish_order))) {
                query.append(" order by " + DataBase.REAL_DATE);
            }
            if (order.equals(context.getString(R.string.creationDown))) {
                query.append(" order by " + DataBase.ID + " desc");
            }
            if (order.equals(context.getString(R.string.dateDown))) {
                query.append(" order by " + DataBase.END_DATE + " desc ");
            }
            if (order.equals(context.getString(R.string.tagDown))) {
                query.append(" order by " + DataBase.ID_TYPE + " desc ");
            }
            if (order.equals(context.getString(R.string.finish_order_Down))) {
                query.append(" order by " + DataBase.REAL_DATE + " desc ");
            }

        }
    }

    public void deleteTasksByIdProyect(Long id) {
        if (id != null) {
            String[] ids = new String[1];
            ids[0] = id.toString();
            sqLiteDatabase.delete(DataBase.TASK_TABLE, TaskEnum.ID_PROYECT.toString() + "=?", ids);
        }
    }

    public List<TaskType> getAllTaskTypes() {
        return selectTaskTypes("SELECT * FROM " + DataBase.TASK_TYPE_TABLE);
    }

    @NonNull
    private List<TaskType> selectTaskTypes(String sql, String... args) {
        Cursor cursor = sqLiteDatabase.rawQuery(sql, args);
        final List<TaskType> proyects = new ArrayList<TaskType>();
        if (cursor.moveToFirst()) {
            do {
                proyects.add(new TaskType(cursor));
            } while (cursor.moveToNext());
        }
        return proyects;
    }

    public TaskType getTaskTypeById(Long idType) {
        List<TaskType> taskTypes = selectTaskTypes("SELECT * FROM " + DataBase.TASK_TYPE_TABLE + " WHERE ID = ?", idType.toString());
        if (taskTypes.size() == 0) {
            return null;
        }
        return taskTypes.get(0);
    }

    public String getValueByProperty(SettingsEnum settingsEnum) {
        return getSettingsAppByProperty(settingsEnum).getValue();
    }

    @Nullable
    private SettingsApp getSettingsAppByProperty(SettingsEnum settingsEnum) {
        List<SettingsApp> settingsApps = selectSettings("SELECT * FROM " + DataBase.SETTINGS_TABLE + " WHERE " + DataBase.KEYWORD + " = ?", settingsEnum.toString());
        if (settingsApps.size() == 0) {
            return null;
        }
        return settingsApps.get(0);
    }

    @NonNull
    private List<SettingsApp> selectSettings(String sql, String... args) {
        Cursor cursor = sqLiteDatabase.rawQuery(sql, args);
        final List<SettingsApp> settingsApps = new ArrayList<SettingsApp>();
        if (cursor.moveToFirst()) {
            do {
                settingsApps.add(new SettingsApp(cursor));
            } while (cursor.moveToNext());
        }
        return settingsApps;
    }

    public void saveProperty(SettingsEnum keyword, String value) {
        if (sqLiteDatabase != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBase.KEYWORD, keyword.toString());
            contentValues.put(DataBase.VALUE, value);
            SettingsApp settingsApp = getSettingsAppByProperty(keyword);
            String[] ids = new String[1];
            ids[0] = settingsApp.getId().toString();
            sqLiteDatabase.update(DataBase.SETTINGS_TABLE, contentValues, "ID = ?", ids);
        }
    }

    public void saveOrUpdate(TaskType taskType) {
        if (sqLiteDatabase != null) {
            if (taskType.getId() == null) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataBase.NAME, taskType.getName());
                saveTaskType(contentValues);
            } else {
                String[] ids = new String[1];
                ids[0] = taskType.getId().toString();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataBase.NAME, taskType.getName());
                sqLiteDatabase.update(DataBase.TASK_TYPE_TABLE, contentValues, "ID = ?", ids);
            }
        }
    }

    private void saveTaskType(ContentValues contentValues) {
        sqLiteDatabase.insert(DataBase.TASK_TYPE_TABLE, null, contentValues);
    }

    public void deleteTaskType(Long id) {
        if (id != null) {
            String[] ids = new String[1];
            ids[0] = id.toString();
            sqLiteDatabase.delete(DataBase.TASK_TYPE_TABLE, "ID = ?", ids);
        }
    }

    public void deleteTaskByTaskType(Long id) {
        String[] ids = new String[1];
        ids[0] = id.toString();
        sqLiteDatabase.delete(DataBase.TASK_TABLE, TaskEnum.ID_TYPE + " = ?", ids);
    }

    public void saveUpdateOrDeleteNotifications(boolean active, NotificationsApp notificationsApp) {
        deleteNotificationByIdTask(notificationsApp.getIdTask());
        if (active && isDateValid(notificationsApp.getDate())) {
            insertNotifications(notificationsApp);
        }
    }

    private boolean isDateValid(final Long time) {
        return new Date(time).after(new Date());
    }

    public void insertNotifications(NotificationsApp notificationsApp) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.DATE_NOTIFICATION, notificationsApp.getDate());
        contentValues.put(DataBase.ID_TASK, notificationsApp.getIdTask());
        sqLiteDatabase.insert(DataBase.NOTIFICATIONS_TABLE, null, contentValues);
    }

    public void updateNotifications(NotificationsApp notificationsApp) {
        String[] ids = new String[1];
        ids[0] = notificationsApp.getIdTask().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.DATE_NOTIFICATION, notificationsApp.getDate());
        contentValues.put(DataBase.ID_TASK, notificationsApp.getIdTask());
        sqLiteDatabase.update(DataBase.NOTIFICATIONS_TABLE, contentValues, "ID_TASK = ?", ids);
    }

    public int deleteNotificationByIdTask(Long idTask) {
        String[] ids = new String[1];
        ids[0] = idTask.toString();
        return sqLiteDatabase.delete(DataBase.NOTIFICATIONS_TABLE, "ID_TASK = ?", ids);
    }

    public List<NotificationsApp> getNotificationToShow(Long time) {
        return selectNotificationsApp("SELECT * FROM " + DataBase.NOTIFICATIONS_TABLE + " WHERE " + DataBase.DATE_NOTIFICATION + " < ?", time.toString());
    }

    public List<NotificationsApp> getAllNotification() {
        return selectNotificationsApp("SELECT * FROM " + DataBase.NOTIFICATIONS_TABLE);
    }

    @NonNull
    private List<NotificationsApp> selectNotificationsApp(String sql, String... args) {
        Cursor cursor = sqLiteDatabase.rawQuery(sql, args);
        final List<NotificationsApp> notificationsApps = new ArrayList<NotificationsApp>();
        if (cursor.moveToFirst()) {
            do {
                notificationsApps.add(new NotificationsApp(cursor));
            } while (cursor.moveToNext());
        }
        return notificationsApps;
    }

    public void deleteNotificationsByIdProyect(Long id) {
        for (TaskApp taskApp : getTaskAppsByIdProyect(id)) {
            deleteNotificationByIdTask(taskApp.getId());
        }
    }

    @NonNull
    private List<TaskApp> getTaskAppsByIdProyect(Long id) {
        return selectTask("SELECT * FROM " + DataBase.TASK_TABLE + " WHERE " + DataBase.ID_PROYECT + " = ?", id.toString());
    }

    public void deleteNotificationByTaskType(Long idTaskType) {
        for (TaskApp taskApp : getTaskAppsByIdType(idTaskType)) {
            deleteNotificationByIdTask(taskApp.getId());
        }
    }

    @NonNull
    private List<TaskApp> getTaskAppsByIdType(Long idTaskType) {
        return selectTask("SELECT * FROM " + DataBase.TASK_TABLE + " WHERE " + DataBase.ID_TYPE + " = ?", idTaskType.toString());
    }

    public int getCountTaskWithoutRealDate(Long id) {
        String[] ids = new String[1];
        ids[0] = id.toString();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(1) FROM " + DataBase.TASK_TABLE + " WHERE " + DataBase.ID_PROYECT + " = ? AND (" + DataBase.REAL_DATE + " IS NULL or " + DataBase.REAL_DATE + " = 0)", ids);
        if (cursor.moveToFirst()) {
            do {
                return cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return 0;
    }

    public Long getMinorNotificationTime() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT MIN(DATE_NOTIFICATION) FROM " + DataBase.NOTIFICATIONS_TABLE, new String[0]);
        if (cursor.moveToFirst()) {
            do {
                return cursor.getLong(0);
            } while (cursor.moveToNext());
        }
        return null;
    }

    public long getCountProyect() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(1) FROM " + DataBase.PROYECT_TABLE, new String[0]);
        if (cursor.moveToFirst()) {
            do {
                return cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return 0;
    }

    public long getCountTypeTask() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(1) FROM " + DataBase.TASK_TYPE_TABLE, new String[0]);
        if (cursor.moveToFirst()) {
            do {
                return cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return 0;
    }

    public Long getCountAllTasksByProyect(Long idProyect) {
        String[] values = new String[1];
        values[0] = idProyect.toString();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(1) FROM " + DataBase.TASK_TABLE + " WHERE ID_PROYECT = ?" , values);
        if (cursor.moveToFirst()) {
            do {
                return cursor.getLong(0);
            } while (cursor.moveToNext());
        }
        return 0l;
    }

    public Integer getCountTaskByTypeTask(Long idType) {
        String[] values = new String[1];
        values[0] = idType.toString();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(1) FROM " + DataBase.TASK_TABLE + " WHERE ID_TYPE = ?", values);
        if (cursor.moveToFirst()) {
            do {
                return cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return 0;
    }
}

package com.faaya.fernandoaranaandrade.demo.Beans;

import android.database.Cursor;

import java.io.Serializable;

public class TaskApp implements Serializable {
    private Long idType;
    private Long proyectId;
    private String name;
    private Long dateEnd;
    private Long realDate;
    private String comments;
    private Long id;
    private String whiteSemaforo;
    private String yellowSemaforo;
    private String orangeSemaforo;
    private String redSemaforo;
    private String activeSemaforo;
    private String realSemaforo;
    private String activeNotification;
    private String dateNotification;

    public TaskApp() {

    }

    public TaskApp(Cursor cursor) {
        setId(cursor.getLong(0));
        setName(cursor.getString(1));
        setIdType(cursor.getLong(2));
        setProyectId(cursor.getLong(3));
        setDateEnd(cursor.getLong(4));
        setRealDate(cursor.getLong(5));
        setComments(cursor.getString(6));
        setWhiteSemaforo(cursor.getString(7));
        setYellowSemaforo(cursor.getString(8));
        setOrangeSemaforo(cursor.getString(9));
        setRedSemaforo(cursor.getString(10));
        setRealSemaforo(cursor.getString(11));
        setActiveSemaforo(cursor.getString(12));
        setActiveNotification(cursor.getString(13));
        setDateNotification(cursor.getString(14));
    }

    public void setProyectId(Long proyectId) {
        this.proyectId = proyectId;
    }

    public Long getProyectId() {
        return proyectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDateEnd(Long dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Long getDateEnd() {
        return dateEnd;
    }

    public void setRealDate(Long realDate) {
        this.realDate = realDate;
    }

    public Long getRealDate() {
        return realDate;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdType() {
        return idType;
    }

    public void setIdType(Long idType) {
        this.idType = idType;
    }

    public String getWhiteSemaforo() {
        return whiteSemaforo;
    }

    public void setWhiteSemaforo(String whiteSemaforo) {
        this.whiteSemaforo = whiteSemaforo;
    }

    public String getYellowSemaforo() {
        return yellowSemaforo;
    }

    public void setYellowSemaforo(String yellowSemaforo) {
        this.yellowSemaforo = yellowSemaforo;
    }

    public String getOrangeSemaforo() {
        return orangeSemaforo;
    }

    public void setOrangeSemaforo(String orangeSemaforo) {
        this.orangeSemaforo = orangeSemaforo;
    }

    public String getRedSemaforo() {
        return redSemaforo;
    }

    public void setRedSemaforo(String redSemaforo) {
        this.redSemaforo = redSemaforo;
    }

    public String getActiveSemaforo() {
        return activeSemaforo;
    }

    public void setActiveSemaforo(String activeSemaforo) {
        this.activeSemaforo = activeSemaforo;
    }

    public String getRealSemaforo() {
        return realSemaforo;
    }

    public void setRealSemaforo(String realSemaforo) {
        this.realSemaforo = realSemaforo;
    }

    public String getActiveNotification() {
        return activeNotification;
    }

    public void setActiveNotification(String activeNotification) {
        this.activeNotification = activeNotification;
    }

    public String getDateNotification() {
        return dateNotification;
    }

    public void setDateNotification(String dateNotification) {
        this.dateNotification = dateNotification;
    }
}

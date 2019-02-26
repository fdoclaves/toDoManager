package com.faaya.fernandoaranaandrade.demo.Beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class AllTareasBean implements Serializable {
    private List<Proyect> allProjects;
    private List<TaskType> allTaskType;
    private List<TaskType> checkedBefore;
    private List<TaskType> checkedCurrent;
    private Calendar endDate;
    private String orderBy;
    private List<Proyect> projectCheckedBefore;
    private List<Proyect> projectsCheckedCurrent;
    private Calendar startDate;
    private String unFinish;

    public AllTareasBean(){

    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String str) {
        this.orderBy = str;
    }

    public void setUnfinish(String str) {
        this.unFinish = str;
    }

    public String getUnfinish() {
        return this.unFinish;
    }

    public void setCheckedBefore(List<TaskType> list) {
        this.checkedBefore = list;
    }

    public List<TaskType> getCheckedBefore() {
        return this.checkedBefore;
    }

    public void setAllTaskType(List<TaskType> list) {
        this.allTaskType = list;
    }

    public List<TaskType> getAllTaskType() {
        return this.allTaskType;
    }

    public void setCheckedCurrent(List<TaskType> list) {
        this.checkedCurrent = list;
    }

    public List<TaskType> getCheckedCurrent() {
        return this.checkedCurrent;
    }

    public List<Proyect> getAllProjects() {
        return this.allProjects;
    }

    public void setAllProjects(List<Proyect> list) {
        this.allProjects = list;
    }

    public List<Proyect> getProjectsCheckedCurrent() {
        return this.projectsCheckedCurrent;
    }

    public void setProjectsCheckedCurrent(List<Proyect> list) {
        this.projectsCheckedCurrent = list;
    }

    public List<Proyect> getProjectCheckedBefore() {
        return this.projectCheckedBefore;
    }

    public void setProjectCheckedBefore(List<Proyect> list) {
        this.projectCheckedBefore = list;
    }

    public void setEndDate(Calendar calendar) {
        this.endDate = calendar;
    }

    public Calendar getEndDate() {
        return this.endDate;
    }

    public void setStartDate(Calendar calendar) {
        this.startDate = calendar;
    }

    public Calendar getStartDate() {
        return this.startDate;
    }
}

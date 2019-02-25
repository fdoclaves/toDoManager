package com.faaya.fernandoaranaandrade.demo.Beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class AllTareasBean implements Serializable {

    private String orderBy;
    private String unFinish;
    private List<TaskType> checkedBefore;
    private List<TaskType> allTaskType;
    private List<TaskType> checkedCurrent;
    private List<Proyect> allProjects;
    private List<Proyect> projectsCheckedCurrent;
    private List<Proyect> projectCheckedBefore;
    private Calendar endDate;
    private Calendar startDate;

    public AllTareasBean(){

    }

    public AllTareasBean(String orderBy){
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setUnfinish(String unFinish) {
        this.unFinish = unFinish;
    }

    public String getUnfinish() {
        return unFinish;
    }

    public void setCheckedBefore(List<TaskType> checkedBefore) {
        this.checkedBefore = checkedBefore;
    }

    public List<TaskType> getCheckedBefore() {
        return checkedBefore;
    }

    public void setAllTaskType(List<TaskType> allTaskType) {
        this.allTaskType = allTaskType;
    }

    public List<TaskType> getAllTaskType() {
        return allTaskType;
    }

    public void setCheckedCurrent(List<TaskType> checkedCurrent) {
        this.checkedCurrent = checkedCurrent;
    }

    public List<TaskType> getCheckedCurrent() {
        return checkedCurrent;
    }

    public List<Proyect> getAllProjects() {
        return allProjects;
    }

    public void setAllProjects(List<Proyect> allProjects) {
        this.allProjects = allProjects;
    }

    public List<Proyect> getProjectsCheckedCurrent() {
        return projectsCheckedCurrent;
    }

    public void setProjectsCheckedCurrent(List<Proyect> projectsCheckedCurrent) {
        this.projectsCheckedCurrent = projectsCheckedCurrent;
    }

    public List<Proyect> getProjectCheckedBefore() {
        return projectCheckedBefore;
    }

    public void setProjectCheckedBefore(List<Proyect> projectCheckedBefore) {
        this.projectCheckedBefore = projectCheckedBefore;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getStartDate() {
        return startDate;
    }
}

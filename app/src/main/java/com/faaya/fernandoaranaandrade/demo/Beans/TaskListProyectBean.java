package com.faaya.fernandoaranaandrade.demo.Beans;

import java.io.Serializable;
import java.util.List;

public class TaskListProyectBean implements Serializable {
    private List<TaskType> checked;
    private Boolean unfishedTask;
    private String orderBy;

    public TaskListProyectBean(){

    }

    public TaskListProyectBean(List<TaskType> checked, Boolean unfishedTask, String orderBy) {
        this.checked = checked;
        this.unfishedTask = unfishedTask;
        this.orderBy = orderBy;
    }

    public List<TaskType> getChecked() {
        return checked;
    }

    public void setChecked(List<TaskType> checked) {
        this.checked = checked;
    }

    public Boolean getUnfishedTask() {
        return unfishedTask;
    }

    public void setUnfishedTask(Boolean unfishedTask) {
        this.unfishedTask = unfishedTask;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}

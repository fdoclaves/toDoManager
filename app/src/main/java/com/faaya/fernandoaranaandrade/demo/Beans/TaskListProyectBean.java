package com.faaya.fernandoaranaandrade.demo.Beans;

import java.io.Serializable;
import java.util.List;

public class TaskListProyectBean implements Serializable {
    private List<TaskType> checked;
    private String comboValue;
    private String orderBy;

    public TaskListProyectBean(){

    }

    public TaskListProyectBean(List<TaskType> checked, String comboValue, String orderBy) {
        this.checked = checked;
        this.comboValue = comboValue;
        this.orderBy = orderBy;
    }

    public List<TaskType> getChecked() {
        return checked;
    }

    public void setChecked(List<TaskType> checked) {
        this.checked = checked;
    }

    public String getUnfishedTask() {
        return comboValue;
    }

    public void setUnfishedTask(String comboValue) {
        this.comboValue = comboValue;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}

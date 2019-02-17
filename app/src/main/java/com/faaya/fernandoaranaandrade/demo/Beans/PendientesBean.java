package com.faaya.fernandoaranaandrade.demo.Beans;

import java.io.Serializable;
import java.util.List;

public class PendientesBean implements Serializable {
    private Boolean today;
    private String orderBy;

    public PendientesBean(){

    }

    public PendientesBean(Boolean today, String orderBy) {
        this.today = today;
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Boolean getToday() {
        return today;
    }

    public void setToday(Boolean today) {
        this.today = today;
    }
}

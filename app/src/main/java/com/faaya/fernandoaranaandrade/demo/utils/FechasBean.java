package com.faaya.fernandoaranaandrade.demo.utils;

import java.util.Calendar;

public class FechasBean {
    private String endDate;
    private String resultaTimeString;
    private Calendar endCalendar;

    public FechasBean(String endDate, String resultaTimeString, Calendar endCalendar) {
        this.endDate = endDate;
        this.resultaTimeString = resultaTimeString;
        this.endCalendar = endCalendar;
    }

    public String getEndDateText() {
        return endDate;
    }

    public String getResultaTimeString() {
        return resultaTimeString;
    }

    public Calendar getEndCalendar() {
        return endCalendar;
    }


}

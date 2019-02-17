package com.faaya.fernandoaranaandrade.demo.utils;

import java.util.Calendar;

public class FechasBean extends Fechas{
    private String endDate;

    public FechasBean(String endDate, String resultaTimeString, Calendar endCalendar) {
        super(resultaTimeString,endCalendar);
        this.endDate = endDate;
    }

    public String getEndDateText() {
        return endDate;
    }
}

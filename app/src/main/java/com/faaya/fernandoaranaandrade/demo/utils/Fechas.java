package com.faaya.fernandoaranaandrade.demo.utils;

import java.util.Calendar;

public abstract class Fechas {

    private String resultaTimeString;
    private Calendar endCalendar;

    public Fechas(String resultaTimeString, Calendar endCalendar) {
        this.resultaTimeString = resultaTimeString;
        this.endCalendar = endCalendar;
    }

    public String getResultaTimeString() {
        return resultaTimeString;
    }

    public Calendar getEndCalendar() {
        return endCalendar;
    }


}

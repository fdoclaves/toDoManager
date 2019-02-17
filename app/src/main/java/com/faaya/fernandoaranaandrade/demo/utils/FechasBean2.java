package com.faaya.fernandoaranaandrade.demo.utils;

import java.util.Calendar;

public class FechasBean2 extends Fechas{
    private Long count;

    public FechasBean2(Long count, String resultaTimeString, Calendar endCalendar) {
        super(resultaTimeString, endCalendar);
        this.count = count;
    }

    public Long getCount() {
        return count;
    }
}

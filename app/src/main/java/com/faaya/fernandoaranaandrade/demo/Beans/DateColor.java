package com.faaya.fernandoaranaandrade.demo.Beans;

import java.util.Calendar;
import java.util.Date;

public class DateColor {

    public static final String BEFORE = "B";
    private int color;
    private String semaforo;
    private final Date endDay;
    private final Date today = new Date();;

    public DateColor(String semaforo, Date endDate, int color) {
        this.color = color;
        this.semaforo = semaforo;
        String[] split = semaforo.split("-");
        int count = Integer.parseInt(split[0]);
        if (isBefore(split)) {
            count = count * -1;
        }
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        if (semaforo.contains("D")) {
            endCalendar.add(Calendar.DATE, count);
        }
        if (semaforo.contains("W")) {
            endCalendar.add(Calendar.DATE, count * 7);
        }
        if (semaforo.contains("M")) {
            endCalendar.add(Calendar.MONTH, count);
        }
        endDay = endCalendar.getTime();
    }

    private boolean isBefore(String[] split) {
        return split[1].contains(BEFORE);
    }

    public boolean isOn() {
        System.out.println(endDay + " VS " + today);
        return endDay.before(today);
    }

    public boolean isOff() {
        return !isOn();
    }

    public int getColor() {
        return color;
    }

    public String getSemaforo() {
        return semaforo;
    }

}

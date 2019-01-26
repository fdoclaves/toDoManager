package com.faaya.fernandoaranaandrade.demo.utils;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FechasUtils {

    public static final String SEMANAS = "semanas";
    public static final String DIAS = "días";
    public static final String MESES = "meses";

    public static final int MILLIS_BY_DAY = 86400000;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static long getDaysBetweenDates(Date startdate, Date enddate)
    {
        long difference =  (startdate.getTime()-enddate.getTime())/ MILLIS_BY_DAY;
        long abs = Math.abs(difference);
        return abs;
    }

    public static FechasBean getFechasBean(Date statEditable, int number, String selectedTimeRange) throws ParseException {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(statEditable);
        endCalendar.setTime(statEditable);
        Long resultTimeLong = Long.parseLong("0");
        String resultaTimeString = "TIEMPO FINALIZADO";
        switch (selectedTimeRange) {
            case MESES:
                endCalendar.add(Calendar.MONTH, number);
                resultTimeLong = FechasUtils.getDaysBetweenDates(new Date(), endCalendar.getTime());
                resultTimeLong = new Double(resultTimeLong / 30).longValue();
                if(resultTimeLong.longValue() == 1){
                    resultaTimeString = "Falta un mes";
                } else {
                    resultaTimeString = "Faltan " + resultTimeLong + " meses";
                }
                break;
            case DIAS:
                endCalendar.add(Calendar.DATE, number);
                resultTimeLong = FechasUtils.getDaysBetweenDates(new Date(), endCalendar.getTime());
                if(resultTimeLong.longValue() == 1){
                    resultaTimeString = "Falta un día";
                } else {
                    resultaTimeString = "Faltan " + resultTimeLong + " días";
                }
                break;
            case SEMANAS:
                endCalendar.add(Calendar.DATE, number * 7);
                resultTimeLong = FechasUtils.getDaysBetweenDates(new Date(), endCalendar.getTime());
                resultTimeLong = new Double(resultTimeLong / 7).longValue();
                if(resultTimeLong.longValue() == 1){
                    resultaTimeString = "Falta una semana";
                } else {
                    resultaTimeString = "Faltan " + resultTimeLong + " semanas";
                }
                break;
        }
        String endDateText = "Fecha final: " + simpleDateFormat.format(endCalendar.getTime());
        return new FechasBean(endDateText, resultaTimeString, endCalendar);
    }
}

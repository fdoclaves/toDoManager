package com.faaya.fernandoaranaandrade.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class FechasUtils {



    public static final int MILLIS_BY_DAY = 86400000;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static long getDaysBetweenDates(Date startdate, Date enddate)
    {
        long difference =  (startdate.getTime()-enddate.getTime())/ MILLIS_BY_DAY;
        long abs = Math.abs(difference);
        return abs;
    }

    public static FechasBean getFechasBean(Date statEditable, int number, String selectedTimeRange, Map<String, String> map) throws ParseException {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(statEditable);
        String resultaTimeString = map.get(StringUtils.tiempo_finalizado);
        if (selectedTimeRange.equals(map.get(StringUtils.DIAS))){
            endCalendar.add(Calendar.DATE, number);
            resultaTimeString = getResultTimeByDays(map, endCalendar);
        }
        if (selectedTimeRange.equals(map.get(StringUtils.SEMANAS))){
            endCalendar.add(Calendar.DATE, number * 7);
            resultaTimeString = getResultTimeByWeeks(map, endCalendar);
        }
        return new FechasBean(simpleDateFormat.format(endCalendar.getTime()), resultaTimeString, endCalendar);
    }

    private static String getResultTimeByWeeks(Map<String, String> map, Calendar endCalendar) {
        String resultaTimeString;
        Long resultTimeLong = FechasUtils.getDaysBetweenDates(new Date(), endCalendar.getTime());
        resultTimeLong = new Double(resultTimeLong / 7).longValue();
        if(resultTimeLong.longValue() == 1){
            resultaTimeString = map.get(StringUtils.faltan_una_semana_);
        } else {
            resultaTimeString = getRealValor(map.get(StringUtils.faltan_)) + resultTimeLong + " " + map.get(StringUtils.semanas_);
        }
        return resultaTimeString;
    }

    private static String getRealValor(String value) {
        if(value.isEmpty()){
            return "";
        }
        return value + " ";
    }

    public static FechasBean2 getFechasBean(Date startDate, Date endDate, String selectedTimeRange, Map<String, String> map) {
        String resultaTimeString = map.get(StringUtils.tiempo_finalizado);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        Long count = FechasUtils.getDaysBetweenDates(startDate, endDate);
        if (selectedTimeRange.equals(map.get(StringUtils.DIAS))){
            resultaTimeString = getResultTimeByDays(map, endCalendar);
        }
        if (selectedTimeRange.equals(map.get(StringUtils.SEMANAS))){
            resultaTimeString = getResultTimeByWeeks(map, endCalendar);
            count = new Double(count / 7).longValue();
        }
        return new FechasBean2(count, resultaTimeString, endCalendar);
    }

    private static String getResultTimeByDays(Map<String, String> map, Calendar endCalendar) {
        String resultaTimeString;
        Long resultTimeLong = FechasUtils.getDaysBetweenDates(new Date(), endCalendar.getTime());
        if(resultTimeLong.longValue() == 1){
            resultaTimeString = map.get(StringUtils.falta_un_dia_);
        } else {
            resultaTimeString = getRealValor(map.get(StringUtils.faltan_)) + resultTimeLong + " " + map.get(StringUtils.dias_);
        }
        return resultaTimeString;
    }
}

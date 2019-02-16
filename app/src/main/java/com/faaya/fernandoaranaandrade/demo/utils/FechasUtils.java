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
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(statEditable);
        endCalendar.setTime(statEditable);
        Long resultTimeLong = Long.parseLong("0");
        String resultaTimeString = map.get(StringUtils.tiempo_finalizado);
        if(selectedTimeRange.equals(map.get(StringUtils.MESES))){
            endCalendar.add(Calendar.MONTH, number);
            resultTimeLong = FechasUtils.getDaysBetweenDates(new Date(), endCalendar.getTime());
            resultTimeLong = new Double(resultTimeLong / 30).longValue();
            if(resultTimeLong.longValue() == 1){
                resultaTimeString = map.get(StringUtils.fata_un_mes_);
            } else {
                resultaTimeString = getRealValor(map.get(StringUtils.faltan_)) + resultTimeLong + " " + map.get(StringUtils.meses_);
            }
        }
        if (selectedTimeRange.equals(map.get(StringUtils.DIAS))){
            endCalendar.add(Calendar.DATE, number);
            resultTimeLong = FechasUtils.getDaysBetweenDates(new Date(), endCalendar.getTime());
            if(resultTimeLong.longValue() == 1){
                resultaTimeString = map.get(StringUtils.falta_un_dia_);
            } else {
                resultaTimeString = getRealValor(map.get(StringUtils.faltan_)) + resultTimeLong + " " + map.get(StringUtils.dias_);
            }
        }
        if (selectedTimeRange.equals(map.get(StringUtils.SEMANAS))){
            endCalendar.add(Calendar.DATE, number * 7);
            resultTimeLong = FechasUtils.getDaysBetweenDates(new Date(), endCalendar.getTime());
            resultTimeLong = new Double(resultTimeLong / 7).longValue();
            if(resultTimeLong.longValue() == 1){
                resultaTimeString = map.get(StringUtils.faltan_una_semana_);
            } else {
                resultaTimeString = getRealValor(map.get(StringUtils.faltan_)) + resultTimeLong + " " + map.get(StringUtils.semanas_);
            }
        }
        String endDateText = map.get(StringUtils.fecha_final_) + simpleDateFormat.format(endCalendar.getTime());
        return new FechasBean(endDateText, resultaTimeString, endCalendar);
    }

    private static String getRealValor(String value) {
        if(value.isEmpty()){
            return "";
        }
        return value + " ";
    }
}

package com.faaya.fernandoaranaandrade.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HourUtils {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
    private static SimpleDateFormat hourAndMinuts = new SimpleDateFormat("HH:mm");

    public static String getTime(String text) {
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(text);
            System.out.println("da" + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hourAndMinuts.format(date);
    }

    public static Calendar setCalendar(String text, Calendar calendar){
        String time = getTime(text);
        String[] split = time.split(":");
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(split[0]));
        calendar.set(Calendar.MINUTE,Integer.parseInt(split[1]));
        return calendar;
    }

}

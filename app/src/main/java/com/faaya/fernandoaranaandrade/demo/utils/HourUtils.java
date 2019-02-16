package com.faaya.fernandoaranaandrade.demo.utils;

import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;

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
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hourAndMinuts.format(date);
    }

    public static Long getCalendar(String text){
        try {
            return DateEnum.fullDateSimpleDateFormat.parse(text).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Calendar.getInstance().getTimeInMillis();
    }

    public static Long getCalendarByHour(String text, Long dateEnd){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateEnd);
        String time = getTime(text);
        String[] split = time.split(":");
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(split[0]));
        calendar.set(Calendar.MINUTE,Integer.parseInt(split[1]));
        return calendar.getTimeInMillis();
    }
}

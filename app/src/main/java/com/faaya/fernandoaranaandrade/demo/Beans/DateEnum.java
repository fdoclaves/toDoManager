package com.faaya.fernandoaranaandrade.demo.Beans;

import java.text.SimpleDateFormat;

public class DateEnum {

    public static final String HOUR_REGEX = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9] ((AM)|(PM))$$";
    public static SimpleDateFormat hourSimpleDateFormat = new SimpleDateFormat("hh:mm a");
    public static final String DATE_REGEX = "^([0-2][0-9]|3[0-1])\\/(0[0-9]|1[0-2])\\/([0-9][0-9])?[0-9][0-9]$";
    public static SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static final String FULL_DATE_REGEX = "^([0-2][0-9]|3[0-1])\\/(0[0-9]|1[0-2])\\/([0-9][0-9])?[0-9][0-9] ([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9] ((AM)|(PM))$$";
    public static SimpleDateFormat fullDateSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

}

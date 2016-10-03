package com.first.michaelclarke.qudue.JavaProcessing;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by michaelclarke on 02/09/2016.
 */
public class CreateDateObject {

    public static Date create(String date){

        // split the date string
        String[] splitDate = date.split("-");

        // assign each substring
        int day = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);
        // System.out.println("Day : " + day+ "\nMonth : " +month+ "\nYear : "+year);

        // create instance of Calendar
        Calendar cal = Calendar.getInstance();
        // Set year, month and day from substrings
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        // Set due date as the calendar date
        Date newDateObj = cal.getTime();

        return newDateObj;

    }

}

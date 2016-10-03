package com.first.michaelclarke.qudue.JavaProcessing;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by michaelclarke on 16/08/2016.
 */
public class FieldValidation {

    public FieldValidation() {
    }

    /**
     * Checks if any of the text fields are empty
     *
     * @param fields - List of textFields
     * @return true if any of the fields are empty
     */
    public boolean emptyFields(List<String> fields) {

        for (int count = 0; count < fields.size(); count++) {

            if (fields.get(count).equals("")) {
                return true;
            }

        }

        return false;
    }



    public boolean passwordCheck(String password) {

        if (password.length() < 6 || password.length() > 12) {
            return false;
        }

        Pattern pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        if (matcher.find()) {
            return false;
        }

        pattern = Pattern.compile("[\t\n\b\r\f]", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(password);
        if (matcher.find()) {
            return false;
        }

        return true;

    }

    public boolean dateCheck(String startDate, String endDate){

        Date sDate = formatDate(startDate);
        Date eDate = formatDate(endDate);

        System.out.println("sDate : " + sDate);
        System.out.println("eDate : " + eDate);

        // Get the current date
        Date currentDate = new Date();
        System.out.println("cDate : " + currentDate);

        // Work out the difference between dates
        int endDateCheck = (int) ((eDate.getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24));
        int diffInDays = (int) ((eDate.getTime() - sDate.getTime()) / (1000 * 60 * 60 * 24));

        System.out.println("endDateCheck : " + endDateCheck);
        System.out.println("diffInDays : " + diffInDays);

        if (endDateCheck < 1 || diffInDays < 1){
            return false;
        }

        return true;
    }

    private Date formatDate(String date){

        String[] temp = date.split("\\s");

        String formattedDate = temp[1] + temp[2] + temp[3];

        // split the date string
        String[] splitDate = formattedDate.split("-");

        // assign each substring
        int day = Integer.parseInt(splitDate[2]);
        int month = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[0]);

        // create instance of Calendar
        Calendar cal = Calendar.getInstance();
        // Set year, month and day from substrings
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        // Set due date as the calendar date
        Date newDate = cal.getTime();

        return newDate;
    }
}
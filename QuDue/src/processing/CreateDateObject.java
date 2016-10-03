package processing;

import java.util.Calendar;
import java.util.Date;


public class CreateDateObject {

	/**
	 * Takes a String in the format "day-month-year" 
	 * and converts it to a java.util.Date object
	 */
    public static Date create(String date){

        // split the date string
        String[] splitDate = date.split("-");

        // assign each substring
        int day = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);

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

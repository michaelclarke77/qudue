package com.first.michaelclarke.qudue.JavaProcessing;

import java.util.Calendar;
import java.util.Date;

/**
 * Class to compare current date and essay due date.
 * If difference is 10 or less days deadline warning is set to true.
 * @author michaelclarke
 *
 */
public class DeadlineWarning {

	public static int diffInDays = 0;
	public static boolean missedDeadline;

	public static boolean getWarning(String date) {

        System.out.println("SET DEADLINE DATE : " + date);

		// split the date string
		String[] splitDate = date.split("-");

		// assign each substring
		int day = Integer.parseInt(splitDate[0]);
		int month = Integer.parseInt(splitDate[1])-1;
		int year = Integer.parseInt(splitDate[2]);
		// System.out.println("Day : " + day+ "\nMonth : " +month+ "\nYear : "+year);

		// create instance of Calendar
		Calendar cal = Calendar.getInstance();
		// Set year, month and day from substrings
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		// Set due date as the calendar date
		Date dueDate = cal.getTime();

		// Get the current date
		Date currentDate = new Date();

        System.out.println("SET DEADLINE DUE DATE : " +dueDate);
        System.out.println("SET DEADLINE CURRENT DATE : " +currentDate);

		// Work out the difference between dates
		diffInDays = (int) ((dueDate.getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24));

        System.out.println("SET DEADLINE CURRENT DAYS : " +diffInDays);

		// If difference less than or equal to 10, deadline warning is true
		if (diffInDays < 10) {	
			if (diffInDays < 0) {
				missedDeadline = true;
			}
			return true;
		} else {
			return false;
		}
	}

}
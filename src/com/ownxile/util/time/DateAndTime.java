package com.ownxile.util.time;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateAndTime {

	static final String[] MONTHS = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };

	public static String getTodaysDate() {
		final Calendar date = new GregorianCalendar();
		return date.get(Calendar.DAY_OF_MONTH) + "."
				+ (date.get(Calendar.MONTH) + 1) + "."
				+ date.get(Calendar.YEAR);
	}

	public static String getYesterdaysDate() {
		final Calendar date = new GregorianCalendar();
		return date.get(Calendar.DAY_OF_MONTH - 1) + "."
				+ (date.get(Calendar.MONTH) + 1) + "."
				+ date.get(Calendar.YEAR);
	}

	public int getDay() {
		return Calendar.DAY_OF_MONTH;
	}

	public String getMonth() {
		return MONTHS[Calendar.MONTH];
	}

	public int getYear() {
		return Calendar.YEAR;
	}

}

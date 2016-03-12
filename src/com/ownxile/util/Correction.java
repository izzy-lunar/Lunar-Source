package com.ownxile.util;

public class Correction {

	public static String getAmountString(int value) {
		if (value >= 0 && value < 10000) {
			return String.valueOf(value);
		} else if (value >= 10000 && value < 10000000) {
			return value / 1000 + "K";
		} else if (value >= 10000000 && value < 999999999) {
			return value / 1000000 + "M";
		} else if (value >= 999999999) {
			return "*";
		} else {
			return "?";
		}
	}

	public static String getFilteredInput(String input) {
		if (input.contains("\r")) {
			input = input.replaceAll("\r", "");
		}

		return input;
	}

	public static String getTimeString(long time) {
		final long convertedTime = System.currentTimeMillis() - time;
		if (convertedTime > 86399999 && convertedTime < 172800000) {
			return convertedTime / 86400000 + " day ago.";
		} else if (convertedTime > 86399999) {
			return convertedTime / 86400000 + " days ago.";
		} else if (convertedTime > 3599999 && convertedTime < 7200000) {
			return convertedTime / 3600000 + " hour ago.";
		} else if (convertedTime > 3599999) {
			return convertedTime / 3600000 + " hours ago.";
		} else if (convertedTime > 59999 && convertedTime < 120000) {
			return convertedTime / 60000 + " minute ago.";
		} else if (convertedTime > 59999) {
			return convertedTime / 60000 + " minutes ago.";
		} else if (convertedTime > 999 && convertedTime < 2000) {
			return convertedTime / 1000 + " second ago";
		} else {
			return convertedTime / 1000 + " seconds ago";
		}
	}

	public static String getTimeString2(long time) {
		final long convertedTime = System.currentTimeMillis() - time;
		if (convertedTime > 86399999 && convertedTime < 172800000) {
			return convertedTime / 86400000 + " day";
		} else if (convertedTime > 86399999) {
			return convertedTime / 86400000 + " days";
		} else if (convertedTime > 3599999 && convertedTime < 7200000) {
			return convertedTime / 3600000 + " hour";
		} else if (convertedTime > 3599999) {
			return convertedTime / 3600000 + " hours";
		} else if (convertedTime > 59999 && convertedTime < 120000) {
			return convertedTime / 60000 + " minute";
		} else if (convertedTime > 59999) {
			return convertedTime / 60000 + " minutes";
		} else if (convertedTime > 999 && convertedTime < 2000) {
			return convertedTime / 1000 + " second";
		} else {
			return convertedTime / 1000 + " seconds";
		}
	}

}

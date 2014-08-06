package com.why.util;

import java.util.Calendar;
import java.util.Date;

public class DateFormatter {

	private static final String yearMarker = "Äê";
	private static final String monthMarker = "ÔÂ";
	private static final String dayMarker = "ÈÕ";
	private static final String splitMarker = " ";
	private static final String timeMarker = ":";

	public static String getFormattedDate() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int ms=calendar.get(Calendar.MILLISECOND);
		StringBuilder sb = new StringBuilder();
		sb.append(year).append(yearMarker).append(month).append(monthMarker)
				.append(day).append(dayMarker).append(splitMarker).append(hour)
				.append(timeMarker).append(minute);
		return sb.toString();
	}


}

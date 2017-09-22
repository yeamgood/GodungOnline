package com.yeamgood.godungonline.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	private DateUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static final String DDMMYYYY = "dd/MM/yyyy";
	
	public static String dateToString(Date date,String format) {
		if(date == null || format == null) {
			return null;
		}
		return new SimpleDateFormat(format).format(date);
	}
	
	public static Date stringToDate(String dateString,String format) throws ParseException {
		if(dateString == null || format == null) {
			return null;
		}
		return new SimpleDateFormat(format).parse(dateString);
	}

	
}

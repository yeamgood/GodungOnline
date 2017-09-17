package com.yeamgood.godungonline.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {
	
	public final static String ddMMyyyy = "dd/MM/yyyy";
	
	public static String dateToString(Date date,String format) {
		if(date == null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	public static Date StringToDate(String dateString,String format) throws ParseException {
		if(StringUtils.isBlank(dateString)) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = sdf.parse(dateString);
		return date;
	}

}

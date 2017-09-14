package com.yeamgood.godungonline.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public final static String ddMMyyyy = "dd/MM/yyyy";
	
	public static String dateToString(Date date,String format) {
		if(date == null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(date);
		return dateStr;
	}

}

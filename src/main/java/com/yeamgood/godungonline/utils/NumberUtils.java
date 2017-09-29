package com.yeamgood.godungonline.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

public class NumberUtils {
	
	private NumberUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static final String EMPLY_DATA = "0.00";
	
	public static BigDecimal stringToBigDecimal(String data) {
		BigDecimal result = new BigDecimal(EMPLY_DATA);
		if(!StringUtils.isBlank(data)) {
			result = new BigDecimal(data.replace(",", ""));
		}
		return result;
	}

	public static String bigDecimalToString(BigDecimal data) {
		String result = EMPLY_DATA;
		if(data != null) {
			DecimalFormat df = new DecimalFormat();
			df.setMinimumFractionDigits(2);
			result = df.format(data);
		}
		return result;
	}
	
	public static String longToString(long data) {
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		return df.format(data);
	}
}

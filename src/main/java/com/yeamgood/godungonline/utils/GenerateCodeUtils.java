package com.yeamgood.godungonline.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class GenerateCodeUtils {
	
	private GenerateCodeUtils() {
		throw new IllegalStateException("Utility class");
	}

	
	private static final String CONCAT = "-";
	private static final String TEXT_PAD = "0";
	private static final String DEFAUT_CODE = "00000";
	private static final int START_ID = 1;
	
	public static final String TYPE_CATEGORY = "CAT";
	public static final String TYPE_BRAND = "BRA";
	public static final String TYPE_MEASURE = "MEA";
	public static final String TYPE_WAREHOUSE = "WAR";
	public static final String TYPE_PRODUCT = "PRO";
	public static final String TYPE_SUPPLIER = "SUP";
	public static final String TYPE_CUSTOMER = "CUS";
	public static final String TYPE_EMPLOYEE = "EMP";
	public static final String TYPE_ROLEGODUNG = "ROL";
	public static final String TYPE_PURCHASE_REQUEST = "PR";
	
	
	public static String generateCode(String title,String maxCode) {
		if(StringUtils.isBlank(title)) {
			throw new IllegalArgumentException("Title don't have null");
		}
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String maxTextId= "";
		if(StringUtils.isBlank(maxCode)) {
			maxTextId = DEFAUT_CODE;
		}else {
			maxTextId = maxCode.substring(maxCode.lastIndexOf(CONCAT)+1);
		}
		
		int maxLenghtId = maxTextId.length();
		int id = Integer.parseInt(maxTextId) + 1;
		if(String.valueOf(id).length() > maxLenghtId) {id = START_ID;}
		return title + yyyyMMdd + CONCAT + StringUtils.leftPad(String.valueOf(id), maxLenghtId, TEXT_PAD);
	}

}

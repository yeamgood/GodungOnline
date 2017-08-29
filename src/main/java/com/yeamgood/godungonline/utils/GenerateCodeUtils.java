package com.yeamgood.godungonline.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class GenerateCodeUtils {
	
	private final static String CONCAT = "-";
	private final static String TEXT_PAD = "0";
	private final static String DEFAUT_CODE = "00000";
	private final static int START_ID = 1;
	
	public final static String TYPE_CATEGORY = "CAT";
	public final static String TYPE_BRAND = "BRA";
	
	public static String generateCode(String title,String maxCode) {
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

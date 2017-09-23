package com.yeamgood.godungonline.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestDateUtils {

	private String format1 = "dd/MM/yyyy";
	private String format2 = "ddMMyyyy";
	private String format3 = "yyyyMMdd";
	private String format4 = "yyyy-MM-dd'T'HH:mm:ss";
	
	private String date1 = "01/01/2017";
	private String date2 = "01/01/2559";
	private String date3 = "01012017";
	private String date4 = "20170101";
	private String date5 = "2001-07-04T12:08:56";
	
	// Date To String
	@Test
	public void testDateToStringCaseDateOne() throws ParseException {
		Date date = new SimpleDateFormat(format1).parse(date1);
		Assert.assertEquals(date1, DateUtils.dateToString(date, format1));
	}
	
	@Test
	public void testDateToStringCaseDateTwo() throws ParseException {
		Date date = new SimpleDateFormat(format1).parse(date2);
		Assert.assertEquals(date2, DateUtils.dateToString(date, format1));
	}
	
	@Test
	public void testDateToStringCaseFormatOne() throws ParseException {
		Date date = new SimpleDateFormat(format2).parse(date3);
		Assert.assertEquals(date3, DateUtils.dateToString(date, format2));
	}
	
	@Test
	public void testDateToStringCaseFormatTwo() throws ParseException {
		Date date = new SimpleDateFormat(format3).parse(date4);
		Assert.assertEquals(date4, DateUtils.dateToString(date, format3));
	}
	
	@Test
	public void testDateToStringCaseFormatThree() throws ParseException {
		Date date = new SimpleDateFormat(format4).parse(date5);
		Assert.assertEquals(date5, DateUtils.dateToString(date, format4));
	}
	
	@Test
	public void testDateToStringCaseDateNull() {
		Assert.assertNull(DateUtils.dateToString(null, format1));
	}
	
	@Test
	public void testDateToStringCaseFormatNull() throws ParseException {
		Date date = new SimpleDateFormat(format1).parse(date1);
		Assert.assertNull(DateUtils.dateToString(date, null));
	}
	
	@Test
	public void testDateToStringCaseDateAndFormatNull() {
		Assert.assertNull(DateUtils.dateToString(null, null));
	}
	
	// String To Date
	@Test
	public void testStringToDateCaseDateOne() throws ParseException {
		Date date = new SimpleDateFormat(format1).parse(date1);
		Assert.assertEquals(date, DateUtils.stringToDate(date1, format1));
	}
	
	@Test
	public void testStringToDateCaseDateTwo() throws ParseException {
		Date date = new SimpleDateFormat(format1).parse(date2);
		Assert.assertEquals(date, DateUtils.stringToDate(date2, format1));
	}

	@Test
	public void testStringToDateCaseDateSringNull() throws ParseException {
		Assert.assertNull(DateUtils.stringToDate(null, format1));
	}
	
	@Test
	public void testStringToDateCaseFormatNull() throws ParseException {
		Assert.assertNull(DateUtils.stringToDate(date1, null));
	}
	
	@Test
	public void testStringToDateCaseStringDateAndFormatNull() throws ParseException {
		Assert.assertNull(DateUtils.stringToDate(null, null));
	}
	
	
	
}

package com.yeamgood.godungonline.utils;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.yeamgood.godungonline.utils.GenerateCodeUtils;

public class TestGenerateCodeUtils {

	
	@Test(expected = IllegalArgumentException.class)
	public void generateCaseTitleAndMaxcodeIsNull() {
		GenerateCodeUtils.generateCode(null, null);
	}
	
	@Test
	public void generateCaseMaxCodeIsNull() {
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		assertEquals("STA" + yyyyMMdd + "-00001", GenerateCodeUtils.generateCode("STA", null));
	}
	
	@Test
	public void generateCaseCode00000() {
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		assertEquals("STA" + yyyyMMdd + "-00001", GenerateCodeUtils.generateCode("STA", "STA" + yyyyMMdd + "-00000"));
	}
	
	@Test
	public void generateCaseCode00001() {
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		assertEquals("STA" + yyyyMMdd + "-00002", GenerateCodeUtils.generateCode("STA", "STA" + yyyyMMdd + "-00001"));
	}
	
	@Test
	public void generateCaseCode99999() {
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		assertEquals("STA" + yyyyMMdd + "-00001", GenerateCodeUtils.generateCode("STA", "STA" + yyyyMMdd + "-99999"));
	}
	
	@Test
	public void generateCaseTitleDiff1() {
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		assertEquals("STAER" + yyyyMMdd + "-00002", GenerateCodeUtils.generateCode("STAER", "STA" + yyyyMMdd + "-00001"));
	}
	
	@Test
	public void generateCaseTitleDiff2() {
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		assertEquals("STA" + yyyyMMdd + "-00002", GenerateCodeUtils.generateCode("STA", "STAYO" + yyyyMMdd + "-00001"));
	}
	
	@Test
	public void generateCaseTitleDiff3() {
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		assertEquals("STAER" + yyyyMMdd + "-00002", GenerateCodeUtils.generateCode("STAER", "STAYO" + yyyyMMdd + "-00001"));
	}
	
	@Test
	public void generateCaseCodeDiff1() {
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		assertEquals("STAER" + yyyyMMdd + "-000002", GenerateCodeUtils.generateCode("STAER", "STA" + yyyyMMdd + "-000001"));
	}
}

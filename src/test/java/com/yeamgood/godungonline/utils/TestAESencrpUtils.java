package com.yeamgood.godungonline.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;

import org.junit.Test;

import com.yeamgood.godungonline.utils.AESencrpUtils;

public class TestAESencrpUtils {

	@Test
	public void testEncruptCaseDataIsNull() throws ParseException {
		assertNull(AESencrpUtils.encrypt(null));
	}
	
	@Test
	public void testEncruptLongCaseDataIsNull() throws ParseException {
		assertNull(AESencrpUtils.encryptLong(null));
	}
	
	@Test
	public void testDecryptCaseDataIsNull() throws ParseException {
		assertNull(AESencrpUtils.decrypt(null));
	}
	
	@Test
	public void testDecryptLongCaseDataIsNull() throws ParseException {
		assertNull(AESencrpUtils.encryptLong(null));
	}
	
	@Test
	public void testEncrupt123() throws ParseException {
		assertNotNull(AESencrpUtils.encrypt("123"));
	}
	
	@Test
	public void testEncruptLong123() throws ParseException {
		assertNotNull(AESencrpUtils.encryptLong(Long.parseLong("123")));
	}
	
	@Test
	public void testEncruptLong999999999999() throws ParseException {
		assertNotNull(AESencrpUtils.encryptLong(Long.parseLong("999999999999")));
	}
	
	@Test
	public void testDecrypt123() throws ParseException {
		assertNull(AESencrpUtils.decrypt("123"));
	}
	
	@Test
	public void testDecryptLong123() throws ParseException {
		assertNull(AESencrpUtils.decryptLong("123"));
	}
	
	
}

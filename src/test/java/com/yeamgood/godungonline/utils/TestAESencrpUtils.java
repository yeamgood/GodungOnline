package com.yeamgood.godungonline.utils;


import org.testng.Assert;
import org.testng.annotations.Test;

public class TestAESencrpUtils {

	@Test
	public void testEncruptCaseDataIsNull() {
		Assert.assertNull(AESencrpUtils.encrypt(null));
	}
	
	@Test
	public void testEncruptLongCaseDataIsNull() {
		Assert.assertNull(AESencrpUtils.encryptLong(null));
	}
	
	@Test
	public void testDecryptCaseDataIsNull() {
		Assert.assertNull(AESencrpUtils.decrypt(null));
	}
	
	@Test
	public void testDecryptLongCaseDataIsNull() {
		Assert.assertNull(AESencrpUtils.encryptLong(null));
	}
	
	@Test
	public void testEncrupt123() {
		Assert.assertNotNull(AESencrpUtils.encrypt("123"));
	}
	
	@Test
	public void testEncruptLong123() {
		Assert.assertNotNull(AESencrpUtils.encryptLong(Long.parseLong("123")));
	}
	
	@Test
	public void testEncruptLong999999999999() {
		Assert.assertNotNull(AESencrpUtils.encryptLong(Long.parseLong("999999999999")));
	}
	
	@Test
	public void testDecrypt123() {
		Assert.assertNull(AESencrpUtils.decrypt("123"));
	}
	
	@Test
	public void testDecryptLong123() {
		Assert.assertNull(AESencrpUtils.decryptLong("123"));
	}
	
	
}

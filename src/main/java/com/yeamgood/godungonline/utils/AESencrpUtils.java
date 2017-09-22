package com.yeamgood.godungonline.utils;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESencrpUtils {
	
	private AESencrpUtils() {
		throw new IllegalStateException("Utility class");
	}

	private static final String ALGO = "AES";
	private static final byte[] KEYVALUE = new byte[] { 'T', 'e', 's', 't', 'S', 'e', 'r', 'v', 'l', 'e', 't', 'Y', 'e', 'a', 'm', '1' };

	public static String encrypt(String data) {
		String encryptedValue;
		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encVal = c.doFinal(data.getBytes());
			encryptedValue = Base64.getUrlEncoder().encodeToString(encVal);
		} catch (Exception e) {
			encryptedValue = null;
		}
		return encryptedValue;
	}

	public static String decrypt(String encryptedData) {
		String decryptedValue;
		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = Base64.getUrlDecoder().decode(encryptedData);
			byte[] decValue = c.doFinal(decordedValue);
			decryptedValue = new String(decValue);
		} catch (Exception e) {
			decryptedValue = null;
		}
		return decryptedValue;	
	}

	public static String encryptLong(Long data) {
		String encryptValue;
		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encVal = c.doFinal(data.toString().getBytes());
			encryptValue = Base64.getUrlEncoder().encodeToString(encVal);
		} catch (Exception e) {
			encryptValue = null;
		}
		return encryptValue;
	}

	public static Long decryptLong(String encryptedData) {
		Long decryptedValue;
		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = Base64.getUrlDecoder().decode(encryptedData);
			byte[] decValue = c.doFinal(decordedValue);
			decryptedValue = Long.valueOf(new String(decValue));
		} catch (Exception e) {
			decryptedValue = null;
		}
		return decryptedValue;
	}
	
	private static Key generateKey() {
		return new SecretKeySpec(KEYVALUE, ALGO);
	}

}

package com.ownxile.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypter {

	private static final String[] INSTANCES = { "MD5", "SHA1" };

	public static String crypt(String message, int i) {
		try {
			MessageDigest md = MessageDigest.getInstance(INSTANCES[i]);
			return hex(md.digest(message.getBytes("CP1252")));
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	public static String hex(byte[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString(array[i] & 0xFF | 0x100)
					.toUpperCase().substring(1, 3));
		}
		return sb.toString();
	}

}

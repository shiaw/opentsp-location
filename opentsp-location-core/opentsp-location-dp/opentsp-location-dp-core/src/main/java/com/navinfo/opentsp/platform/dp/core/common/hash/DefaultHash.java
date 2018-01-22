package com.navinfo.opentsp.platform.dp.core.common.hash;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 提供的默认Hash值算法
 * @author HK
 *
 */
public class DefaultHash implements Hash {

	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	@Override
	public long hash(Object key) {

		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("System doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(key.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("System doesn't support your  EncodingException.");
		}

		byte[] bytes = msgDigest.digest();
		String md5Str = new String(encodeHex(bytes));

		return md5Str.hashCode() & 0xffffffffL;
	}

	public static char[] encodeHex(byte[] data) {

		int l = data.length;
		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

	@Override
	public long hash(byte[] digest, int nTime) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] computeMd5(String k) {
		// TODO Auto-generated method stub
		return null;
	}
}

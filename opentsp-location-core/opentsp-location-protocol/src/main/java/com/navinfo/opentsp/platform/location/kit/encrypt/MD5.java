package com.navinfo.opentsp.platform.location.kit.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	/**
	 * MD5 16或32位加密
	 * 
	 * @param str
	 *            String 需要加密的字符串
	 * @param x
	 *            int 采用多少位的加密方式(16或者32)
	 * 
	 * @return String 加密后的密文
	 */
	public static String encrypt(String message, int x) {
		try {
			// 提供了消息摘要算法,应用MD5加密
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(message.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 机密成32位
			if (x == 32) {
				return buf.toString();
				// 加密成16为
			} else if (x == 16) {
				return buf.toString().substring(8, 24);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}

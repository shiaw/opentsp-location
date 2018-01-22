package com.navinfo.opentsp.platform.da.core.common.hash;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class AlgorithmHash implements Hash{

	public long hash(byte[] digest, int nTime) {
		long rv = ((long) (digest[3 + nTime * 4] & 0xFF) << 24)
				| ((long) (digest[2 + nTime * 4] & 0xFF) << 16)
				| ((long) (digest[1 + nTime * 4] & 0xFF) << 8)
				| (digest[0 + nTime * 4] & 0xFF);

		return rv & 0xffffffffL; /* Truncate to 32-bits */
	}

	/**
	 * Get the md5 of the given key.
	 */
	public byte[] computeMd5(String k) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 not supported", e);
		}
		md5.reset();
		byte[] keyBytes = null;
		try {
			keyBytes = k.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unknown string :" + k, e);
		}

		md5.update(keyBytes);
		return md5.digest();
	}

	@Override
	public long hash(Object key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String computeMd5String(String src, String charset) {
		MessageDigest md;
        try {
            md = MessageDigest.getInstance("md5");
            return bytesToHex(md.digest(src.getBytes(charset)));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
	}
	
	/**
     * 字节数组按字节转换为16进制表达
     *
     * @param bytes 输入字节
     * @return String
     */
    private String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            String tmp = (Integer.toHexString(bytes[i] & 0xFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toLowerCase(Locale.getDefault());
    }
}

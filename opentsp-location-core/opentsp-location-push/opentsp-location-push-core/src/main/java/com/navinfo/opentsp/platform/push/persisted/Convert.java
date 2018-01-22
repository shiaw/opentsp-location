package com.navinfo.opentsp.platform.push.persisted;

/**
 * 进制转换
 * 
 * @author lgw
 * 
 */
public final class Convert {
	private final static byte[] hex = "0123456789ABCDEF".getBytes();

	/**
	 * 字节数组转换到十六进制字符串
	 * 
	 * @param bytes
	 *            {@link Byte}字节数组
	 * @return {@link String} 十六进制字符串
	 */
	public static String bytesToHexString(byte[] bytes) {
		byte[] buff = new byte[2 * bytes.length];
		for (int i = 0, length = bytes.length; i < length; i++) {
			buff[2 * i] = hex[(bytes[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[bytes[i] & 0x0f];
		}
		return new String(buff);
	}

	/**
	 * 十六进制字符串转换到字节数组
	 * 
	 * @param hexstr
	 *            {@link String} 十六进制字符串
	 * @return {@link Byte}[]字节数组
	 */
	public static byte[] hexStringToBytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0, length = b.length; i < length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return b;
	}

	private static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

	/**
	 * 十进制转十六进制
	 * 
	 * @param number
	 *            int 十进制
	 * @param x
	 *            int 位数
	 * @return
	 */
	public static String decimalToHexadecimal(long number, int x) {
		String hex = Long.toHexString(number).toUpperCase();
		return fillZero(hex, x);
	}

	/**
	 * 填充0
	 * 
	 * @param text
	 *            {@link Object} 需要补0的对象
	 * @param length
	 *            {@link Integer} 补0后的长度
	 * @return {@link String}
	 */
	public static String fillZero(Object text, int length) {
		StringBuilder builder = new StringBuilder(length);
		if (text == null) {
			for (int i = 0; i < length; i++)
				builder.append("0");
		} else {
			for (int i = String.valueOf(text).length(); i < length; i++) {
				builder.append("0");
			}
			builder.append(text);
		}
		return builder.toString();
	}

	/**
	 * 整形转字节
	 * 
	 * @param number
	 * @param size
	 * 
	 * @return
	 */
	public static byte[] longTobytes(long number, int size) {
		byte[] b = new byte[size];
		for (int i = 0; i < size; i++) {
			b[i] = (byte) (number >> 8 * (size - i - 1) & 0xFF);
		}
		return b;
	}

	/**
	 * 字节转整形<br>
	 * 最大支持4字节
	 * 
	 * @param bytes
	 * @param size
	 * @return
	 */
	public static long byte2Long(byte[] bytes, int size) {
		long intValue = 0l;
		for (int i = 0; i < bytes.length; i++) {
			intValue |= (long)(bytes[i] & 0xFF) << (8 * (size - i - 1));
		}
		return intValue;
	}
	/**
	 * 按照java字节序处理
	 * @param bytes
	 * @param size
	 * @return
	 */
	public static int byte2Int(byte[] bytes, int size) {
		int intValue = 0;
		for (int i = 0; i < bytes.length; i++) {
			intValue += (bytes[i] & 0xFF) << (8 * (size - i - 1));
		}
		return intValue;
	}
	
	public static void main(String[] args) {
		String ss = "0001";
		byte[] bb = Convert.hexStringToBytes(ss);
		System.err.println(byte2Int(bb, 2));
	}
	
	public static long byte2LongLittleEndian(byte[] bytes) {
		long intValue = 0l;
		for (int i = 0; i < bytes.length; i++) {
			intValue |= (long)(bytes[i] & 0xFF) << (8 * i);
		}
		return intValue;
	}

	public static int byte2IntLittleEndian(byte[] bytes) {
		int intValue = 0;
		for (int i = 0; i < bytes.length; i++) {
			intValue += (bytes[i] & 0xFF) << (8 * i);
		}
		return intValue;
	}

	/**
	 * 整形转字节
	 * 
	 * @param number
	 * @param size
	 * 
	 * @return
	 */
	public static byte[] intTobytes(int number, int size) {
		byte[] b = new byte[size];
		for (int i = 0; i < size; i++) {
			b[i] = (byte) (number >> 8 * (size - i - 1) & 0xFF);
		}
		return b;
	}
	
	/**
	 * 唯一标识转long进行数据存储
	 * 
	 * @param uniqueMark
	 *            {@link String} 终端唯一标识
	 * @return {@link Long}
	 */
	public static long uniqueMarkToLong(String uniqueMark) {
		return Long.parseLong(uniqueMark);
	}

	/**
	 * 终端标识转换成数据包唯一标识
	 * 
	 * @param terminalId
	 * @return
	 */
	public static String terminalIdToUniqueMark(long terminalId) {
		String temp = String.valueOf(terminalId);
		for (int i = temp.length(); i < 12 ; i++) {
			temp = "0" + temp;
		}
		return temp;
	}
	
	/**
	 * 节点标识转数据包唯一标识
	 * @param nodeCode
	 * @return
	 */
	public static String nodeCodeToUniqueMark(int nodeCode){
		return Convert.fillZero(nodeCode, 12);
	}
	
	public static byte[] byteReverse(byte[] b){
		
		byte[] reB= new byte[b.length];
		
		for(int i=b.length-1; i>=0;i--){
			reB[b.length-i-1] = b[i];
		}
		return reB;
	}
}

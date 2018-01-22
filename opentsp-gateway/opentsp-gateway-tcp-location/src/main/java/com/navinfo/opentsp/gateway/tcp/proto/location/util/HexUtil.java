package com.navinfo.opentsp.gateway.tcp.proto.location.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;

/**
 * User: zhanhk
 * Date: 16/7/11
 * Time: 下午5:24
 */
public class HexUtil {

    public static final String charset = "utf-8";
    private static final Logger logger = LoggerFactory.getLogger(HexUtil.class);

    public static String toHex(String source) {
        try {
            return DatatypeConverter.printHexBinary(source.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public static String toHexDigit(int num) {
        return Integer.toHexString(num);
    }


    public static int fromHexDigit(String hex) {
        return Integer.valueOf(hex, 16);
    }

    public static String fromHex(String hex) {
        return new String(DatatypeConverter.parseHexBinary(hex)).trim();
    }

    /**
     * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 计算校验和
     *
     * @param str
     * @return
     */
    public static String getCheckNum(String str) {
        char[] ch = str.toCharArray();
        String[] arr = new String[str.length() / 2];
        int j = 0;
        for (int i = 0; i < ch.length; i++) {
            if ((i != 0 && i % 2 != 0)) {
                arr[j] = String.valueOf(ch[i - 1]) + String.valueOf(ch[i]);
                j++;
            }
        }
        int sum = 0;
        for (String temp : arr) {
            sum += HexUtil.fromHexDigit(temp);
        }
        String result = HexUtil.toHexDigit(sum);
        if (result.length() != 4) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 4 - result.length(); i++) {
                sb.append("0");
            }
            sb.append(result);
            return sb.toString();
        }
        return result;
    }

    /**
     * 将16进制字符串取反后返回16进制字符串
     *
     * @param hexStr
     * @return
     */
    public static String binaryInvert(String hexStr) {
        byte[] _A = HexUtil.hexStringToBytes(hexStr);
        byte[] _AF = new byte[_A.length];
        for (int i = 0; i < _A.length; i++) {
            _AF[i] = (byte) ~_A[i];
        }
        return HexUtil.bytesToHexString(_AF);
    }

    /**
     * 获取4位长度，两个字节的16进制字符串
     *
     * @param num 10进制数值
     * @return 16进制字符串
     */
    public static String getPackageLength(int num) {
        String result = HexUtil.toHexDigit(num);
        if (result.length() != 4) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 4 - result.length(); i++) {
                sb.append("0");
            }
            sb.append(result);
            return sb.toString();
        }
        return result;
    }

    /**
     * 十进制转十六进制
     *
     * @param number int 十进制
     * @param x      int 位数
     * @return
     */
    public static String decimalToHexadecimal(long number, int x) {
        String hex = Long.toHexString(number).toUpperCase();
        return fillZero(hex, x);
    }

    /**
     * 填充0
     *
     * @param text   {@link Object} 需要补0的对象
     * @param length {@link Integer} 补0后的长度
     * @return {@link String}
     */
    public static String fillZero(Object text, int length) {
        StringBuilder builder = new StringBuilder(length);
        if (text == null) {
            for (int i = 0; i < length; i++) {
                builder.append("0");
            }
        } else {
            for (int i = String.valueOf(text).length(); i < length; i++) {
                builder.append("0");
            }
            builder.append(text);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.print(toHex("55AA"));
    }
}

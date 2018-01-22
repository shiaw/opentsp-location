package com.navinfo.opentsp.gateway.tcp.proto.location.util;

import java.io.UnsupportedEncodingException;

public class StringUtils {
    public static String bytesToGbkString(byte[] bytes) {
        try {
            return new String(bytes, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String bytesToUtf8String(byte[] bytes) {
        try {
            return new String(bytes, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 字符串转GBK <br>
     * mixStr2Hex
     *
     * @param str
     * @return
     */
    public static String strToGbk(String str) {
        byte[] bytes = null;
        try {
            bytes = str.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append("0123456789ABCDEF".charAt((bytes[i] & 0xf0) >> 4));
            sb.append("0123456789ABCDEF".charAt((bytes[i] & 0x0f) >> 0));
        }
        return sb.toString();
    }
}

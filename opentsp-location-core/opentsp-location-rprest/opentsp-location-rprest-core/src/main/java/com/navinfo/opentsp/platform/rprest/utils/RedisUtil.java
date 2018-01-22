package com.navinfo.opentsp.platform.rprest.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by hxw on 2017/5/24.
 */
public class RedisUtil {

    public byte[] string2byte(String string) {
        try {
            return string.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public String byte2string(byte[] bytes) {
        try {
            return new String(bytes,"utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}

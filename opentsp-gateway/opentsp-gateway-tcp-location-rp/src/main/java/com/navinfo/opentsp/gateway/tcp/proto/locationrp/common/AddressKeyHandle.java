package com.navinfo.opentsp.gateway.tcp.proto.locationrp.common;

public class AddressKeyHandle {
    /**
     * 组合链路寻址key
     *
     * @param params
     */
    public static String combinationKey(String... params) {
        StringBuffer buffer = new StringBuffer();
        int len = params.length;
        for (int i = 0; i < len - 1; i++) {
            buffer.append(params[i]).append("_");
        }
        buffer.append(params[len - 1]);
        return buffer.toString();
    }

    /**
     * 拆分链路寻址组合key
     *
     * @param key
     * @return String[]
     */
    public static String[] splitKey(String key) {
        String[] addressKey = key.split("_");
        return addressKey;
    }
}

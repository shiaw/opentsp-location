package com.navinfo.opentsp.platform.rprest.utils;

/**
 * 瓦片ID工具类
 */
public class SlippyUtil {
    /**
     * 根据经纬度信息获取瓦片ID
     * @param lat 纬度
     * @param lon 经度
     * @param zoom 缩放级别
     * @return 瓦片ID
     */
    public static String getTileID(final double lat, final double lon,
                                       final int zoom) {
        int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
        int ytile = (int) Math
                .floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1
                        / Math.cos(Math.toRadians(lat)))
                        / Math.PI)
                        / 2 * (1 << zoom));
        if (xtile < 0)
            xtile = 0;
        if (xtile >= (1 << zoom))
            xtile = ((1 << zoom) - 1);
        if (ytile < 0)
            ytile = 0;
        if (ytile >= (1 << zoom))
            ytile = ((1 << zoom) - 1);
        return ("" + zoom + xtile + ytile);
    }
}

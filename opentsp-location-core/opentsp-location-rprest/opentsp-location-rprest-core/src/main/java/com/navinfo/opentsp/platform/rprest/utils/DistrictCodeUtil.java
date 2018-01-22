package com.navinfo.opentsp.platform.rprest.utils;

/**
 * Created by liuwenbin on 2017/5/22.
 * 根据国标码获取相关信息
 */
public class DistrictCodeUtil {
    /**
     * 根据城市/区县编码,获取对应的省份编码
     * @param districtCode 城市/区县编码
     * @return 省份编码
     */
    public static int getProvinceCode(int districtCode){
        return districtCode / 10000 * 10000;
    }

    /**
     * 根据区县编码,获取对应的城市编码
     * @param districtCode 区县编码
     * @return 城市编码
     */
    public static int getCityCode(int districtCode){
        return districtCode / 100 * 100;
    }

    /**
     * 判断是否是省级行政单位
     * @param districtCode 国标码
     */
    public static boolean isProvince(int districtCode){
        return districtCode % 10000 == 0;
    }

    /**
     * 判断是否是城市行政单位
     * @param districtCode 国标码
     * @return
     */
    public static boolean isCity(int districtCode){
        return districtCode % 100 == 0 && districtCode % 10000 != 0;
    }

    /**
     * 判断是否是区县行政单位
     * @param districtCode 国标码
     * @return
     */
    public static boolean isDistrict(int districtCode){
        return districtCode % 100 != 0;
    }
}
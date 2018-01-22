package com.navinfo.opentsp.platform.dsa.utils;

import java.util.Calendar;

public class SegmentUtils {

    /*
     * 计算当前时间节点的段位,实时计算
     *
     * 实时计算以30s为周期，一天的总段位是[0,2880)
     */
    public static int getRTSeg(Calendar cal) {
        long millis = cal.getTimeInMillis();
        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(cal.getTime());
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        long today = todayCal.getTimeInMillis();
        long seg = (millis - today) / (30 * 1000);
        seg = seg - 1;
        if (seg < 0) {
            seg = 2879;
        }
        return (int) seg;
    }

    public static int getSGSegCurrent() {
        Calendar cal = Calendar.getInstance();
        return getSGSeg(cal);
    }

    /**
     * 5分钟分段计算 [0, 287)
     *
     * @param cal
     * @return
     */
    public static int getSGSeg(Calendar cal) {
        long millis = cal.getTimeInMillis();
        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(cal.getTime());
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        long today = todayCal.getTimeInMillis();
        long seg = (millis - today) / (300 * 1000);
        seg = seg - 1;
        if (seg < 0) {
            seg = 287;
        }
        return (int) seg;
    }

    public static void main(String[] args) throws Exception {
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);

        for (int i = 0; i < 288; i++) {
            int sgSeg = getSGSeg(todayCal);
            System.out.println(DateUtils.date2String(todayCal.getTime(), DateUtils.ALL) + ":" + sgSeg);
            todayCal.add(Calendar.MINUTE, 5);

        }

        // todayCal.setTime(DateUtils.string2Date("2016-07-24 23:50:00",
        // DateUtils.ALL));
    }

    /*
     * 通过时间分段获取执行日期终点，起点请在此基础上进行cal.add(Calendar.SECONDS,-30)操作
     */
    public static Calendar getRTEndTime(int segment) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        if (segment == 2879) {
            cal.add(Calendar.DATE, -1);
        }
        if (segment != -1) {
            long mills = (segment + 1) * 30 * 1000 + cal.getTimeInMillis();
            cal.setTimeInMillis(mills);
        }
        return cal;
    }

    public static Calendar getSGEndTime(int segment) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        if (segment == 287) {
            cal.add(Calendar.DATE, -1);
        }
        if (segment != -1) {
            long mills = (segment + 1) * 300 * 1000 + cal.getTimeInMillis();
            cal.setTimeInMillis(mills);
        }
        return cal;
    }

}

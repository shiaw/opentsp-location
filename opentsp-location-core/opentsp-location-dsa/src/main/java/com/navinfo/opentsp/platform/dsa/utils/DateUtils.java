package com.navinfo.opentsp.platform.dsa.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    public static final String GMT = "GMT+00:00";

    public static final String YYYYMMDD = "yyyy-MM-dd";

    public static final String MMDD = "MM-dd";

    public static final String HHMMSS = "HH:mm:ss";

    public static final String HHMM = "HH:mm";

    public static final String ALL = "yyyy-MM-dd HH:mm:ss";

    public static final String ALL2 = "yyyy/MM/dd HH:mm:ss";

    public static final String SUFFIXHHMMSS = " 00:00:00";

    public static final String TIMESTAMP = "yyyyMMddHHmmssSSSS";

    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

    public static final String yyyyMMdd = "yyyyMMdd";

    public static String date2String(Date date, String formatType)
            throws ParseException {
        DateFormat df = new SimpleDateFormat(formatType);
        return df.format(date);
    }

    public static Date string2Date(String date, String formatType)
            throws ParseException {
        DateFormat df = new SimpleDateFormat(formatType);
        // df.setTimeZone(TimeZone.getTimeZone(GMT));
        return df.parse(date);
    }

    public static String long2Datestr(long source, String formatType) {
        DateFormat df = new SimpleDateFormat(formatType);
//		df.setTimeZone(TimeZone.getTimeZone(GMT));
        return df.format(source);
    }

    public static String subDateFormat(String source, Date target)
            throws ParseException {
        DateFormat df = new SimpleDateFormat(HHMMSS);
        long sp = df.parse(source).getTime();
        long dp = df.parse(df.format(target)).getTime();
        long re = Math.abs(dp - sp);
        df.setTimeZone(TimeZone.getTimeZone(GMT));
        return df.format(re);
    }

    // 获取相差天数
    public static long getDay(String end, String begin) {
        DateFormat df = new SimpleDateFormat(YYYYMMDD);
        long days = 0;
        try {
            Date d1 = df.parse(end);
            Date d2 = df.parse(begin);
            long diff = d1.getTime() - d2.getTime();
            days = diff / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 取得当月天数
     */
    public static int getCurrentMonthLastDay(Date date) {
        Calendar a = Calendar.getInstance();
        a.setTime(date);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static boolean isDateBig(Date source, Date target)
            throws ParseException {
        boolean isBig = true;
        SimpleDateFormat sf = new SimpleDateFormat(ALL);
        long sourceFormat = sf.parse(sf.format(source)).getTime();
        long targetFormat = sf.parse(sf.format(target)).getTime();
        if (sourceFormat - targetFormat < 0) {
            isBig = false;
        }
        return isBig;
    }

    public static boolean compare(Date source, Date target) {
        SimpleDateFormat sf = new SimpleDateFormat(YYYYMMDD);
        String sourceFormat = sf.format(source);
        String targetFormat = sf.format(target);
        return sourceFormat.equals(targetFormat);
    }

    public static long compare(String source, String target)
            throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(HHMM);
        long sp = sf.parse(source).getTime();
        long dp = sf.parse(target).getTime();
        return sp - dp;
    }

    public static long compare(String source, Date target)
            throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(HHMMSS);
        long sp = sf.parse(source).getTime();
        long dp = sf.parse(sf.format(target)).getTime();
        return sp - dp;
    }

    public static long compareInAll(String source, String target)
            throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(ALL);
        long sp = sf.parse(source).getTime();
        long dp = sf.parse(target).getTime();
        return sp - dp;
    }

    // 获得now的当月起止日期yyyy-MM-dd HH:mm:dd
    // 由于sql使用 cur>=start and cur < end,故获取的end为下个月第一天
    public static Map<String, Date> getCurrentMonth(Date now, String formatType)
            throws ParseException {
        Map<String, Date> re = new HashMap<String, Date>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        re.put("stime",
                string2Date(date2String(cal.getTime(), formatType), formatType));
        cal.add(Calendar.MONTH, 1);
        re.put("etime",
                string2Date(date2String(cal.getTime(), formatType), formatType));
        return re;
    }

    // 获得now的本周起止日期yyyy-MM-dd HH:mm:dd，周一到周日
    // 由于sql使用 cur>=start and cur < end,故获取的end为下周一
    public static Map<String, Date> getCurrentWeek(Date now, String formatType)
            throws ParseException {
        Map<String, Date> re = new HashMap<String, Date>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        int day = cal.get(Calendar.DAY_OF_WEEK) - 1;// 今天是星期的第几天
        cal.add(Calendar.DATE, 1 - day);
        re.put("stime",
                string2Date(date2String(cal.getTime(), formatType), formatType));
        cal.add(Calendar.DATE, 7);
        re.put("etime",
                string2Date(date2String(cal.getTime(), formatType), formatType));
        return re;
    }

    // 获取now时间上加years年后的format格式日期
    public static Date getIncreYearDate(Date now, int years, String format)
            throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.YEAR, years);
        Date date = string2Date(date2String(cal.getTime(), format), format);
        return date;
    }

    // 获取now时间上加days（可为负数）天后的format格式日期
    public static Date getIncreDate(Date now, int days, String format)
            throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, days);
        Date date = string2Date(date2String(cal.getTime(), format), format);
        return date;
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(compare("08:23:00", new Date()));
        System.out.println(DateUtils.string2Date("2014/12/5 9:1:0",
                DateUtils.ALL2));
    }

    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public static Date getDateFromUK(String date) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
        return sdf1.parse(date);
    }
}

package com.navinfo.opentsp.platform.da.core.persistence.application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils.DateFormat;

public class DateDiffUtil {
    private static Logger logger = LoggerFactory.getLogger(DateDiffUtil.class);

    /**
     * 得到两日期相差几个月
     *
     * @param String
     * @return
     */
    public static long getMonthDiff(String startDate, String endDate) {

        long monthday;
        Date startDate1 = DateUtils.parse(startDate, DateFormat.YYYY_MM_DD);
        Calendar starCal = Calendar.getInstance();
        starCal.setTime(startDate1);
        int sYear = starCal.get(Calendar.YEAR);
        int sMonth = starCal.get(Calendar.MONTH);
        int sDay = starCal.get(Calendar.DAY_OF_MONTH);
        Date endDate1 = DateUtils.parse(endDate, DateFormat.YYYY_MM_DD);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate1);
        int eYear = endCal.get(Calendar.YEAR);
        int eMonth = endCal.get(Calendar.MONTH);
        int eDay = endCal.get(Calendar.DAY_OF_MONTH);
        monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));
        // 这里计算零头的情况，根据实际确定是否要加1 还是要减1
        // if (sDay < eDay) {
        // monthday = monthday - 1;
        //
        // }
        return monthday;

    }

    public static List<String> getMonthBetween(String minDate, String maxDate) {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化为年月
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyMM");// 格式化为年月
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        try {
            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf1.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }

    /**
     * Get the Dates between Start Date and End Date.
     *
     * @param p_start Start Date
     * @param p_end   End Date
     * @return Dates List
     */
    public static List<Date> getDates(Calendar p_start, Calendar p_end) {
        List<Date> result = new ArrayList<Date>();
        Calendar temp = p_start;
        while (temp.before(p_end)) {
            result.add(temp.getTime());
            temp.add(Calendar.DAY_OF_YEAR, 1);
        }
        result.add(p_end.getTime());
        return result;
    }

    void test() {
        DateDiffUtil test = new DateDiffUtil();

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 1);
        List<Date> dates = test.getDates(start, end);
        test.printDate("Start\t", start.getTime());
        for (Date date : dates) {
            test.printDate("-->\t", date);
        }
        test.printDate("End\t", end.getTime());
    }

    // Print the date with the format.
    public void printDate(String p_msg, Date p_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println(p_msg + sdf.format(p_date));
    }


    public static Set<String> getDatesDiff(long start, long end) {
        Calendar startInstance = Calendar.getInstance();
        Calendar endInstance = Calendar.getInstance();
        startInstance.setTimeInMillis(start * 1000);
        endInstance.setTimeInMillis(end * 1000);
        List<Date> dates = getDates(startInstance, endInstance);
        Set<String> dateSet = new HashSet();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 格式化为年月
        if (dates != null) {
            for (Date date : dates) {
                dateSet.add(sdf.format(date));
            }
            return dateSet;
        }
        return null;
    }

    /**
     * 传入yyMM获取当月的第一天的00：00：00
     *
     * @param dayOfMonth
     * @return
     */
    public static long getBeginDayOfMonth(String dayOfMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
        try {
            Date date = sdf.parse(dayOfMonth);
            Calendar calendar = DateUtils.calendar(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            long time = calendar.getTimeInMillis();
            return time / 1000;
        } catch (ParseException e) {
            logger.error("获取当月第一天时间出错。" + dayOfMonth, e);
        }
        return 0l;
    }

    /**
     * 传入yyMM获取当月的最后一天的23：59：59
     *
     * @param dayOfMonth
     * @return
     */
    public static long getEndDayOfMonth(String dayOfMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
        try {
            Date date = sdf.parse(dayOfMonth);
            Calendar calendar = DateUtils.calendar(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            long time = calendar.getTimeInMillis();
            return time / 1000;
        } catch (ParseException e) {
            logger.error("获取当月最后一天时间出错。" + dayOfMonth, e);
        }
        return 0l;
    }

    /**
     * 获取时间段
     *
     * @param beginTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static Map<String, Map<String, Long>> splitDateWithMonth(long beginTime, long endTime) {
        Map<String, Map<String, Long>> result = new HashMap<String, Map<String, Long>>();

        // 日期转换 yyyy-mm-ss
        Date beginDate = new Date(beginTime * 1000);
        Date endDate = new Date(endTime * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String getBeginMonth = formatter.format(beginDate);
        String getendMonth = formatter.format(endDate);
        //计算月份差
        long monthDiff = DateDiffUtil.getMonthDiff(getBeginMonth, getendMonth);
        List<String> monthBetween = DateDiffUtil.getMonthBetween(getBeginMonth, getendMonth);
        if (monthDiff == 0) {

            // 是否是当前月
            Map<String, Long> timeCondition = new HashMap<String, Long>();
            timeCondition.put("beginTime", beginTime);
            timeCondition.put("endTime", endTime);
            result.put(monthBetween.get(0), timeCondition);

        } else {
            for (String month : monthBetween) {
                Map<String, Long> timeCondition = new HashMap<String, Long>();
                String format = (new SimpleDateFormat("yyMM")).format(beginDate);
                if (month.equals(format)) {
                    timeCondition.put("beginTime", beginTime);
                } else {
                    timeCondition.put("beginTime", DateDiffUtil.getMinMonthDate(month));
                }
                format = (new SimpleDateFormat("yyMM")).format(endDate);
                if (month.equals(format)) {
                    timeCondition.put("endTime", endTime);
                } else {
                    timeCondition.put("endTime", DateDiffUtil.getMaxMonthDate(month));
                }

                result.put(month, timeCondition);
            }
        }
        return result;
    }

    /**
     * 获取月份起始日期
     *
     * @param date
     * @return
     * @throws ParseException
     * @throws ParseException
     */
    public static long getMinMonthDate(String date) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(2, 4)) - 1);
        calendar.set(Calendar.YEAR, 2000 + Integer.parseInt(date.substring(0, 2)));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis() / 1000;
    }

    public static long getMaxMonthDate(String date) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(2, 4)));
        calendar.set(Calendar.YEAR, 2000 + Integer.parseInt(date.substring(0, 2)));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis() / 1000 - 1;
    }
}
package com.navinfo.opentsp.platform.rprest.kit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期工具类<br>
 * <h>约定：</h> <li>所有时间单位为秒</li>
 * 
 * @author lgw
 * 
 */
public class DateUtils {
	public static Date parse(long date) {
		return new Date(date * 1000);
	}

	public static Date parse(String dateText, DateFormat dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				dateFormatCache.get(dateFormat.ordinal()));
		if (sdf != null)
			try {
				return sdf.parse(dateText);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public static Calendar calendar(int year, int month, int day) {
		return calendar(year, month, day, 0, 0, 0);
	}

	public static Calendar calendar(int year, int month, int day, int hour, int minute,
			int second) {
		Calendar calendar = Calendar.getInstance();
		if ((hour | minute | second) != 0)
			calendar.set(year, month - 1, day, hour, minute, second);
		else
			calendar.set(year, month - 1, day);
		return calendar;
	}

	/**
	 * 按天拆分 按月拼集合
	 *
	 * @param beginTime
	 * @param endTime
	 * @return Map<YYMM,List<YYYYMMDD>
	 */
	public static Map<String, List<String>> splitWithDays(long beginTime, long endTime) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		String currentDayString = DateUtils.format(beginTime, DateUtils.DateFormat.YYYYMMDD);
		String endDayString = DateUtils.format(endTime, DateUtils.DateFormat.YYYYMMDD);
		List<String> middleMonth = DateUtils.getMiddleMonth(beginTime, endTime);
		for (int i = 0; i < middleMonth.size(); i++) {
			String mon = middleMonth.get(i);
			List<String> days = new ArrayList<String>();
			if (0 != i) {
				currentDayString = mon + "01";
			}
			while (true) {
				days.add(currentDayString);
				if (currentDayString.equals(endDayString)) {
					break;
				} else if (DateUtils.isLastDayOfMonth(DateUtils.parse(currentDayString, DateUtils.DateFormat.YYYYMMDD))) {
					break;
				}
				currentDayString = DateUtils.getNextDay(currentDayString);
			}
			mon = mon.substring(2);
			result.put(mon, days);
		}
		return result;
	}

	public static Calendar calendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	public static Calendar calendar(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time * 1000);
		return calendar;
	}
	public static Calendar current(){
		return Calendar.getInstance();
	}
	/**
	 * 判断是否为同一天，是同一天则返回0，只支持同一年
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static int differDay(Calendar beginTime , Calendar endTime){
		return Math.abs(beginTime.get(Calendar.DAY_OF_YEAR) -endTime.get(Calendar.DAY_OF_YEAR));
	}
	/**
	 * 获取开始和结束日期中间的所有天的起止时间
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @return Long[0]为当天开始时间，Long[1]为当天结束时间
	 */
	public static List<Long[]> getMiddleDays(long begin,long end){
		List<Long[]> result = new ArrayList<Long[]>();
		long next = begin;
		while(true){
			Long[] dayTimes = new Long[2];
			dayTimes[0] = next;
			next = getNextDayBeginTime(next)/1000;
			if(next > end){
				dayTimes[1] = end;
				result.add(dayTimes);
				break;
			}else{
				dayTimes[1] = next-1;
			}
			result.add(dayTimes);
		}
		return result;
	}
	/**
	 * 获取下一天的开始时间
	 * @param time 当天的任何时间
	 * @return
	 */
	public static long getNextDayBeginTime(long time) {
		Date date = DateUtils.parse(time);
		Calendar calendar = DateUtils.calendar(date);
		calendar.set(Calendar.DAY_OF_YEAR,
				calendar.get(Calendar.DAY_OF_YEAR) + 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long result = calendar.getTime().getTime();
		return result;
	}
	public static void main(String[] args) {
//		Calendar beginTime = DateUtils.calendar(2013, 3, 10);
//		Calendar endTime = DateUtils.calendar(2014, 3, 25);
//		System.err.println(differDay(beginTime, endTime));
		System.out.println(compareStartDifferEnd(parse("2017-12-01", DateFormat.YYYY_MM_DD),
				parse("2017-11-15", DateFormat.YYYY_MM_DD), 30));
	}

	/**
	 * * 判断给定日期是否为月末的一天 *
	 *
	 * @param date
	 * @return true搜索:是|false:不是
	 */
	public static boolean isLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
		if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
			return true;
		}
		return false;
	}

	public static String getNextDay(String day) {
		Date date = DateUtils.parse(day, DateFormat.YYYYMMDD);
		Calendar calendar = DateUtils.calendar(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		String result = DateUtils.format(calendar.getTime(), DateFormat.YYYYMMDD);
		return result;
	}

	/**
	 * 返回中间的月份，至少返回一个，格式为201509,201510,201511
	 *
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getMiddleMonth(long beginTime, long endTime) {
		List<String> result = new ArrayList<String>();
		String endMonth = DateUtils.format(endTime, DateFormat.YYYYMM);
		int endMon = Integer.parseInt(endMonth);
		int currentMon = 0;
		long currentTime = beginTime;
		do {
			String beginMonth = DateUtils.format(currentTime, DateFormat.YYYYMM);
			result.add(beginMonth);
			Calendar c = DateUtils.calendar(currentTime);
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
			currentTime = c.getTimeInMillis() / 1000;
			String currentMonth = DateUtils.format(currentTime, DateFormat.YYYYMM);
			currentMon = Integer.parseInt(currentMonth);
		} while (currentMon <= endMon);
		return result;
	}
	public static String format(Date date, DateFormat dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				dateFormatCache.get(dateFormat.ordinal()));
		return sdf.format(date);
	}

	public static String format(long date, DateFormat dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				dateFormatCache.get(dateFormat.ordinal()));
		return sdf.format(DateUtils.parse(date));
	}

	static Map<Integer, String> dateFormatCache = new ConcurrentHashMap<Integer, String>();
	static {
		dateFormatCache.put(0, "yyyy-MM-dd HH:mm:ss");
		dateFormatCache.put(1, "yyyy-MM-dd");
		dateFormatCache.put(2, "HH:mm:ss");
		dateFormatCache.put(3, "yyyy");
		dateFormatCache.put(4, "yyyyMM");
		dateFormatCache.put(5, "yyyyMMdd");
		dateFormatCache.put(6, "yyyyMMddHH");
		dateFormatCache.put(7, "yyyyMMddHHmm");
		dateFormatCache.put(8, "yyyyMMddHHmmss");
		dateFormatCache.put(9, "yyMM");
		dateFormatCache.put(10, "yyMMddHHmmss");
	}
	public static long getTodayEnd() {
		long now = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);

		return (calendar.getTimeInMillis() / 1000) - 1;
	}

	public static long getTodayBegin() {
		long now = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return (calendar.getTimeInMillis() / 1000);
	}

	/**
	 * 判断结束日期大于开始日期，并且间隔日期不能超过给定值。
	 * @param start 开始日期
	 * @param end 结束日期
	 * @param interval 间隔大小
	 * @return boolean
	 */
	public static boolean compareStartDifferEnd(Date start, Date end, int interval) {
		long startTime = start.getTime();
		long endTime = end.getTime();
		long oneDay = 60 * 60 * 24 * 1000;
		if (endTime > startTime && (endTime - startTime) / oneDay <= interval) {
			return true;
		}
		return false;
	}

	public enum DateFormat {
		/**
		 * Example 2011-11-03 18:00:00
		 */
		YY_YY_MM_DD_HH_MM_SS,
		/**
		 * Example 2011-11-03
		 */
		YYYY_MM_DD,
		/**
		 * Example 18:00:00
		 */
		HH_MM_SS,
		/**
		 * Example 2011
		 */
		YYYY,
		/**
		 * Example 201111
		 */
		YYYYMM,
		/**
		 * Example 20111103
		 */
		YYYYMMDD,
		/**
		 * Example 20111103
		 */
		YYYYMMDDHH,
		/**
		 * Example 2011110310
		 */
		YYYYMMDDHHMM,
		/**
		 * Example 201111031000
		 */
		YYYYMMDDHHMMSS,
		/**Example 1401*/
		YYMM,
		/**Example 140101120000*/
		YYMMDDHHMMSS,
		MMDDHHMMSS
	}

	/**
	 * 根据传入的格式将时间字符串转换成时间对象
	 *
	 * @param pattern 转换格式
	 * @param date    转换时间
	 * @return 时间对象
	 */
	public static Date parse(String pattern, String date) {

		try {
			if (date != null) {
				return new SimpleDateFormat(pattern).parse(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的格式将时间字符串转换成UNIX时间
	 *
	 * @param pattern 转换格式
	 * @param date    转换时间
	 * @return UNIX时间
	 */
	public static Long getTime(String pattern, String date) {
		try {
			if (date != null) {
				return new SimpleDateFormat(pattern).parse(date).getTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的格式将UNIX时间转换成格式字符串
	 *
	 * @param pattern 转换格式
	 * @param date    转换时间
	 * @return 时间字符串
	 */
	public static String format(String pattern, Long date) {

		try {
			if (date != null) {
				return new SimpleDateFormat(pattern).format(new Date(date));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

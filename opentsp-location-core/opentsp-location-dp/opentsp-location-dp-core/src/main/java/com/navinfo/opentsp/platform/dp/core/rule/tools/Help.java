package com.navinfo.opentsp.platform.dp.core.rule.tools;


import java.util.Calendar;
import java.util.Date;

public class Help {
	/**
	 * 时间范围判断
	 * 
	 * @param time
	 *            {@link Long} Gps时间
	 * @param beginTime
	 *            {@link Long} 开始时间
	 * @param endTime
	 *            {@link Long} 结束时间
	 * @param isEveryDay
	 *            {@link Boolean} 是否每天
	 * @return
	 */
	public static boolean dateIsLegal(long time, long beginTime, long endTime,
			boolean isEveryDay) {
		if (isEveryDay) {
			Calendar g = DateUtils.calendar(time);
			g.set(Calendar.YEAR, 1970);
			g.set(Calendar.MONTH, 0);
			g.set(Calendar.DAY_OF_MONTH, 1);
			long t = g.getTimeInMillis() / 1000;

			if( !( t >= beginTime && t<= endTime ) ){
				return false;
			}
		} else {
			if (time < beginTime || time > endTime) {
				return false;
			}
		}
		return true;
	}
	public static void main(String[] args) {
		Calendar beginTime = DateUtils.calendar(1970, 1, 1, 11, 10, 10);
		Calendar endTime = DateUtils.calendar(1970, 1, 1, 18, 10, 10);
		Calendar gpsTime = DateUtils.calendar(2015, 3, 7, 15, 04, 10);
		System.err.println(dateIsLegal(gpsTime.getTimeInMillis()/1000, beginTime.getTimeInMillis()/1000
				, endTime.getTimeInMillis()/1000, true));
	}
	/**
	 * 判断两个是否在同一天
	 * @param t1 UTC时间，单位秒
	 * @param t2 UTC时间，单位秒
	 * @return true：是；false：否
	 */
	public static boolean dateOnSameDay(long t1, long t2){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(t1*1000));
		int year01 = calendar.get(Calendar.YEAR);
		int month01 = calendar.get(Calendar.MONTH);
		int day01 = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.setTime(new Date(t2*1000));
		int year02 = calendar.get(Calendar.YEAR);
		int month02 = calendar.get(Calendar.MONTH);
		int day02 = calendar.get(Calendar.DAY_OF_MONTH);
		if(year01==year02 && month01==month02 && day01==day02){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 时间之后为true；否则为false；
	 * 时间段（isEveryDay：false）判断与结束时间的大小；
	 * 循环时间段（isEveryDay：true）判断与结束时间（只有时分秒）的大小；
	 * @param time
	 * @param beginDate
	 * @param endDate
	 * @param isEveryDay
	 * @return
	 */
	public static boolean dateIsAfer(long gpsTime, long endDate, boolean isEveryDay){
		if(isEveryDay){
			Calendar calendar = DateUtils.calendar(gpsTime);
			calendar.set(Calendar.YEAR, 1970);
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			gpsTime = calendar.getTimeInMillis() / 1000;
		}
		if( gpsTime > endDate){
			return true;
		}
		return false;
	}
	
	public enum StopMark {
		UnStop, Stop
	}

	public enum InOutMark {
		In, Out
	}
}

package com.navinfo.opentsp.platform.da.core.persistence.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.protobuf.TextFormat.ParseException;

public class LcDateFromat {
	
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/** 
     * 获取日期年份
     * @param date
     * @return
     * @throws ParseException
	 * @throws java.text.ParseException
     */
    public static int getYear(String date) throws ParseException, java.text.ParseException{
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(date));
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取日期月份
     * @param date
     * @return
     * @throws ParseException
     * @throws java.text.ParseException
     */
    public static int getMonth(String date) throws ParseException, java.text.ParseException{
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(date));
        return (calendar.get(Calendar.MONTH) + 1);
    }

    /**
     * 获取日期号
     * @param date
     * @return
     * @throws ParseException
     * @throws java.text.ParseException
     */
    public static int getDay(String date) throws ParseException, java.text.ParseException{
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(date));
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    /**
     * 获取月份起始日期
     * @param date
     * @return
     * @throws ParseException
     * @throws java.text.ParseException
     *
     */
    public static String getMinMonthDate(String date) throws ParseException, java.text.ParseException{
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 获取月份最后日期
     * @param date
     * @return
     * @throws ParseException
     * @throws java.text.ParseException
     */
    public static String getMaxMonthDate(String date) throws ParseException, java.text.ParseException{
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormat.format(calendar.getTime());
    }
    /**
     * 格式化时间，月份最后日期小时为yyyy-MM-dd  23:59:59
     * @param date 
     * @return
     */
    public static String getSubStringMonthDate(String date){
		String[] dateString=date.split(" ");
		String[] hhString=dateString[1].split(":");
		for (int i = 0; i < hhString.length; i++) {
			if(i==0){
				hhString[i]="23";
			}else {
				hhString[i]=":59";
			}
		}
		date=dateString[0]+" ";
		for (int i = 0; i < hhString.length; i++) {
			date+=hhString[i];
		}
    	return date;
    }
}

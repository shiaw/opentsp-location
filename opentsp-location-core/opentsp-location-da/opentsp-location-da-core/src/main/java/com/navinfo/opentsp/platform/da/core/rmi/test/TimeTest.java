package com.navinfo.opentsp.platform.da.core.rmi.test;

import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeTest {

	public static void main(String[] args) {
		System.err.println("-----------"+ DateUtils.format(1422028799, DateUtils.DateFormat.YY_YY_MM_DD_HH_MM_SS));
		System.err.println("-----------"+DateUtils.format(1422004567, DateUtils.DateFormat.YY_YY_MM_DD_HH_MM_SS));

		long time=1418832019l;
		long time1=1418372400l;
     Date date=new Date(time*1000);
     Date date1=new Date(time1*1000);
     SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     System.out.println(sf.format(date));
     System.out.println(sf.format(date1));
     
     
		String time2="2014-12-15 19:00:00";
		try {
			Date date2=sf.parse(time2);
			System.out.println(date2);
			System.out.println(date2.getTime()/1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

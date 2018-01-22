package com.navinfo.opentsp.platform.da.core.rmi.test;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.rmi.impl.district.AlarmStatisticsStoreServiceimpl;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.AreaINOUTAlarm;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import org.junit.Before;
import org.junit.Test;


public class TestStroreService {
	
	AlarmStatisticsStoreService service;
	
	@Before
	public void init() throws RemoteException{
		service=new AlarmStatisticsStoreServiceimpl();
	}
//	@Test
//	public void  testSaveOverspeedAlarmInfo() throws RemoteException, ParseException{
//	
//		String time="2014-09-01 00:00:00";
//		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date=sf.parse(time);
//		System.out.println(date);
//		System.out.println(date.getTime()/1000);
//		//long startTime=date.getTime()/1000;
//		int terminalId=0;
//		for(int j=0;j<100;j++){
//			List<OverspeedAlarm> overspeedAlarmList =new ArrayList<OverspeedAlarm>();
//			terminalId=2014001+j;
//			System.out.println("------terminalId"+terminalId);
//			long startTime=date.getTime()/1000;
//			long endTime=startTime+5;
//			for(int i=0;i<1440;i++){
//			OverspeedAlarm osa=new OverspeedAlarm();
//			osa.setTerminalId(terminalId);
//			osa.setBeginDate(startTime+i*1800);
//			osa.setContinuousTime(10);
//			osa.setEndDate(endTime+i*1800);
//		    osa.setBeginLat(112551128);
//		    osa.setBeginLng(37788121);
//		    osa.setLimitSpeed(80);
//		    osa.setEndLat(112551128);
//		    osa.setEndLng(37788121);
//		    osa.setMaxSpeed(120);
//		    osa.setMinSpeed(60);
//		    
//			overspeedAlarmList.add(osa);
//			}
//			service.saveOverspeedAlarmInfo(terminalId, 140901, overspeedAlarmList);
//		}
//		
//   }
	@Test

	public void  testSaveAreaINOUTAlarm() throws RemoteException, ParseException{
		String time="2014-09-01 00:00:00";
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=sf.parse(time);
		System.out.println(date);
		System.out.println(date.getTime()/1000);
		//long startTime=date.getTime()/1000;
		long terminalId=0;
		for(int j=0;j<100;j++){
			long startTime=date.getTime()/1000;
			List<AreaINOUTAlarm> areaINOUTAlarmList =new ArrayList<AreaINOUTAlarm>();
			terminalId=2014001+j;
			long endTime=startTime+5;
			for(int i=0;i<1440;i++){
			AreaINOUTAlarm osa=new AreaINOUTAlarm();
			osa.setTerminalId(terminalId);
			osa.setBeginDate(startTime+i*1800);
			osa.setContinuousTime(10);
			osa.setEndDate(endTime+i*1800);
		    osa.setBeginLat(112551128);
		    osa.setBeginLng(37788121);
		    osa.setEndLat(112551128);
		    osa.setEndLng(37788121);
		    osa.setAreaId(001);
			areaINOUTAlarmList.add(osa);
			}
			service.saveAreaINOUTAlarm(terminalId, System.currentTimeMillis()/1000, areaINOUTAlarmList);
		}
	}
//	public void  testSaveOverspeedAlarmInfo() throws RemoteException{
//		List<OverspeedAlarm> overspeedAlarmList =new ArrayList<OverspeedAlarm>();
//		for(int i=0;i<10;i++){
//		OverspeedAlarm osa=new OverspeedAlarm();
//		osa.setTerminalId(666667);
//		osa.setBeginDate(i);
//		osa.setContinuousTime(10);
//		osa.setEndDate(i+1);
//		osa.setEndLng(2392);
//		overspeedAlarmList.add(osa);
//
//		}
//	
//   }
//	@Test
//	public void  testSaveAreaOverspeedAlarmInfo() throws RemoteException{
//		List <AreaOverspeedAlarm>areaOverspeedAlarmList =new ArrayList<AreaOverspeedAlarm>();
//		AreaOverspeedAlarm osa=new AreaOverspeedAlarm();
//		osa.setBeginDate(8900);
//		osa.setContinuousTime(890);
//		osa.setEndDate(8089);
//		osa.setEndLng(2392);
//		areaOverspeedAlarmList.add(osa);
//		service.saveAreaOverspeedAlarmInfo(99999, 140811, areaOverspeedAlarmList);
//   }
//	@Test
//	public void  testSaveRouteINOUTAlarmInfo() throws RemoteException{
//		List<RouteINOUTAlarm> routeINOUTAlarmList =new ArrayList<RouteINOUTAlarm>();
//		RouteINOUTAlarm osa=new RouteINOUTAlarm();
//		osa.setBeginDate(8900);
//		osa.setContinuousTime(890);
//		osa.setEndDate(8089);
//		osa.setEndLng(2392);
//		routeINOUTAlarmList.add(osa);
//		service.saveRouteINOUTAlarmInfo(99999, 140811, routeINOUTAlarmList);
//   }
//	@Test
//	public void  testSaveRLineOverspeedAlarmInfo() throws RemoteException, ParseException{
//		String time="2014-08-01 00:00:00";
//		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date=sf.parse(time);
//		System.out.println(date);
//		System.out.println(date.getTime()/1000);
//		//long startTime=date.getTime()/1000;
//		long terminalId=0;
//		for(int j=0;j<100;j++){
//			long startTime=date.getTime()/1000;
//			List <RLineOverspeedAlarm>rLineOverspeedAlarmList =new ArrayList<RLineOverspeedAlarm>();
//			terminalId=2014001+j;
//			System.out.println("terminalId:"+"i"+j+"-------"+terminalId);
//			long endTime=startTime+5;
//			for(int i=0;i<1440;i++){
//			RLineOverspeedAlarm osa=new RLineOverspeedAlarm();
//			osa.setTerminalId(terminalId);
//			osa.setBeginDate(startTime+i*1800);
//			osa.setContinuousTime(10);
//			osa.setEndDate(endTime+i*1800);
//			osa.setEndLng(2392);
//			rLineOverspeedAlarmList.add(osa);
//			}
//			service.saveRLineOverspeedAlarmInfo(terminalId, 140811, rLineOverspeedAlarmList);
//		}
//		
//   }
public static void main(String[] args) throws RemoteException, ParseException {
	AlarmStatisticsStoreServiceimpl service=new AlarmStatisticsStoreServiceimpl();

	String time="2014-09-01 00:00:00";
	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date=sf.parse(time);
	System.out.println(date);
	System.out.println(date.getTime()/1000);
	//long startTime=date.getTime()/1000;
	long terminalId=0;
	for(int j=0;j<100;j++){
		long startTime=date.getTime()/1000;
		List<AreaINOUTAlarm> areaINOUTAlarmList =new ArrayList<AreaINOUTAlarm>();
		terminalId=2014001+j;
		long endTime=startTime+5;
		for(int i=0;i<1440;i++){
		AreaINOUTAlarm osa=new AreaINOUTAlarm();
		osa.setTerminalId(terminalId);
		osa.setBeginDate(startTime+i*1800);
		osa.setContinuousTime(10);
		osa.setEndDate(endTime+i*1800);
	    osa.setBeginLat(112551128);
	    osa.setBeginLng(37788121);
	    osa.setEndLat(112551128);
	    osa.setEndLng(37788121);
	    osa.setAreaId(001);
		areaINOUTAlarmList.add(osa);
		}
		service.saveAreaINOUTAlarm(terminalId, System.currentTimeMillis()/1000, areaINOUTAlarmList);
}
}
}

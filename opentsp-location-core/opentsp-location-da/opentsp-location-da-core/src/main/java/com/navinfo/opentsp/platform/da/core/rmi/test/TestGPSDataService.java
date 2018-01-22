package com.navinfo.opentsp.platform.da.core.rmi.test;

import com.navinfo.opentsp.platform.da.core.rmi.impl.district.TermianlDataServiceImpl;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.GpsDataEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.GpsDetailedEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlDataService;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;


public class TestGPSDataService {
	
	TermianlDataService service;
	
	@Before
	public void init() throws RemoteException{
		service=new TermianlDataServiceImpl();
	}

	@Test

	public void  testQueryTermianlGPSData() throws RemoteException, ParseException{
		String time="2014-09-23 00:00:00";
		String time1="2014-09-23 23:00:00";
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=sf.parse(time);
		Date date1=sf.parse(time1);
		long startTime=date.getTime()/1000;
		long endTime=date1.getTime()/1000;
		System.out.println(date);
		System.out.println(date.getTime()/1000);
	    System.out.println(sf.format(new Date(1411401600L*1000)));
		GpsDataEntity queryGpsData = service.queryGpsData(16800071544l, 1411401600 , 1411401600);
		if(queryGpsData!=null){
			System.out.println("--->"+queryGpsData.getDay());
			System.out.println("--->"+queryGpsData.gettId());
			List<GpsDetailedEntity> dataList = queryGpsData.getDataList();
			for(GpsDetailedEntity entity:dataList){
				System.out.println("--->"+entity.getGpsTime());
			}
			
		}
		
	}


}

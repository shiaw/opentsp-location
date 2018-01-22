package com.lc.rp.webService.common;

import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.GrandSonAreaTimesEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.ResVehiclePassTimesRecordsQuery;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.SonAreaTimesEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.VehiclePassTimesEntity;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCVehiclePassTimesRecords.VehiclePassTimesRecords;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCGrandSonAreaTimes.GrandSonAreaTimes;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCSonAreaTimes.SonAreaTimes;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCVehiclePassTimes.VehiclePassTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConvertKit {
	private static Logger logger = LoggerFactory.getLogger(ConvertKit.class);
	/**
	 * 四叉树类转protobuf
	 * @param recoreds
	 * @return
	 */
	public static byte[] convertEntity2Proto(ResVehiclePassTimesRecordsQuery recoreds){
		if(null!=recoreds){
		    if(recoreds.getStatusCode()==1){
		    	List<VehiclePassTimesEntity> dataList=recoreds.getDataList();
		    	VehiclePassTimesRecords.Builder builder=VehiclePassTimesRecords.newBuilder();
		    	builder.setStatusCode(recoreds.getStatusCode());
		    	builder.setTotalRecords(recoreds.getTotalRecords());
		    	for(VehiclePassTimesEntity vehicleEntity:dataList){
		    		VehiclePassTimes.Builder vehicle=VehiclePassTimes.newBuilder();
		    		vehicle.setId(vehicleEntity.get_id());
		    		vehicle.setTimes(vehicleEntity.getTimes());
		    		List<SonAreaTimesEntity> sonAreaTimes = vehicleEntity.getSonAreaTimes();
		    		if(null != sonAreaTimes && sonAreaTimes.size() > 0){
		    			for(SonAreaTimesEntity sonEntity : sonAreaTimes){
		    				SonAreaTimes.Builder son = SonAreaTimes.newBuilder();
		    				son.setId(sonEntity.get_id());
		    				son.setTimes(sonEntity.getTimes());
		    				List<GrandSonAreaTimesEntity> grandSonAreaTimes = sonEntity.getGrandSonAreaTimes();
		    				if(null != grandSonAreaTimes && grandSonAreaTimes.size() > 0){
		    					for(GrandSonAreaTimesEntity grandEntity : grandSonAreaTimes){
		    						GrandSonAreaTimes.Builder grand = GrandSonAreaTimes.newBuilder();
		    						grand.setId(grandEntity.get_id());
		    						grand.setTimes(grandEntity.getTimes());
		    						son.addGrandsonAreaTimes(grand);
		    					}
		    				}
		    				vehicle.addSonAreaTimes(son);
		    			}
		    		}
		    		builder.addDataList(vehicle);
		    	}
		    	return builder.build().toByteArray();
		    }else{
		    	logger.info("网格车次检索发生错误，状态码为："+recoreds.getStatusCode());
		    }
	    }else{
	    	logger.info("网格车次检索查询为空");
	    }
		return null;
	}
}

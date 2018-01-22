package com.navinfo.opentsp.platform.da.core.cache.alarm;

import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.application.DataStatisticsServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DACommonSummaryEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa.TerminalOnlinePercentage;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;


/***************************
 * 多终端报警概要分页缓存
 *
 * @author jin_s
 *
 */
public class TerminalOnOffDataCache  {
public int totalRecords;


    /**
     *查询key
     */
	private String queryKey;
	/**
	 * 报警数据缓存
	 */
	public TerminalOnOffDataCache(List<Long> terminalIds, String queryKey) {
		this.queryKey=queryKey;
		
	}

	
	private List<TerminalOnlinePercentage> terminalOnlineDataCache=new ArrayList<TerminalOnlinePercentage>();


	
	
   public boolean	queryTerminalOnlinePercentageData(List<Long> terminalIds,
			long beginTime,long endTime,Set <String>statisticDay){
	   DataStatisticsServiceImpl service=new DataStatisticsServiceImpl();
	   List<DACommonSummaryEntity> temps=null;

			try {
				temps = service.queryAlarmSummaryDataForTerminalOnOff(terminalIds, Constant.PropertiesKey.AlarmTypeTableMapping.AlarmTerminalOnOffLineStatus, statisticDay,1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			List<TerminalOnlinePercentage> terminalOnlinePercentageList=new ArrayList<TerminalOnlinePercentage>();
		    Map<Long,DACommonSummaryEntity> dataMap=new HashMap<Long,DACommonSummaryEntity>();
		 
		    	for(DACommonSummaryEntity summary:temps){
					dataMap.put(summary.getTerminalID(), summary);
				}
			    NumberFormat nf = NumberFormat.getNumberInstance();
		          nf.setMaximumFractionDigits(4);

				for(long terminalId:terminalIds){
					TerminalOnlinePercentage terminalOnlinePercentage=new TerminalOnlinePercentage();
					terminalOnlinePercentage.setTerminalId(terminalId);
					terminalOnlinePercentage.setStatisticDay(statisticDay.size());
					DACommonSummaryEntity daCommonSummaryEntity = dataMap.get(terminalId);
					if(daCommonSummaryEntity!=null){
						int recordsTotal = daCommonSummaryEntity.getRecordsTotal();
						terminalOnlinePercentage.setOnlineDay(recordsTotal);
						 float percentage = (float)recordsTotal/statisticDay.size();
						terminalOnlinePercentage.setOnlinePercentage(Float.parseFloat(nf.format(percentage)));
					}else{
						terminalOnlinePercentage.setOnlineDay(0);
						terminalOnlinePercentage.setOnlinePercentage(0);
					}
					terminalOnlinePercentageList.add(terminalOnlinePercentage);
				}
			
		 
		    if(terminalOnlinePercentageList!=null&&terminalOnlinePercentageList.size()>0){
		    
		    		terminalOnlineDataCache.addAll(terminalOnlinePercentageList);
		    		totalRecords+=terminalIds.size();
		    	
	    	 }
		return true;
	}
   public  List<TerminalOnlinePercentage>	getTerminalOnlinePercentageData(List<Long> terminalIds, long beginTime,
			long endTime, CommonParameter comParameter, String queryKey){
		
		if(totalRecords>0){
			int startIndex=(comParameter.getPageIndex()-1)*comParameter.getPageSize();
			if(startIndex<=totalRecords){
				int lastIndex=comParameter.getPageIndex()*comParameter.getPageSize()<=totalRecords?comParameter.getPageIndex()*comParameter.getPageSize():totalRecords;
			    if(lastIndex>=0){
			    	return terminalOnlineDataCache.subList(startIndex, lastIndex);
			    }
			}
		
		
		}
		return null;
	}

}

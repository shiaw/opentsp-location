package com.navinfo.opentsp.platform.da.core.cache.alarm;

import com.navinfo.opentsp.platform.da.core.persistence.application.DataStatisticsServiceImpl;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.MileageSummaryEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/***************************
 * 多终端报警概要分页缓存
 *
 * @author jin_s
 *
 */
public class MilagesSummaryCache  {

	public MilagesSummaryCache(List<Long> terminalId, String queryKey) {
		this.terminalIds=terminalId;
		this.queryKey=queryKey;

	}
	//缓存报警类型
	public String alarmType;

	//缓存有效存储时间
	public int DURATION_TIME;

	//最后一次访问有效时间
	public long lastUpdates;

	//增量缓存大小，记录个数
	public int DEFAULT_BUFFER_SIZE;



	public int totalRecords;


    /**
     *查询key
     */
	private String queryKey;
	/**
	 * 报警数据缓存
	 */
	private ArrayList<MileageSummaryEntity> alarmsCache=new ArrayList<MileageSummaryEntity>();

    /**
     * 终端列表
     */
	private List<Long> terminalIds = null;
	/**
	 * 缓存数量
	 */
	private int  cacheSize=0;
	/**
	 * 当前缓存的页数
	 */
	private int  currentPageSize=0;
	/**
	 * 页大小
	 */
	private int pageSize=20;
	/**
	 * 当前查询终端索引
	 */
	private int	 currentTerminalIndex=0;

	private int	 terminalRange=100;

	private int	 needToQueryScope=8;

	private int	 onceForQueryCount=100;

	private int	 firstForQueryCount=150;


	/**
	 * 获取缓存数据
	 *
	 * @param key
	 * @param tid
	 * @param type
	 * @param st
	 * @param et
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ParseException
	 */
	public List<MileageSummaryEntity> getAlarmCacheData(String key,  List<Long> tid, String type, long st, long et,int pageIndex,	int pageSize) {

		if((currentPageSize-pageIndex)<=needToQueryScope||(pageIndex-currentPageSize)>=0){
			if(currentTerminalIndex<tid.size()){
				expandAlarmCache(st,  et, pageIndex,pageSize, type);
			}

		}

		List<MileageSummaryEntity> retList=null;
		int startIndex=(pageIndex-1)*pageSize;
		int endIndex=pageIndex*pageSize;
		if(currentPageSize>0){
			if(endIndex>terminalIds.size()){
				endIndex=terminalIds.size();
			}
			retList=alarmsCache.subList(startIndex,endIndex);
		}

		 return retList;

	}

	public boolean expandAlarmCache(long st, long et, int pageIndex, int pageSize2,String type) {
		int lastTerminalIndex=currentTerminalIndex;
		List<MileageSummaryEntity> queryAlarmData=new ArrayList<MileageSummaryEntity>();
		List<MileageSummaryEntity> queryAlarmDataExt=new ArrayList<MileageSummaryEntity>();
		DataStatisticsServiceImpl service=new DataStatisticsServiceImpl();
		boolean isExpand=false;
		int querySize=0;
		do {
			int expandIndex=terminalRange<terminalIds.size()?terminalRange:terminalIds.size();
			try {
				queryAlarmDataExt=service.queryMileageSummaryData(terminalIds.subList(currentTerminalIndex, expandIndex), type, st, et);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	        querySize+=expandIndex;
			isExpand=true;
			currentTerminalIndex=expandIndex;
			queryAlarmData.addAll(queryAlarmDataExt);
		} while (querySize<onceForQueryCount&&currentTerminalIndex+1<terminalIds.size());

		if(isExpand){
			Map<Long,MileageSummaryEntity> dataMap=new HashMap<Long,MileageSummaryEntity>();
			for(MileageSummaryEntity summary:queryAlarmData){
				dataMap.put(summary.getTerminalId(), summary);
			}
			for(int i=lastTerminalIndex;i<querySize;i++){
				if(dataMap.get(terminalIds.get(i))==null){
					MileageSummaryEntity entity=new MileageSummaryEntity();
					entity.setTerminalId(terminalIds.get(i));

					alarmsCache.add(entity);
				}else{
					alarmsCache.add((MileageSummaryEntity) dataMap.get(terminalIds.get(i)));
				}

			}
			//alarmsCache.addAll(queryAlarmData);
			cacheSize=alarmsCache.size();
			currentPageSize=(cacheSize%pageSize==0)?cacheSize/pageSize:cacheSize/pageSize+1;
		}

		return isExpand;
	}
	public boolean initCache(long beginTime, long endTime, int pageIndex, int pageSize,String type) {
		List<MileageSummaryEntity> queryAlarmData=new ArrayList<MileageSummaryEntity>();
		List<MileageSummaryEntity> queryAlarmDataExt=new ArrayList<MileageSummaryEntity>();
		DataStatisticsServiceImpl service=new DataStatisticsServiceImpl();
		this.currentTerminalIndex=0;
		boolean init=false;
		int querySize=0;
		do {
			int expandIndex=currentTerminalIndex;
			//当前终端是否已经全部查完
			if(currentTerminalIndex==terminalIds.size()){
				
				break;
			}else{
				expandIndex=(currentTerminalIndex+terminalRange)<terminalIds.size()?(currentTerminalIndex+terminalRange):terminalIds.size();
			}
			
			try {
				queryAlarmDataExt=service.queryMileageSummaryData(terminalIds.subList(currentTerminalIndex, expandIndex), type, beginTime, endTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			init=true;
			querySize=expandIndex;
			currentTerminalIndex=expandIndex;
			queryAlarmData.addAll(queryAlarmDataExt);
		} while (querySize<firstForQueryCount&&currentTerminalIndex<terminalIds.size());
		
		if(init){
			Map<Long,MileageSummaryEntity> dataMap=new HashMap<Long,MileageSummaryEntity>();
			for(MileageSummaryEntity summary:queryAlarmData){
				dataMap.put(summary.getTerminalId(), summary);
			}
			for(int i=0;i<querySize;i++){
				if(dataMap.get(terminalIds.get(i))==null){
					MileageSummaryEntity entity=new MileageSummaryEntity();
					entity.setTerminalId(terminalIds.get(i));
					entity.setBeginDate(beginTime);
					entity.setEndDate(endTime);
					alarmsCache.add(entity);	
				}else{
					alarmsCache.add((MileageSummaryEntity) dataMap.get(terminalIds.get(i)));
				}
				
			}
			cacheSize=alarmsCache.size();
			currentPageSize=(cacheSize%pageSize==0)?cacheSize/pageSize:cacheSize/pageSize+1;
		}
			
		return init;
	}

}

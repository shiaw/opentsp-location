package com.navinfo.opentsp.platform.da.core.cache.alarm;

import com.navinfo.opentsp.platform.da.core.persistence.application.DataStatisticsServiceImpl;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.WFSummary;

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
public class WFSummaryCache  {

	public WFSummaryCache(List<Long> terminalId, String queryKey) {
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
	private ArrayList<WFSummary> alarmsCache=new ArrayList<WFSummary>();

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
	private int pageSize=10;
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
	public List<WFSummary> getAlarmCacheData(String key,  List<Long> tid, String type, long st, long et,int pageIndex,	int pageSize) {

		if((currentPageSize-pageIndex)<=needToQueryScope||(pageIndex-currentPageSize)>=0){
			if(currentTerminalIndex<tid.size()){
				expandAlarmCache(st,  et, pageIndex,pageSize, type);
			}

		}

		List<WFSummary> retList=null;
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
		List<WFSummary> queryAlarmData=new ArrayList<WFSummary>();
		List<WFSummary> queryAlarmDataExt=new ArrayList<WFSummary>();
		DataStatisticsServiceImpl service=new DataStatisticsServiceImpl();
		boolean isExpand=false;
		int querySize=0;
		do {
			int expandIndex=terminalRange<terminalIds.size()?terminalRange:terminalIds.size();
			queryAlarmDataExt=service.queryWFSummaryData(terminalIds.subList(currentTerminalIndex, expandIndex), type, st, et);
			 //queryAlarmData = service.queryAlarmData(terminalIds.subList(currentTerminalIndex, expandIndex), Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOverSpeed, st, et);
//			if(queryAlarmData!=null&&queryAlarmData.size()>0)
//			{
//				//querySize+=queryAlarmData.size();
//				isExpand=true;
//			}
	        querySize+=expandIndex;
			isExpand=true;
			currentTerminalIndex=expandIndex;
			queryAlarmData.addAll(queryAlarmDataExt);
		} while (querySize<onceForQueryCount&&currentTerminalIndex+1<terminalIds.size());

		if(isExpand){
			Map<Long,WFSummary> dataMap=new HashMap<Long,WFSummary>();
			for(WFSummary summary:queryAlarmData){
				dataMap.put(summary.getTerminalId(), summary);
			}
			for(int i=lastTerminalIndex;i<querySize;i++){
				if(dataMap.get(terminalIds.get(i))==null){
					WFSummary entity=new WFSummary();
					entity.setTerminalId(terminalIds.get(i));

					alarmsCache.add(entity);
				}else{
					alarmsCache.add((WFSummary) dataMap.get(terminalIds.get(i)));
				}

			}
			//alarmsCache.addAll(queryAlarmData);
			cacheSize=alarmsCache.size();
			currentPageSize=(cacheSize%pageSize==0)?cacheSize/pageSize:cacheSize/pageSize+1;
		}

		return isExpand;
	}
	public boolean initCache(long beginTime, long endTime, int pageIndex, int pageSize,String type) {
		List<WFSummary> queryAlarmData=new ArrayList<WFSummary>();
		List<WFSummary> queryAlarmDataExt=new ArrayList<WFSummary>();
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
			
			queryAlarmDataExt=service.queryWFSummaryData(terminalIds.subList(currentTerminalIndex, expandIndex), type, beginTime, endTime);

			init=true;
			querySize=expandIndex;
			currentTerminalIndex=expandIndex;
			queryAlarmData.addAll(queryAlarmDataExt);
		} while (querySize<firstForQueryCount&&currentTerminalIndex<terminalIds.size());
		
		if(init){
			Map<Long,WFSummary> dataMap=new HashMap<Long,WFSummary>();
			for(WFSummary summary:queryAlarmData){
				dataMap.put(summary.getTerminalId(), summary);
			}
			for(int i=0;i<querySize;i++){
				if(dataMap.get(terminalIds.get(i))==null){
					WFSummary entity=new WFSummary();
					entity.setTerminalId(terminalIds.get(i));
		
					alarmsCache.add(entity);	
				}else{
					alarmsCache.add((WFSummary) dataMap.get(terminalIds.get(i)));
				}
				
			}
			cacheSize=alarmsCache.size();
			currentPageSize=(cacheSize%pageSize==0)?cacheSize/pageSize:cacheSize/pageSize+1;
		}
			
		return init;
	}

}

package com.navinfo.opentsp.platform.da.core.cache.alarm;

import com.navinfo.opentsp.platform.da.core.persistence.application.DataStatisticsServiceImpl;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.AllAlarmData;

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
public class AllAlarmSummaryCache  {

	public AllAlarmSummaryCache(List<Long> terminalId, String queryKey) {
		this.terminalIds=terminalId;
		this.queryKey=queryKey;

	}
	/**
	 * 缓存报警类型
	 */
	public String alarmType;

	/**
	 * 缓存有效存储时间
	 */
	public int DURATION_TIME;

	/**
	 * 最后一次访问有效时间
	 */
	public long lastUpdates;

	/**
	 * 增量缓存大小，记录个数
	 */
	public int DEFAULT_BUFFER_SIZE;


   /**
    * 缓存数据总条数
    */
	public int totalRecords;


    /**
     *查询key
     */
	private String queryKey;
	/**
	 * 报警数据缓存
	 */
	private ArrayList<AllAlarmData> alarmsCache=new ArrayList<AllAlarmData>();

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
	/**
	 * 单次查询终端数量
	 */
	private int	 terminalRange=100;
	/**
	 * 需要扩充缓存的最小页数
	 */
	private int	 needToQueryScope=8;
    /**
     * 初次查询终端数量
     */
	private int	 onceForQueryCount=100;
	/**
	 * 初次缓冲数据条数
	 */
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
	public List<AllAlarmData> getAlarmCacheData(String key,  List<Long> tid, String type, long st, long et,int pageIndex,	int pageSize) {
		//当缓存的页数-请求的当前页数<需要扩充缓存的最小页数
		if((currentPageSize-pageIndex)<=needToQueryScope||(pageIndex-currentPageSize)>=0){
			if(currentTerminalIndex<tid.size()){
				//查询数据,并追加到缓存中
				expandAlarmCache(st,  et, pageIndex,pageSize, type);
			}
		}

		List<AllAlarmData> retList=null;
		//开始索引
		int startIndex=(pageIndex-1)*pageSize;
		//结束索引
		int endIndex=pageIndex*pageSize;
		if(currentPageSize>0){
			if(endIndex>terminalIds.size()){
				endIndex=terminalIds.size();
			}
			//截取缓存数据
			retList=alarmsCache.subList(startIndex,endIndex);
		}

		 return retList;

	}
	/**
	 * 查询数据,并追加到缓存中
	 * @param startTime
	 * @param endTime
	 * @param pageIndex
	 * @param pageSize2
	 * @param type
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 */
	public boolean expandAlarmCache(long startTime, long endTime, int pageIndex, int pageSize2,String type) {
		//上次查询缓存数量
		int lastTerminalIndex=currentTerminalIndex;
		List<AllAlarmData> queryAlarmData=new ArrayList<AllAlarmData>();
		List<AllAlarmData> queryAlarmDataExt=new ArrayList<AllAlarmData>();
		DataStatisticsServiceImpl service=new DataStatisticsServiceImpl();
		//是否扩充缓存成功
		boolean isExpand=false;
		int querySize=0;
		do {
			int expandIndex=terminalRange<terminalIds.size()?terminalRange:terminalIds.size();
			//查询数据
			try {
				queryAlarmDataExt=service.queryAllAlarmSummaryData(terminalIds.subList(currentTerminalIndex, expandIndex), type, startTime, endTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}

	        querySize+=expandIndex;
			isExpand=true;
			currentTerminalIndex=expandIndex;
			queryAlarmData.addAll(queryAlarmDataExt);
		} while (querySize<onceForQueryCount&&currentTerminalIndex+1<terminalIds.size());
		//扩充成功,将数据加入缓存
		if(isExpand){
			Map<Long,AllAlarmData> dataMap=new HashMap<Long,AllAlarmData>();
			for(AllAlarmData summary:queryAlarmData){
				dataMap.put(summary.getTerminalID(), summary);
			}
			for(int i=lastTerminalIndex;i<querySize;i++){
				if(dataMap.get(terminalIds.get(i))==null){
					AllAlarmData entity=new AllAlarmData();
					entity.setTerminalID(terminalIds.get(i));

					alarmsCache.add(entity);
				}else{
					alarmsCache.add((AllAlarmData) dataMap.get(terminalIds.get(i)));
				}

			}
			//alarmsCache.addAll(queryAlarmData);
			cacheSize=alarmsCache.size();
			currentPageSize=(cacheSize%pageSize==0)?cacheSize/pageSize:cacheSize/pageSize+1;
		}

		return isExpand;
	}
	/**
	 * 初始化缓存数据
	 * @param beginTime
	 * @param endTime
	 * @param pageIndex
	 * @param pageSize
	 * @param type
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 */
	public boolean initCache(long beginTime, long endTime, int pageIndex, int pageSize,String type)  {
		List<AllAlarmData> queryAlarmData=new ArrayList<AllAlarmData>();
		List<AllAlarmData> queryAlarmDataExt=new ArrayList<AllAlarmData>();
		DataStatisticsServiceImpl service=new DataStatisticsServiceImpl();
		this.currentTerminalIndex=0;
		boolean init=false;
		int querySize=0;

		/** terminalRange单次查询终端数量 = 100 上来直接根据查一次
		 * *************************************************************************
		 currentTermianlIndex 开始是查询第 0个
		 expandIndex = 0;
		 if(currentTerminalIndex+单次查询终端数量 < 所有查询的终端数) { 0+100 < 查询的所有终端数
		 	expandIndex = currentTerminalIndex+单次查询终端数量  0+100
		 }else {
		 	expandIndex = 查询的所有的终端数
		 }
		 queryAlarmDataExt = queryAllAlarmSummaryData(terminalIds.subList(0, 100), type, beginTime, endTime);//这一句上来可能例外
		 init=true;
		 querySize=100;
		 currentTerminalIndex=100;
		 queryAlarmData.addAll(queryAlarmDataExt);

		 (querySize<firstForQueryCount&&currentTerminalIndex<terminalIds.size());
		 while(100<初次缓冲数据条数 && 100<总查询终端数)
		 ***************************************************************************
		 expandIndex = currentTerminalIndex = 100;

		 */
		do {
			int expandIndex=currentTerminalIndex;
			//当前终端是否已经全部查完
			if(currentTerminalIndex==terminalIds.size()){
				break;
			}else{//判断一次截取 list中的多少个， 即一次查询多少个终端，
				//如果 currentTerminalIndex+一次
				expandIndex=(currentTerminalIndex+terminalRange)<terminalIds.size()?(currentTerminalIndex+terminalRange):terminalIds.size();
			}
			
			try {
				queryAlarmDataExt=service.queryAllAlarmSummaryData(terminalIds.subList(currentTerminalIndex, expandIndex), type, beginTime, endTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			init=true;
			querySize=expandIndex;
			currentTerminalIndex=expandIndex;
			queryAlarmData.addAll(queryAlarmDataExt);
		} while (querySize<firstForQueryCount&&currentTerminalIndex<terminalIds.size());
		
		if(init){
			Map<Long,AllAlarmData> dataMap=new HashMap<Long,AllAlarmData>();
			for(AllAlarmData summary:queryAlarmData){
				dataMap.put(summary.getTerminalID(), summary);
			}
			for(int i=0;i<querySize;i++){
				if(dataMap.get(terminalIds.get(i))==null){
					AllAlarmData entity=new AllAlarmData();
					entity.setTerminalID(terminalIds.get(i));
					
					alarmsCache.add(entity);	
				}else{
					alarmsCache.add((AllAlarmData) dataMap.get(terminalIds.get(i)));
				}
				
			}
			cacheSize=alarmsCache.size();
			currentPageSize=(cacheSize%pageSize==0)?cacheSize/pageSize:cacheSize/pageSize+1;
		}
			
		return init;
	}
public static void main(String[] args) {
	
}
}

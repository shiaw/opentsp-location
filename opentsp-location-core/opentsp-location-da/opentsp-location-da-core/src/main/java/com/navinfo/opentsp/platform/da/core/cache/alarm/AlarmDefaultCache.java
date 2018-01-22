package com.navinfo.opentsp.platform.da.core.cache.alarm;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.application.DataStatisticsServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.BaseAlarmEntity;

/**
 * 报警统计查询分页缓存
 * @author jin_s
 *
 * @param <T>
 */
public abstract class AlarmDefaultCache<T extends BaseAlarmEntity> {

	public AlarmDefaultCache(List<Long> terminalId, String queryKey) {
		this.queryKey=queryKey;
		this.terminalIds=terminalId;
	}


	public String getQueryKey() {
		return queryKey;
	}


	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	/**
	 * 缓存报警类型
	 */
	public String alarmType;

	/**
	 * 缓存有效存储时间
	 */
	public int DURATION_TIME;

	//最后一次访问有效时间
	public long lastUpdates;

	//增量缓存大小，记录个数
	public int DEFAULT_BUFFER_SIZE;

    /**
     * 本次查询满足条件的数据总条数
     */
	public int totalRecords;


    /**
     *查询key
     */
	private String queryKey;
	/**
	 * 报警数据缓存
	 */
	private ArrayList<T> alarmsCache=null;

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
	/**
	 * 一次查询的终端数据量上限
	 */
	private int	 terminalRange=100;
	/**
	 * 需要进行缓存扩充的最小页数
	 */
	private int	 needToQueryScope=10;
    /**
     * 一次扩充的数据条数
     */
	private int	 onceForQueryCount=100;
	/**
	 * 初次查询的数据量
	 */
	private int	 firstForQueryCount=200;
	/**
	 * 本次查询的时间分段区间
	 */
	private Map<String, Map<String,Long>> monthInterval;

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
	 */
	public List<T> getAlarmCacheData(String key, List<Long> tids, String type, long st, long et,int pageIndex,	int pageSize,Class clazz) throws InstantiationException, IllegalAccessException{

		if((currentPageSize-pageIndex)>=needToQueryScope){
			expandAlarmCache(st,  et, pageIndex,pageSize, clazz);
		}

		return alarmsCache.subList((pageIndex-1)*pageSize, pageIndex*pageSize);

	}

	/**
	 * 扩充缓存数据
	 * @param st
	 * @param et
	 * @param pageIndex
	 * @param pageSize2
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public boolean expandAlarmCache(long st, long et, int pageIndex, int pageSize2,Class clazz) throws InstantiationException, IllegalAccessException {
		List<DBObject> queryAlarmData=new ArrayList<DBObject>();
		DataStatisticsServiceImpl service=new DataStatisticsServiceImpl();
		//扩充数据是否成功
		boolean isExpand=false;
		int querySize=0;
		do {
			int expandIndex=terminalRange<terminalIds.size()?terminalRange:terminalIds.size();
			try {
				queryAlarmData = service.queryAlarmData(terminalIds.subList(currentTerminalIndex, expandIndex), Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOverSpeed, st, et);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(queryAlarmData!=null)
			{
				querySize+=queryAlarmData.size();
				isExpand=true;
			}

			currentTerminalIndex=expandIndex;
		} while (querySize<onceForQueryCount&&currentTerminalIndex+1<terminalIds.size());

		if(isExpand){
			for(DBObject opda : queryAlarmData){
				T item =(T) clazz.newInstance();
				item.dbObjectToBean(opda);
				alarmsCache.add(item);
			}
			//设置当前缓存数量
			cacheSize=alarmsCache.size();
			//设置当前缓存页数
			currentPageSize=(cacheSize%pageSize==0)?cacheSize/pageSize:cacheSize/pageSize+1;
		}

		return isExpand;
	}
	/**
	 * 初始化查询缓存
	 * @param st
	 * @param et
	 * @param pageIndex
	 * @param pageSize
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public boolean initCache(long st, long et, int pageIndex, int pageSize,Class clazz) throws InstantiationException, IllegalAccessException {
		List<DBObject> queryAlarmData=new ArrayList<DBObject>();
		DataStatisticsServiceImpl service=new DataStatisticsServiceImpl();
		this.currentTerminalIndex=0;
		//是否初始化成功并加载数据
		boolean init=false;
		int querySize=0;
		do {
			int expandIndex=currentTerminalIndex;
			//当前终端是否已经全部查完
			if(currentTerminalIndex+1==terminalIds.size()){
				
				break;
			}else{
				expandIndex=(currentTerminalIndex+terminalRange)<terminalIds.size()?(currentTerminalIndex+terminalRange):terminalIds.size();
			}
			
			 try {
				queryAlarmData = service.queryAlarmData(terminalIds.subList(currentTerminalIndex, expandIndex), Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOverSpeed, st, et);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(queryAlarmData!=null)
			{
				querySize+=queryAlarmData.size();
				init=true;
			}
			
			currentTerminalIndex=expandIndex;
	
		} while (querySize<firstForQueryCount&&currentTerminalIndex+1<terminalIds.size());
		
		if(init){
			for(DBObject opda : queryAlarmData){
				T item =(T) clazz.newInstance();
				item.dbObjectToBean(opda);
				alarmsCache.add(item);
			}
			cacheSize+=alarmsCache.size();
			currentPageSize=(cacheSize%pageSize==0)?cacheSize/pageSize:cacheSize/pageSize+1;
		}
			
		return init;
	}
	
public void clean(){
	this.alarmsCache.clear();
	this.terminalIds=null;
}

}

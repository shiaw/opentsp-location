package com.navinfo.opentsp.platform.dp.core.cache;

import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 待纠偏Gps位置数据缓存 <br>
 * 提供给纠偏服务使用 <br>
 * RP收到TA上传的GpsLocationDataEntity数据,以terminalID+serialNumber为key,缓存当前数据<br>
 * RP将数据转发到纠偏服务,收到纠偏服务返回后的纠偏数据后,从缓存中清除数据
 * 
 * @author lgw
 * 
 */
public class WaitCorrectGpsDataCache {
	/**纠偏超时缓存**/
	private final static long _correct_time_out = 2 * 10;
	private static Map<String, GpsLocationDataEntity> cache = new ConcurrentHashMap<String, GpsLocationDataEntity>();
	private final static WaitCorrectGpsDataCache instance = new WaitCorrectGpsDataCache();
	private final static Logger logger = LoggerFactory.getLogger(WaitCorrectGpsDataCache.class);
	private WaitCorrectGpsDataCache() {

	}

	public final static WaitCorrectGpsDataCache getInstance() {
		return instance;
	}
	/**
	 * 终端上报的位置数据如果流水号相同，认为是一条位置数据，覆盖
	 */
	public void addLocationData(long terminalId,int serialNumber, GpsLocationDataEntity locationData) {
		cache.put(terminalId + "_" + serialNumber, locationData);
	}

	public void delLocationData(long terminalId,int serialNumber) {
		cache.remove(terminalId + "_" + serialNumber);
	}
	
	
	public GpsLocationDataEntity getLocationData(long terminalId, int serialNumber) {
		GpsLocationDataEntity dataEntity = cache.get(terminalId + "_" + serialNumber);
		if(dataEntity == null){
			logger.error("终端[ "+terminalId+" ]纠偏后,原始位置数据未找到");
			return null;
		}else{
			cache.remove(terminalId + "_" + serialNumber);
			return dataEntity;
		}
	}
	
	public Map<String, GpsLocationDataEntity> get(){
		Map<String, GpsLocationDataEntity> temp = cache;
		return temp;
	}
	
	public void deleteGps(long terminalId){
		
	}

	public void clear() {
		cache.clear();
	}

	public int size() {
		return cache.size();
	}
	/**
	 * 获取最老的纠偏时间节点数据
	 * @return {@link Long}
	 */
	public long getOldestCorrectTime(){
		Map<String, GpsLocationDataEntity> temp = cache;
		long time = Long.MAX_VALUE;
		for (Entry<String, GpsLocationDataEntity> e : temp.entrySet()) {
			if(e.getValue().getCreateTime() < time){
				time = e.getValue().getCreateTime();
			}
		}
		return time;
	}
	/**
	 * 获取超时纠偏数据
	 * @return
	 */
	public List<GpsLocationDataEntity> getTimeOutGpsDataEntity(){
		Map<String, GpsLocationDataEntity> temp = cache;
		Iterator<Entry<String, GpsLocationDataEntity>> iterator = temp.entrySet().iterator();
		long _current_time = System.currentTimeMillis() / 1000;
		List<GpsLocationDataEntity> ret = new ArrayList<GpsLocationDataEntity>();
		while(iterator.hasNext()){
			Entry<String, GpsLocationDataEntity> entry = iterator.next();
			GpsLocationDataEntity dataEntity = entry.getValue();
			if(_current_time - dataEntity.getCreateTime() > _correct_time_out){
				ret.add(dataEntity);
				iterator.remove();
			}
		}
		return ret;
	}
}

class GpsDataCacheEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	private long createTime;
	private int timeout = 600;
	private Object object;
	private int commandId;

	public GpsDataCacheEntry(long createTime, int timeout, Object object, int commandId) {
		super();
		this.createTime = createTime;
		this.timeout = timeout;
		this.object = object;
		this.commandId = commandId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}

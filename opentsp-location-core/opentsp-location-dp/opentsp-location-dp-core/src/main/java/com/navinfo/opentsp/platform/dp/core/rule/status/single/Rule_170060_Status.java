package com.navinfo.opentsp.platform.dp.core.rule.status.single;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.rule.RuleStatus;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class Rule_170060_Status implements Serializable,RuleStatus {
	//
	private static final long serialVersionUID = 2213662024436176894L;

	@Resource
	private IRedisService redisService;

	private long terminalId;
	//禁驾报警，连续产生两次开始报警，key：上层业务系统提供的禁驾标识，value：禁驾报警产生的次数
	private Map<Long, Integer> drivingBanCounts = new HashMap<Long, Integer>();
	
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("终端["+this.terminalId+"]禁驾规则状态：{");
		Map<Long, Integer> temp = redisService.getAllKeysByLong(RuleEum.R170060.getMapKey(),Integer.class);
//		Iterator<Entry<Long, Integer>> iterator = drivingBanCounts.entrySet().iterator();
		Iterator<Entry<Long, Integer>> iterator = temp.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<Long, Integer> e = iterator.next();
				builder.append("[禁驾标识="+e.getKey()+";");
				builder.append("累计次数="+e.getValue()+"]");
		}
		return builder.toString();
	}
	public Integer getBanCounts(Long banIdentity) {
//		return drivingBanCounts.get(banIdentity);
		return redisService.getHashValue(RuleEum.R170060.getMapKey(), banIdentity, Integer.class);
	}
	//全部清空禁驾状态
	public void reset(){
//		drivingBanCounts.clear();
		redisService.delete(RuleEum.R170060.getMapKey());
	}
	//清空指定禁驾标识状态
	public void reset(Long banIdentify){
//		drivingBanCounts.remove(banIdentify);
		redisService.delete(RuleEum.R170060.getMapKey());
	}

	public void putBanCounts(Long banIdentify, Integer counts) {
//		drivingBanCounts.put(banIdentify, counts);
		redisService.hset(RuleEum.R170060.getMapKey(), banIdentify, counts);
	}
	
	public void removeBanCounts(Long banIdentity){
//		drivingBanCounts.remove(banIdentity);
		redisService.delete(RuleEum.R170060.getMapKey(), banIdentity);
	}

	@Override
	public void alarmHandle() {
		// TODO Auto-generated method stub
		
	} 
}

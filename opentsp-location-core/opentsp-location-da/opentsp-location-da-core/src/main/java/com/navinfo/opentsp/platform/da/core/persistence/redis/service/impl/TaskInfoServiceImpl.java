package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisListDao;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisListDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TaskInfoServiceImpl {
	private static Logger logger = LoggerFactory.getLogger(TaskInfoServiceImpl.class);
	private final static IRedisListDao redis_list_dao = new RedisListDaoImpl();
	/**
	 * 任务存储
	 * @param taskType
	 * @param nodeCode
	 * @param longs
	 * @return
	 */
	public void saveTaskInfo(int taskType,int nodeCode,long[] longs){
		if(redis_list_dao.existKey(String.valueOf(nodeCode)+"_"+String.valueOf(taskType))){
			redis_list_dao.del(String.valueOf(nodeCode)+"_"+String.valueOf(taskType));
		}
		redis_list_dao.pushForLimit(String.valueOf(nodeCode)+"_"+String.valueOf(taskType),longs);


	}
	/**
	 * 任务检索
	 * @param taskType
	 * @param nodeCode
	 * @return
	 */
	public Map<String,List<Long>> queryTaskInfo(int taskType,long[] nodeCode){
		Map<String, List <Long>> taskInfoMap=new HashMap<String, List <Long>>();
		if(nodeCode==null||nodeCode.length==0){
			logger.error("查询的节点列表为空");
			return taskInfoMap;
		}
		for(int i=0;i<nodeCode.length;i++){
			long node = nodeCode[i];
			List<Long> terminalList=redis_list_dao.get(String.valueOf(node)+"_"+String.valueOf(taskType));
			taskInfoMap.put(String.valueOf(node)+"_"+String.valueOf(taskType),terminalList);
		}
		return taskInfoMap;

	}

}

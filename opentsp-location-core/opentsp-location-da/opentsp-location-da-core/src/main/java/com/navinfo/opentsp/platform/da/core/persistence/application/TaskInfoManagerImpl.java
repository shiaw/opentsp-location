package com.navinfo.opentsp.platform.da.core.persistence.application;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.navinfo.opentsp.platform.da.core.persistence.TaskInfoManager;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TaskInfoServiceImpl;

public class TaskInfoManagerImpl implements  TaskInfoManager{
	private static Logger logger = LoggerFactory.getLogger(TaskInfoManagerImpl.class);
	TaskInfoServiceImpl service=new TaskInfoServiceImpl();
	
	/**
	 * 任务存储
	 * @param taskType
	 * @param nodeCode
	 * @param longs
	 * @return
	 */
	public void saveTaskInfo(int taskType,int nodeCode,long[] longs){
		logger.info("存储转存统计任务："+taskType+"---"+nodeCode+"---"+longs.length);
		service.saveTaskInfo(taskType, nodeCode, longs);

	}
	/**
	 * 任务检索
	 * @param taskType
	 * @param nodeCode
	 * @return
	 */
	public Map<String,List<Long>> queryTaskInfo(int taskType,long[] nodeCode){
		return service.queryTaskInfo(taskType, nodeCode);
	}
}

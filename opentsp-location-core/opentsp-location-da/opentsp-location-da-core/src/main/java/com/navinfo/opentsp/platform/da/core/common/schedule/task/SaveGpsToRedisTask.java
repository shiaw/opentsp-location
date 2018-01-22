package com.navinfo.opentsp.platform.da.core.common.schedule.task;

import java.util.TimerTask;

import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalOnOffStatusServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  com.navinfo.opentsp.platform.da.core.common.schedule.ITask;
import  com.navinfo.opentsp.platform.da.core.common.schedule.TaskStatus;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsData;

/**
 * 存储Gps数据任务
 * 
 * @author lgw
 * 
 */
public class SaveGpsToRedisTask extends TimerTask implements ITask {
	
	private Logger log = LoggerFactory.getLogger(SaveGpsToRedisTask.class);
	@Override
	public void setExecuteCycle(long cycle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getLastExecuteTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TaskStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		try {
			TempGpsData.saveGpsDataToRedis();
			TerminalOnOffStatusServiceImpl onLineData=new TerminalOnOffStatusServiceImpl();
			onLineData.saveStatusDataToRedis();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
}

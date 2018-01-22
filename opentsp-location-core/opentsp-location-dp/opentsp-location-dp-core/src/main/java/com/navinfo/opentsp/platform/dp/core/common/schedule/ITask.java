package com.navinfo.opentsp.platform.dp.core.common.schedule;

import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCMonitorThreadStatus.MonitorThreadStatus.ThreadStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public abstract class ITask extends TimerTask {
	protected Logger log = LoggerFactory.getLogger(ScheduleController.class);
	protected long lastExecuteTime;
	protected long executeCycle;

	/**
	 * 设置执行周期(秒)
	 * 
	 * @param cycle
	 *            {@link Long} 任务执行周期
	 */
	public void setExecuteCycle(long cycle){
		this.executeCycle = cycle;
	}

	/**
	 * 获取上次执行时间
	 * 
	 * @return {@link Long}
	 */
	public long getLastExecuteTime(){
		return lastExecuteTime;
	}

	/**
	 * 获取定时器状态
	 * 
	 * @return
	 */
	public ThreadStatus getStatus(){
		if (NodeHelper.getCurrentTime() - this.lastExecuteTime > this.executeCycle) {
			return ThreadStatus.threadException;
		} else {
			return ThreadStatus.threadNormal;
		}
	}
}

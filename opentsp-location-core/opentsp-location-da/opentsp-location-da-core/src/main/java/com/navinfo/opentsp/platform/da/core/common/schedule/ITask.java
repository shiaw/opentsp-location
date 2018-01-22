package com.navinfo.opentsp.platform.da.core.common.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ITask {
	Logger log = LoggerFactory.getLogger(ScheduleController.class);

	/**
	 * 设置执行周期(秒)
	 *
	 * @param cycle
	 *            {@link Long} 任务执行周期
	 */
	abstract void setExecuteCycle(long cycle);

	/**
	 * 获取上次执行时间
	 *
	 * @return {@link Long}
	 */
	abstract long getLastExecuteTime();

	/**
	 * 获取定时器状态
	 * 
	 * @return
	 */
	abstract TaskStatus getStatus();
}

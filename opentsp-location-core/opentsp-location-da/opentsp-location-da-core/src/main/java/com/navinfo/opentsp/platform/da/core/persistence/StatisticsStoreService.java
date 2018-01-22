package com.navinfo.opentsp.platform.da.core.persistence;

import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.DriverLogin;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.TerminalOnOffLineStatus;
import org.springframework.stereotype.Component;

/**
 *
 * @author jin_s
 *
 */
public interface StatisticsStoreService  {



	/**
	 * 保存终端产生驾驶员登录统计信息
	 * @param terminalId
	 * @param driverLogin
	 * @return
	 */
	abstract public PlatformResponseResult saveDriverLoginInfo(long terminalId,
															   DriverLogin	driverLogin);
	/**
	 * 保存终端在线状态统计信息
	 * @param terminalId
	 * @param day (数据格式为时间的YYMMDD)
	 * @param terminalOnOffLineStatusList
	 * @return
	 */
	abstract public PlatformResponseResult saveTerminalOnOffLineStatusInfo(long terminalId,
																		   TerminalOnOffLineStatus	terminalOnOffLineStatus);
	/**
	 *
	 * @param terminalId
	 * @param hourOfDay
	 * @param upFlow
	 * @param downFlow
	 * @return
	 */
	abstract PlatformResponseResult saveTerminalWFlowInfo(long terminalId,
														  long hourOfDay, float upFlow, float downFlow) ;



}

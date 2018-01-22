package com.navinfo.opentsp.platform.dp.core.common.schedule;

import com.navinfo.opentsp.platform.dp.core.cache.LatestGpsDataCache;
import com.navinfo.opentsp.platform.dp.core.common.schedule.task.CleanUpTerminalAlarmNotifyDACacheTask;
import com.navinfo.opentsp.platform.dp.core.common.schedule.task.CommonDataCache;
import com.navinfo.opentsp.platform.dp.core.common.schedule.task.SumGpsDataNumberTask;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCMonitorThreadStatus.MonitorThreadStatus.ThreadStatus;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCMonitorThreadStatus.MonitorThreadStatus.ThreadType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

/**
 * 调试任务管理器
 * 
 * @author aerozh
 * 
 */
public class ScheduleController {
	private final static long MILLIS = 1000;
	private final static long SECOND = 1 * MILLIS;
	private final static long MINUTE = 60 * SECOND;
	private final static long HOUR = 60 * MINUTE;
	private final static long DAY = 24 * HOUR;
	private final static Logger log = LoggerFactory
			.getLogger(ScheduleController.class);
	private static Timer timer = new Timer();
	private final static ScheduleController instance = new ScheduleController();

	private ScheduleController() {
	}

	public final static ScheduleController instance() {
		return instance;
	}

//	private static RemoveInvalidDataTask dp_removeInvalidDataProcess_VALUE = null;
//	private static DpAndDaDataGuaranteeCache dp_dpAndTaDataGuaranteeProcess_VALUE = null;
	private static CommonDataCache dp_commonDataProcess_VALUE = null;
//	private static TerminalParameterTask dp_terminalParameterProcess_VALUE = null;
//	private static CommonHeartbeatTask dp_commonHeartbeatProcess_VALUE = null;
//	private static MMStatusHeartbeatTask dp_statusHeartbeatProcess = null;
	
//	private static LatestGpsDataCacheTask latestGpsDataCacheTask = null;//最新位置数据缓存
	private static CleanUpTerminalAlarmNotifyDACacheTask cleanUpTerminalAlarmNotifyDACacheTask= null;
	private static SumGpsDataNumberTask	sumGpsDataNumberTask = null; //记录当日所有位置轨迹数	 
	private static boolean _is_start = false;
	public void start() {
		Calendar c1 = new GregorianCalendar();
	    c1.set(Calendar.HOUR_OF_DAY, 0);
	    c1.set(Calendar.MINUTE, 0);
	    c1.set(Calendar.SECOND, 0);
	    Calendar c2 = new GregorianCalendar();
	    c2.set(Calendar.HOUR_OF_DAY, 23);
	    c2.set(Calendar.MINUTE, 59);
	    c2.set(Calendar.SECOND, 59);
		LatestGpsDataCache.beginDate= c1.getTime().getTime()/1000;
		LatestGpsDataCache.endDate=c2.getTime().getTime()/1000;
//		log.error("SumGpsDataNumberTask启动："+c1.getTime().getTime()/1000);
				
//		// 清除断开TA相关数据任务
//		dp_removeInvalidDataProcess_VALUE = new RemoveInvalidDataTask();
//		dp_removeInvalidDataProcess_VALUE.setExecuteCycle(Configuration
//				.getInt("dp_removeInvalidDataInterval") * SECOND);
//		timer.schedule(dp_removeInvalidDataProcess_VALUE, 3 * SECOND,
//				Configuration.getInt("dp_removeInvalidDataInterval") * SECOND);
//		// 终端参数设置处理任务
//		dp_terminalParameterProcess_VALUE = new TerminalParameterTask();
//		dp_terminalParameterProcess_VALUE.setExecuteCycle(Configuration
//				.getInt("dp_terminalSetProcessInterval") * SECOND);
//		timer.schedule(dp_terminalParameterProcess_VALUE, 7 * SECOND,
//				Configuration.getInt("dp_terminalSetProcessInterval") * SECOND);
//		// DP和TA数据保障任务
//		dp_dpAndTaDataGuaranteeProcess_VALUE = new DpAndDaDataGuaranteeCache();
//		dp_dpAndTaDataGuaranteeProcess_VALUE.setExecuteCycle(Configuration
//				.getInt("dp_dpAndTaGuaranteeInterval") * SECOND);
//		timer.schedule(dp_dpAndTaDataGuaranteeProcess_VALUE, 13 * SECOND,
//				Configuration.getInt("dp_dpAndTaGuaranteeInterval") * SECOND);
//		// 公共数据缓存处理任务
//		dp_commonDataProcess_VALUE = new CommonDataCache();
//		dp_commonDataProcess_VALUE.setExecuteCycle(Configuration
//				.getInt("dp_commonDataProcessInterval") * SECOND);
//		timer.schedule(dp_commonDataProcess_VALUE, 17 * SECOND,
//				Configuration.getInt("dp_commonDataProcessInterval") * SECOND);
//
//		//最新位置数据缓存
//		latestGpsDataCacheTask = new LatestGpsDataCacheTask();
//		latestGpsDataCacheTask.setExecuteCycle(30);
//		timer.schedule(latestGpsDataCacheTask, 0 * MINUTE, 30 * SECOND);
//
//		//心跳任务
//		log.info("加载心跳任务...");
//		dp_commonHeartbeatProcess_VALUE = new CommonHeartbeatTask();
//		dp_commonHeartbeatProcess_VALUE.setExecuteCycle(30);
//		timer.schedule(dp_commonHeartbeatProcess_VALUE, 0 * MINUTE, 30 * SECOND);
		//MM状态心跳任务
//		log.info("加载MM状态心跳任务");
//		dp_statusHeartbeatProcess = new MMStatusHeartbeatTask();
//		dp_statusHeartbeatProcess.setExecuteCycle( 30 * SECOND);
//		timer.schedule(dp_statusHeartbeatProcess, 1 * SECOND, 30 * SECOND);
		
//		cleanUpTerminalAlarmNotifyDACacheTask = new CleanUpTerminalAlarmNotifyDACacheTask();
//
//		long PERIOD_DAY = 24 * 60 * 60 * 1000;
//		Calendar calendar = Calendar.getInstance();
//	    calendar.set(Calendar.HOUR_OF_DAY, 0); //凌晨0点
//	    calendar.set(Calendar.MINUTE, 0);
//	    calendar.set(Calendar.SECOND, 0);
//	    Date date=calendar.getTime(); //第一次执行定时任务的时间
//	    
//	    if (date.before(new Date())) {
//	        date = addDay(date, 1);
//	    }
//		cleanUpTerminalAlarmNotifyDACacheTask.setExecuteCycle(24 * HOUR);
//		log.info("加载清理报警终端缓存任务,firstTime="+date);
//		timer.schedule(cleanUpTerminalAlarmNotifyDACacheTask,date,24 * HOUR);
		
//		log.info("定时重置当日轨迹总数量");
//		sumGpsDataNumberTask = new SumGpsDataNumberTask();
//		sumGpsDataNumberTask.setExecuteCycle(24*HOUR);
//		timer.schedule(sumGpsDataNumberTask, date,24 * HOUR);
		
		_is_start = true;
	}
	public static Date addDay(Date date, int amount) {
	    Calendar startDT = Calendar.getInstance();
	    startDT.setTime(date);  
	    startDT.add(Calendar.DAY_OF_MONTH, amount);
	    return startDT.getTime();  
	} 
	public ThreadStatus getTaskStatus(ThreadType task) {
		if(!_is_start)
			return ThreadStatus.threadException; 
		switch (task.getNumber()) {
//		case ThreadType.dp_commonDataProcess_VALUE:
//			return dp_commonDataProcess_VALUE.getStatus();
//		case ThreadType.da_cleanAlarmQueryCacheTask_VALUE:
//			return cleanUpTerminalAlarmNotifyDACacheTask.getStatus();

		default:
			break;
		}
		return null;
	}

	
}

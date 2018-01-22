package com.navinfo.opentsp.platform.da.core.persistence.application;

import java.util.ArrayList;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.LogManage;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.*;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.*;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.*;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class LogManageImpl implements LogManage {
	
	private Logger log = LoggerFactory.getLogger(LogManageImpl.class);
	/**
	 * [方法为添加服务日志]
	 * @param lcServiceLog {@link:LcServiceLog 服务日志}
	 */
	@Override
	public void serverLogSave(LcServiceLogDBEntity lcServiceLog) {
		try {
			//获取数据库连接
			MySqlConnPoolUtil.startTransaction();
			LcServiceLogDao lcServiceLogDao=new LcServiceLogDaoImpl();
			//添加服务日志至数据库
			int result=lcServiceLogDao.add(lcServiceLog);
			if(result== LCPlatformResponseResult.PlatformResponseResult.success_VALUE){
				MySqlConnPoolUtil.commit();
			}else {
				MySqlConnPoolUtil.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	/**
	 * 节点日志
	 * @param lcNodeLog {@link:LcNodeLog 节点日志}
	 */
	@Override
	public void nodeLogSave(LcNodeLogDBEntity lcNodeLog) {
		try {
			//获取数据库连接
			MySqlConnPoolUtil.startTransaction();
			LcNodeLogDao lcNodeLogDao=new LcNodeLogDaoImpl();
			//添加节点日志至数据库
			int result=lcNodeLogDao.add(lcNodeLog);
			if(result== LCPlatformResponseResult.PlatformResponseResult.success_VALUE){
				MySqlConnPoolUtil.commit();
			}else {
				MySqlConnPoolUtil.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	/**
	 * 终端操作日志存储
	 * @param terminalOperationLog {@link:LcTerminalOperationLog 终端操作日志实体类}
	 */
	@Override
	public void terminalOperateLogSave(
			LcTerminalOperationLogDBEntity terminalOperationLog) {
		try {
			//获取数据库连接
			MySqlConnPoolUtil.startTransaction();
			LcTerminalOperationLogDao lcTerminalOperationLogDao=new LcTerminalOperationLogDaoImpl();
			//添加终端操作日志至数据库
			int result=lcTerminalOperationLogDao.add(terminalOperationLog);
			if(result== LCPlatformResponseResult.PlatformResponseResult.success_VALUE){
				MySqlConnPoolUtil.commit();
			}else {
				MySqlConnPoolUtil.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();

		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	/**
	 * 终端操作状态更新
	 * @param terminalId {@link:Long 终端标识}
	 * @param messageCod {@link:Integer 终端指令编码}
	 * @param results {@link:Boolean 操作状态}
	 */
	@Override
	public void terminalOperateLogUpdate(long terminalId, int messageCod,
			boolean results) {
		try {
			//根据条件修改终端操作状态results
			MySqlConnPoolUtil.startTransaction();
			LcTerminalOperationLogDao lcTerminalOperationLogDao= new LcTerminalOperationLogDaoImpl();
			boolean result=lcTerminalOperationLogDao.terminalOperateLogUpdate(terminalId, messageCod, results);
			if(result==true){
				MySqlConnPoolUtil.commit();
			}else {
				MySqlConnPoolUtil.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcTerminalSwitchLogDBEntity> queryTerminalSwitchLogList(
			long terminalId, int switchType, long start, long end) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalSwitchLogDao switchLogDao = new LcTerminalSwitchLogDaoImpl();
			List<LcTerminalSwitchLogDBEntity> switchLogs = new ArrayList<LcTerminalSwitchLogDBEntity>();
			switchLogs = switchLogDao.queryTerminalSwitchLog(terminalId,
					switchType, start, end);
			return switchLogs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalRuleLogDBEntity> queryTerminalRuleLogList(
			long terminalId, int ruleCode, long start, long end) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcTerminalRuleDaoLogImpl().queryTerminalRuleLogList(
					terminalId, ruleCode, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalAreaLogDBEntity> queryTerminalAreaLogList(
			long terminalId, int originalAreaId, long start, long end) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcTerminalAreaLogDaoImpl().queryTerminalAreaLogList(
					terminalId, originalAreaId, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalAreaDataLogDBEntity> queryTerminalAreaDataList(
			long terminalId, int taId) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcTerminalAreaDataLogDaoImpl()
					.queryTerminalAreaDataList(taId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public int queryServiceLogCount(long start, long end,
			int logDistrict, String logIp, int typeCode) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcServiceLogDaoImpl().queryServiceLogCount(start, end,
					logDistrict, logIp, typeCode);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcServiceLogDBEntity> queryServiceLogList(long start, long end,
			int logDistrict, String logIp, int typeCode,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcServiceLogDaoImpl().queryServiceLogList(start, end,
					logDistrict, logIp, typeCode,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public int queryNodeLogCount(int nlId, long start,
			long end, int logDistrict) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcNodeLogDaoImpl().queryNodeLogCount(nlId, start, end,
					logDistrict);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcNodeLogDBEntity> queryNodeLogList(int nlId, long start,
			long end, int logDistrict,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcNodeLogDaoImpl().queryNodeLogList(nlId, start, end,
					logDistrict,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalOperationLogDBEntity> queryTerminalOperationLogList(
			long terminalId, int operatinoType, long start, long end,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalOperationLogDao operationLogDao = new LcTerminalOperationLogDaoImpl();
			List<LcTerminalOperationLogDBEntity> operationLogs = new ArrayList<LcTerminalOperationLogDBEntity>();
			operationLogs = operationLogDao.queryTerminalOperationLogList(terminalId,
					operatinoType, start, end,currentPage,pageSize);
			return operationLogs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public LcTerminalRuleLogDBEntity queryTerminalRuleLogById(int ruleId,
			long terminalId) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalRuleLogDao ruleData=new LcTerminalRuleDaoLogImpl();
			LcTerminalRuleLogDBEntity entity=ruleData.findById(ruleId, LcTerminalRuleDBEntity.class);
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public LcTerminalOperationLogDBEntity queryTerminalOperationLogById(
			int tol_id,long terminalId) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcTerminalOperationLogDaoImpl().findById(tol_id, LcTerminalOperationLogDBEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public boolean saveMessageBroadcastLog(LcMessageBroadcastLogDBEntity entity) {
		boolean flag = false;
		try {
			//获取数据库连接
			MySqlConnPoolUtil.startTransaction();
			LcMessageBroadcastLogDao dao = new LcMessageBroadcastLogDaoImpl();
			//添加日志至数据库
			int result=dao.add(entity);
			if(result== LCPlatformResponseResult.PlatformResponseResult.success_VALUE){
				MySqlConnPoolUtil.commit();
				flag = true;
			}else {
				MySqlConnPoolUtil.rollback();
				log.error("存储信息播报日志出错："+entity.toString());
			}
		} catch (Exception e) {
			MySqlConnPoolUtil.rollback();
			e.printStackTrace();
			log.error("存储信息播报日志出错："+entity.toString());
		} finally {
			MySqlConnPoolUtil.close();
		}
		return flag;
	}
	@Override
	public List<LcMessageBroadcastLogDBEntity> queryMessageBroadcastLogList(
			long terminalId, int areaId, long start, long end,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcMessageBroadcastLogDao dao = new LcMessageBroadcastLogDaoImpl();
			return dao.queryLogList(terminalId, areaId, start, end,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public boolean saveOutRegionLimitSpeedLog(
			LcOutRegionLimitSpeedLogDBEntity entity) {
		boolean flag = false;
		try {
			//获取数据库连接
			MySqlConnPoolUtil.startTransaction();
			LcOutRegionLimitSpeedLogDao dao = new LcOutRegionLimitSpeedLogDaoImpl();
			//添加日志至数据库
			int result=dao.add(entity);
			if(result== LCPlatformResponseResult.PlatformResponseResult.success_VALUE){
				MySqlConnPoolUtil.commit();
				flag = true;
			}else {
				MySqlConnPoolUtil.rollback();
				log.error("存储信息播报日志出错："+entity.toString());
			}
		} catch (Exception e) {
			MySqlConnPoolUtil.rollback();
			e.printStackTrace();
			log.error("存储信息播报日志出错："+entity.toString());
		} finally {
			MySqlConnPoolUtil.close();
		}
		return flag;
	}
	@Override
	public List<LcOutRegionLimitSpeedLogDBEntity> queryOutRegionLimitSpeedLogList(
			long terminalId, int areaId, long start, long end,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcOutRegionLimitSpeedLogDao dao = new LcOutRegionLimitSpeedLogDaoImpl();
			return dao.queryLogList(terminalId, areaId, start, end,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public int getCount(long terminalId, int areaId, long start, long end) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcOutRegionLimitSpeedLogDao dao = new LcOutRegionLimitSpeedLogDaoImpl();
			return dao.getCount(terminalId, areaId, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
}

package com.navinfo.opentsp.platform.da.core.persistence.application;

import com.navinfo.opentsp.platform.da.core.persistence.SynchronousManage;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcSynchronizationLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcSynchronizationLogDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcSynchronizationLogDaoImpl;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;

import java.util.List;



public class SynchronousManageImpl implements SynchronousManage {

	@Override
	public LCPlatformResponseResult.PlatformResponseResult saveSynchronousData(
			LcSynchronizationLogDBEntity log) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcSynchronizationLogDao lcSynchronizationLogDao = new LcSynchronizationLogDaoImpl();
			int result = 0;
			result = lcSynchronizationLogDao.add(log);
			if (result == LCPlatformResponseResult.PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
				return LCPlatformResponseResult.PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return LCPlatformResponseResult.PlatformResponseResult.failure;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}

	}

	@Override
	public List<LcSynchronizationLogDBEntity> getSynchronizationData() {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcSynchronizationLogDao synLogDao = new LcSynchronizationLogDaoImpl();
			return synLogDao.getSynchronizationData();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public LCPlatformResponseResult.PlatformResponseResult updateSynchronousData(
			LcSynchronizationLogDBEntity log) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcSynchronizationLogDao lcSynchronizationLogDao = new LcSynchronizationLogDaoImpl();
			int result = 0;
			result = lcSynchronizationLogDao.updateByUniqueCode(log);
			if (result == LCPlatformResponseResult.PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
				return LCPlatformResponseResult.PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return LCPlatformResponseResult.PlatformResponseResult.failure;
			}
		} catch (Exception e) {
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}

	}

	@Override
	public int getMaxRecordValue() {

		try {
			MySqlConnPoolUtil.startTransaction();
			LcSynchronizationLogDao lcSynchronizationLogDao = new LcSynchronizationLogDaoImpl();
			int result = 0;
			result = lcSynchronizationLogDao.getMaxRecordValue();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("数据库操作异常");
		} finally {
			MySqlConnPoolUtil.close();
		}

	
	}
}

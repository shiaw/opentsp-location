package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.common.DBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcSynchronizationLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcSynchronizationLogDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMap;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMapException;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;



public class LcSynchronizationLogDaoImpl extends BaseDaoImpl<LcSynchronizationLogDBEntity> implements LcSynchronizationLogDao {

	@Override
	public List<LcSynchronizationLogDBEntity> getSynchronizationData() {
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT lcsl.ID, lcsl.DISTRICT, lcsl.MODULENAME, lcsl.OPERATIONDATE, lcsl.OPERATIONOBJECT, lcsl.OPERATIONTYPE,lcsl.PRIMARYKEYS,");
		builder.append("lcsl.STATUS,lcsl.TYPE,lcsl.WEBSERVICECLASS FROM LC_SYNCHRONIZATIONLOG lcsl WHERE lcsl.STATUS = 0");
		try {
			List<LcSynchronizationLogDBEntity> synchronizationDatas =null;
			synchronizationDatas=qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcSynchronizationLogDBEntity>(
							LcSynchronizationLogDBEntity.class));
			return synchronizationDatas;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int updateByUniqueCode(LcSynchronizationLogDBEntity log) {
		try {
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(log);
			// 得到类注解
			String tableName = entity.getTableName();
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("UPDATE ");
			builder.append(tableName.toUpperCase());
			builder.append(" SET ");
			builder.append(" status= ?");
			builder.append(" WHERE  id =?");
			// 执行sql
			executeUpdate(builder.toString(),log.getStatus(),log.getId());
			return PlatformResponseResult.success_VALUE;
		} catch (ObjectForMapException | SQLException e) {
			e.printStackTrace();
			return PlatformResponseResult.failure_VALUE;
		}
	}

	@Override
	public int getMaxRecordValue() throws Exception{
		try {
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT lcsl.ID, lcsl.DISTRICT, lcsl.MODULENAME, lcsl.OPERATIONDATE, lcsl.OPERATIONOBJECT, lcsl.OPERATIONTYPE,lcsl.PRIMARYKEYS,");
			builder.append("lcsl.STATUS,lcsl.TYPE,lcsl.WEBSERVICECLASS FROM LC_SYNCHRONIZATIONLOG lcsl ORDER BY lcsl.ID DESC LIMIT 0,1");
			LcSynchronizationLogDBEntity synchronizationData =qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanHandler<LcSynchronizationLogDBEntity>(
							LcSynchronizationLogDBEntity.class));
			if(synchronizationData==null){
				return 1;
			}else{
				return synchronizationData.getId();

			}
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public int add(LcSynchronizationLogDBEntity t) throws SQLException {
		try {
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(t);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到所有属性
			String fields = entity.fieldNamesToString();
			//拼接主键
			fields=fields+","+entity.getPrimaryKeyName();
			// 得到属性的值
			Object[] values = entity.fieldValues();
			//拼凑所有要插入的值
			Object[] allValues=new Object[values.length+1];
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("INSERT INTO ");
			builder.append(tableName);
			builder.append("(" + fields.toUpperCase());
			builder.append(") VALUES(");
			for (int i = 0; i <= values.length; i++) {
				if (i == 0) {
					builder.append("?");
				} else {
					builder.append(",?");
				}
				if(i==values.length){
					allValues[i]=entity.getPrimaryKeyValue();
				}else{
					allValues[i]=values[i];
				}
			}
			builder.append(")");
			// 执行sql
			executeUpdate(builder.toString(), allValues);
			return PlatformResponseResult.success_VALUE;
		} catch (ObjectForMapException e) {
			e.printStackTrace();
			return PlatformResponseResult.failure_VALUE;
		}
	}


}

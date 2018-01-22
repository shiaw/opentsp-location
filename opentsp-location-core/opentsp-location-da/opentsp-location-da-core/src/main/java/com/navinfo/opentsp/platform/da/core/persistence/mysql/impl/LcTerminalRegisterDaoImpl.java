package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;

import com.navinfo.opentsp.platform.da.core.persistence.common.DBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMap;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMapException;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRegisterDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalRegisterDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;


public class LcTerminalRegisterDaoImpl extends BaseDaoImpl<LcTerminalRegisterDBEntity> implements LcTerminalRegisterDao {
	/**
	 * 根据终端id查找
	 * @param terminalId
	 * @return
	 */
	@Override
	public LcTerminalRegisterDBEntity getByTerminalId(long terminalId) {
		try {
			//拼接sql，查询LC_NODE_CONFIG表  条件为服务区域类型 LC_NODE_CONFIG.DISTRICT
			StringBuilder builder=new StringBuilder("SELECT ");
			builder.append(" REGISTER.REGISTER_ID,REGISTER.TERMINAL_ID,REGISTER.AUTH_CODE,REGISTER.PROVINCE, ");
			builder.append(" REGISTER.CITY,REGISTER.PRODUCT,REGISTER.TERMINAL_TYPE,REGISTER.TERMINAL_SN, ");
			builder.append(" REGISTER.LICENSE_COLOR,REGISTER.LICENSE ");
			builder.append(" FROM LC_TERMINAL_REGISTER REGISTER ");
			builder.append(" WHERE REGISTER.TERMINAL_ID=? ");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			LcTerminalRegisterDBEntity lcTerminalRegister = qryRun.query(conn, builder.toString(), new BeanHandler<LcTerminalRegisterDBEntity>(LcTerminalRegisterDBEntity.class),terminalId);
			return lcTerminalRegister;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public LcTerminalRegisterDBEntity getByAuthCode(String auth_code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByTerminalId(LcTerminalRegisterDBEntity terminalRegister) throws SQLException{
		try {
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(terminalRegister);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到所有有效属性
			String[] fields = entity.fieldNames();
			// 得到有效属性的值
			Object[] values = entity.fieldValues();
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("UPDATE ");
			builder.append(tableName.toUpperCase());
			builder.append(" SET ");
			for (int i = 0; i < fields.length; i++) {
				if (i == 0) {
					builder.append(fields[i].toUpperCase() + "=?");
				} else {
					builder.append("," + fields[i].toUpperCase() + "=?");
				}
			}
			builder.append(" WHERE TERMINAL_ID=?");
			// 执行sql
			Object[] objValues=new Object[values.length+1];
			for (int i = 0; i < objValues.length; i++) {
				if(i==objValues.length-1){
					objValues[i]=terminalRegister.getTerminal_id();
				}else {
					objValues[i]=values[i];
				}
			}
			executeUpdate(builder.toString(),objValues );
			return PlatformResponseResult.success_VALUE;
		} catch (ObjectForMapException e) {
			e.printStackTrace();
			return PlatformResponseResult.failure_VALUE;
		}
	}



}

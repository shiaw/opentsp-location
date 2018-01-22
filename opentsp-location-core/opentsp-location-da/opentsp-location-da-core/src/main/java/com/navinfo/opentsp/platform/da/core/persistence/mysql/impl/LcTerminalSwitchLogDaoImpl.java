package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.common.DBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMap;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMapException;
import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.TerminalInfoModel;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalOperationLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalSwitchLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalSwitchLogDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;



public class LcTerminalSwitchLogDaoImpl extends
		BaseDaoImpl<LcTerminalSwitchLogDBEntity> implements
		LcTerminalSwitchLogDao {

	@Override
	public void terminalOperateLogSave(
			LcTerminalOperationLogDBEntity terminalOperationLog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminalOperateLogUpdate(long terminalId, int messageCod,
			boolean results) {
		// TODO Auto-generated method stub

	}

	@Override
	public TerminalInfoModel queryTerminalInfoRes(long terminalId) {
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT "
				+ "info.TERMINAL_ID,"
				+ "info.DISTRICT,"
				+ "info.PROTO_CODE,"
				+ "regist.TERMINAL_SN,"
				+ "switch.SWITCH_TYPE,"
				+ "node.LOCAL_IP_ADDRESS "
				+ "FROM LC_TERMINAL_INFO info "
				+ "LEFT JOIN LC_TERMINAL_SWITCH_LOG switch ON info.TERMINAL_ID = switch.TERMINAL_ID "
				+ "LEFT JOIN LC_TERMINAL_REGISTER regist ON info.TERMINAL_ID = regist.TERMINAL_ID "
				+ "LEFT JOIN LC_NODE_CONFIG node ON info.NODE_CODE = node.NODE_CODE where  info.TERMINAL_ID = ?");
		try {
			TerminalInfoModel terminalInfoModel = qryRun
					.query(conn, builder.toString().toUpperCase(),
							new BeanHandler<TerminalInfoModel>(
									TerminalInfoModel.class), terminalId);
			return terminalInfoModel;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<LcTerminalSwitchLogDBEntity> queryTerminalSwitchLog(
			long terminalId, int switchType, long start, long end) {
		try {
			LcTerminalSwitchLogDBEntity lcSwitchLog = new LcTerminalSwitchLogDBEntity();
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(lcSwitchLog);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到有效属性
			String fields = entity.fieldNamesToString();
			// 拼接主键
			fields += "," + entity.getPrimaryKeyName();
			StringBuilder builder = new StringBuilder();
			builder.append("select ");
			builder.append(fields);
			builder.append(" from ");
			builder.append(tableName);
			builder.append(" where 1= 1 ");
			if (start != 0) {
				builder.append(" and SWITCH_TIME >=" + start + " ");
			}
			if (end != 0) {
				builder.append(" and SWITCH_TIME <=" + end + " ");
			}
			if (terminalId != 0) {
				builder.append(" and TERMINAL_ID =" + terminalId + " ");
			}
			if (switchType != 0) {
				builder.append(" and SWITCH_TYPE = " + switchType + " ");
			}

			builder.append(" ORDER BY SWITCH_TIME");
			return super.queryForList(builder.toString().toUpperCase(),
					LcTerminalSwitchLogDBEntity.class);
		} catch (ObjectForMapException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT "
				+ "info.TERMINAL_ID,"
				+ "info.DISTRICT,"
				+ "info.PROTO_CODE,"
				+ "regist.TERMINAL_SN,"
				+ "switch.SWITCH_TYPE,"
				+ "node.LOCAL_IP_ADDRESS "
				+ "FROM LC_TERMINAL_INFO info "
				+ "LEFT JOIN LC_TERMINAL_SWITCH_LOG switch ON info.TERMINAL_ID = switch.TERMINAL_ID "
				+ "LEFT JOIN LC_TERMINAL_REGISTER regist ON info.TERMINAL_ID = regist.TERMINAL_ID "
				+ "LEFT JOIN LC_NODE_CONFIG node ON info.NODE_CODE = node.NODE_CODE where  info.TERMINAL_ID = ?");
		System.out.println(builder.toString());
	}
}

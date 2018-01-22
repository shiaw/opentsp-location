package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalMappingDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalMappingDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LcTerminalMappingDaoImpl extends BaseDaoImpl<LcTerminalMappingDBEntity>
		implements LcTerminalMappingDao {
	private Logger logger = LoggerFactory.getLogger(LcTerminalMappingDaoImpl.class);

	@Override
	public int addTerminalMapping(long mainId, long secondId) {
		int result = 0;
		LcTerminalMappingDBEntity entity = new LcTerminalMappingDBEntity();
		entity.setMain_tid(mainId);
		entity.setSecondary_tid(secondId);
		try {
//			result = super.add(entity);
			QueryRunner qryRun = new QueryRunner();
			Connection connection = MySqlConnPoolUtil.getContainer().get();
			String sql = "INSERT INTO LC_TERMINAL_MAPPING(MAIN_TID,SECONDARY_TID) VALUES(?,?)";
			result = qryRun.update(connection, sql, mainId,secondId);
		} catch (Exception e) {
			logger.error("添加终端关联映射失败："+entity.toString());
		}
		return result;
	}

	@Override
	public int deleteTerminalMapping(long mainId, long secondId) {
		int result = 0;
		try {
			QueryRunner qryRun = new QueryRunner();
			Connection connection = MySqlConnPoolUtil.getContainer().get();
			String sql = "DELETE FROM LC_TERMINAL_MAPPING WHERE MAIN_TID=? AND SECONDARY_TID=?";
			result = qryRun.update(connection, sql, mainId,secondId);
		} catch (SQLException e) {
			logger.error("删除终端关联映射失败：mainId:"+mainId+",secondId:"+secondId);
		}
		return result;
	}

	@Override
	public List<LcTerminalMappingDBEntity> queryAllMapping() {
		MySqlConnPoolUtil.startTransaction();
		QueryRunner qryRun = new QueryRunner();
		String sql = "SELECT * FROM LC_TERMINAL_MAPPING";
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		try {
			List<LcTerminalMappingDBEntity> list = qryRun.query(conn, sql, new BeanListHandler<LcTerminalMappingDBEntity>(
					LcTerminalMappingDBEntity.class));
			return list;
		} catch (SQLException e) {
			logger.error("查询终端关联映射失败");
			MySqlConnPoolUtil.rollback();
		}finally{
			MySqlConnPoolUtil.close();
		}
		return null;
	}

	@Override
	public LcTerminalMappingDBEntity getMappingByMainId(long mainId) {
		try {
			QueryRunner qryRun = new QueryRunner();
			Connection connection = MySqlConnPoolUtil.getContainer().get();
			String sql = "SELECT * FROM LC_TERMINAL_MAPPING WHERE MAIN_TID=?";
			LcTerminalMappingDBEntity entity = qryRun.query(connection, sql, new BeanHandler<LcTerminalMappingDBEntity>(LcTerminalMappingDBEntity.class), mainId);
			return entity;
		} catch (SQLException e) {
			logger.error("查询终端关联映射失败：mainId:"+mainId);
		}
		return null;
	}

	@Override
	public LcTerminalMappingDBEntity getMappingBySecondId(long secondId) {
		try {
			QueryRunner qryRun = new QueryRunner();
			Connection connection = MySqlConnPoolUtil.getContainer().get();
			String sql = "SELECT * FROM LC_TERMINAL_MAPPING WHERE SECONDARY_TID=?";
			LcTerminalMappingDBEntity entity = qryRun.query(connection, sql, new BeanHandler<LcTerminalMappingDBEntity>(LcTerminalMappingDBEntity.class), secondId);
			return entity;
		} catch (SQLException e) {
			logger.error("查询终端关联映射失败：secondId:"+secondId);
		}
		return null;
	}


}

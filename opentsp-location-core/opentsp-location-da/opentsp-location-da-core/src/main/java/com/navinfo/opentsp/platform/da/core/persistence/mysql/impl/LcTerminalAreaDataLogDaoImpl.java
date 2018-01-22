package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDataLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalAreaDataLogDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;


public class LcTerminalAreaDataLogDaoImpl extends BaseDaoImpl<LcTerminalAreaDataLogDBEntity>
		implements LcTerminalAreaDataLogDao {

	@Override
	public List<LcTerminalAreaDataLogDBEntity> queryTerminalAreaDataList(int taId) {
		try {
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			String sql = "SELECT   AREA_DATA.TAD_ID,AREA_DATA.TA_ID,AREA_DATA.DATA_SN,AREA_DATA.DATA_STATUS,AREA_DATA.LATITUDE,AREA_DATA.LONGITUDE FROM LC_TERMINAL_AREA_DATA AREA_DATA  WHERE   AREA_DATA.TA_ID=?";
			List<LcTerminalAreaDataLogDBEntity> lcTerminalAreasData = qryRun.query(conn,
					sql, new BeanListHandler<LcTerminalAreaDataLogDBEntity>(
							LcTerminalAreaDataLogDBEntity.class), taId);
			return lcTerminalAreasData;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}




}

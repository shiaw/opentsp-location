package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDataDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalAreaDataDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;


public class LcTerminalAreaDataDaoImpl extends BaseDaoImpl<LcTerminalAreaDataDBEntity>
		implements LcTerminalAreaDataDao {

	@Override
	public List<LcTerminalAreaDataDBEntity> getTerminalAreaData(int taId) {
		try {
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			String sql = "SELECT   AREA_DATA.TAD_ID,AREA_DATA.TA_ID,AREA_DATA.DATA_SN,AREA_DATA.DATA_STATUS,AREA_DATA.LATITUDE,AREA_DATA.LONGITUDE,AREA_DATA.RADIUS_LEN FROM LC_TERMINAL_AREA_DATA AREA_DATA  WHERE   AREA_DATA.TA_ID=?";
			List<LcTerminalAreaDataDBEntity> lcTerminalAreasData = qryRun.query(conn,
					sql, new BeanListHandler<LcTerminalAreaDataDBEntity>(
							LcTerminalAreaDataDBEntity.class), taId);
			conn.commit();
			return lcTerminalAreasData;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void deleteByAreaId(long terminalId, int... originalAreaId) {
		try {
			Object[] obj = new Object[originalAreaId.length + 1];
			StringBuilder builder = new StringBuilder();
			builder.append("DELETE FROM LC_TERMINAL_AREA_DATA WHERE EXISTS ( SELECT TA_ID FROM LC_TERMINAL_AREA  WHERE LC_TERMINAL_AREA.ta_id=LC_TERMINAL_AREA_DATA.ta_id");
			for (int i = 0; i < originalAreaId.length; i++) {
				if (i == 0) {
					builder.append(" and LC_TERMINAL_AREA.ORIGINAL_AREA_ID=?");
				} else {
					builder.append(" OR LC_TERMINAL_AREA.ORIGINAL_AREA_ID=?");
				}
				obj[i] = originalAreaId[i];
			}
			builder.append(" AND LC_TERMINAL_AREA.TERMINAL_ID=?)");
			obj[originalAreaId.length] = terminalId;
			super.executeUpdate(builder.toString().toUpperCase(), obj);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		LcTerminalAreaDataDaoImpl d = new LcTerminalAreaDataDaoImpl();
		d.deleteByAreaId(1, 1, 11, 1);
	}
}

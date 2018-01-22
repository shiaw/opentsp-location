package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDataDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalAreaDataDao2;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaData;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;


public class LcTerminalAreaDataDaoImpl2 extends BaseDaoImpl<LcTerminalAreaDataDBEntity>
		implements LcTerminalAreaDataDao2 {

	@Override
	public List<LcTerminalAreaDataDBEntity> getTerminalAreaData(int taId)throws Exception {
		try {
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			String sql = "SELECT   AREA_DATA.TAD_ID,AREA_DATA.TA_ID,AREA_DATA.DATA_SN,AREA_DATA.DATA_STATUS,AREA_DATA.LATITUDE,AREA_DATA.LONGITUDE,AREA_DATA.RADIUS_LEN FROM LC_TERMINAL_AREA_DATA AREA_DATA  WHERE   AREA_DATA.TA_ID=?";
			List<LcTerminalAreaDataDBEntity> lcTerminalAreasData = qryRun.query(conn,
					sql, new BeanListHandler<LcTerminalAreaDataDBEntity>(
							LcTerminalAreaDataDBEntity.class), taId);
//			conn.commit();
			return lcTerminalAreasData;
		} catch (SQLException e) {
//			e.printStackTrace();
//			return null;
			throw e;
		}

	}

	@Override
	public void deleteByAreaId(long terminalId, int... originalAreaId) throws Exception {
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
//			e.printStackTrace();
			throw e;
		}

	}

	public static void main(String[] args) {
		LcTerminalAreaDataDaoImpl2 d = new LcTerminalAreaDataDaoImpl2();
		//d.deleteByAreaId(1, 1, 11, 1);
	}

	@Override
	public void saveAreaData(List<LCAreaData.AreaData> areaDataList, int taId) throws Exception {
		// 新增区域数据
		try {
			String delteSql = "delete from LC_TERMINAL_AREA_DATA where ta_id=? ";
			super.executeUpdate2(delteSql, taId);
			String sql_data = "insert into LC_TERMINAL_AREA_DATA (TA_ID,DATA_SN,DATA_STATUS,RADIUS_LEN,LATITUDE,LONGITUDE) values (?,?,?,?,?,?)";
			List<LCAreaData.AreaData> areaDatas = areaDataList;
			Object[][] batchParams = new Object[areaDatas.size()][];
			for (int i = 0; i < areaDatas.size(); i++) {
				LCAreaData.AreaData areaData = areaDatas.get(i);
				Object[] params = new Object[6];
				params[0] = taId;
				params[1] = areaData.getDataSN();
				params[2] = areaData.getDataStatus();
				params[3] = areaData.getRadiusLength();
				params[4] = areaData.getLatitude();
				params[5] = areaData.getLongitude();
				batchParams[i] = params;
			}
			batchUpdate(sql_data, batchParams);
		} catch (SQLException e) {
//			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deleteByTaId(Integer taId) throws Exception {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("DELETE FROM LC_TERMINAL_AREA_DATA WHERE TA_ID = ?");

			super.executeUpdate(builder.toString().toUpperCase(), taId);
		} catch (Exception e) {
//			e.printStackTrace();
			 throw e;
		}
	}
}

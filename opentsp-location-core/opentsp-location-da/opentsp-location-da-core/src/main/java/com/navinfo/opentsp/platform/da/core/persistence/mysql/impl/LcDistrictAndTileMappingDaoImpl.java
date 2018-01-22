package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDistrictAndTileMappingDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcDistrictAndTileMappingDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LcDistrictAndTileMappingDaoImpl extends BaseDaoImpl<LcDistrictAndTileMappingDBEntity>
		implements LcDistrictAndTileMappingDao {
	private Logger logger = LoggerFactory.getLogger(LcDistrictAndTileMappingDaoImpl.class);


	@Override
	public List<LcDistrictAndTileMappingDBEntity> getByZoom(Integer zoom) {
		List<LcDistrictAndTileMappingDBEntity> list = new ArrayList<LcDistrictAndTileMappingDBEntity>();
		try {
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();

			StringBuilder builder = new StringBuilder();
			builder.append("SELECT mapping.TILE_ID,mapping.DISTRICT_ID,mapping.PARENT_DISTRICT_ID from LC_DISTRICT_AND_TILE_MAPPING mapping where  mapping.TILE_ID like ? ");
			list = qryRun.query(conn, builder.toString(),
					new BeanListHandler<LcDistrictAndTileMappingDBEntity>(
							LcDistrictAndTileMappingDBEntity.class),new StringBuilder().append(String.valueOf(zoom)).append("%").toString());
			return list;
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
//			e.printStackTrace();
		}finally {
			MySqlConnPoolUtil.close();
		}
		return list;
	}

	@Override
	public List<LcDistrictAndTileMappingDBEntity> getDistrictAndTileMappingList() {
		List<LcDistrictAndTileMappingDBEntity> list = new ArrayList<LcDistrictAndTileMappingDBEntity>();
		try {
			QueryRunner qryRun = new QueryRunner();
			MySqlConnPoolUtil.startTransaction();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT mapping.TILE_ID,mapping.DISTRICT_ID,mapping.PARENT_DISTRICT_ID from LC_DISTRICT_AND_TILE_MAPPING mapping ");
			list = qryRun.query(conn, builder.toString(),
					new BeanListHandler<LcDistrictAndTileMappingDBEntity>(
							LcDistrictAndTileMappingDBEntity.class));
			return list;
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
//			e.printStackTrace();
		}finally {
			MySqlConnPoolUtil.close();
		}
		return list;
	}
	@Override
	public List<LcDistrictAndTileMappingDBEntity> getByDistrictId(
			Integer districtId) {
		List<LcDistrictAndTileMappingDBEntity> list = new ArrayList<LcDistrictAndTileMappingDBEntity>();
		try {
			QueryRunner qryRun = new QueryRunner();
			MySqlConnPoolUtil.startTransaction();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT mapping.TILE_ID,mapping.DISTRICT_ID,mapping.PARENT_DISTRICT_ID from LC_DISTRICT_AND_TILE_MAPPING mapping where mapping.DISTRICT_ID=? ");
			list = qryRun.query(conn, builder.toString(),
					new BeanListHandler<LcDistrictAndTileMappingDBEntity>(
							LcDistrictAndTileMappingDBEntity.class),districtId);
			conn.commit();
			return list;
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
//			e.printStackTrace();
		}finally {
			MySqlConnPoolUtil.close();
		}
		return list;
	}
	/**
	 * 临时测试需要只加载北京的行政区域瓦片——hk
	 */
	@Override
	public List<LcDistrictAndTileMappingDBEntity> getDistrictAndTileMappingListPage(int pageNum, int pageSize) {
		List<LcDistrictAndTileMappingDBEntity> list = new ArrayList<LcDistrictAndTileMappingDBEntity>();
		try {
			QueryRunner qryRun = new QueryRunner();
			MySqlConnPoolUtil.startTransaction();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			StringBuilder builder = new StringBuilder();
			int beginNum = (pageNum-1)*pageSize;
			builder.append(
					"SELECT mapping.TILE_ID,mapping.DISTRICT_ID,mapping.PARENT_DISTRICT_ID from LC_DISTRICT_AND_TILE_MAPPING mapping  limit "
							+ beginNum + "," + pageSize);
			list = qryRun.query(conn, builder.toString(),
					new BeanListHandler<LcDistrictAndTileMappingDBEntity>(
							LcDistrictAndTileMappingDBEntity.class));
			return list;
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
//			e.printStackTrace();
		}finally {
			MySqlConnPoolUtil.close();
		}
		return list;
	}
}

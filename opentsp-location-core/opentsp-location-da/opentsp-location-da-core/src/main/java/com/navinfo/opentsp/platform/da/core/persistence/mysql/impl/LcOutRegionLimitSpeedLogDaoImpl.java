package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcOutRegionLimitSpeedLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcOutRegionLimitSpeedLogDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LcOutRegionLimitSpeedLogDaoImpl extends BaseDaoImpl<LcOutRegionLimitSpeedLogDBEntity>
		implements LcOutRegionLimitSpeedLogDao {

	Logger logger = LoggerFactory.getLogger(LcOutRegionLimitSpeedLogDaoImpl.class);

	@Override
	public List<LcOutRegionLimitSpeedLogDBEntity> queryLogList(
			long terminalId, int areaId, long start, long end,int currentPage,int pageSize) {
		try {
			StringBuilder builder = new StringBuilder("select  * from LC_OUTREGION_LIMIT_SPEED_LOG where 1=1 ");
			if(terminalId != 0)
				builder.append(" and  TERMINAL_ID="+terminalId);
			if(areaId != 0)
				builder.append(" and  ORIGINAL_AREA_ID="+areaId);
			if(start !=0 )
				builder.append(" and  LIMIT_DATE >= "+start);
			if( end != 0)
				builder.append(" and  LIMIT_DATE <= "+end);
			builder.append(" order by LIMIT_DATE desc");
			if(currentPage<=0){
				currentPage = 1;
			}
			int beginRecord = (currentPage-1)*pageSize;//开始记录
			int endRecord = pageSize;//从开始到结束的记录数
			builder.append(" limit "+beginRecord+","+endRecord+"");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcOutRegionLimitSpeedLogDBEntity>  list = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcOutRegionLimitSpeedLogDBEntity>(LcOutRegionLimitSpeedLogDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public int getCount(long terminalId, int areaId, long start, long end) {
		try {
			StringBuilder builder = new StringBuilder("select COUNT(ID) from LC_OUTREGION_LIMIT_SPEED_LOG where 1=1 ");
			if(terminalId != 0)
				builder.append(" and  TERMINAL_ID="+terminalId);
			if(areaId != 0)
				builder.append(" and  ORIGINAL_AREA_ID="+areaId);
			if(start !=0 )
				builder.append(" and  LIMIT_DATE >= "+start);
			if( end != 0)
				builder.append(" and  LIMIT_DATE <= "+end);

			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			long list = qryRun.query(conn, builder.toString().toUpperCase(),
					new ScalarHandler<Long>());
			return (int) list;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
}

package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcMessageBroadcastLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcMessageBroadcastLogDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LcMessageBroadcastLogDaoImpl extends BaseDaoImpl<LcMessageBroadcastLogDBEntity>
		implements LcMessageBroadcastLogDao {

	Logger logger = LoggerFactory.getLogger(LcMessageBroadcastLogDaoImpl.class);

	@Override
	public List<LcMessageBroadcastLogDBEntity> queryLogList(
			long terminalId, int areaId, long start, long end,int currentPage,int pageSize) {
		try {
			StringBuilder builder = new StringBuilder("select  * from LC_MESSAGE_BROADCAST_LOG where 1=1 ");
			if(terminalId != 0)
				builder.append(" and  TERMINAL_ID="+terminalId);
			if(areaId != 0)
				builder.append(" and  ORIGINAL_AREA_ID="+areaId);
			if(start !=0 )
				builder.append(" and  BROADCAST_TIME >= "+start);
			if( end != 0)
				builder.append(" and  BROADCAST_TIME <= "+end);
			builder.append(" order by BROADCAST_TIME desc");
			if(currentPage<=0){
				currentPage = 1;
			}
			int beginRecord = (currentPage-1)*pageSize;//开始记录
			int endRecord = pageSize;//从开始到结束的记录数
			builder.append(" limit "+beginRecord+","+endRecord+"");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcMessageBroadcastLogDBEntity>  list = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcMessageBroadcastLogDBEntity>(LcMessageBroadcastLogDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
}

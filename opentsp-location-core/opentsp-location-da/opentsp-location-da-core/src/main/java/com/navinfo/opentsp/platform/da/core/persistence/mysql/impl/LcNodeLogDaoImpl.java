package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcNodeLogDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


public class LcNodeLogDaoImpl extends BaseDaoImpl<LcNodeLogDBEntity> implements LcNodeLogDao {

	@Override
	public List<LcNodeLogDBEntity> findNodeLogByCode(int nodeCode,
													 long beginTime, long endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcNodeLogDBEntity> findNodeLogByType(int nodeType,
													 long beginTime, long endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcNodeLogDBEntity> findNodeLogByIp(String nodeIp,
												   long beginTime, long endTime) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 根据dbutil通用类，查询结果数量
	 */
	@SuppressWarnings({ "rawtypes" })
	private ScalarHandler scalarHandler = new ScalarHandler() {
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			Object obj = super.handle(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();
			return obj;
		}
	};
	@SuppressWarnings("unchecked")
	@Override
	public int queryNodeLogCount(int nodecode, long start,
								 long end, int logDistrict) {
		int count=0;
		try {
			StringBuilder builder = new StringBuilder("select count(*)  from  LC_NODE_LOG node  where 1=1 ");
			if(nodecode != 0)
				builder.append(" and  node.NODE_CODE="+nodecode);
			if(logDistrict !=0)
				builder.append(" and  node.LOG_DISTRICT  ="+logDistrict);
			if(start !=0 )
				builder.append(" and  node.LOG_TIME >= "+start);
			if( end != 0)
				builder.append(" and  node.LOG_TIME <= "+end);

			builder.append(" order by node.LOG_TIME desc");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			Number logCount = 0;
			logCount = (Number)qryRun.query(conn,builder.toString(), scalarHandler);
			count = (int)logCount.longValue();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	@Override
	public List<LcNodeLogDBEntity> queryNodeLogList(int nodecode, long start,
													long end, int logDistrict,int currentPage,int pageSize) {
		try {
			StringBuilder builder = new StringBuilder("select node.NL_ID,node.NODE_CODE,node.LOG_TIME,node.LOG_DISTRICT,node.LOG_CONTENT,node.LOG_TYPECODE,node.LOG_IP  from  LC_NODE_LOG node  where 1=1 ");
			if(nodecode != 0)
				builder.append(" and  node.NODE_CODE="+nodecode);
			if(logDistrict !=0)
				builder.append(" and  node.LOG_DISTRICT  ="+logDistrict);
			if(start !=0 )
				builder.append(" and  node.LOG_TIME >= "+start);
			if( end != 0)
				builder.append(" and  node.LOG_TIME <= "+end);

			builder.append(" order by node.LOG_TIME desc");
			if(currentPage>0){
				int beginRecord = (currentPage-1)*pageSize;//开始记录
				int endRecord = pageSize;//从开始到结束的记录数
				builder.append("  limit "+beginRecord+","+endRecord+"");
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcNodeLogDBEntity>  list = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcNodeLogDBEntity>(LcNodeLogDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}



}

package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcServiceLogDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


public class LcServiceLogDaoImpl extends BaseDaoImpl<LcServiceLogDBEntity> implements LcServiceLogDao {

	@Override
	public List<LcServiceLogDBEntity> queryServiceLogList(long start, long end,
														  int logDistrict, String logIp, int typeCode,int currentPage,int pageSize) {
		try {
			StringBuilder builder = new StringBuilder("select LC_SERVICE_LOG.SL_ID,LC_SERVICE_LOG.LOG_TIME,LC_SERVICE_LOG.LOG_IP,LC_SERVICE_LOG.LOG_CONTENT,LC_SERVICE_LOG.LOG_DISTRICT,LC_SERVICE_LOG.LOG_TYPECODE from LC_SERVICE_LOG where 1=1");
			if(logIp!=null&&!"".equals(logIp))
				builder.append(" and  LC_SERVICE_LOG.LOG_IP="+logIp);
			if(logDistrict!=0)
				builder.append(" and  LC_SERVICE_LOG.LOG_DISTRICT ="+logDistrict);
			if(typeCode!=0)
				builder.append(" and  LC_SERVICE_LOG.LOG_TYPECODE = "+typeCode);
			if(start !=0 )
				builder.append(" and  LC_SERVICE_LOG.LOG_TIME >= "+start);
			if( end != 0)
				builder.append(" and  LC_SERVICE_LOG.LOG_TIME <= "+end);

			builder.append(" order by LC_SERVICE_LOG.LOG_TIME desc");
			if(currentPage>0){
				int beginRecord = (currentPage-1)*pageSize;//开始记录
				int endRecord = pageSize;//从开始到结束的记录数
				builder.append("  limit "+beginRecord+","+endRecord+"");
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcServiceLogDBEntity>  list = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcServiceLogDBEntity>(LcServiceLogDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
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
	public int queryServiceLogCount(long start, long end,
									int logDistrict, String logIp, int typeCode) {
		int count = 0;
		try {
			StringBuilder builder = new StringBuilder("select count(*) from LC_SERVICE_LOG where 1=1");
			if(logIp!=null&&!"".equals(logIp))
				builder.append(" and  LC_SERVICE_LOG.LOG_IP="+logIp);
			if(logDistrict!=0)
				builder.append(" and  LC_SERVICE_LOG.LOG_DISTRICT ="+logDistrict);
			if(typeCode!=0)
				builder.append(" and  LC_SERVICE_LOG.LOG_TYPECODE = "+typeCode);
			if(start !=0 )
				builder.append(" and  LC_SERVICE_LOG.LOG_TIME >= "+start);
			if( end != 0)
				builder.append(" and  LC_SERVICE_LOG.LOG_TIME <= "+end);
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			Number logCount =0;
			logCount = (Number)qryRun.query(conn,builder.toString(), scalarHandler);
			count =(int)logCount.longValue();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}


}

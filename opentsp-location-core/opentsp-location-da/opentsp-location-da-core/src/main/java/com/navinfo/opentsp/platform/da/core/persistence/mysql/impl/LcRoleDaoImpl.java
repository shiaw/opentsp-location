package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcRoleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcRoleDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


public class LcRoleDaoImpl extends BaseDaoImpl<LcRoleDBEntity> implements LcRoleDao {

	@Override
	public LcRoleDBEntity getRoleInfo(String role_name) {
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
	public int queryRoleCount(String role_name,int flag){
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		int count = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("select count(*) from LC_ROLE   ");
		if(flag==1){
			builder.append(" WHERE LC_ROLE.ROLE_NAME like '%"+role_name+"%'");
		}else{
			builder.append(" WHERE LC_ROLE.ROLE_NAME = '"+role_name+"'");
		}

		try {
			Number roleCount = 0;
			roleCount = (Number)qryRun.query(conn,builder.toString(),scalarHandler);
			count = (int)roleCount.longValue();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	@Override
	public List<LcRoleDBEntity> queryRoleList(String role_name,int currentPage,int pageSize) {
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		builder.append("select ROLE_ID,ROLE_NAME,DESCRIPTION from LC_ROLE   ");
		builder.append(" WHERE LC_ROLE.ROLE_NAME like '%"+role_name+"%'");

		if(currentPage>0){
			int beginRecord = (currentPage-1)*pageSize;//开始记录
			int endRecord = pageSize;//从开始到结束的记录数
			builder.append("  limit "+beginRecord+","+endRecord+"");
		}
		try {
			List<LcRoleDBEntity> lcRoleDBEntity =null;
			lcRoleDBEntity=qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcRoleDBEntity>(
							LcRoleDBEntity.class));

			return lcRoleDBEntity;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}




}

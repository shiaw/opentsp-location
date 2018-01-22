package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcUserDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcUserDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


public class LcUserDaoImpl extends BaseDaoImpl<LcUserDBEntity> implements LcUserDao {

	@Override
	public LcUserDBEntity getUserInfo(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcUserDBEntity> getUserInfo(int role_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcUserDBEntity> getUserInfoByStatus(int data_status) {
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
	public int queryUserCount(String userName, String password,int roleId){
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		int userCount=0;
		builder.append("select count(*) from LC_USER users   where 1=1");
		if(userName!=null ){
			if(roleId==1){
				builder.append(" and binary users.USERNAME like '%"+userName+"%'");
			}else{
				builder.append(" and binary users.USERNAME ='"+userName+"'");
			}

		}
		if(password!=null){
			builder.append(" and binary users.PASSWORD ='"+password+"'");
		}
		builder.append(" order by users.CREATE_TIME desc");
		try {
			Number count = 0;
			count = (Number)qryRun.query(conn,builder.toString(),scalarHandler);
			userCount = (int)count.longValue();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userCount;
	}
	@Override
	public List<LcUserDBEntity> querUserList(String userName, String password,int roleId,int currentPage,int pageSize) {
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		builder.append("select users.USER_ID,users.USERNAME,users.PASSWORD,users.CREATE_TIME,users.ROLE_ID");
		builder.append(" from LC_USER users   where 1=1");
		if(userName!=null ){
			if(roleId==1){
				builder.append(" and binary users.USERNAME like '%"+userName+"%'");
			}else{
				builder.append(" and binary users.USERNAME ='"+userName+"'");
			}

		}
		if(password!=null){
			builder.append(" and binary users.PASSWORD ='"+password+"'");
		}
		builder.append(" order by users.CREATE_TIME desc");

		if(currentPage>0){
			int beginRecord = (currentPage-1)*pageSize;//开始记录
			int endRecord = pageSize;//从开始到结束的记录数
			builder.append("  limit "+beginRecord+","+endRecord+"");
		}
		try {
			List<LcUserDBEntity> lcUserDBEntities =null;
			lcUserDBEntities=qryRun.query(conn, builder.toString(),
					new BeanListHandler<LcUserDBEntity>(
							LcUserDBEntity.class));
			return lcUserDBEntities;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public PlatformResponseResult updateUserPasswod(String userId,
													String password) {
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		builder.append("update LC_USER set PASSWORD = ?     where USER_ID =?");
		try {
			int updateCount = qryRun.update(conn,builder.toString(), new Object [] {password,Integer.parseInt(userId)});
			if(updateCount>0)
				MySqlConnPoolUtil.commit();
			return PlatformResponseResult.success;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return PlatformResponseResult.failure;
	}



}

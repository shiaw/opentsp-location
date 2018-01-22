package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcFuncDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcFuncDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


public class LcFuncDaoImpl extends BaseDaoImpl<LcFuncDBEntity> implements LcFuncDao {

	@Override
	public LcFuncDBEntity getFuncInfo(String func_code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcFuncDBEntity> getFuncInfoByName(String func_name) {
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
	public int queryFuncCount(String funcName){
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		int count = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("select count(*)  from LC_FUNC  ");
		builder.append(" WHERE FUNC_NAME like '%"+funcName+"%'");
		builder.append(" ORDER BY FUNC_ID ");
		try {
			Number funcCount =0;
			funcCount = (Number)qryRun.query(conn,builder.toString(),scalarHandler);
			count = (int)funcCount.longValue();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	@Override
	public List<LcFuncDBEntity> queryFuncList(String funcName,int currentPage,int pageSize){
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		builder.append("select FUNC_ID,FUNC_CODE,FUNC_NAME,PARENT_FUNC    from LC_FUNC  ");
		builder.append(" WHERE FUNC_NAME like '%"+funcName+"%'");
		builder.append(" ORDER BY FUNC_ID ");
		if(currentPage>0){
			int beginRecord = (currentPage-1)*pageSize;//开始记录
			int endRecord = pageSize;//从开始到结束的记录数
			builder.append("  LIMIT "+beginRecord+","+endRecord+"");
		}
		try {
			List<LcFuncDBEntity> lcFuncDBEntity =null;
			lcFuncDBEntity=qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcFuncDBEntity>(
							LcFuncDBEntity.class));
			return lcFuncDBEntity;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<LcFuncDBEntity> queryRoleFuncList(int roleId) {
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		builder.append("select func.FUNC_ID,func.FUNC_CODE,func.FUNC_NAME ,func.PARENT_FUNC from LC_ROLE role inner join LC_ROLE_FUNC role_func on role.ROLE_ID=role_func.ROLE_ID inner join LC_FUNC func on role_func.FUNC_ID=func.FUNC_ID");
		if(roleId!=0){
			builder.append(" WHERE role.ROLE_ID=?");
		}
		try {
			List<LcFuncDBEntity> lcFuncDBEntity =null;
			if(roleId!=0 ){
				lcFuncDBEntity=qryRun.query(conn, builder.toString().toUpperCase(),
						new BeanListHandler<LcFuncDBEntity>(
								LcFuncDBEntity.class),roleId);
			}else {
				lcFuncDBEntity=qryRun.query(conn, builder.toString().toUpperCase(),
						new BeanListHandler<LcFuncDBEntity>(
								LcFuncDBEntity.class));
			}
			return lcFuncDBEntity;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<LcFuncDBEntity> selectFuncByParent_func(int parent_func) {
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		builder.append("select FUNC_ID,FUNC_CODE,FUNC_NAME,PARENT_FUNC    from LC_FUNC ");
		builder.append(" WHERE parent_func=?");
		try {
			List<LcFuncDBEntity> lcFuncDBEntity =null;
			lcFuncDBEntity=qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcFuncDBEntity>(
							LcFuncDBEntity.class),parent_func);
			return lcFuncDBEntity;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

}

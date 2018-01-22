package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import com.navinfo.opentsp.platform.da.core.persistence.common.DBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMap;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMapException;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcServiceConfigDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class LcServiceConfigDaoImpl extends BaseDaoImpl<LcServiceConfigDBEntity> implements LcServiceConfigDao{

	@Override
	public LcServiceConfigDBEntity getServiceConfig(String auth_name) {
		try {
			LcServiceConfigDBEntity lcServiceConfig = new LcServiceConfigDBEntity();
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(lcServiceConfig);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到有效属性
			String fields = entity.fieldNamesToString();
			// 拼接主键
			fields += "," + entity.getPrimaryKeyName();
			StringBuilder builder = new StringBuilder();
			builder.append("select ");
			builder.append(fields);
			builder.append(" from ");
			builder.append(tableName);
			if(StringUtils.isNotBlank(auth_name)){
				builder.append(" where AUTH_NAME = '"+auth_name+"'");
			}
			return super.queryForObject(builder.toString(), LcServiceConfigDBEntity.class);
		} catch (ObjectForMapException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public LcServiceConfigDBEntity getServiceConfigByIp(String ip) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcServiceConfigDBEntity> getServiceConfigByUpdateUser(
			String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcServiceConfigDBEntity> getServiceConfig(int stats) {
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
	public int queryServiceConfigResCount(String userName,
										  String ip, int district, long start, long end,int flag){
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		int count=0;
		builder.append("select count(*) from LC_SERVICE_CONFIG  where DATA_STATUS = 1");
		if(start!=0){
			builder.append(" and CREATE_TIME >="+start+" ");
		}
		if(end!=0){
			builder.append(" and CREATE_TIME <="+end+" ");
		}
		if(StringUtils.isNotBlank(userName)){
			if(flag==1){
				builder.append(" and AUTH_NAME  like '%"+userName+"%' ");
			}else{
				builder.append(" and AUTH_NAME  = '"+userName+"' ");
			}

		}
		if(StringUtils.isNotBlank(ip)){
			builder.append(" and IP_ADDRESS like '%"+ip+"%' ");
		}
		if(district!=0){
			builder.append(" and sc_id in (Select sc_id from LC_SERVICE_DISTRICT_CONFIG where SERVICE_DISTRICT='"+district+"')");
		}
		builder.append(" ORDER BY CREATE_TIME DESC");
		try {
			Number serviceCount = 0;
			serviceCount = (Number)qryRun.query(conn, builder.toString(), scalarHandler);
			count = (int)serviceCount.longValue();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<LcServiceConfigDBEntity> queryServiceConfigRes(String userName,
															   String ip, int district, long start, long end,int flag,int currentPage,int pageSize) {
		try {
			LcServiceConfigDBEntity lcServiceConfig = new LcServiceConfigDBEntity();
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(lcServiceConfig);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到有效属性
			String fields = entity.fieldNamesToString();
			// 拼接主键
			fields += "," + entity.getPrimaryKeyName();
			StringBuilder builder = new StringBuilder();
			builder.append("select ");
			builder.append(fields);
			builder.append(" from ");
			builder.append(tableName);
			if(flag==1){
				builder.append(" where 1= 1 ");
			}else{
				builder.append(" where DATA_STATUS = 1");
			}
			if(start!=0){
				builder.append(" and CREATE_TIME >="+start+" ");
			}
			if(end!=0){
				builder.append(" and CREATE_TIME <="+end+" ");
			}
			if(flag==1){
				if(StringUtils.isNotBlank(userName)){
					builder.append(" and AUTH_NAME = '"+userName+"' ");
				}
			}else{
				if(StringUtils.isNotBlank(userName)){
					builder.append(" and AUTH_NAME like '%"+userName+"%' ");
				}
			}
			if(StringUtils.isNotBlank(ip)){
				builder.append(" and IP_ADDRESS like '%"+ip+"%' ");
			}
			if(district!=0){
				builder.append(" and sc_id in (Select sc_id from LC_SERVICE_DISTRICT_CONFIG where SERVICE_DISTRICT='"+district+"')");
			}
			builder.append(" ORDER BY CREATE_TIME DESC");
			if(currentPage>0){
				int beginRecord = (currentPage-1)*pageSize;//开始记录
				int endRecord = pageSize;//从开始到结束的记录数
				builder.append("  limit "+beginRecord+","+endRecord+"");
			}
			return super.queryForList(builder.toString(), LcServiceConfigDBEntity.class);
		} catch (ObjectForMapException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int add(LcServiceConfigDBEntity t) throws SQLException {
		try {
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(t);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到所有属性
			String fields = entity.fieldNamesToString();
			// 得到属性的值
			Object[] values = entity.fieldValues();
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("INSERT INTO ");
			builder.append(tableName);
			builder.append("(" + fields.toUpperCase());
			builder.append(") VALUES(");
			for (int i = 0; i < values.length; i++) {
				if (i == 0) {
					builder.append("?");
				} else {
					builder.append(",?");
				}
			}
			builder.append(")");
			// 执行sql
			executeUpdate(builder.toString(), values);
			//添加返回主键信息
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			int sc_id = Integer.parseInt(qryRun.query(conn, "SELECT LAST_INSERT_ID()",
					new ScalarHandler(1)).toString());
			return sc_id;
		} catch (ObjectForMapException e) {
			e.printStackTrace();
			return PlatformResponseResult.failure_VALUE;
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int updatebyScid(LcServiceConfigDBEntity serviceConfig) {
		try {
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(serviceConfig);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到所有有效属性
			String[] fields = entity.fieldNames();
			// 得到有效属性的值
			Object[] values = entity.fieldValues();
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("UPDATE ");
			builder.append(tableName.toUpperCase());
			builder.append(" SET ");
			for (int i = 0; i < fields.length; i++) {
				if (i == 0) {
					builder.append(fields[i].toUpperCase() + "=?");
				} else {
					builder.append("," + fields[i].toUpperCase() + "=?");
				}
			}
			builder.append(" WHERE SC_ID=?");
			// 执行sql
			Object[] objValues=new Object[values.length+1];
			for (int i = 0; i <= values.length; i++) {
				if(i==values.length){
					objValues[i]=serviceConfig.getSc_id();
				}else {
					objValues[i]=values[i];
				}
			}
			executeUpdate(builder.toString(),objValues );
			//添加返回主键信息
			QueryRunner qryRun = new QueryRunner();
			//Connection conn = MySqlConnPoolUtil.getContainer().get();
			int sc_id = serviceConfig.getSc_id();//Integer.parseInt(qryRun.query(conn, "SELECT LAST_INSERT_ID()",new ScalarHandler(1)).toString());
			return sc_id;
		} catch (ObjectForMapException | SQLException e) {
			e.printStackTrace();
			return PlatformResponseResult.failure_VALUE;
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int updatebyAuthName(LcServiceConfigDBEntity serviceConfig) {
		try {
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(serviceConfig);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到所有有效属性
			String[] fields = entity.fieldNames();
			// 得到有效属性的值
			Object[] values = entity.fieldValues();
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("UPDATE ");
			builder.append(tableName.toUpperCase());
			builder.append(" SET ");
			for (int i = 0; i < fields.length; i++) {
				if (i == 0) {
					builder.append(fields[i].toUpperCase() + "=?");
				} else {
					builder.append("," + fields[i].toUpperCase() + "=?");
				}
			}
			builder.append(" WHERE AUTH_NAME=?");
			// 执行sql
			Object[] objValues=new Object[values.length+1];
			for (int i = 0; i <= values.length; i++) {
				if(i==values.length){
					objValues[i]=serviceConfig.getAuth_name();
				}else {
					objValues[i]=values[i];
				}
			}
			executeUpdate(builder.toString(),objValues );
			//添加返回主键信息
			QueryRunner qryRun = new QueryRunner();
			//Connection conn = MySqlConnPoolUtil.getContainer().get();
			int sc_id = serviceConfig.getSc_id();//Integer.parseInt(qryRun.query(conn, "SELECT LAST_INSERT_ID()",new ScalarHandler(1)).toString());
			return sc_id;
		} catch (ObjectForMapException | SQLException e) {
			e.printStackTrace();
			return PlatformResponseResult.failure_VALUE;
		}
	}
	@SuppressWarnings("rawtypes")
	@Override
	public int delete(Class clazz, int[] id) throws SQLException {
		try {
			LcServiceConfigDBEntity obj = (LcServiceConfigDBEntity) clazz.newInstance();
			DBEntity entity = ObjectForMap.dbObjectForMap(obj);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到主键
			String primaryKey = entity.getPrimaryKeyName();
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("update ");
			builder.append(tableName.toUpperCase());
			builder.append(" set DATA_STATUS=0 ");
			builder.append(" WHERE ");
			builder.append(primaryKey.toUpperCase());
			builder.append(" IN (");
			for (int i = 0; i < id.length; i++) {
				if (i == 0) {
					builder.append(id[i]);
				} else {
					builder.append("," + id[i]);
				}
			}
			builder.append(")");
			// 执行sql
			executeUpdate(builder.toString().toUpperCase());
			return PlatformResponseResult.success_VALUE;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ObjectForMapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return PlatformResponseResult.failure_VALUE;
	}

	@Override
	public void deleteByAuthName(String authName) {

		try {
			LcServiceConfigDBEntity obj = new LcServiceConfigDBEntity();
			DBEntity entity = ObjectForMap.dbObjectForMap(obj);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到主键
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("update ");
			builder.append(tableName.toUpperCase());
			builder.append(" set DATA_STATUS=0 ");
			builder.append(" WHERE ");
			builder.append(" AUTH_NAME=?");
			// 执行sql
			executeUpdate(builder.toString().toUpperCase(), authName);
		}  catch (ObjectForMapException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}


}

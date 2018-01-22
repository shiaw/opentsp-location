package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.common.DBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMap;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.BaseDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;



public class BaseDaoImpl<T> implements BaseDao<T> {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> queryForList(String sql, Class clazz, Object... args)
			throws SQLException {
		try {
			/* sql：要执行的sql;args:用到的参数 */
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<T> list = qryRun.query(conn, sql,
					new BeanListHandler<T>(clazz), args);
//			conn.commit();
			return list;
		} catch (SQLException e) {
			throw e;
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public T queryForObject(String sql, Class clazz, Object... args)
			throws SQLException {
		try {
			/* sql：要执行的sql;args:用到的参数 */
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			T t = qryRun.query(conn, sql, new BeanHandler<T>(clazz), args);
//			conn.commit();
			return t;
		} catch (SQLException e) {
			throw e;
		}
	}

	public void executeUpdate(String sql, Object... args) throws SQLException {
		try {
			/* sql：要执行的sql;args:用到的参数 */
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			qryRun.update(conn, sql, args);
//			conn.commit();
		} catch (SQLException e) {
			throw e;
		}
	}

	public int executeUpdate2(String sql, Object... args) throws SQLException {
		try {
			/* sql：要执行的sql;args:用到的参数 */
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			qryRun.update(conn, sql, args);
//			conn.commit();
			BigInteger id = qryRun.query(conn, "SELECT LAST_INSERT_ID()", new ScalarHandler<BigInteger>());
            return id.intValue();
		} catch (SQLException e) {
			throw e;
		}
	}

	public int[] batchUpdate(String sql, Object[][] objs) throws SQLException {
		int[] ids = null;
		try {
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			ids = qryRun.batch(conn, sql, objs);
//			conn.commit();
			return ids;
		} catch (SQLException e) {
			throw e;
		}

	}



	@Override
	public int update(T t) throws Exception {
		try {
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(t);
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
			builder.append(" WHERE " + entity.getPrimaryKeyName().toUpperCase()
					+ "=?");
			// 执行sql
			Object[] objValues=new Object[values.length+1];
			for (int i = 0; i < objValues.length; i++) {
				if(i==objValues.length-1){
					objValues[i]=entity.getPrimaryKeyValue();
				}else {
					objValues[i]=values[i];
				}
			}
			executeUpdate(builder.toString(),objValues );
			return LCPlatformResponseResult.PlatformResponseResult.success_VALUE;
		} catch (Exception e) {
//			e.printStackTrace();
//			return PlatformResponseResult.failure_VALUE;
			throw e;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int delete(Class clazz, int[] id) throws Exception {
		try {
			T obj = (T) clazz.newInstance();
			DBEntity entity = ObjectForMap.dbObjectForMap(obj);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到主键
			String primaryKey = entity.getPrimaryKeyName();
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("DELETE FROM ");
			builder.append(tableName.toUpperCase());
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
			executeUpdate(builder.toString());
			return LCPlatformResponseResult.PlatformResponseResult.success_VALUE;
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public T findById(int id, Class clazz) throws Exception {

		try {
			T obj = (T) clazz.newInstance();
			DBEntity entity = ObjectForMap.dbObjectForMap(obj);
			// 得到有效属性
			String fields = entity.fieldNamesToString();
			// 拼接主键
			fields += "," + entity.getPrimaryKeyName();
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT ");
			builder.append(fields.toUpperCase());
			builder.append(" FROM ");
			builder.append(entity.getTableName());
			builder.append(" where " + entity.getPrimaryKeyName().toUpperCase()
					+ "=?");
			return queryForObject(builder.toString().toUpperCase(), clazz, id);
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> findAll(Class clazz, int... param) throws Exception {
		// 先判断param是否为空，若不为空则查询结果分页，param[0]:起始位置，param[1]:结束位置
		try {
			T obj = (T) clazz.newInstance();
			DBEntity entity = ObjectForMap.dbObjectForMap(obj);
			// 得到有效属性
			String fields = entity.fieldNamesToString();
			// 拼接主键
			fields += "," + entity.getPrimaryKeyName();
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT ");
			builder.append(fields.toUpperCase());
			builder.append(" FROM ");
			builder.append(entity.getTableName());
			if (param != null && param.length > 0) {
				// 分页查询
				builder.append(" LIMIT ?,?");
				return queryForList(builder.toString(), clazz, param[0],
						param[1] - param[0]);
			} else {
				// 返回所有的
				return queryForList(builder.toString(), clazz);
			}
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public int add(T t) throws Exception {
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
			return LCPlatformResponseResult.PlatformResponseResult.success_VALUE;
		} catch (Exception e) {
//			e.printStackTrace();
//			return PlatformResponseResult.failure_VALUE;
			throw e;
		}
	}
}

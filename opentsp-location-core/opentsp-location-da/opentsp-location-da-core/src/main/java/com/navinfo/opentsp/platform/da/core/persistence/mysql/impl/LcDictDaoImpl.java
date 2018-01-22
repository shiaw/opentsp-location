package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.common.DBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMap;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMapException;
import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.DictData;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictTypeDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcDictDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;



public class LcDictDaoImpl extends BaseDaoImpl<LcDictDBEntity> implements
		LcDictDao {

	@Override
	public List<DictData> getDictDataRes(int dictType) {
		try {

			StringBuilder builder = new StringBuilder(
					"SELECT l.DICT_CODE dataCode,l.GB_CODE gbCode,l.DT_CODE dictType,l.DICT_NAME name,d.PARENT_CODE parentDataCode,l.DICT_DATA dictValue FROM LC_DICT l LEFT JOIN LC_DISTRICT d ON l.DICT_CODE=d.DICT_CODE WHERE l.DT_CODE=?");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<DictData> list = qryRun.query(conn, builder.toString()
					.toUpperCase(), new BeanListHandler<DictData>(
					DictData.class), dictType);
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<LcDictDBEntity> queryDictList(String dtName, int dictId) {
		try {
			StringBuilder builder = new StringBuilder(
					"select dict.DICT_ID,dict.DICT_CODE,dict.GB_CODE,dict.DT_CODE,dict.DICT_NAME,dict.DICT_DATA from LC_DICT dict inner join LC_DICT_TYPE dictType on dict.DT_CODE=dictType.DT_CODE where 1=1 ");
			if (dtName != null || "".equals(dtName)) {
				builder.append(" and ( dict.DICT_NAME = '" + dtName+ "' ");
			}
			if (dictId != 0) {
				builder.append(" OR  dict.DICT_CODE=" + dictId+")" );
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcDictDBEntity> list = qryRun.query(conn, builder.toString()
					.toUpperCase(), new BeanListHandler<LcDictDBEntity>(
					LcDictDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	@Override
	public List<LcDictTypeDBEntity> queryDictTypeList(String dtName, int dictId) {
		try {
			StringBuilder builder = new StringBuilder(
					"select dict.DT_CODE,dict.DICT_NAME from  LC_DICT_TYPE dict  where 1=1 ");
			if (dtName != null || "".equals(dtName)) {
				builder.append(" and ( dict.DICT_NAME = '" + dtName+ "' ");
			}
			if (dictId != 0) {
				builder.append(" OR  dict.DT_CODE=" + dictId+")" );
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcDictTypeDBEntity> list = qryRun.query(conn, builder.toString()
					.toUpperCase(), new BeanListHandler<LcDictTypeDBEntity>(
					LcDictTypeDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	@Override
	public int updateByDictCode(LcDictDBEntity lcDictDBEntity) {
		try {
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(lcDictDBEntity);
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
			builder.append(" WHERE DICT_CODE=?");
			// 执行sql
			Object[] objValues = new Object[values.length];
			for (int i = 0; i <= objValues.length; i++) {
				if (i == objValues.length) {
					objValues[i] = lcDictDBEntity.getDict_code();
				} else {
					objValues[i] = values[i];
				}
			}
			executeUpdate(builder.toString(), objValues);
			return LCPlatformResponseResult.PlatformResponseResult.success_VALUE;
		} catch (ObjectForMapException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return LCPlatformResponseResult.PlatformResponseResult.failure_VALUE;
	}

	@Override
	public List<LcDictDBEntity> getDictByType(int dictType,int currentPage,int pageSize) {
		try {
			String sql = "SELECT dict.DICT_ID, dict.DICT_CODE, dict.GB_CODE, dict.DT_CODE, dict.DICT_NAME,dict.DICT_DATA FROM LC_DICT dict";
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcDictDBEntity> list=null;
			if(dictType != 0){
				sql=sql+" WHERE dict.DT_CODE=?";
				if(currentPage>0){
					int beginRecord = (currentPage-1)*pageSize;//开始记录
					int endRecord = pageSize;//从开始到结束的记录数
					sql=sql+"  LIMIT "+beginRecord+","+endRecord+"";
				}
				list = qryRun.query(conn, sql.toUpperCase(),
						new BeanListHandler<LcDictDBEntity>(LcDictDBEntity.class),dictType);
			}else {
				if(currentPage>0){
					int beginRecord = (currentPage-1)*pageSize;//开始记录
					int endRecord = pageSize;//从开始到结束的记录数
					sql=sql+"  LIMIT "+beginRecord+","+endRecord+"";
				}
				list = qryRun.query(conn, sql.toUpperCase(),
						new BeanListHandler<LcDictDBEntity>(LcDictDBEntity.class));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int deleteByCode(int[] dictCode) {
		int result=0;
		try {
			StringBuilder builder = new StringBuilder(
					"DELETE  FROM LC_DICT  WHERE DICT_CODE=?");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			result= qryRun.update(conn, builder.toString()
					.toUpperCase(), new BeanListHandler<DictData>(
					DictData.class), dictCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public LcDictDBEntity getDictByCode(int dictCode) {
		try {
			String sql = "SELECT dict.DICT_ID, dict.DICT_CODE, dict.GB_CODE, dict.DT_CODE, dict.DICT_NAME,dict.DICT_DATA FROM LC_DICT dict";
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			LcDictDBEntity entity=null;
			if(dictCode != 0){
				sql=sql+" WHERE dict.DICT_CODE=?";
				entity = qryRun.query(conn, sql.toUpperCase(),
						new BeanHandler<LcDictDBEntity>(LcDictDBEntity.class),dictCode);
			}else {
				entity = qryRun.query(conn, sql.toUpperCase(),
						new BeanHandler<LcDictDBEntity>(LcDictDBEntity.class));
			}

			return entity;
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
	public int getDictDataResCount(int dictType) {
		try {
			String sql = "SELECT count(*) FROM LC_DICT dict";
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			int count = 0;
			if(dictType != 0){
				sql=sql+" WHERE dict.DT_CODE="+dictType;
			}
			Number dictCount =0;
			dictCount = (Number)qryRun.query(conn,sql,scalarHandler);
			count =(int)dictCount.longValue();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
}

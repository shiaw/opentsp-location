package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.common.DBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMap;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMapException;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictTypeDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcDictTypeDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;



public class LcDictTypeDaoImpl extends BaseDaoImpl<LcDictTypeDBEntity> implements LcDictTypeDao {

	@Override
	public LcDictTypeDBEntity getDictType(String role_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcDictTypeDBEntity> queryDictTypeList(String dtName) {
		try {
			StringBuilder builder=new StringBuilder("select dictType.DT_CODE,dictType.DICT_NAME,dictType.DICT_DESC from LC_DICT_TYPE dictType ");
			if( !"".equals(dtName)  && dtName!=null ){
				builder.append(" where dictType.DICT_NAME like '%"+dtName+"%'");
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcDictTypeDBEntity> list =new ArrayList<LcDictTypeDBEntity>();
				list = qryRun.query(conn, builder.toString().toUpperCase(),
						new BeanListHandler<LcDictTypeDBEntity>(LcDictTypeDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	@Override
	public int add(LcDictTypeDBEntity t) throws SQLException {
		try {
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(t);
			// 得到类注解
			String tableName = entity.getTableName();
			String primaryKey=entity.getPrimaryKeyName();
			// 得到所有属性
			String fields = entity.fieldNamesToString();
			// 得到属性的值
			Object[] values = entity.fieldValues();
			//属性拼接主键
			fields+=","+primaryKey;
			//主键值
			int primaryKeyValue=entity.getPrimaryKeyValue();
			//参数值
			Object[] objValues=new Object[values.length+1];
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("INSERT INTO ");
			builder.append(tableName);
			builder.append("(" + fields.toUpperCase());
			builder.append(") VALUES(");
			for (int i = 0; i <= values.length; i++) {
				if (i == 0) {
					builder.append("?");
					objValues[i]=values[i];
				} else {
					if(i==values.length){
						objValues[i]=primaryKeyValue;
					}else{
						objValues[i]=values[i];
					}
					builder.append(",?");
				}
			}
			builder.append(")");
			// 执行sql
			executeUpdate(builder.toString(), objValues);
			return LCPlatformResponseResult.PlatformResponseResult.success_VALUE;
		} catch (ObjectForMapException e) {
			e.printStackTrace();
			return LCPlatformResponseResult.PlatformResponseResult.failure_VALUE;
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
	public int queryDictTypeListCount(String dtName) {
		try {
			String sql = "select count(*) from LC_DICT_TYPE dictType ";
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			int count = 0;
			if( !"".equals(dtName)  && dtName!=null ){
				sql=sql+" where dictType.DICT_NAME like '%"+dtName+"%'";
			}
			Number dictCount =0;
			dictCount=(Number)qryRun.query(conn,sql, scalarHandler);
			count = (int)dictCount.longValue();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	@Override
	public List<LcDictTypeDBEntity> queryDictTypeList(String dtName,int currentPage,int pageSize) {
		try {
			StringBuilder builder=new StringBuilder("select dictType.DT_CODE,dictType.DICT_NAME,dictType.DICT_DESC from LC_DICT_TYPE dictType ");
			if( !"".equals(dtName)  && dtName!=null ){
				builder.append(" where dictType.DICT_NAME like '%"+dtName+"%'");
			}
			if(currentPage>0){
				int beginRecord = (currentPage-1)*pageSize;//开始记录
				int endRecord = pageSize;//从开始到结束的记录数
				builder.append("  limit "+beginRecord+","+endRecord+"");
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcDictTypeDBEntity> list =new ArrayList<LcDictTypeDBEntity>();
				list = qryRun.query(conn, builder.toString().toUpperCase(),
						new BeanListHandler<LcDictTypeDBEntity>(LcDictTypeDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	
	}


}

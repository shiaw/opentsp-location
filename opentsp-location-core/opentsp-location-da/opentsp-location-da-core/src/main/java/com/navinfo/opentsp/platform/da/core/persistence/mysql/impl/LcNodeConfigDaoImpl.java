package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.common.DBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcNodeConfigDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMap;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMapException;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeConfigDBEntity;
import com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;



public class LcNodeConfigDaoImpl extends BaseDaoImpl<LcNodeConfigDBEntity> implements LcNodeConfigDao {
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
	public int queryNodeConfigResCount(int district, int node_code,int nodetype){
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		int count=0;
		builder.append("select count(*) from LC_NODE_CONFIG  where DATA_STATUS = 1");
		if(node_code!=0){
			builder.append(" and NODE_CODE ="+node_code+" ");
		}
		if(district!=0){
			builder.append(" and DISTRICT ="+district+" ");
		}
		if(nodetype!=0){
			builder.append(" and NODE_TYPE="+nodetype+"");
		}
		builder.append(" ORDER BY LAST_UPDATE_TIME DESC");

		try {
			Number nodeCount =0;
			nodeCount = (Number)qryRun.query(conn,builder.toString(),scalarHandler);
			count =(int)nodeCount.longValue();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	@Override
	public List<LcNodeConfigDBEntity> getbyNodeCode(int node_code, int district,int nodeType,int currentPage,int pageSize,int nodetype) {
		try {
			LcNodeConfigDBEntity lcNodeConfig = new LcNodeConfigDBEntity();
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(lcNodeConfig);
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
			if(nodeType!=0){
				builder.append(" where 1= 1 ");
			}else{
				builder.append(" where DATA_STATUS=1 ");
			}
			if(node_code!=0){
				builder.append(" and NODE_CODE ="+node_code+" ");
			}
			if(district!=0){
				builder.append(" and DISTRICT ="+district+" ");
			}
			if(nodetype!=0){
				builder.append(" and NODE_TYPE="+nodetype+" ");
			}

			builder.append(" ORDER BY LAST_UPDATE_TIME DESC");
			if(currentPage>0){
				int beginRecord = (currentPage-1)*pageSize;//开始记录
				int endRecord = pageSize;//从开始到结束的记录数
				builder.append("  limit "+beginRecord+","+endRecord+"");
			}
			return super.queryForList(builder.toString().toUpperCase(), LcNodeConfigDBEntity.class);
		} catch (ObjectForMapException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public LcNodeConfigDBEntity getbyIp(String ip_address, int district) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcNodeConfigDBEntity> getbyNodeType(int node_type, int district) {
		try {
			//拼接sql，查询LC_NODE_CONFIG表  条件为服务区域类型 LC_NODE_CONFIG.DISTRICT
			StringBuilder builder=new StringBuilder("SELECT "
					+ " LC_NODE_CONFIG.NC_ID,LC_NODE_CONFIG.NODE_CODE,LC_NODE_CONFIG.MAX_LIMIT,LC_NODE_CONFIG.MIN_LIMIT,"
					+ " LC_NODE_CONFIG.LOCAL_IP_ADDRESS,LC_NODE_CONFIG.INTERNET_IP_ADDRESS,LC_NODE_CONFIG.NODE_TYPE,LC_NODE_CONFIG.DISTRICT,"
					+ " LC_NODE_CONFIG.EXT_CONTENT,LC_NODE_CONFIG.LAST_UPDATE_TIME,LC_NODE_CONFIG.USERNAME,"
					+ " LC_NODE_CONFIG.DATA_STATUS"
					+ " FROM LC_NODE_CONFIG"
					+ " WHERE LC_NODE_CONFIG.DATA_STATUS='1'");
			if(district!=0){
				builder.append(" and LC_NODE_CONFIG.DISTRICT=?");
			}
			if(node_type!=0){
				builder.append(" and LC_NODE_CONFIG.NODE_TYPE=?");
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			if(district!=0&&node_type!=0){
				List<LcNodeConfigDBEntity> list = qryRun.query(conn, builder.toString().toUpperCase(),
						new BeanListHandler<LcNodeConfigDBEntity>(LcNodeConfigDBEntity.class), district,node_type);
				return list;
			}else if(district==0&&node_type!=0){
				List<LcNodeConfigDBEntity> list = qryRun.query(conn, builder.toString().toUpperCase(),
						new BeanListHandler<LcNodeConfigDBEntity>(LcNodeConfigDBEntity.class),node_type);
				return list;
			}else if(district!=0&&node_type==0){
				List<LcNodeConfigDBEntity> list = qryRun.query(conn, builder.toString().toUpperCase(),
						new BeanListHandler<LcNodeConfigDBEntity>(LcNodeConfigDBEntity.class),district);
				return list;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 【方法为查询服务区域类型】
	 * @param district
	 * @return List<LcNodeConfig>
	 */
	@Override
	public List<LcNodeConfigDBEntity> getbyDistrict(int district) {
		try {
			//拼接sql，查询LC_NODE_CONFIG表  条件为服务区域类型 LC_NODE_CONFIG.DISTRICT
			StringBuilder builder=new StringBuilder("SELECT "
					+ " LC_NODE_CONFIG.NC_ID,LC_NODE_CONFIG.NODE_CODE,LC_NODE_CONFIG.MAX_LIMIT,LC_NODE_CONFIG.MIN_LIMIT,"
					+ " LC_NODE_CONFIG.LOCAL_IP_ADDRESS,LC_NODE_CONFIG.INTERNET_IP_ADDRESS,LC_NODE_CONFIG.NODE_TYPE,LC_NODE_CONFIG.DISTRICT,"
					+ " LC_NODE_CONFIG.EXT_CONTENT,LC_NODE_CONFIG.LAST_UPDATE_TIME,LC_NODE_CONFIG.USERNAME,"
					+ " LC_NODE_CONFIG.DATA_STATUS , IS_REDUNDANCE"
					+ " FROM LC_NODE_CONFIG"
					+ " WHERE LC_NODE_CONFIG.DATA_STATUS='1' AND  LC_NODE_CONFIG.DISTRICT=?");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcNodeConfigDBEntity> list = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcNodeConfigDBEntity>(LcNodeConfigDBEntity.class), district);
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<LcNodeConfigDBEntity> getbyUserName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByNodeCode(LcNodeConfigDBEntity nodeConfig) {
		try {
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(nodeConfig);
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
			builder.append(" WHERE NODE_CODE=?");
			// 执行sql
			Object[] objValues=new Object[values.length+1];
			for (int i = 0; i < objValues.length; i++) {
				if(i==objValues.length-1){
					objValues[i]=nodeConfig.getNode_code();
				}else {
					objValues[i]=values[i];
				}
			}
			executeUpdate(builder.toString(),objValues );
			return PlatformResponseResult.success_VALUE;
		} catch (ObjectForMapException | SQLException e) {
			e.printStackTrace();
			return PlatformResponseResult.failure_VALUE;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int delete(Class clazz, int[] id) throws SQLException {
		try {
			LcNodeConfigDBEntity obj = (LcNodeConfigDBEntity) clazz.newInstance();
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
			executeUpdate(builder.toString());
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
	public void deleteByNodeCode(int nodeCode) {
		try {
			LcNodeConfigDBEntity obj = new LcNodeConfigDBEntity();
			DBEntity entity = ObjectForMap.dbObjectForMap(obj);
			// 得到类注解
			String tableName = entity.getTableName();
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("update ");
			builder.append(tableName.toUpperCase());
			builder.append(" set DATA_STATUS=0 ");
			builder.append(" WHERE NODE_CODE=?");
			// 执行sql
			executeUpdate(builder.toString().toUpperCase(), nodeCode);
		} catch (ObjectForMapException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @mark 此查询SQL中缺少  IS_REDUNDANCE 字段导致组装protobuf返回给MM时例外
	 * @modify jbp
	 */
	@Override
	public List<LcNodeConfigDBEntity> getbyDistrictMM(int nodeType) {
		try {
			//拼接sql，查询LC_NODE_CONFIG表  条件为服务区域类型 LC_NODE_CONFIG.DISTRICT
			StringBuilder builder=new StringBuilder("SELECT "
					+ " LC_NODE_CONFIG.NC_ID,LC_NODE_CONFIG.NODE_CODE,LC_NODE_CONFIG.MAX_LIMIT,LC_NODE_CONFIG.MIN_LIMIT,"
					+ " LC_NODE_CONFIG.LOCAL_IP_ADDRESS,LC_NODE_CONFIG.INTERNET_IP_ADDRESS,LC_NODE_CONFIG.NODE_TYPE,LC_NODE_CONFIG.DISTRICT,"
					+ " LC_NODE_CONFIG.EXT_CONTENT,LC_NODE_CONFIG.LAST_UPDATE_TIME,LC_NODE_CONFIG.USERNAME,"
					+ " LC_NODE_CONFIG.DATA_STATUS,LC_NODE_CONFIG.IS_REDUNDANCE"
					+ " FROM LC_NODE_CONFIG"
					+ " WHERE LC_NODE_CONFIG.DATA_STATUS='1' AND LC_NODE_CONFIG.DISTRICT !="+DistrictCode.center_VALUE);
			builder.append(" and LC_NODE_CONFIG.NODE_TYPE=?");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcNodeConfigDBEntity> list = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcNodeConfigDBEntity>(LcNodeConfigDBEntity.class),nodeType);
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}

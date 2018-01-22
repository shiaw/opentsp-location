package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDistrictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcDistrictDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


public class LcDistrictDaoImpl extends BaseDaoImpl<LcDistrictDBEntity> implements LcDistrictDao {

	@Override
	public LcDistrictDBEntity getDistrict(int dictCode) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<LcDistrictDBEntity> queryDistrictList(String dictCodeName) {
		StringBuilder builder = null;
		try {
			builder=new StringBuilder("select district.DISTRICT_ID,district.DICT_CODE,district.PARENT_CODE from LC_DISTRICT district inner join LC_DICT dict on district.DICT_CODE=dict.DICT_CODE");
			builder.append(" where DICT_NAME like '%"+dictCodeName+"%'");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcDistrictDBEntity>  list = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcDistrictDBEntity>(LcDistrictDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void deleteByParentCode(int parent_code) {
		StringBuilder builder = null;
		try {
			builder=new StringBuilder("delete from LC_DISTRICT  where PARENT_CODE=?");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			qryRun.update(conn, builder.toString().toUpperCase(), parent_code);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public List<LcDistrictDBEntity> queryDistrictByParent_code(int parent_code,int currentPage,int pageSize) {
		StringBuilder builder = null;
		try {
			builder=new StringBuilder("select district.DISTRICT_ID,district.DICT_CODE,district.PARENT_CODE from LC_DISTRICT district ");
			if(parent_code != 0){
				builder.append("where district.PARENT_CODE="+parent_code);
			}
			if(currentPage>0){
				int beginRecord = (currentPage-1)*pageSize;//开始记录
				int endRecord = pageSize;//从开始到结束的记录数
				builder.append("  limit "+beginRecord+","+endRecord+"");
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcDistrictDBEntity>  list = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcDistrictDBEntity>(LcDistrictDBEntity.class));
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
	public int queryDistrictCount(int parent_code){
		StringBuilder builder = null;
		try {
			builder=new StringBuilder("select count(*) from LC_DISTRICT district ");
			if(parent_code != 0){
				builder.append("where district.PARENT_CODE="+parent_code);
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			int count=0;
			Number dictCount = 0;
			dictCount =(Number)qryRun.query(conn,builder.toString(), scalarHandler);
			count =(int)dictCount.longValue();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	public PlatformResponseResult deleteBydictCode(int[] dictCode) {
		// 执行sql
		try {
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("delete from  LC_DICT where DICT_ID ");

			builder.append(" IN (");
			for (int i = 0; i < dictCode.length; i++) {
				if (i == 0) {
					builder.append(dictCode[i]);
				} else {
					builder.append("," + dictCode[i]);
				}
			}
			builder.append(")");

			executeUpdate(builder.toString());
			return PlatformResponseResult.success;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return PlatformResponseResult.failure;
	}
	public PlatformResponseResult deleteDictType(int[] dtId) {
		// 执行sql
		try {
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("delete from  LC_DICT_TYPE where DT_CODE ");

			builder.append(" IN (");
			for (int i = 0; i < dtId.length; i++) {
				if (i == 0) {
					builder.append(dtId[i]);
				} else {
					builder.append("," + dtId[i]);
				}
			}
			builder.append(")");

			executeUpdate(builder.toString());
			return PlatformResponseResult.success;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return PlatformResponseResult.failure;
	}

}

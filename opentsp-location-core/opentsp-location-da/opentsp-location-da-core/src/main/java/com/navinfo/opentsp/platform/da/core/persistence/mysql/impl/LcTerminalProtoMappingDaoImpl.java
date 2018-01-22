package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalProtoMappingDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalProtoMappingDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


public class LcTerminalProtoMappingDaoImpl extends BaseDaoImpl<LcTerminalProtoMappingDBEntity> implements
		LcTerminalProtoMappingDao {

	@Override
	public List<LcTerminalProtoMappingDBEntity> getByLogicCode(int logic_code) {
		StringBuilder builder = null;
		try {
			builder=new StringBuilder("select TPL_ID,LOGIC_CODE,DICT_CODE,MESSAGE_CODE from LC_TERMINAL_PROTO_MAPPING");
			if(logic_code!=0){
				builder.append(" where LOGIC_CODE="+logic_code);
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcTerminalProtoMappingDBEntity>  list = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcTerminalProtoMappingDBEntity>(LcTerminalProtoMappingDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void deleteProtoMapping(int logicCode) {
		StringBuilder builder = null;
		try {
			builder=new StringBuilder("delete from LC_TERMINAL_PROTO_MAPPING  where LOGIC_CODE=?");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			qryRun.update(conn, builder.toString().toUpperCase(), logicCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public List<LcTerminalProtoMappingDBEntity> getByGroupLogicCode(
			int logic_code) {
		StringBuilder builder = null;
		try {
			builder=new StringBuilder("select proto.TPL_ID,proto.LOGIC_CODE,proto.DICT_CODE,proto.MESSAGE_CODE from LC_TERMINAL_PROTO_MAPPING proto where 1=1");
			if(logic_code!=0){
				builder.append(" AND  proto.LOGIC_CODE="+logic_code);
			}
			builder.append(" group by proto.LOGIC_CODE");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcTerminalProtoMappingDBEntity>  list = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcTerminalProtoMappingDBEntity>(LcTerminalProtoMappingDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<LcTerminalProtoMappingDBEntity> getTerminalProtoMapping(int logic_code, int dictCode,
																		int messageCode,int currentPage,int pageSize) {

		StringBuilder builder = null;
		try {
			builder=new StringBuilder("select proto.TPL_ID,proto.LOGIC_CODE,proto.DICT_CODE,proto.MESSAGE_CODE from LC_TERMINAL_PROTO_MAPPING proto where 1=1");
			if(logic_code!=0){
				builder.append(" AND  proto.LOGIC_CODE="+logic_code);
			}
			if(dictCode!=0){
				builder.append(" AND  proto.DICT_CODE="+dictCode);
			}
			if(messageCode!=0){
				builder.append(" AND  proto.MESSAGE_CODE="+messageCode);
			}
			//builder.append(" group by proto.LOGIC_CODE");
			if(currentPage>0){
				int beginRecord = (currentPage-1)*pageSize;//开始记录
				int endRecord = pageSize;//从开始到结束的记录数
				builder.append("  limit "+beginRecord+","+endRecord+"");
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcTerminalProtoMappingDBEntity>  list = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcTerminalProtoMappingDBEntity>(LcTerminalProtoMappingDBEntity.class));
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
	public int selectPMListCount(int logic_code,int dictCode,int messageCode){
		StringBuilder builder = null;
		try {
			builder=new StringBuilder("select count(*) from LC_TERMINAL_PROTO_MAPPING proto where 1=1");
			if(logic_code!=0){
				builder.append(" AND  proto.LOGIC_CODE="+logic_code);
			}
			if(dictCode!=0){
				builder.append(" AND  proto.DICT_CODE="+dictCode);
			}
			if(messageCode!=0){
				builder.append(" AND  proto.MESSAGE_CODE="+messageCode);
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			/*List<LcTerminalProtoMappingDBEntity>  list = qryRun.query(conn, builder.toString().toUpperCase(),
						new BeanListHandler<LcTerminalProtoMappingDBEntity>(LcTerminalProtoMappingDBEntity.class));*/
			int count=0;
			Number dictCount =0;
			dictCount = (Number)qryRun.query(conn,builder.toString(), scalarHandler);
			count =(int)dictCount.longValue();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	@Override
	public void deleteProtoMapping(int[] tplId, int messageCode) {
		StringBuilder builder = null;
		try {
			builder=new StringBuilder("delete from LC_TERMINAL_PROTO_MAPPING  where 1=1");
			if(messageCode!=0){
				builder.append(" and MESSAGE_CODE=?")	;
			}
			if(tplId!=null&&tplId.length>0){
				for(int tpl:tplId){
					builder.append(" and TPL_ID="+tpl)	;
				}
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			qryRun.update(conn, builder.toString().toUpperCase(), messageCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}



}

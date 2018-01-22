package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalParaDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalParaDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;



public class LcTerminalParaDaoImpl extends BaseDaoImpl<LcTerminalParaDBEntity> implements LcTerminalParaDao{

	@Override
	public List<LcTerminalParaDBEntity> getByTerminalId(long terminalId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据终端标识和终端指令删除
	 * @param terminalId
	 * @param para_code
	 * @return
	 */
	@Override
	public int deleteByTerminalInfo(long terminalId, int para_code) {
		try {
			//拼接sql，查询LC_NODE_CONFIG表  条件为服务区域类型 LC_NODE_CONFIG.DISTRICT
			StringBuilder builder=new StringBuilder("delete from LC_TERMINAL_PARA where TERMINAL_ID=? and PARA_CODE=?");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			int result=qryRun.update(conn,builder.toString().toUpperCase(),terminalId,para_code);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 查询终端参数
	 *
	 * @param terminalId
	 * @param paramterCode
	 *            {@link Integer}终端参数编码
	 * @return {@link List}<{@link LcTerminalParaDBEntity}>
	 */
	@Override
	public List<LcTerminalParaDBEntity> getByTerminalParameter(long terminalId,int... paramterCode){
		try {
			//拼接sql，查询LC_TERMINAL_PARA表
			StringBuilder builder=new StringBuilder("select ");
			builder.append("para.PARA_ID,para.TERMINAL_ID,para.PARA_CODE,para.PARA_CONTENT,para.LAST_UPDATE_TIME ");
			builder.append("from LC_TERMINAL_PARA para ");
			builder.append("where para.TERMINAL_ID=? ");
			Object[] objPara=new Object[paramterCode.length+1];
			objPara[0]=terminalId;
			//循环添加条件参数
			for(int i=0;i<paramterCode.length;i++){
				//判断是否para.PARA_CODE参数是否是最后一个
				if(i==0){
					builder.append(" and para.PARA_CODE=?");
					objPara[i+1]=paramterCode[i];
				}else{
					builder.append(" or para.PARA_CODE=? ");
					objPara[i+1]=paramterCode[i];
				}

			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcTerminalParaDBEntity> list=qryRun.query(conn,builder.toString().toUpperCase(),new BeanListHandler<LcTerminalParaDBEntity>(LcTerminalParaDBEntity.class),objPara);
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<LcTerminalParaDBEntity> queryTerminalParaRes(long terminalId,
															 int command,int currentPage,int pageSize) {
		try {
			//拼接sql，查询LC_TERMINAL_PARA表
			StringBuilder builder=new StringBuilder("select ");
			builder.append("para.PARA_ID,para.TERMINAL_ID,para.PARA_CODE,para.PARA_CONTENT,para.LAST_UPDATE_TIME ");
			builder.append("from LC_TERMINAL_PARA para ");
			builder.append("where 1=1 ");
			int flag_terminalId=0;
			int flag_commandName=0;
			if(terminalId!=0){
				builder.append("and para.TERMINAL_ID=? ");
				flag_terminalId=1;
			}
			if(command!=0){
				builder.append("and para.PARA_CODE =? ");
				flag_commandName=1;
			}
			if(currentPage>0){
				int beginRecord = (currentPage-1)*pageSize;//开始记录
				int endRecord = pageSize;//从开始到结束的记录数
				builder.append("  limit "+beginRecord+","+endRecord+"");
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcTerminalParaDBEntity> list=null;
			if(flag_terminalId==1 &&flag_commandName==1){
				list=qryRun.query(conn,builder.toString().toUpperCase(),new BeanListHandler<LcTerminalParaDBEntity>(LcTerminalParaDBEntity.class),terminalId,command);
			}else if(flag_terminalId==1 &&flag_commandName==0){
				list=qryRun.query(conn,builder.toString().toUpperCase(),new BeanListHandler<LcTerminalParaDBEntity>(LcTerminalParaDBEntity.class),terminalId);
			}else if(flag_terminalId==0 &&flag_commandName==1){
				list=qryRun.query(conn,builder.toString().toUpperCase(),new BeanListHandler<LcTerminalParaDBEntity>(LcTerminalParaDBEntity.class),command);
			}else{
				list=qryRun.query(conn,builder.toString().toUpperCase(),new BeanListHandler<LcTerminalParaDBEntity>(LcTerminalParaDBEntity.class));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<LcTerminalParaDBEntity> queryTerminalParaResForDsa(long[] terminalIds) {
		try {
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			String sql = "select para.PARA_ID,para.TERMINAL_ID,para.PARA_CODE,para.PARA_CONTENT,para.LAST_UPDATE_TIME from LC_TERMINAL_PARA para ,LC_TERMINALID_TEMP info where para.TERMINAL_ID=info.TERMINAL_ID";
			List<LcTerminalParaDBEntity> lcTerminalRule = qryRun.query(conn, sql.toUpperCase(),
					new BeanListHandler<LcTerminalParaDBEntity>(
							LcTerminalParaDBEntity.class));
			return lcTerminalRule;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<LcTerminalParaDBEntity> getByCommandCode(int... para_code) {
		// TODO Auto-generated method stub

		try {
			//拼接sql，查询LC_TERMINAL_PARA表
			StringBuilder builder=new StringBuilder("select ");
			builder.append("para.PARA_ID,para.TERMINAL_ID,para.PARA_CODE,para.PARA_CONTENT,para.LAST_UPDATE_TIME ");
			builder.append("from LC_TERMINAL_PARA para ");
			builder.append("where 1=1 ");

			Object[] objPara=new Object[para_code.length];

			//循环添加条件参数
			for(int i=0;i<para_code.length;i++){
				//判断是否para.PARA_CODE参数是否是最后一个
				if(i==0){
					builder.append(" and para.PARA_CODE=?");
					objPara[i]=para_code[i];
				}else{
					builder.append(" or para.PARA_CODE=? ");
					objPara[i+1]=para_code[i];
				}

			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcTerminalParaDBEntity> list=qryRun.query(conn,builder.toString().toUpperCase(),new BeanListHandler<LcTerminalParaDBEntity>(LcTerminalParaDBEntity.class),objPara);
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}

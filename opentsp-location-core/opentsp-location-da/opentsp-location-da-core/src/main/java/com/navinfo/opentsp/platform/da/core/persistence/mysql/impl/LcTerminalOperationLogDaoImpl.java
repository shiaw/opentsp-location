package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalOperationLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalOperationLogDao;

import java.sql.SQLException;
import java.util.List;


public class LcTerminalOperationLogDaoImpl extends BaseDaoImpl<LcTerminalOperationLogDBEntity> implements
		LcTerminalOperationLogDao {
	/**
	 * 终端操作状态更新
	 * @param terminalId {@link:Long 终端标识}
	 * @param messageCod {@link:Integer 终端指令编码}
	 * @param results {@link:Boolean 操作状态}
	 */
	@Override
	public boolean terminalOperateLogUpdate(long terminalId,
											int messageCod, boolean results) {
		//结果集
		int lcTerminalOperationLog=0;
		try {
			//拼接sql,根据条件修改OPERATOR_RESULT字段的值
			StringBuilder builder=new StringBuilder("UPDATE ");
			builder.append(" LC_TERMINAL_OPERATION_LOG AS LC_TERMINAL_OPERATION_LOGS SET");
			builder.append(" LC_TERMINAL_OPERATION_LOGS.OPERATOR_RESULT=?");
			builder.append(" WHERE LC_TERMINAL_OPERATION_LOGS.TERMINAL_ID=? AND LC_TERMINAL_OPERATION_LOGS.OPERATION_TYPE=?");
			executeUpdate(builder.toString(),results==true?1:0,terminalId,messageCod);
			lcTerminalOperationLog=1;
		} catch (SQLException e) {
			e.printStackTrace();
			lcTerminalOperationLog=0;
		}
		//如果结果集>0则添加成功，<=0添加失败
		if(lcTerminalOperationLog>0){
			return true;
		}else {
			return false;
		}
	}

	@Override
	public List<LcTerminalOperationLogDBEntity> queryTerminalOperationLogList(
			long terminalId, int operatinoType, long start, long end,int currentPage,int pageSize) {
		try {

			StringBuilder builder = new StringBuilder();
			builder.append("SELECT operation.OPERATION_CONTENT,operation.OPERATION_TIME,operation.OPERATION_TYPE,operation.OPERATOR_RESULT,operation.TERMINAL_ID,operation.TOL_ID ");
			builder.append(" from LC_TERMINAL_OPERATION_LOG operation ");
			builder.append(" where 1=1 ");
			if(start!=0){
				builder.append(" and operation.OPERATION_TIME >="+start+" ");
			}
			if(end!=0){
				builder.append(" and operation.OPERATION_TIME <="+end+" ");
			}
			if(terminalId!=0){
				builder.append(" and TERMINAL_ID ="+terminalId+" ");
			}
			if(operatinoType!=0){
				builder.append(" and operation.OPERATION_TYPE ="+operatinoType);
			}
			builder.append("  order by operation.OPERATION_TIME desc");
			if(currentPage>0){
				int beginRecord = (currentPage-1)*pageSize;//开始记录
				int endRecord = pageSize;//从开始到结束的记录数
				builder.append("  limit "+beginRecord+","+endRecord+"");
			}
			return super.queryForList(builder.toString().toUpperCase(), LcTerminalOperationLogDBEntity.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

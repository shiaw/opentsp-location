package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRuleLogDBEntity;

import java.util.List;


public interface LcTerminalRuleLogDao  extends BaseDao<LcTerminalRuleLogDBEntity>{


	/**
	 * 根据参数查询规则日志
	 * @param terminalId 终端标识
	 * @param ruleCode 规则编码
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return List< LcTerminalRuleDBEntity>
	 */
	public List<LcTerminalRuleLogDBEntity> queryTerminalRuleLogList(
			long terminalId, int ruleCode, long start, long end) ;
	
}

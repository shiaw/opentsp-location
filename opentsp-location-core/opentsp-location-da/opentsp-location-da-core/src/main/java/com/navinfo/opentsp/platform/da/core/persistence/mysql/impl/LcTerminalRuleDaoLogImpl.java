package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRuleLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalRuleLogDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;


public class LcTerminalRuleDaoLogImpl extends BaseDaoImpl<LcTerminalRuleLogDBEntity>
		implements LcTerminalRuleLogDao {

	@Override
	public List<LcTerminalRuleLogDBEntity> queryTerminalRuleLogList(
			long terminalId, int ruleCode, long start, long end) {
		try {
			StringBuilder builder = new StringBuilder("select  * from LC_TERMINAL_RULE_LOG where 1=1 ");
			if(terminalId != 0)
				builder.append(" and  TERMINAL_ID="+terminalId);
			if(ruleCode != 0)
				builder.append(" and  RULE_TYPE="+ruleCode);
			if(start !=0 )
				builder.append(" and  LAST_UPDATE_TIME >= "+start);
			if( end != 0)
				builder.append(" and  LAST_UPDATE_TIME <= "+end);
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcTerminalRuleLogDBEntity>  list = qryRun.query(conn, builder.toString().toUpperCase(),
						new BeanListHandler<LcTerminalRuleLogDBEntity>(LcTerminalRuleLogDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

}

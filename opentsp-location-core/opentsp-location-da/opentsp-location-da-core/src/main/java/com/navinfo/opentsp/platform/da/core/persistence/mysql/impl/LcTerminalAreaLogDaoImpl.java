package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalAreaLogDao;

import java.sql.SQLException;
import java.util.List;


public class LcTerminalAreaLogDaoImpl extends BaseDaoImpl<LcTerminalAreaLogDBEntity>
implements LcTerminalAreaLogDao {

	@Override
	public List<LcTerminalAreaLogDBEntity> queryTerminalAreaLogList(
			long terminalId, int originalAreaId, long start, long end) {
		try {
			StringBuilder builder = new StringBuilder("select  TA_ID,ORIGINAL_AREA_ID,AREA_TYPE,CREATE_TIME,TERMINAL_ID from LC_TERMINAL_AREA_LOG where 1=1 ");
			if(terminalId != 0)
				builder.append(" and  TERMINAL_ID="+terminalId);
			if(originalAreaId != 0)
				builder.append(" and  ORIGINAL_AREA_ID="+originalAreaId);
			if(start !=0 )
				builder.append(" and  CREATE_TIME >= "+start);
			if( end != 0)
				builder.append(" and  CREATE_TIME <= "+end);
			return super.queryForList(builder.toString().toUpperCase(), LcTerminalAreaLogDBEntity.class);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

}

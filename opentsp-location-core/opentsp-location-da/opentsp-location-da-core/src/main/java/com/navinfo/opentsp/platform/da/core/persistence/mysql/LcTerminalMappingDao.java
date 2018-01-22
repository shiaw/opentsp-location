package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalMappingDBEntity;

import java.util.List;


public interface LcTerminalMappingDao extends BaseDao<LcTerminalMappingDBEntity>{
	
	public int addTerminalMapping(long mainId, long secondId);
	
	public int deleteTerminalMapping(long mainId, long secondId);
	
	public List<LcTerminalMappingDBEntity> queryAllMapping();
	
	public LcTerminalMappingDBEntity getMappingByMainId(long mainId);
	
	public LcTerminalMappingDBEntity getMappingBySecondId(long secondId);
}

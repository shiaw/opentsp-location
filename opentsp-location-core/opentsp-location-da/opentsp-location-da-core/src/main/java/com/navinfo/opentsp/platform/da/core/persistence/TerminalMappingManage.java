package com.navinfo.opentsp.platform.da.core.persistence;


import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalMappingDBEntity;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;

public interface TerminalMappingManage {
	

	public LcTerminalMappingDBEntity getMappingByMainId(long mainId);
	
	public LcTerminalMappingDBEntity getMappingBySecondId(long secondId);
	
	public LCPlatformResponseResult.PlatformResponseResult terminalMappingBindOrNotSync(long mainTerminalId,
																						long secondaryTerminalId, boolean isBinding);
	
	public LCPlatformResponseResult.PlatformResponseResult terminalMappingBindOrNot(long mainTerminalId,
																					long secondaryTerminalId, boolean isBinding);
}

package com.navinfo.opentsp.platform.da.core.jmx;

public interface EchoMBean {
	abstract String delMileageAndOilDataStatisticCache();
	abstract String getMileageAndOilDataByTerminalId(Long terminalId);
	abstract String getAllMileageAndOilData(); 
}
 
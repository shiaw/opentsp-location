package com.navinfo.opentsp.platform.dp.core.common;

import com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constant {
	/** 链路唯一标识Key */
	public static final String UniqueMark = "UniqueMark";

	@Value("${district.code}")
	private int districtCode;

	public final DistrictCode DISTRICT_CODE = DistrictCode.valueOf(districtCode);

	public enum DPModules {
		TAL_Listener, RPL_Listener, MM_Connect, CORRECT_Connect, DAL_Connect
	}
	public static class ConfigKey{
		public static final String DP_FOR_RPCODE = "dpForRpCode";
		public static final String DP_FOR_TACODE = "dpForTaCode";
		public static final String DA_NODE = "daNode";
		//public static final String CORRECT_NODES = "correctNodes";
		public static final String NODE_CODE = "NODE.CODE";
		public static final String DISTRICT_CODE = "DISTRICT.CODE";
		public static final String MM_MASTER_CODE = "MM.MASTER.CODE";
		public static final String MM_MASTER_IP = "MM.MASTER.IP";
		public static final String MM_MASTER_PORT = "MM.MASTER.PORT";

		public static final String MM_SLAVE_CODE = "MM.SLAVE.CODE";
		public static final String MM_SLAVE_IP = "MM.SLAVE.IP";
		public static final String MM_SLAVE_PORT = "MM.SLAVE.PORT";
		
		public static final String MM_RECONNECT_INTERVAL = "MM.RECONNECT.INTERVAL";
		public static final String REQUEST_NODE_INTERVAL = "REQUEST.NODE.INTERVAL";

	}

	public static final String RULE_CACHE_KEY = "RuleCache";
	public static final String RULE_COMMON_CACHE_KEY = "RuleCommonCache";
	public static final String TERMINAL_CACHE_KEY = "TerminalCache";
	public static final String DICT_CACHE_KEY = "DictCache";
	public static final String AREA_CACHE_KEY = "AreaCache";
	public static final String RULE_SATUS_CACHE_KEY = "RuleStatusCache";
	public static final String AREA_COMMON_CACHE_KEY = "AreaCommonCache";
}

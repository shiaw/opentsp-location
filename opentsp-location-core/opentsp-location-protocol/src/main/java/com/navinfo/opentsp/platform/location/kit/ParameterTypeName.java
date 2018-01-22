package com.navinfo.opentsp.platform.location.kit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ParameterTypeName {
	private static Map<String, String> names = new ConcurrentHashMap<String, String>();
	static {
		names.put("all_reconnectTimes", "重连次数");
		names.put("all_reconnectInterval", "重连时间间隔");
		names.put("all_nodeNumber", "节点编号");
		names.put("all_nodeDistrict", "服务区域编码");
		names.put("all_heartbeatInterval", "心跳时间间隔");

		names.put("ta_pictureDataTimeoutThreshold", "图片数据缓存超时时间阀值");
		names.put("ta_pictureProcessInterval", "拍照数据处理线程运行间隔");
		names.put("ta_commandTimeoutThreshold", "终端指令应答超时阀值");
		names.put("ta_commandProcessInterval", "终端指令处理线程运行间隔");
		names.put("ta_commonCacheProcessInterval", "公共缓存处理线程运行间隔");
		names.put("ta_patchDataThreshold", "补发数据判断阀值");

		names.put("dp_terminalSettingThreshold", "终端参数设置值超时阀值");
		names.put("dp_dataSaveThreshold", "DP层数据存储超时阀值");
		names.put("dp_removeInvalidDataThreshold", "TA层断开缓存数据清除阀值");
		names.put("dp_terminalSetProcessInterval", "终端参数设置处理线程运行间隔");
		names.put("dp_dpAndTaGuaranteeInterval", "DP和TA数据保障任务运行间隔");
		names.put("dp_removeInvalidDataInterval", "清除断开TA相关数据运行间隔");
		names.put("dp_commonDataProcessInterval", "公共数据缓存处理线程运行间隔");

		names.put("en_threadNumber", "加密服务处理线程数");
	}

	public static String getConfigName(String key) {
		return names.get(key);
	}

}

package com.navinfo.opentsp.gateway.tcp.proto.location.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 存放终端最新CAN数据，用于合并到位置数据中.
 *
 */
public class TerminalLastestCANData {
	/* 全局can数据Hash*/
	public static Map<String, CANDataEntity> canMap = new HashMap<String, CANDataEntity>();
	/**
	 * 获取最新CAN数据
	 * @param canId
	 * @return
	 */
	public static CANDataEntity getLastCANData(String uniqueMark){
		return canMap.get(uniqueMark);
	}
	/**
	 * 添加CAN数据
	 * @param uniqueMark
	 * @param obj
	 */
	public static void putCANData(String uniqueMark, CANDataEntity obj){
		synchronized (canMap){
		canMap.put(uniqueMark, obj);}
	}

}
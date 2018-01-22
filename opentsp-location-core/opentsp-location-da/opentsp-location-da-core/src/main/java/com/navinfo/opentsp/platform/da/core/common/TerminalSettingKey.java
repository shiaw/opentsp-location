package com.navinfo.opentsp.platform.da.core.common;

import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TerminalSettingKey {
	private static Map<Integer, Integer> cache = new ConcurrentHashMap<Integer, Integer>();
	static {
		cache.put(AllCommands.Terminal.OvertimeParkingCancel_VALUE, AllCommands.Terminal.OvertimeParking_VALUE);
		cache.put(AllCommands.Terminal.DrivingBanSettingCancel_VALUE, AllCommands.Terminal.DrivingBanSetting_VALUE);

		cache.put(AllCommands.Terminal.SpeedingAlarmCancel_VALUE, AllCommands.Terminal.SpeedingAlarmSetting_VALUE);
	}

	/**
	 * 查找对应的指令号，如设置超速告警--取消超速告警
	 * @param key
	 * @return 如果为-1，则表示为找到对应的指令号
	 */
	public static int GetCommandCode(int key) {
		if (cache.containsKey(key)) {
			return cache.get(key);
		} else {
			return -1;
		}
	}
}

package com.navinfo.opentsp.platform.location.kit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandName {

	// map存放协议（key协议指令,value 协议指令名称）
	private static Map<Integer, String> _command_name_mapping = new ConcurrentHashMap<Integer, String>();
	static {
		_command_name_mapping.put(0x1001, "分区信息汇报");
		_command_name_mapping.put(0x0001, "分区信息汇报应答");
		_command_name_mapping.put(0x0003, "分区MM节点查询");
		_command_name_mapping.put(0x1003, "分区MM节点查询应答");

		_command_name_mapping.put(0x1099, "链路心跳");
		_command_name_mapping.put(0x1100, "平台通用应答");

		_command_name_mapping.put(0x1101, "请求平台鉴权标识");
		_command_name_mapping.put(0x0101, "请求平台鉴权标识应答");
		_command_name_mapping.put(0x0102, "鉴权");
		_command_name_mapping.put(0x1102, "鉴权应答");
		_command_name_mapping.put(0x0103, "登录");
		_command_name_mapping.put(0x1103, "登录应答");
		_command_name_mapping.put(0x0105, "重连");
		_command_name_mapping.put(0x0106, "注销");
		_command_name_mapping.put(0x0107, "服务状态通知");
		_command_name_mapping.put(0x1107, "服务状态通知应答");
		_command_name_mapping.put(0x1108, "服务过期通知");
		_command_name_mapping.put(0x0109, "多服务鉴权请求");
		_command_name_mapping.put(0x1109, "多服务鉴权请求应答");

		_command_name_mapping.put(0x0200, "订阅请求");
		_command_name_mapping.put(0x1200, "订阅请求应答");
		_command_name_mapping.put(0x0201, "数据订阅");
		_command_name_mapping.put(0x1201, "数据订阅应答");
		_command_name_mapping.put(0x0202, "取消数据订阅");

		_command_name_mapping.put(0x0203, "批量位置查询");
		_command_name_mapping.put(0x1203, "批量位置查询应答");
		_command_name_mapping.put(0x0601, "节点信息广播");
		_command_name_mapping.put(0x0602, "节点标识汇报");
		_command_name_mapping.put(0x1603, "状态监控心跳");
		_command_name_mapping.put(0x1604, "请求DA层新节点");
		_command_name_mapping.put(0x0604, "分配DA层新节点");
		_command_name_mapping.put(0x1605, "请求DP层新节点");
		_command_name_mapping.put(0x0605, "分配DP层新节点");
		_command_name_mapping.put(0x1606, "链路状态通知");
		_command_name_mapping.put(0x1607, "节点交替广播");
		_command_name_mapping.put(0x1608, "节点状态汇报");

		_command_name_mapping.put(0x0650, "TA层节点配置");

		_command_name_mapping.put(0x0670, "DP层节点配置");
		_command_name_mapping.put(0x1671, "请求加密服务节点");
		_command_name_mapping.put(0x0671, "分配加密服务节点");
		_command_name_mapping.put(0x1672, "DP节点推送终端信息");

		_command_name_mapping.put(0x0690, "RP层节点配置");

		_command_name_mapping.put(0x0710, "DA层节点配置");

		_command_name_mapping.put(0x0730, "查询节点信息");
		_command_name_mapping.put(0x1730, "查询节点信息应答");
		_command_name_mapping.put(0x1731, "新增节点通知");
		_command_name_mapping.put(0x0732, "根据类型查询节点信息");
		_command_name_mapping.put(0x1732, "根据类型查询节点信息应答");

		_command_name_mapping.put(0x1760, "加密服务节点配置");

		_command_name_mapping.put(0x0900, "查询终端信息");
		_command_name_mapping.put(0x1900, "查询终端信息应答");
		_command_name_mapping.put(0x0901, "更新终端信息");
		_command_name_mapping.put(0x0902, "新增终端通知");
		_command_name_mapping.put(0x0903, "终端注册信息存储");
		_command_name_mapping.put(0x0904, "终端注册信息检索");
		_command_name_mapping.put(0x1904, "终端注册信息检索应答");

		_command_name_mapping.put(0x0910, "获取字典信息");
		_command_name_mapping.put(0x1910, "获取字典信息应答");

		_command_name_mapping.put(0x0930, "区域检索");
		_command_name_mapping.put(0x1930, "区域检索应答");
		_command_name_mapping.put(0x0931, "区域信息删除");
		_command_name_mapping.put(0x0932, "规则数据查询");
		_command_name_mapping.put(0x1932, "规则数据查询应答");
		_command_name_mapping.put(0x0933, "规则数据存储");
		_command_name_mapping.put(0x0934, "参数存储");
		_command_name_mapping.put(0x0935, "规则数据删除");

		_command_name_mapping.put(0x0940, "数据加密请求");
		_command_name_mapping.put(0x1940, "数据加密应答");

		_command_name_mapping.put(0x0960, "服务/节点日志存储");
		_command_name_mapping.put(0x0961, "终端操作日志存储");
		_command_name_mapping.put(0x0962, "终端操作状态更新");

		_command_name_mapping.put(0x0970, "数据存储");
		_command_name_mapping.put(0x0971, "数据检索");
		_command_name_mapping.put(0x1971, "数据汇报");

		_command_name_mapping.put(0x0980, "位置数据");
		_command_name_mapping.put(0x0981, "多媒体数据");
		_command_name_mapping.put(0x0982, "透传数据");
		_command_name_mapping.put(0x0983, "行驶记录仪");

		_command_name_mapping.put(0x3001, "下行消息的通用应答");
		_command_name_mapping.put(0x2001, "上行消息的通用应答");

		_command_name_mapping.put(0x3002, "位置数据汇报");
		_command_name_mapping.put(0x3003, "终端注册");
		_command_name_mapping.put(0x2003, "终端注册应答");
		_command_name_mapping.put(0x3004, "终端鉴权");
		_command_name_mapping.put(0x3005, "终端注销");
		_command_name_mapping.put(0x3006, "驾驶员身份信息采集上报");
		_command_name_mapping.put(0x3007, "终端上下线状态汇报");
		_command_name_mapping.put(0x3008, "精简位置数据汇报");
		_command_name_mapping.put(0x3009, "电子运单上报");

		_command_name_mapping.put(0x2050, "位置查询");
		_command_name_mapping.put(0x3050, "位置查询应答");

		_command_name_mapping.put(0x2055, "采集执行标准版本");
		_command_name_mapping.put(0x3055, "采集执行标准版本应答");
		_command_name_mapping.put(0x2056, "采集速度");
		_command_name_mapping.put(0x3056, "采集速度应答");
		_command_name_mapping.put(0x2057, "采集事故疑点");
		_command_name_mapping.put(0x3057, "采集事故疑点应答");
		_command_name_mapping.put(0x2058, "采集脉冲数据");
		_command_name_mapping.put(0x3058, "采集脉冲数据应答");

		_command_name_mapping.put(0x2131, "数据下行透传");
		_command_name_mapping.put(0x3131, "数据上行透传");

		_command_name_mapping.put(0x2151, "调度短信");
		_command_name_mapping.put(0x2152, "单向监听");
		_command_name_mapping.put(0x2153, "立即拍照/录像");
		_command_name_mapping.put(0x2156, "油路/电路控制");
		_command_name_mapping.put(0x2157, "车辆控制");
		_command_name_mapping.put(0x2158, "事件设置");
		_command_name_mapping.put(0x3159, "事件报告");
		_command_name_mapping.put(0x2160, "提问下发");
		_command_name_mapping.put(0x3160, "提问应答");
		_command_name_mapping.put(0x2161, "信息点播菜单设置");
		_command_name_mapping.put(0x2162, "信息点播/取消");
		_command_name_mapping.put(0x2163, "信息服务");

		_command_name_mapping.put(0x2163, "消息超时处理");
		_command_name_mapping.put(0x2252, "终端连接服务配置");
		_command_name_mapping.put(0x2253, "汇报策略和间隔");
		_command_name_mapping.put(0x2254, "特权号码");
		_command_name_mapping.put(0x2255, "报警触发设置");
		_command_name_mapping.put(0x2256, "超速报警");
		_command_name_mapping.put(0x2257, "取消超速报警");
		_command_name_mapping.put(0x2258, "疲劳驾驶报警");
		_command_name_mapping.put(0x2259, "取消疲劳驾驶报警");
		_command_name_mapping.put(0x2260, "多媒体参数设置");
		_command_name_mapping.put(0x2261, "车辆信息设置");
		_command_name_mapping.put(0x2262, "道路运输证");
		_command_name_mapping.put(0x2263, "碰撞报警");
		_command_name_mapping.put(0x2264, "侧翻报警");
		_command_name_mapping.put(0x2265, "定时定距拍照控制");
		_command_name_mapping.put(0x2266, "GNSS设置");
		_command_name_mapping.put(0x2267, "CAN总线设置");

		_command_name_mapping.put(0x2302, "参数查询");
		_command_name_mapping.put(0x3302, "参数查询应答");
		_command_name_mapping.put(0x2303, "终端自检");
		_command_name_mapping.put(0x3303, "终端自检应答");
		_command_name_mapping.put(0x2304, "电话薄设置");
		_command_name_mapping.put(0x2305, "设置圆形区域报警");
		_command_name_mapping.put(0x2306, "删除圆形区域 ");
		_command_name_mapping.put(0x2307, "设置矩形区域 ");
		_command_name_mapping.put(0x2308, "删除矩形区域 ");
		_command_name_mapping.put(0x2309, "设置多边形区域");
		_command_name_mapping.put(0x2310, "删除多边形区域");
		_command_name_mapping.put(0x2311, "设置路线");
		_command_name_mapping.put(0x2312, "删除路线");
		_command_name_mapping.put(0x2313, "临时位置跟踪控制");
		_command_name_mapping.put(0x2314, "禁驾报警设置");
		_command_name_mapping.put(0x3314, "取消禁驾报警设置");

		_command_name_mapping.put(0x2400, "终端关机");
		_command_name_mapping.put(0x2401, "终端复位");
		_command_name_mapping.put(0x2402, "终端恢复出厂设置");
		_command_name_mapping.put(0x2403, "关闭数据通信");
		_command_name_mapping.put(0x2404, "关闭所有无线通信");
		_command_name_mapping.put(0x2405, "无线升级");
		_command_name_mapping.put(0x2406, "连接指定服务器");

		_command_name_mapping.put(0x4001, "数据检索");
		_command_name_mapping.put(0x4501, "数据检索应答");

	}

	/**
	 * 根据key获取值
	 * 
	 * @param command
	 * @return
	 */
	public static String getCommandName(int command) {
		String _name = _command_name_mapping.get(command);
		return (_name == null || "".equals(_name)) ? "<"
				+ Convert.decimalToHexadecimal(command, 4) + ">未找到对应指令名称"
				: _name;

	}

	/**
	 * 根据Key 获取值
	 * 
	 * @param commandHex
	 *            16进制key
	 * @return
	 */
	public static String getCommandName(String commandHex) {
		int key = Integer.parseInt(commandHex, 10);
		return getCommandName(key);
	}
}

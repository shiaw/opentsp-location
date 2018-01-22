package com.navinfo.opentsp.platform.da.core.common;

public class StatisticType {

	public static final int speedingAlarm = 1001;		//超速（跟区域无关）
	public static final int areaInOutAlarm = 1002;		//进出区域报警
	public static final int areaSpeedingAlarm = 1003;	//区域超速报警
	public static final int routeInOutAlarm = 1004;		//进出路线报警
	public static final int routeDeviate = 1104;
	public static final int roadSpeedingAlarm = 1005;	//路段超速报警
	public static final int roadOvertimeAlarm = 1006;	//路段行驶时间不足或过长
	public static final int outParkingAlarm = 1007;		//超时停车报警
	public static final int tiredDrivingAlarm = 1008;	//疲劳驾驶报警
	public static final int emergencyAlarm = 1009;		//紧急报警
	public static final int underPowerAlarm = 1010;		//欠压报警
	public static final int lossPowerAlarm = 1011;		//断电报警

}

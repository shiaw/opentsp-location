package com.navinfo.opentsp.gateway.tcp.proto.location.pojo;

import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.BatteryVehicleInfo;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.VehicleStatusAddition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CAN数据
 * @author chengbing
 *
 */
public class CANDataEntity implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/* 电池组（SOC）剩余电量 */
	private int batteryPower = 0;
	/* 里程 */
	private long mileage = 0;
	/* 电池电压、箱号 */
	private List<ModuleVoltageEntity> moduleVoltageList = new ArrayList<ModuleVoltageEntity>();
	/* 电车状态标志位 */
	private long electricVehicle = 0;

	public int getCombineTimes() {
		return combineTimes;
	}

	public void setCombineTimes(int combineTimes) {
		this.combineTimes = combineTimes;
	}

	/*合并次数*/
	private int combineTimes=0;
	private BatteryVehicleInfo.Builder batteryVehicleInfo = BatteryVehicleInfo.newBuilder();

	private VehicleStatusAddition.Builder statusAddition = VehicleStatusAddition.newBuilder();
	/* 故障码 */
	private LCLocationData.VehicleBreakdownAddition.Builder breakdownAddition = LCLocationData.VehicleBreakdownAddition.newBuilder();

	/*1个字节，FF*/
	public static int BytesOf1=255;
	/*2个字节，FFFF*/
	public static int BytesOf2=65535;
	/*4个字节，FFFFFFFF*/
	public static Long BytesOf4=4294967295L;

	public long getEcuTime() {
		return ecuTime;
	}

	public void setEcuTime(long ecuTime) {
		this.ecuTime = ecuTime;
	}

	public long ecuTime=0;

	public BatteryVehicleInfo.Builder getBatteryVehicleInfo() {
		return batteryVehicleInfo;
	}

	public VehicleStatusAddition.Builder getStatusAddition() {
		return statusAddition;
	}

	public void setStatusAddition(VehicleStatusAddition.Builder statusAddition) {
		this.statusAddition = statusAddition;
	}

	public void setBatteryVehicleInfo(BatteryVehicleInfo.Builder batteryVehicleInfo) {
		this.batteryVehicleInfo = batteryVehicleInfo;
	}

	public long getElectricVehicle() {
		return electricVehicle;
	}

	public void setElectricVehicle(long electricVehicle) {
		this.electricVehicle = electricVehicle;
	}

	public int getBatteryPower() {
		return batteryPower;
	}

	public void setBatteryPower(int batteryPower) {
		this.batteryPower = batteryPower;
	}

	public long getMileage() {
		return mileage;
	}

	public void setMileage(long mileage) {
		this.mileage = mileage;
	}

	public List<ModuleVoltageEntity> getModuleVoltageList() {
		return moduleVoltageList;
	}

	public void setModuleVoltageList(List<ModuleVoltageEntity> moduleVoltageList) {
		this.moduleVoltageList = moduleVoltageList;
	}

	public LCLocationData.VehicleBreakdownAddition.Builder getBreakdownAddition() {
		return breakdownAddition;
	}

	public void setBreakdownAddition(LCLocationData.VehicleBreakdownAddition.Builder breakdownAddition) {
		this.breakdownAddition = breakdownAddition;
	}

	/** 电池组（SOC）剩余电量、BMS基本状态、电池组平均温度、电池组充放电电流、电池组总电压 */
	public static final byte[] IdOf10F8159E = Convert.hexStringToBytes("10F8159E");
	/** CANID --- 总里程 */
	public static final byte[] IdOf18F40217 = Convert.hexStringToBytes("18F40217");
	/** 最高允许充电端电压、最高允许充电电流、充电机状态、充电线连接状态、电池充电状态 */
	public static final byte[] IdOf18F8229E = Convert.hexStringToBytes("18F8229E");
	/** BMS ***/
	/** 整车漏电报警 */
	public static final byte[] IdOf1801d0d7 = Convert.hexStringToBytes("1801D0D7");
	/** 电机温度、电机控制器温度、电机温度报警、MCU 故障状态、MCU系统故障代码 */
	public static final byte[] IdOf18F501F0 = Convert.hexStringToBytes("18F501F0");
	/** 电池组系统故障信息 */
	public static final byte[] IdOf10F81D9E = Convert.hexStringToBytes("10F81D9E");
	/** 电池组允许最高温度、电池组允许最低温度、电池组允许最低使用 SOC */
	public static final byte[] IdOf10F8169E = Convert.hexStringToBytes("10F8169E");
	/*** 仪表 ***/
	/** 整车状态、车辆速度、档位状态、变速器故障、VCU 故障、VCU 系统故障码 */
	public static final byte[] IdOf18F101D0 = Convert.hexStringToBytes("18F101D0");
	/** 续航里程、油门踏板状态、制动踏板状态、整车模式信息、整车部分开关量信息、电附件使能 */
	public static final byte[] IdOf18F103D0 = Convert.hexStringToBytes("18F103D0");
	/** 驱动电机转速 */
	public static final byte[] IdOf18F502F0 = Convert.hexStringToBytes("18F502F0");
	/** EEC1 转矩控制模式、驾驶员需求发动机转矩百分比、实际发动机转矩百分比、发动机转速 */
	public static final byte[] IdOf0CF00400 = Convert.hexStringToBytes("0CF00400");
	// 0x0CF00300
	/** EEC2 加速踏板低怠速开关、加速踏板Kickdown开关、加速踏板开度、当前速度下，负载百分比 */
	public static final byte[] IdOf0CF00300 = Convert.hexStringToBytes("0CF00300");

	/** EEC3 名义摩擦力矩百分比 */
	public static final byte[] IdOf18FEDF00 = Convert.hexStringToBytes("18FEDF00");

	/** CCVS 名义驻车制动器开关、巡航控制开关状态、制动开关、巡航控制设置速度 */
	public static final byte[] IdOf18FEF100 = Convert.hexStringToBytes("18FEF100");

	/** VD 日里程 */
	public static final byte[] IdOf18FEE000 = Convert.hexStringToBytes("18FEE000");

	/** LFE 燃油消耗率 */
	public static final byte[] IdOf18FEF200 = Convert.hexStringToBytes("18FEF200");

	/** EngTem 发动机温度 */
	public static final byte[] IdOf18FEEE00 = Convert.hexStringToBytes("18FEEE00");

	/** TI1 尿素箱 */
	public static final byte[] IdOf18FE5600 = Convert.hexStringToBytes("18FE5600");
	/** TCO1 输出轴转速 */
	public static final byte[] IdOf0CFE6CEE = Convert.hexStringToBytes("0CFE6CEE");

	/** LFC 发动机累计燃油总消耗 */
	public static final byte[] IdOf18FEE900 = Convert.hexStringToBytes("18FEE900");
	/** 发动机运行时间 */
	public static final byte[] IdOf18FEE500 = Convert.hexStringToBytes("18FEE500");

	/** 发动机液位/压力 */
	public static final byte[] IdOf18FEEF00 = Convert.hexStringToBytes("18FEEF00");

	/** 发动机进气状况 */
	public static final byte[] IdOf18FEF600 = Convert.hexStringToBytes("18FEF600");

	/** AMCON 环境条件 */
	public static final byte[] IdOf18FEF500 = Convert.hexStringToBytes("18FEF500");

	/** ShutDwn */
	public static final byte[] IdOf18FEE400 = Convert.hexStringToBytes("18FEE400");

	/** VDHR 整车里程信息 */
	public static final byte[] IdOf18FEC117 = Convert.hexStringToBytes("18FEC117");

	/** WFI/油中有水指示 */
	public static final byte[] IdOf18FEFF00 = Convert.hexStringToBytes("18FEFF00");
	/** DD/车辆当前油量 */
	public static final byte[] IdOf18FEFC21 = Convert.hexStringToBytes("18FEFC21");

	/** DM1/DM2 */
	public static final byte[] IdOf18FECA00 = Convert.hexStringToBytes("18FECA00");
	/** DM1/DM2*/
	public static final byte[] IdOf18FECB00 = Convert.hexStringToBytes("18FECB00");

	/** DM1/DM2*/
	public static final byte[] IdOf18EBFF00 = Convert.hexStringToBytes("18EBFF00");

	/** 电子传动控制 档位*/
	public static final byte[] IdOf18F00503 = Convert.hexStringToBytes("18F00503");

	/** TI1 尿素箱*/
	public static final byte[] IdOf18FE563D = Convert.hexStringToBytes("18FE563D");


	/** 整车电源*/
	public static final byte[] IdOf18FEF700 = Convert.hexStringToBytes("18FEF700");

	/** MFD1自定义报文*/
	public static final byte[] IdOf18FF0800 = Convert.hexStringToBytes("18FF0800");
	/** FanDrv*/
	public static final byte[] IdOf18FEBD00 = Convert.hexStringToBytes("18FEBD00");

	/** ERC1电控缓速器1*/
	public static final byte[] IdOf18F0000F = Convert.hexStringToBytes("18F0000F");
	/** VDD 仪表显示 */
	public static final byte[] IdOf18FEE017 = Convert.hexStringToBytes("18FEE017");
	/** DD/车辆当前油量 */
	public static final byte[] IdOf18FEFCFD = Convert.hexStringToBytes("18FEFCFD");

	/** DD/车辆当前油量 */
	public static final byte[] IdOf18FEFC17 = Convert.hexStringToBytes("18FEFC17");
	/** 广播、多包故障、发动机配置 */
	public static final byte[] IdOf18ECFF00 = Convert.hexStringToBytes("18ECFF00");

	public static final byte[] IdOf18FEF2FD = Convert.hexStringToBytes("18FEF2FD");

	public static final byte[] IdOf18FFEB4E = Convert.hexStringToBytes("18FFEB4E");
	public static final byte[] IdOf18FFEB4F = Convert.hexStringToBytes("18FFEB4F");
	public static final byte[] IdOf18FFEB50 = Convert.hexStringToBytes("18FFEB50");
	public static final byte[] IdOf18FFEB51 = Convert.hexStringToBytes("18FFEB51");
}
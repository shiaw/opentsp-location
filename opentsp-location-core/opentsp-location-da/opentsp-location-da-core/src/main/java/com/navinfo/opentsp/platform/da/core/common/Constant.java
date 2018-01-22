package com.navinfo.opentsp.platform.da.core.common;

public class Constant {
	/** 链路唯一标识Key */
	public static final String UniqueMark = "UniqueMark";
	/** mongodb collection 名称 */
	/*public static final String[] COLLECTION_NAMES ={"ACCStatusAlarm","AreaINOUTAlarm","AreaOverspeedAlarm","DriverLogin",
		"ElectricPowerOffAlarm","EmergencyAlarm","FatigueAlarm","Milages","OverspeedAlarm","OvertimeParkingAlarm",
		"PowerStayInLowerAlarm","RLineOverspeedAlarm","RLineOvertimeAlarm","RouteINOUTAlarm","TerminalOnOffLineStatus",
		"TotalAlarmInfo","WFlow","OvertimeParkingInArea","VehicleDrivingNumberInGrid","VehicleDrivingNumberInArea","FaultCode"};*/
	public static final String[] COLLECTION_NAME = {"MileageConsumption","TerminalOnOffLineStatus","WFlow","OvertimeParkingInArea","StaytimeParkingInArea","VehicleDrivingNumberInGrid",
		"VehicleDrivingNumberInArea","FaultCode","StagnationTimeout"};

	public class PropertiesKey {

		/**
		 * 统计计算与分页处理参数
		 *
		 * @author ss
		 *
		 */
		public class DSAConfig {
			public final static int DURATION_TIME = 20;               //实时动态缓存存储有效时间 （s）
			public final static int DEFAULT_BUFFER_SIZE = 100;        //动态分页缓存批量预处理记录条数
		}


		/**
		 * ws的参数 对应ws配置文件的key
		 * @author ss
		 *
		 */
		public class WebServicePropertiesKey {
			public final static String wsIp = "WS.IP";//本身ip
			public final static String centerip = "WS.CENTER.IP";//总部ip
			public final static String expirationtime = "WS.EXPIRATION.TIME";//超时时间
			public final static String isCenter = "IS.CENTER";//是否是总部
			public final static String wsPort = "WS.PORT";//本身端口号
			public final static String wsModule = "WS.MODULE";//要发布的ws
		}
		/**
		 * 数据同步的参数
		 * @author ss
		 *
		 */
		public class SynchronousDataParameters {
			public final static String TerminalWebService = "TerminalWebService";//终端
			public final static String DictWebService = "DictWebService";//字典
			public final static String ConfigWebService = "ConfigWebService";//配置（节点和服务配置）
		}
		/**
		 * 操作类型参数
		 * @author ss
		 *
		 */
		public class OperationTypeParameters {
			public final static String save = "save";
			public final static String delete = "delete";
			public final static String update = "update";
			public final static int operating= 2;//操作中

		}
		/**
		 * mysql数据库表明
		 * @author ss
		 *
		 */
		public class DBTableName {
			public final static String LC_DISTRICT = "LC_DISTRICT";
			public final static String LC_TERMINAL_PROTO_MAPPING = "LC_TERMINAL_PROTO_MAPPING";
			public final static String LC_DICT_TYPE = "LC_DICT_TYPE";
			public final static String LC_DICT = "LC_DICT";
			public final static String LC_NODE_CONFIG = "LC_NODE_CONFIG";
			public final static String LC_SERVICE_CONFIG = "LC_SERVICE_CONFIG";
			public final static String LC_TERMINAL_INFO = "LC_TERMINAL_INFO";

		}

		/**
		 * TerminalInfoManage接口中用到的常量
		 * @author ss
		 *
		 */
		public class TerminalInfoManageConstant {
			public final static String TERMINALINFO = "terminalInfo";//终端基本信息
			public final static String TERMINALREGISTERINFO = "terminalRegisterInfo";//终端注册信息
			public final static String TOPIC = "topic";//区域信息
			public final static String DETAILED = "detailed";//区域数据
		}

		/***
		 * 业务系统设计的报警类型，对应的Collection命名前缀
		 *
		 * @author claus
		 *
		 */
		public class AlarmTypeTableMapping {
			public final static String AlarmOverSpeed = "OverspeedAlarm";
			public final static String AlarmAreaINOUT = "AreaINOUTAlarm";
			public final static String AlarmAreaOverspeed = "AreaOverspeedAlarm";
			public final static String AlarmRouteINOUT = "RouteINOUTAlarm";
			public final static String AlarmRLineOverspeed = "RLineOverspeedAlarm";
			public final static String AlarmRLineOvertime = "RLineOvertimeAlarm";
			public final static String AlarmOvertimeParking = "OvertimeParkingAlarm";
			public final static String AlarmFatigue = "FatigueAlarm";
			public final static String AlarmEmergency = "EmergencyAlarm";
			public final static String AlarmPowerStayInLower = "PowerStayInLowerAlarm";
			public final static String AlarmElectricPowerOff = "ElectricPowerOffAlarm";
			public final static String AlarmACCStatus = "ACCStatusAlarm";
			public final static String AlarmMilages = "Milages";
			public final static String AlarmDriverLogin = "DriverLogin";
			public final static String AlarmTerminalOnOffLineStatus = "TerminalOnOffLineStatus";
			public final static String AlarmWFlow = "WFlow";
			public final static String AlarmCommon = "CommonSummary";
			public final static String AlarmAll = "TotalAlarmInfo";
			public final static String AlarmOvertimePark = "OvertimeParkingInArea";
			public final static String AlarmStaytimePark = "StaytimeParkingInArea";
			public final static String VehicleDrivingNumberInGrid = "VehicleDrivingNumberInGrid";
			public final static String VehicleDrivingNumberInArea = "VehicleDrivingNumberInArea";
			public final static String FaultCode = "FaultCode";
			public final static String StagnationTimeout = "StagnationTimeout";
		}


		/**
		 * TerminalInfoManage接口中用到的常量
		 * @author ss
		 *
		 */
		public class UpperServiceManageConstant {
			public final static String SERVERIDENTIFIES = "serverIdentifies";//服务标识
		}

		/**
		 * 动态口令定时任务中用到的常量
		 * @author ss
		 *
		 */
		public class DynamicPasswordConstant {
			public final static long OUTOFTIME = 30*60*100l;//服务标识
		}
		
	}
	public class ConfigKey{
		public final static String GPS_LOCAL_TO_REDIS_COMMIT_SIZE = "GPS.LOCAL.TO.REDIS.COMMIT.SIZE";
		public final static String GPS_LOCAL_TO_REDIS_COMMIT_TIME = "GPS.LOCAL.TO.REDIS.COMMIT.TIME";
		
		public final static String WS_CENTERIP = "WS.CENTERIP";
	}
}

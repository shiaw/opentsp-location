package com.navinfo.opentsp.gateway.tcp.proto.location.util;

import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
/**
 * @author:qingqi
 * */
public class Constant {
	/**链路唯一标识Key*/
	public static final String UniqueMark = "UniqueMark";
	public static final long _min_gps_date = DateUtils.calendar(2014, 1, 1).getTimeInMillis() / 1000;
	public static final long _max_gps_date = DateUtils.calendar(2016, 12, 30).getTimeInMillis() / 1000;
//	public static byte pkBegin = 0x7E;
//	public static byte pkEnd = 0x7B;
//	public static final byte[] escapeByte = new byte[]{0x7E,0x7D,0x7B,0x7A};
//	public static final byte[][] toEscapeByte = new byte[][]{{0x7D,0x02},{0x7D,0x01},{0x7A,0x02},{0x7A,0x01}};

	//	public class InternalCacheKeyPrefix{
//		/**补传数据*/
//		public static final String PassUpData = "PASSUP_DATA_";
//		/**服务实例,约定：前缀+远程IP_远程端口;实例：SERVICE_INSTANCE_127.0.0.1_9999*/
//		public static final String ServiceInstance = "SERVICE_INSTANCE";
//		/**当前MM节点序号*/
//		public static final String CurrentMMNodeSerial = "_CURRENT_MM_NODE_SERIAL";
//
//		public static final String TERMINAL_SERVER_STATUS = "TERMINAL_SERVER_STATUS";
//		public static final String TA_DP_CLIENT_STATUS = "TA_DP_CLIENT_STATUS";
//		public static final String TA_DA_CLIENT_STATUS = "TA_DA_CLIENT_STATUS";
//
//	}
	public static final byte[] TR_PACKET_BEGIN = {0x55 , 0X7A};
	public static class DrivingRecorderCommand{
		public static class Version2012{
			public static final int CollectVersion = 0x00;
			public static final int CollectCurrectDriver = 0x01;
			public static final int CollectCurrentTime = 0x02;
			public static final int CollectMileage = 0x03;
			public static final int CollectPulse = 0x04;
			public static final int CollectVehicleInfo = 0x05;
			public static final int CollectStatusSignalConfig = 0x06;
			public static final int CollectUniqueMark = 0x07;
			public static final int CoolectSpeed = 0x08;
			public static final int CollectGpsLocation = 0x09;
			public static final int CollectDoubt = 0x10;
			public static final int CollectTimeoutDriving = 0x11;
			public static final int CollectDriverRecord = 0x12;
			public static final int CollectPowerSupplyRecord =  0x13;
			public static final int CorrectParamterUpdateRecord = 0x14;
			public static final int CorrectSpeedStatus = 0x15;
		}

	}
	public class JTProtocol {
		public static final int TerminalCommonResponse = 0x0001;
		public static final int PlatformCommonResponse = 0x8001;
		public static final int TerminalHeartbeat = 0x0002;
		public static final int TerminalRegistration = 0x0100;
		public static final int TerminalRegistrationResponse = 0x8100;
		public static final int TerminalLogout = 0x0101;
		public static final int SetTerminalParameters = 0x8103;
		public static final int QueryTerminalParameters = 0x8104;
		public static final int QueryAppointTerminalParameters = 0x8106;
		public static final int QueryTerminalProperties = 0x8107;
		public static final int QueryTerminalParametersResponse = 0x0104;
		public static final int TerminalControl = 0x8105;
		public static final int TerminalLocationReporting = 0x0200;
		public static final int QueryTerminalLocation = 0x8201;
		public static final int QueryTerminalLocationResponse = 0x0201;
		public static final int TerminalLocationTemporaryTrack = 0x8202;
		public static final int TerminalDispatchMessage = 0x8300;
		public static final int TerminalPhoneCallBack = 0x8400;
		public static final int SetTerminalPhotoBookSetting = 0x8401;
		public static final int CarControl = 0x8500;
		public static final int CarControlResponse = 0x0500;
		public static final int TerminalTachographDataCollection = 0x8700;
		public static final int TerminalTachographDataReported = 0x0700;
		public static final int SetTerminalTachographParameters = 0x8701;
		public static final int TerminalElectronicWaybillReported = 0x0701;
		public static final int TerminalDriverInformationReported = 0x0702;
		public static final int TerminalMultimediaEventReported = 0x0800;
		public static final int TerminalMultimediaDataReported = 0x0801;
		public static final int TerminalMultimediaDataReportedResponse = 0x8800;
		public static final int TerminalCamera = 0x8801;
		public static final int TerminalDownPassThrough = 0x8900;
		public static final int TerminalUpPassThrough = 0x0900;
		public static final int SetTerminalCircleArea = 0x8600;
		public static final int DeleteTerminalCircleArea = 0x8601;
		public static final int SetTerminalRectangleArea = 0x8602;
		public static final int DeleteTerminalRectangleArea = 0x8602;
		public static final int SetTerminalPolygonArea = 0x8604;
		public static final int DeleteTerminalPolygonArea = 0x8605;
		public static final int SetTerminalRoute = 0x8606;
		public static final int DeleteTerminalRoute = 0x8607;
		public static final int CallListener = 0x8400;
		public static final int TakePhotography = 0x8801;
		public static final int MultimediaUploadRes = 0x8800;
		public static final int VehicleControl = 0x8500;
		public static final int TerminalStatusControl = 0x8F34;//4.4.4.23	外设控制
		public static final int CANBUSDataQuery   = 0x8F35;//4.4临时CAN数据查询
		public static final int CANRemoveControl = 0x8F40;//	CAN防拆命令
		public static final int TerminalSetting = 0x8103;
		public static final int ParameterQuery = 0x8104;
		public static final int LocationQuery = 0x8201;
		public static final int DownPassThrough = 0x8900;
		public static final int UpPassThrough = 0x0900;
		public static final int EventSetting = 0x8301;
		public static final int AskQuestion = 0x8302;
		public static final int InfoDemandMenu = 0x8303;
		public static final int InfoDemandOrCancel = 0x0303;
		public static final int InfoService = 0x8304;
		public static final int TemporaryLocationControl = 0x8202;
		public static final int DriverInfoUpPassThrough = 0x8702;
		public static final int MediaDataQuery = 0x8802;
		public static final int SaveMultimediaUpload = 0x8803;
		public static final int RecordStartCommand = 0x8804;
		public static final int SaveMultimediaSingleUpload = 0x8805;
		public static final int PlatformRSAKey = 0x8A00;
		public static final int TerminalUpgradePackage = 0x8108;
		public static final int ConfirmAlarmMessage = 0x8203;
		public static final int TerminalBindSetting = 0x8F20;
		public static final int TerminalLockSetting = 0x8F10;

//		public static final int T = 0x8304;

	}

	public static class ConfigKey{
		public static final String DP_NODE = "dpNode";
		public static final String DA_NODE = "daNode";
		public static final String districtCode = "DISTRICT.CODE";


		public static final String NODE_CODE = "NODE.CODE";
		public static final String DISTRICT_CODE = "DISTRICT.CODE";
		public static final String MM_MASTER_CODE = "MM.MASTER.CODE";
		public static final String MM_MASTER_IP = "MM.MASTER.IP";
		public static final String MM_MASTER_PORT = "MM.MASTER.PORT";

		public static final String MM_SLAVE_CODE = "MM.SLAVE.CODE";
		public static final String MM_SLAVE_IP = "MM.SLAVE.IP";
		public static final String MM_SLAVE_PORT = "MM.SLAVE.PORT";

		public static final String MM_RECONNECT_INTERVAL = "MM.RECONNECT.INTERVAL";

		/**终端掉线时间间隔*/
		public static final String onOffLineTime = "ON.OFF.LINE.TIME";
		public static final String REQUEST_NODE_INTERVAL = "REQUEST.NODE.INTERVAL";
	}

	/**
	 * 里程、油耗、速度标记
	 * */
	//仪表里程
	public static final int BOARD_MILEAGE = 1;
	//CAN里程
	public static final int CAN_MILEAGE = 2;
	//积分里程
	public static final int DIFFERENTIAL_MILEAGE = 3;
	//GPS里程
	public static final int GPS_MILEAGE = 4;

	//燃油总消耗
	public static final int TOTAL_FUEL_CONSUMPTION = 1;
	//积分油耗
	public static final int INTEGRAL_CONSUMPTION = 2;

	//仪表车速
	public static final int BOARD_SPEED = 1;
	//车轮车速
	public static final int WHEEL_SPEED = 2;
	//GPS速度
	public static final int GPS_SPEED = 3;
	//未打上标记
	public static final int NO_FLAG = -1;

}

package com.navinfo.opentsp.gateway.tcp.proto.location.util.lc;

import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleBreakdown;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocationAdditionProcess_LittleEndian {

	private static final Logger logger = LoggerFactory.getLogger(LocationAdditionProcess_LittleEndian.class);
	/**
	 * 状态数据
	 * @param statusAddition
	 * @return
	 */
	public static LCLocationData.VehicleStatusAddition.Builder getStatusAddition(byte[] statusAddition,String uniqueMark){
		/**
		 * 附加信息的格式
		 * 
		 * 状态总数     是否故障         扩展ID(PGN)              数据（位置）                                                                                                         扩展ID(PGN)          数据（位置）                                         扩展ID(PGN)             数据（位置）                                 
		 *   03    01          18 FE F6 00       00    00    00    00    00    00    00    00    18 FE F6 00    00 00 00 00 00 00 00 00   18 FE F6 00     00 00 00 00 00 00 00 00
		 *                                       Byte1 Byte2 Byte3 Byte4 Byte5 Byte6 Byte7 Byte8
		 */
		LCLocationData.VehicleStatusAddition.Builder vsaddBuilder = LCLocationData.VehicleStatusAddition.newBuilder();
		int statusAdditionIndex = 0;//开始下标
//		String hexStatusAddition = Convert.bytesToHexString(statusAddition);
//		logger.info("开始打印终端【"+uniqueMark+"】附加信息----------------------------------------------------------------");
		if(statusAddition.length >= 2){
//			int statusTotal = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, statusAdditionIndex, 1), 1);
//			int isBreakdown = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+1, 1), 1);
			statusAdditionIndex +=2;//下标从扩展ID开始
			while(statusAddition.length>statusAdditionIndex){
				LCVehicleStatusData.VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
				
				int pgnId = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, statusAdditionIndex, 4), 4);
//				String hexStrId = Convert.bytesToHexString((ArraysUtils.subarrays(statusAddition, statusAdditionIndex, 4)));
				switch(pgnId){
				case 0x18FEE000:  //	Byte5-8	整车里程（0.125Km/bit）
					if(!"FFFFFFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 4, 4)))){
						double mileage = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+4, 4));//截取的 5~8
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (mileage*100*0.125));//乘以100 存储
						statusData.setTypes(LCStatusType.StatusType.mileage);
						vsaddBuilder.addStatus(statusData.build());
						
						print(uniqueMark,"0x18FEE000","mileage Byte5-8	整车里程（0.125Km/bit）:" ,(long) (mileage*0.125),statusAddition, statusAdditionIndex );
					}
					
					statusAdditionIndex +=12;
					break;
				case 0x18FEF600:  //    Byte1	颗粒捕集器进气压力(0.5kPa/bit)  ; Byte2	相对增压压力(2kPa/bit)  ; Byte4	绝对增压压力(2kPa/bit) ; Byte6-7	排气温度(0.03125℃/bit,Offset:-273℃)
					
					//Byte1	颗粒捕集器进气压力(0.5kPa/bit)  ;
					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1)))){
						double dpfPressure = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+0, 1));//Byte1
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (dpfPressure*0.5*100));
						statusData.setTypes(LCStatusType.StatusType.dpfPressure);
						vsaddBuilder.addStatus(statusData.build());
						
						print(uniqueMark,"0x18FEF600","dpfPressure Byte1	颗粒捕集器进气压力(0.5kPa/bit):" ,(long) (dpfPressure*0.5),statusAddition, statusAdditionIndex );
					}
					
					//Byte2	相对增压压力(2kPa/bit)
					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 1)))){
						double relativePressure = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+1, 1));//Byte2
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (relativePressure*2*100));
						statusData.setTypes(LCStatusType.StatusType.relativePressure);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEF600","relativePressure Byte2	相对增压压力(2kPa/bit):" ,(long) (relativePressure*2),statusAddition, statusAdditionIndex );
					}
					
					//Byte4	绝对增压压力(2kPa/bit) ; 
					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 1)))){
						double absolutePressure = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+3, 1));//Byte4
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (absolutePressure*2*100));
						statusData.setTypes(LCStatusType.StatusType.absolutePressure);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEF600","absolutePressure Byte4	绝对增压压力(2kPa/bit):" ,(long) (absolutePressure*2),statusAddition, statusAdditionIndex );
					}
					
					//Byte6-7	排气温度(0.03125℃/bit,Offset:-273℃)
					if(!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 5, 2)))){
						double exhaustTem = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+5, 2));//Byte6-7
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) ((exhaustTem*0.03125-273)*100));
						statusData.setTypes(LCStatusType.StatusType.exhaustTem);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEF600","exhaustTem  Byte6-7	排气温度(0.03125℃/bit,Offset:-273℃):" ,(long) ((exhaustTem*0.03125-273)),statusAddition, statusAdditionIndex );
					}
					
					statusAdditionIndex +=12;
					break;
					
				case 0X18FEF500:  //	Byte1	大气压力(0.5kPa/bit,Offset:0hPa) ; Byte2-3	发动机舱内部温度(Not realized，Set 0xFFFF) ; Byte4-5	大气温度(0.03125℃/bit,Offset:-273℃) ; Byte6	进气温度(1℃/bit,Offset:-40℃);Byte7-8	路面温度(Not realized，Set 0xFFFF)
					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1)))){
						double atmosphericPressure = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+0, 1));//Byte1
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (atmosphericPressure*100*0.5));
						statusData.setTypes(LCStatusType.StatusType.atmosphericPressure);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0X18FEF500","atmosphericPressure Byte1	大气压力(0.5kPa/bit,Offset:0hPa) :" ,(long) (atmosphericPressure*0.5),statusAddition, statusAdditionIndex );
						
					}
					
					
					if(!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 2)))){
						double engineTem = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+1, 2));//Byte2-3
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (engineTem*100));
						statusData.setTypes(LCStatusType.StatusType.engineTem);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0X18FEF500","engineTem Byte2-3	发动机舱内部温度(Not realized，Set 0xFFFF):" ,(long) (engineTem),statusAddition, statusAdditionIndex );
						
					}
					

					if(!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 2)))){
						double atmosphericTem = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+3, 2));//Byte4-5
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) ((atmosphericTem*0.03125-273)*100));
						statusData.setTypes(LCStatusType.StatusType.atmosphericTem);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0X18FEF500","atmosphericTem  Byte4-5	大气温度(0.03125℃/bit,Offset:-273℃):" ,(long) ((atmosphericTem*0.03125-273)),statusAddition, statusAdditionIndex );
						
					}
					

					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 5, 1)))){
						double intakeAirTem = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+5, 1));//Byte6
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) ((intakeAirTem-40)*100));
						statusData.setTypes(LCStatusType.StatusType.intakeAirTem);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0X18FEF500","intakeAirTem  Byte6	进气温度(1℃/bit,Offset:-40℃);:" ,(long) ((intakeAirTem-40)),statusAddition, statusAdditionIndex );
						
					}
					

					if(!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 6, 2)))){
						double pavementTem = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+6, 2));//Byte7-8
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (pavementTem*100));
						statusData.setTypes(LCStatusType.StatusType.pavementTem);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0X18FEF500","pavementTem  Byte7-8	路面温度(Not realized，Set 0xFFFF):" ,(long) (pavementTem),statusAddition, statusAdditionIndex );
						
					}
					
					
					
					statusAdditionIndex +=12;
					break;

				case 0x18FEE400:  //	Byte4	冷启动灯（1表示正常，0表示异常）
					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 1)))){
						double lampStatus = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+3, 1));//Byte4
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (lampStatus*100));
						statusData.setTypes(LCStatusType.StatusType.lampStatus);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEE400","pavementTem Byte4	冷启动灯（1表示正常，0表示异常）:" ,(long) (lampStatus),statusAddition, statusAdditionIndex );
					}
					
					statusAdditionIndex +=12;
					break;
				case 0x18FEFF00:  //	Byte1Bit1-2	油中有水指示（1表示正常，0表示异常）:   Bit1-2: 000011 = 3
					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1)))){
						int waterInOilStatus =  Convert.byte2IntLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+0, 1));//截取的 1
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						switch(waterInOilStatus&3){// 00:表示异常   01:表示正常    10:Error Value   11:Not available
							case 0:
								statusData.setStatusValue(0);
								statusData.setTypes(LCStatusType.StatusType.waterInOilStatus);
								break;
							case 1:
								statusData.setStatusValue(1*100);
								statusData.setTypes(LCStatusType.StatusType.waterInOilStatus);
								break;
							case 2://10:Error Value   11:Not available   认为是数据异常
								statusData.setStatusValue(0);
								statusData.setTypes(LCStatusType.StatusType.waterInOilStatus);
								break;
							case 3://10:Error Value   11:Not available   认为是数据异常
								statusData.setStatusValue(0);
								statusData.setTypes(LCStatusType.StatusType.waterInOilStatus);
								break;
							default:
								break;
						}
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEFF00","waterInOilStatus Byte1Bit1-2	油中有水指示（1表示正常，0表示异常）:   Bit1-2: 000011 = 3:" ,waterInOilStatus&3,statusAddition, statusAdditionIndex );
					}
					statusAdditionIndex +=12;
					break;
				case 0x18FEE900:  //燃油消耗总量(升) byte 5-8 小端解析有，大端解析没有
					if(!"FFFFFFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 4, 4)))){
						double totalFuelConsumption =  Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+4, 4));//截取的 5~8
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (totalFuelConsumption*100*0.5));
						statusData.setTypes(LCStatusType.StatusType.totalFuelConsumption);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEE900","totalFuelConsumption Byte5-8	车辆当前油量（0.5 L/bit,）: " ,(long) (totalFuelConsumption*100*0.5),statusAddition, statusAdditionIndex );
					}
					statusAdditionIndex +=12;
					break;
				case 0x0CF00400:  //	Byte4-5	车辆当前转速(0.125rpm/bit)
					if(!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 2)))){
						double rotation =  Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+3, 2));//Byte4-5
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (rotation*100*0.125));
						statusData.setTypes(LCStatusType.StatusType.rotation);
						vsaddBuilder.addStatus(statusData.build());
						
						print(uniqueMark,"0x0CF00400","rotation Byte4-5	车辆当前转速(0.125rpm/bit): " ,(long) (rotation*0.125),statusAddition, statusAdditionIndex );
						
					}
					
					statusAdditionIndex +=12;
					break;
				case 0x18FEEF00://Byte 1	燃油压力（4Kpa/bit）  ;Byte 3	机油液位（0.4%/bit） ;Byte 4	机油压力（4Kpa/bit）;Byte 8	冷却液液位（0.4%/bit）.
					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1)))){
						double fuelPressure = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+0, 1));//Byte1
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (fuelPressure*100*4));
						statusData.setTypes(LCStatusType.StatusType.fuelPressure);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEEF00","fuelPressure Byte 1	燃油压力（4Kpa/bit） : " ,(long) (fuelPressure*4),statusAddition, statusAdditionIndex );
					}
					

					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 2, 1)))){
						double oilLevel = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+2, 1));//Byte 3
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (oilLevel*100*0.4));
						statusData.setTypes(LCStatusType.StatusType.oilLevel);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEEF00","oilLevel Byte 3	机油液位（0.4%/bit）: " ,(long) (oilLevel*0.4),statusAddition, statusAdditionIndex );
					}
					
					

					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 1)))){
						double oilPressure = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+3, 1));//Byte 4
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (oilPressure*100*4));
						statusData.setTypes(LCStatusType.StatusType.oilPressure);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEEF00","oilPressure Byte 4	机油压力（4Kpa/bit）:" ,(long) (oilPressure*4),statusAddition, statusAdditionIndex );
					}
					
					

					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 7, 1)))){
						double coolantLevel = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+7, 1));//Byte 8	
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (coolantLevel*100*0.4));
						statusData.setTypes(LCStatusType.StatusType.coolantLevel);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEEF00","coolantLevel Byte 8	冷却液液位（0.4%/bit）:" ,(long) (coolantLevel*0.4),statusAddition, statusAdditionIndex );
					}
					
					statusAdditionIndex +=12;
					break;
				case 0x18FEEE00://Byte 1	发动机冷却水温度（1℃/bit，Offset:-40℃）;Byte 2	燃油温度（1℃/bit，Offset:-40℃）;Byte3-4	机油温度（0.03125℃/bit，Offset:-273℃）
					
					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1)))){
						double coolingWaterTem = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+0, 1));//Byte 1							statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long)  (coolingWaterTem*1-40)*100);
						statusData.setTypes(LCStatusType.StatusType.coolingWaterTem);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEEE00","coolingWaterTem Byte 1	发动机冷却水温度（1℃/bit，Offset:-40℃）;: " ,(long)  (coolingWaterTem*1-40),statusAddition, statusAdditionIndex );
						
					}
					

					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 1)))){
						double fuelTem = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+1, 1));//Byte 2	
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long)  (fuelTem*1-40)*100);
						statusData.setTypes(LCStatusType.StatusType.fuelTem);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEEE00","fuelTem Byte 2	燃油温度（1℃/bit，Offset:-40℃）;:" ,(long)  (fuelTem*1-40),statusAddition, statusAdditionIndex );
						
					}
					

					if(!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 2, 2)))){
						double oilTem = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+2, 2));//Byte 3~4	
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long)  (oilTem*0.03125-273)*100);
						statusData.setTypes(LCStatusType.StatusType.oilTem);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEEE00","oilTem Byte3-4	机油温度（0.03125℃/bit，Offset:-273℃））:" ,(long)  (oilTem*0.03125-273),statusAddition, statusAdditionIndex );
						
					}
					
					statusAdditionIndex +=12;
					break;
				case 0x18FE5600://Byte 1	尿素箱液位（0.4%/bit，Offset:0）;Byte 2	尿素箱温度（1℃/bit，Offset:-40℃）
					
					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1)))){
						double ureaTankLiquidLevel = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+0, 1));//Byte1
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (ureaTankLiquidLevel*100*0.4));
						statusData.setTypes(LCStatusType.StatusType.ureaTankLiquidLevel);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FE5600","ureaTankLiquidLevel Byte 1	尿素箱液位（0.4%/bit，Offset:0）;:" ,(long) (ureaTankLiquidLevel*0.4),statusAddition, statusAdditionIndex );
						
					}
					

					if(!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 1)))){
						double ureaTankTem = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+1, 1));//Byte 2	
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (ureaTankTem*1-40)*100);
						statusData.setTypes(LCStatusType.StatusType.ureaTankTem);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FE5600","ureaTankTem Byte 8	Byte 2	尿素箱温度（1℃/bit，Offset:-40℃）:" ,(long) (ureaTankTem*1-40),statusAddition, statusAdditionIndex );
						
					}
					
					statusAdditionIndex +=12;
					break;
				case 0x18FEE500://Byte1-4	发动机累计运行时间（0.05h/bit） ;Byte5-8	发动机累计转数（1000rpm/bit）
					if(!"FFFFFFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 4)))){
						double cumulativeRunningTime = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+0, 4));//Byte1
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (cumulativeRunningTime*100*0.05*3600));//转化为秒
						statusData.setTypes(LCStatusType.StatusType.cumulativeRunningTime);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEE500","cumulativeRunningTime Byte1-4	发动机累计运行时间（0.05h/bit） ;:" ,(long) (cumulativeRunningTime*0.05),statusAddition, statusAdditionIndex );
						
					}
					

					if(!"FFFFFFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 4, 4)))){
						double cumulativeTurningNumber = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+4, 4));//Byte 2	
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (cumulativeTurningNumber*1000)*100);
						statusData.setTypes(LCStatusType.StatusType.cumulativeTurningNumber);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEE500","cumulativeTurningNumber Byte5-8	发动机累计转数（1000rpm/bit）:" ,(long) (cumulativeTurningNumber*1000),statusAddition, statusAdditionIndex );
						
					}
					
					statusAdditionIndex +=12;
					break;
				case 0x18FEF200://Byte1-2	发动机燃油消耗率（0.05L/h/bit）;Byte5-6	平均燃油消耗率（0.001953125Km/L/bit）
					
					if(!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 2)))){
						double fuelConsumptionRate = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+0, 2));//Byte1
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (fuelConsumptionRate*1000*100*0.05)); 
						statusData.setTypes(LCStatusType.StatusType.fuelConsumptionRate);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEF200","fuelConsumptionRate Byte1-2	发动机燃油消耗率（0.05L/h/bit）;:" ,(long) (fuelConsumptionRate*0.05),statusAddition, statusAdditionIndex );
						
					}
					

					if(!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 4, 2)))){
						double averageFuelConsumption = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+4, 2));//Byte 2	
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((long) (averageFuelConsumption*0.001953125)*100);
						statusData.setTypes(LCStatusType.StatusType.averageFuelConsumption);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEF200","averageFuelConsumption Byte5-6	平均燃油消耗率（0.001953125Km/L/bit）:" ,(long) (averageFuelConsumption*0.001953125),statusAddition, statusAdditionIndex );
						
					}
					statusAdditionIndex +=12;
					break;
					
//				case 0X00FEF616:
//					//Byte6-7	排气温度(0.03125℃/bit,Offset:-273℃)
//					if(!Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+5, 2)).equals("FFFF")){
//						double exhaustTem = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+5, 2)), 2);//Byte6-7
//						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
//						statusData.setStatusValue((long) ((exhaustTem*0.03125-273)*100));
//						statusData.setTypes(LCStatusType.StatusType.exhaustTem);
//						vsaddBuilder.addStatus(statusData.build());
//						print(uniqueMark,"0X00FEF616","exhaustTem  Byte6-7	排气温度(0.03125℃/bit,Offset:-273℃):" ,(long) ((exhaustTem*0.03125-273)*100),statusAddition, statusAdditionIndex );
//						
//					}
//					statusAdditionIndex +=12;
//					break;
					
				case 0xFFFFFF01:
					//Byte1-8	Byte1-4	每天燃油消耗（ml/bit）
					if(!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 4)))){
						long dayFuelConsumption = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+0, 4));//Byte1-4	
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
//						statusData.setStatusValue((dayFuelConsumption*100));
						statusData.setStatusValue((dayFuelConsumption*100/1000));
						statusData.setTypes(LCStatusType.StatusType.dayFuelConsumption);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0xFFFFFF01","dayFuelConsumption  Byte1-8	每天燃油消耗（ml/bit）:" ,(long) (dayFuelConsumption),statusAddition, statusAdditionIndex );
						
					}
					statusAdditionIndex +=12;
					break;
					
				case 0x18FEFC17:
					//Byte1 	Byte2	剩余油量（ml/bit）
					if(!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 1)))){
						long oilValue = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+1, 1));//Byte1-4	
						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
						statusData.setStatusValue((oilValue*40));
						statusData.setTypes(LCStatusType.StatusType.oilValue);
						vsaddBuilder.addStatus(statusData.build());
						print(uniqueMark,"0x18FEFC17","oilValue  Byte2	剩余油量:" ,(double) (oilValue),statusAddition, statusAdditionIndex );
						
					}
					statusAdditionIndex += 12;
					break;

				default:
					//测试用
//					double TestoilValue = 88.88;
//					statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
//					statusData.setStatusValue((long)TestoilValue*100);
//					statusData.setTypes(LCStatusType.StatusType.oilValue);
//					vsaddBuilder.addStatus(statusData.build());
//					print(uniqueMark,"0x18FEFC17","oilValue  Byte2	剩余油量:" ,TestoilValue,statusAddition, statusAdditionIndex );
//					
					statusAdditionIndex +=12;
				}
			}
		}
		
//		logger.info("结束打印终端【"+uniqueMark+"】附加信息----------------------------------------------------------------");
		return vsaddBuilder;
	}
	
	
	/**
	 * 故障数据
	 * @param
	 * @return
	 */
	
	public static LCLocationData.VehicleBreakdownAddition.Builder getBreakdownAddition(byte[] breakdownAddition,String uniqueMark){
		/**
		 * 故障信息的格式
		 * 
		 * 故障总数              SPN 第一字节,  SPN 第二字节  ,  SPN MSB ,FMI 码              
		 *             Byte1       Byte2                 Byte3 
		 *                                            Bit8-6,Bit 5-1
		 */
		LCLocationData.VehicleBreakdownAddition.Builder breakdownBuilder = LCLocationData.VehicleBreakdownAddition.newBuilder();
		int breakdownAdditionIndex = 0;//开始下表
		if(breakdownAddition.length >= 1){
			int breakdownTotal = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex, 1));
			breakdownAdditionIndex +=1;//下标从故障项列表ID开始 
			LCVehicleBreakdown.VehicleBreakdown.Builder vehicleBreakdown = null;
			for(int i=0;i<breakdownTotal;i++){
				vehicleBreakdown = LCVehicleBreakdown.VehicleBreakdown.newBuilder();
				int SPN_1=Convert.byte2IntLittleEndian(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex, 1));
				int SPN_2=Convert.byte2IntLittleEndian(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex+1, 1));
				int byte_3 = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex+2, 1));
				int SPN_MSB=byte_3>>5;
				int SPN = SPN_1+(SPN_2<<8)+(SPN_MSB<<16);
				int FMI=byte_3&31;
				vehicleBreakdown.setBreakdownSPNValue(SPN);
				vehicleBreakdown.setBreakdownFMIValue(FMI);
				breakdownBuilder.addBreakdown(vehicleBreakdown);
				logger.info("终端故障码【"+uniqueMark+"】  第【"+(i+1)+"】个故障码:   原始包: "+Convert.bytesToHexString(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex, 3))+" ,解析值: SPN="+SPN+",FMI="+FMI);
				breakdownAdditionIndex +=3;
			}
		}
		return breakdownBuilder;
	}
	
	
	
	public static void print(String uniqueMark ,String id,String name ,double value ,byte[] statusAddition, int statusAdditionIndex ){
//		logger.info(uniqueMark+" :  "+id+": "+Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4, 8))+" :  " +value+ ":          "+name);
	}
	
	
	public static void main(String[] args) {
		
//		Convert.hexStringToBytes("802C");
//		Convert.byte2Long(Convert.hexStringToBytes("802C"), 2);
		System.out.println(Convert.byte2LongLittleEndian(Convert.hexStringToBytes("2C80")));
		System.out.println(Convert.byte2LongLittleEndian(Convert.hexStringToBytes("2C80"))*0.03125-273);
		
//		
//		byte[] breakdownAddition={};
//		
//		int SPN =520204;
//		int FIM = 11;
//		System.err.println("输入的故障码 SPN： "+SPN+"      FIM："+FIM);
//		int byte1;
//		int byte2;
//		int byte3;
//		
//		int spn1;
//		int spn2;
//		int spn_msb;
//		
//		spn1 = SPN&255;
//		spn2 = (SPN>>8)& 255;
//		spn_msb = (SPN>>16)& 255; 
//		
//		System.err.println("故障码 SPN： "+(spn1+(spn2<<8)+(spn_msb<<16)));
		
//		byte1 = spn1;
//		byte2 = spn2;
//		byte3 = (spn_msb<<5) +FIM;
//		
//		
//		breakdownAddition = ArraysUtils.arraycopy(breakdownAddition, Convert.hexStringToBytes(Integer.toBinaryString(1)));
//		breakdownAddition = ArraysUtils.arraycopy(breakdownAddition, Convert.hexStringToBytes(Integer.toBinaryString(byte1)));
//		breakdownAddition = ArraysUtils.arraycopy(breakdownAddition, Convert.hexStringToBytes(Integer.toBinaryString(byte2)));
//		breakdownAddition = ArraysUtils.arraycopy(breakdownAddition, Convert.hexStringToBytes(Integer.toBinaryString(byte3)));
		
//		breakdownAddition = ArraysUtils.arraycopy(breakdownAddition, Convert.intTobytes(1, 1));
//		breakdownAddition = ArraysUtils.arraycopy(breakdownAddition, Convert.intTobytes(byte1, 1));
//		breakdownAddition = ArraysUtils.arraycopy(breakdownAddition, Convert.intTobytes(byte2, 1));
//		breakdownAddition = ArraysUtils.arraycopy(breakdownAddition, Convert.intTobytes(byte3, 1));
//		
//		getBreakdownAddition(breakdownAddition,"aaaa");
//		
		
//		int SPN_1=spn1;
//		int SPN_2=spn2;
//		int byte_3 = byte3;
//		int SPN_MSB=byte_3>>5;
//		int SPN1 = SPN_1+(SPN_2<<8)+(SPN_MSB<<16);
//		int FIM1=byte_3&31;
//		System.err.println("解析后故障码 SPN： "+SPN1+"      FIM："+FIM1);
	}
	
}

package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;

import com.google.protobuf.ByteString;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.*;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
//import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.lc.LocationAdditionProcess_BigEndian;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.lc.LocationAdditionProcess_LittleEndian;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.*;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCCallNameRes;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@LocationCommand(id = "0201")
public class JT_0201_CallNameRes extends TerminalCommand {
    @Value("${opentsp.location.platform:1}")
    private int endianType;

    @Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//AnswerCommandCache.getInstance().getTimeoutAnswerEntry();
		byte[] content = packet.getContent();
		int serialNumber = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 2), 2);
		byte[] bodys = ArraysUtils.subarrays(content, 2);
		long alarm = Convert.byte2Long(ArraysUtils.subarrays(bodys, 0, 4), 4);
		long status = Convert.byte2Long(ArraysUtils.subarrays(bodys, 4, 4), 4);
		int latitude = Convert.byte2Int(ArraysUtils.subarrays(bodys, 8, 4), 4);
		int longitude = Convert.byte2Int(ArraysUtils.subarrays(bodys, 12, 4), 4);
		int height = Convert.byte2Int(ArraysUtils.subarrays(bodys, 16, 2), 2);
		int speed = Convert.byte2Int(ArraysUtils.subarrays(bodys, 18, 2), 2)/10;
		int direction = Convert.byte2Int(ArraysUtils.subarrays(bodys, 20, 2), 2);
		long currentTime = System.currentTimeMillis()/1000;
		long gpsTime = currentTime;
		String timestr = "20"+Convert.bytesToHexString(ArraysUtils.subarrays(bodys, 22, 6));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		try {
			gpsTime = sdf.parse(timestr).getTime()/1000;
//			if(! (gpsTime > Constant._min_gps_date && gpsTime < Constant._max_gps_date)){
//				this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(), LCResultCode.JTTerminal.MSG_ERROR);
//				return -1;
//			}
		} catch (ParseException e) {
//			this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(), LCResultCode.JTTerminal.MSG_ERROR);
//			return -1;
			e.printStackTrace();
		}
		
		LCLocationData.LocationData.Builder builder = LCLocationData.LocationData.newBuilder();
		builder.setAlarm(alarm);
		builder.setStatus(status);
		builder.setOriginalLat(latitude);
		builder.setOriginalLng(longitude);
		builder.setGpsDate(gpsTime);
		builder.setHeight(height);
		builder.setSpeed(speed);
		builder.setDirection(direction);
		builder.setReceiveDate(currentTime);
		builder.setIsPatch(false);
		
		byte[] addition = ArraysUtils.subarrays(content, 30, content.length-30);
		int additionIndex = 0;
		if(addition.length >= 2){
			while(addition.length > additionIndex){
				int additionId = Convert.byte2Int(ArraysUtils.subarrays(addition, additionIndex, 1), 1);
//				String hex = Convert.bytesToHexString(ArraysUtils.subarrays(addition, additionIndex, 1));
				int additionLength = Convert.byte2Int(ArraysUtils.subarrays(addition, additionIndex+1, 1), 1);
				switch (additionId) {
				case 1:
					long mileage = Convert.byte2Long(ArraysUtils.subarrays(addition, additionIndex+2, additionLength), additionLength) * 100;
					additionIndex = additionIndex+2+additionLength;
					builder.setMileage(mileage);
					break;
				case 2:
					int oil = Convert.byte2Int(ArraysUtils.subarrays(addition, additionIndex+2, additionLength), additionLength);
					additionIndex = additionIndex+2+additionLength;
					builder.setOil(oil);
					break;
				case 3:
					int recorderSpeed = Convert.byte2Int(ArraysUtils.subarrays(addition, additionIndex+2, additionLength), additionLength);
					additionIndex = additionIndex+2+additionLength;
					builder.setRecorderSpeed(recorderSpeed);
					break;
				case 0x31://星数（默认为1）
					int starNumber = Convert.byte2Int(ArraysUtils.subarrays(addition, additionIndex+2, additionLength), additionLength);
					additionIndex = additionIndex+2+additionLength;
					builder.setStarNumber(starNumber);
					break;
				case 0xEF://EF  附加信息ID = 0x EF， 附加信息长度=8   扩展状态第二个字节   16进制：EF 08 00 40 00 00 00 00 00 00
					//取出附加信息
					byte[] alermstatusAddition = ArraysUtils.subarrays(addition, additionIndex+2, additionLength);//00 40 00 00 00 00 00 00
					additionIndex = additionIndex+2+additionLength;
					builder.setAdditionAlarm(ByteString.copyFrom(alermstatusAddition));//设置 附加报警位 到LocationData的additonAlarm字段上
					break;
				case 0x74:// 车辆状态附加信息: 
			    	//取出附件信息
			    	byte[] statusAddition = ArraysUtils.subarrays(addition, additionIndex+2, additionLength);
			    	additionIndex = additionIndex+2+additionLength;
			    	if(endianType == 1){
			    		builder.setStatusAddition(LocationAdditionProcess_BigEndian.getStatusAddition(statusAddition,packet.getUniqueMark()));
			    	}else{
			    		builder.setStatusAddition(LocationAdditionProcess_LittleEndian.getStatusAddition(statusAddition,packet.getUniqueMark()));
			    	}
			    	break;
			    case 0x83:// 车辆故障附加信息
			    	byte[] breakdownAddition = ArraysUtils.subarrays(addition, additionIndex+2, additionLength);
			    	additionIndex = additionIndex+2+additionLength;
			    	if(endianType == 1){
			    		builder.setBreakdownAddition(LocationAdditionProcess_BigEndian.getBreakdownAddition(breakdownAddition, packet.getUniqueMark()));
			    	}else{
			    		builder.setBreakdownAddition(LocationAdditionProcess_LittleEndian.getBreakdownAddition(breakdownAddition, packet.getUniqueMark()));
			    	}
			    	break;
				default :
			    	additionIndex = additionIndex + 2 + additionLength;
			    	break;
				}
			}
		}
		CANDataEntity cANDataEntity = TerminalLastestCANData.getLastCANData(packet.getUniqueMark());
		if(cANDataEntity != null){
			// 电量
			builder.setBatteryPower(cANDataEntity.getBatteryPower());
			// 里程
			if(cANDataEntity.getMileage() > 0){//位置信息里面没有车辆状态附加信息
				LCLocationData.VehicleStatusAddition.Builder vsaddBuilder = builder.getStatusAdditionBuilder();
				if(vsaddBuilder == null){
					vsaddBuilder = LCLocationData.VehicleStatusAddition.newBuilder();
					LCVehicleStatusData.VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
					statusData.setStatusValue(cANDataEntity.getMileage()/10);
					statusData.setTypes(LCStatusType.StatusType.mileage);
					vsaddBuilder.addStatus(statusData);
					builder.setStatusAddition(vsaddBuilder);
				}else{
					int index = -1;
					for(int i=0; i < vsaddBuilder.getStatusList().size(); i++){
						LCVehicleStatusData.VehicleStatusData statusData = vsaddBuilder.getStatusList().get(i);
						if(statusData.getTypes().getNumber() == LCStatusType.StatusType.mileage.getNumber()){
							index = i;
							break;
						}
					}
					if(index != -1){
						vsaddBuilder.removeStatus(index);
					}
					LCVehicleStatusData.VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
					statusData.setStatusValue(cANDataEntity.getMileage()/10);
					statusData.setTypes(LCStatusType.StatusType.mileage);
					vsaddBuilder.addStatus(statusData);
				}
			}
			// 电源箱号、电压
			for(ModuleVoltageEntity mv : cANDataEntity.getModuleVoltageList()){
				LCLocationData.ModuleVoltage.Builder mvBuilder = LCLocationData.ModuleVoltage.newBuilder();
				mvBuilder.setModuleNum(mv.getModuleNum());
				mvBuilder.setNumber(mv.getNumber());
				mvBuilder.setVoltage(mv.getVoltage());
				builder.addModuleVoltages(mvBuilder);
			}
			builder.setElectricVehicle(cANDataEntity.getElectricVehicle());
			builder.setBatteryInfo(cANDataEntity.getBatteryVehicleInfo());
		}
		LCCallNameRes.CallNameRes.Builder callNameBuilder = LCCallNameRes.CallNameRes.newBuilder();
		callNameBuilder.setSerialNumber(serialNumber);
		callNameBuilder.setResult(LCResponseResult.ResponseResult.success);
		callNameBuilder.setData(builder);
		
		AnswerEntry answerEntry = AnswerCommandCache.getInstance().getAnswerEntry(packet.getUniqueMark(), serialNumber, true);
		if(answerEntry != null){
			Packet pk = new Packet();
			pk.setSerialNumber(packet.getSerialNumber());
			pk.setProtocol(LCConstant.LCMessageType.TERMINAL);
			pk.setUniqueMark(packet.getUniqueMark());
			pk.setCommand(LCAllCommands.AllCommands.Terminal.CallNameRes_VALUE);
			pk.setContent(callNameBuilder.build().toByteArray());
		//	super.writeToDataProcessing(pk);

            super.writeKafKaToDP(pk, TopicConstants.POSRAW);
		}
		return null;
	}

}

package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.terminalsetting;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaProperty;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaProperty.AreaProperty;
import com.navinfo.opentsp.platform.location.protocol.common.LCLineProperty;
import com.navinfo.opentsp.platform.location.protocol.common.LCLineProperty.LineProperty;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCSetRouteAlarm;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCSetRouteAlarm.SetRouteAlarm;


import java.util.List;

/**
 *  设置路线 
 *  --设置路线部标只支持追加功能。
 * @author jin_s
 *
 */
@LocationCommand(id = "2311")
public class DP_2311_SetRouteAlarm extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.SetRouteAlarm_VALUE);// ************
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.SetTerminalRoute);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			SetRouteAlarm setRouteAlarm = SetRouteAlarm.parseFrom(packet.getContent());
			List<LCSetRouteAlarm.Route> routesList = setRouteAlarm.getRoutesList();
			//设置路线部标只支持追加功能
			int operationsValue = setRouteAlarm.getOperations().getNumber();
			for(LCSetRouteAlarm.Route route:routesList){
				BytesArray bytesArray = new BytesArray();
				bytesArray.append(Convert.longTobytes(route.getRouteIdentify(), 4));
				int routeProperty = route.getRouteProperty();
				bytesArray.append(Convert.intTobytes(routeProperty, 2));
				//拐点
				List<LCSetRouteAlarm.TurningPoint> turningPointsList = route.getTurningPointsList();
			
				//根据时间
				if((routeProperty& AreaProperty.accordingTime_VALUE)== AreaProperty.accordingTime_VALUE){
					//bytesArray.append(DateUtils.format(route.getBeginDate(),DateFormat.YYMMDDHHMMSS).getBytes("gbk"));
					//bytesArray.append(DateUtils.format(route.getEndDate(),DateFormat.YYMMDDHHMMSS).getBytes("gbk"));
					bytesArray.append(Convert.hexStringToBytes(DateUtils.format(route.getBeginDate(), DateUtils.DateFormat.YYMMDDHHMMSS)));
					bytesArray.append(Convert.hexStringToBytes(DateUtils.format(route.getEndDate(), DateUtils.DateFormat.YYMMDDHHMMSS)));
				}
				//路线总拐点数
				bytesArray.append(Convert.intTobytes(turningPointsList.size(), 2));
				for (LCSetRouteAlarm.TurningPoint turningPoint : turningPointsList) {
					BytesArray areaArray = new BytesArray();
					areaArray.append(Convert.longTobytes(turningPoint.getTurningIdentify(), 4));
					areaArray.append(Convert.longTobytes(turningPoint.getLineIdentify(), 4));
					//拐点纬度
					areaArray.append(Convert.longTobytes(turningPoint.getTurningLatitude(), 4));
					//拐点经度
					areaArray.append(Convert.longTobytes(turningPoint.getTurningLongitude(), 4));
					//路段宽度BYTE
					areaArray.append(Convert.longTobytes(turningPoint.getTurningLongitude(), 1));
					//路段属性BYTE
					 int lineProperty = turningPoint.getLineProperty();
					 areaArray.append(Convert.longTobytes(lineProperty, 1));
					//根据时间
				    if((lineProperty& LineProperty.drivingTime_VALUE)== LineProperty.drivingTime_VALUE){
				    	//路段行驶过长阈值WORD
						areaArray.append(Convert.longTobytes(turningPoint.getDrivingOutTime(), 2));
						//路段行驶不足阈值WORD
						areaArray.append(Convert.longTobytes(turningPoint.getDrivingLackOfTime(), 2));
				    }
				    //限速
					if((lineProperty& LineProperty.speedLimit_VALUE)== LineProperty.speedLimit_VALUE){
						//路段最高速度WORD
						areaArray.append(Convert.intTobytes(turningPoint.getMaxSpeed(), 2));
						//路段超速持续时间 BYTE
						areaArray.append(Convert.intTobytes(turningPoint.getSpeedingContinuousTime(), 1));
						
					}
				
					bytesArray.append(areaArray.get());
				}
			
		
			
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.SetTerminalRoute);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
			//return super.writeToTerminal(outpacket);
				PacketResult packetResult=new PacketResult();
				packetResult.setTerminalPacket(outpacket);
				return packetResult;
		}
			
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}

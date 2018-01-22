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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCSetPolygonAreaAlarm;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCSetPolygonAreaAlarm.SetPolygonAreaAlarm;


import java.util.List;

/**
 * 	设置多边形区域
 * @author jin_s
 *
 */
@LocationCommand(id = "2309")
public class DP_2309_SetPolygonAreaAlarm extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.SetPolygonAreaAlarm_VALUE);// ************
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.SetTerminalPolygonArea);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			SetPolygonAreaAlarm setPolygonAreaAlarm = SetPolygonAreaAlarm.parseFrom(packet.getContent());
			List<LCSetPolygonAreaAlarm.PolygonArea> areasList = setPolygonAreaAlarm.getAreasList();
			//int operationsValue = setPolygonAreaAlarm.getOperations().getNumber();
//			BytesArray bytesArray = new BytesArray();
//			bytesArray.append(Convert.intTobytes(operationsValue, 1));
//			bytesArray.append(Convert.intTobytes(areasList.size(), 1));
			for (LCSetPolygonAreaAlarm.PolygonArea polygonArea : areasList) {
				BytesArray areaArray = new BytesArray();
				areaArray.append(Convert.longTobytes(polygonArea.getAreaId(), 4));
				int areaProperty = polygonArea.getAreaProperty();
				areaArray.append(Convert.intTobytes(areaProperty, 2));
			
				//根据时间
				if((areaProperty& LCAreaProperty.AreaProperty.accordingTime_VALUE)== LCAreaProperty.AreaProperty.accordingTime_VALUE){
				//	areaArray.append(DateUtils.format(polygonArea.getBeginDate(),DateFormat.YYMMDDHHMMSS).getBytes("gbk"));
					//areaArray.append(DateUtils.format(polygonArea.getEndDate(),DateFormat.YYMMDDHHMMSS).getBytes("gbk"));
					areaArray.append(Convert.hexStringToBytes(DateUtils.format(polygonArea.getBeginDate(), DateUtils.DateFormat.YYMMDDHHMMSS)));
					areaArray.append(Convert.hexStringToBytes(DateUtils.format(polygonArea.getEndDate(), DateUtils.DateFormat.YYMMDDHHMMSS)));
				}
				//限速
				if((areaProperty& LCAreaProperty.AreaProperty.speedLimit_VALUE)== LCAreaProperty.AreaProperty.speedLimit_VALUE){
					areaArray.append(Convert.intTobytes(polygonArea.getMaxSpeed(), 2));
					areaArray.append(Convert.intTobytes(polygonArea.getSpeedingContinuousTime(), 1));
					
				}
				List<LCSetPolygonAreaAlarm.PolygonPoint> polygonPointsList = polygonArea.getPolygonPointsList();
				areaArray.append(Convert.intTobytes(polygonPointsList.size(), 2));
				for(LCSetPolygonAreaAlarm.PolygonPoint polygonPoint:polygonPointsList){
					areaArray.append(Convert.intTobytes(polygonPoint.getLatitude(), 4));
					areaArray.append(Convert.intTobytes(polygonPoint.getLongitude(), 4));
				}
				//areaArray.append(areaArray.get());
				Packet outpacket = new Packet();
				outpacket.setSerialNumber(packet.getSerialNumber());
				outpacket.setCommand(Constant.JTProtocol.SetTerminalPolygonArea);
				outpacket.setUniqueMark(packet.getUniqueMark());
				outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
				outpacket.setContent(areaArray.get());
				//super.writeToTerminal(outpacket);
				PacketResult packetResult=new PacketResult();
				packetResult.setTerminalPacket(outpacket);
				return packetResult;
			}
		
			return null;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}

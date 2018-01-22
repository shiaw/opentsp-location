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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCSetCircleAreaAlarm;


import java.util.List;

/**
 * 设置圆形区域报警
 * @author jin_s
 *
 */
@LocationCommand(id = "2305")
public class DP_2305_SetCircleAreaAlarm extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.SetCircleAreaAlarm_VALUE);// ************
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.SetTerminalCircleArea);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			LCSetCircleAreaAlarm.SetCircleAreaAlarm setCircleAreaAlarm = LCSetCircleAreaAlarm.SetCircleAreaAlarm.parseFrom(packet.getContent());
			List<LCSetCircleAreaAlarm.CircleArea> areasList = setCircleAreaAlarm.getAreasList();
			int operationsValue = setCircleAreaAlarm.getOperations().getNumber();
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.intTobytes(operationsValue, 1));
			bytesArray.append(Convert.intTobytes(areasList.size(), 1));
			for (LCSetCircleAreaAlarm.CircleArea circleArea : areasList) {
				BytesArray circleAreaArray = new BytesArray();
				circleAreaArray.append(Convert.longTobytes(circleArea.getAreaIdentify(), 4));
				int areaProperty = circleArea.getAreaProperty();
				circleAreaArray.append(Convert.longTobytes(areaProperty, 2));
				circleAreaArray.append(Convert.longTobytes(circleArea.getCenterLatitude(), 4));
				circleAreaArray.append(Convert.longTobytes(circleArea.getCenterLongitude(), 4));
				circleAreaArray.append(Convert.longTobytes(circleArea.getRadius(), 4));
				//根据时间
				if((areaProperty& LCAreaProperty.AreaProperty.accordingTime_VALUE)== LCAreaProperty.AreaProperty.accordingTime_VALUE){
					circleAreaArray.append(Convert.hexStringToBytes(DateUtils.format(circleArea.getBeginDate(), DateUtils.DateFormat.YYMMDDHHMMSS)));
					circleAreaArray.append(Convert.hexStringToBytes(DateUtils.format(circleArea.getEndDate(), DateUtils.DateFormat.YYMMDDHHMMSS)));
				}
				//限速
				if((areaProperty& LCAreaProperty.AreaProperty.speedLimit_VALUE)== LCAreaProperty.AreaProperty.speedLimit_VALUE){
					circleAreaArray.append(Convert.intTobytes(circleArea.getMaxSpeed(), 2));
					circleAreaArray.append(Convert.intTobytes(circleArea.getSpeedingContinuousTime(), 1));
					
				}
			
				bytesArray.append(circleAreaArray.get());
			}
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.SetTerminalCircleArea);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}

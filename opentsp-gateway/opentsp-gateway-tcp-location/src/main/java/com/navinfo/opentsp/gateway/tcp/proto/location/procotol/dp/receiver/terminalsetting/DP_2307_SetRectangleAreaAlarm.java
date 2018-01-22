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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCSetRectangleAreaAlarm;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCSetRectangleAreaAlarm.SetRectangleAreaAlarm;


import java.util.List;

/**
 * 设置矩形区域
 * @author jin_s
 *
 */
@LocationCommand(id = "2307")
public class DP_2307_SetRectangleAreaAlarm extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.SetRectangleAreaAlarm_VALUE);// ************
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.SetTerminalRectangleArea);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			SetRectangleAreaAlarm setRectangleAreaAlarm = SetRectangleAreaAlarm.parseFrom(packet.getContent());
			List<LCSetRectangleAreaAlarm.RectangleArea> areasList = setRectangleAreaAlarm.getAreasList();
			int operationsValue = setRectangleAreaAlarm.getOperations().getNumber();
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.intTobytes(operationsValue, 1));
			bytesArray.append(Convert.intTobytes(areasList.size(), 1));
			for (LCSetRectangleAreaAlarm.RectangleArea rectangleArea : areasList) {
				BytesArray circleAreaArray = new BytesArray();
				circleAreaArray.append(Convert.longTobytes(rectangleArea.getAreaId(), 4));
				int areaProperty = rectangleArea.getAreaProperty();
				circleAreaArray.append(Convert.longTobytes(areaProperty, 2));
				circleAreaArray.append(Convert.longTobytes(rectangleArea.getLeftTopLatitude(), 4));
				circleAreaArray.append(Convert.longTobytes(rectangleArea.getLeftToplongitude(), 4));
				circleAreaArray.append(Convert.longTobytes(rectangleArea.getRightBottomLatitude(), 4));
				circleAreaArray.append(Convert.longTobytes(rectangleArea.getRightBottomLongitude(), 4));
				//根据时间
				if((areaProperty& AreaProperty.accordingTime_VALUE)== AreaProperty.accordingTime_VALUE){
					//circleAreaArray.append(DateUtils.format(rectangleArea.getBeginDate(),DateFormat.YYMMDDHHMMSS).getBytes("gbk"));
					//circleAreaArray.append(DateUtils.format(rectangleArea.getEndDate(),DateFormat.YYMMDDHHMMSS).getBytes("gbk"));
					circleAreaArray.append(Convert.hexStringToBytes(DateUtils.format(rectangleArea.getBeginDate(), DateUtils.DateFormat.YYMMDDHHMMSS)));
					circleAreaArray.append(Convert.hexStringToBytes(DateUtils.format(rectangleArea.getEndDate(), DateUtils.DateFormat.YYMMDDHHMMSS)));
				}
				//限速
				if((areaProperty& AreaProperty.speedLimit_VALUE)== AreaProperty.speedLimit_VALUE){
					circleAreaArray.append(Convert.intTobytes(rectangleArea.getMaxSpeed(), 2));
					circleAreaArray.append(Convert.intTobytes(rectangleArea.getSpeedingContinuousTime(), 1));
					
				}
			
				bytesArray.append(circleAreaArray.get());
			}
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.SetTerminalRectangleArea);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
		//	return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}

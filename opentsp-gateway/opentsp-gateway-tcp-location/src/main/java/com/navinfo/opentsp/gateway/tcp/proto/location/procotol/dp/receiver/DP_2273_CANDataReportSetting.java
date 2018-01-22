package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCCANDataReportSetting;
@LocationCommand(id = "2273")
public class DP_2273_CANDataReportSetting extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(AllCommands.Terminal.CANDataReportSetting_VALUE);//2273
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);//8103
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			LCCANDataReportSetting.CANDataReportSetting cANDataReportSetting = LCCANDataReportSetting.CANDataReportSetting.parseFrom(packet.getContent());

			int count = 0;//统计本次设置多少项内容
			BytesArray bytesArray = new BytesArray();
			BytesArray bytesArrayFinal = new BytesArray();
//			bytesArray.append(Convert.intTobytes(4, 1));//设置4个参数
			/*if(cANDataReportSetting.hasReportStrategy()){
				bytesArray.append(Convert.intTobytes(0XF00F, 4));//CAN数据汇报策略，0：定时汇报；1：定距汇报；2：定时和定距汇报
				bytesArray.append(Convert.intTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(cANDataReportSetting.getReportStrategy(),4));
				count++;
			}

			if(cANDataReportSetting.hasReportPlan()){
				bytesArray.append(Convert.intTobytes(0XF010, 4));//CAN数据汇报方案，0：根据ACC 状态； 1：根据登录状态和ACC 状态，先判断登录状态，若登录再根据ACC 状态
				bytesArray.append(Convert.intTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(cANDataReportSetting.getReportPlan(),4));
				count++;}


			if(cANDataReportSetting.hasReportInterval()){
				bytesArray.append(Convert.intTobytes(0XF011,4)); // 驾驶行为异常（报警）时汇报时间间隔，单位为秒（s），>0
				bytesArray.append(Convert.intTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(cANDataReportSetting.getReportInterval(),4));
				count++;
			}*/

			if(cANDataReportSetting.hasEngineType()&&cANDataReportSetting.hasVehType()){
				bytesArray.append(Convert.intTobytes(0XF012,4)); // 发动机型号：0:潍柴；1:玉柴；2:锡柴 3；杭发 ；4沃能；5:汽研；
				bytesArray.append(Convert.intTobytes(2, 1));
				bytesArray.append(Convert.longTobytes(cANDataReportSetting.getVehType(), 1));
				bytesArray.append(Convert.longTobytes(cANDataReportSetting.getEngineType(),1));
				count++;
			}

			bytesArrayFinal.append(Convert.intTobytes(count, 1));//计算总共有多少个
			bytesArrayFinal.append(bytesArray.get());
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.TerminalSetting);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArrayFinal.get());

			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

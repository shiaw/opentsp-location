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
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCReportTacticsAndInterval;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCReportTacticsAndInterval.ReportTacticsAndInterval;

@LocationCommand(id = "2253")
public class DP_2253_ReportTacticsAndInterval extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		try {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(AllCommands.Terminal.ReportTacticsAndInterval_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

			ReportTacticsAndInterval reportTacticsAndInterval = ReportTacticsAndInterval.parseFrom(packet.getContent());
			int pnumber = 0;
			BytesArray bytesArray = new BytesArray();
			//ReportTactics	tactics	汇报策略枚举
			bytesArray.append(Convert.longTobytes(0x20, 4));
			bytesArray.append(Convert.longTobytes(4, 1));
			bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getTactics().getNumber(), 4));
			pnumber++;
			//ReportProgram	program	汇报方案枚举
			bytesArray.append(Convert.longTobytes(0x21, 4));
			bytesArray.append(Convert.longTobytes(4, 1));
			bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getProgram().getNumber(), 4));
			pnumber++;
			//notLoginTime	驾驶员未登录汇报间隔，单位秒
			if(reportTacticsAndInterval.hasNotLoginTime()){
				bytesArray.append(Convert.longTobytes(0x22, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getNotLoginTime(), 4));
				pnumber++;
			}
			//sleepingTime	休眠时汇报时间间隔，单位秒
			if(reportTacticsAndInterval.hasSleepingTime()){
				bytesArray.append(Convert.longTobytes(0x27, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getSleepingTime(), 4));
				pnumber++;
			}
			//urgentTime	紧急报警汇报间隔，单位秒
			if(reportTacticsAndInterval.hasUrgentTime()){
				bytesArray.append(Convert.longTobytes(0x28, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getUrgentTime(), 4));
				pnumber++;
			}
			//defaultTime	缺省时间汇报间隔，单位秒
			if(reportTacticsAndInterval.hasDefaultTime()){
				bytesArray.append(Convert.longTobytes(0x29, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getDefaultTime(), 4));
				pnumber++;
			}
			//defaultDistance	缺省距离汇报间隔，单位米
			if(reportTacticsAndInterval.hasDefaultDistance()){
				bytesArray.append(Convert.longTobytes(0x2C, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getDefaultDistance(), 4));
				pnumber++;
			}
			//notLoginDistance	驾驶员未登录汇报距离间隔，单位米
			if(reportTacticsAndInterval.hasNotLoginDistance()){
				bytesArray.append(Convert.longTobytes(0x2D, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getNotLoginDistance(), 4));
				pnumber++;
			}
			//sleepingDistance	休眠时汇报距离间隔，单位米
			if(reportTacticsAndInterval.hasSleepingDistance()){
				bytesArray.append(Convert.longTobytes(0x2E, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getSleepingDistance(), 4));
				pnumber++;
			}
			//urgentDistance	紧急报警时汇报距离间隔，单位米
			if(reportTacticsAndInterval.hasUrgentDistance()){
				bytesArray.append(Convert.longTobytes(0x2F, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getUrgentDistance(), 4));
				pnumber++;
			}
			//inflectionAngle	拐点补传角度(<180)
			if(reportTacticsAndInterval.hasInflectionAngle()){
				bytesArray.append(Convert.longTobytes(0x30, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getInflectionAngle(), 4));
				pnumber++;
			}
			//fenceRadius 电子围栏半径（非法位移阀值），单位米
			if(reportTacticsAndInterval.hasFenceRadius()){
				bytesArray.append(Convert.longTobytes(0x31,4));
				bytesArray.append(Convert.longTobytes(2,1));
				bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getFenceRadius(),2));
				pnumber++;
			}
			//驾驶行为异常（报警）时汇报时间间隔，单位为秒，>0
			if (reportTacticsAndInterval.hasAlarmInterval()){
				bytesArray.append(Convert.longTobytes(0xF011,4));
				bytesArray.append(Convert.longTobytes(4,1));
				bytesArray.append(Convert.longTobytes(reportTacticsAndInterval.getAlarmInterval(),4));
				pnumber++;
			}
			BytesArray bytesArrayFin = new BytesArray();
			bytesArrayFin.append(Convert.longTobytes(pnumber, 1));
			bytesArrayFin.append(bytesArray.get());
			Packet outpacket = new Packet();
			outpacket.setCommand(Constant.JTProtocol.TerminalSetting);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setContent(bytesArrayFin.get());
			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
			//byte[] bytes = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
			
//			packet.setCommand(Constant.JTProtocol.TerminalSetting);
//			packet.setContent(bytesArray.get());
//			return super.writeToTerminal(packet);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
}

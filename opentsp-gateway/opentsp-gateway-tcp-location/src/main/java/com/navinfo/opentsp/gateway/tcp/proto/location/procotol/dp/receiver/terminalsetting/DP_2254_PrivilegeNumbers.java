package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.terminalsetting;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCPrivilegeNumbers;

import java.io.UnsupportedEncodingException;
@LocationCommand(id = "2254")
public class DP_2254_PrivilegeNumbers extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		try {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.PrivilegeNumbers_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

			LCPrivilegeNumbers.PrivilegeNumbers privilegeNumbers = LCPrivilegeNumbers.PrivilegeNumbers.parseFrom(packet.getContent());
			int pnumber = 0;
			BytesArray bytesArray = new BytesArray();
			
			//platformPhone	监控平台电话号码
			if(privilegeNumbers.hasPlatformPhone()){
				bytesArray.append(Convert.longTobytes(0x40, 4));
				byte[] platformPhone = privilegeNumbers.getPlatformPhone().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(platformPhone.length, 1));
				bytesArray.append(platformPhone);
				pnumber++;
			}
			//resetPhone	复位电话号码，可采用此电话号码拨打终端电话让终端复位
			if(privilegeNumbers.hasResetPhone()){
				bytesArray.append(Convert.longTobytes(0x41, 4));
				byte[] resetPhone = privilegeNumbers.getResetPhone().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(resetPhone.length, 1));
				bytesArray.append(resetPhone);
				pnumber++;
			}
			//factoryResetPhone	恢复出厂设置电话号码，可采用此电话号码拨打终端电话让终端恢复出厂设置
			if(privilegeNumbers.hasFactoryResetPhone()){
				bytesArray.append(Convert.longTobytes(0x42, 4));
				byte[] factoryResetPhone = privilegeNumbers.getFactoryResetPhone().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(factoryResetPhone.length, 1));
				bytesArray.append(factoryResetPhone);
				pnumber++;
			}
			//PlatformsmsPhone	监控平台SMS电话号码
			if(privilegeNumbers.hasPlatformsmsPhone()){
				bytesArray.append(Convert.longTobytes(0x43, 4));
				byte[] platformsmsPhone = privilegeNumbers.getPlatformsmsPhone().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(platformsmsPhone.length, 1));
				bytesArray.append(platformsmsPhone);
				pnumber++;
			}
			//smsAlarmPhone	接收终端SMS文本报警号码
			if(privilegeNumbers.hasSmsAlarmPhone()){
				bytesArray.append(Convert.longTobytes(0x44, 4));
				byte[] smsAlarmPhone = privilegeNumbers.getSmsAlarmPhone().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(smsAlarmPhone.length, 1));
				bytesArray.append(smsAlarmPhone);
				pnumber++;
			}
			//tactics	终端电话接听策略
			if(privilegeNumbers.hasTactics()){
				bytesArray.append(Convert.longTobytes(0x45, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(privilegeNumbers.getTactics().getNumber(), 4));
				pnumber++;
			}
			//everyTalking	每次最长通话时间，单位秒
			if(privilegeNumbers.hasEveryTalking()){
				bytesArray.append(Convert.longTobytes(0x46, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(privilegeNumbers.getEveryTalking(), 4));
				pnumber++;
			}
			//monthTalking	每月最长通话时间，单位秒
			if(privilegeNumbers.hasMonthTalking()){
				bytesArray.append(Convert.longTobytes(0x47, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(privilegeNumbers.getMonthTalking(), 4));
				pnumber++;
			}
			//listenerPhone	监听电话号码
			if(privilegeNumbers.hasListenerPhone()){
				bytesArray.append(Convert.longTobytes(0x48, 4));
				byte[] listenerPhone = privilegeNumbers.getListenerPhone().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(listenerPhone.length, 1));
				bytesArray.append(listenerPhone);
				pnumber++;
			}
			//privilegeSmsPhone	监管平台特权短信号码
			if(privilegeNumbers.hasPrivilegeSmsPhone()){
				bytesArray.append(Convert.longTobytes(0x49, 4));
				byte[] privilegeSmsPhone = privilegeNumbers.getPrivilegeSmsPhone().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(privilegeSmsPhone.length, 1));
				bytesArray.append(privilegeSmsPhone);
				pnumber++;
			}
			byte[] bytes = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
			
			packet.setCommand(Constant.JTProtocol.TerminalSetting);
			packet.setContent(bytes);
			//return super.writeToTerminal(packet);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(packet);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}

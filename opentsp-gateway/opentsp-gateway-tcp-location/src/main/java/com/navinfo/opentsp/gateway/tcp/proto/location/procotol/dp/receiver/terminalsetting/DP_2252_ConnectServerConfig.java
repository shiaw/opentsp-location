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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCConnectServerConfig;


import java.io.UnsupportedEncodingException;
@LocationCommand(id = "2252")
public class DP_2252_ConnectServerConfig extends DPCommand {
	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		try {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.ConnectServerConfig_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

			LCConnectServerConfig.ConnectServerConfig connectServerConfig = LCConnectServerConfig.ConnectServerConfig.parseFrom(packet.getContent());
			int pnumber = 0;
			BytesArray bytesArray = new BytesArray();
			//masterApn	主服务器APN，无线通信拨号访问点。若网络制式为CDMA，则该处为PPP拨号号码
			if(connectServerConfig.hasMasterApn()){
				bytesArray.append(Convert.longTobytes(0x10, 4));
				byte[] masterApn = connectServerConfig.getMasterApn().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(masterApn.length, 1));
				bytesArray.append(masterApn);
				pnumber++;
			}
			//masterName	主服务器无线通信拨号用户名
			if(connectServerConfig.hasMasterName()){
				bytesArray.append(Convert.longTobytes(0x11, 4));
				byte[] masterName = connectServerConfig.getMasterName().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(masterName.length, 1));
				bytesArray.append(masterName);
				pnumber++;
			}
			//masterPassword	主服务器无线通信拨号密码
			if(connectServerConfig.hasMasterPassword()){
				bytesArray.append(Convert.longTobytes(0x12, 4));
				byte[] masterPassword = connectServerConfig.getMasterPassword().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(masterPassword.length, 1));
				bytesArray.append(masterPassword);
				pnumber++;
			}
			//masterIP	主服务器地址IP或域名
			if(connectServerConfig.hasMasterIP()){
				bytesArray.append(Convert.longTobytes(0x13, 4));
				byte[] masterIp = connectServerConfig.getMasterIP().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(masterIp.length, 1));
				bytesArray.append(masterIp);
				pnumber++;
			}
			//backupApn	备用服务器APN，无线通信拨号访问点
			if(connectServerConfig.hasBackupApn()){
				bytesArray.append(Convert.longTobytes(0x14, 4));
				byte[] backupApn = connectServerConfig.getBackupApn().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(backupApn.length, 1));
				bytesArray.append(backupApn);
				pnumber++;
			}
			//backupName	备用服务器无线通信拨号用户名
			if(connectServerConfig.hasBackupName()){
				bytesArray.append(Convert.longTobytes(0x15, 4));
				byte[] backupName = connectServerConfig.getBackupName().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(backupName.length, 1));
				bytesArray.append(backupName);
				pnumber++;
			}
			//backupPasswd	备用服务器无线通信拨号密码
			if(connectServerConfig.hasBackupPasswd()){
				bytesArray.append(Convert.longTobytes(0x16, 4));
				byte[] backupPasswd = connectServerConfig.getBackupPasswd().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(backupPasswd.length, 1));
				bytesArray.append(backupPasswd);
				pnumber++;
			}
			//backupIP	备用服务器地址IP或域名
			if(connectServerConfig.hasBackupIP()){
				bytesArray.append(Convert.longTobytes(0x17, 4));
				byte[] backupIP = connectServerConfig.getBackupIP().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(backupIP.length, 1));
				bytesArray.append(backupIP);
				pnumber++;
			}
			//tcpPort	服务器TCP端口
			if(connectServerConfig.hasTcpPort()){
				bytesArray.append(Convert.longTobytes(0x18, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(connectServerConfig.getTcpPort(), 4));
				pnumber++;
			}
			//udpPort	服务器UDP端口
			if(connectServerConfig.hasUdpPort()){
				bytesArray.append(Convert.longTobytes(0x19, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(connectServerConfig.getUdpPort(), 4));
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

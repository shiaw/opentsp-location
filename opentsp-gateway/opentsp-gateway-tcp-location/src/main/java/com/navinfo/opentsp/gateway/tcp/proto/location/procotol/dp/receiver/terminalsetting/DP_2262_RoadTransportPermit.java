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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCRoadTransportPermit;


import java.io.UnsupportedEncodingException;
@LocationCommand(id = "2262")
public class DP_2262_RoadTransportPermit  extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.RoadTransportPermit_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
			try {
				LCRoadTransportPermit.RoadTransportPermit roadTransportPermit= LCRoadTransportPermit.RoadTransportPermit.parseFrom(packet.getContent());
				int pnumber = 0;
				BytesArray bytesArray = new BytesArray();
				//IC 卡认证注主服务器IP 或域名
				if(roadTransportPermit.hasMainServerIp()){
					pnumber++;
					bytesArray.append(Convert.longTobytes(0x1A, 4));
					byte[] mainServerIp = roadTransportPermit.getMainServerIp().getBytes("GBK");
					bytesArray.append(Convert.longTobytes(mainServerIp.length, 1));
					bytesArray.append(mainServerIp);
				}
				//IC 卡认证注服务器TCP 端口
				if(roadTransportPermit.hasTcpPort()){
					pnumber++;
					bytesArray.append(Convert.longTobytes(0x1B, 4));
					bytesArray.append(Convert.longTobytes(4, 1));
					bytesArray.append(Convert.intTobytes(roadTransportPermit.getTcpPort(), 4));
				}
				//IC 卡认证注服务器UDP 端口
				if(roadTransportPermit.hasUdpPort()){
					pnumber++;
					bytesArray.append(Convert.longTobytes(0x1C, 4));
					bytesArray.append(Convert.longTobytes(4, 1));
					bytesArray.append(Convert.intTobytes(roadTransportPermit.getUdpPort(), 4));
				}
				//IC 卡认证注备份服务器IP 或域名
				if(roadTransportPermit.hasBackupServerIp()){
					pnumber++;
					bytesArray.append(Convert.longTobytes(0x1D, 4));
					byte[] backupServerIp = roadTransportPermit.getBackupServerIp().getBytes("GBK");
					bytesArray.append(Convert.longTobytes(backupServerIp.length, 1));
					bytesArray.append(backupServerIp);
				}
				
				byte[] content = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
				Packet outpacket = new Packet();
				outpacket.setCommand(Constant.JTProtocol.TerminalSetting);
				outpacket.setUniqueMark(packet.getUniqueMark());
				outpacket.setSerialNumber(packet.getSerialNumber());
				outpacket.setContent(content);
				//return super.writeToTerminal(outpacket);
				PacketResult packetResult=new PacketResult();
				packetResult.setTerminalPacket(outpacket);
				return packetResult;
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
	}

}

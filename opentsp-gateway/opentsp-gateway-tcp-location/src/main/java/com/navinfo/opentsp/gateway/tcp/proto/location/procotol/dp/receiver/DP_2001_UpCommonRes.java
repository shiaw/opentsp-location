package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCUpCommonRes;
@LocationCommand(id = "2001")
public class DP_2001_UpCommonRes extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		try {
			LCUpCommonRes.UpCommonRes commonRes = LCUpCommonRes.UpCommonRes.parseFrom(packet.getContent());
			//签权成功，1、更新终端签权标识；2、发送终端上下线通知；3、存储终端上下线状态
			/*if( commonRes.getResponseId() == 0x0102 ){
				TerminalInfo t = TerminalManage.getInstance().getTerminal(packet.getUniqueMark());
				if( t != null ){
					if( commonRes.getResult().getNumber() == ResponseResult.success_VALUE ){
						t.setAuth(true);
						t.setSendOnLineMark(true);
						t.setSendOffLineMark(false);						
						DPFacade.terminalOnlineSwitch(Long.parseLong(packet.getUniqueMark()), NodeHelper.getCurrentTime(), true);
						DAFacade.terminalOnlineSwitchInfoSave(Long.parseLong(packet.getUniqueMark()), NodeHelper.getCurrentTime(), true);
					}else{
						t.setAuth(false);
					}
					TerminalManage.getInstance().addTerminal(t);
				}
			}*/
			//--------------------------------------
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.intTobytes(commonRes.getSerialNumber(), 2));
			bytesArray.append(Convert.intTobytes(commonRes.getResponseId(), 2));
			bytesArray.append(Convert.intTobytes(commonRes.getResult().getNumber(), 1));
			
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.PlatformCommonResponse);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
            return  packetResult;
			//return super.writeToTerminal(outpacket);
		} catch (InvalidProtocolBufferException e) {
		}
		return null;
	}

}

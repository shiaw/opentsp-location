
package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCInfoDemandOrCancel;

@LocationCommand(id = "0303")
public class JT_0303_InfoDemandOrCancel extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		log.debug("收到终端[ "+packet.getUniqueMark()+" ]信息点播/取消.");
		byte[] bytes = packet.getContent();
		int infoType = Convert.byte2Int(ArraysUtils.subarrays(bytes, 0, 1), 1);
		int isDemand = Convert.byte2Int(ArraysUtils.subarrays(bytes, 1, 1), 1);
		LCInfoDemandOrCancel.InfoDemandOrCancel.Builder builder = LCInfoDemandOrCancel.InfoDemandOrCancel.newBuilder();
		//infoType	信息类型
		builder.setInfoType(infoType);
		//isDemand	true：点播；false：取消
         builder.setIsDemand(isDemand==1?true:false);
		
		Packet _out_packet = new Packet();
		_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.InfoDemandOrCancel_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());;
		//return super.writeToDataProcessing(_out_packet);
        super.writeKafKaToDP(_out_packet, TopicConstants.JT_0303_InfoDemandOrCancel);
        return null;
	}

}

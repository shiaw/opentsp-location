package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import  com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import  com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
@DaRmiNo(id = "1099")
public class Mutual_1099_Heartbeat extends Dacommand {

	@Override
	public Packet processor(Packet packet) {
//		logger.info("收到来自[" + packet.getFrom() + "]节点心跳数据.流水号是："+packet.getSerialNumber());
		return super.commonResponsesForPlatform(packet.getFrom(), packet.getSerialNumber(), AllCommands.Platform.Heartbeat_VALUE, PlatformResponseResult.success);
	}

}

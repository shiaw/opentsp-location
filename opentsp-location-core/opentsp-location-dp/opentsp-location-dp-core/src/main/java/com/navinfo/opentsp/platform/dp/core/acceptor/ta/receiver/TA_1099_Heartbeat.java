//package com.navinfo.opentsp.platform.dp.core.acceptor.receiver;
//
//import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
//import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
//import com.lc.dp.acceptor.tal.protocol.TACommand;
//import com.navinfo.opentsp.platform.location.kit.Packet;
//
//
///*********************************
// * TA连接服务节点收到心跳，统一回复通用应答
// *
// * @author claus
// *
// */
//public class TA_1099_Heartbeat extends TACommand {
//
//	@Override
//	public int processor(Packet packet) {
//		log.info("收到来自[" + packet.getFrom() + "]节点心跳数据.");
//		return super.commonResponsesForPlatform(packet.getFrom(), packet.getSerialNumber(), AllCommands.Platform.Heartbeat_VALUE, PlatformResponseResult.success);
//	}
//
//}

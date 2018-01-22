package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;//package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;
//
//import com.google.protobuf.InvalidProtocolBufferException;
//import com.navinfo.opentsp.platform.location.kit.Packet;
//import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
//import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
//import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
//import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
//import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes;
//
//public class DP_1100_ServerCommonRes extends DPCommand {
//
//	@Override
//	public PacketResult processor(NettyClientConnection connection,Packet packet) {
//		try {
//			LCServerCommonRes.ServerCommonRes commonRes = LCServerCommonRes.ServerCommonRes.parseFrom(packet
//                    .getContent());
//			logger.info("收到DP节点通用应答:[应答指令号:"
//					+ Integer.toHexString(commonRes.getResponseId())
//					+ ",应答流水号:"
//					+ Integer.toHexString(commonRes.getSerialNumber())
//					+ ",应答结果:" + commonRes.getResults().name() + "]");
//			// 如果收到应答ID为节点汇报,向MM发送链路状态通知
//			if (commonRes.getResponseId() == LCAllCommands.AllCommands.NodeCluster.ReportServerIdentify_VALUE
//					&& commonRes.getResults().getNumber() == LCPlatformResponseResult.PlatformResponseResult.success_VALUE) {
//				GlobalProtocol.sendLinkStatusSwitchNotice(packet.getFrom(),
//						true);
//			}
//		} catch (InvalidProtocolBufferException e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}
//
//}

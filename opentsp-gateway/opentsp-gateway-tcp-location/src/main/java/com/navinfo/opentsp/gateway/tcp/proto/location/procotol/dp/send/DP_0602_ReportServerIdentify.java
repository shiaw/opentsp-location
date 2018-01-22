package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.send;//package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.send;
//
//
//import com.navinfo.opentsp.platform.location.kit.Packet;
//import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
//import com.navinfo.opentsp.platform.location.kit.LCConstant;
//import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
//import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
//import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
//import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeType.NodeType;
//import com.navinfo.opentsp.platform.location.protocol.services.common.LCNodeType;
//import com.navinfo.opentsp.platform.location.protocol.services.common.LCReportServerIdentify;
//
//public class DP_0602_ReportServerIdentify extends DPCommand {
//    private final static NodeType NODE_TYPE = NodeType.ta;
//	@Override
//	public PacketResult processor(NettyClientConnection connection,Packet packet) {
//		LCReportServerIdentify.ReportServerIdentify.Builder builder = LCReportServerIdentify.ReportServerIdentify.newBuilder();
//		builder.setType(NODE_TYPE);
//		builder.setNodeCode(NodeHelper.getNodeCode());
//
//		packet.setCommand(AllCommands.NodeCluster.ReportServerIdentify_VALUE);
//		packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
//		packet.setContent(builder.build().toByteArray());
//		packet.setFrom(NodeHelper.getNodeCode());
//		packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
//		return this.write(packet);
//	}
//
//}

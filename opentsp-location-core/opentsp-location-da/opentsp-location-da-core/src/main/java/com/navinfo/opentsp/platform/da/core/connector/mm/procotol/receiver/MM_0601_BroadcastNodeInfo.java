package com.navinfo.opentsp.platform.da.core.connector.mm.procotol.receiver;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import  com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import  com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import  com.navinfo.opentsp.platform.location.protocol.nodecluster.LCBroadcastNodeInfo.BroadcastNodeInfo;
import  com.navinfo.opentsp.platform.location.protocol.nodecluster.LCNodeStatusReport.NodeStatusReport;
import  com.navinfo.opentsp.platform.location.protocol.nodecluster.LCNodeStatusReport.NodeStatusReport.NodeStatus;
import  com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeInfo.NodeInfo;
import  com.navinfo.opentsp.platform.da.core.cache.NodeList;
import  com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import  com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMCommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import com.navinfo.opentsp.platform.location.kit.Packet;

public class MM_0601_BroadcastNodeInfo extends MMCommand {
	/////
	@Override
	public int processor(Packet packet) {
		try {
			BroadcastNodeInfo nodeInfo = BroadcastNodeInfo.parseFrom(packet
					.getContent());
			List<NodeInfo> nodes = nodeInfo.getNodesList();
			for (NodeInfo node : nodes) {
				NodeList.addNode(node);
			}

			super.commonResponses(packet.getFrom(), packet.getSerialNumber(),
					packet.getCommand(), PlatformResponseResult.success);

			NodeStatusReport.Builder builder = NodeStatusReport.newBuilder();
			Packet _out_packet = new Packet(true);
			_out_packet.setCommand(AllCommands.NodeCluster.NodeStatusReport_VALUE);
			_out_packet.setProtocol(LCMessageType.PLATFORM);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setFrom(NodeHelper.getNodeCode());

			logger.info("当前节点状态: "+NodeHelper.nodeStatus.name());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
//			//是否已经运行当中
			if(NodeHelper.nodeStatus.getNumber() == NodeStatus.failed_VALUE){
				NodeHelper.nodeStatus = NodeStatus.notBuilding;
				builder.setStatus(NodeHelper.nodeStatus);
			}else{
				builder.setStatus(NodeHelper.nodeStatus);
			}
			_out_packet.setContent(builder.build().toByteArray());
			super.broadcast(_out_packet);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return 0;
	}

}

//package com.navinfo.opentsp.platform.dp.core.acceptor.receiver;
//
//import com.google.protobuf.InvalidProtocolBufferException;
//import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalOnlineSwitch;
//import com.lc.dp.acceptor.tal.protocol.TACommand;
//import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
//import com.navinfo.opentsp.platform.dp.core.common.NodeLinkPo;
//import com.navinfo.opentsp.platform.dp.core.redis .JacksonUtil;
//import com.navinfo.opentsp.platform.dp.core.redis .RedisUtil;
//import com.navinfo.opentsp.platform.location.kit.Packet;
//
//public class TA_3007_TerminalOnlineSwitch extends TACommand {
//
//	@Override
//	public int processor(Packet packet) {
//		long nodeCode = 0;
//		if(packet.getParamter("taNodeCode") != null) {
//			nodeCode = (long)packet.getParamter("taNodeCode");
//		}
//		NodeLinkPo nodeLinkPo = RedisUtil.getRedis().getHashValue("NodeLink",packet.getUniqueMark(),NodeLinkPo.class);
//		byte[] bytes = packet.getContent();
//		try {
//			LCTerminalOnlineSwitch.TerminalOnlineSwitch builder = LCTerminalOnlineSwitch.TerminalOnlineSwitch.parseFrom(bytes);
//			//缓存上下线状态,TA-DP链路
//			if(nodeLinkPo != null) {
//				if(nodeCode != nodeLinkPo.getTaNode() || NodeHelper.getNodeCode() != nodeLinkPo.getDpNode()) {
//					nodeLinkPo.setTaNode(nodeCode);
//					nodeLinkPo.setDpNode(NodeHelper.getNodeCode());
//					nodeLinkPo.setOnlineStatus(builder.getStatus());
//					RedisUtil.getRedis().setHashValue("NodeLink",packet.getUniqueMark(), JacksonUtil.toJSon(nodeLinkPo));
//				}
//			} else {
//				nodeLinkPo = new NodeLinkPo();
//				nodeLinkPo.setTaNode(nodeCode);
//				nodeLinkPo.setDpNode(NodeHelper.getNodeCode());
//				nodeLinkPo.setOnlineStatus(builder.getStatus());
//				RedisUtil.getRedis().setHashValue("NodeLink",packet.getUniqueMark(), JacksonUtil.toJSon(nodeLinkPo));
//			}
//		} catch (InvalidProtocolBufferException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return super.writeToRequestProcessing(packet);
//	}
//
//}

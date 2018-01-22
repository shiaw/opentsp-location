//package com.navinfo.opentsp.platform.dp.core.acceptor.ta.receiver;
//
//import com.google.protobuf.InvalidProtocolBufferException;
//import com.navinfo.opentsp.platform.location.kit.Packet;
//import com.navinfo.opentsp.platform.dp.core.acceptor.ta.TACommand;
//import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
//import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCReportServerIdentify.ReportServerIdentify;
//
///**
// * 节点汇报<br>
// * 收到TA节点汇报后,向DA节点发送获取此TA节点的终端列表请求
// *
// * @author lgw
// *
// */
//public class TA_0602_ReportServerIdentify extends TACommand {
//
//	public int processor(Packet packet) {
//		try {
//			ReportServerIdentify reportServerIdentify = ReportServerIdentify.parseFrom(packet.getContent());
//			log.info("收到TA层节点["+reportServerIdentify.getNodeCode()+"]汇报节点编号.");
//
//			//绑定节点编号与Session
//			long sessionId = Long.parseLong(packet.getParamterForString("sessionId"));
//			//TAMutualSessionManage.getInstance().bind(reportServerIdentify.getNodeCode(), sessionId);
//			//设置Session属性
//			//TAMutualSessionManage.getInstance().getSessionForSessionId(sessionId).getIoSession().setAttribute("nodeCode", reportServerIdentify.getNodeCode());
//
//			super.commonResponsesForPlatform(packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success);
//
//		} catch (InvalidProtocolBufferException e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}
//
//
//}

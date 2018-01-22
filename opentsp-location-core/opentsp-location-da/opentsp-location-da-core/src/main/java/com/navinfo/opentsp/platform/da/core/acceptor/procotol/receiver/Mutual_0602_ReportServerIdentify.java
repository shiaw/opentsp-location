package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.acceptor.session.MutualSessionManage;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCReportServerIdentify;
@DaRmiNo(id = "0602")
public class Mutual_0602_ReportServerIdentify extends Dacommand {
	@Override
	public Packet processor(Packet packet) {
		try {
			LCReportServerIdentify.ReportServerIdentify reportServerIdentify = LCReportServerIdentify.ReportServerIdentify
					.parseFrom(packet.getContent());
			logger.info("收到节点["+reportServerIdentify.getNodeCode()+"]汇报节点编号.");
			// 绑定节点编号与Session
			long sessionId = Long.parseLong(packet
					.getParamterForString("sessionId"));
			MutualSessionManage.getInstance().bind(
					reportServerIdentify.getNodeCode(), sessionId);
			// 设置Session属性
			MutualSessionManage
					.getInstance()
					.getMutualSessionForSessionId(sessionId)
					.getIoSession()
					.setAttribute("nodeCode",
							reportServerIdentify.getNodeCode());
			return super.commonResponsesForPlatform(packet.getFrom(),
					packet.getSerialNumber(), packet.getCommand(),
					LCPlatformResponseResult.PlatformResponseResult.success);
		} catch (InvalidProtocolBufferException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

}

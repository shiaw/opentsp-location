package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.OptResult;
import com.navinfo.opentsp.platform.da.core.persistence.UpperServiceManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.UpperServiceManageImpl;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCServerStatusNotice;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCServerStatusNoticeRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DaRmiNo(id = "0107")
public class Mutual_0107_ServerStatusNotice extends Dacommand {
	final static UpperServiceManage upperServiceManage = new UpperServiceManageImpl();
	private static Logger logger = LoggerFactory.getLogger(Mutual_0107_ServerStatusNotice.class);
	@Override
	public Packet processor(Packet packet) {
		try {
			LCServerStatusNotice.ServerStatusNotice statusNotice = LCServerStatusNotice.ServerStatusNotice
					.parseFrom(packet.getContent());
			logger.info("登录操作：from:"+packet.getFrom()+" IP:"+statusNotice.getServerIp()+" Iden:"+statusNotice.getServerIdentifies()+" status:"+statusNotice.getStatus().getNumber());
			OptResult optResult = upperServiceManage.serverStatusNotice(
					statusNotice.getServerIp(), statusNotice
							.getServerIdentifies(), statusNotice.getStatus()
							.getNumber());
			logger.info("result:"+optResult.getStatus());
			return this.buildPacket(statusNotice, optResult, packet);
		} catch (InvalidProtocolBufferException e) {
			logger.error(e.getMessage());
			return null;
		}

	}
	/**
	 * 构建packet
	 *
	 * @return
	 */
	private Packet buildPacket(LCServerStatusNotice.ServerStatusNotice statusNotice , OptResult optResult , Packet packet) {
		LCServerStatusNoticeRes.ServerStatusNoticeRes.Builder builder = LCServerStatusNoticeRes.ServerStatusNoticeRes.newBuilder();
		builder.setChannelIdentify(statusNotice.getChannelIdentify());
		builder.setResults(LCPlatformResponseResult.PlatformResponseResult.valueOf(optResult.getStatus()));
		builder.setSerialNumber(packet.getSerialNumber());
		builder.setStatus(statusNotice.getStatus());

		Packet _out_packet = new Packet(true);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setCommand(LCAllCommands.AllCommands.Platform.ServerStatusNoticeRes_VALUE);
		_out_packet.setTo(packet.getFrom());
		_out_packet.setContent(builder.build().toByteArray());
		return _out_packet;
	}
}
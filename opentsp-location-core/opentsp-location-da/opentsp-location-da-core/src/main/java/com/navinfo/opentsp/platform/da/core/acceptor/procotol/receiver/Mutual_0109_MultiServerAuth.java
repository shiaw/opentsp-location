package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.application.UpperServiceManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.UpperServiceManage;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCMultiServerAuth;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCMultiServerAuthRes;
@DaRmiNo(id = "0109")
public class Mutual_0109_MultiServerAuth extends Dacommand {
	final static UpperServiceManage upperServiceManage = new UpperServiceManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			LCMultiServerAuth.MultiServerAuth multiServerAuth = LCMultiServerAuth.MultiServerAuth.parseFrom(packet
					.getContent());
			Map<Long, String> map = upperServiceManage.multiServerAuth(
					Long.parseLong(multiServerAuth.getChannelIdentify()),
					multiServerAuth.getServerIpList());
			logger.info("多服务鉴权,主uniqueMark:"+multiServerAuth.getChannelIdentify()+" 结果:"+map.toString());
			return buildPacket(map,
					multiServerAuth.getChannelIdentify(),packet);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 构建packet
	 * @param map
	 * @param channelIdentity
	 * @param packet
	 * @return
	 */
	private Packet buildPacket(Map<Long, String> map, String channelIdentity , Packet packet) {
		LCMultiServerAuthRes.MultiServerAuthRes.Builder builder = LCMultiServerAuthRes.MultiServerAuthRes
				.newBuilder();
		LCMultiServerAuthRes.MultiServerAuthRes.ServerAuthInfo.Builder authInfo = LCMultiServerAuthRes.MultiServerAuthRes.ServerAuthInfo.newBuilder();
		builder.setChannelIdentify(channelIdentity);
		builder.setResults(LCPlatformResponseResult.PlatformResponseResult.success);
		builder.setSerialNumber(packet.getSerialNumber());
		if (map != null && !map.isEmpty()) {
			Set<Map.Entry<Long, String>> set = map.entrySet();
			for (Iterator<Map.Entry<Long, String>> it = set.iterator(); it
					.hasNext();) {
				Map.Entry<Long, String> entry = (Map.Entry<Long, String>) it
						.next();
				authInfo.setServerIdentify(entry.getKey());
				authInfo.setServerIp(entry.getValue());
				builder.addInfos(authInfo.build());
			}

		}

		Packet _out_packet = new Packet(true);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setCommand(LCAllCommands.AllCommands.Platform.MultiServerAuthRes_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setTo(packet.getFrom());
		_out_packet.setFrom(NodeHelper.getNodeCode());
		_out_packet.setContent(builder.build().toByteArray());
		return _out_packet;
	}

}

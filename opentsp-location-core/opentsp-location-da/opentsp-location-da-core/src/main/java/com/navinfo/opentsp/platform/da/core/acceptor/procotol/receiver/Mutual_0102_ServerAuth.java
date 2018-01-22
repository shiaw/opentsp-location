package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.common.Configuration;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.OptResult;
import com.navinfo.opentsp.platform.da.core.persistence.application.LcNodeManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.UpperServiceManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.LcNodeManage;
import com.navinfo.opentsp.platform.da.core.persistence.UpperServiceManage;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCExtendConfig;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeType;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCRequestLoginKeyRes;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCServerAuth;

@DaRmiNo(id = "0102")
public class Mutual_0102_ServerAuth extends Dacommand {
	final static UpperServiceManage upperServiceManage = new UpperServiceManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			LCServerAuth.ServerAuth serverAuth = LCServerAuth.ServerAuth.parseFrom(packet.getContent());
			logger.info("DA收到分MM鉴权指令："+serverAuth.getServerIp()+"---"+serverAuth.getDistrict().getNumber());
			OptResult optResult = upperServiceManage.authentication(serverAuth
					.getName(), serverAuth.getPassword(), serverAuth
					.getVersion(), serverAuth.getDistrict().getNumber(),
					serverAuth.getServerIp());

			return buildPacket(serverAuth, optResult, packet);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 构建packet
	 * @param serverAuth
	 * @param optResult
	 * @param packet
	 * @return
	 */
	private Packet buildPacket(LCServerAuth.ServerAuth serverAuth, OptResult optResult,
			Packet packet) {
		LCRequestLoginKeyRes.RequestLoginKeyRes.Builder builder = LCRequestLoginKeyRes.RequestLoginKeyRes
				.newBuilder();
		builder.setDistrictCode(serverAuth.getDistrict());
		builder.setRequestLoginSerial(packet.getSerialNumber());
		if(optResult.getStatus() == LCPlatformResponseResult.PlatformResponseResult.success_VALUE){
			builder.setServerIdentifies(optResult.getParamterForLong("serverIdentifies"));
			LcNodeManage lcNodeManage = new LcNodeManageImpl();
			List<LcNodeConfigDBEntity> nodes = lcNodeManage.getNodesByNodeType(LCNodeType.NodeType.mm_VALUE);
			if(nodes != null && nodes.size() > 0){
				for (LcNodeConfigDBEntity lcNodeConfigDBEntity : nodes) {
					//节点编号
					Integer node_code = lcNodeConfigDBEntity.getNode_code();
					try {
						LCExtendConfig.ExtendConfig extendConfig = LCExtendConfig.ExtendConfig.parseFrom(lcNodeConfigDBEntity.getExt_content());
						List<LCExtendConfig.NodePort> ports = extendConfig.getPortsList();
						for (LCExtendConfig.NodePort nodePort : ports) {
							if(nodePort.getTypes().getNumber() == LCExtendConfig.NodePort.PortType.mm_server_VALUE){
								if(node_code.intValue() ==Integer.valueOf(Configuration.getString("MM.MASTER.CODE")).intValue() ){
									builder.setMmMasterNodeIp(lcNodeConfigDBEntity.getLocal_ip_address());
									builder.setMmMasterNodePort(nodePort.getPorts());
								}else if(node_code.intValue() ==Integer.valueOf(Configuration.getString("MM.SLAVE.CODE")).intValue() ){
									builder.setMmSlaverNodeIp(lcNodeConfigDBEntity.getLocal_ip_address());
									builder.setMmSlaverNodePort(nodePort.getPorts());
								}
							}
						}
					} catch (InvalidProtocolBufferException e) {
						e.printStackTrace();
					}
				}
			}
		}
		builder.setResults(LCPlatformResponseResult.PlatformResponseResult.valueOf(optResult.getStatus()));
		builder.setChannelIdentify(serverAuth.getChannelIdentify());


		logger.error("[DA->分区MM] 返回鉴权验证 0x1102");
		Packet _out_packet = new Packet();
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setCommand(LCAllCommands.AllCommands.Platform.ServerAuthRes_VALUE);
		_out_packet.setTo(packet.getFrom());
		_out_packet.setContent(builder.build().toByteArray());
		return _out_packet;
	}

}

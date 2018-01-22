package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.persistence.application.LcNodeManageImpl;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.LcNodeManage;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode;
import com.navinfo.opentsp.platform.location.protocol.district.LCQueryDistrictMMNodeRes;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCExtendConfig;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeInfo;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeType;
import org.apache.commons.collections.CollectionUtils;

@DaRmiNo(id = "0003")
public class Mutual_0003_QueryDistrictMMNode extends Dacommand {
	final static LcNodeManage lcNodeManage = new LcNodeManageImpl();

	@Override
	public Packet processor(Packet packet) {
		List<LcNodeConfigDBEntity> mmNode=lcNodeManage.getNodesByNodeType(LCNodeType.NodeType.mm_VALUE);
		return buildPacket(mmNode, packet);
	}

	/**
	 * 构建packet
	 * @param mmNode
	 * @param packet
	 * @return
	 */
	private Packet buildPacket(List<LcNodeConfigDBEntity> mmNode,Packet packet) {
		LCQueryDistrictMMNodeRes.QueryDistrictMMNodeRes.Builder builder=LCQueryDistrictMMNodeRes.QueryDistrictMMNodeRes.newBuilder();
		builder.setSerialNumber(packet.getSerialNumber());
		if(CollectionUtils.isNotEmpty(mmNode)){
			for(LcNodeConfigDBEntity entity:mmNode){
				try {
					LCNodeInfo.NodeInfo.Builder nodeBuilder= LCNodeInfo.NodeInfo.newBuilder();
					if(entity.getExt_content() != null){
						nodeBuilder.setExtendConfigs(LCExtendConfig.ExtendConfig.parseFrom(entity.getExt_content()));
					}
					nodeBuilder.setAddressInternetIP(entity.getInternet_ip_address());
					nodeBuilder.setAddressLocalIP(entity.getLocal_ip_address());
					nodeBuilder.setDistrictCode(LCDistrictCode.DistrictCode.valueOf(entity.getDistrict()));
					nodeBuilder.setMaxLimit(entity.getMax_limit());
					nodeBuilder.setMinLimit(entity.getMin_limit());
					nodeBuilder.setNodeCode(entity.getNode_code());
					nodeBuilder.setTypes(LCNodeType.NodeType.valueOf(entity.getNode_type()));
					nodeBuilder.setIsRedundancy(entity.getIs_redundance() == 1 ? false : true);
					builder.addNodes(nodeBuilder.build());
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
			}
		}
		Packet _out_packet = new Packet(true);
		_out_packet.setCommand(LCAllCommands.AllCommands.Platform.QueryDistrictMMNodeRes_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setTo(packet.getFrom());
		_out_packet.setFrom(NodeHelper.getNodeCode());
		_out_packet.setContent(builder.build().toByteArray());
		return _out_packet;
	}

}

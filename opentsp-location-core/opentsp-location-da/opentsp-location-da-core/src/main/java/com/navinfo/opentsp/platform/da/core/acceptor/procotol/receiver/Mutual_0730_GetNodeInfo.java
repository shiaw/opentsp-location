package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.util.List;

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
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCGetNodeInfo;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCGetNodeInfoRes;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCExtendConfig;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeInfo;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeType;
import org.apache.commons.collections.CollectionUtils;

import com.google.protobuf.InvalidProtocolBufferException;
@DaRmiNo(id = "0730")
public class Mutual_0730_GetNodeInfo extends Dacommand {
	final static LcNodeManage lcNodeManage = new LcNodeManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			LCGetNodeInfo.GetNodeInfo getNodeInfo = LCGetNodeInfo.GetNodeInfo.parseFrom(packet.getContent());
			//获取参数
			int districtCode = getNodeInfo.getDistrictCode().getNumber();
			//从数据库取数据
			List<LcNodeConfigDBEntity> nodes=lcNodeManage.getNodeInfo(districtCode);
			return buildPacket(nodes,packet);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 构建packet
	 * @param mmNode
	 * @param packet
	 * @return
	 */
	private Packet buildPacket(List<LcNodeConfigDBEntity> mmNode,Packet packet) {
		LCGetNodeInfoRes.GetNodeInfoRes.Builder builder= LCGetNodeInfoRes.GetNodeInfoRes.newBuilder();
		builder.setSerialNumber(packet.getSerialNumber());
		builder.setResults(LCPlatformResponseResult.PlatformResponseResult.success);
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
					nodeBuilder.setIsRedundancy(entity.getIs_redundance() == 1 ? true : false);
					System.err.println(nodeBuilder.getNodeCode()+"---"+nodeBuilder.getIsRedundancy());
					builder.addNodeInfo(nodeBuilder.build());
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
			}
		}
		Packet _out_packet = new Packet(true);
		_out_packet.setCommand(LCAllCommands.AllCommands.NodeCluster.GetNodeInfoRes_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setTo(packet.getFrom());
		_out_packet.setFrom(NodeHelper.getNodeCode());
		_out_packet.setContent(builder.build().toByteArray());
		return _out_packet;
	}

}

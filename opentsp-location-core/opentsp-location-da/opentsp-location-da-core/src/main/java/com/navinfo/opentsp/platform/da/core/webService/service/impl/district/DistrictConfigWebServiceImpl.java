package com.navinfo.opentsp.platform.da.core.webService.service.impl.district;

import java.util.List;

import javax.jws.WebService;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCExtendConfig.ExtendConfig;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeInfo;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeType.NodeType;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.MutualCommandFacotry;
import  com.navinfo.opentsp.platform.da.core.acceptor.session.MutualSessionManage;
import  com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import  com.navinfo.opentsp.platform.da.core.persistence.LcNodeManage;
import  com.navinfo.opentsp.platform.da.core.persistence.application.LcNodeManageImpl;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeConfigDBEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceConfigDBEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceDistrictConfigDBEntity;
import  com.navinfo.opentsp.platform.da.core.webService.service.ConfigWebService;

@WebService(endpointInterface = " com.navinfo.opentsp.platform.da.core.webService.service.ConfigWebService")
public class DistrictConfigWebServiceImpl implements ConfigWebService {
	private LcNodeManage lcNodeManage = new LcNodeManageImpl();
	@Override
	public int queryServiceConfigResCount(String userName,
			String ip, int district, long start, long end,int flag){
		int count = lcNodeManage.queryServiceConfigResCount(userName, ip, district, start, end,flag);
		return count;
	}
	@Override
	public List<LcServiceConfigDBEntity> queryServiceConfigRes(String userName,
			String ip, int district, long start, long end,int flag,int currentPage,int pageSize) {
		List<LcServiceConfigDBEntity> result = lcNodeManage
				.queryServiceConfigRes(userName, ip, district, start, end,flag,currentPage,pageSize);
		return result;
	}

	@Override
	public PlatformResponseResult saveOrUpdateServiceConfig(
			LcServiceConfigDBEntity serviceConfig, int[] district,String authName) {
		PlatformResponseResult responeresult =lcNodeManage.saveOrUpdateServiceConfig(serviceConfig, district,authName);
	
		return responeresult;
	}

	@Override
	public int queryNodeConfigResCount(int district,
			int nodeCode,int nodetype) {
		int count = lcNodeManage
				.queryNodeConfigResCount(district, nodeCode,nodetype);
		return count;
	}
	@Override
	public List<LcNodeConfigDBEntity> queryNodeConfigRes(int district,
			int nodeCode,int nodeType,int currentPage,int pageSize,int nodetype) {
		List<LcNodeConfigDBEntity> nodeConfigs = lcNodeManage
				.queryNodeConfigRes(district, nodeCode,nodeType,currentPage,pageSize,nodetype);
		return nodeConfigs;
	}

	@Override
	public PlatformResponseResult saveOrUpdateNodeConfig(
			LcNodeConfigDBEntity nodeConfig) {
		PlatformResponseResult responeresult =lcNodeManage.saveOrUpdateNodeConfig(nodeConfig);
		
		LCNodeInfo.NodeInfo.Builder nodebuiler=LCNodeInfo.NodeInfo.newBuilder();
		nodebuiler.setTypes(NodeType.valueOf(nodeConfig.getNode_type()));
		nodebuiler.setNodeCode(nodeConfig.getNode_code());
		nodebuiler.setAddressLocalIP(nodeConfig.getLocal_ip_address());
		nodebuiler.setAddressInternetIP(nodeConfig.getInternet_ip_address());
		nodebuiler.setDistrictCode(DistrictCode.valueOf(nodeConfig.getDistrict()));
		nodebuiler.setMaxLimit(nodeConfig.getMax_limit());
		nodebuiler.setMinLimit(nodeConfig.getMin_limit());
		nodebuiler.setIsRedundancy(nodeConfig.getIs_redundance() == 1 ? false : true);
		ExtendConfig extendConfig = null;
		try {
			extendConfig = ExtendConfig.parseFrom(nodeConfig.getExt_content());
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		nodebuiler.setExtendConfigs(extendConfig);


		List<Long> nodeCodes = MutualSessionManage.getInstance().getNodeListByNodeType(NodeType.mm);
		for (Long nodeCode : nodeCodes) {
			Packet pk = new Packet(true);
			pk.setProtocol(LCConstant.LCMessageType.PLATFORM);
			pk.setCommand(AllCommands.DataAccess.TerminalInfoSynchronous_VALUE);
 		    pk.setContent(nodebuiler.build().toByteArray());
			pk.setUniqueMark(NodeHelper.getNodeUniqueMark());
			pk.setTo(nodeCode);
			MutualCommandFacotry.processor(pk);
		}
		return responeresult;
	}

	@Override
	public PlatformResponseResult deleteNodeConfig(int[] primaryKeys) {
		PlatformResponseResult responeresult =lcNodeManage.deleteNodeConfig(primaryKeys);
		return responeresult;
	}

	@Override
	public PlatformResponseResult deleteServiceConfig(int[] primaryKeys) {
		PlatformResponseResult responeresult =lcNodeManage.deleteServiceConfig(primaryKeys);
		return responeresult;
	}

	@Override
	public PlatformResponseResult deleteServiceConfigByAuthName(String authName) {
		PlatformResponseResult result=lcNodeManage.deleteServiceConfigByAuthName(authName);
		return result;
	}

	@Override
	public PlatformResponseResult deleteNodeConfigByNodeCode(int nodeCode) {
		PlatformResponseResult result=lcNodeManage.deleteNodeConfigByNodeCode(nodeCode);
		return result;
	}

	@Override
	public LcServiceConfigDBEntity queryServiceConfigById(int sc_id) {
		LcServiceConfigDBEntity serviceConfig = lcNodeManage.queryServiceConfigById(sc_id);
		return serviceConfig;
}

	@Override
	public List<LcServiceDistrictConfigDBEntity> queryServiceInDistrict(
			int sc_id) {
		List<LcServiceDistrictConfigDBEntity> result = lcNodeManage.queryServiceInDistrict(sc_id);
		return result;
}

	@Override
	public LcNodeConfigDBEntity queryNodeConfigById(int nc_id) {
		LcNodeConfigDBEntity enttiy = lcNodeManage.queryNodeConfigById(nc_id);
		return enttiy;
	}

}

package com.navinfo.opentsp.platform.da.core.webService.service.impl.center;

import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.LcNodeManage;
import com.navinfo.opentsp.platform.da.core.persistence.SynchronousManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.LcNodeManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.SynchronousManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceDistrictConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcSynchronizationLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.webService.manage.SynchronousDataCache;
import com.navinfo.opentsp.platform.da.core.webService.manage.WSConfigCache;
import com.navinfo.opentsp.platform.da.core.webService.manage.WSConfigCacheManage;
import com.navinfo.opentsp.platform.da.core.webService.manage.WebServiceTools;
import com.navinfo.opentsp.platform.da.core.webService.service.ConfigWebService;
import com.navinfo.opentsp.platform.location.kit.lang.ObjectUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCExtendConfig.ExtendConfig;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCExtendConfig.NodePort;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCExtendConfig.NodePort.PortType;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeType.NodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(endpointInterface = "com.navinfo.opentsp.platform.da.core.webService.service.ConfigWebService")
public class CenterConfigWebServiceImpl implements ConfigWebService {
	private static final Logger log = LoggerFactory.getLogger(CenterConfigWebServiceImpl.class);
	private LcNodeManage lcNodeManage = new LcNodeManageImpl();
	private SynchronousManage synchronousManage = new SynchronousManageImpl();
	@Override
	public int queryServiceConfigResCount(String userName,
										  String ip, int district, long start, long end,int flag){
		int count = lcNodeManage.queryServiceConfigResCount(userName, ip, district, start, end,flag);
		return count;
	}
	@Override
	public List<LcServiceConfigDBEntity> queryServiceConfigRes(String userName,
															   String ip, int district, long start, long end,int flag,int currentPage,int pageSize) {
		List<LcServiceConfigDBEntity> result= new ArrayList<LcServiceConfigDBEntity>();

		try {
			result = lcNodeManage
					.queryServiceConfigRes(userName, ip, district, start, end,flag,currentPage,pageSize);
//			if(district!=0){
//				//调用分区ws
//				ConfigWebService configWebService = (ConfigWebService) WebServiceTools
//						.getOperationClass(district + "", ConfigWebService.class);
//				result=configWebService.queryServiceConfigRes(userName, ip, district, start, end);
//				//根据分区编码在总部数据中查询分区数据
//			}else{
//				result = lcNodeManage
//						.queryServiceConfigRes(userName, ip, district, start, end);
//			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	@Override
	public PlatformResponseResult saveOrUpdateServiceConfig(
			LcServiceConfigDBEntity serviceConfig, int[] district,String authName) {
		// 基本数据保存
		PlatformResponseResult responeresult = lcNodeManage
				.saveOrUpdateServiceConfig(serviceConfig, district,authName);
		// 调用分区ws同步数据
		if (responeresult== PlatformResponseResult.success) {
			for (int i : district) {
				LcSynchronizationLogDBEntity entity = new LcSynchronizationLogDBEntity();
				try {
					//调用分区ws
					ConfigWebService configWebService = (ConfigWebService) WebServiceTools
							.getOperationClass(i + "", ConfigWebService.class);
					// 调用同步的方法，根据返回结果修改entity.setStatus(0);的值
					PlatformResponseResult wsResult = configWebService
							.saveOrUpdateServiceConfig(serviceConfig,
									new int[] { i },authName);
					// 同步成功status值为1，失败值为0;
					if (wsResult == PlatformResponseResult.success) {
						entity.setStatus(PlatformResponseResult.success_VALUE);
					} else {
						entity.setStatus(PlatformResponseResult.failure_VALUE);
					}
				} catch (Exception e) {
					e.printStackTrace();
					entity.setStatus(PlatformResponseResult.failure_VALUE);
				}
				//组装同步数据日志
				if (serviceConfig.getSc_id() == null
						|| serviceConfig.getSc_id() == 0) {
					entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.save);
				} else {
					entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.update);
				}
				entity.setId(SynchronousDataCache.updateMaxSynchronizationLogId());
				entity.setDistrict(i + "");
				entity.setModuleName(Constant.PropertiesKey.SynchronousDataParameters.ConfigWebService);
				entity.setOperationDate(System.currentTimeMillis() / 1000);
				entity.setOperationObject(ObjectUtils
						.objectToBytes(serviceConfig));
				entity.setType(Constant.PropertiesKey.DBTableName.LC_SERVICE_CONFIG);
				entity.setPrimaryKeys(i + "");
				entity.setWebserviceClass(ConfigWebService.class.getName());
				// 根据结果添加缓存数据存放，根据分区进行存放
				if (entity.getStatus() == PlatformResponseResult.failure_VALUE) {
					SynchronousDataCache.add(i, entity);
				}
				// 放同步数据日志表
				PlatformResponseResult result=synchronousManage.saveSynchronousData(entity);
				if(result==PlatformResponseResult.failure){
					log.error("同步数据日志表存储失败");

				}
			}
		}
		if(responeresult==PlatformResponseResult.success){
			return responeresult;
		}else{
			return PlatformResponseResult.failure;
		}
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
		// 基本数据保存
		PlatformResponseResult responeresult = lcNodeManage
				.saveOrUpdateNodeConfig(nodeConfig);
		if(nodeConfig.getDistrict()!=130000){ //总部没有分区同步。
			if(responeresult==PlatformResponseResult.success){
				MySqlConnPoolUtil.startTransaction();
				//如果是DA节点，添加DA节点至缓存中(新增分区服务节点时，先添加DA节点配置，再添加其他节点配置信息。)
				if(WSConfigCacheManage.getWSConfigCatch(String.valueOf(nodeConfig.getDistrict()+"")) == null){
					if(nodeConfig.getNode_type() == NodeType.da_VALUE){
						WSConfigCache wsConfigCache=new WSConfigCache();
						wsConfigCache.setIp(nodeConfig.getLocal_ip_address());
						wsConfigCache.setNodeCode(nodeConfig.getNode_code());
						wsConfigCache.setDistrict(String.valueOf(nodeConfig.getDistrict()+""));
						try {
							ExtendConfig extendConfig = ExtendConfig.parseFrom(nodeConfig.getExt_content());
							if(extendConfig != null){
								List<NodePort> ports = extendConfig.getPortsList();
								for (NodePort nodePort : ports) {
									if(PortType.da_ws_port_VALUE== nodePort.getTypes().getNumber()){
										wsConfigCache.setPort(nodePort.getPorts()+"");
									}
								}
							}
							WSConfigCacheManage.addWSConfigCatch(String.valueOf(nodeConfig.getDistrict()+""), wsConfigCache);
							MySqlConnPoolUtil.commit();
						} catch (Exception e) {
							e.printStackTrace();
							MySqlConnPoolUtil.rollback();
							lcNodeManage.deleteNodeConfigByNodeCode(nodeConfig.getNode_code());
							return PlatformResponseResult.failure;
						}finally {
							MySqlConnPoolUtil.close();
						}
					}else{
						//事务回滚
						MySqlConnPoolUtil.rollback();
						lcNodeManage.deleteNodeConfigByNodeCode(nodeConfig.getNode_code());
						return PlatformResponseResult.failure;
					}
				}
				LcSynchronizationLogDBEntity entity = new LcSynchronizationLogDBEntity();
				//调用分区ws
				try {
					ConfigWebService configWebService = (ConfigWebService) WebServiceTools
							.getOperationClass(nodeConfig.getDistrict() + "",
									ConfigWebService.class);
					PlatformResponseResult result = configWebService
							.saveOrUpdateNodeConfig(nodeConfig);
					if (result.getNumber() == PlatformResponseResult.success_VALUE) {
						entity.setStatus(PlatformResponseResult.success_VALUE);
					} else {
						entity.setStatus(PlatformResponseResult.failure_VALUE);
					}
				} catch (Exception e) {
					e.printStackTrace();
					entity.setStatus(PlatformResponseResult.failure_VALUE);
				}
				//组装同步数据日志
				if (nodeConfig.getNc_id() == null || nodeConfig.getNc_id() == 0) {
					entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.save);
				} else {
					entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.update);
				}
				entity.setId(SynchronousDataCache.updateMaxSynchronizationLogId());
				entity.setDistrict(nodeConfig.getDistrict() + "");
				entity.setModuleName(Constant.PropertiesKey.SynchronousDataParameters.ConfigWebService);
				entity.setOperationDate(System.currentTimeMillis() / 1000);
				entity.setOperationObject(ObjectUtils.objectToBytes(nodeConfig));
				entity.setType(Constant.PropertiesKey.DBTableName.LC_NODE_CONFIG);
				entity.setWebserviceClass(ConfigWebService.class.getName());
				//缓存
				if (entity.getStatus() == PlatformResponseResult.failure_VALUE) {
					SynchronousDataCache.add(nodeConfig.getDistrict(), entity);
				}
				//日志表
				PlatformResponseResult result=synchronousManage.saveSynchronousData(entity);
				if(result==PlatformResponseResult.failure){
					log.error("同步数据日志表存储失败");
				}
			}
		}
		if(responeresult==PlatformResponseResult.success){
			return responeresult;
		}else{
			return PlatformResponseResult.failure;
		}
	}

	@Override
	public PlatformResponseResult deleteNodeConfig(int[] primaryKeys) {
		// 基本删除
		PlatformResponseResult responeresult = lcNodeManage
				.deleteNodeConfig(primaryKeys);
		// 调用分区ws同步数据
		if (responeresult == PlatformResponseResult.success) {
			for (int i : primaryKeys) {
				LcNodeConfigDBEntity node = lcNodeManage.queryNodeConfigById(i);
				LcSynchronizationLogDBEntity entity = new LcSynchronizationLogDBEntity();
				//调用ws
				try {
					ConfigWebService configWebService = (ConfigWebService) WebServiceTools
							.getOperationClass(node.getDistrict() + "", ConfigWebService.class);
					PlatformResponseResult wsResult=configWebService.deleteNodeConfigByNodeCode(node
							.getNode_code());
					if(wsResult==PlatformResponseResult.success){
						entity.setStatus(PlatformResponseResult.success_VALUE);
					}else{
						entity.setStatus(PlatformResponseResult.failure_VALUE);
					}
				} catch (Exception e) {
					e.printStackTrace();
					entity.setStatus(PlatformResponseResult.failure_VALUE);
				}
				entity.setId(SynchronousDataCache.updateMaxSynchronizationLogId());
				//组装同步数据
				entity.setDistrict(node.getDistrict() + "");
				entity.setModuleName(Constant.PropertiesKey.SynchronousDataParameters.ConfigWebService);
				entity.setOperationDate(System.currentTimeMillis() / 1000);
				entity.setOperationObject(ObjectUtils.objectToBytes(entity));
				entity.setType(Constant.PropertiesKey.DBTableName.LC_NODE_CONFIG);
				entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.delete);
				entity.setWebserviceClass(ConfigWebService.class.getName());
				//放缓存
				if(entity.getStatus()==0){
					SynchronousDataCache.add(node.getDistrict(), entity);
				}
				//保存日志
				PlatformResponseResult result=synchronousManage.saveSynchronousData(entity);
				if(result==PlatformResponseResult.failure){
					log.error("同步数据日志表存储失败");
				}
			}
		}
		if(responeresult==PlatformResponseResult.success){
			return responeresult;
		}else{
			return PlatformResponseResult.failure;
		}

	}

	@Override
	public PlatformResponseResult deleteServiceConfig(int[] primaryKeys) {
		// 基本删除
		PlatformResponseResult responeresult = lcNodeManage
				.deleteServiceConfig(primaryKeys);
		// 调用分区ws同步数据
		if (responeresult.getNumber() == PlatformResponseResult.success_VALUE) {
			for (int i : primaryKeys) {
				LcServiceConfigDBEntity serviceConfig = lcNodeManage.queryServiceConfigById(i);
				List<LcServiceDistrictConfigDBEntity> list = lcNodeManage.queryServiceInDistrict(i);
				for (int k = 0; k < list.size(); k++) {
					LcSynchronizationLogDBEntity entity = new LcSynchronizationLogDBEntity();
					//调用ws
					try {
						ConfigWebService configWebService = (ConfigWebService) WebServiceTools
								.getOperationClass(list.get(k).getService_district()+"", ConfigWebService.class);
						PlatformResponseResult wsResult=configWebService.deleteServiceConfigByAuthName(serviceConfig
								.getAuth_name());
						if(wsResult==PlatformResponseResult.success){
							entity.setStatus(1);
						}else{
							entity.setStatus(0);
						}
					} catch (Exception e) {
						e.printStackTrace();
						entity.setStatus(0);
					}
					entity.setId(SynchronousDataCache.updateMaxSynchronizationLogId());
					entity.setDistrict(list.get(k).getService_district()+"");
					entity.setModuleName(Constant.PropertiesKey.SynchronousDataParameters.ConfigWebService);
					entity.setOperationDate(System.currentTimeMillis() / 1000);
					entity.setOperationObject(ObjectUtils.objectToBytes(entity));
					entity.setType(Constant.PropertiesKey.DBTableName.LC_SERVICE_CONFIG);
					entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.delete);
					entity.setWebserviceClass(ConfigWebService.class.getName());
					//放缓存
					if(entity.getStatus()==0){
						SynchronousDataCache.add(list.get(k).getService_district(), entity);
					}
					//放数据库
					PlatformResponseResult result=synchronousManage.saveSynchronousData(entity);
					if(result==PlatformResponseResult.failure){
						log.error("同步数据日志表存储失败");
					}
				}
			}

		}
		if(responeresult==PlatformResponseResult.success){
			return responeresult;
		}else{
			return PlatformResponseResult.failure;
		}
	}


	@Override
	public PlatformResponseResult deleteServiceConfigByAuthName(String authName) {
		PlatformResponseResult result = lcNodeManage
				.deleteServiceConfigByAuthName(authName);
		return result;
	}

	@Override
	public PlatformResponseResult deleteNodeConfigByNodeCode(int nodeCode) {
		PlatformResponseResult result = lcNodeManage
				.deleteNodeConfigByNodeCode(nodeCode);
		return result;
	}

	@Override
	public LcServiceConfigDBEntity queryServiceConfigById(int sc_id) {
		LcServiceConfigDBEntity serviceConfig = lcNodeManage
				.queryServiceConfigById(sc_id);
		return serviceConfig;
	}

	@Override
	public List<LcServiceDistrictConfigDBEntity> queryServiceInDistrict(
			int sc_id) {
		List<LcServiceDistrictConfigDBEntity> result = lcNodeManage
				.queryServiceInDistrict(sc_id);
		return result;
	}

	@Override
	public LcNodeConfigDBEntity queryNodeConfigById(int nc_id) {
		LcNodeConfigDBEntity enttiy = lcNodeManage.queryNodeConfigById(nc_id);
		return enttiy;
	}

}

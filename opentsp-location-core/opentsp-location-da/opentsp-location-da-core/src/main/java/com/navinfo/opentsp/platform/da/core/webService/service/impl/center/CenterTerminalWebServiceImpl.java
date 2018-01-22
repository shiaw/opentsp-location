package com.navinfo.opentsp.platform.da.core.webService.service.impl.center;

import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.OptResult;
import com.navinfo.opentsp.platform.da.core.persistence.SynchronousManage;
import com.navinfo.opentsp.platform.da.core.persistence.TerminalInfoManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.SynchronousManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.TerminalInfoManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.MediaFileModel;
import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.TerminalOnlineModel;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.*;
import com.navinfo.opentsp.platform.da.core.webService.manage.SynchronousDataCache;
import com.navinfo.opentsp.platform.da.core.webService.manage.WebServiceTools;
import com.navinfo.opentsp.platform.da.core.webService.service.TerminalWebService;
import com.navinfo.opentsp.platform.location.kit.lang.ObjectUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(endpointInterface = "com.navinfo.opentsp.platform.da.core.webService.service.TerminalWebService")
public class CenterTerminalWebServiceImpl implements TerminalWebService {
	private static final Logger log = LoggerFactory
			.getLogger(CenterConfigWebServiceImpl.class);
	private TerminalInfoManage terminalInfoManage = new TerminalInfoManageImpl();
	private SynchronousManage synchronousManage = new SynchronousManageImpl();

	@Override
	public List<LcTerminalParaDBEntity> queryTerminalParaRes(long terminalId,
															 int command,int currentPage,int pageSize) {
		LcTerminalInfoDBEntity terminal = terminalInfoManage
				.queryTerminalInfo(terminalId);
		if (terminal != null) {
			try {
				// 调用分区ws
				TerminalWebService terminalWebService = (TerminalWebService) WebServiceTools
						.getOperationClass(terminal.getDistrict().toString(),
								TerminalWebService.class);
				List<LcTerminalParaDBEntity> list = terminalWebService
						.queryTerminalParaRes(terminalId, command,currentPage,pageSize);
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public PlatformResponseResult saveOrUpdateTerminalInfo(
			LcTerminalInfoDBEntity terminalInfo) {
		return terminalInfoManage.saveOrUpdateTerminalInfoForSyn(terminalInfo);

	}
	@Override
	public PlatformResponseResult saveOrUpdateTerminalInfo2(
			LcTerminalInfoDBEntity terminalInfo,boolean isAdd) {
		log.error("这是分区方法，总部不应该调用，请检查逻辑。");
		return null;
	}

	@Override
	public List<LcTerminalInfoDBEntity> queryTerminalInfoRes(long terminalId,
															 int protoType,String deviceId,long changeId, long start, long end,int flag,int currentPage,int pageSize) {
		List<LcTerminalInfoDBEntity> list=terminalInfoManage.queryTerminalInfoRes(terminalId, protoType,deviceId,changeId, start, end,flag,currentPage,pageSize);
		return list;
	}
	@Override
	public int queryTerminalInfoResCount(long terminalId,
										 int protoType,String deviceId,long changeId, long start, long end){
		int count = terminalInfoManage.queryTerminalInfoResCount(terminalId, protoType,deviceId,changeId, start, end);
		return count;
	}

	@Override
	public LcTerminalInfoDBEntity queryTerminalInfoResById(long primaryKey) {
		LcTerminalInfoDBEntity terminalInfo=terminalInfoManage.queryTerminalInfo(primaryKey);
		return terminalInfo;
	}

	@Override
	public PlatformResponseResult deleteTerminalInfo(long[] primaryKeys) {
		return terminalInfoManage.deleteTerminalInfoForSyn(primaryKeys);

	}

	@Override
	public LcTerminalRegisterDBEntity queryTerminalRegisterRes(long terminalId) {
		LcTerminalInfoDBEntity terminal = terminalInfoManage
				.queryTerminalInfo(terminalId);
		if (terminal != null) {
			try {
				// 调用分区ws
				TerminalWebService terminalWebService = (TerminalWebService) WebServiceTools
						.getOperationClass(terminal.getDistrict().toString(),
								TerminalWebService.class);
				LcTerminalRegisterDBEntity register = terminalWebService
						.queryTerminalRegisterRes(terminalId);
				return register;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}

	}

	@Override
	public List<LcTerminalRuleDBEntity> queryTerminalRuleRes(long terminalId,int currentPage,int pageSize) {

		LcTerminalInfoDBEntity terminal = terminalInfoManage
				.queryTerminalInfo(terminalId);
		if (terminal != null) {
			try {
				// 调用分区ws
				TerminalWebService terminalWebService = (TerminalWebService) WebServiceTools
						.getOperationClass(terminal.getDistrict().toString(),
								TerminalWebService.class);
				List<LcTerminalRuleDBEntity> list = terminalWebService
						.queryTerminalRuleRes(terminalId,currentPage,pageSize);
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public TerminalOnlineModel queryTerminalOnLineRes(long terminalId) {

		LcTerminalInfoDBEntity terminal = terminalInfoManage
				.queryTerminalInfo(terminalId);
		if (terminal != null) {
			try {
				// 调用分区ws
				TerminalWebService terminalWebService = (TerminalWebService) WebServiceTools
						.getOperationClass(terminal.getDistrict().toString(),
								TerminalWebService.class);
				TerminalOnlineModel model = terminalWebService.queryTerminalOnLineRes(terminalId);
				return model;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public List<LcMultimediaParaDBEntity> queryMediaParaRes(long terminalId,
															long start, long end,int currentPage,int pageSize) {
		LcTerminalInfoDBEntity terminal = terminalInfoManage
				.queryTerminalInfo(terminalId);
		if (terminal != null) {
			try {
				// 调用分区ws
				TerminalWebService terminalWebService = (TerminalWebService) WebServiceTools
						.getOperationClass(terminal.getDistrict().toString(),
								TerminalWebService.class);
				List<LcMultimediaParaDBEntity> list = terminalWebService.queryMediaParaRes(terminalId, start, end,currentPage,pageSize);
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public MediaFileModel queryMediaFileRes(String fileCode, long terminalId) {
		// 多媒体文件查询
		LcTerminalInfoDBEntity terminal = terminalInfoManage
				.queryTerminalInfo(terminalId);
		if (terminal != null) {
			try {
				// 调用分区ws
				TerminalWebService terminalWebService = (TerminalWebService) WebServiceTools
						.getOperationClass(terminal.getDistrict().toString(),
								TerminalWebService.class);
				MediaFileModel model = terminalWebService.queryMediaFileRes(fileCode,terminalId);
				return model;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}

	}

	@Override
	public List<LcTerminalAreaDBEntity> queryTerminalAreaRes(long terminalId,
															 int areaId, long start, long end,int currentPage,int pageSize) {
		LcTerminalInfoDBEntity terminal = terminalInfoManage
				.queryTerminalInfo(terminalId);
		if (terminal != null) {
			try {
				// 调用分区ws
				TerminalWebService terminalWebService = (TerminalWebService) WebServiceTools
						.getOperationClass(terminal.getDistrict().toString(),
								TerminalWebService.class);
				List<LcTerminalAreaDBEntity> list = terminalWebService.queryTerminalAreaRes(terminalId, areaId, start, end,currentPage,pageSize);
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}


	@Override
	public List<LcTerminalAreaDataDBEntity> queryAreaDataRes(long terminalId,
															 int taId) {
		LcTerminalInfoDBEntity terminal = terminalInfoManage
				.queryTerminalInfo(terminalId);
		if (terminal != null) {
			try {
				// 调用分区ws
				TerminalWebService terminalWebService = (TerminalWebService) WebServiceTools
						.getOperationClass(terminal.getDistrict().toString(),
								TerminalWebService.class);
				List<LcTerminalAreaDataDBEntity> list = terminalWebService.queryAreaDataRes(terminalId, taId);
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public PlatformResponseResult deleteTerminalInfoByTerminalId(long terminalId) {
		PlatformResponseResult responseResult = deleteTerminalInfo(new long[] { terminalId });
		return responseResult;
	}

	@Override
	public LcTerminalRuleDBEntity queryTerminalRuleById(long terminalId,
														int trId) {
		LcTerminalInfoDBEntity terminal = terminalInfoManage
				.queryTerminalInfo(terminalId);
		if (terminal != null) {
			try {
				// 调用分区ws
				TerminalWebService terminalWebService = (TerminalWebService) WebServiceTools
						.getOperationClass(terminal.getDistrict().toString(),
								TerminalWebService.class);
				LcTerminalRuleDBEntity entity = terminalWebService.queryTerminalRuleById(terminalId, trId);
				return entity;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public PlatformResponseResult batchAddTerminalInfo(List<LcTerminalInfoDBEntity> list) {
		// 基本信息保存
		PlatformResponseResult responseResult = terminalInfoManage.batchAddTerminalInfo(list);
		if (responseResult == PlatformResponseResult.success) {
			// 调用分区ws
			LcSynchronizationLogDBEntity entity = new LcSynchronizationLogDBEntity();
			try {
				TerminalWebService termianlWebService = (TerminalWebService) WebServiceTools
						.getOperationClass(list.get(0).getDistrict() + "",
								TerminalWebService.class);
				PlatformResponseResult wsResult = termianlWebService.batchAddTerminalInfo(list);
				if (wsResult == PlatformResponseResult.success) {
					entity.setStatus(PlatformResponseResult.success_VALUE);
				} else {
					entity.setStatus(PlatformResponseResult.failure_VALUE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				entity.setStatus(PlatformResponseResult.failure_VALUE);
			}
			for (LcTerminalInfoDBEntity lcTerminalInfoDBEntity : list) {
				// 组装同步数据
				if (lcTerminalInfoDBEntity.getTi_id() == null || lcTerminalInfoDBEntity.getTi_id() == 0) {
					entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.save);
				} else {
					entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.update);
				}
				entity.setId(SynchronousDataCache.updateMaxSynchronizationLogId());
				entity.setDistrict(lcTerminalInfoDBEntity.getDistrict() + "");
				entity.setModuleName(Constant.PropertiesKey.SynchronousDataParameters.TerminalWebService);
				entity.setOperationDate(System.currentTimeMillis() / 1000);
				entity.setOperationObject(ObjectUtils.objectToBytes(lcTerminalInfoDBEntity));
				entity.setType(Constant.PropertiesKey.DBTableName.LC_TERMINAL_INFO);
				entity.setWebserviceClass(TerminalWebService.class.getName());
				// 放缓存
				if (entity.getStatus() == PlatformResponseResult.failure_VALUE) {
					SynchronousDataCache.add(lcTerminalInfoDBEntity.getDistrict(), entity);
				}
				// 放数据库
				PlatformResponseResult result = synchronousManage
						.saveSynchronousData(entity);
				if (result == PlatformResponseResult.failure) {
					log.error("同步数据日志表存储失败");
				}
			}

		}

		return responseResult;
	}

	@Override
	public List<Integer> saveTerminalRegister(
			List<LcTerminalRegisterDBEntity> terminalRegisters) {
		List<Integer> list = new ArrayList<Integer>();
		for(LcTerminalRegisterDBEntity terminalRegister : terminalRegisters){
			OptResult or =terminalInfoManage.saveTerminalRegister(terminalRegister);
			if(PlatformResponseResult.success_VALUE != or.getStatus()){
				log.error("保存失败："+terminalRegister.toString());
				list.add(terminalRegister.getRegister_id());
			}
		}
		return list;
	}

	@Override
	public PlatformResponseResult terminalMappingBindOrNot(long mainTerminalId,
														   long secondaryTerminalId, boolean isBinding) {
		log.info("不应该调用这个方法，请检查");
		return null;
	}


}

package com.navinfo.opentsp.platform.da.core.webService.service.impl.district;

import com.navinfo.opentsp.platform.da.core.acceptor.procotol.MutualCommandFacotry;
import com.navinfo.opentsp.platform.da.core.acceptor.session.MutualSessionManage;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.*;
import com.navinfo.opentsp.platform.da.core.persistence.application.RegularDataAreaAndDataManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.TermianlDynamicManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.TerminalInfoManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.TerminalMappingManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.MediaFileModel;
import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.TerminalOnlineModel;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.*;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.webService.service.TerminalWebService;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.BusinessType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeType.NodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(endpointInterface = "com.navinfo.opentsp.platform.da.core.webService.service.TerminalWebService")
public class DistrictTerminalWebServiceImpl implements TerminalWebService {
	private static final Logger log = LoggerFactory.getLogger(DistrictTerminalWebServiceImpl.class);
	private static TerminalInfoManage terminalInfoManage = new TerminalInfoManageImpl();
	private static TermianlDynamicManage termianlDynamicManage =new TermianlDynamicManageImpl();
	private static RegularDataAreaAndDataManage  releAreaDataManager = new RegularDataAreaAndDataManageImpl();
	private TerminalMappingManage mappingManage = new TerminalMappingManageImpl();

	@Override
	public List<LcTerminalParaDBEntity> queryTerminalParaRes(long terminalId,
															 int command,int currentPage,int pageSize) {
		List<LcTerminalParaDBEntity> list = terminalInfoManage
				.queryTerminalParaRes(terminalId, command,currentPage,pageSize);
		return list;
	}

	@Override
	public PlatformResponseResult saveOrUpdateTerminalInfo(
			LcTerminalInfoDBEntity terminalInfo) {
		PlatformResponseResult responseResult = terminalInfoManage
				.saveOrUpdateTerminalInfo(terminalInfo);
		log.info("新增修改终端："+terminalInfo.toString());
		log.info("结果："+responseResult.getNumber());
		LCTerminalInfoSynchronous.TerminalInfoSynchronous.Builder builder=LCTerminalInfoSynchronous.TerminalInfoSynchronous.newBuilder();
		if (terminalInfo.getTi_id() == null || terminalInfo.getTi_id() == 0) {
			builder.setType(OperationType.add);
		} else {
			builder.setType(OperationType.modify);
		}
		LCTerminalInfo.TerminalInfo.Builder terminalbuiler=LCTerminalInfo.TerminalInfo.newBuilder();
		if(terminalInfo.getNode_code() != null){
			terminalbuiler.setNodeCode(terminalInfo.getNode_code().longValue());
		}
		terminalbuiler.setTerminalId(terminalInfo.getTerminal_id());
		terminalbuiler.setProtocolType(terminalInfo.getProto_code());
		terminalbuiler.setRegularInTerminal(terminalInfo.getRegular_in_terminal()==0?false:true);
		terminalbuiler.setDeviceId(terminalInfo.getDevice_id());//新增加的 deviceId

		if(terminalInfo.getTi_id() != null) {
			terminalbuiler.setChangeTid(terminalInfo.getTi_id());
		}
		terminalbuiler.setType(BusinessType.valueOf(terminalInfo.getBusiness_type()));
		builder.setInfo(terminalbuiler);
		List<Long> nodeCodes = MutualSessionManage.getInstance().getNodeListByNodeType(NodeType.mm);
		for (Long nodeCode : nodeCodes) {
			Packet pk = new Packet(true);
			pk.setProtocol(LCMessageType.PLATFORM);
			pk.setCommand(AllCommands.DataAccess.TerminalInfoSynchronous_VALUE);
			pk.setContent(builder.build().toByteArray());
			pk.setUniqueMark(NodeHelper.getNodeUniqueMark());
			pk.setTo(nodeCode);
			log.error("分区da-推送给MM终端信息："+terminalInfo.toString()+",操作类型："+builder.getType().name());
			MutualCommandFacotry.processor(pk);
		}
		return responseResult;
	}

	@Override
	public PlatformResponseResult saveOrUpdateTerminalInfo2(
			LcTerminalInfoDBEntity terminalInfo,boolean isAdd) {
		PlatformResponseResult responseResult = terminalInfoManage
				.saveOrUpdateTerminalInfo(terminalInfo,isAdd);
		log.info("新增修改终端："+terminalInfo.toString());
		log.info("结果："+responseResult.getNumber());
		if(0 == responseResult.getNumber()){
			return responseResult;
		}
		LCTerminalInfoSynchronous.TerminalInfoSynchronous.Builder builder=LCTerminalInfoSynchronous.TerminalInfoSynchronous.newBuilder();
		if (isAdd) {
			builder.setType(OperationType.add);
		} else {
			builder.setType(OperationType.modify);
		}
		LCTerminalInfo.TerminalInfo.Builder terminalbuiler=LCTerminalInfo.TerminalInfo.newBuilder();
		if(terminalInfo.getNode_code() != null){
			terminalbuiler.setNodeCode(terminalInfo.getNode_code().longValue());
		}
		terminalbuiler.setTerminalId(terminalInfo.getTerminal_id());
		terminalbuiler.setProtocolType(terminalInfo.getProto_code());
		terminalbuiler.setRegularInTerminal(terminalInfo.getRegular_in_terminal()==0?false:true);
		terminalbuiler.setDeviceId(terminalInfo.getDevice_id());//新增加的 deviceId

		if(terminalInfo.getTi_id() != null) {
			terminalbuiler.setChangeTid(terminalInfo.getTi_id());
		}
		terminalbuiler.setType(BusinessType.valueOf(terminalInfo.getBusiness_type()));
		builder.setInfo(terminalbuiler);
		List<Long> nodeCodes = MutualSessionManage.getInstance().getNodeListByNodeType(NodeType.mm);
		for (Long nodeCode : nodeCodes) {
			Packet pk = new Packet(true);
			pk.setProtocol(LCMessageType.PLATFORM);
			pk.setCommand(AllCommands.DataAccess.TerminalInfoSynchronous_VALUE);
			pk.setContent(builder.build().toByteArray());
			pk.setUniqueMark(NodeHelper.getNodeUniqueMark());
			pk.setTo(nodeCode);
			log.error("分区da-推送给MM终端信息："+terminalInfo.toString()+",操作类型："+builder.getType().name());
			MutualCommandFacotry.processor(pk);
		}
		return responseResult;
	}

	@Override
	public List<LcTerminalInfoDBEntity> queryTerminalInfoRes(long terminalId,
															 int protoType, String deviceId,long changeId,long start, long end,int flag,int currentPage,int pageSize) {
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
		LcTerminalInfoDBEntity terminalInfo = terminalInfoManage
				.queryTerminalInfo(primaryKey);
		return terminalInfo;
	}

	@Override
	public PlatformResponseResult deleteTerminalInfo(long[] primaryKeys) {
		PlatformResponseResult responseResult = terminalInfoManage
				.deleteTerminalInfo(primaryKeys);
		LCTerminalInfoSynchronous.TerminalInfoSynchronous.Builder builder=LCTerminalInfoSynchronous.TerminalInfoSynchronous.newBuilder();
		builder.setType(OperationType.delete);
		List<Long> nodeCodes = MutualSessionManage.getInstance().getNodeListByNodeType(NodeType.mm);
		for (Long nodeCode : nodeCodes) {
			for(long terminalId:primaryKeys){
				LCTerminalInfo.TerminalInfo.Builder terminalbuiler=LCTerminalInfo.TerminalInfo.newBuilder();

				terminalbuiler.setTerminalId(terminalId);
				builder.setInfo(terminalbuiler);
				Packet pk = new Packet(true);
				pk.setProtocol(LCMessageType.PLATFORM);
				pk.setCommand(AllCommands.DataAccess.TerminalInfoSynchronous_VALUE);
				pk.setContent(builder.build().toByteArray());
				pk.setUniqueMark(NodeHelper.getNodeUniqueMark());
				pk.setTo(nodeCode);
				MutualCommandFacotry.processor(pk);
				log.error("分区da-推送给MM终端信息："+terminalbuiler.toString()+",操作类型："+builder.getType().name());
			}
		}
		return responseResult;

	}

	@Override
	public LcTerminalRegisterDBEntity queryTerminalRegisterRes(long terminalId) {
		LcTerminalRegisterDBEntity entity = terminalInfoManage
				.queryTerminalRegister(terminalId);
		return entity;
	}

	@Override
	public List<LcTerminalRuleDBEntity> queryTerminalRuleRes(long terminalId,int currentPage,int pageSize) {
		List<LcTerminalRuleDBEntity> list = null;
		try {
			list = releAreaDataManager
					.queryTerminalRuleRes(terminalId,currentPage,pageSize);
		} catch (Exception e) {
			log.info(e.getMessage(),e);
		}
		return list;
	}

	@Override
	public TerminalOnlineModel queryTerminalOnLineRes(long terminalId) {
		TerminalOnlineModel model=terminalInfoManage.queryTerminalOnLineRes(terminalId);
		return model;
	}

	@Override
	public List<LcMultimediaParaDBEntity> queryMediaParaRes(long terminalId,
															long start, long end,int currentPage,int pageSize) {
		List<LcMultimediaParaDBEntity> list =terminalInfoManage.queryMediaParaRes(terminalId, start, end,currentPage,pageSize);
		return list;
	}

	@Override
	public MediaFileModel queryMediaFileRes(String fileCode, long terminalId) {
		MediaFileModel model=termianlDynamicManage.queryMediaFileRes(fileCode,terminalId);
		return model;
	}

	@Override
	public List<LcTerminalAreaDBEntity> queryTerminalAreaRes(long terminalId,
															 int areaId, long start, long end,int currentPage,int pageSize) {
		List<LcTerminalAreaDBEntity> list = null;;
		try {
			list = releAreaDataManager.queryTerminalAreaRes(terminalId, areaId, start, end,currentPage,pageSize);
		} catch (Exception e) {
			log.info(e.getMessage(),e);
		}
		return list;
	}

	@Override
	public List<LcTerminalAreaDataDBEntity> queryAreaDataRes(long terminal,
															 int taId) {
		List<LcTerminalAreaDataDBEntity> list = null;
		try {
			list = releAreaDataManager.queryAreaDataRes(terminal, taId);
		} catch (Exception e) {
			log.info(e.getMessage(),e);
		}
		return list;
	}

	@Override
	public PlatformResponseResult deleteTerminalInfoByTerminalId(long terminalId) {
		PlatformResponseResult responseResult = deleteTerminalInfo(new long[] { terminalId });
		return responseResult;
	}

	@Override
	public LcTerminalRuleDBEntity queryTerminalRuleById(long terminalId,
														int trId) {
		LcTerminalRuleDBEntity entity = null;
		try {
			entity = releAreaDataManager.queryTerminalRuleById(terminalId, trId);
			return entity;
		} catch (Exception e) {
			log.info(e.getMessage(),e);
			return entity;
		}
	}

	@Override
	public PlatformResponseResult batchAddTerminalInfo(
			List<LcTerminalInfoDBEntity> list) {
		PlatformResponseResult result = terminalInfoManage.batchAddTerminalInfo(list);
		return result;
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
		log.error("district terminalMappingBindOrNot(): mainTerminalId=" + mainTerminalId
				+ ",secondaryTerminalId=" +secondaryTerminalId+",isBinding="+isBinding);
		MySqlConnPoolUtil.startTransaction();
		PlatformResponseResult result = mappingManage.terminalMappingBindOrNot(mainTerminalId, secondaryTerminalId, isBinding);
		MySqlConnPoolUtil.commit();
		MySqlConnPoolUtil.close();
		log.info("district terminalMappingBindOrNot() return=" + result.getNumber());
		return result;
	}

}

package com.navinfo.opentsp.platform.da.core.webService.service.impl.district;

import com.navinfo.opentsp.platform.da.core.persistence.LogManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.LogManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.*;
import com.navinfo.opentsp.platform.da.core.webService.service.LogWebService;

import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "com.navinfo.opentsp.platform.da.core.webService.service.LogWebService")
public class DistrictLogWebServiceImpl implements LogWebService {
	private LogManage logManage=new LogManageImpl();
	@Override
	public List<LcTerminalSwitchLogDBEntity> queryTerminalSwitchLogList(
			long terminalId, int switchType, long start, long end) {
		List<LcTerminalSwitchLogDBEntity> list=logManage.queryTerminalSwitchLogList(terminalId, switchType, start, end);		
		return list;
	}

	@Override
	public List<LcTerminalRuleLogDBEntity> queryTerminalRuleLogList(
			long terminalId, int ruleCode, long start, long end) {
		List<LcTerminalRuleLogDBEntity> list=logManage.queryTerminalRuleLogList(terminalId, ruleCode, start, end);		
		return list;
	}

	@Override
	public List<LcTerminalAreaLogDBEntity> queryTerminalAreaLogList(
			long terminalId, int originalAreaId, long start, long end) {
		List<LcTerminalAreaLogDBEntity> list=logManage.queryTerminalAreaLogList(terminalId, originalAreaId, start, end);		
		return list;
	}

	@Override
	public List<LcTerminalAreaDataLogDBEntity> queryTerminalAreaDataList(
			long terminalId, int taId) {
		List<LcTerminalAreaDataLogDBEntity> list=logManage.queryTerminalAreaDataList(terminalId, taId);		
		return list;
	}
	@Override
	public int queryServiceLogCount(long start, long end,
			int logDistrict, String logIp, int typeCode) {
		int count=logManage.queryServiceLogCount(start, end, logDistrict, logIp, typeCode);		
		return count;
	}
	@Override
	public List<LcServiceLogDBEntity> queryServiceLogList(long start, long end,
			int logDistrict, String logIp, int typeCode,int currentPage,int pageSize) {
		List<LcServiceLogDBEntity> list=logManage.queryServiceLogList(start, end, logDistrict, logIp, typeCode,currentPage,pageSize);		
		return list;
	}
	@Override
	public int queryNodeLogCount(int nlId, long start,
			long end, int logDistrict) {
		int count=logManage.queryNodeLogCount(nlId, start, end, logDistrict);		
		return count;
	}
	@Override
	public List<LcNodeLogDBEntity> queryNodeLogList(int nlId, long start,
			long end, int logDistrict,int currentPage,int pageSize) {
		List<LcNodeLogDBEntity> list=logManage.queryNodeLogList(nlId, start, end, logDistrict,currentPage,pageSize);		
		return list;
	}

	@Override
	public List<LcTerminalOperationLogDBEntity> queryTerminalOperationLogList(
			long terminalId, int operatinoType, long start, long end,int currentPage,int pageSize) {
		List<LcTerminalOperationLogDBEntity> list=logManage.queryTerminalOperationLogList(terminalId, operatinoType, start, end,currentPage,pageSize);		
		return list;
	}

	@Override
	public LcTerminalRuleLogDBEntity queryTerminalRuleLogById(int ruleId,
			long terminalId) {
		LcTerminalRuleLogDBEntity lcTerminalRuleLogDBEntity=logManage.queryTerminalRuleLogById(ruleId, terminalId);		
		return lcTerminalRuleLogDBEntity;
	}

	@Override
	public LcTerminalOperationLogDBEntity queryTerminalOperationLogById(
			int tol_id,long terminalId) {
		LcTerminalOperationLogDBEntity lcTerminalOperationLogDBEntity=logManage.queryTerminalOperationLogById(tol_id, terminalId);		
		return lcTerminalOperationLogDBEntity;
	}

	@Override
	public List<LcMessageBroadcastLogDBEntity> queryMessageBroadcastLogList(
			long terminalId, int areaId, long start, long end,int currentPage,int pageSize) {
		List<LcMessageBroadcastLogDBEntity> entities = logManage.queryMessageBroadcastLogList(terminalId, areaId, start, end,currentPage,pageSize);
		return entities;
	}

	@Override
	public List<LcOutRegionLimitSpeedLogDBEntity> queryOutRegionLimitSpeedLogList(
			long terminalId, int areaId, long start, long end,int currentPage,int pageSize) {
		List<LcOutRegionLimitSpeedLogDBEntity> entities = logManage.queryOutRegionLimitSpeedLogList(terminalId, areaId, start, end,currentPage,pageSize);
		return entities;
	}

}

package com.lc.rp.webService.service.impl.center;

import com.lc.rp.webService.service.SynchronousTerminalDataWebService;
import com.navinfo.opentsp.platform.rpws.core.configuration.RMIConnctorManager;
import com.navinfo.opentsp.platform.rpws.core.service.TerminalUpdateNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import java.util.Arrays;
import java.util.Map;

/*
 * 终端数据同步以及登陆校验 
 * @author Administrator
 *
 */
@WebService(endpointInterface = "com.lc.rp.webService.service.SynchronousTerminalDataWebService", portName = "TerminalPort", serviceName="TerminalWS")
public class SynchronousTerminalDataWebServiceImpl implements SynchronousTerminalDataWebService{
	private Logger logger = LoggerFactory.getLogger(SynchronousTerminalDataWebServiceImpl.class);

	@Override
	public String DataSynchronizeAuth(String authName, String password) {
		try {
			logger.error("DataSynchronizeAuth,authName="+authName+",password="+password);
			String dynamicPwd = RMIConnctorManager.getInstance().getSynchronousTermianlDataService().DataSynchronizeAuth(authName, password);
			logger.error("DataSynchronizeAuth() return dynamicPwd="+dynamicPwd);
			return dynamicPwd;
		} catch (Exception e) {
			logger.error("获取动态口令错误",e);
		}
		return null;
	}

	@Override
	public int TerminalAddOrUpdate(long terminalId, int protoCode,
								   int districtCode, boolean regularInTerminal,String deviceId,int businessType,boolean isAdd ,String dynamicPassword){
		try {
			logger.error("TerminalAddOrUpdate()="+isAdd+": terminalId="+terminalId+",protoCode="+protoCode+",districtCode="+districtCode+",regularInTerminal="
					+regularInTerminal+",deviceId="+deviceId +",businessType="+businessType+",dynamicPassword="+dynamicPassword);
//			int result = RMIConnctorManager.getInstance().getSynchronousTermianlDataService().TerminalAddOrUpdate(terminalId, protoCode, districtCode, regularInTerminal,deviceId,businessType,isAdd, dynamicPassword);

			int result = 0;
			// 添加终端信息变更通知流程 调用DA新的rmi接口SynchronousTermianlDataService.RmiTerminalAddOrUpdate()可获得DA添加成功的终端信息
			Map<String,Object> terminalInfo = RMIConnctorManager.getInstance().getSynchronousTermianlDataService().RmiTerminalAddOrUpdate(terminalId, protoCode, districtCode, regularInTerminal,deviceId,businessType,isAdd, dynamicPassword);
			// 调用DA的RMI接口进行终端添加或更新返回成功
			if(terminalInfo != null)
			{
				// 调用push Command 进行终端信息同步
				TerminalUpdateNoticeService.getInstance().terminalAddOrUpdateSynPush(String.valueOf(terminalId), protoCode, deviceId);
				// 调用dp的终端存储接口进行终端信息同步
				TerminalUpdateNoticeService.getInstance().terminalAddOrUpdateSynDP(terminalInfo);
				result = 1;
			}

			logger.error("TerminalAddOrUpdate() return="+result);
			return result;
		} catch (Exception e) {
			logger.error("添加终端错误",e);
		}
		return 0;
	}

	@Override
	public int TerminalDelete(long[] terminalId, String dynamicPassword){
		try {
			logger.error("TerminalDelete(): size="+terminalId.length+",terminals="+Arrays.toString(terminalId)+",dynamicPassword= "+dynamicPassword);
			int result  = RMIConnctorManager.getInstance().getSynchronousTermianlDataService().TerminalDelete(terminalId, dynamicPassword);
			// 调用DA的RMI接口进行终端删除返回成功
			if(result != 0)
			{
				// 调用push Command 进行终端信息同步
				TerminalUpdateNoticeService.getInstance().terminalDeleteSynPush(terminalId);
				// 调用dp的终端删除接口进行终端数据同步
				TerminalUpdateNoticeService.getInstance().terminalDeleteSynDP(terminalId);
			}
			logger.error("TerminalDelete() return="+result);
			return  result;
		} catch (Exception e) {
			logger.error("批量删除终端错误",e);
		}
		return 0;
	}

	@Override
	public int TerminalMappingBindOrNot(long mainTerminalId,
										long secondaryTerminalId, boolean isBinding, String dynamicPassword) {
		try {
			logger.error("TerminalMappingBindOrNot(): mainTerminalId="+mainTerminalId+",secondaryTerminalId="+secondaryTerminalId+",isBinding="+isBinding+",dynamicPassword= "+dynamicPassword);
			int result  = RMIConnctorManager.getInstance().getSynchronousTermianlDataService().TerminalMappingBindOrNot(mainTerminalId, secondaryTerminalId, isBinding, dynamicPassword);
			logger.error("TerminalMappingBindOrNot() return="+result);
			return  result;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return 0;
	}
}
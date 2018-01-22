package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.synchronousdata;

import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.synchronousdata.CustomException.DynamicPasswordException;
import java.util.Map;


public interface SynchronousTerminalDataService {
	
	/**
	 * 登录校验
	 * @param authName （用户名）
	 * @param password（密码）
	 * @return
	 */
	public String DataSynchronizeAuth(String authName, String password) ;
	/**
	 * 新增或修改终端
	 * @param terminalId （终端标识）
	 * @param protoCode（协议类型编码）
	 * @param districtCode（服务分区编码）
	 * @param regularInTerminal（
	 * 			是否在终端处理规则（区域和路线相关）true在终端处理；false在平台）
	 * @param deviceId 设备id
	 * @param businessType 0默认，1江淮
	 * @param isAdd     	true新增；false修改
	 * @param dynamicPassword（WS请求动态口令）
	 * @return
	 */
	public int TerminalAddOrUpdate(long terminalId, int protoCode, int districtCode, boolean regularInTerminal, String deviceId, int businessType, boolean isAdd, String dynamicPassword) throws DynamicPasswordException;

	/**
	 * 新增或修改终端-rmi调用
	 * @param terminalId （终端标识）
	 * @param protoCode（协议类型编码）
	 * @param districtCode（服务分区编码）
	 * @param regularInTerminal（
	 * 			是否在终端处理规则（区域和路线相关）true在终端处理；false在平台）
	 * @param deviceId 设备id
	 * @param businessType 0默认，1江淮
	 * @param isAdd     	true新增；false修改
	 * @param dynamicPassword（WS请求动态口令）
	 * @return
	 */
	public Map<String,Object> RmiTerminalAddOrUpdate(long terminalId, int protoCode, int districtCode, boolean regularInTerminal, String deviceId, int businessType, boolean isAdd, String dynamicPassword)  throws DynamicPasswordException;
	/**
	 * 删除终端
	 * @param terminalId （终端标识）
	 * @param dynamicPassword （WS请求动态口令）
	 * @return
	 */
	public int TerminalDelete(long[] terminalId, String dynamicPassword)  throws DynamicPasswordException;
	
	/**
	 * 防拆盒和北斗终端映射关系
	 * @param mainTerminalId
	 * @param secondaryTerminalId
	 * @param isBinding
	 * @param dynamicPassword
	 * @return
	 * @
	 */
	public int TerminalMappingBindOrNot(long mainTerminalId, long secondaryTerminalId, boolean isBinding, String dynamicPassword) ;
}

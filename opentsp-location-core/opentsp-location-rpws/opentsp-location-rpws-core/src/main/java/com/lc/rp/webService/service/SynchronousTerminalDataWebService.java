package com.lc.rp.webService.service;

import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;

/**
 *
 * 总部ws数据同步接口
 *
 */

@WebService
public interface SynchronousTerminalDataWebService {

	/**
	 * 登录校验
	 * @param authName （用户名）
	 * @param password（密码）
	 * @return
	 */
	public String DataSynchronizeAuth(
			@WebParam(name = "authName", mode = Mode.IN) String authName,
			@WebParam(name = "password", mode = Mode.IN) String password);
	/**
	 * 新增或修改终端
	 * @param terminalId （终端标识）
	 * @param protoCode（协议类型编码）
	 * @param districtCode（服务分区编码）
	 * @param regularInTerminal（
	 * 			是否在终端处理规则（区域和路线相关）true在终端处理；false在平台）
	 * @param dynamicPassword（WS请求动态口令）
	 * @return
	 */
	public int TerminalAddOrUpdate(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
			@WebParam(name = "protoCode", mode = Mode.IN) int protoCode,
			@WebParam(name = "districtCode", mode = Mode.IN) int districtCode,
			@WebParam(name = "regularInTerminal", mode = Mode.IN) boolean regularInTerminal,
			@WebParam(name = "deviceId", mode = Mode.IN) String deviceId,
			@WebParam(name = "businessType", mode = Mode.IN) int businessType,
			@WebParam(name = "isAdd", mode = Mode.IN) boolean isAdd,
			@WebParam(name = "dynamicPassword", mode = Mode.IN) String dynamicPassword);
	/**
	 * 删除终端
	 * @param terminalId （终端标识）
	 * @param dynamicPassword （WS请求动态口令）
	 * @return
	 */
	public int TerminalDelete(
			@WebParam(name = "terminalId", mode = Mode.IN) long[] terminalId,
			@WebParam(name = "dynamicPassword", mode = Mode.IN) String dynamicPassword);

	/**
	 * 2.3	防拆盒和北斗终端映射关系
	 * @param mainTerminalId 主终端标识（防拆盒）
	 * @param secondaryTerminalId 副终端标识（北斗部标终端）
	 * @param isBinding 是否绑定：true：绑定；false：取消绑定
	 * @param dynamicPassword
	 * @return
	 */
	public int TerminalMappingBindOrNot(
			@WebParam(name = "mainTerminalId", mode = Mode.IN) long mainTerminalId,
			@WebParam(name = "secondaryTerminalId", mode = Mode.IN) long secondaryTerminalId,
			@WebParam(name = "isBinding", mode = Mode.IN) boolean isBinding,
			@WebParam(name = "dynamicPassword", mode = Mode.IN) String dynamicPassword);

}

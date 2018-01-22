package com.navinfo.opentsp.platform.da.core.persistence;

import java.util.List;
import java.util.Map;


public interface UpperServiceManage {
	/**
	 * 用户鉴权。 根据用户名获取用户信息，验证密码，服务区域，IP地址，验证通过后，在redis中维护服务标识信息，并把服务标识信息返回
	 *
	 * @param name
	 *            {@link String} 用户名
	 * @param password
	 *            {@link String}密码
	 * @param version
	 *            {@link String}版本
	 * @param districtCode
	 *            {@link Integer}服务区域
	 * @param serverIp
	 *            {@link String}服务ip
	 * @return 应答实体==>{@link OptResult}。OptResult的map变量的 key: serverIdentifies==〉服务标识，类型{@link Long}<br>
	 *         status==>{@link Integer} 状态：0x01--请求成功；0x03--分区错误；0x04--非法服务 IP地址；0x05--用户名错误；0x06--密码错误
	 */
	public OptResult authentication(String name, String password, String version,
									int districtCode, String serverIp);


	/**
	 * 服务状态通知
	 * 根据服务状态来操作服务标识，服务状态为断开时，修改redis中服务标识的状态为false,为注销时，删除redis中服务标识
	 * @param serverIp {@link String} 服务 IP地址
	 * @param serverIdentifies {@link Long} 服务标识
	 * @param status {@link Integer} 服务状态, 0x01:断开;0x02:注销.
	 * @return 应答实体==》 {@link OptResult}。OptResult的 status属性==>{@link Integer} 状态：1:操作成功；0:操作失败
	 */
	public OptResult serverStatusNotice(String serverIp, long serverIdentifies,
										int status);
	/**
	 * 从鉴权
	 * @param mainServerIdentify 主服务标识
	 * @param serverIp 从ip
	 * @return Map<Long,String> key：标识，value：ip
	 */
	public Map<Long,String> multiServerAuth(long mainServerIdentify,List<String> serverIp);
}

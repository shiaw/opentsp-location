package com.navinfo.opentsp.platform.da.core.persistence.application;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.UpperServiceImp;
import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.OptResult;
import com.navinfo.opentsp.platform.da.core.persistence.UpperServiceManage;
import com.navinfo.opentsp.platform.da.core.persistence.common.ServiceMarkFactory;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceDistrictConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcServiceConfigDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcServiceDistrictConfigDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcServiceConfigDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcServiceDistrictConfigDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.entity.ServiceUniqueMark;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.IUpperService;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerStatus;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UpperServiceManageImpl implements UpperServiceManage {
	private static final long EFFECTIVETIME = 60 * 5l;
	private static Logger logger = LoggerFactory.getLogger(UpperServiceManageImpl.class);
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
	 * @return 应答实体 {@link OptResult}。OptResult的map变量的 key:
	 *         serverIdentifies==〉服务标识，类型{@link Long}<br>
	 *         status==>{@link Integer} 状态：0x01--请求成功；0x03--分区错误；0x04--非法服务
	 *         IP地址；0x05--用户名错误；0x06--密码错误
	 */
	@Override
	public
	OptResult authentication(String name, String password,
			String version, int districtCode, String serverIp) {
		try {
			MySqlConnPoolUtil.startTransaction();
			OptResult optResult = new OptResult();
			// 服务配置
			LcServiceConfigDao lcServiceConfigDao = new LcServiceConfigDaoImpl();
			// 服务所在分区
			LcServiceDistrictConfigDao lcServiceDistrictConfigDao = new LcServiceDistrictConfigDaoImpl();
			LcServiceConfigDBEntity serviceConfig = lcServiceConfigDao
					.getServiceConfig(name);
			// 验证用户名、密码、ip
			if (serviceConfig == null) {
				// 用户名错误
				optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.nameNotExist_VALUE);
				return optResult;
			} else {
				if (!serviceConfig.getPassword().equals(password)) {
					// 密码错误
					optResult
							.setStatus(LCPlatformResponseResult.PlatformResponseResult.passwordError_VALUE);
					return optResult;
				}
				if (!serviceConfig.getIp_address().equals(serverIp)) {
					// ip错误
					optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.illegalIp_VALUE);
					return optResult;
				}
				// 验证服务所在区域
				List<LcServiceDistrictConfigDBEntity> serviceDistrict = lcServiceDistrictConfigDao
						.getByScId(serviceConfig.getSc_id());
				// 验证区域编码
				if (CollectionUtils.isEmpty(serviceDistrict)) {
					// 分区错误
					optResult
							.setStatus(LCPlatformResponseResult.PlatformResponseResult.districtError_VALUE);
					return optResult;
				} else {

					if (isDistrict(serviceDistrict, districtCode)) {
						// redis中存放服务标识信息，并返回信息
						optResult
								.setStatus(LCPlatformResponseResult.PlatformResponseResult.success_VALUE);
						// optResult中存放信息
						IUpperService upperService = new UpperServiceImp();
						ServiceUniqueMark serviceUniqueMark = new ServiceUniqueMark();
						serviceUniqueMark.setDistrictCode(districtCode);
						serviceUniqueMark.setIp(serverIp);
						serviceUniqueMark
								.setStatus(LCServerStatus.ServerStatus.disconnect_VALUE);
						serviceUniqueMark.setUniqueMark(ServiceMarkFactory
								.getServiceMark());
						serviceUniqueMark.setActionTime(System
								.currentTimeMillis() / 1000);
						serviceUniqueMark.setAuthTime(System
								.currentTimeMillis() / 1000);
						// 1代表主标识，0代表从标识
						serviceUniqueMark.setType(1);
						serviceUniqueMark
								.setChildren(new ConcurrentHashMap<Long, String>());
						boolean flag = upperService.saveServiceUniqueMark(serviceUniqueMark);
						logger.info("save SERVICE_UNIQUEMARK:"+serviceUniqueMark+" result:"+flag);
						if(flag){
							//redis保存成功
							Map<String, Object> map = new ConcurrentHashMap<String, Object>();
							map.put(Constant.PropertiesKey.UpperServiceManageConstant.SERVERIDENTIFIES,
									serviceUniqueMark.getUniqueMark());
							optResult.setMap(map);
						}else {
							//redis保存失败
							optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.failure_VALUE);
						}
						return optResult;
					} else {
						optResult
								.setStatus(LCPlatformResponseResult.PlatformResponseResult.districtError_VALUE);
						return optResult;
					}
				}
			}
		} catch (Exception e) {
			logger.error("用户鉴权发生异常",e);
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	private boolean isDistrict(
			List<LcServiceDistrictConfigDBEntity> serviceDistrict,
			int districtCode) {
		for (LcServiceDistrictConfigDBEntity entity : serviceDistrict) {
			if (entity.getService_district() == districtCode) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 登录 用户根据服务标识来登陆,如果服务标识验证成功，则修改redis中服务标识状态为true,如果登陆不成功，
	 *
	 * @param serverIdentifies
	 *            {@link Long} 服务标识
	 * @return 应答实体==》 {@link OptResult}。OptResult的 status属性==>{@link Integer}
	 *         状态：0x01--登陆成功；0x03--标识错误；0x02--响应超时
	 */
	public OptResult serverLogin(String serverIp, long serverIdentifies) {
		// 根据服务标识从redis中查找，如果验证通过，就修改redis中的状态，返回最新的服务标识信息，如果没有就返回空
		OptResult optResult = new OptResult();
		IUpperService upperService = new UpperServiceImp();
		ServiceUniqueMark serviceUniqueMark = upperService
				.findServiceUniqueMark(serverIdentifies);
		if (serviceUniqueMark != null) {
			if (serverIp.equals(serviceUniqueMark.getIp())) {
				optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.success_VALUE);
				// 修改redis中服务标识的状态
				serviceUniqueMark.setStatus(LCServerStatus.ServerStatus.login_VALUE);
				serviceUniqueMark
						.setActionTime(System.currentTimeMillis() / 1000);
				serviceUniqueMark
						.setLoginTime(System.currentTimeMillis() / 1000);
				logger.info("登录成功，更新服务标识"+serviceUniqueMark.getIp()+" "+serviceUniqueMark.getUniqueMark());
				upperService.updateServiceUniqueMark(serviceUniqueMark);
			} else {
				logger.info("reids中记录的IP："+serviceUniqueMark.getIp()+" 不同于参数："+serverIp);
				// ip没有通过验证
				optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.illegalIp_VALUE);
			}
		} else {
			logger.info("redis中没有找到SERVICE_UNIQUEMARK的key为："+serverIdentifies+"的对象。");
			optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.serverIdentifyTimeOut_VALUE);
		}
		return optResult;
	}

	/**
	 * 服务状态通知 根据服务状态来操作服务标识，服务状态为断开时，修改redis中服务标识的状态为false,为注销时，删除redis中服务标识
	 *
	 * @param serverIp
	 *            {@link String} 服务 IP地址
	 * @param serverIdentifies
	 *            {@link Long} 服务标识
	 * @param status
	 *            {@link Integer} 服务状态, 0x03:断开;0x04:注销.
	 * @return 应答实体==》 {@link OptResult}。OptResult的 status属性==>{@link Integer}
	 *         状态：1:操作成功；0:操作失败
	 */
	@Override
	public OptResult serverStatusNotice(String serverIp, long serverIdentifies,
			int status) {
		// 首先根据服务标识验证，然后验证serverip,然后根据status做操作，不同的操作返回不同的结果
		logger.info("收到鉴权："+serverIp+" 鉴权码："+serverIdentifies+" 状态："+status);
		OptResult optResult = new OptResult();
		// 缺少重连和登录操作
		switch (status) {
		case LCServerStatus.ServerStatus.disconnect_VALUE:// 断开
			optResult = disconnect(serverIp, serverIdentifies);
			break;
		case LCServerStatus.ServerStatus.logout_VALUE:// 注销
			optResult = logout(serverIp, serverIdentifies);
			break;
		case LCServerStatus.ServerStatus.login_VALUE:// 登录
			optResult = serverLogin(serverIp, serverIdentifies);
			break;
		case LCServerStatus.ServerStatus.reconnect_VALUE:// 重连
			optResult = reconnect(serverIp, serverIdentifies);
			break;
		default:
			optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.failure_VALUE);
		}
		return optResult;
	}

	/**
	 * 断开
	 *
	 * @param serverIp
	 *            {@link String} 服务 IP地址
	 * @param serverIdentifies
	 *            {@link Long} 服务标识
	 * @return 应答实体==》 {@link OptResult}。OptResult的 status属性==>{@link Integer}
	 *         状态：1:操作成功；0:操作失败
	 */
	private OptResult disconnect(String serverIp, long serverIdentifies) {
		OptResult optResult = new OptResult();
		IUpperService upperService = new UpperServiceImp();
		ServiceUniqueMark serviceUniqueMark = upperService
				.findServiceUniqueMark(serverIdentifies);
		if (serviceUniqueMark != null) {
			// 验证ip
			if (serverIp.equals(serviceUniqueMark.getIp())) {
				serviceUniqueMark.setStatus(LCServerStatus.ServerStatus.disconnect_VALUE);
				serviceUniqueMark
						.setActionTime(System.currentTimeMillis() / 1000);
				serviceUniqueMark
						.setDisconnectTime(System.currentTimeMillis() / 1000);
				upperService.updateServiceUniqueMark(serviceUniqueMark);
				optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.success_VALUE);
				// 判断有没有从服务标识，如果有则断开
				Map<Long, String> children = serviceUniqueMark.getChildren();
				if (children != null && !children.isEmpty()) {
					Set<Map.Entry<Long, String>> set = children.entrySet();
					for (Iterator<Map.Entry<Long, String>> it = set.iterator(); it
							.hasNext();) {
						Map.Entry<Long, String> entry = (Map.Entry<Long, String>) it
								.next();
						// 递归调用
						disconnect(entry.getValue(), entry.getKey());
					}

				}
			} else {
				// ip没有通过验证
				optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.illegalIp_VALUE);
				logger.error("业务系统连接断开通知的ip没有通过验证"+serverIdentifies+" "+serverIp);
			}
		} else {
			// 标识不存在
			optResult
					.setStatus(LCPlatformResponseResult.PlatformResponseResult.serverIdentifyTimeOut_VALUE);
			logger.error("业务系统连接断开通知的标识不存在"+serverIdentifies+" "+serverIp);
		}

		return optResult;
	}

	/**
	 * 重连
	 *
	 * @param serverIp
	 *            {@link String} 服务 IP地址
	 * @param serverIdentifies
	 *            {@link Long} 服务标识
	 * @return 应答实体==》 {@link OptResult}。OptResult的 status属性==>{@link Integer}
	 *         状态：1:操作成功；0:操作失败
	 */
	private OptResult reconnect(String serverIp, long serverIdentifies) {
		OptResult optResult = new OptResult();
		IUpperService upperService = new UpperServiceImp();
		ServiceUniqueMark serviceUniqueMark = upperService
				.findServiceUniqueMark(serverIdentifies);
		if (serviceUniqueMark != null) {
			// 验证ip
			if (serverIp.equals(serviceUniqueMark.getIp())) {
				// 验证服务状态
				//switch (serviceUniqueMark.getStatus()) {
				//case ServerStatus.disconnect_VALUE:// 断开
					if (System.currentTimeMillis() / 1000
							- serviceUniqueMark.getAuthTime() < EFFECTIVETIME) {
						// 验证是否是过期
						serviceUniqueMark.setStatus(LCServerStatus.ServerStatus.login_VALUE);
						serviceUniqueMark.setActionTime(System
								.currentTimeMillis() / 1000);// 修改活动时间
						serviceUniqueMark.setReloginTime(System
								.currentTimeMillis() / 1000);// 修改重连时间
						upperService.updateServiceUniqueMark(serviceUniqueMark);
						optResult
								.setStatus(LCPlatformResponseResult.PlatformResponseResult.success_VALUE);
					} else {
						// 过期处理
						optResult
								.setStatus(LCPlatformResponseResult.PlatformResponseResult.serverIdentifyTimeOut_VALUE);
					}
				//	break;
				//}
			} else {
				// ip没有通过验证
				optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.failure_VALUE);
			}
		} else {
			// 标识不存在
			optResult
					.setStatus(LCPlatformResponseResult.PlatformResponseResult.serverIdentifyTimeOut_VALUE);
		}

		return optResult;
	}

	/**
	 * 注销
	 *
	 * @param serverIp
	 *            {@link String} 服务 IP地址
	 * @param serverIdentifies
	 *            {@link Long} 服务标识
	 * @return 应答实体==》 {@link OptResult}。OptResult的 status属性==>{@link Integer}
	 *         状态：1:操作成功；0:操作失败
	 */
	private OptResult logout(String serverIp, long serverIdentifies) {
		OptResult optResult = new OptResult();
		IUpperService upperService = new UpperServiceImp();
		ServiceUniqueMark serviceUniqueMark = upperService
				.findServiceUniqueMark(serverIdentifies);
		if (serviceUniqueMark != null) {
			// 验证ip
			if (serverIp.equals(serviceUniqueMark.getIp())) {
				upperService.delServiceUniqueMark(serverIdentifies);
				optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.success_VALUE);
				// 判断有没有从服务标识，如果有则注销
				Map<Long, String> children = serviceUniqueMark.getChildren();
				if (children != null && !children.isEmpty()) {
					Set<Map.Entry<Long, String>> set = children.entrySet();
					for (Iterator<Map.Entry<Long, String>> it = set.iterator(); it
							.hasNext();) {
						Map.Entry<Long, String> entry = (Map.Entry<Long, String>) it
								.next();
						// 递归调用
						logout(entry.getValue(), entry.getKey());
					}
				}
			} else {
				// ip没有通过验证
				optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.failure_VALUE);
			}
		} else {
			// 标识不存在
			optResult
					.setStatus(LCPlatformResponseResult.PlatformResponseResult.serverIdentifyTimeOut_VALUE);
		}

		return optResult;
	}

	@Override
	public Map<Long, String> multiServerAuth(long mainServerIdentify,
			List<String> serverIp) {
		Map<Long, String> result = new ConcurrentHashMap<Long, String>();
		// 生成从服务标识
		IUpperService upperService = new UpperServiceImp();
		ServiceUniqueMark mainUniqueMark = upperService
				.findServiceUniqueMark(mainServerIdentify);
		for (String ip : serverIp) {
			ServiceUniqueMark uniqueMark = new ServiceUniqueMark();
			uniqueMark.setDistrictCode(mainUniqueMark.getDistrictCode());// 分区
			uniqueMark.setIp(ip);// ip
			uniqueMark.setStatus(LCServerStatus.ServerStatus.disconnect_VALUE);// 状态
			uniqueMark.setUniqueMark(ServiceMarkFactory.getServiceMark());// 服务标识
			uniqueMark.setActionTime(System.currentTimeMillis() / 1000);// 活动时间
			uniqueMark.setAuthTime(System.currentTimeMillis() / 1000);// 鉴权时间
			uniqueMark.setDisconnectTime(System.currentTimeMillis()/1000);
			// 1代表主标识，0代表从标识
			uniqueMark.setType(0);
			upperService.saveServiceUniqueMark(uniqueMark);
			// 在主标识中维护从标识
			mainUniqueMark.getChildren().put(uniqueMark.getUniqueMark(), ip);
			result.put(uniqueMark.getUniqueMark(), ip);
		}
		upperService.saveServiceUniqueMark(mainUniqueMark);

		return result;
	}

}

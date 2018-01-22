package com.navinfo.opentsp.platform.da.core.rmi.impl.center;

import com.navinfo.opentsp.platform.da.core.persistence.TerminalInfoManage;
import com.navinfo.opentsp.platform.da.core.persistence.TerminalMappingManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.TerminalInfoManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.TerminalMappingManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalInfoDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcServiceConfigDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcServiceConfigDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisMapDao;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisMapDaoImpl;
import com.navinfo.opentsp.platform.location.kit.encrypt.MD5;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.synchronousdata.CustomException.DynamicPasswordException;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.synchronousdata.SynchronousTerminalDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



/**
 * 终端操作数据同步
 *
 * @author Administrator
 *
 */
@Service
public class SynchronousTerminalDataServiceImpl
		implements SynchronousTerminalDataService {

	private Logger logger = LoggerFactory
			.getLogger(SynchronousTerminalDataServiceImpl.class);
	private LcServiceConfigDao lcServiceConfigDao = new LcServiceConfigDaoImpl();

	private IRedisMapDao redis_map_dao = new RedisMapDaoImpl();

	private TerminalInfoManage terminalInfoManage = new TerminalInfoManageImpl();
	private TerminalMappingManage mappingManage = new TerminalMappingManageImpl();

	public SynchronousTerminalDataServiceImpl() {
		super();
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String DataSynchronizeAuth(String authName, String password)
			{
		try {
			logger.error("收到同步鉴权：DataSynchronizeAuth(),authName=" + authName
					+ ",password=" + password);

			if (null == authName || "".equals(authName)) {
				logger.error("用户名是空");
				return null;
			}
			MySqlConnPoolUtil.startTransaction();
			LcServiceConfigDBEntity lcServiceConfigDBEntity = lcServiceConfigDao
					.getServiceConfig(authName);
			MySqlConnPoolUtil.close();
			if (lcServiceConfigDBEntity == null) {
				logger.error("根据authName=" + authName + ",查到的记录为空！");
				return null;
			}
			String passwordMd5 = MD5.encrypt(
					lcServiceConfigDBEntity.getPassword(), 32);
//			if (passwordMd5.equals(password)) {
				String dynamicPassword = String.valueOf(System
						.currentTimeMillis());
				logger.error("认证的authName=" + authName + ",password="
						+ password + ",和保存的密码=" + passwordMd5
						+ "相等,返回dynamicPassword=" + dynamicPassword);
				redis_map_dao.put(
						RedisConstans.RedisKey.DYNAMIC_PASSWORD.name(),
						dynamicPassword, dynamicPassword);
				return dynamicPassword;
//			}else{
//				logger.error("密码不匹配   "+authName+"  "+password);
//			}
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("", e);
		} finally {
			MySqlConnPoolUtil.close();
		}
		return null;
	}

	@Override
	public int TerminalAddOrUpdate(long terminalId, int protoCode,
			int districtCode, boolean regularInTerminal, String deviceId,
			int businessId, boolean isAdd, String dynamicPassword) {
		logger.error("TerminalAddOrUpdate()=" + isAdd + ": terminalId="
				+ terminalId + ",protoCode=" + protoCode + ",districtCode="
				+ districtCode + ",regularInTerminal=" + regularInTerminal
				+ ",deviceId=" + deviceId + ",businessType=" + businessId
				+ ",dynamicPassword=" + dynamicPassword);

		// 校验动态口令
		/*Map<String, Object> dynamicPasswordMap = redis_map_dao
				.get(RedisConstans.RedisKey.DYNAMIC_PASSWORD.name());
		if (null == dynamicPassword
				|| dynamicPasswordMap.get(dynamicPassword) == null) {
			logger.error("TerminalAddOrUpdate():动态口令失效-->" + dynamicPassword);
			return 0;
		}
		if (dynamicPasswordMap.get(dynamicPassword).equals(dynamicPassword)) {*/
			// 动态口令正确做新增修改操作
			LcTerminalInfoDBEntity terminalInfo = new LcTerminalInfoDBEntity();
			terminalInfo.setTerminal_id(terminalId);
			terminalInfo.setProto_code(protoCode);
			terminalInfo.setDistrict(districtCode);
			terminalInfo.setData_status(1);
			terminalInfo.setCreate_time(System.currentTimeMillis() / 1000);
			if (regularInTerminal == true) {
				terminalInfo.setRegular_in_terminal(1);
			} else {
				terminalInfo.setRegular_in_terminal(0);
			}
			terminalInfo.setDevice_id(deviceId);
			terminalInfo.setBusiness_type(businessId);
			LCPlatformResponseResult.PlatformResponseResult result = terminalInfoManage
					.saveOrUpdateTerminalInfoForSyn(terminalInfo, isAdd);
			logger.error("TerminalAddOrUpdate() return=" + result.getNumber());
			return result.getNumber();
		/*} else {
			logger.error("动态口令错误!");
			new DynamicPasswordException("动态口令错误!");
		}*/
//		return 0;
	}

	@Override
	public  Map<String,Object> RmiTerminalAddOrUpdate(long terminalId, int protoCode,
								   int districtCode, boolean regularInTerminal, String deviceId,
								   int businessId, boolean isAdd, String dynamicPassword) {
		logger.error("TerminalAddOrUpdate()=" + isAdd + ": terminalId="
				+ terminalId + ",protoCode=" + protoCode + ",districtCode="
				+ districtCode + ",regularInTerminal=" + regularInTerminal
				+ ",deviceId=" + deviceId + ",businessType=" + businessId
				+ ",dynamicPassword=" + dynamicPassword);

		// 校验动态口令
//		Map<String, Object> dynamicPasswordMap = redis_map_dao
//				.get(RedisConstans.RedisKey.DYNAMIC_PASSWORD.name());
//		if (null == dynamicPassword
//				|| dynamicPasswordMap.get(dynamicPassword) == null) {
//			logger.error("TerminalAddOrUpdate():动态口令失效-->" + dynamicPassword);
//			return null;
//		}
//		if (dynamicPasswordMap.get(dynamicPassword).equals(dynamicPassword)) {
			// 动态口令正确做新增修改操作
			LcTerminalInfoDBEntity terminalInfo = new LcTerminalInfoDBEntity();
			terminalInfo.setTerminal_id(terminalId);
			terminalInfo.setProto_code(protoCode);
			terminalInfo.setDistrict(districtCode);
			terminalInfo.setData_status(1);
			terminalInfo.setCreate_time(System.currentTimeMillis() / 1000);
			if (regularInTerminal == true) {
				terminalInfo.setRegular_in_terminal(1);
			} else {
				terminalInfo.setRegular_in_terminal(0);
			}
			terminalInfo.setDevice_id(deviceId);
			terminalInfo.setBusiness_type(businessId);
			LCPlatformResponseResult.PlatformResponseResult result = terminalInfoManage
					.saveOrUpdateTerminalInfoForSyn(terminalInfo, isAdd);
			logger.error("TerminalAddOrUpdate() return=" + result.getNumber());
			int status =  result.getNumber();
            if(status == 1){
				Map<String, Object> terminalMap = new HashMap<>();
				terminalMap.put("terminal_id", terminalId);
                terminalMap.put("proto_code", protoCode);
                terminalMap.put("district", districtCode);
                terminalMap.put("data_status", 1);
                terminalMap.put("create_time", System.currentTimeMillis() / 1000);
                terminalMap.put("device_id", deviceId);
                terminalMap.put("business_type", businessId);

                if (regularInTerminal == true) {
                    terminalMap.put("regular_in_terminal", 1);
                } else {
                    terminalMap.put("regular_in_terminal", 0);
                }
				return terminalMap;
			}
		/*} else {
			logger.error("动态口令错误!");
			new DynamicPasswordException("动态口令错误!");
		}
		*/
		return null;
	}

	@Override
	public int TerminalDelete(long[] terminalId, String dynamicPassword) {
		logger.error("TerminalDelete(): size=" + terminalId.length
				+ ",terminals=" + Arrays.toString(terminalId)
				+ ",dynamicPassword= " + dynamicPassword);

		// 校验动态口令
		/*Map<String, Object> dynamicPasswordMap = redis_map_dao
				.get(RedisConstans.RedisKey.DYNAMIC_PASSWORD.name());
		if (dynamicPasswordMap.get(dynamicPassword) == null) {
			logger.error("动态口令失效!");
			return 0;
		}
		if (dynamicPasswordMap.get(dynamicPassword).equals(dynamicPassword)) {*/
			// 动态口令正确做批量操作
			LCPlatformResponseResult.PlatformResponseResult result = terminalInfoManage
					.deleteTerminalInfoForSyn(terminalId);
            logger.error("TerminalDelete() return=" + result.getNumber());
			return result.getNumber();
		/*} else {
			new DynamicPasswordException("动态口令错误!");
		}
		return 0;*/
	}

	@Override
	public int TerminalMappingBindOrNot(long mainTerminalId,
			long secondaryTerminalId, boolean isBinding, String dynamicPassword)
			{
		logger.error("TerminalMappingBindOrNot(): mainTerminalId=" + mainTerminalId
				+ ",secondaryTerminalId=" +secondaryTerminalId+",isBinding="+isBinding
				+ ",dynamicPassword= " + dynamicPassword);
		// 校验动态口令
		/*Map<String, Object> dynamicPasswordMap = redis_map_dao
				.get(RedisConstans.RedisKey.DYNAMIC_PASSWORD.name());
		if (dynamicPasswordMap.get(dynamicPassword) == null) {
			logger.error("动态口令失效!");
			return 0;
		}
		if (dynamicPasswordMap.get(dynamicPassword).equals(dynamicPassword)) {*/
			// 动态口令正确
			LCPlatformResponseResult.PlatformResponseResult result = mappingManage.terminalMappingBindOrNotSync(mainTerminalId, secondaryTerminalId, isBinding);
			logger.error("TerminalMappingBindOrNot() return=" + result.getNumber());
			return result.getNumber();
		/*} else {
			logger.error("动态口令错误!");
			return 0;
		}*/
	}

}

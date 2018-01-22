package com.navinfo.opentsp.platform.da.core.persistence.application;

import com.navinfo.opentsp.platform.da.core.webService.manage.WebServiceTools;
import com.navinfo.opentsp.platform.da.core.webService.service.TerminalWebService;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  com.navinfo.opentsp.platform.da.core.persistence.TerminalMappingManage;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalInfoDBEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalMappingDBEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalInfoDao;
import  com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalMappingDao;
import  com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import  com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcTerminalInfoDaoImpl;
import  com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcTerminalMappingDaoImpl;

public class TerminalMappingManageImpl implements TerminalMappingManage {
	private static final Logger log = LoggerFactory
			.getLogger(TerminalMappingManageImpl.class);
	private LcTerminalInfoDao infoDao = new LcTerminalInfoDaoImpl();
	private LcTerminalMappingDao mappingDao = new LcTerminalMappingDaoImpl();
	
	@Override
	public LCPlatformResponseResult.PlatformResponseResult terminalMappingBindOrNot(long mainTerminalId,
			long secondaryTerminalId, boolean isBinding) {
		int result = 0;
		LcTerminalMappingDBEntity main = mappingDao.getMappingByMainId(mainTerminalId);
		LcTerminalMappingDBEntity second = mappingDao.getMappingBySecondId(secondaryTerminalId);
		if(isBinding){
			if(null != main){
				log.error("终端映射表中已存在该MainID，不能再次绑定");
				return LCPlatformResponseResult.PlatformResponseResult.terminalIdentifyIsExist;
			}else if(null != second){
				log.error("终端映射表中已存在该SecondID，不能再次绑定");
				return LCPlatformResponseResult.PlatformResponseResult.terminalIdentifyIsExist;
			}else{
				//判断main，second是否在终端表中有对应记录
				LcTerminalInfoDBEntity mainInfo = infoDao.getByTerminalId(mainTerminalId);
				LcTerminalInfoDBEntity secondInfo = infoDao.getByTerminalId(secondaryTerminalId);
				if(null == mainInfo){
					log.error("终端表不存在terminalID为："+mainTerminalId+"的记录，不能进行绑定。");
				}else if(null == secondInfo){
					log.error("终端表不存在terminalID为："+secondaryTerminalId+"的记录，不能进行绑定。");
				}else{
					result = mappingDao.addTerminalMapping(mainTerminalId, secondaryTerminalId);
				}
			}
		}else{
			if(null == main){
				log.error("终端映射表中不存在该MainID，不能解绑");
			}else if(null == second){
				log.error("终端映射表中不存在该SecondID，不能解绑");
			}else{
				result = mappingDao.deleteTerminalMapping(mainTerminalId, secondaryTerminalId);
			}
		}
		if( 1 == result){
			return LCPlatformResponseResult.PlatformResponseResult.success;
		}else{
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		}
	}

	@Override
	public LCPlatformResponseResult.PlatformResponseResult terminalMappingBindOrNotSync(
			long mainTerminalId, long secondaryTerminalId, boolean isBinding) {
		MySqlConnPoolUtil.startTransaction();
		LCPlatformResponseResult.PlatformResponseResult result = terminalMappingBindOrNot(mainTerminalId, secondaryTerminalId, isBinding);
		log.info("总部处理完成，结果："+result.getNumber());
		if(LCPlatformResponseResult.PlatformResponseResult.success == result){
			//提交事务
			MySqlConnPoolUtil.commit();
//			log.info("开始同步到分区");
//			final LcTerminalInfoDBEntity terminalInfo = infoDao.getInfoByTerminalId(secondaryTerminalId);
//			try {
//				TerminalWebService termianlWebService = (TerminalWebService) WebServiceTools.getOperationClass(terminalInfo.getDistrict() + "", TerminalWebService.class);
//				LCPlatformResponseResult.PlatformResponseResult wsResult = termianlWebService.terminalMappingBindOrNot(mainTerminalId, secondaryTerminalId, isBinding);
//				if (wsResult == LCPlatformResponseResult.PlatformResponseResult.success) {
//					log.error("调用分区district="+terminalInfo.getDistrict()+"，同步终端映射成功");
//					MySqlConnPoolUtil.commit();
//				} else {
//					log.error("调用分区district="+terminalInfo.getDistrict()+"，同步终端映射失败");
//					result = LCPlatformResponseResult.PlatformResponseResult.failure;
//					MySqlConnPoolUtil.rollback();
//				}
//			} catch (Exception e) {
//				log.error("terminalMappingBindOrNotSync调用分区操作失败",e);
//				result = LCPlatformResponseResult.PlatformResponseResult.failure;
//				MySqlConnPoolUtil.rollback();
//			}
		}else{
			log.error("总部处理失败");
			MySqlConnPoolUtil.rollback();
		}
		MySqlConnPoolUtil.close();
		return result;
	}

	@Override
	public LcTerminalMappingDBEntity getMappingByMainId(long mainId) {
		LcTerminalMappingDBEntity main = mappingDao.getMappingByMainId(mainId);
		return main;
	}

	@Override
	public LcTerminalMappingDBEntity getMappingBySecondId(long secondId) {
		LcTerminalMappingDBEntity second = mappingDao.getMappingByMainId(secondId);
		return second;
	}
}

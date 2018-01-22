package com.navinfo.opentsp.platform.da.core.persistence.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.OptResult;
import com.navinfo.opentsp.platform.da.core.persistence.SynchronousManage;
import com.navinfo.opentsp.platform.da.core.persistence.TerminalInfoManage;
import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.TerminalOnlineModel;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.*;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.*;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.*;
import com.navinfo.opentsp.platform.da.core.webService.manage.SynchronousDataCache;
import com.navinfo.opentsp.platform.da.core.webService.manage.WebServiceTools;
import com.navinfo.opentsp.platform.da.core.webService.service.TerminalWebService;
import com.navinfo.opentsp.platform.location.kit.lang.ObjectUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TerminalInfoManageImpl implements TerminalInfoManage {
	private static final Logger log = LoggerFactory
			.getLogger(TerminalInfoManageImpl.class);
	private SynchronousManage synchronousManage = new SynchronousManageImpl();
	/**
	 * 查询终端列表
	 *
	 * @param districtCode
	 * @return {@link List}<{@link Map}> Key<br>
	 *         terminalInfo=终端信息-->{@link LcTerminalInfoDBEntity}<br>
	 *         terminalRegisterInfo=终端注册信息-->{@link LcTerminalRegisterDBEntity}
	 */
	@Override
	public List<Map<String, Object>> getTerminalInfos(int districtCode) {
		// 单表查询，map用new ConcurrentHashMap<String, Object>()实现
		try {
			MySqlConnPoolUtil.startTransaction();
			// 查询终端信息表
			LcTerminalInfoDao terminalInfoDao = new LcTerminalInfoDaoImpl();
//			LcTerminalRegisterDao terminalRegisterDao = new LcTerminalRegisterDaoImpl();
			List<LcTerminalInfoDBInfo> terminalInfos = terminalInfoDao
					.getByNodeCodeAndDistrict(districtCode);
			if (CollectionUtils.isNotEmpty(terminalInfos)) {
				List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
				for (LcTerminalInfoDBInfo terminalInfo : terminalInfos) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(Constant.PropertiesKey.TerminalInfoManageConstant.TERMINALINFO, terminalInfo);
					// 把map放入到result中
					result.add(map);
				}
				return result;
			} else {
				return null;
			}

		} catch (Exception e) {
			log.error(e.getMessage(),e);
		} finally {
			MySqlConnPoolUtil.close();
		}
		return null;
	}

	/**
	 * 更新终端所在TA节点信息
	 * @param terminalId
	 * @param districtCode
	 * @param node_code
	 * @param changeTId
	 * @return
	 */
	@Override
	public OptResult updateTerminalInfo(long terminalId, int districtCode,
										long node_code,long changeTId) {
		// 根据terminalId和districtCode查询，然后更新taIp,然后修改redis中终端的信息
		// 单表的插入操作
		OptResult optResult = new OptResult();
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalInfoDao lcTerminalInfoDao = new LcTerminalInfoDaoImpl();
			int result = lcTerminalInfoDao.updateTerminalInfo(terminalId,
					districtCode, node_code,changeTId);
			// 将返回值存入optResult的属性中
			optResult.setStatus(result);
			if (result == PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
			} else {
				MySqlConnPoolUtil.rollback();
				optResult.setStatus(PlatformResponseResult.failure_VALUE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			optResult.setStatus(PlatformResponseResult.failure_VALUE);
		} finally {
			MySqlConnPoolUtil.close();
		}
		return optResult;
	}

	/**
	 * 终端注册信息存储
	 *
	 * @param terminalRegister
	 *            {@link LcTerminalRegisterDBEntity}
	 * @return
	 */
	@Override
	public OptResult saveTerminalRegister(
			LcTerminalRegisterDBEntity terminalRegister) {
		// 单表的插入操作
		OptResult optResult = new OptResult();
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalRegisterDao lcTerminalRegisterDao = new LcTerminalRegisterDaoImpl();
			// 注册的逻辑是先通过终端id查找，如果有则更新，如果没有新建
			LcTerminalRegisterDBEntity register = lcTerminalRegisterDao
					.getByTerminalId(terminalRegister.getTerminal_id());
			int result = PlatformResponseResult.failure_VALUE;
			if (register == null) {
				// 新增
				result = lcTerminalRegisterDao.add(terminalRegister);
			} else {
				// 修改
				result = lcTerminalRegisterDao
						.updateByTerminalId(terminalRegister);
			}

			// 将返回值存入optResult的属性中
			optResult.setStatus(result);
			if (result == PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
			} else {
				MySqlConnPoolUtil.rollback();
				optResult.setStatus(PlatformResponseResult.failure_VALUE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			optResult.setStatus(PlatformResponseResult.failure_VALUE);
		} finally {
			MySqlConnPoolUtil.close();
		}
		return optResult;
	}

	/**
	 * 根据终端id查找
	 *
	 * @param terminalId
	 * @return
	 */
	@Override
	public LcTerminalRegisterDBEntity queryTerminalRegister(long terminalId) {
		// 单表的查询
		LcTerminalRegisterDBEntity lcTerminalRegister = null;
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalRegisterDao lcTerminalRegisterDao = new LcTerminalRegisterDaoImpl();
			lcTerminalRegister = lcTerminalRegisterDao
					.getByTerminalId(terminalId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MySqlConnPoolUtil.close();
		}
		return lcTerminalRegister;
	}

	/**
	 * 参数存储
	 *
	 * @param terminalId
	 * @param parameterCode
	 * @param content
	 * @return
	 */
	@Override
	public OptResult saveOrUpdateTerminalParameter(
			LcTerminalParaDBEntity terminalPara) {
		// 单表操作
		// 单表的查询
		OptResult optResult = new OptResult();
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalParaDao lcTerminalParaDao = new LcTerminalParaDaoImpl();
			int resutl = 0;
			// 先从数据库查找，如果有就修改，如果没有就添加
			List<LcTerminalParaDBEntity> list = lcTerminalParaDao
					.getByTerminalParameter(terminalPara.getTerminal_id(),
							terminalPara.getPara_code());
			if (CollectionUtils.isNotEmpty(list)) {
				// 修改
				LcTerminalParaDBEntity object = list.get(0);
				object.setLast_update_time(terminalPara.getLast_update_time());
				object.setPara_content(terminalPara.getPara_content());
				resutl = lcTerminalParaDao.update(object);
			} else {
				// 添加
				resutl = lcTerminalParaDao.add(terminalPara);
			}
			if (resutl == PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
			} else {
				MySqlConnPoolUtil.rollback();
			}
			optResult.setStatus(resutl);
		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();
			optResult.setStatus(PlatformResponseResult.failure_VALUE);
		} finally {
			MySqlConnPoolUtil.close();
		}
		return optResult;
	}

	/**
	 * 删除终端参数
	 *
	 * @param terminalId
	 * @param parameterCode
	 * @return
	 */
	@Override
	public OptResult deleteTerminalParameter(long terminalId, int parameterCode) {
		// 单表删除
		OptResult optResult = new OptResult();
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalParaDao lcTerminalParaDao = new LcTerminalParaDaoImpl();
			int result = lcTerminalParaDao.deleteByTerminalInfo(terminalId,
					parameterCode);
			if (result == PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
			} else {
				MySqlConnPoolUtil.rollback();
				optResult.setStatus(PlatformResponseResult.failure_VALUE);
			}
			optResult.setStatus(result);
		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();
			optResult.setStatus(PlatformResponseResult.failure_VALUE);
		} finally {
			MySqlConnPoolUtil.close();
		}
		return optResult;
	}

	/**
	 * 查询终端参数
	 *
	 * @param terminalId
	 * @param paramterCode
	 *            {@link Integer}终端参数编码
	 * @return {@link List}<{@link LcTerminalParaDBEntity}>
	 */
	@Override
	public List<LcTerminalParaDBEntity> getTerminalParameter(long terminalId,
															 int... paramterCode) {
		// 单表操作
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalParaDao lcTerminalParaDao = new LcTerminalParaDaoImpl();
			return lcTerminalParaDao.getByTerminalParameter(terminalId,
					paramterCode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MySqlConnPoolUtil.close();
		}
		return null;
	}
	/**
	 * 原方法，没有isAdd参数
	 */
	@Override
	public PlatformResponseResult saveOrUpdateTerminalInfo(
			LcTerminalInfoDBEntity terminalInfo) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalInfoDao lcTerminalInfoDao = new LcTerminalInfoDaoImpl();
			int result=0;
			//①终端信息保存时，只需把数据同步到对应分区库即可。add。
			//②终端修改时，分两种情况。一、分区不改变时，只需修改。二、分区改变时，需把旧分区终端信息删除（旧分区逻辑操作删除），新分区库添加一条终端信息。
			LcTerminalInfoDBEntity queryInfo = lcTerminalInfoDao.getInfoByTerminalId(terminalInfo.getTerminal_id());
			if(null != queryInfo && 0 != queryInfo.getTi_id()){
				//修改
				terminalInfo.setTi_id(queryInfo.getTi_id());
				terminalInfo.setNode_code(queryInfo.getNode_code());
				terminalInfo.setData_status(1);
				log.info("终端修改操作："+terminalInfo.toString());
				result = lcTerminalInfoDao.updateByTerminalId(terminalInfo);
			}else{
				//新增
				log.info("终端新增操作："+terminalInfo.toString());
				result = lcTerminalInfoDao.add(terminalInfo);
			}
			if (result == PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
				return PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return PlatformResponseResult.failure;
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	/**
	 * 只以terminalId为唯一校验标准，不做deviceId校验，deviceId交给寰游校验唯一性——hk
	 */
	@Override
	public PlatformResponseResult saveOrUpdateTerminalInfo(
			LcTerminalInfoDBEntity terminalInfo,boolean isAdd) {
		int result=0;
		try {
			log.error("saveOrUpdateTerminalInfo(),isAdd="+isAdd+",terminalInfo="+terminalInfo.toString());
			MySqlConnPoolUtil.startTransaction();
			LcTerminalInfoDao lcTerminalInfoDao = new LcTerminalInfoDaoImpl();
			if(isAdd) {
				//①终端信息保存时，只需把数据同步到对应分区库即可。add。
				//②终端修改时，分两种情况。一、分区不改变时，只需修改。二、分区改变时，需把旧分区终端信息删除（旧分区逻辑操作删除），新分区库添加一条终端信息。
				if (terminalInfo.getTi_id() == null) {
					//判断设备ID是否存在
					LcTerminalInfoDBEntity deviceIdQuery = lcTerminalInfoDao.getInfoByDeviceId(terminalInfo.getDevice_id());
					if(null != deviceIdQuery && null != deviceIdQuery.getTi_id() && deviceIdQuery.getData_status().equals(1) ){//&& deviceIdQuery.getData_status().equals(1) DeviceId查重，只从正常数据中查
						log.error("正常数据中此记录已存在deviceId="+terminalInfo.getDevice_id()+",result="+PlatformResponseResult.deviceIdentifyIsExist.name()+","+deviceIdQuery.toString());
						return PlatformResponseResult.deviceIdentifyIsExist;
					}
					//判断terminalID
					//TerminalId(唯一)在所有的数据中查重，包括已经被删除的数据
					LcTerminalInfoDBEntity queryInfo = lcTerminalInfoDao.getInfoByTerminalId(terminalInfo.getTerminal_id());
					if(null != queryInfo && null != queryInfo.getTi_id() ) {
						//数据库中存在terminalID相同的终端
						if(1 == queryInfo.getData_status()) {//在正常数据中查到记录
							log.error("terminalId已存在：result="+PlatformResponseResult.terminalIdentifyIsExist.name()+ ","+queryInfo.toString());
							return PlatformResponseResult.terminalIdentifyIsExist;
						}else if(0 == queryInfo.getData_status()) {
							//在已删除数据中查到此记录
							log.error("已删除数据中已经存在此记录:TerminalId="+queryInfo.getTerminal_id()+",result="+PlatformResponseResult.terminalIdentifyIsExist.name()+
									","+queryInfo.toString());
							terminalInfo.setData_status(1);
							if(1==lcTerminalInfoDao.deletePhysiscByTerminalId(terminalInfo.getTerminal_id())){
//								result = lcTerminalInfoDao.updateByTerminalId(terminalInfo);
								////根据2016-9-12讨论决定，如果新增终端的数据在数据库中属于已删除，则删除原来的再新增
								result = lcTerminalInfoDao.add(terminalInfo);
							}
//							result = lcTerminalInfoDao.updateByTerminalId(terminalInfo);
//							if(PlatformResponseResult.success_VALUE == result){
//								log.info("更新成功，把删除更改为正常数据");
//							}else{
//								log.error("把删除更改为正常数据操作失败");
//								return PlatformResponseResult.failure;
//							}
						}
					}else{
						//新增
						result = lcTerminalInfoDao.add(terminalInfo);
					}
				}else{
					log.error("新增操作，但是主键不是null,"+terminalInfo.getTi_id());
					return PlatformResponseResult.failure;
				}
			}else {//修改
				LcTerminalInfoDBEntity queryByTerminalId = lcTerminalInfoDao.getInfoByTerminalId(terminalInfo.getTerminal_id());
				if(null != queryByTerminalId ) {
					//如果查询得到的终端信息设备ID和传入的不同，则要判断传入的设备ID在数据库是否存在
					if(!queryByTerminalId.getDevice_id().equals(terminalInfo.getDevice_id())){
						LcTerminalInfoDBEntity infoByDeviceId = lcTerminalInfoDao.getInfoByDeviceId(terminalInfo.getDevice_id());
						if(null != infoByDeviceId){
							log.error("修改操作，修改了终端的设备ID，但改设备ID已存在，不能修改");
							return PlatformResponseResult.deviceIdentifyIsExist;
						}
					}
					if(1 == queryByTerminalId.getData_status()) {//在正常数据中查到记录
						log.error("修改操作:"+queryByTerminalId.toString());
						result = lcTerminalInfoDao.updateByTerminalId(terminalInfo);
						if(1 == result){
							log.info("修改成功");
						}else{
							log.error("修改失败");
							return PlatformResponseResult.failure;
						}
					}else{
						//在已删除数据中查到此记录，删除查到的记录
						log.error("修改操作，已删除数据中已经存在此记录:TerminalId=："+queryByTerminalId.getTerminal_id()+",result="+PlatformResponseResult.terminalIdentifyIsExist.name()+
								","+queryByTerminalId.toString());
						terminalInfo.setData_status(1);
						result = lcTerminalInfoDao.updateByTerminalId(terminalInfo);
						if(PlatformResponseResult.success_VALUE == result){
							log.info("更新成功，把删除更改为正常数据");
						}else{
							log.error("把删除更改为正常数据操作失败");
							return PlatformResponseResult.failure;
						}
					}
				} else {
					log.error("修改操作，数据库中没有找到对应终端ID的记录，执行新增操作");
					//新增
					result = lcTerminalInfoDao.add(terminalInfo);
				}
			}
			log.error("saveOrUpdateTerminalInfo(),isAdd="+isAdd+",result="+result+",terminalInfo="+terminalInfo.toString());
			return PlatformResponseResult.valueOf(result);
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			MySqlConnPoolUtil.rollback();
			return PlatformResponseResult.failure;
		}finally {
			if (result == PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
			} else {
				MySqlConnPoolUtil.rollback();
			}
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public PlatformResponseResult deleteTerminalInfo(long[] primaryKeys) {
		try {
			log.error("deleteTerminalInfo(),terminalIds="+Arrays.toString(primaryKeys));
			MySqlConnPoolUtil.startTransaction();
			LcTerminalInfoDao terminalInfoDao = new LcTerminalInfoDaoImpl();
			for (long l : primaryKeys) {
				terminalInfoDao.deleteByTerminalId(l);
			}
			MySqlConnPoolUtil.commit();
			return PlatformResponseResult.success;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			MySqlConnPoolUtil.rollback();
			return PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public LcTerminalInfoDBEntity queryTerminalInfo(long terminalId) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalInfoDao terminalInfoDao = new LcTerminalInfoDaoImpl();
			LcTerminalInfoDBEntity entity=terminalInfoDao.getByTerminalId(terminalId);
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public LcTerminalInfoDBEntity queryTerminalInfoById(long terminalId) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalInfoDao terminalInfoDao = new LcTerminalInfoDaoImpl();
			LcTerminalInfoDBEntity entity=terminalInfoDao.getInfoByTerminalId(terminalId);
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalParaDBEntity> queryTerminalParaRes(long terminalId,
															 int command,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalParaDao terminalParaDao = new LcTerminalParaDaoImpl();
			return terminalParaDao.queryTerminalParaRes(terminalId, command,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalInfoDBEntity> queryTerminalInfoRes(long terminalId,
															 int protoType,String deviceId,long changeId, long start, long end,int flag,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalInfoDao terminalInfoDao = new LcTerminalInfoDaoImpl();
			return terminalInfoDao.queryTerminalParaRes(terminalId, protoType,deviceId,changeId,
					start, end,flag,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public int queryTerminalInfoResCount(long terminalId,
										 int protoType,String deviceId,long changeId, long start, long end){
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalInfoDao terminalInfoDao = new LcTerminalInfoDaoImpl();
			return terminalInfoDao.queryTerminalInfoResCount(terminalId, protoType,deviceId,changeId,
					start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public TerminalOnlineModel queryTerminalOnLineRes(long terminalId) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalSwitchLogDao terminalSwitchLogDao = new LcTerminalSwitchLogDaoImpl();
			TerminalOnlineModel terminalOnlineModel = new TerminalOnlineModel();
			terminalOnlineModel.setTerminalInfoModel(terminalSwitchLogDao
					.queryTerminalInfoRes(terminalId));
			// 添加位置数据
			// terminalOnlineModel.setLocationData(byte[]);
			return terminalOnlineModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcMultimediaParaDBEntity> queryMediaParaRes(long terminalId,
															long start, long end,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcMultimediaParaDao multimediaParaDao = new LcMultimediaParaDaoImpl();
			List<LcMultimediaParaDBEntity> multimeParas = new ArrayList<LcMultimediaParaDBEntity>();
			multimeParas = multimediaParaDao.getMultimedia(
					new long[] { terminalId }, 0, start, end,currentPage,pageSize);
			return multimeParas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public PlatformResponseResult batchAddTerminalInfo(
			List<LcTerminalInfoDBEntity> list) {
		try{
			MySqlConnPoolUtil.startTransaction();
			LcTerminalInfoDao lcTerminalInfoDao = new LcTerminalInfoDaoImpl();
			int result=lcTerminalInfoDao.batchAddTerminalInfo(list);
			if (result == PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
				return PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return PlatformResponseResult.failure;
			}

		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();
			return PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public PlatformResponseResult saveOrUpdateTerminalInfoForSyn(
			LcTerminalInfoDBEntity terminalInfo) {
		// 基本信息保存
		PlatformResponseResult responseResult = saveOrUpdateTerminalInfo(terminalInfo);
		// 数据同步
		if (responseResult == PlatformResponseResult.success) {
			// 调用分区ws
			LcSynchronizationLogDBEntity entity = new LcSynchronizationLogDBEntity();
			try {
				TerminalWebService termianlWebService = (TerminalWebService) WebServiceTools
						.getOperationClass(terminalInfo.getDistrict() + "",
								TerminalWebService.class);
				PlatformResponseResult wsResult = termianlWebService
						.saveOrUpdateTerminalInfo(terminalInfo);
				if (wsResult == PlatformResponseResult.success) {
					entity.setStatus(PlatformResponseResult.success_VALUE);
				} else {
					entity.setStatus(PlatformResponseResult.failure_VALUE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				entity.setStatus(PlatformResponseResult.failure_VALUE);
			}
			// 组装同步数据
			if (terminalInfo.getTi_id() == null || terminalInfo.getTi_id() == 0) {
				entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.save);
			} else {
				entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.update);
			}
			entity.setId(SynchronousDataCache.updateMaxSynchronizationLogId());
			entity.setDistrict(terminalInfo.getDistrict() + "");
			entity.setModuleName(Constant.PropertiesKey.SynchronousDataParameters.TerminalWebService);
			entity.setOperationDate(System.currentTimeMillis() / 1000);
			entity.setOperationObject(ObjectUtils.objectToBytes(terminalInfo));
			entity.setType(Constant.PropertiesKey.DBTableName.LC_TERMINAL_INFO);
			entity.setWebserviceClass(TerminalWebService.class.getName());
			// 放缓存
			if (entity.getStatus() == PlatformResponseResult.failure_VALUE) {
				SynchronousDataCache.add(terminalInfo.getDistrict(), entity);
			}
			// 放数据库
			PlatformResponseResult result = synchronousManage
					.saveSynchronousData(entity);
			if (result == PlatformResponseResult.failure) {
				log.error("同步数据日志表存储失败");
			}
		}
		return responseResult;
	}


	static ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
	@Override
	public PlatformResponseResult saveOrUpdateTerminalInfoForSyn(
			LcTerminalInfoDBEntity terminalInfo,boolean isAdd) {
		// 基本信息保存
		PlatformResponseResult responseResult = saveOrUpdateTerminalInfo(terminalInfo,isAdd);

		// 分区数据同步
		//这里为了优化返回响应速度，启用一个线程来执行对分区数据的同步操作
		if (responseResult == PlatformResponseResult.success) {
			pool.execute(new ExecuteDistrictsaveOrUpdateThread(terminalInfo,isAdd));
		}
		return responseResult;
	}

	class ExecuteDistrictsaveOrUpdateThread implements Runnable {
		LcTerminalInfoDBEntity terminalInfo;
		boolean isAdd = false;
		public ExecuteDistrictsaveOrUpdateThread(LcTerminalInfoDBEntity terminalInfo,boolean isAdd) {
			this.terminalInfo = terminalInfo;
			this.isAdd = isAdd;
		}
		@Override
		public void run() {
			// 调用分区ws
			LcSynchronizationLogDBEntity entity = new LcSynchronizationLogDBEntity();
			try {
				TerminalWebService termianlWebService = (TerminalWebService) WebServiceTools.getOperationClass(terminalInfo.getDistrict() + "",TerminalWebService.class);
				PlatformResponseResult wsResult = termianlWebService.saveOrUpdateTerminalInfo2(terminalInfo,isAdd);
				if (wsResult == PlatformResponseResult.success) {
					log.error("调用分区district="+terminalInfo.getDistrict()+"，同步成功");
					entity.setStatus(PlatformResponseResult.success_VALUE);
				} else {
					log.error("调用分区district="+terminalInfo.getDistrict()+"，同步失败");
					entity.setStatus(PlatformResponseResult.failure_VALUE);
				}
			} catch (Exception e) {
//				e.printStackTrace();
				log.error("saveOrUpdateTerminalInfoForSyn调用分区districtCode="+terminalInfo.getDistrict()+",操作失败",e);
				entity.setStatus(PlatformResponseResult.failure_VALUE);
			}
			// 组装同步数据
			if (isAdd) {
				entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.save);
			} else {
				entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.update);
			}
			entity.setId(SynchronousDataCache.updateMaxSynchronizationLogId());
			entity.setDistrict(terminalInfo.getDistrict() + "");
			entity.setModuleName(Constant.PropertiesKey.SynchronousDataParameters.TerminalWebService);
			entity.setOperationDate(System.currentTimeMillis() / 1000);
			entity.setOperationObject(ObjectUtils.objectToBytes(terminalInfo));
			entity.setType(Constant.PropertiesKey.DBTableName.LC_TERMINAL_INFO);
			entity.setWebserviceClass(TerminalWebService.class.getName());
			// 放缓存
			if (entity.getStatus() == PlatformResponseResult.failure_VALUE) {
				SynchronousDataCache.add(terminalInfo.getDistrict(), entity);
			}
			// 日志放数据库
			PlatformResponseResult result = synchronousManage.saveSynchronousData(entity);
			if (result == PlatformResponseResult.failure) {
				log.error("同步分区数据日志表存储失败");
			}
		}
	}

	@Override
	public PlatformResponseResult deleteTerminalInfoForSyn(long[] primaryKeys) {
		// 基本信息删除
		PlatformResponseResult responseResult = deleteTerminalInfo(primaryKeys);
		if (responseResult == PlatformResponseResult.success) {
			//这里为了优化返回响应速度，启用一个线程来执行对分区数据的同步操作
			pool.execute(new ExecuteDistrictDeleteDataThread(primaryKeys));
		}else {
			log.error("总部终端删除");
		}
		return responseResult;
	}

	class ExecuteDistrictDeleteDataThread implements Runnable {
		long[] primaryKeys;
		public ExecuteDistrictDeleteDataThread(long[] primaryKeys) {
			this.primaryKeys = primaryKeys;
		}
		@Override
		public void run() {
			for (Long i : primaryKeys) {
				LcSynchronizationLogDBEntity entity = new LcSynchronizationLogDBEntity();
				LcTerminalInfoDBEntity terminalInfo = queryTerminalInfoById(i);
				if(terminalInfo == null) {
					continue;
				}
				// 调用ws
				try {
					TerminalWebService termianlWebService = (TerminalWebService) WebServiceTools
							.getOperationClass(terminalInfo.getDistrict() + "",
									TerminalWebService.class);
					PlatformResponseResult wsResult = termianlWebService
							.deleteTerminalInfo(new long[] { i });
					if (wsResult == PlatformResponseResult.success) {
						log.error("分区district="+terminalInfo.getDistrict()+"，同步删除成功");
						entity.setStatus(PlatformResponseResult.success_VALUE);
					} else {
						log.error("调用分区district="+terminalInfo.getDistrict()+"，同步删除失败");
						entity.setStatus(PlatformResponseResult.failure_VALUE);
					}
				} catch (Exception e) {
//					e.printStackTrace();
					log.error("saveOrUpdateTerminalInfoForSyn调用分区districtCode="+terminalInfo.getDistrict()+",操作失败",e);
					entity.setStatus(PlatformResponseResult.failure_VALUE);
				}
				// 组装同步数据
				entity.setId(SynchronousDataCache.updateMaxSynchronizationLogId());
				entity.setDistrict(terminalInfo.getDistrict() + "");
				entity.setModuleName(Constant.PropertiesKey.SynchronousDataParameters.TerminalWebService);
				entity.setOperationDate(System.currentTimeMillis() / 1000);
				entity.setOperationObject(ObjectUtils.objectToBytes(terminalInfo));
				entity.setType(Constant.PropertiesKey.DBTableName.LC_TERMINAL_INFO);
				entity.setWebserviceClass(TerminalWebService.class.getName());
				entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.delete);
				// 放缓存
				if (entity.getStatus() == PlatformResponseResult.failure_VALUE) {
					SynchronousDataCache.add(terminalInfo.getDistrict(), entity);
				}
				// 放数据库
				PlatformResponseResult result = synchronousManage.saveSynchronousData(entity);
				if (result == PlatformResponseResult.failure) {
					log.error("同步数据日志表存储失败");
				}
			}
		}
	}
}
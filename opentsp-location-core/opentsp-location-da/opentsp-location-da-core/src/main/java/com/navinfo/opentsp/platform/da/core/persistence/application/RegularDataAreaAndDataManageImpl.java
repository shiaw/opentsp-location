package com.navinfo.opentsp.platform.da.core.persistence.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.navinfo.opentsp.platform.da.core.persistence.OptResult;
import com.navinfo.opentsp.platform.da.core.persistence.RegularDataAreaAndDataManage;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.*;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalAreaDao2;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalAreaDataDao2;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalParaDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalRuleDao2;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcTerminalAreaDaoImpl2;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcTerminalAreaDataDaoImpl2;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcTerminalParaDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcTerminalRuleDaoImpl2;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class RegularDataAreaAndDataManageImpl implements RegularDataAreaAndDataManage {
	private static final Logger log = LoggerFactory.getLogger(RegularDataAreaAndDataManageImpl.class);
	// private SynchronousManage synchronousManage = new
	// SynchronousManageImpl();
	private static LcTerminalRuleDao2 ruleDao = new LcTerminalRuleDaoImpl2();
	private static LcTerminalAreaDao2 areaDao = new LcTerminalAreaDaoImpl2();
	private static LcTerminalAreaDataDao2 areaDataDao = new LcTerminalAreaDataDaoImpl2();

	/**
	 * 查询终端参数
	 *
	 * @param terminalId
	 * @param paramterCode
	 *            {@link Integer}终端参数编码
	 * @return {@link List}<{@link LcTerminalParaDBEntity}>
	 */
	@Override
	public List<LcTerminalParaDBEntity> getTerminalParameter(long terminalId, int... paramterCode) throws Exception {
		// 单表操作
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalParaDao lcTerminalParaDao = new LcTerminalParaDaoImpl();
			List<LcTerminalParaDBEntity> list = lcTerminalParaDao.getByTerminalParameter(terminalId, paramterCode);
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	/**
	 * 据TA节点IP查询区域数据 <br>
	 * 1、查询TA节点下的终端列表 <br>
	 * 2、据1查询的终端列表查询区域
	 *
	 * @param districtCode
	 * @return {@link List}<{@link Map}> Key<br>
	 *         topic=区域信息-->{@link LcTerminalAreaDBEntity}<br>
	 *         detailed=区域数据-->{@link LcTerminalAreaDataDBEntity}
	 */
	@Override
	public List<Map<String, Object>> queryAreaInfo(int districtCode, long node_code) throws Exception {
		// 根据districtCode和taServerIp找到终端，然后通过终端得到区域信息
		// LC_TERMINAL_INFO c ,LC_TERMINAL_AREA ,LC_TERMINAL_AREA_DATA
		try {
			MySqlConnPoolUtil.startTransaction();
			// 查询终端信息表

			List<LcTerminalAreaDBEntity> lcTerminalAreas = areaDao.getAreaInfo(districtCode,
					new int[] { new Long(node_code).intValue() });
			if (CollectionUtils.isNotEmpty(lcTerminalAreas)) {
				List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
				for (LcTerminalAreaDBEntity lcTerminalArea : lcTerminalAreas) {
					Map<String, Object> map = new ConcurrentHashMap<String, Object>();
					map.put("topic", lcTerminalArea);
					// 查找区域数据
					map.put("detailed", areaDataDao.getTerminalAreaData(lcTerminalArea.getTa_id()));
					// 把map放入到result中
					result.add(map);
				}
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	public List<LcTerminalAreaAndDataDBEntity> queryAreaInfoByTerminalId(long[] terminalIds) throws Exception {
		if (terminalIds != null && terminalIds.length > 0) {
			try {
				MySqlConnPoolUtil.startTransaction();
				// 查询终端信息表
				List<LcTerminalAreaAndDataDBEntity> lcTerminalAreas = areaDao.getAreaInfoByTerminalId(terminalIds);
				return lcTerminalAreas;
			} catch (Exception e) {
				// e.printStackTrace();
				throw e;
			} finally {
				MySqlConnPoolUtil.close();
			}
		}
		return null;
	}

	@Override
	public OptResult deleteAreaInfo(long terminalId, int... originalAreaId) throws Exception {
		// 删除区域的同时删除区域数据和相关规则
		OptResult optResult = new OptResult();
		try {
			MySqlConnPoolUtil.startTransaction();
			// 删除区域数据
			areaDataDao.deleteByAreaId(terminalId, originalAreaId);
			// 删除区域相关的规则
			ruleDao.deleteByTermianlIdAndAreaId(terminalId, originalAreaId);
			// 删除区域
			areaDao.deleteAreaInfo(terminalId, originalAreaId);
			optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.success_VALUE);

			MySqlConnPoolUtil.commit();
		} catch (Exception e) {
			MySqlConnPoolUtil.rollback();
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
		return optResult;
	}

	@Override
	public List<LcTerminalRuleDBEntity> queryRegularData(int districtCode, long node_code) throws Exception {
		// 先找到终端信息，然后根据终端信息找到规则
		// from LC_TERMINAL_INFO c ,LC_TERMINAL_RULE
		try {
			MySqlConnPoolUtil.startTransaction();
			List<LcTerminalRuleDBEntity> list = ruleDao.queryRegularData(districtCode, new Long(node_code).intValue());
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	/*
	 * @Override public OptResult saveRegular(List<RegularData> regularDatas) {
	 * OptResult optResult = new OptResult(); try {
	 * MySqlConnPoolUtil.startTransaction(); if
	 * (ruleDao.batchSave(regularDatas)) {
	 * optResult.setStatus(PlatformResponseResult.success_VALUE);
	 * MySqlConnPoolUtil.commit(); } else {
	 * optResult.setStatus(PlatformResponseResult.failure_VALUE);
	 * MySqlConnPoolUtil.rollback(); } } catch (Exception e) {
	 * e.printStackTrace();
	 * optResult.setStatus(PlatformResponseResult.failure_VALUE);
	 * MySqlConnPoolUtil.rollback(); } finally { MySqlConnPoolUtil.close(); }
	 * return optResult; }
	 */

	/*
	 * @Override public OptResult saveAreaInfo(List<AreaInfo> areaInfo) {
	 * OptResult optResult = new OptResult(); try {
	 * MySqlConnPoolUtil.startTransaction(); for (AreaInfo area : areaInfo) {
	 * Boolean result1 = areaDao.addAreaInfo(area); if (!result1) {
	 * optResult.setStatus(PlatformResponseResult.failure_VALUE);
	 * MySqlConnPoolUtil.rollback(); return optResult; } }
	 * optResult.setStatus(PlatformResponseResult.success_VALUE);
	 * MySqlConnPoolUtil.commit(); } catch (Exception e) { e.printStackTrace();
	 * optResult.setStatus(PlatformResponseResult.failure_VALUE);
	 * MySqlConnPoolUtil.rollback(); } finally { MySqlConnPoolUtil.close(); }
	 * return optResult; }
	 */

	@Override
	public OptResult deleteRegular(long terminalId, int regularCode, List<Long> regularIdentifyList) throws Exception {
		try {
			MySqlConnPoolUtil.startTransaction();
			boolean flag = true;
			if (null != regularIdentifyList && regularIdentifyList.size() > 0) {
				for (int i = 0; i < regularIdentifyList.size(); i++) {
					int areaId = regularIdentifyList.get(i).intValue();
					// int count = ruleDao.ruleCount(terminalId, areaId);
					List<LcTerminalRuleDBEntity> ruleList = ruleDao.getRegularData2(terminalId, regularCode, areaId);
					for (LcTerminalRuleDBEntity rule : ruleList) {
						int ruleId = rule.getTr_id();
						// 根据trId删除 area
						LcTerminalAreaDBEntity area = areaDao.getAreaInfoByRuleId(ruleId);
						if (area != null) {
							int taId = area.getTa_id();
							areaDataDao.deleteByTaId(taId);
							areaDao.delete(LcTerminalAreaDBEntity.class, new int[] { taId });
						}
					}
					// 删除规则
					ruleDao.deleteRegularData(terminalId, regularCode, areaId);
				}
			} else {
				ruleDao.deleteRegularData(terminalId, regularCode, 0l);
			}

			MySqlConnPoolUtil.commit();
		} catch (Exception e) {
			MySqlConnPoolUtil.rollback();
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
		return null;
	}

	@Override
	public OptResult deleteRegularByAreaId(long terminalId, int... originalAreaId) throws Exception {
		// 删除与区域相关的规则
		OptResult optResult = new OptResult();
		try {
			MySqlConnPoolUtil.startTransaction();
			/*
			 * if (ruleDao.deleteByTermianlIdAndAreaId(terminalId,
			 * originalAreaId)) {
			 * optResult.setStatus(PlatformResponseResult.success_VALUE);
			 * MySqlConnPoolUtil.commit(); } else {
			 * optResult.setStatus(PlatformResponseResult.failure_VALUE);
			 * MySqlConnPoolUtil.rollback(); }
			 */
			ruleDao.deleteByTermianlIdAndAreaId(terminalId, originalAreaId);
			MySqlConnPoolUtil.commit();
			optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.success_VALUE);
			return optResult;
		} catch (Exception e) {
			optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.failure_VALUE);
			MySqlConnPoolUtil.rollback();
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> queryTerminalRuleRes(long terminalId, int currentPage, int pageSize)
			throws Exception {
		try {
			MySqlConnPoolUtil.startTransaction();
			return ruleDao.getRegularData(terminalId, currentPage, pageSize);
		} catch (Exception e) {
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalAreaDBEntity> queryTerminalAreaRes(long terminalId, int areaId, long start, long end,
			int currentPage, int pageSize) throws Exception {
		try {
			MySqlConnPoolUtil.startTransaction();
			return areaDao.queryTerminalAreaRes(terminalId, areaId, start, end, currentPage, pageSize);
		} catch (Exception e) {
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalAreaDataDBEntity> queryAreaDataRes(long terminalId, int taId) throws Exception {
		try {
			MySqlConnPoolUtil.startTransaction();
			return areaDataDao.getTerminalAreaData(taId);
		} catch (Exception e) {
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public LcTerminalRuleDBEntity queryTerminalRuleById(long terminalId, int trId) throws Exception {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalRuleDBEntity entity = ruleDao.findById(trId, LcTerminalRuleDBEntity.class);
			return entity;
		} catch (Exception e) {
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularData(long terminalId, int areaId) throws Exception {
		try {
			MySqlConnPoolUtil.startTransaction();
			List<LcTerminalRuleDBEntity> entity = ruleDao.getRegularData(terminalId, areaId);
			return entity;
		} catch (Exception e) {
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularDataByAreaIds(long terminalId, int[] areaIds) throws Exception {
		try {
			MySqlConnPoolUtil.startTransaction();
			List<LcTerminalRuleDBEntity> entity = ruleDao.getRegularDataByAreaIds(terminalId, areaIds);
			return entity;
		} catch (Exception e) {
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularDataByAreaIds(List<Long> areaIds) throws Exception {
		try {
			MySqlConnPoolUtil.startTransaction();
			List<LcTerminalRuleDBEntity> entity = ruleDao.getRegularDataByAreaIds(areaIds);
			return entity;
		} catch (Exception e) {
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularDataByTerminalIds(List<Long> terminalId) throws Exception {
		try {
			MySqlConnPoolUtil.startTransaction();
			List<LcTerminalRuleDBEntity> entity = ruleDao.getRegularDataByTerminalIds(terminalId);
			return entity;
		} catch (Exception e) {
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public OptResult saveRegularAndAreaInfo(LCRegularDataSave.RegularDataSave rds) throws Exception {
		try {
			MySqlConnPoolUtil.startTransaction();
			// 规则数据
			List<LCRegularData.RegularData> regularDatas = rds.getDatasList();
			// 区域数据
			List<LCAreaInfo.AreaInfo> areaInfos = rds.getInfosList();
			// System.out.println("regularDatas.size="+regularDatas.size()+",areaInfos.size="+areaInfos.size());
			for (int i = 0; i < regularDatas.size(); i++) {
				LCRegularData.RegularData regularData = regularDatas.get(i);
				if (areaInfos != null && areaInfos.size() == regularDatas.size()) {
					LCAreaInfo.AreaInfo areaInfo = areaInfos.get(i);
					log.error("saveRegularAndAreaInfo(),regularDatas.size=" + regularDatas.size() + ",areaInfos.size="
							+ areaInfos.size() + ",regularData=" + regularData.toString() + ",areaInfo"
							+ areaInfo.toString());
					int trId = ruleDao.saveRegularData(regularData);// 保存RegularData到Rule表返回主键TR_ID
					int taId = areaDao.saveAreaInfo(areaInfo, trId);// 保存AreaInfo到Area表返回TA_ID
					areaDataDao.saveAreaData(areaInfo.getDatasList(), taId);// 保存AreaData
				} else {
					int trId = ruleDao.saveRegularData(regularData);// 保存RegularData到Rule表返回主键TR_ID

				}
			}
			MySqlConnPoolUtil.commit();
			OptResult optResult = new OptResult();
			optResult.setStatus(LCPlatformResponseResult.PlatformResponseResult.success_VALUE);
			return optResult;
		} catch (Exception e) {
			MySqlConnPoolUtil.rollback();
			throw e;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularDatasByRegularCode(LCRegularCode.RegularCode code) {
		try {
			List<LcTerminalRuleDBEntity> list = ruleDao.getRuleByCode(code.getNumber());
			return list;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public List<LcTerminalParaDBEntity> queryTerminalByParaCode(int... paramterCode) throws Exception {
		// TODO Auto-generated method stub
		// 单表操作
				try {
					MySqlConnPoolUtil.startTransaction();
					LcTerminalParaDao lcTerminalParaDao = new LcTerminalParaDaoImpl();
					List<LcTerminalParaDBEntity> list = lcTerminalParaDao.getByCommandCode( paramterCode);
					return list;
				} catch (Exception e) {
					throw e;
				} finally {
					MySqlConnPoolUtil.close();
				}
	}
}

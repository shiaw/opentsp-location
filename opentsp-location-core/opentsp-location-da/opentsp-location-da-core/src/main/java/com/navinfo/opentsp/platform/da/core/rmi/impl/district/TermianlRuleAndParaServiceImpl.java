package com.navinfo.opentsp.platform.da.core.rmi.impl.district;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.persistence.DictManage;
import com.navinfo.opentsp.platform.da.core.persistence.LogManage;
import com.navinfo.opentsp.platform.da.core.persistence.RegularDataAreaAndDataManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.DictManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.LogManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.RegularDataAreaAndDataManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.*;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.*;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.*;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TerminalRuleData;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaData.AreaData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOverTimePark.OverTimePark;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.BusinessType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa.*;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.LcOutRegionLimitSpeedLogEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.OptResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.OutRegionLimitSpeedQuery;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlRuleAndParaService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 根据终端列表获取对应的规则数据和终端参数
 *
 * @author jin_s
 *
 */
@Service
public class TermianlRuleAndParaServiceImpl implements TermianlRuleAndParaService {
	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(TermianlRuleAndParaServiceImpl.class);
	/**
	 * 规则查询
	 */
	private static LcTerminalRuleDao2 ruleDao = new LcTerminalRuleDaoImpl2();
	private LcDistrictAndTileMappingDao districtAndTileMappingDao = new LcDistrictAndTileMappingDaoImpl();
	/**
	 * 终端查询
	 */
	final static RegularDataAreaAndDataManage ruleAreaAndDataManager = new RegularDataAreaAndDataManageImpl();

	public TermianlRuleAndParaServiceImpl()  {
		super();
	}

	/**
	 * 为Dsa模块定制的查询终端列表的规则和参数信息
	 */
	public OptResult queryRuleAndPara(List<Long> terminalIds) {
		try {
			OptResult optResult = new OptResult();
			// 规则和参数信息结果集
			Map<String, Object> ruleAndParaMap = new HashMap<>();
			long terminalId[] = new long[terminalIds.size()];
			for (int i = 0; i < terminalIds.size(); i++) {
				terminalId[i] = terminalIds.get(i);
			}
			// 规则数据
			List<DSATerminalRuleDBEntity> rulelist = ruleDao.getRegularDataForDsa(terminalId);
			if (null != rulelist && rulelist.size() > 0) {
				List<DSATerminalRuleEntity> rulelistReturn = new ArrayList<DSATerminalRuleEntity>(rulelist.size());
				for (DSATerminalRuleDBEntity entity : rulelist) {
					DSATerminalRuleEntity terminalRuleDBEntity = new DSATerminalRuleEntity();
					terminalRuleDBEntity.setTr_id(entity.getTr_id());
					terminalRuleDBEntity.setLast_update_time(entity.getLast_update_time());
					terminalRuleDBEntity.setOriginal_area_id(entity.getOriginal_area_id());
					terminalRuleDBEntity.setLine_id(entity.getOriginal_line_id());
					terminalRuleDBEntity.setRule_content(entity.getRule_content());
					terminalRuleDBEntity.setRule_type(entity.getRule_type());
					terminalRuleDBEntity.setTerminal_id(entity.getTerminal_id());
					rulelistReturn.add(terminalRuleDBEntity);
				}
				ruleAndParaMap.put("terminalRule", rulelistReturn);
			}
			// 参数数据
			LcTerminalParaDao paraDao = new LcTerminalParaDaoImpl();
			List<LcTerminalParaDBEntity> paraList = paraDao.queryTerminalParaResForDsa(terminalId);
			if (null != paraList && paraList.size() > 0) {
				List<LcTerminalParaEntity> paraListReturn = new ArrayList<LcTerminalParaEntity>(paraList.size());
				for (LcTerminalParaDBEntity entity : paraList) {
					LcTerminalParaEntity terminalParaDBEntity = new LcTerminalParaEntity();
					terminalParaDBEntity.setLast_update_time(entity.getLast_update_time());
					terminalParaDBEntity.setPara_code(entity.getPara_code());
					terminalParaDBEntity.setPara_content(entity.getPara_content());
					terminalParaDBEntity.setPara_id(entity.getPara_id());
					terminalParaDBEntity.setTerminal_id(entity.getTerminal_id());
					paraListReturn.add(terminalParaDBEntity);
				}
				ruleAndParaMap.put("terminalParam", paraListReturn);
			}
			// 区域数据
			List<LcTerminalAreaAndDataDBEntity> terminalArea = ruleAreaAndDataManager
					.queryAreaInfoByTerminalId(terminalId);
			List<LcTerminalAreaInfo> LcTerminalAreaInfoList = new ArrayList<LcTerminalAreaInfo>();
			Map<Long, LcTerminalAreaInfo> map = new HashMap<Long, LcTerminalAreaInfo>();
			if (null != terminalArea) {
				// 组装数据
				for (LcTerminalAreaAndDataDBEntity entity : terminalArea) {
					LcTerminalAreaInfo lcTerminalAreaInfo = map.get(entity.getTerminal_id());
					if (lcTerminalAreaInfo == null) {
						lcTerminalAreaInfo = new LcTerminalAreaInfo();
						lcTerminalAreaInfo.setTerminal_id(entity.getTerminal_id());
						lcTerminalAreaInfo.setOriginal_area_id(entity.getOriginal_area_id());
						lcTerminalAreaInfo.setCreate_time(entity.getCreate_time());
						lcTerminalAreaInfo.setArea_type(entity.getArea_type());
						lcTerminalAreaInfo.setTa_id(entity.getTa_id());
						map.put(entity.getTerminal_id(), lcTerminalAreaInfo);
						LcTerminalAreaInfoList.add(lcTerminalAreaInfo);
					}
					LcTerminalAreaInfoData data = new LcTerminalAreaInfoData();
					data.setData_sn(entity.getData_sn());
					data.setData_status(entity.getData_status());
					data.setLatitude(entity.getLatitude());
					data.setLongitude(entity.getLongitude());
					data.setRadius_len(entity.getRadius_len());
					lcTerminalAreaInfo.addreaData(data);
				}
				ruleAndParaMap.put("terminalArea", LcTerminalAreaInfoList);
			}

			// 查询成功
			optResult.setStatus(PlatformResponseResult.success_VALUE);
			optResult.setMap(ruleAndParaMap);
			return optResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据终端ID查询终端对应的规则数据
	 */
	@Override
	public List<LcTerminalRuleEntity> queryTerminalRule(long terminalId) {
		try {
			List<LcTerminalRuleDBEntity> dataList = ruleAreaAndDataManager.queryTerminalRuleRes(terminalId, 0, 0);
			if (CollectionUtils.isNotEmpty(dataList)) {
				// 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
				List<LcTerminalRuleEntity> terminalRuleDBEntityList = new ArrayList<>();
				for (LcTerminalRuleDBEntity entity : dataList) {
					LcTerminalRuleEntity terminalRuleDBEntity = new LcTerminalRuleEntity();
					terminalRuleDBEntity.setTr_id(entity.getTr_id());
					terminalRuleDBEntity.setLast_update_time(entity.getLast_update_time());
					terminalRuleDBEntity.setOriginal_area_id(entity.getOriginal_area_id());
					terminalRuleDBEntity.setRule_content(entity.getRule_content());
					terminalRuleDBEntity.setRule_type(entity.getRule_type());
					terminalRuleDBEntity.setTerminal_id(entity.getTerminal_id());
					// Todo
					terminalRuleDBEntityList.add(terminalRuleDBEntity);

				}
				return terminalRuleDBEntityList;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据终端ID和参数类型协议查询终端对应的参数数据
	 */
	@Override
	public LcTerminalParaEntity queryTerminalPara(long terminalId, int paramterCode) {
		try {
			List<LcTerminalParaDBEntity> dataList = ruleAreaAndDataManager.getTerminalParameter(terminalId,
					paramterCode);
			if (CollectionUtils.isNotEmpty(dataList)) {
				LcTerminalParaDBEntity daEntity = dataList.get(0);

				LcTerminalParaEntity returnEntity = new LcTerminalParaEntity();
				returnEntity.setLast_update_time(daEntity.getLast_update_time());
				returnEntity.setPara_code(daEntity.getPara_code());
				returnEntity.setPara_content(daEntity.getPara_content());
				returnEntity.setPara_id(daEntity.getPara_id());
				returnEntity.setTerminal_id(daEntity.getTerminal_id());
				return returnEntity;
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 */
	@Override
	public List<LcTerminalRuleEntity> queryTerminalRule(List<Long> terminalId, List<Long> areaIds, boolean type) {
		try {
			List<LcTerminalRuleDBEntity> ruleDBEntityList = null;
			if (type) {
				ruleDBEntityList = ruleAreaAndDataManager.getRegularDataByTerminalIds(terminalId);
			} else {
				ruleDBEntityList = ruleAreaAndDataManager.getRegularDataByAreaIds(areaIds);
			}

			if (ruleDBEntityList != null) {
				List<LcTerminalRuleEntity> terminalRuleDBEntityList = new ArrayList<LcTerminalRuleEntity>(
						ruleDBEntityList.size());
				for (LcTerminalRuleDBEntity ruleDBEntity : ruleDBEntityList) {
					// 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
					LcTerminalRuleEntity terminalRuleDBEntity = new LcTerminalRuleEntity();

					terminalRuleDBEntity.setTr_id(ruleDBEntity.getTr_id());
					terminalRuleDBEntity.setLast_update_time(ruleDBEntity.getLast_update_time());
					Long original_area_id = ruleDBEntity.getOriginal_area_id();
					// System.err.println("--ruleDBEntity.getTr_id()->:"+ruleDBEntity.getTr_id()+"original_area_id-->:"+original_area_id);
					if (null == original_area_id) {
						terminalRuleDBEntity.setOriginal_area_id(0l);
					} else {
						terminalRuleDBEntity.setOriginal_area_id(original_area_id);
					}

					terminalRuleDBEntity.setRule_content(ruleDBEntity.getRule_content());
					terminalRuleDBEntity.setRule_type(ruleDBEntity.getRule_type());
					terminalRuleDBEntity.setBusiness_type(ruleDBEntity.getBusiness_type());
					terminalRuleDBEntity.setTerminal_id(ruleDBEntity.getTerminal_id());
					//
					terminalRuleDBEntityList.add(terminalRuleDBEntity);
				}
				return terminalRuleDBEntityList;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据终端ID和区域ID查询终端规则
	 */
	@Override
	public List<LcTerminalRuleEntity> queryTerminalRuleByAreaIds(long terminalId, int[] areaIds) {
		try {
			List<LcTerminalRuleDBEntity> ruleDBEntityList = ruleAreaAndDataManager.getRegularDataByAreaIds(terminalId,
					areaIds);
			if (ruleDBEntityList != null) {
				List<LcTerminalRuleEntity> terminalRuleDBEntityList = new ArrayList<LcTerminalRuleEntity>(
						ruleDBEntityList.size());
				for (LcTerminalRuleDBEntity ruleDBEntity : ruleDBEntityList) {
					// 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
					LcTerminalRuleEntity terminalRuleDBEntity = new LcTerminalRuleEntity();

					terminalRuleDBEntity.setTr_id(ruleDBEntity.getTr_id());
					terminalRuleDBEntity.setLast_update_time(ruleDBEntity.getLast_update_time());
					terminalRuleDBEntity.setOriginal_area_id(ruleDBEntity.getOriginal_area_id());
					terminalRuleDBEntity.setRule_content(ruleDBEntity.getRule_content());
					terminalRuleDBEntity.setRule_type(ruleDBEntity.getRule_type());
					terminalRuleDBEntity.setTerminal_id(ruleDBEntity.getTerminal_id());
					//
					terminalRuleDBEntityList.add(terminalRuleDBEntity);
				}
				return terminalRuleDBEntityList;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public OutRegionLimitSpeedQuery queryOutRegionLimitSpeedLogList(List<Long> terminalIds, long beginDate,
																	long endDate, CommonParameter commonParameter) {
		OutRegionLimitSpeedQuery query = new OutRegionLimitSpeedQuery();
		LogManage logManage = new LogManageImpl();
		List<LcOutRegionLimitSpeedLogEntity> logs = new ArrayList<LcOutRegionLimitSpeedLogEntity>();
		int count = 0;
		if (null != commonParameter && !commonParameter.isMultipage()) {
			commonParameter.setPageSize(Integer.MAX_VALUE);
		}
		for (long terminalId : terminalIds) {
			int c = logManage.getCount(terminalId, 0, beginDate, endDate);
			count = count + c;
			List<LcOutRegionLimitSpeedLogDBEntity> entities = logManage.queryOutRegionLimitSpeedLogList(terminalId, 0,
					beginDate, endDate, commonParameter.getPageIndex(), commonParameter.getPageSize());
			LcOutRegionLimitSpeedLogEntity log = null;
			for (LcOutRegionLimitSpeedLogDBEntity entity : entities) {
				log = new LcOutRegionLimitSpeedLogEntity();
				log.setId(entity.getId());
				log.setTerminal_id(entity.getTerminal_id());
				log.setOriginal_area_id(entity.getOriginal_area_id());
				log.setLimit_date(entity.getLimit_date());
				log.setLimit_speed(entity.getLimit_speed());
				log.setSign(entity.getSign());
				logs.add(log);
			}
		}
		query.setStatusCode(PlatformResponseResult.success_VALUE);
		query.setTotalRords(count);
		query.getDataList().addAll(logs);
		return query;
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public int getDictByCode(int dictCode)  {
		DictManage manage = new DictManageImpl();
		LcDictDBEntity entity = manage.getDictByCode(dictCode);
		if (null == entity) {
			logger.error("数据库中查不到字典编码为" + dictCode + "的字典值。请检查数据库。");
			return 0;
		}
		String dict_data = entity.getDict_data();
		int dictValue = Integer.parseInt(dict_data);
		return dictValue;
	}

	@Override
	public Map<Long, Integer[]> getDistrictAndTileMap()  {
		List<LcDistrictAndTileMappingDBEntity> list = districtAndTileMappingDao.getDistrictAndTileMappingList();
		Map<Long, Integer[]> map = new HashMap<Long, Integer[]>();
		for (LcDistrictAndTileMappingDBEntity entity : list) {
			Integer[] district = new Integer[2];
			district[0] = entity.getDistrict_id();
			Integer parent = entity.getParent_district_id();
			if (null == parent) {
				district[1] = 0;
			} else {
				district[1] = parent;
			}
			map.put(entity.getTile_id(), district);
		}
		return map;
	}

	@Override
	public Map<Long, Integer[]> getDistrictAndTileMapPage(int pageNum, int pagesize)  {
		List<LcDistrictAndTileMappingDBEntity> list = districtAndTileMappingDao.getDistrictAndTileMappingListPage(
				pageNum, pagesize);
		Map<Long, Integer[]> map = new HashMap<Long, Integer[]>();
		for (LcDistrictAndTileMappingDBEntity entity : list) {
			Integer[] district = new Integer[2];
			district[0] = entity.getDistrict_id();
			Integer parent = entity.getParent_district_id();
			if (null == parent) {
				district[1] = 0;
			} else {
				district[1] = parent;
			}
			map.put(entity.getTile_id(), district);
		}
		logger.error("瓦片加载页数："+pageNum + "  加载个数：    "+map.size());
		return map;
	}

	@Override
	public List<AreaInfo> getAreaInfoForStatistic()  {
		LcTerminalRuleDao2 rule = new LcTerminalRuleDaoImpl2();
		LcTerminalAreaDao2 area = new LcTerminalAreaDaoImpl2();
		List<LcTerminalRuleDBEntity> rules = rule.getRuleByType(RegularType.statistic_VALUE);
		if (null == rules || rules.size() == 0) {
			return null;
		}
		int[] ruleIds = new int[rules.size()];
		for (int i = 0; i < rules.size(); i++) {
			LcTerminalRuleDBEntity entity = rules.get(i);
			ruleIds[i] = entity.getTr_id();
		}
		List<LcTerminalAreaAndDataDBEntity> areas = area.getAreaInfosByRuleIds(ruleIds);
		Map<Integer, AreaInfo.Builder> allAreas = new HashMap<Integer, AreaInfo.Builder>(rules.size());
		for (LcTerminalAreaAndDataDBEntity entity : areas) {
			Integer ta_id = entity.getTa_id();
			if (allAreas.containsKey(ta_id)) {
				AreaInfo.Builder areaInfo = allAreas.get(ta_id);
				AreaData.Builder areaData = AreaData.newBuilder();
				areaData.setDataSN(entity.getData_sn());
				areaData.setDataStatus(entity.getData_status());
				areaData.setLatitude(entity.getLatitude());
				areaData.setLongitude(entity.getLongitude());
				areaData.setRadiusLength(entity.getRadius_len());
				areaInfo.addDatas(areaData.build());
			} else {
				AreaInfo.Builder areaInfo = AreaInfo.newBuilder();
				areaInfo.setAreaIdentify(entity.getOriginal_area_id());
				areaInfo.setTypes(AreaType.valueOf(entity.getArea_type()));
				AreaData.Builder areaData = AreaData.newBuilder();
				areaData.setDataSN(entity.getData_sn());
				areaData.setDataStatus(entity.getData_status());
				areaData.setLatitude(entity.getLatitude());
				areaData.setLongitude(entity.getLongitude());
				areaData.setRadiusLength(entity.getRadius_len());
				areaInfo.addDatas(areaData.build());
				allAreas.put(entity.getTa_id(), areaInfo);
			}
		}
		List<AreaInfo> result = new ArrayList<AreaInfo>(rules.size());
		Set<Integer> keySet = allAreas.keySet();
		for (Integer key : keySet) {
			result.add(allAreas.get(key).build());
		}
		if(result.size()>0){
			logger.error(">>>dsa初始化加载[服务站缓存]条数： "+result.size());
		}
		return result;
	}

	@Override
	public Map<Long, Integer> getRegularDatasByRegularCode(RegularCode code)  {
		Map<Long, Integer> result = new HashMap<Long, Integer>();
		RegularDataAreaAndDataManage manager = new RegularDataAreaAndDataManageImpl();
		List<LcTerminalRuleDBEntity> entities = manager.getRegularDatasByRegularCode(code);
		if (CollectionUtils.isNotEmpty(entities)) {
			for (LcTerminalRuleDBEntity obj : entities) {
				try {
					if (code.getNumber() == obj.getBusiness_type()) {
						Long area_id = obj.getOriginal_area_id();
						OverTimePark overTimePark = OverTimePark.parseFrom(obj.getRule_content());
						if (overTimePark != null) {
							int overtimeLimit = overTimePark.getOvertimeLimit();
							result.put(area_id, overtimeLimit);
						}
					}
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
			}
			logger.error(">>>dsa初始化加载[滞留超时]规则条数： "+result.size());
		}
		return result;
	}

	@Override
	public List<TerminalInfo> getAllTerminalInfo()  {
		LcTerminalInfoDao lcTerminalInfoDao = new LcTerminalInfoDaoImpl();
		List<LcTerminalInfoDBEntity> list = lcTerminalInfoDao.getAllTerminal();
		List<TerminalInfo> lInfos = new ArrayList<>();
		if (list != null) {
			for (LcTerminalInfoDBEntity entity : list) {
				TerminalInfo.Builder build = TerminalInfo.newBuilder();
				build.setTerminalId(entity.getTerminal_id() == null ? 0 : entity.getTerminal_id());
				build.setChangeTid(entity.getChange_tid() == null ? 0 : entity.getChange_tid());
				build.setDeviceId(entity.getDevice_id() == null ? "" : entity.getDevice_id());
				build.setNodeCode(entity.getNode_code() == null ? 0 : entity.getNode_code());
				build.setProtocolType(entity.getProto_code() == null ? 0 : entity.getProto_code());
				build.setRegularInTerminal(entity.getRegular_in_terminal() == 1 ? true : false);
				build.setType(entity.getBusiness_type() == 1 ? BusinessType.jianghuai : BusinessType.defaultType);
				lInfos.add(build.build());
			}
			logger.error(">>>dsa初始化加载[终端缓存]规则条数： "+list.size());
		}
		return lInfos;
	}

	@Override
	public Map<Long, Long> getAlarmRule()  {
		// TODO Auto-generated method stub
		return TerminalRuleData.getTerminalRuleFromStaticRedis();
	}

	@Override
	public boolean AlarmCancleOrNot(long tid, long ruleid, boolean cancleorNot)  {
		// TODO Auto-generated method stub
		return TerminalRuleData.SaveOrDelAlarmRule(tid, ruleid, cancleorNot);

//		return false;
	}

	@Override
	public Map<String, Object> queryTerminalByParaCode(int... paramterCode)  {
		try {
			Map<String, Object> map = new ConcurrentHashMap<String, Object>();
			List<LcTerminalParaDBEntity> dataList = ruleAreaAndDataManager.queryTerminalByParaCode(paramterCode);
			if (CollectionUtils.isNotEmpty(dataList)) {
				for (LcTerminalParaDBEntity lcTerminalParaDBEntity : dataList) {
					map.put(lcTerminalParaDBEntity.getTerminal_id()+"_"+lcTerminalParaDBEntity.getPara_code(), lcTerminalParaDBEntity.getPara_content());
				}
			}

			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

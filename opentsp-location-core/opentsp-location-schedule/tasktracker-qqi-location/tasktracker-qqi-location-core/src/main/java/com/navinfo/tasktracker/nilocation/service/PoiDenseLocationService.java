package com.navinfo.tasktracker.nilocation.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.protocol.common.LCConcentratedRealTimeData;
import com.navinfo.opentsp.platform.location.protocol.common.RealTimeDataPb;
import com.navinfo.tasktracker.nilocation.entity.ResultCode;
import com.navinfo.tasktracker.nilocation.entity.SuccessResponse;
import com.navinfo.tasktracker.nilocation.util.BathUpdateOptions;
import com.navinfo.tasktracker.nilocation.util.MongoManager;
import com.navinfo.tasktracker.nilocation.util.MongodbUtil;
import com.navinfo.tasktracker.nilocation.util.RedisDaoTem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.stereotype.Service;
import redis.clients.util.SafeEncoder;
import java.util.*;


/**
 * 内存中维护区域信息
 *@author zhangyue
 */
@Service
public class PoiDenseLocationService
{
	protected static final Logger logger = LoggerFactory.getLogger(PoiDenseLocationService.class);

	/**
	 * 单独存终端id的key
	 * */
	private final String TERMINAL_ID = "terminal_id";
	@Autowired
	private RedisDaoTem redisDaoTem;

	@Autowired
	private MongoManager mongoManager;

	private final String VEHICLE_CHANGE_OIL = "concentrated_data_";

	private final String CONCENTRATED = "concentrated_";

	private final int num = 10;

	@Value("${mongodb.master.db}")
	private String mongodbDB;

	@Value("${time.outs:3600}")
	private long timeOut;

	@Value("${redisNum.numMax:3600}")
	private long numMax;

	@Value("${mongodbNum.inUpNum:50}")
	private long inUpNum;

	@Value("${redisNum.team:1000}")
	private long teamNum;
	/**
	 *
	 * @return
	 */
	public SuccessResponse poiLocation()
	{
		SuccessResponse result = new SuccessResponse(ResultCode.SUCCESS, "OK");
		try {
			logger.info("====================================PoiLocation start==================================");
			Set<String> setId = redisDaoTem.sMembers(this.TERMINAL_ID);
			logger.info("PoiDenseLocationService: TERMINAL_ID end"  + setId.size());
 			if(setId != null && setId.size() > 0) {
				List lis = new ArrayList(setId);

				Set<String> allTridNum = new HashSet();
				//取出所有tmid数量
				Map<String, Long> mapList = redisDaoTem.zSetCarALL(lis);
				logger.info("PoiDenseLocationService: zSetCarALL end"  + lis.size());
				if(mapList == null){
					return null;
				}
				for(Map.Entry<String, Long> entry  : mapList.entrySet()){
					//如果redis数据大于3600放入集合
					if ( entry.getValue() > numMax ){
						allTridNum.add(entry.getKey());
					}
				}
				//取差集
				setId.removeAll(allTridNum);

				List listId = new ArrayList(setId);
				Map<String, Set<RedisZSetCommands.Tuple>> setMap = redisDaoTem.zRevRangeByScoreALL(listId, 0, 0);
				logger.info("PoiDenseLocationService: zRevRangeByScoreALL end" + setMap.size());
				Set<String> allTridTime = new HashSet();

				for (Map.Entry<String,Set<RedisZSetCommands.Tuple>> entry  : setMap.entrySet()) {
					try {
						Set<RedisZSetCommands.Tuple> setCont = entry.getValue();
						Iterator<RedisZSetCommands.Tuple> it = setCont.iterator();
						//第一个点
						RedisZSetCommands.Tuple firType = it.next();
						//系统时间
						long systemMillis = System.currentTimeMillis() / 1000;
						//存mongodb开始时间
						long firScoreMillis = firType.getScore().longValue();
						//系统时间比score时间大于一小时
						logger.info("PoiDenseLocationService systetime:" + systemMillis + "firtime:" + firScoreMillis + "timeout:" + timeOut);
						if ((systemMillis - firScoreMillis) >= timeOut) {
							allTridTime.add(entry.getKey());
						}
					}catch (Exception e){
						logger.error("PoiDenseLocationService zRevRangeByScoreALL" + e.getMessage());
					}
	 			}
				//大于3600和大于一小时的合集
				allTridTime.addAll(allTridNum);
				List listTimeNum = new ArrayList(allTridTime);

				List listTemp = new ArrayList();
				for(int j = 0 ;j < listTimeNum.size(); j++) {
					listTemp.add(listTimeNum.get(j));
					if((j+1) % teamNum == 0) {
						logger.info("PoiDenseLocationService: zRangeByScoreWithScoresAll 500 start"  + listTimeNum.size());
						Map<String, Set<RedisZSetCommands.Tuple>> allSetCont = redisDaoTem.zRangeByScoreWithScoresAll(listTemp, 0, System.currentTimeMillis() / 1000);
						if (allSetCont != null && allSetCont.size() > 0) {
							this.saveMongodb(allSetCont);
							listTemp = new ArrayList();
							logger.info("PoiDenseLocationService: saveMongodb end" + listTemp.size());
						}
					}else if(j == (listTimeNum.size()-1)){
						logger.info("PoiDenseLocationService: zRangeByScoreWithScoresAll not 500 start"  + listTimeNum.size());
						Map<String, Set<RedisZSetCommands.Tuple>> allSetCont = redisDaoTem.zRangeByScoreWithScoresAll(listTemp, 0, System.currentTimeMillis() / 1000);
						if (allSetCont != null && allSetCont.size() > 0) {
							this.saveMongodb(allSetCont);
							listTemp = new ArrayList();
							logger.info("PoiDenseLocationService: saveMongodb end" + listTemp.size());
						}
					}
				}
			}

			logger.info("====================================PoiLocation end==================================");
		}catch(Exception e){
			logger.error("PoiDenseLocationService" + e.getMessage());
		}finally {
			MongoManager.close();
		}

		return result;
	}
	public void saveMongodb(Map<String, Set<RedisZSetCommands.Tuple>> allSetCont){

		List<BathUpdateOptions> listOptionsTrip = new ArrayList<BathUpdateOptions>();
		List<Long> minList = new ArrayList<>();
		List<Long> maxList = new ArrayList<>();
		List<String> tiIdList = new ArrayList<>();
		int iuNumOut = 0;
		//如果小keyid存缓存里
		for (Map.Entry<String,Set<RedisZSetCommands.Tuple>> entry : allSetCont.entrySet()) {
			try {
				if(entry.getValue() == null){
					continue;
				}
				Set<RedisZSetCommands.Tuple> allTemp = entry.getValue();
				long firScoreMillis= 0;
				List<RedisZSetCommands.Tuple> list = new ArrayList<>(allTemp);
				if(list == null || list.size() <= 0){
					continue;
				}
				//最后一个点redis的score时间结束时间
				LinkedHashMap<String,byte[]> mapUt = new LinkedHashMap<>();
				//存mongodb结束时间
				long lastScoreMillis = 0;
				//删除redis开始时间
				long lastRemoveMillis = 0;
				//删除redis结束时间
				long firRemoveMillis = 0;
				int listNum = list.size();
				if(listNum > 0 && list.get(0) != null){
					RedisZSetCommands.Tuple firTyple = list.get(0);
					firRemoveMillis = firTyple.getScore().longValue();
					RedisZSetCommands.Tuple lastTyple = list.get(listNum -1);
					lastRemoveMillis = lastTyple.getScore().longValue();
				}
				for(int i = 0;i < listNum; i ++) {
					RedisZSetCommands.Tuple reTyple = list.get(i);
					if(reTyple == null){
						continue;
					}
					LCConcentratedRealTimeData.SpecialRealTimeData concentratedRealTimeData = LCConcentratedRealTimeData.SpecialRealTimeData.
							parseFrom(Convert.hexStringToBytes(SafeEncoder.encode(reTyple.getValue())));
					LCConcentratedRealTimeData.GpsLocationData gpsLocationData = concentratedRealTimeData.getGpsLocationData();
					List<LCConcentratedRealTimeData.RealTimeDataUnit> realTimeDataUnit = concentratedRealTimeData.getRealTimeDataUnitList();

					int time = 0;
					if(realTimeDataUnit != null && realTimeDataUnit.size() > 0){
						time = 1000 / realTimeDataUnit.size();
					}else{
						continue;
					}
					for(int j = 0;j < realTimeDataUnit.size(); j++){
						RealTimeDataPb.RealTimeData.Builder realBuilder = RealTimeDataPb.RealTimeData.newBuilder();
						long gpsTime = gpsLocationData.getGpsTime() * 1000 + time * j;
						if(i == 0 && j == 0){
							firScoreMillis = gpsTime;
						}
						if(i == (listNum - 1) && j == (realTimeDataUnit.size() -1)){
							lastScoreMillis = gpsTime;
						}
						realBuilder.setGpsTime(gpsTime);
						realBuilder.setLatitude(gpsLocationData.getLatitude());
						realBuilder.setLongitude(gpsLocationData.getLongitude());
						realBuilder.setHeight(gpsLocationData.getHeight());
						realBuilder.setEngineOutputTorque(realTimeDataUnit.get(j).getEngineOutputTorque());
						realBuilder.setSpeed(realTimeDataUnit.get(j).getSpeed());
						realBuilder.setAccelerator(realTimeDataUnit.get(j).getAccelerator());
						realBuilder.setBrake(realTimeDataUnit.get(j).getBrake());
						realBuilder.setRotation(realTimeDataUnit.get(j).getRotation());
						realBuilder.setGear(realTimeDataUnit.get(j).getGear());
						realBuilder.setClutchSwitch(realTimeDataUnit.get(j).getClutchSwitch());
						realBuilder.setRealTimeOilConsumption(realTimeDataUnit.get(j).getRealTimeOilConsumption());
						realBuilder.setFuelConsumptionRate(realTimeDataUnit.get(j).getFuelConsumptionRate());
						mapUt.put(gpsTime + "",realBuilder.build().toByteArray());
					}
				}
				String terminalId = "";
				if(entry.getKey() != null && entry.getKey().split("_").length > 0){
					terminalId = entry.getKey().split("_")[1];
				}else{
					continue;
				}
				String tableName = getMonth(firScoreMillis);
				DBCollection dbCollection = mongoManager.start(mongodbDB, VEHICLE_CHANGE_OIL + tableName);
				Criteria criteria11 = new Criteria();
				criteria11.andOperator(Criteria.where("terminalId").is(terminalId), Criteria.where("beginDate").is(firScoreMillis));
				Query query = new Query(criteria11);
				Update update = new Update();
				update.set("endDate", lastScoreMillis);
				update.set("data", mapUt);
				listOptionsTrip.add(new BathUpdateOptions(query, update, true, true));
				minList.add(firRemoveMillis);
				maxList.add(lastRemoveMillis);
				tiIdList.add(CONCENTRATED + terminalId);
				if ((iuNumOut + 1) % inUpNum == 0) {
					logger.info("PoiDenseLocationService iuNumOut if:" + iuNumOut);
					int re = MongodbUtil.bathUpdate(dbCollection, VEHICLE_CHANGE_OIL + tableName, listOptionsTrip);
					if(re > 0) {
						redisDaoTem.removeRangeByScoreAll(tiIdList, minList, maxList);
					}
					listOptionsTrip.clear();
					minList.clear();
					maxList.clear();
					tiIdList.clear();
				} else if (iuNumOut == (allSetCont.size() - 1)) {
					logger.info("PoiDenseLocationService iuNumOut else:" + iuNumOut);
					int re = MongodbUtil.bathUpdate(dbCollection, VEHICLE_CHANGE_OIL + tableName, listOptionsTrip);
					if(re > 0) {
						redisDaoTem.removeRangeByScoreAll(tiIdList, minList, maxList);
					}
					listOptionsTrip.clear();
					minList.clear();
					maxList.clear();
					tiIdList.clear();
				}
				iuNumOut ++;
			}catch (Exception e){
				listOptionsTrip.clear();
				minList.clear();
				maxList.clear();
				tiIdList.clear();
				logger.error(e.getMessage());
			}
		}
	}
	/**
	 * 获取当前天
	 */
	public String getMonth(long time){
		try {
			//先解析给定的时间
			Date da = new Date(time);
			Calendar c = Calendar.getInstance();
			c.setTime(da);
			int re = c.get(Calendar.MONTH) + 1;
			String s = "";
			if(re < num){
				s = "0" + re;
			}else{
				s = "" + re;
			}

			return (c.get(Calendar.YEAR)) + s;
		}catch (Exception e){
			logger.error(e.getMessage() + "OilingStealOilServiceImpl upperMonth");
			return "";
		}
	}

}

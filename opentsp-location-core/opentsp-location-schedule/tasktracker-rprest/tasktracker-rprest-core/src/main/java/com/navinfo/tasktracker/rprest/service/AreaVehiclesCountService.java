package com.navinfo.tasktracker.rprest.service;

import com.alibaba.fastjson.JSON;

import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.tasktracker.rprest.domain.LcDistrictAndTileMapping;
import com.navinfo.tasktracker.rprest.entity.AreaDataEntity;
import com.navinfo.tasktracker.rprest.entity.AreaVehiclesEntity;
import com.navinfo.tasktracker.rprest.entity.MemoryData;
import com.navinfo.tasktracker.rprest.entity.SuccessResponse;
import com.navinfo.tasktracker.rprest.util.JsonUtil;
import com.navinfo.tasktracker.rprest.util.VehiclePassTimesQuadtree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 内存中维护区域信息
 *
 */
@Service
public class AreaVehiclesCountService
{
	protected static final Logger logger = LoggerFactory.getLogger(AreaVehiclesCountService.class);

	@Autowired
	private RedisCommonService redisService;

	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	/**
	 * 关闭会话接口
	 *
	 * @return
	 */
	public SuccessResponse areaVehiclesExecute()
	{
		logger.info("====================================AreaVehiclesExecute Start==================================");
		//所有车
		Map<byte[], byte[]> dataMap = redisService.hGetAll("LASTEST_COMBINATION_LOCATION_DATA");

		int redisDataSize = dataMap.size();
		logger.info("redis LASTEST_COMBINATION_LOCATION_DATA mapSize:{}", redisDataSize);
		Map<String, Integer> provincecountMap= new HashMap();
		Map<String, Integer> citycountMap= new HashMap();
		try {
			if (redisDataSize == 0) {
				logger.info("redis LASTEST_COMBINATION_LOCATION_DATA no data, return!");
			}
			for (Map.Entry<byte[], byte[]> entry : dataMap.entrySet()) {
				//String vid = new String(entry.getKey());

				LCLocationData.LocationData locationData = LCLocationData.LocationData.parseFrom(entry.getValue());
				// 1、从redis中的轨迹点数据中获取经纬度
				int longitude = locationData.getLongitude();
				int latitude = locationData.getLatitude();
                Date now = new Date();
				long diff = now.getTime()/1000 - locationData.getGpsDate();
                // 计算差多少分钟


                // 末次位置大于10分钟的丢弃
                if (diff > 600) {
                    continue;
                }
				// 2、根据经纬度，通过已有方法，获取瓦片
				long tileId15 = VehiclePassTimesQuadtree.getTileNumber(latitude, longitude, 15);
				logger.info("AreaVehiclesExecutes longitude:{}, latitude:{}, tileId15:{}", longitude, latitude, tileId15);

				LcDistrictAndTileMapping are = MemoryData.AreaInfoM.get(tileId15);
				if (are != null) {
                    String provinceCode;
                    if (are.getParentDistrictId()==0){
                        provinceCode = String.valueOf(are.getDistrictId());
                    } else {
                        provinceCode = String.valueOf(are.getParentDistrictId());
                    }

					// 保存省、市车次的统计结果到临时MAP中，统计存储到redis
                    if (provincecountMap.get(provinceCode) != null){
                        provincecountMap.put(provinceCode, provincecountMap.get(provinceCode) + 1);
                    } else {
                        provincecountMap.put(provinceCode, 1);
                    }

                    /*if (citycountMap.get(cityCode) != null){
                        citycountMap.put(cityCode, citycountMap.get(cityCode) + 1);
                    } else {
                        citycountMap.put(cityCode, 1);
                    }*/
					//saveVehicle(MemoryData.provCountMap, provinceCode, vid);
					//saveVehicle(MemoryData.cityCountMap, cityCode,vid);
				}

			}

			// 存储省份的车次结果
			//saveVehicleCount("VEHICLE_COUNT_PROV", MemoryData.provCountMap);
			// 存储地市的车次结果
			//saveVehicleCount("VEHICLE_COUNT_CITY", MemoryData.cityCountMap);

            stringRedisTemplate.opsForValue().set("VEHICLE_COUNT_PROV", JsonUtil.toJson(provincecountMap));
			//redisTemplate.opsForHash().putAll("VEHICLE_COUNT_CITY", citycountMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SuccessResponse result = new SuccessResponse(200, "OK");
		logger.info("====================================AreaVehiclesExecute end==================================");
		return result;
	}

	/**
	 * 保存最后的统计结果
	 */
	/*private void saveVehicleCount(String redisKey, Map<String, Set<String>> memoryMap) {
		if (memoryMap.isEmpty()) {
			logger.info("AreaVehiclesExecutesvid memoryMap:{} is empty, do not save!", redisKey);
			return;
		}
		// 排除原有数据在新数据中不存在的KEY
		Set<String> oldKeys = redisService.hkeys(redisKey);
		Set<String> newKeys = memoryMap.keySet();
		logger.info("AreaVehiclesExecutesvid redisKey:{}, oldKeys:{}, newKeys:{}", redisKey, oldKeys, newKeys);
		oldKeys.removeAll(newKeys);
		if (!oldKeys.isEmpty()) {
			String[] mapKeyArray = new String[oldKeys.size()];
			redisService.hDel(redisKey, oldKeys.toArray(mapKeyArray));
		}

		Map<String, byte[]> saveMap = new HashMap();
		for (String key : newKeys) {
			try {
				Set<String> valueSet = memoryMap.get(key);;

                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                if(valueSet != null) {
                    DataOutputStream dout = new DataOutputStream(bout);
                    int size = valueSet.size();
                    dout.writeInt(size);
                    for (String s : valueSet) {
                        dout.writeUTF(s);
                    }
                }
                byte[] bytes = bout.toByteArray();
                bout.close();
				saveMap.put(key, bytes);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

		redisService.hMSetForBytes(redisKey, saveMap);
		logger.info("AreaVehiclesExecutesvid redisKey:{}, remove no exist oldKeySize:{}, save new MapSize:{}", redisKey, oldKeys.size(), saveMap.size());
	}*/

}

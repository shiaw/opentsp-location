package com.navinfo.opentsp.platform.rprest.service;

import com.navinfo.opentsp.platform.rprest.dto.CarsInDistrictOnlineNumDto;
import com.navinfo.opentsp.platform.rprest.utils.JsonUtil;
import com.navinfo.opentspcore.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * com.navinfo.opentsp.platform.rprest.service
 *
 * @author zhangdong
 * @date 2017/11/29
 */
@Service
public class CarsCurrentOnlineRankService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public CarsInDistrictOnlineNumDto[] provorder() throws IOException {

        Map provCountMap = JsonUtil.toMap(stringRedisTemplate.opsForValue().get("VEHICLE_COUNT_PROV"));
        // 判provCountMap空
        List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(
                provCountMap.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }
        });
        CarsInDistrictOnlineNumDto[] carsInDistrictOnlineNumDtos = new CarsInDistrictOnlineNumDto[infoIds.size()];
        //int max = infoIds.size()<6?infoIds.size():6;
        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, Integer> ent = infoIds.get(i);
            carsInDistrictOnlineNumDtos[i] = new CarsInDistrictOnlineNumDto();
            carsInDistrictOnlineNumDtos[i].setNumber(ent.getValue());
            carsInDistrictOnlineNumDtos[i].setDistrict(Integer.valueOf(ent.getKey()));
        }

        return carsInDistrictOnlineNumDtos;
    }


}

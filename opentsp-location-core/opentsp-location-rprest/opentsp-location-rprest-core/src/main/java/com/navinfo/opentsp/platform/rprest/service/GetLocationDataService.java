package com.navinfo.opentsp.platform.rprest.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.rprest.cache.LastestLocationCache;
import com.navinfo.opentsp.platform.rprest.cache.RedisLastLocationStorage;
import com.navinfo.opentsp.platform.rprest.dto.GetLocationDataDto;
import com.navinfo.opentsp.platform.rprest.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hxw on 2017/5/23.
 */
@Component
public class GetLocationDataService {
    protected static final Logger logger = LoggerFactory.getLogger(GetLocationDataService.class);

    @Autowired
    private RedisLastLocationStorage redisLastLocationStorage;


    /**
     * 获取最新的末次位置
     */
    public List<GetLocationDataDto> getLocationDataDtos(){
        List<GetLocationDataDto> list = LastestLocationCache.getInstance().getCache("location");
        RedisUtil util = new RedisUtil();
        if(list!=null && list.size()>0){
            return list;
        }
        list = new ArrayList<>();
        //查询末次位置redis，获取所有终端的末次位置，并提取经纬度点
        Map<byte[], byte[]> data = (Map<byte[], byte[]>) redisLastLocationStorage.getAll();
        for(Map.Entry<byte[],byte[]>  entry : data.entrySet()){
            String key = util.byte2string(entry.getKey());
            LCLocationData.LocationData locationData = null;
            try {
                locationData = LCLocationData.LocationData.parseFrom(entry.getValue());
                GetLocationDataDto dto = new GetLocationDataDto();
                dto.setGpsDate(locationData.getGpsDate());
                dto.setLat(((double)locationData.getLatitude())/1000000);
                dto.setLng((double)locationData.getLongitude()/1000000);
                dto.setTid(Long.parseLong(key));
                list.add(dto);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }
        LastestLocationCache.getInstance().addCache(list);
        return list;
    }
}

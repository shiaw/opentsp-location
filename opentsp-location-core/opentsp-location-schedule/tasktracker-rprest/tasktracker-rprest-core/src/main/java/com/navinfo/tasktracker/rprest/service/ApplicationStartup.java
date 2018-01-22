package com.navinfo.tasktracker.rprest.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.tasktracker.rprest.dao.LcDistrictAndTileMappingMapper;
import com.navinfo.tasktracker.rprest.domain.LcDistrictAndTileMapping;
import com.navinfo.tasktracker.rprest.entity.AreaVehiclesEntity;
import com.navinfo.tasktracker.rprest.entity.MemoryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 */
@Component
public class ApplicationStartup implements CommandLineRunner {

    protected static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    @Autowired
    private LcDistrictAndTileMappingMapper lcDistrictAndTileMappingMapper;
    @Override
    public void run(String... args) throws Exception {
        logger.info(">>>>>>>>>>>>>>>task data start  <<<<<<<<<<<<<");

        List<LcDistrictAndTileMapping> areList = lcDistrictAndTileMappingMapper.selectAll();
        MemoryData.addAreaData(areList);

        /*Pipeline pipeline = jedis.pipelined();
        LCLocationData.LocationData baseData = null;
        try {
            String baseDataPb = "CAAQgYAwGAAgACgAMAA4AEAASABQ5Nyq0AVYAGDm3KrQBWgAcAB4AKABAKgBAbgBAIIC9gIKBQiuARAACgUIJBCQDQoFCB8QhAcKDQhaEJSm/////////wEKDQghEPim/////////wEKBggPEJbGAgoFCDMQvgUKBgiyARCEawoGCLMBEPQbCgUINhCwbQoFCBcQ6BEKBQgYEIgOCggIMRDo9Z2gYQoNCAsQm9rmsZz/////AQoECAoQZAoICB4QuuaVvX0KBQgSEMBXCgQIJxAACgUIMxD/JwoECCoQAAoECCkQAAoGCBsQvYcHCgUIMhD4IwoFCBwQ3CQKBQgGEIIpCgYIBxDoyQMKBggIEKjWAwoFCFkQ2DYKBQgBEPIlCgYIAhCQmQEKBQgDEIwuCgYIBBCgnAEKBggFEPqwAwoHCIwBEIGPBgoGCDkQi6MGCgQIDBBkCgoIGRDQvq+UkuUFCgoIGhCg5u2gxNkhCgUIDhCQMAoFCA4Q0C0KDgiPARDd4fXPuP////8BCg4IkAEQj8a+4bv/////AQoFCKQBEAEKBgicARChBJICCAAAAAAAAADomgIMGAAoADgASABQAGAAoAIAsAIAugJOCAAQABgAIAAoADUAAAAAPQAAAABAAEgAUABYAGUAAAAAbQAAAABwAH0AAAAAgAEAiAEAkAEAnQEAAAAApQEAAAAAqAEAsAEAuAEAwAEA1QIDAX/N3QIAAAAA6AIA8gIA";
            baseData = LCLocationData.LocationData.parseFrom(Base64.getDecoder().decode(baseDataPb));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        Map map = new HashMap();
        for (int i=0;i<1000;i++){
            LCLocationData.LocationData.Builder locationDataBuilder = baseData.toBuilder();
            System.out.println(locationDataBuilder.getLatitude());
            System.out.println(locationDataBuilder.getLongitude());
            locationDataBuilder.setGpsDate(new Date().getTime()/1000);
            locationDataBuilder.setLongitude(127732404);
            locationDataBuilder.setLatitude(47004694);
            locationDataBuilder.build().toByteArray();
            pipeline.hset(string2byte("LASTEST_COMBINATION_LOCATION_DATA_BAK"),
                    string2byte(String.valueOf(i)),
                    locationDataBuilder.build().toByteArray());
        }
        pipeline.syncAndReturnAll();*/

        logger.info(">>>>>>>>>>>>>>>task data end  <<<<<<<<<<<<<");
    }

    public byte[] string2byte(String string) {
        try {
            return string.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}

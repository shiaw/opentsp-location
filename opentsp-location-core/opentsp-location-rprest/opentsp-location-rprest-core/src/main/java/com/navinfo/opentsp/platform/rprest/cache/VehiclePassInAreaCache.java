package com.navinfo.opentsp.platform.rprest.cache;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlRuleAndParaService;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCVehiclePassInAreaInfo.*;
import com.navinfo.opentsp.platform.rprest.dto.GetLocationDataDto;
import com.navinfo.opentsp.platform.rprest.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.rprest.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by HOUQL on 2017/5/25.
 */
@Component
public class VehiclePassInAreaCache {
    private Map<Integer, VehiclePassInAreaInfo> pssInAreaMap = new ConcurrentHashMap<>();
    private Logger logger = LoggerFactory.getLogger(VehiclePassInAreaCache.class);
    @Autowired
    RedisLastLocationStorage redisLastLocationStorage;
    private Object mux = new Object();
    private void initPassInArea() {
        try {
            logger.info("开始加载[服务站周边缓存]...");
            RedisUtil util = new RedisUtil();
            Map<byte[], byte[]> data = (Map<byte[], byte[]>) redisLastLocationStorage.getAllPassTimes();
            for(Map.Entry<byte[],byte[]>  entry : data.entrySet()){
                String key = util.byte2string(entry.getKey());
                VehiclePassInAreaInfo  passInAreaInfo= null;
                try {
                    passInAreaInfo = VehiclePassInAreaInfo.parseFrom(entry.getValue());
                    pssInAreaMap.put(Integer.valueOf(key),passInAreaInfo);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }
//            TermianlRuleAndParaService currentServer = (TermianlRuleAndParaService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_RULES);
//            // 加载服务站车次统计使用信息。
//            List<LCAreaInfo.AreaInfo> areaInfos = currentServer.getAreaInfoForStatistic();
            if (null != pssInAreaMap && pssInAreaMap.size() > 0) {
//                areas = areaInfos;
                logger.info("加载[服务站周边缓存]完成");
            } else {
                logger.warn("加载[服务站缓存]出错，加载结果为空，请检查");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, VehiclePassInAreaInfo> getPassAreaInfoForStatistic() {
        if (pssInAreaMap.size() < 1) {
            synchronized (mux) {
                if (pssInAreaMap.size() < 1) {
                    initPassInArea();
                }
            }
        }
        return pssInAreaMap;
    }

    public void reload() {
        pssInAreaMap.clear();
        initPassInArea();

    }
}

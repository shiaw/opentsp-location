package com.navinfo.opentsp.platform.dp.core.rule.handler.common;

import com.navinfo.opentsp.platform.dp.core.cache.AreaCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaDataEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.common.tile.LngLatAndTileConvert;
import com.navinfo.opentsp.platform.dp.core.rule.status.common.FilterCache;
import com.navinfo.opentsp.platform.dp.core.rule.tools.GeometricCalculation;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * User: zhanhk
 * Date: 16/10/21
 * Time: 上午10:41
 */
@Component
public class CommonRegularHandler {

    @Resource
    private AreaCommonCache areaCommonCache;

    @Autowired
    private CommonRegularDispatcher commonRegularDispatcher;

    private static final Logger log = LoggerFactory.getLogger(CommonRegularHandler.class);

    private static int zoom = 13;

    private static long noProcessCount = 0;

    private static long processCount = 0;

    static Set<Integer> terminalCommonRules = new HashSet<>();

    static {
//        terminalCommonRules.add(LCRegularCode.RegularCode.messageBroadcast_VALUE);
        terminalCommonRules.add(LCRegularCode.RegularCode.overtimePark_VALUE);
//        terminalCommonRules.add(LCRegularCode.RegularCode.limitSpeedForFault_VALUE);
//        terminalCommonRules.add(LCRegularCode.RegularCode.abnormalOilAlarm_VALUE);
        terminalCommonRules.add(LCRegularCode.RegularCode.inOrOutAreaNotifySetPara_VALUE);
    }

    /**
     * 通用规则处理
     */
    public GpsLocationDataEntity process(final GpsLocationDataEntity dataEntity) {
//        long s;
        if (dataEntity.getOriginalLat() == 0 || dataEntity.getOriginalLng() == 0){
            return dataEntity;
        }
        /*Map<Long, AreaEntity> areaMap = areaCommonCache.getAreaEntity(); //获取通用规则区域
        if(areaMap == null){
            return dataEntity;
        }*/
        int[] tile = LngLatAndTileConvert.getTileNumber(dataEntity.getLatitude(),dataEntity.getLongitude(),zoom);
        long tileId = LngLatAndTileConvert.xyzToTileId(zoom, tile[1], tile[2]);
        List<Long> list = areaCommonCache.getAreaTiles().get(tileId);
        if (list != null && list.size() > 0) {
//            s = System.currentTimeMillis();
            for (long areaid : list){
                AreaEntity areaData = areaCommonCache.getAreaEntity(areaid);
                double distance = GeometricCalculation.getDistance(dataEntity.getLatitude() / 1000000D,
                        dataEntity.getLongitude() / 1000000D, areaData.getDatas().get(0).getLatitude() / 1000000D,
                        areaData.getDatas().get(0).getLongitude() / 1000000D);
                ruleCommonEntityCal(distance, areaData, dataEntity);
            }
//            long e = System.currentTimeMillis() - s;
//            log.info("{}通用规则处理时间:{}ms", dataEntity.getUniqueMark(), e);
            processCount++;
            if (processCount%10000==0){
                log.error("进行通用规则处理个数：{}",processCount);
            }
        }else{
            noProcessCount++;
            if (noProcessCount%10000==0){
                log.error("从未进行通用规则处理个数：{}",noProcessCount);
            }
        }
        return dataEntity;
        /*long distanceTime = 0;
        long ruletime = 0;
        //过滤掉不在最短距离内的服务站
        Long filterTime = FilterCache.getIntence().getFilterTime(dataEntity.getTerminalId());
        if (filterTime != null && filterTime != 0) {
            if (filterTime >= dataEntity.getGpsDate()) {
                return dataEntity;
            }
        }
        for (Map.Entry<Long, AreaEntity> entry : areaMap.entrySet()) {
            AreaEntity areaEntity = entry.getValue();
            if (areaEntity == null || dataEntity.getOriginalLat() == 0 || dataEntity.getOriginalLng() == 0) {
                return dataEntity;
            }
            s = System.currentTimeMillis();
            double distance = areaFilter(entry.getKey(), areaEntity.getDatas().get(0), dataEntity);// 车与服务站的距离
            long e = System.currentTimeMillis() - s;

            distanceTime += e;
            if (distance != -1) {
                s = System.currentTimeMillis();
                ruleCommonEntityCal(distance, areaEntity, dataEntity);
                ruletime += (System.currentTimeMillis() - s);
            }
        }
        dataEntity.setProcessFilterTime(distanceTime);
        dataEntity.setRuleProcessTime(ruletime);
        log.info(dataEntity.getUniqueMark()+"--服务站过滤处理处理时间:"+distanceTime +" ms,"+"通用规则处理时间:"+ruletime+" ms");*/
    }

    /**
     * 服务站过滤处理,取车与所有服务站最小距离的时长
     *
     * @param areaId
     *            到服务站距离
     * @param areaData
     *            服务器数据
     * @param dataEntity
     *            位置数据
     */
    public static double areaFilter(long areaId, AreaDataEntity areaData, GpsLocationDataEntity dataEntity) {
        /*Long filterTime = FilterCache.getIntence().getFilterTime(dataEntity.getTerminalId());
        if (filterTime != null && filterTime != 0) {
            if (filterTime >= dataEntity.getGpsDate()) {
                return -1;
            }
        }*/
        Long filterTime = FilterCache.getIntence().getFilterTime(dataEntity.getTerminalId());
        double distance = GeometricCalculation.getDistance(dataEntity.getLatitude() / 1000000D,
                dataEntity.getLongitude() / 1000000D, areaData.getLatitude() / 1000000D,
                areaData.getLongitude() / 1000000D);
        if ((distance - areaData.getRadiusLength()) > LCConstant.AgreedValue.minDistance) {
            Long time = (long) (System.currentTimeMillis() / 1000 + distance / LCConstant.AgreedValue.averageSpeed);
            if(filterTime == null){
                FilterCache.getIntence().addFilterCache(dataEntity.getTerminalId(),time);
            }else if(time < filterTime){
                FilterCache.getIntence().addFilterCache(dataEntity.getTerminalId(),time);
            }
            return -1;
        } else {
            FilterCache.getIntence().addFilterCache(dataEntity.getTerminalId(),dataEntity.getGpsDate());
            return distance;
        }
    }

    /**
     * 通用规则处理
     * @param distance  到服务站距离
     * @param areaEntity 服务站数据
     * @param dataEntity 位置数据
     */
    private void ruleCommonEntityCal( double distance,final AreaEntity areaEntity,final GpsLocationDataEntity dataEntity){
        final double distanceFinal = distance;
        final Iterator<Integer> commRules = terminalCommonRules.iterator();  //通用规则Set集合
        while (commRules.hasNext()) {
            long s = System.currentTimeMillis();
            Integer ruleId = commRules.next();
            CommonRegularProcess commonRegularProcess = commonRegularDispatcher.getCommonRegular(ruleId);
            commonRegularProcess.process(distanceFinal,areaEntity, dataEntity);
            long e = System.currentTimeMillis() - s;
            if(e != 0) {
                dataEntity.getDetailMap().put(ruleId+"_"+areaEntity.getAreaId(),e);
//                log.info(dataEntity.getUniqueMark()+"区域ID:"+areaEntity.getAreaId()+",通用规则ID-->"+ruleId+",处理时间:"+ e +"ms");
            }
        }
    }
}

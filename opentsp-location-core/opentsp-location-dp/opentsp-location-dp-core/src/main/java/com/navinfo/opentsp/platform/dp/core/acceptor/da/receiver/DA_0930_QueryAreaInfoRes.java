package com.navinfo.opentsp.platform.dp.core.acceptor.da.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.acceptor.da.DACommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.TerminalCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaDataEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.common.Constant;
import com.navinfo.opentsp.platform.dp.core.common.RMIConectorManager;
import com.navinfo.opentsp.platform.dp.core.redis.JacksonUtil;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCQueryAreaInfoRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用规则区域信息的终端id 为 0 ①、终端id为0 认为是通用规则的区域信息 ②、终端id不为0 认为是一般规则的区域信息
 *
 * @author yinsihua
 */
@Component(value = "dA_1930_QueryAreaInfoRes")
public class DA_0930_QueryAreaInfoRes extends DACommand {

    public static Logger logger = LoggerFactory.getLogger(DA_0930_QueryAreaInfoRes.class);

    @Resource
    private AreaCommonCache areaCommonCache;// redis 通用dao

    @Resource
    private AreaCache areaCache;// redis 通用dao

    @Override
    public int processor(Packet packet) {
        try {
            long s = System.currentTimeMillis();
            LCQueryAreaInfoRes.QueryAreaInfoRes areaInfoRes =
                    LCQueryAreaInfoRes.QueryAreaInfoRes.parseFrom(packet.getContent());
            List<LCAreaInfo.AreaInfo> areaInfos = areaInfoRes.getInfosList();
            int count = 0;
            Map<String, String> areaEntityMap = new HashMap<>();
            for (LCAreaInfo.AreaInfo areaInfo : areaInfos) {
                if (areaInfo.getTerminalId() == 0) {
                    count ++;

                    // 终端id为0 认为是通用规则的区域信息
                    AreaEntity areaEntity =  new AreaEntity(areaInfo);
                    areaEntity.setAreaType(areaInfo.getTypes().getNumber());
                    areaEntity.setCreateDate(areaInfo.getCreateDate());
                    areaEntity.setOriginalAreaId(areaInfo.getAreaIdentify());
                    areaEntity.setTerminalId(areaInfo.getTerminalId());
                    List<AreaDataEntity> datas = new ArrayList<AreaDataEntity>();
                    for (int i = 0; i < areaInfo.getDatasCount(); i++) {
                        AreaDataEntity entity = new AreaDataEntity();
                        entity.setDataSn(areaInfo.getDatas(i).getDataSN());
                        entity.setDataStatus(areaInfo.getDatas(i).getDataStatus());
                        entity.setLatitude(areaInfo.getDatas(i).getLatitude());
                        entity.setLongitude(areaInfo.getDatas(i).getLongitude());
                        entity.setRadiusLength(areaInfo.getDatas(i).getRadiusLength());
                        entity.setDataStatus(areaInfo.getDatas(i).getDataStatus());
                        datas.add(entity);
                    }
                    areaEntity.setDatas(datas);

                    logger.info("更新区域通用缓存 key is : " + Constant.AREA_COMMON_CACHE_KEY + "_"
                            + String.valueOf(areaInfo.getAreaIdentify()));
                    areaEntityMap.put(areaEntity.getAreaId()+"", JacksonUtil.toJSon(areaEntity));
                    //加载初始化内存数据
                    areaCommonCache.initAddAreaEntity(areaEntity);
                } else {
                    logger.error("更新区域缓存 key is : " + Constant.AREA_CACHE_KEY + "_"
                            + String.valueOf(areaInfo.getTerminalId()));

                    // 终端id不为0 认为是一般规则的区域信息
                    // Map<Long, AreaEntity> map = AreaCache.getInstance()
                    Map<Long, AreaEntity> map = areaCache.getAreaEntity(areaInfo.getTerminalId());
                    if (map == null) {
                        map = new ConcurrentHashMap<>();
                    }
                    AreaEntity areaEntity = new AreaEntity(areaInfo);
                    map.put(areaEntity.getOriginalAreaId(), areaEntity);
                    // AreaCache.getInstance().addAreaEntity(areaEntity);
                    areaCache.addAreaEntity(areaEntity);
                }
            }
            //批量set redis
            areaCommonCache.batchSet(Constant.AREA_COMMON_CACHE_KEY,areaEntityMap);
            logger.error("加载0930区域缓存数据成功!数量:{},耗时:{}",count,System.currentTimeMillis()-s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}

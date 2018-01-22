package com.navinfo.opentsp.platform.dp.core.cache.init;

import com.navinfo.opentsp.platform.dp.core.acceptor.da.receiver.DA_0930_QueryAreaInfoRes;
import com.navinfo.opentsp.platform.dp.core.cache.init.base.InitBaseCache;
import com.navinfo.opentsp.platform.dp.core.common.CmdConstant;
import com.navinfo.opentsp.platform.dp.core.common.RmiConstant;
import com.navinfo.opentsp.platform.dp.core.rmi.DaRmiService;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCQueryAreaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 调用rmi接口初始化区域数据缓存数据
 * <p>
 * Created by zhangyu on 2016/9/20.
 */
@Component
public class InitAreaCache extends InitBaseCache {

    private static final Logger log = LoggerFactory.getLogger(InitAreaCache.class);

    @Value("${district.code}")
    public int districtCode;

    @Autowired
    private DaRmiService daRmiService;// rmi调用通用类

    @Autowired
    private DA_0930_QueryAreaInfoRes dA_1930_QueryAreaInfoRes;// 区域检索

    public static Logger logger = LoggerFactory.getLogger(InitAreaCache.class);

    /**
     * 初始化缓存
     */
    public void initCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 设置入参
                    Packet packetIn = setPacketIn();
                    log.info("开始加载AreaCache-RMI数据...");
                    Packet packet =
                            daRmiService.callRmi(RmiConstant.RMI_SERVICE_ID,
                                    RmiConstant.RMI_INTERFACE_NAME,
                                    RmiConstant.RmiInterFaceEum.DaRmiInterface.getClassType(),
                                    packetIn);

                    if (packet != null) {
                        dA_1930_QueryAreaInfoRes.processor(packet);
                    } else {
                        logger.info("加载DA-RMI(0930)无数据!");
                    }
                } catch (Exception e) {
                    logger.error("初始化区域缓存数据错误",e);
                }
            }
        }).start();
    }

    /**
     * 设置入参
     *
     * @return
     */
    public Packet setPacketIn() {
        Packet packetIn = new Packet();
        packetIn.setCommand(CmdConstant.QUERY_AREA_CACHE_CMD_10HEX);

        LCQueryAreaInfo.QueryAreaInfo.Builder builder = LCQueryAreaInfo.QueryAreaInfo.newBuilder();
        builder.setDistrictCode(LCDistrictCode.DistrictCode.valueOf(districtCode));
        builder.setNodeCode(0);

        packetIn.setContent(builder.build().toByteArray());

        return packetIn;
    }
}

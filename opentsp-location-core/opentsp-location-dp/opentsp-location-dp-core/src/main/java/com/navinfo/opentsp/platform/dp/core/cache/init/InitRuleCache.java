package com.navinfo.opentsp.platform.dp.core.cache.init;

import com.navinfo.opentsp.platform.dp.core.acceptor.da.receiver.DA_0932_QueryRegularDataRes;
import com.navinfo.opentsp.platform.dp.core.cache.init.base.InitBaseCache;
import com.navinfo.opentsp.platform.dp.core.common.CmdConstant;
import com.navinfo.opentsp.platform.dp.core.common.RmiConstant;
import com.navinfo.opentsp.platform.dp.core.rmi.DaRmiService;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCQueryRegularData;
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
public class InitRuleCache extends InitBaseCache {
    @Autowired
    private DaRmiService daRmiService;// rmi调用通用类

    @Autowired
    private DA_0932_QueryRegularDataRes dA_1932_QueryRegularDataRes;// 规则数据

    @Value("${district.code}")
    public int districtCode;

    public static Logger logger = LoggerFactory.getLogger(InitRuleCache.class);

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
                    logger.info("开始加载Rule-RMI数据...");
                    Packet packet =
                            daRmiService.callRmi(RmiConstant.RMI_SERVICE_ID,
                                    RmiConstant.RMI_INTERFACE_NAME,
                                    RmiConstant.RmiInterFaceEum.DaRmiInterface.getClassType(),
                                    packetIn);

                    if (packet != null) {
                        // 初始化缓存
                        dA_1932_QueryRegularDataRes.processor(packet);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
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
        packetIn.setCommand(CmdConstant.QUERY_REGULAR_DATA_CACHE_CMD_10HEX);

        LCQueryRegularData.QueryRegularData.Builder builder = LCQueryRegularData.QueryRegularData.newBuilder();
        builder.setDistrictCode(LCDistrictCode.DistrictCode.valueOf(districtCode));
        builder.setNodeCode(0);

        packetIn.setContent(builder.build().toByteArray());

        return packetIn;
    }
}

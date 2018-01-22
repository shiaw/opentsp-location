package com.navinfo.opentsp.platform.dp.core.cache.init;

import com.navinfo.opentsp.platform.dp.core.acceptor.da.receiver.DA_0910_GetDictDataRes;
import com.navinfo.opentsp.platform.dp.core.cache.init.base.InitBaseCache;
import com.navinfo.opentsp.platform.dp.core.common.RmiConstant;
import com.navinfo.opentsp.platform.dp.core.rmi.DaRmiService;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDictType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCGetDictData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yinsihua on 2016/9/22.
 */
@Component
public class InitDictDataCache extends InitBaseCache {
    @Autowired
    private DaRmiService daRmiService;// rmi调用通用类

    @Autowired
    private DA_0910_GetDictDataRes da_1910_GetDictDataRes; //获取数据字典

    public static Logger logger = LoggerFactory.getLogger(InitAreaCache.class);

    /**
     * 初始化缓存
     */
    @Override
    public void initCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 设置入参
                    Packet packetIn = setPacketIn();
                    logger.info("开始加载DictData-RMI数据...");
                    Packet packet =
                            daRmiService.callRmi(RmiConstant.RMI_SERVICE_ID,
                                    RmiConstant.RMI_INTERFACE_NAME,
                                    RmiConstant.RmiInterFaceEum.DaRmiInterface.getClassType(),
                                    packetIn);

                    if (packet != null) {
                        da_1910_GetDictDataRes.processor(packet);

                    } else {
                        logger.info("获取RMI对象失败！");
                    }
                } catch (Exception e) {
                    logger.error("初始化加载数据字典缓存失败：" + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * 设置入参
     *
     * @return
     */
    @Override
    public Packet setPacketIn() {
        Packet packetIn = new Packet();
        packetIn.setCommand(LCAllCommands.AllCommands.DataAccess.GetDictData_VALUE);

        LCGetDictData.GetDictData.Builder builder = LCGetDictData.GetDictData.newBuilder();
        builder.addDictType(LCDictType.DictType.commonRuleCode);

        packetIn.setContent(builder.build().toByteArray());

        return packetIn;
    }
}

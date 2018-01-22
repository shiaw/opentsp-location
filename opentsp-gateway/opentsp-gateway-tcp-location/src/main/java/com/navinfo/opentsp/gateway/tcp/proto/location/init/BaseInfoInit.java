package com.navinfo.opentsp.gateway.tcp.proto.location.init;

import com.navinfo.opentsp.gateway.tcp.proto.location.cache.LastMileageOilTypeCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalSatusSyncCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalSnInfoCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
/**
 * @author: qingqi
 * */
@Component
public class BaseInfoInit implements CommandLineRunner {
    @Autowired
    LastMileageOilTypeCache lastMileageOilTypeCache;
    @Autowired
    TerminalSnInfoCache infoCache;

//    @Autowired
//    TerminalSatusSyncCache terminalSatusSyncCache;

    @Override
    public void run(String... strings) throws Exception {
        //加载里程油耗速度标记
        lastMileageOilTypeCache.loadOilMileageType();
        //加载车牌号
        infoCache.reload();
        //加载积分里程、油耗
//        terminalSatusSyncCache.reload();
    }
}

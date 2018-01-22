package com.navinfo.opentsp.platform.dsa.listener;

import com.navinfo.opentsp.platform.dsa.service.RealTimeService;
import com.navinfo.opentsp.platform.dsa.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.io.FileNotFoundException;

public class AppStartListener implements ApplicationListener<ApplicationStartedEvent> {

    private static Logger logger = LoggerFactory.getLogger(AppStartListener.class);

    private void initData() {
        try {
            ConfigUtils.loadConfig();
            // 重启后将批量执行标识重置
            logger.info("系统重置批量执行标志位");
            ConfigUtils.writePros(RealTimeService.OFFLINEBATCHSWITCH, "0");
            ConfigUtils.writePros(RealTimeService.RLBATCHSWITCH, "0");
            ConfigUtils.writePros(RealTimeService.SGBATCHSWITCH, "0");
            ConfigUtils.writePros(RealTimeService.SGAREAINFOBATCHSWITCH, "0");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        initData();// 加载静态数据
    }
}

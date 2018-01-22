package com.navinfo.opentsp.platform.dsa.listener;

import com.navinfo.opentsp.platform.dsa.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AppOKListener implements ApplicationListener<ApplicationPreparedEvent> {

    private static Logger logger = LoggerFactory.getLogger(AppOKListener.class);
    @Autowired
    protected MilleagesCache milleagesCache;
    @Autowired
    protected TerminalRuleCache terminalRuleCache;
    @Autowired
    protected DistrictAndTileMappingCache districtAndTileMappingCache;
    @Autowired
    protected TerminalsCache terminalsCache;
    @Autowired
    protected TerminalParameterCache terminalParameterCache;

    private void initData() {
//        logger.info("初始化开始加载缓存文件……");
//        terminalsCache.reload();
//        districtAndTileMappingCache.reload();
//        milleagesCache.reload();
//        terminalRuleCache.reload();
//        logger.info("初始化加载缓存文件完毕……");
    }

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        initData();// 加载静态数据
    }
}

package com.navinfo.opentsp.platform.da.core.startup;

import com.navinfo.opentsp.platform.da.core.acceptor.MutualServer;
import com.navinfo.opentsp.platform.da.core.common.schedule.ScheduleController;
import com.navinfo.opentsp.platform.da.core.connector.mm.MMMutualClient;
import com.navinfo.opentsp.platform.da.core.jmx.Echo;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TerminalOnLineData;
import com.navinfo.opentsp.platform.da.core.rmi.server.DARmiServer;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Component
public class DARun implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(DARun.class);

    private boolean run = false;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!run) {
            return;
        }
        registerJmx();
        //启动DA监听
        MutualServer daServer = new MutualServer();
        daServer.start();
        //连接主从MM节点
        MMMutualClient MM_Master = new MMMutualClient();
        MM_Master.start(LCConstant.MasterSlave.Master);
//		MMMutualClient MM_Slaver = new MMMutualClient();
//		MM_Slaver.start(MasterSlave.Slave);
        try {
            DARmiServer.start();
        } catch (Exception e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
            e.printStackTrace();
        }
        //WebService发布
//        DeployWebService.deployService();
        //启动定时任务管理器
        ScheduleController.instance().start();
        //初始化加载type=3的末次缓存
        TempGpsData.init();
        //初始化所有终端下线
        TerminalOnLineData.InitOff();
    }

    private static void registerJmx() {
        logger.info("registerJmx  com.navinfo.opentsp.platform.da.core.jmx:type=Operate");
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name;
        try {
            name = new ObjectName(" com.navinfo.opentsp.platform.da.core.jmx:type=Operate");
            Echo mbean = new Echo();
            mbs.registerMBean(mbean, name);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
    public static String localhost = null;
    @PostConstruct
    public void initHost(){
        localhost = discoveryClient.getLocalServiceInstance().getHost();
    }
}

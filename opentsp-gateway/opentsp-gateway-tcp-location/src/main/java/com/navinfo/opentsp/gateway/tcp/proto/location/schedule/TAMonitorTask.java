package com.navinfo.opentsp.gateway.tcp.proto.location.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TAMonitorCache;
import com.navinfo.opentsp.platform.location.kit.LocationMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * User: zhanhk
 * Date: 16/11/21
 * Time: 上午10:54
 */
@Component
@Configurable
@EnableScheduling
public class TAMonitorTask implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    public static Logger logger = LoggerFactory.getLogger(TAMonitorTask.class);

    String host = null;

    private int serverPort;

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        this.serverPort = event.getEmbeddedServletContainer().getPort();

    }
    public int getPort() {
        return this.serverPort;
    }

    private static int taFlag = 0;

    private static int taBackFlag = 0;

    private static int kafkaFlag = 0;

    private static double kafkaTimeFlag=0.00;

    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    @Value("${location.monitor.topic:location-monitor}")
    private String topic;

    @Value("${location.monitor.commandId:80001}")
    private String commandId;


    @Scheduled(cron = "${opentsp.location.monitor.schedule.cron:0/1 * * * * ?}")
    public void run() {
        try {
            int taCount;
            if(taFlag == 0) {
                taCount = TAMonitorCache.getTACount();
                taFlag = taCount;
            } else {
                taCount = TAMonitorCache.getTACount() - taFlag;
                taFlag = TAMonitorCache.getTACount();
            }

            int taBackCount;
            if(taBackFlag == 0) {
                taBackCount = TAMonitorCache.getTABackCount();
                taBackFlag = taBackCount;
            } else {
                taBackCount = TAMonitorCache.getTABackCount() - taBackFlag;
                taBackFlag = TAMonitorCache.getTABackCount();
            }

            int kafkaCount;
            if(kafkaFlag == 0) {
                kafkaCount = TAMonitorCache.getKAFKACount();
                kafkaFlag = kafkaCount;
            } else {
                kafkaCount = TAMonitorCache.getKAFKACount() - kafkaFlag;
                kafkaFlag = TAMonitorCache.getKAFKACount();
            }

            double kafkaTime;

            if(kafkaTimeFlag == 0) {
                kafkaTime = TAMonitorCache.getKAFKATime();
                kafkaTimeFlag = kafkaTime;
            } else {
                kafkaTime = TAMonitorCache.getKAFKATime()-kafkaTimeFlag;
                kafkaTimeFlag = TAMonitorCache.getKAFKATime();
            }
            if(TAMonitorCache.getKAFKATime() != 0 && kafkaCount != 0) {
                kafkaTime = kafkaTime/kafkaCount;
            }
           logger.info("进TA数据量:{},回写终端数据量：{},发送KAFKA数据量:{},TA总量:{}，回写终端总量:{},KAFKA平均发送时间:{}", taCount,taBackCount,kafkaCount,taFlag,taBackFlag,String.format("%.2f",kafkaTime));
            LocationMonitor locationMonitor = new LocationMonitor();
            try {
                host = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                logger.error("get server host Exception e:", e);
            }
          //  String ip = request.getLocalAddr();    //取得服务器IP
          //  String port = String.valueOf(request.getLocalPort());    //取得服务器端口
            locationMonitor.setModuleName("TA_"+host+"_"+getPort());
            logger.info("~~~~~~~~~ModuleName~~~~~~~~"+locationMonitor.getModuleName());
          //  locationMonitor.setModuleName("TA_"+ip+"_"+port);
            locationMonitor.setAppName("TA");
            locationMonitor.setCurrentTime(System.currentTimeMillis());
            locationMonitor.setInTaCount(taCount);
            locationMonitor.setOutTaCount(kafkaCount);
            locationMonitor.setBackTerminalCount(taBackCount);
            KafkaCommand kafkaCommand = new KafkaCommand();
            try {
                kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(locationMonitor));
                kafkaCommand.setCommandId(commandId);
                kafkaCommand.setTopic(topic);
                kafkaCommand.setKey("80001");
                kafkaMessageChannel.send(kafkaCommand);
                logger.info("taMonitor send to kafka success !{}",locationMonitor.toString());
            } catch (JsonProcessingException e) {
                logger.error("序列化出错!{}",kafkaCommand,e);
            }
        } catch (Exception e) {
            logger.error("TA监控任务出错",e);
        }
    }

}

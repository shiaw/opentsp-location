package com.navinfo.opentsp.platform.dp.core.common.schedule.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.platform.dp.core.acceptor.ta.receiver.TA_3002_LocationReport;
import com.navinfo.opentsp.platform.dp.core.cache.DPMonitorCache;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * User: zhanhk
 * Date: 16/11/21
 * Time: 上午10:54
 */
@Component
@Configurable
@EnableScheduling
public class DPMonitorTask  implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    public static Logger logger = LoggerFactory.getLogger(DPMonitorTask.class);

    String host = null;

    private int serverPort;

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        this.serverPort = event.getEmbeddedServletContainer().getPort();

    }
    public int getPort() {
        return this.serverPort;
    }


    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    @Value("${location.monitor.topic:location-monitor}")
    private String topic;

    @Value("${location.monitor.commandId:80001}")
    private String commandId;
    /**
     * 进入DP总量
     */
    private static int dpAllCount = 0;

    private static int rpAllCount = 0;

    private static int daAllCount = 0;

    /**
     * 监控线程扫描一次时间间隔内处理总量
     */
    private static int dpFlag = 0;

    private static int rpFlag = 0;

    private static int daFlag = 0;

    private DPMonitorCache dpMonitorCache;

    @Autowired
    private TA_3002_LocationReport locationReport;

    @Scheduled(cron = "${dp.monitor.schedule:0/5 * * * * ?}")
    public void run() {
        try {
            //每个缓存对象累加和,等于总量
            int dpCount = 0;
            int rpCount = 0;
            int daCount = 0;
            long maxTime = 0;
            if(locationReport.getDpMonitorCache() != null ) {
                DPMonitorCache temp = locationReport.getDpMonitorCache();
                dpCount += temp.getDPCount();
                rpCount += temp.getRPCount();
                daCount += temp.getDACount();
                if(maxTime < temp.getMaxTime()) {
                    maxTime = temp.getMaxTime();
                    dpMonitorCache = temp;
                }
            }

            //计算时间区间内的值
            dpFlag = dpCount - dpAllCount;
            dpAllCount = dpCount;

            rpFlag = rpCount - rpAllCount;
            rpAllCount = rpCount;

            daFlag = daCount - daAllCount;
            daAllCount = daCount;

            StringBuffer sb = new StringBuffer();
            if(dpMonitorCache != null) {
                sb.append(dpMonitorCache.getTid() + "-最长规则处理时间:").append(maxTime).append("ms,{");
                long allTime = 0;
                if (!dpMonitorCache.getDetailMap().isEmpty()) {
                    for (Map.Entry<String, Long> temp : dpMonitorCache.getDetailMap().entrySet()) {
                        allTime += temp.getValue();
                        sb.append(temp.getKey()).append(":").append(temp.getValue()).append("ms,");
                    }
                    sb.append("allTime:"+allTime+"ms},{").
                            append("专用规则:").append(dpMonitorCache.getRuleSignTime()).append("ms,").
                            append("过滤算法:").append(dpMonitorCache.getProcessFilterTime()+"ms,").
                            append("通用规则:").append(dpMonitorCache.getRuleProcessTime()).append("ms,").
                            append("发送数据:").append(dpMonitorCache.getForwardTime()).append("ms,").
                            append("总时间:").append(dpMonitorCache.getEncryptTime()).append("ms}");
                }
            }
            logger.info("进DP数据量:{},出RP数据量:{},出DA数据量:{};DP总量:{},RP总量:{},DA总量:{},队列长度:{},DP处理积压:{},\n{}",
                    dpFlag, rpFlag, daFlag, dpCount, rpCount, daCount, 0, dpCount - daCount,sb.toString());
            KafkaCommand kafkaCommand = new KafkaCommand();
            try {
            LocationMonitor locationMonitor = new LocationMonitor();
            try {
                host = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                logger.error("get server host Exception e:", e);
            }
          //  String ip = request.getLocalAddr();    //取得服务器IP
          //  String port = String.valueOf(request.getLocalPort());    //取得服务器端口
            locationMonitor.setModuleName("DP_"+host+"_"+getPort());
            logger.info("~~~~~~~~~ModuleName~~~~~~~~"+locationMonitor.getModuleName());
          //  locationMonitor.setModuleName("DP_127.0.0.1_8090");
            locationMonitor.setAppName("DP");
            locationMonitor.setCurrentTime(System.currentTimeMillis());
            locationMonitor.setInDpCount(dpFlag);
            locationMonitor.setOutRpCount(rpFlag);
            locationMonitor.setOutDaCount(daFlag);
                kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(locationMonitor));
                kafkaCommand.setCommandId(commandId);
                kafkaCommand.setTopic(topic);
                kafkaCommand.setKey("80001");
                kafkaMessageChannel.send(kafkaCommand);
                logger.info("dpMonitor send to kafka success !{}",locationMonitor.toString());
            } catch (JsonProcessingException e) {
                logger.error("序列化出错!{}",kafkaCommand,e);
            }
        } catch (Exception e) {
            logger.error("DP监控任务出错", e);
        }
    }
}

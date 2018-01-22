package com.navinfo.opentsp.platform.da.core;

import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.AmqpConfiguration;
import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.CommandChannelConfiguration;
import com.navinfo.opentsp.platform.configuration.EurekaClient;
import com.navinfo.opentsp.platform.da.core.common.SpringContextUtil;
import com.navinfo.opentsp.platform.da.core.common.schedule.ScheduleController;
import com.navinfo.opentsp.platform.da.core.configuration.CommandListenerConfiguration;
import com.navinfo.opentsp.platform.da.core.configuration.KafkaConsumerConfiguration;
import com.navinfo.opentsp.platform.da.core.persistence.StatisticsStoreService;
import com.navinfo.opentsp.platform.da.core.persistence.application.StatisticsStoreServiceimpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisClusters;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisStatic;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalOnOffStatusServiceImpl;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.da.DaRmiInterface;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.*;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.synchronousdata.SynchronousTerminalDataService;
import com.navinfo.opentspcore.common.kafka.KafkaConfig;
import com.navinfo.opentspcore.common.log.LogConfiguration;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Service launcher for the User module which contains all the necessary configuration
 */
@Configuration
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan
@Import({AmqpConfiguration.class,
        CommandChannelConfiguration.class,
        CommandListenerConfiguration.class,
        EurekaClient.class,
        LogConfiguration.class,
        KafkaConfig.class})
public class Application implements CommandLineRunner{

    @Value("${da.rmi.port:1199}")
    private Integer rmiPort;

    @Bean
    public RmiServiceExporter rmiServiceExporter(DaRmiInterface daRmiService) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setService(daRmiService);
        rmiServiceExporter.setServiceName("DaRmiInterface");
        rmiServiceExporter.setServiceInterface(DaRmiInterface.class);
        rmiServiceExporter.setRegistryPort(rmiPort);
        return rmiServiceExporter;
    }

    @Bean
    public RmiServiceExporter synchronousTerminalDataExporter(SynchronousTerminalDataService synchronousTerminalDataService) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setService(synchronousTerminalDataService);
        rmiServiceExporter.setServiceName("SynchronousTerminalDataService");
        rmiServiceExporter.setServiceInterface(SynchronousTerminalDataService.class);
        rmiServiceExporter.setRegistryPort(rmiPort);
        return rmiServiceExporter;
    }

    /**
     * 报警统计结果存储服务
     *
     * @param alarmStatisticsStoreService
     * @return
     */
    @Bean
    public RmiServiceExporter alarmStatisticsStoreServiceExporter(AlarmStatisticsStoreService alarmStatisticsStoreService) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setService(alarmStatisticsStoreService);
        rmiServiceExporter.setServiceName("AlarmStatisticsStoreService");
        rmiServiceExporter.setServiceInterface(AlarmStatisticsStoreService.class);
        rmiServiceExporter.setRegistryPort(rmiPort);
        return rmiServiceExporter;
    }

    /**
     * 报警统计结果查询服务
     *
     * @param alarmStatisticsQueryService
     * @return
     */
    @Bean
    public RmiServiceExporter alarmStatisticsQueryServiceExporter(AlarmStatisticsQueryService alarmStatisticsQueryService) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setService(alarmStatisticsQueryService);
        rmiServiceExporter.setServiceName("AlarmStatisticsQueryService");
        rmiServiceExporter.setServiceInterface(AlarmStatisticsQueryService.class);
        rmiServiceExporter.setRegistryPort(rmiPort);
        return rmiServiceExporter;
    }

    /**
     * 终端位置数据查询服务
     *
     * @param termianlDataService
     * @return
     */
    @Bean
    public RmiServiceExporter termianlDataServiceExporter(TermianlDataService termianlDataService) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setService(termianlDataService);
        rmiServiceExporter.setServiceName("TermianlDataService");
        rmiServiceExporter.setServiceInterface(TermianlDataService.class);
        rmiServiceExporter.setRegistryPort(rmiPort);
        return rmiServiceExporter;
    }

    /**
     * 终端规则和参数查询服务
     *
     * @param termianlRuleAndParaService
     * @return
     */
    @Bean
    public RmiServiceExporter termianlRuleAndParaServiceExporter(TermianlRuleAndParaService termianlRuleAndParaService) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setService(termianlRuleAndParaService);
        rmiServiceExporter.setServiceName("TermianlRuleAndParaService");
        rmiServiceExporter.setServiceInterface(TermianlRuleAndParaService.class);
        rmiServiceExporter.setRegistryPort(rmiPort);
        return rmiServiceExporter;
    }

    /**
     * 每天终端统计任务状态服务
     *
     * @param terminalStatusService
     * @return
     */
    @Bean
    public RmiServiceExporter terminalStatusServiceExporter(TerminalStatusService terminalStatusService) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setService(terminalStatusService);
        rmiServiceExporter.setServiceName("TerminalStatusService");
        rmiServiceExporter.setServiceInterface(TerminalStatusService.class);
        rmiServiceExporter.setRegistryPort(rmiPort);
        return rmiServiceExporter;
    }

    /**
     * 查询key缓存服务
     *
     * @param rpQueryKeyService
     * @return
     */
    @Bean
    public RmiServiceExporter rpQueryKeyServiceExporter(RpQueryKeyService rpQueryKeyService) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setService(rpQueryKeyService);
        rmiServiceExporter.setServiceName("RpQueryKeyService");
        rmiServiceExporter.setServiceInterface(RpQueryKeyService.class);
        rmiServiceExporter.setRegistryPort(rmiPort);
        return rmiServiceExporter;
    }

    /**
     *
     * @param dSATaskStatusService
     * @return
     */
    @Bean
    public RmiServiceExporter dsATaskStatusServiceExporter(DSATaskStatusService dSATaskStatusService) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setService(dSATaskStatusService);
        rmiServiceExporter.setServiceName("DSATaskStatusService");
        rmiServiceExporter.setServiceInterface(DSATaskStatusService.class);
        rmiServiceExporter.setRegistryPort(rmiPort);
        return rmiServiceExporter;
    }

    /**
     *
     * @param statisticsQueryService
     * @return
     */
    @Bean
    public RmiServiceExporter statisticsQueryServiceExporter(StatisticsQueryService statisticsQueryService) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setService(statisticsQueryService);
        rmiServiceExporter.setServiceName("StatisticsQueryService");
        rmiServiceExporter.setServiceInterface(StatisticsQueryService.class);
        rmiServiceExporter.setRegistryPort(rmiPort);
        return rmiServiceExporter;
    }

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }

    @Bean(name = "TerminalOnOffStatusServiceImpl")
    public TerminalOnOffStatusServiceImpl TerminalOnOffStatusServiceImpl(){
        return new TerminalOnOffStatusServiceImpl();
    }

    @Bean(name = "StatisticsStoreService")
    public StatisticsStoreService StatisticsStoreService() {
        return new StatisticsStoreServiceimpl();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        //启动定时任务管理器
        ScheduleController.instance().start();
    }

    @Autowired
    private KafkaConsumerConfiguration kafkaConsumerConfiguration;

    @Override
    public void run(String... args) throws Exception {
        RedisClusters.getInstance();
        RedisStatic.getInstance();
        kafkaConsumerConfiguration.start();
    }
}

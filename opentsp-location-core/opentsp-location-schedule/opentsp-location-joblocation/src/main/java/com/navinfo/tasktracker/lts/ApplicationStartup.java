package com.navinfo.tasktracker.lts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author chenjc
 * @version 1.0
 * @date 2016-10-09
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    protected static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    @Autowired
    private SchedulerService schedulerService;

    // 定时任务开关
    @Value("${task.poiLocation.period:true}")
    private boolean demoenabled;

    // 定时删除seg开关
    @Value("${task.delseg.period:false}")
    private boolean delseg;


    @Value("${task.areaVehiclesExecuted:true}")
    private boolean areaVehiclesExecuted;

    @Value("${task.queryCarOnlineCountsed:true}")
    private boolean queryCarOnlineCountsed;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //定时
        if(demoenabled){
            schedulerService.poiLocation();
        }

        if(delseg){
            schedulerService.delSeg();
        }

        if(queryCarOnlineCountsed){
            schedulerService.queryCarOnlineCounts();
        }

        if(areaVehiclesExecuted){
            schedulerService.areaVehiclesExecute();
        }
    }
}

package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.rprest.CurrentCarsOnlineCommand;
import com.navinfo.opentsp.platform.rprest.service.CarStatisticsServiceImpl;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 */
@Component
public class CurrentCarsOnlineHandler extends AbstractCommandHandler<CurrentCarsOnlineCommand, CurrentCarsOnlineCommand.Result> {
        private static final Logger LOG = LoggerFactory.getLogger(CurrentCarsOnlineHandler.class);

    @Autowired
     private CarStatisticsServiceImpl carStatisticsService;

    public CurrentCarsOnlineHandler() {
            super(CurrentCarsOnlineCommand.class, CurrentCarsOnlineCommand.Result.class);
    }

    @Override
    public CurrentCarsOnlineCommand.Result handle(CurrentCarsOnlineCommand command) {

        CurrentCarsOnlineCommand.Result commandResult = new CurrentCarsOnlineCommand.Result();
        commandResult.setResultCode(ResultCode.OK.code());
        commandResult.setMessage("access");
//        Map<String,int[]> dataMap=new HashMap<>();
//        int dayArr[]=new int[24];
//        int avgArr[]=new int[24];
//        Random rand = new Random();
//        for(int i=0;i<24;i++) {
//            dayArr[i]=rand.nextInt(10000) ;
//            avgArr[i]=rand.nextInt(10000) ;;
//        }
//        dataMap.put("today",dayArr);
//        dataMap.put("ave",avgArr);
        try {
            commandResult.setData(carStatisticsService.carOnlineStatis30Day());
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
            commandResult.setResultCode(ResultCode.SERVER_ERROR.code());
            commandResult.setMessage("server error");
            return commandResult;
        }
        return commandResult;
    }
}

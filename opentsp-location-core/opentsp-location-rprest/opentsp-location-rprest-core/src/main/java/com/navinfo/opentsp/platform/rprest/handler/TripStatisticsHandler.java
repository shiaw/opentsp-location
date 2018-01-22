package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.rprest.TripStatisticsCommand;
import com.navinfo.opentsp.platform.rprest.cache.RedisTerminalInfoStorage;
import com.navinfo.opentsp.platform.rprest.cache.TerminalInfo;
import com.navinfo.opentsp.platform.rprest.entity.TripStatisticData;
import com.navinfo.opentsp.platform.rprest.service.TripStatisticsService;
import com.navinfo.opentsp.platform.rprest.utils.Base64;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 修伟 on 2017/11/8 0008.
 */
@Component
public class TripStatisticsHandler extends AbstractCommandHandler<TripStatisticsCommand, TripStatisticsCommand.Result> {
    private static final Logger logger = LoggerFactory.getLogger(TripStatisticsCommand.class);

    @Autowired
    TripStatisticsService tripStatisticsService;

    public TripStatisticsHandler(){
        super(TripStatisticsCommand.class,TripStatisticsCommand.Result.class);
    }
    @Override
    public TripStatisticsCommand.Result handle(TripStatisticsCommand command) {
        TripStatisticsCommand.Result commandResult = new TripStatisticsCommand.Result();
        if(command.getTerminalId() == null || command.getTerminalId().size() == 0){
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("terminalId is not null");
            return commandResult;
            /*List<TerminalInfo> list= redisTerminalInfoStorage.getAll();
            if (list != null && list.size()>0){
                List<Long> tList = new ArrayList<>();
                for (TerminalInfo info : list){
                    tList.add(Long.parseLong(info.getTerminalId()));
                }
                command.setTerminalId(tList);
            }*/
        }
        if(command.getBeginDate() == 0){
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("BeginDate is not null");
            return commandResult;
        }
        if(command.getEndDate() == 0){
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("EndDate is not null");
            return commandResult;
        }
        List<TripStatisticData> list = tripStatisticsService.queryTripInfo(command);
        commandResult.setData(list);
        commandResult.setResultCode(ResultCode.OK.code());
        return commandResult;
    }
}

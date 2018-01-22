package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.rprest.FaultInfoCommand;
import com.navinfo.opentsp.platform.rprest.entity.FaultData;
import com.navinfo.opentsp.platform.rprest.service.FaultInfoService;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 修伟 on 2017/11/10 0010.
 */
@Component
public class FaultInfoHandler extends AbstractCommandHandler<FaultInfoCommand, FaultInfoCommand.Result> {

    @Autowired
    FaultInfoService faultInfoService;

    public FaultInfoHandler(){
        super(FaultInfoCommand.class,FaultInfoCommand.Result.class);
    }
    @Override
    public FaultInfoCommand.Result handle(FaultInfoCommand command) {
        FaultInfoCommand.Result commandResult = new FaultInfoCommand.Result();
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
        List<FaultData> list = faultInfoService.queryFaultInfo(command);
        commandResult.setData(list);
        commandResult.setResultCode(ResultCode.OK.code());
        return commandResult;
    }
}

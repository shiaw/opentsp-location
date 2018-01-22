package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.rprest.PoiDenseLocationCommand;
import com.navinfo.opentsp.platform.rprest.service.PoiDenseLocationDataService;
import com.navinfo.opentsp.platform.rprest.utils.Base64;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyue
 */
@Component
public class PoiDenseLocationHandler extends AbstractCommandHandler<PoiDenseLocationCommand, PoiDenseLocationCommand.Result> {
    private static final Logger logger = LoggerFactory.getLogger(PoiDenseLocationHandler.class);
    @Autowired
    private PoiDenseLocationDataService poiDenseLocationDataService;

    public PoiDenseLocationHandler() {
        super(PoiDenseLocationCommand.class, PoiDenseLocationCommand.Result.class);
    }

    @Override
    public PoiDenseLocationCommand.Result handle(PoiDenseLocationCommand command) {
        logger.info("PoiDenseLocationHandler start");
        //根据command传过来的查询参数进行查询（此方法无参），并返回结果 ---- 返回最近的五分钟数据
        PoiDenseLocationCommand.Result commandResult = new PoiDenseLocationCommand.Result();
        if(command.getTerminalId() == 0){
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("TerminalId is not null");
            return commandResult;
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
        commandResult.setResultCode(ResultCode.OK.code());
        List month = poiDenseLocationDataService.nextTimme(command.getBeginDate(), command.getEndDate());
        if(month.size() > 6){
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("beginDate and endDate time out month 6");
            return commandResult;
        }
        byte[] dataDtoList = poiDenseLocationDataService.getPoiLocationData(command,month);
        if(dataDtoList == null){
            return commandResult;
        }
        Base64.encode(dataDtoList);
        commandResult.setData(Base64.encode(dataDtoList));
        logger.info("PoiDenseLocationHandler end");
        return commandResult;
    }

}

package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.rprest.CarsDistrictLastestDaysNumsCommand;
import com.navinfo.opentsp.platform.rprest.GetLocationDataCommand;
import com.navinfo.opentsp.platform.rprest.dto.CarsInDistrictLastestDaysNumDto;
import com.navinfo.opentsp.platform.rprest.dto.GetLocationDataDto;
import com.navinfo.opentsp.platform.rprest.service.GetLocationDataService;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 */
@Component
public class GetLocationDataHandler extends AbstractCommandHandler<GetLocationDataCommand, GetLocationDataCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(GetLocationDataHandler.class);
    @Autowired
    private GetLocationDataService getLocationDataService;

    public GetLocationDataHandler() {
        super(GetLocationDataCommand.class, GetLocationDataCommand.Result.class);
    }

    @Override
    public GetLocationDataCommand.Result handle(GetLocationDataCommand command) {
        //根据command传过来的查询参数进行查询（此方法无参），并返回结果 ---- 返回最近的五分钟数据
        GetLocationDataCommand.Result commandResult = new GetLocationDataCommand.Result();
        commandResult.setResultCode(ResultCode.OK.code());
        List<GetLocationDataDto> dataDtoList = new ArrayList<>();
        dataDtoList = getLocationDataService.getLocationDataDtos();
        commandResult.setData(dataDtoList);
        return commandResult;
    }
}

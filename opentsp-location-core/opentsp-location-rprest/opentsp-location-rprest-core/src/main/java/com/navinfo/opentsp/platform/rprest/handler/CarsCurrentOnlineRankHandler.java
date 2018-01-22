package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.rprest.CarsCurrentOnlineRankCommand;
import com.navinfo.opentsp.platform.rprest.CarsInDistrictOnlineNumCommand;
import com.navinfo.opentsp.platform.rprest.dto.CarsInDistrictOnlineNumDto;
import com.navinfo.opentsp.platform.rprest.entity.OnlineCarCity;
import com.navinfo.opentsp.platform.rprest.entity.OnlineCarProvinceCity;
import com.navinfo.opentsp.platform.rprest.persisted.OnlineCarProvinceCityRepository;
import com.navinfo.opentsp.platform.rprest.service.CarsCurrentOnlineRankService;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by machi1 on 2017/5/22.
 */
@Component
public class CarsCurrentOnlineRankHandler extends AbstractCommandHandler<CarsCurrentOnlineRankCommand, CarsCurrentOnlineRankCommand.Result> {
    private static final Logger log = LoggerFactory.getLogger(CarsCurrentOnlineRankHandler.class);

    public CarsCurrentOnlineRankHandler() {
        super(CarsCurrentOnlineRankCommand.class, CarsCurrentOnlineRankCommand.Result.class);
    }

    @Autowired
    private CarsCurrentOnlineRankService carsCurrentOnlineRankService;

    @Override
    public CarsCurrentOnlineRankCommand.Result handle(CarsCurrentOnlineRankCommand command) {
        log.debug("进入CarsCurrentOnlineRankHandler");
        CarsCurrentOnlineRankCommand.Result commandResult = new CarsCurrentOnlineRankCommand.Result();
        try{
            commandResult.setData(carsCurrentOnlineRankService.provorder());
            commandResult.setResultCode(ResultCode.OK.code());
            commandResult.setMessage("access");

            log.debug("CarsCurrentOnlineRankHandler ok！");
        }catch(Exception e){
            commandResult.setResultCode(ResultCode.SERVER_ERROR.code());
            commandResult.setMessage("fail");
            log.error("CarsCurrentOnlineRankHandler error",e);
        }
        return commandResult;
    }

}
package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.rprest.GetLocationDataCommand;
import com.navinfo.opentsp.platform.rprest.ServiceMapCommand;
import com.navinfo.opentsp.platform.rprest.dto.GetLocationDataDto;
import com.navinfo.opentsp.platform.rprest.dto.ServiceMapDto;
import com.navinfo.opentsp.platform.rprest.dto.ServiceStatisticsDto;
import com.navinfo.opentsp.platform.rprest.service.StationStaticService;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Component
public class ServiceMapHandler extends AbstractCommandHandler<ServiceMapCommand, ServiceMapCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceMapHandler.class);

    @Autowired
    StationStaticService stationStaticService;

    public ServiceMapHandler() {
        super(ServiceMapCommand.class, ServiceMapCommand.Result.class);
    }

    @Override
    public ServiceMapCommand.Result handle(ServiceMapCommand command) {

        ServiceMapCommand.Result commandResult = new ServiceMapCommand.Result();
        try {
            if ( command.getLevel() == null || command.getBeginDate() == null||command.getEndDate()==null) {
                commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
                commandResult.setMessage("查询参数错误");
            } else {
                LOG.error("服务站周边车辆信息查询参数，开始时间:"+command.getBeginDate()+"，结束时间："+command.getEndDate()+",查询类型:"+command.getLevel());

                List<ServiceMapDto> dataDtoList = stationStaticService.GetVehicleNearByStation(command.getLevel(),command.getBeginDate(),command.getEndDate());
                commandResult.setResultCode(ResultCode.OK.code());
                commandResult.setData(dataDtoList);
            }
        } catch (Exception e) {
            LOG.error("serviceMap服务站周边车辆信息查询出错！{}", command, e);
            commandResult.setResultCode(ResultCode.SERVER_ERROR.code());
            commandResult.setMessage("服务器错误");
        }
        return commandResult;
    }
}

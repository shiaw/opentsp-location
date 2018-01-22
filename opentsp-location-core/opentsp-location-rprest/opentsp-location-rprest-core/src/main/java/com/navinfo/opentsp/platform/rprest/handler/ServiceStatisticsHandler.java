package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.rprest.ServiceMapCommand;
import com.navinfo.opentsp.platform.rprest.ServiceStatisticsCommand;
import com.navinfo.opentsp.platform.rprest.dto.ServiceMapDto;
import com.navinfo.opentsp.platform.rprest.dto.ServiceStatisticsDto;
import com.navinfo.opentsp.platform.rprest.service.StationStaticService;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.apache.commons.collections.map.HashedMap;
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
public class ServiceStatisticsHandler extends AbstractCommandHandler<ServiceStatisticsCommand, ServiceStatisticsCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceStatisticsHandler.class);

    @Autowired
    StationStaticService stationStaticService;

    public ServiceStatisticsHandler() {
        super(ServiceStatisticsCommand.class, ServiceStatisticsCommand.Result.class);
    }

    @Override
    public ServiceStatisticsCommand.Result handle(ServiceStatisticsCommand command) {

        ServiceStatisticsCommand.Result commandResult = new ServiceStatisticsCommand.Result();
        try {
            if (command.getDistrict() == null || command.getDistrictType() == null || command.getBeginDate() == null || command.getEndDate() == null) {
                commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
                commandResult.setMessage("查询参数错误");
            } else {
                LOG.error("服务能力统计查询参数，开始时间:" + command.getBeginDate() + "，结束时间：" + command.getEndDate() + ",区域编码:" + command.getDistrict() + ",查询类型:" + command.getDistrictType());

                List<ServiceStatisticsDto> dataDtoList = stationStaticService.GetVehiclePassInArea(command.getDistrict(), command.getDistrictType(), command.getBeginDate(), command.getEndDate());
                commandResult.setResultCode(ResultCode.OK.code());
//                List<ServiceStatisticsDto> dataDtoList = new ArrayList<>();
//                ServiceStatisticsDto serviceStatisticsDto = new ServiceStatisticsDto();
//                serviceStatisticsDto.setDistrict(100000);
//                serviceStatisticsDto.setNumber(205);
//                dataDtoList.add(serviceStatisticsDto);
                Map<String, Object> map = new HashMap<>();
                if (command.getDistrictType() == 0) {
                    map.put("type", 0);
                } else {
                    map.put("type", 1);
                }
                map.put("districtList", dataDtoList);
                commandResult.setData(map);
            }
        } catch (Exception e) {
            LOG.error("serviceStatistics服务能力统计出错！{}", command, e);
            commandResult.setResultCode(ResultCode.SERVER_ERROR.code());
            commandResult.setMessage("服务器错误");
        }
        return commandResult;
    }
}

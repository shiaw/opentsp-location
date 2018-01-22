package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.mail.dto.MailSendResult;
import com.navinfo.opentsp.platform.rprest.CarsDistrictLastestDaysNumsCommand;
import com.navinfo.opentsp.platform.rprest.SelOnlineStatisticsCommand;
import com.navinfo.opentsp.platform.rprest.dto.CarsInDistrictLastestDaysNumDto;
import com.navinfo.opentsp.platform.rprest.service.CarsInDistrictLastestDaysNumService;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 */
@Component
public class CarsInDistrictLastestDaysNumHandler extends AbstractCommandHandler<CarsDistrictLastestDaysNumsCommand, CarsDistrictLastestDaysNumsCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(CarsInDistrictLastestDaysNumHandler.class);
    @Autowired
    private CarsInDistrictLastestDaysNumService carsInDistrictLastestDaysNumService;

    public CarsInDistrictLastestDaysNumHandler() {
        super(CarsDistrictLastestDaysNumsCommand.class, CarsDistrictLastestDaysNumsCommand.Result.class);
    }

    @Override
    public CarsDistrictLastestDaysNumsCommand.Result handle(CarsDistrictLastestDaysNumsCommand command) {

        CarsDistrictLastestDaysNumsCommand.Result commandResult = new CarsDistrictLastestDaysNumsCommand.Result();
        Integer districtType = command.getDistrictType();
        Integer districtCode = command.getDistrictCode();
        try {
            if (districtType != null && (districtType == 0 || districtType == 1 || districtType == 2)
                    && districtCode != null) {
                CarsInDistrictLastestDaysNumDto data = carsInDistrictLastestDaysNumService.getData(districtType,districtCode);
                if (data != null) {
                    commandResult.setData(data);
                    commandResult.setResultCode(ResultCode.OK.code());
                } else {
                    commandResult.setResultCode(ResultCode.SERVER_ERROR.code());
                    commandResult.setMessage("服务器错误");
                }
            } else {
                commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
                commandResult.setMessage("参数有误");
            }
        }catch (Exception e){
            LOG.error("获取行政区域最近7天的在线车辆数出错！{}",command,e);
            commandResult.setResultCode(ResultCode.SERVER_ERROR.code());
            commandResult.setMessage("服务器错误");
        }

        return commandResult;
    }

}

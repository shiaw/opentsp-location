package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.mail.dto.MailSendResult;
import com.navinfo.opentsp.platform.rprest.GetTotalMileageAndPackageCommand;
import com.navinfo.opentsp.platform.rprest.SelOnlineStatisticsCommand;
import com.navinfo.opentsp.platform.rprest.cache.RedisPackageStorage;
import com.navinfo.opentsp.platform.rprest.dto.GetTotalMileageAndPackageDto;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

/**
 */
@Component
public class GetTotalMileageAndPackageHandler extends AbstractCommandHandler<GetTotalMileageAndPackageCommand, GetTotalMileageAndPackageCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(GetTotalMileageAndPackageHandler.class);

    @Autowired
    private RedisPackageStorage packageStorage;

    public GetTotalMileageAndPackageHandler() {
        super(GetTotalMileageAndPackageCommand.class, GetTotalMileageAndPackageCommand.Result.class);
    }

    @Override
    public GetTotalMileageAndPackageCommand.Result handle(GetTotalMileageAndPackageCommand command) {

        GetTotalMileageAndPackageCommand.Result commandResult = new GetTotalMileageAndPackageCommand.Result();
        final ArrayList<MailSendResult> results = new ArrayList<>();
        commandResult.fillResult(ResultCode.OK);
        Map data=(Map) packageStorage.getAll();
        GetTotalMileageAndPackageDto getTotalMileageAndPackageDto=new GetTotalMileageAndPackageDto();
        getTotalMileageAndPackageDto.setDriveNum(data.get("driveNum")==null?0l:(Long)data.get("driveNum"));
        getTotalMileageAndPackageDto.setOnlineNum(data.get("onlineNum")==null?0:Integer.parseInt(data.get("onlineNum").toString()));
        getTotalMileageAndPackageDto.setPackageNum(data.get("packageNum")==null?0l:(Long)data.get("packageNum"));
        getTotalMileageAndPackageDto.setMileage(data.get("mileage")==null?0l:(Long)data.get("mileage"));
        commandResult.setData(getTotalMileageAndPackageDto);
        return commandResult;
    }
}

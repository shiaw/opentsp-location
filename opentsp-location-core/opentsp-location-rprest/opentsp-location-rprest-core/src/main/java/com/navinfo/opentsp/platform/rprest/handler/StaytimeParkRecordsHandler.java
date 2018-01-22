package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.rprest.StaytimeParkRecordsCommand;
import com.navinfo.opentsp.platform.rprest.StaytimeParkRecordsCommand.Result;
import com.navinfo.opentsp.platform.rprest.entity.StaytimeParkRecords;
import com.navinfo.opentsp.platform.rprest.service.StaytimeParkRecordsService;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaytimeParkRecordsHandler extends AbstractCommandHandler<StaytimeParkRecordsCommand, StaytimeParkRecordsCommand.Result>
{
    private static final Logger LOG = LoggerFactory.getLogger(StaytimeParkRecordsHandler.class);

    @Autowired
    StaytimeParkRecordsService staytimeParkRecordsService;

    public StaytimeParkRecordsHandler() { super(StaytimeParkRecordsCommand.class, StaytimeParkRecordsCommand.Result.class); }


    public StaytimeParkRecordsCommand.Result handle(StaytimeParkRecordsCommand command)
    {
        StaytimeParkRecordsCommand.Result commandResult = new StaytimeParkRecordsCommand.Result();
        try {
            if ((command.getTerminalID() == null) || (command.getAreaIds() == null) || (command.getStartDate() == 0L) || (command.getEndDate() == 0L)) {
                commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
                commandResult.setMessage("查询参数错误");
            } else {
                LOG.info("区域停留时长统计查询参数，开始时间:" + command.getStartDate() + "，结束时间：" + command.getEndDate() + ",查询区域:" + command.getAreaIds() + ",查询终端:" + command.getTerminalID());
                StaytimeParkRecords data = this.staytimeParkRecordsService.getStaytimeParkRecords(command.getTerminalID(), command.getAreaIds(), command.getStartDate(), command.getEndDate());
                commandResult.setResultCode(ResultCode.OK.code());
                commandResult.setData(data);
            }
        } catch (Exception e) {
            LOG.error("StaytimeParkRecordsHandler出错！{}", command, e);
            commandResult.setResultCode(ResultCode.SERVER_ERROR.code());
            commandResult.setMessage("服务器错误");
        }
        return commandResult;
    }
}
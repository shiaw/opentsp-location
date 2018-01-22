package com.navinfo.opentsp.platform.monitor.handler;

import com.navinfo.opentsp.platform.monitor.LastestMonitorCommand;
import com.navinfo.opentsp.platform.monitor.service.LastestMonitorService;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by machi1 on 2017/6/26.
 */
@Component
public class LastestMonitorHandler extends AbstractCommandHandler<LastestMonitorCommand, LastestMonitorCommand.Result> {
    private static final Logger logger = LoggerFactory.getLogger(LocationMonitorHandler.class);

    @Value("${opentsp.monitor.monitorTerminalId:17000010002}")
    private String monitorTerminalId;
    @Autowired
    private LastestMonitorService lastestMonitorService;

    public LastestMonitorHandler() {
        super(LastestMonitorCommand.class, LastestMonitorCommand.Result.class);
    }

    @Override
    public LastestMonitorCommand.Result handle(LastestMonitorCommand locationMonitorCommand) {
        try {
            logger.debug("进入末次位置监控Handler");
            LastestMonitorCommand.Result commandResult = new LastestMonitorCommand.Result();
            if(null==locationMonitorCommand.getMonitorTerminalId()||"".equals(locationMonitorCommand.getMonitorTerminalId())){
                //如果终端id为空
                commandResult = lastestMonitorService.query(monitorTerminalId);
                return commandResult;
            }else{
                commandResult = lastestMonitorService.query(locationMonitorCommand.getMonitorTerminalId());
                logger.debug("末次位置监控Handler方法结束，返回值："+commandResult.getResultCode());
                return commandResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("末次位置监控Handler异常："+e);
        }
        return null;
    }

}

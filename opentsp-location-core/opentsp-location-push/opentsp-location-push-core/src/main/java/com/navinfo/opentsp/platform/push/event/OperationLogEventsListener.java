package com.navinfo.opentsp.platform.push.event;

import com.navinfo.opentsp.platform.push.DeviceCommand;
import com.navinfo.opentsp.platform.push.impl.TerminalServiceImpl;
import com.navinfo.opentsp.platform.push.persisted.TerminalOperationLogEntry;
import com.navinfo.opentsp.platform.push.persisted.TerminalOperationLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/31
 * @modify
 * @copyright opentsp
 */
@Component
public class OperationLogEventsListener  implements ApplicationListener<OperationLogEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(OperationLogEventsListener.class);

    @Autowired
    private TerminalServiceImpl terminalService;


    @Async
    @Override
    public void onApplicationEvent(final OperationLogEvent operationLogEvent) {
        DeviceCommand deviceCommand= operationLogEvent.getDeviceCommand();
        LOG.debug("push operation command:deviceId={},command={}",deviceCommand.getDevice(),deviceCommand.getCommand());
        TerminalOperationLogEntry operationLogEntry=new TerminalOperationLogEntry();
        operationLogEntry.setOperationId(deviceCommand.getId());
        operationLogEntry.setTerminalId(deviceCommand.getDevice());
        operationLogEntry.setOperationTime(new Date().getTime());
        operationLogEntry.setOperationContent(deviceCommand.getCommand());
        terminalService.saveOperationLog(operationLogEntry);
    }
}

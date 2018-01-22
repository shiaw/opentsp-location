package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.utils.Throwables;
import com.navinfo.opentsp.platform.mail.dto.MailSendResult;
import com.navinfo.opentsp.platform.rprest.SelOnlineStatisticsCommand;
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
public class SelOnlineStatisticsHandler extends AbstractCommandHandler<SelOnlineStatisticsCommand, SelOnlineStatisticsCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(SelOnlineStatisticsHandler.class);


    public  SelOnlineStatisticsHandler() {
        super(SelOnlineStatisticsCommand.class, SelOnlineStatisticsCommand.Result.class);
    }

    @Override
    public SelOnlineStatisticsCommand.Result handle(SelOnlineStatisticsCommand command) {

        SelOnlineStatisticsCommand.Result commandResult = new SelOnlineStatisticsCommand.Result();
        final ArrayList<MailSendResult> results = new ArrayList<>();
        commandResult.fillResult(ResultCode.OK);
        //commandResult.setResults(results);
        return commandResult;
    }
}

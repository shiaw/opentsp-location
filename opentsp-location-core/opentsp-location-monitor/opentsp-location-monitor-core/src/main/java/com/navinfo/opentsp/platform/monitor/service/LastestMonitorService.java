package com.navinfo.opentsp.platform.monitor.service;

import com.navinfo.opentsp.platform.monitor.LastestMonitorCommand;
import com.navinfo.opentsp.platform.monitor.persisted.LastestMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by machi1 on 2017/6/26.
 */
@Component
public class LastestMonitorService {

    @Autowired
    private LastestMonitorRepository lastestMonitorRepository;

    public LastestMonitorCommand.Result query(String terminalId){
        return  lastestMonitorRepository.query(terminalId);
    }
}

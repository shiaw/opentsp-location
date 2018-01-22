package com.navinfo.opentsp.gateway.tcp.proto.location.schedule;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.DownCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.platform.auth.DownStatusCommand;
import com.navinfo.opentsp.platform.push.DownCommandState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/9/28
 * @modify
 * @copyright opentsp
 */
public class DownStatusCommandTask {

    private static final Logger log = LoggerFactory.getLogger(RecoverAnswerTask.class);

    private final MessageChannel messageChannel;

    @Autowired
    public DownStatusCommandTask(MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
    }

    @Scheduled(cron = "${opentsp.downCommand.recoverAnswer.schedule.cron:0/20 * * * * ?}")
    public void reportCurrentTime() {
        log.info("运行扫描PUSH下发指令超时指令缓存");
        Map<String, String> downCommandMap = DownCommandCache.getInstance().get();
        if (downCommandMap != null) {
            Iterator<String> iterator = downCommandMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String sendId = DownCommandCache.getInstance().get(key);
                log.info("处理终端超时指令，sendId={},{}", sendId, key);
                DownStatusCommand downStatusCommand = new DownStatusCommand();
                downStatusCommand.setId(sendId);
                downStatusCommand.setState(DownCommandState.T_TIMEOUT);
                messageChannel.send(downStatusCommand);
                DownCommandCache.getInstance().remove(key);

            }
        }
    }

}

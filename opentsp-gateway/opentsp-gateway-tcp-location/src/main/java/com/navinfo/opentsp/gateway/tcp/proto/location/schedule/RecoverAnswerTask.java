package com.navinfo.opentsp.gateway.tcp.proto.location.schedule;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.DownCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalResponse;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.ProtocolDispatcher;
import com.navinfo.opentsp.platform.auth.DownStatusCommand;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult;
import com.navinfo.opentsp.platform.push.DeviceCommand;
import com.navinfo.opentsp.platform.push.DeviceResult;
import com.navinfo.opentsp.platform.push.DownCommand;
import com.navinfo.opentsp.platform.push.DownCommandState;
import com.navinfo.opentspcore.common.gateway.ClientConnection;
import com.navinfo.opentspcore.common.gateway.ClientsConnections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/9/12
 * @modify 下发业务指令，对超时指令进行相应的处理
 * @copyright opentsp
 */
@Component
@Configurable
@EnableScheduling
public class RecoverAnswerTask {

    private static Set<Integer> commands = new HashSet<Integer>();

    static {
        commands.add(AllCommands.Terminal.CallName_VALUE);
        commands.add(AllCommands.Terminal.ParameterQuery_VALUE);
        commands.add(AllCommands.Terminal.MediaDataQuery_VALUE);
    }

    private static final Logger log = LoggerFactory.getLogger(RecoverAnswerTask.class);

    private final MessageChannel messageChannel;

    @Autowired
    public RecoverAnswerTask(MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
    }

    @Scheduled(cron = "${opentsp.recoverAnswer.schedule.cron:0 0/5 *  * * ?}")
    public void reportCurrentTime() {
        log.info("运行扫描超时指令缓存");
        List<AnswerEntry> answerEntries = AnswerCommandCache.getInstance()
                .getTimeoutAnswerEntry();
        if (answerEntries != null) {
            for (AnswerEntry answerEntry : answerEntries) {
                String key = answerEntry.getUniqueMark() + "-" + answerEntry.getSerialNumber();
                String sendId = DownCommandCache.getInstance().get(key);
                log.info("处理终端超时指令，sendId={},cmd={},serialNumber={},device={}", sendId, answerEntry.getTerminalCommand(),answerEntry.getSerialNumber(), answerEntry.getUniqueMark());
                DownStatusCommand downStatusCommand = new DownStatusCommand();
                downStatusCommand.setId(sendId);
                downStatusCommand.setState(DownCommandState.T_TIMEOUT);
                Packet packet = TerminalResponse.response(answerEntry.getInternalCommand(), Long.parseLong(answerEntry.getUniqueMark()), ResponseResult.failure, answerEntry.getSerialNumber());
                downStatusCommand.setCommand(packet.getCommand());
                downStatusCommand.setProtocol(packet.getProtocol());
                downStatusCommand.setSerialNumber(packet.getSerialNumber());
                downStatusCommand.setData(Convert.bytesToHexString(packet.getContent()));
                messageChannel.send(downStatusCommand);
                DownCommandCache.getInstance().remove(key);

            }
        }

        log.info("运行扫描PUSH下发指令超时指令缓存");
//        Map<String, String> downCommandMap = DownCommandCache.getInstance().get();
//        if (downCommandMap != null) {
//            Iterator<String> iterator = downCommandMap.keySet().iterator();
//            while (iterator.hasNext()) {
//                String key = iterator.next();
//                String sendId = DownCommandCache.getInstance().get(key);
//                log.info("处理终端超时指令，sendId={},{}", sendId, key);
//                DownStatusCommand downStatusCommand = new DownStatusCommand();
//                downStatusCommand.setId(sendId);
//                downStatusCommand.setState(DownCommandState.T_TIMEOUT);
//                messageChannel.send(downStatusCommand);
//                DownCommandCache.getInstance().remove(key);
//
//            }
//        }
    }

}

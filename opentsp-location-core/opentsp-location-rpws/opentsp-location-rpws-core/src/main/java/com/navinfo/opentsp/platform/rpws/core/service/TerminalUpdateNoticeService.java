package com.navinfo.opentsp.platform.rpws.core.service;

import com.navinfo.opentsp.common.messaging.*;
import com.navinfo.opentsp.platform.dp.command.DeleteTerminalCommand;
import com.navinfo.opentsp.platform.dp.command.StorageTerminalInfoCommand;
import com.navinfo.opentsp.platform.push.TerminalAddCommand;
import com.navinfo.opentsp.platform.push.TerminalDelCommand;
import com.navinfo.opentsp.platform.rpws.core.common.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 终端信息更新通知服务
 * User: chenjc
 * Date: 16/9/23
 * Time: 下午6:14
 */
@Service("terminalUpdateNoticeService")
public class TerminalUpdateNoticeService {
    protected static final Logger logger = LoggerFactory.getLogger(TerminalUpdateNoticeService.class);

    // 消息发送渠道
    @Autowired
    private MessageChannel messageChanel;

    // 终端信息变更后通知DP、PUSH的重试次数，默认：3
    @Value("${terminal.syn.retry.times:3}")
    private int reTryTimes;

    // TerminalUpdateNoticeService实例
    private static TerminalUpdateNoticeService _instance = null;

    public static TerminalUpdateNoticeService getInstance() {
        if (_instance == null) {
            _instance = (TerminalUpdateNoticeService) SpringContextUtil.getBean("terminalUpdateNoticeService");
        }

        return _instance;
    }

//    /**
//     * 添加或更新终端信息同步给DP
//     * @param terminalInfo
//     */
//    public void terminalAddOrUpdateSynDP2(Map<String,Object> terminalInfo)
//    {
//        StorageTerminalInfoCommand storageTerminalInfoCommand = new StorageTerminalInfoCommand();
//        storageTerminalInfoCommand.setTerminalInfo(terminalInfo);
//
//        try {
//            messageChanel.sendAndReceive(storageTerminalInfoCommand, new ResultCallback<CommandResult>(){
//                @Override
//                public synchronized void onResult(CommandResult result) {
//                    if (result != null && result.getResultCode() == ResultCode.OK.code()) {
//                        logger.info("添加或更新终端信息同步DP成功");
//                    } else
//                    {
//                        logger.info("添加或更新终端信息同步DP失败");
//                    }
//                }
//
//                @Override
//                public synchronized void onError(RuntimeException exception) {
//                    logger.info("添加或更新终端信息同步DP异常");
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 添加或更新终端信息同步给Push
     * @param terminalId
     * @param protocolType
     * @param deviceId
     */
    public void terminalAddOrUpdateSynPush(String terminalId, int protocolType, String deviceId)
    {
        TerminalAddCommand terminalAddCommand = new TerminalAddCommand();
        terminalAddCommand.setTerminalId(terminalId);
        terminalAddCommand.setProtocolType(protocolType);
        terminalAddCommand.setDeviceId(deviceId);

        try {
            messageChanel.sendAndReceive(terminalAddCommand, new SyncResultHandler<Command.Result>(messageChanel, terminalAddCommand, 2, 1, reTryTimes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加或更新终端信息同步给DP
     * @param terminalInfo
     */
    public void terminalAddOrUpdateSynDP(Map<String,Object> terminalInfo)
    {
        StorageTerminalInfoCommand storageTerminalInfoCommand = new StorageTerminalInfoCommand();
        storageTerminalInfoCommand.setTerminalInfo(terminalInfo);

        try {
            messageChanel.sendAndReceive(storageTerminalInfoCommand, new SyncResultHandler<CommandResult>(messageChanel, storageTerminalInfoCommand, 1, 1, reTryTimes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     *  删除终端信息同步给DP
//     * @param terminalInfos
//     */
//    public void terminalDeleteSynDP2(long[] terminalInfos)
//    {
//        DeleteTerminalCommand deleteTerminalCommand = new DeleteTerminalCommand();
//        Long[] terminals = new Long[terminalInfos.length];
//        for (int i = 0; i < terminalInfos.length; i++)
//        {
//            terminals[i] = terminalInfos[i];
//        }
//        deleteTerminalCommand.setTerminalInfos(terminals);
//
//        try {
//            messageChanel.sendAndReceive(deleteTerminalCommand, new ResultCallback<CommandResult>(){
//                @Override
//                public synchronized void onResult(CommandResult result) {
//                    if (result != null && result.getResultCode() == ResultCode.OK.code()) {
//                        logger.info("删除终端信息同步DP成功");
//                    } else
//                    {
//                        logger.info("删除终端信息同步DP失败");
//                    }
//                }
//
//                @Override
//                public synchronized void onError(RuntimeException exception) {
//                    logger.info("删除终端信息同步DP异常");
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     *  删除终端信息同步给Push
     * @param terminalInfos
     */
    public void terminalDeleteSynPush(long[] terminalInfos)
    {
        TerminalDelCommand terminalDelCommand = new TerminalDelCommand();
        String[] terminals = new String[terminalInfos.length];
        for (int i = 0; i < terminalInfos.length; i++)
        {
            terminals[i] = String.valueOf(terminalInfos[i]);
        }
        terminalDelCommand.setTerminalIds(terminals);

        try {
            messageChanel.sendAndReceive(terminalDelCommand, new SyncResultHandler<Command.Result>(messageChanel, terminalDelCommand, 2, 2, reTryTimes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  删除终端信息同步给DP
     * @param terminalInfos
     */
    public void terminalDeleteSynDP(long[] terminalInfos)
    {
        DeleteTerminalCommand deleteTerminalCommand = new DeleteTerminalCommand();
        Long[] terminals = new Long[terminalInfos.length];
        for (int i = 0; i < terminalInfos.length; i++)
        {
            terminals[i] = terminalInfos[i];
        }
        deleteTerminalCommand.setTerminalInfos(terminals);

        try {
            messageChanel.sendAndReceive(deleteTerminalCommand, new SyncResultHandler<CommandResult>(messageChanel, deleteTerminalCommand, 1, 2, reTryTimes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 终端信息同步结果处理类
     * @param <R>
     */
    private static final class SyncResultHandler<R extends Command.Result> implements ResultCallback<R> {
        // 消息发送渠道
        MessageChannel messageChanel;
        // 终端同步command
        private AbstractCommand<R> command;
        // 同步目标系统 1:DP、2:PUSH
        private int syncDesc;
        // 操作 1:添加或更新、2:删除
        private int operation;
        // 同步重试次数
        private int reTryTimes;
        // 同步计数器
        private int curTimes = 1;

        /**
         *
         * @param messageChanel
         * @param command
         * @param reTryTimes
         */
        public SyncResultHandler(MessageChannel messageChanel, AbstractCommand<R> command, int syncDesc, int operation, int reTryTimes){
            this.messageChanel = messageChanel;
            this.command = command;
            this.syncDesc = syncDesc;
            this.operation = operation;
            this.reTryTimes = reTryTimes;
        }
        @Override
        public synchronized void onResult(R result) {
            CommandResult commandResult = (CommandResult)result;
            if (commandResult != null && commandResult.getResultCode() == ResultCode.OK.code()) {
                logger.info("第" + curTimes + "次调用" + (syncDesc == 1 ? "DP" : "PUSH") +"的终端" + (operation == 1 ? "添加或更新" : "删除") + "接口，返回成功！");
            } else
            {
                logger.info("第" + curTimes + "次调用" + (syncDesc == 1 ? "DP" : "PUSH") +"的终端" + (operation == 1 ? "添加或更新" : "删除") + "接口，返回失败！");
                if(curTimes < reTryTimes)
                {
                    messageChanel.sendAndReceive(command, this);
                }
            }
            curTimes++;
        }

        @Override
        public synchronized void onError(RuntimeException exception) {
            logger.info("第" + curTimes + "次调用" + (syncDesc == 1 ? "DP" : "PUSH") +"的终端" + (operation == 1 ? "添加或更新" : "删除") + "接口，返回异常！");
            if(curTimes < reTryTimes)
            {
                messageChanel.sendAndReceive(command, this);
            }
            curTimes++;
        }
    }
}

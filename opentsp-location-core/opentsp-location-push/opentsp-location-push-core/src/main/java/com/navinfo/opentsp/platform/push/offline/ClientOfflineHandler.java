package com.navinfo.opentsp.platform.push.offline;

import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.push.ClientOfflineCommand;
import com.navinfo.opentsp.platform.push.online.DeviceRegistration;
import com.navinfo.opentsp.platform.push.online.OnlineUsers;
import com.navinfo.opentsp.platform.push.online.RedisOnlineStatusStorage;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 下线处理
 * User: zhanhk
 * Date: 16/7/26
 * Time: 上午9:23
 */
@Component
public class ClientOfflineHandler extends AbstractCommandHandler<ClientOfflineCommand, CommandResult> {

    private static final Logger LOG = LoggerFactory.getLogger(ClientOfflineHandler.class);


    private final OnlineUsers onlineUsers;

    @Autowired
    private RedisOnlineStatusStorage redisOnlineStatusStorage;

    @Autowired
    public ClientOfflineHandler(OnlineUsers onlineUsers) {
        super(ClientOfflineCommand.class, CommandResult.class);
        this.onlineUsers = onlineUsers;
    }

    @Override
    public CommandResult handle(ClientOfflineCommand command) {

        DeviceRegistration deviceRegistration = redisOnlineStatusStorage.get(command.getDeveiceId());
        if (deviceRegistration == null) {
            LOG.info("device online is null");
            return new CommandResult().fillResult(ResultCode.CLIENT_ERROR);
        }
        LOG.info("device：{},queueName:{},cache QueueName:{}", command.getDeveiceId(), command.getQueueName(), deviceRegistration.getReturnAddress());

        /**
         * 1，当终端先在A节点上线后，由于网络异常下线。这时服务器并未感知。
         * 2，些时终端在B节点上线，并发出上线通知。更新在线列表。
         * 3，A节点超时检测出终端异常下线，发出下线通知。
         * 4，此时应该忽略此下线通知。判断条件：当发现在线列表中的queuename和发起command的queuename不相同。
         */
        String queueName = command.getQueueName();
        if (!StringUtils.isEmpty(queueName) && queueName.equals(deviceRegistration.getReturnAddress())) {
            LOG.info("device：{} Offline", command.getDeveiceId());
            //redis缓存操作
            onlineUsers.offline(command.getDeveiceId());
        }
        return new CommandResult().fillResult(ResultCode.OK);
    }
}

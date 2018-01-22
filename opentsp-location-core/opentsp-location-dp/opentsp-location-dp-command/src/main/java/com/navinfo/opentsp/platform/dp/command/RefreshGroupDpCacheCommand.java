package com.navinfo.opentsp.platform.dp.command;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 发给DP服务组,每个服务都收到消息,刷新内存缓存
 * User: zhanhk
 * Date: 16/9/6
 * Time: 下午4:51
 */
@DpGroupQueue
public class RefreshGroupDpCacheCommand extends AbstractCommand<CommandResult> {

    private Packet packet;

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    @Override
    public Class<? extends CommandResult> getResultType() {
        return CommandResult.class;
    }

}

package com.navinfo.opentsp.platform.dp.command;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 *
 * User: zhanhk
 * Date: 16/9/5
 * Time: 下午6:03
 */
@DpQueue
public class PacketCommand extends AbstractCommand<CommandResult> {

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

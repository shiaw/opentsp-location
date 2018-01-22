package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.reveive;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentspcore.common.handler.AbstractKafkaCommandHandler;


public abstract class BaseDPCommand extends AbstractKafkaCommandHandler {

    protected BaseDPCommand() {
        super(KafkaCommand.class);
    }

}

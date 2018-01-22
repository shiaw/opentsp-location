package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.da;

import com.navinfo.opentsp.platform.location.kit.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DACommand {
    public static Logger logger = LoggerFactory.getLogger(DACommand.class);

    public abstract Packet processor(Packet packet);
}

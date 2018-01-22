package com.navinfo.opentsp.platform.da.core.handler;

import com.navinfo.opentsp.platform.da.core.rmi.DaDispatcher;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: wzw
 * 2016年9月20日14:24:01
 */
@Component
public class PacketHandler {

    @Autowired
    private DaDispatcher daDispatcher;

    protected static final Logger logger = LoggerFactory.getLogger(PacketHandler.class);

    public Packet handle(Packet packet) {

        Packet newPacket = new Packet();
        Dacommand daCommand = daDispatcher.getHandler(String.valueOf(packet.getCommand()));
        newPacket = daCommand.processor(packet);
        return newPacket;
    }
}

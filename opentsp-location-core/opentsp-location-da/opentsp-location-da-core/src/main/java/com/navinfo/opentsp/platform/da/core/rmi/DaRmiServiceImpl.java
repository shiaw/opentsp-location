package com.navinfo.opentsp.platform.da.core.rmi;

import com.navinfo.opentsp.platform.da.core.handler.PacketHandler;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.da.DaRmiInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: wzw
 * 2016年9月20日14:08:21
 */
@Service
public class DaRmiServiceImpl implements DaRmiInterface {
    @Autowired
    private PacketHandler packetHandler;

    @Autowired
    private DaDispatcher daDispatcher;
    private static final Logger logger = LoggerFactory.getLogger(DaRmiServiceImpl.class);

    public Packet getRmiPacket(Packet packet){

        try {
            Dacommand dacommand = daDispatcher.getHandler(packet.getCommandForHex()+"");
            logger.info("==================DaRmiServiceImpl:" +
                    "================");
            return dacommand.processor(packet);
        } catch (Exception e) {
            logger.info("==================DaRmiServiceImpl:调用rmi新增或修改终端失败================");
            System.out.println(packet.toString());
            e.printStackTrace();
            return null;
        }

    }

}

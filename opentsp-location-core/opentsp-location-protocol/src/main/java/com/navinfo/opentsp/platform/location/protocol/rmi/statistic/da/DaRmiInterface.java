package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.da;

import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * User: wzw
 * 2016年9月20日14:08:21
 */
public interface DaRmiInterface {
    Packet getRmiPacket(Packet packet);
}
package com.navinfo.opentsp.platform.da.core.rmi;

import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 协议解析基类
 *
 * @author wzw
 */
public abstract class Command {
    public abstract Packet processor(Packet packet);
}

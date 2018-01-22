package com.navinfo.opentsp.platform.location.kit;

/**
 * 协议解析基类
 * 
 * @author lgw
 * 
 */
public abstract class Command {
	
	public abstract int processor(Packet packet);

	public abstract int write(Packet packet);

}

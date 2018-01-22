package com.navinfo.opentsp.platform.location.kit.id.builder;

import com.navinfo.opentsp.platform.location.kit.id.ID;

/**
 * 流水号生成器
 * 
 * @author lgw
 * 
 */
public class SerialNumber implements ID {
	static volatile int id = 0;
	@Override
	public Object next() {
		id++;
		if(id > 65535){
			id = 0;
		}
		return id;
	}

}

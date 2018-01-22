package com.navinfo.opentsp.platform.location.kit.id;

/**
 * ID生成器接口
 * 
 * @author aerozh-lgw
 * 
 */
public interface ID {
	/**
	 * 获取下一个ID
	 * 
	 * @return Object
	 */
	abstract Object next();
}

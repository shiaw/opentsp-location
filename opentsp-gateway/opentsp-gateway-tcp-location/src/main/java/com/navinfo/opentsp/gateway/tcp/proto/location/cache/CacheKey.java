package com.navinfo.opentsp.gateway.tcp.proto.location.cache;


import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;

/**
 * 缓存Key生成
 * 
 * @author lgw
 * 
 */
public class CacheKey {
	/**
	 * 获取多媒体key
	 * 
	 * @param uniqueMark
	 *            {@link String} 终端唯一标识
	 * @param serialNumber
	 *            {@link Integer} 流水号
	 * @param mediaId
	 *            {@link Long} 多媒体ID
	 * @return {@link String}
	 */
	public static String multimediaKey(String uniqueMark, int serialNumber,
									   long mediaId) {
		return uniqueMark + "_" + serialNumber + "_" + mediaId;
	}

}

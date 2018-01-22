package com.navinfo.opentsp.platform.da.core.persistence.redis.service;

import java.util.List;

public interface ILinkService {
	/**
	 * 存储链路异常数据
	 * 里层key加nodeCodeTo
	 * @param nodeCode
	 *            {@link Integer} 节点编号
	 * @param nodeCodeTo
	 *            {@link Integer} 目标节点编码
	 * @param dataType
	 *            {@link Integer} 数据类型
	 * @param dataValue
	 *            {@link Byte}[] 数据内容
	 */
	abstract void saveLinkExceptionData(long nodeCode,long nodeCodeTo, int dataType,
										byte[] dataValue);

	/**
	 * 查询链路异常数据
	 *
	 * @param nodeCode
	 *            {@link Integer} 节点编号
	 * @param dataType
	 *            {@link Integer} 数据类型
	 * @param size
	 *            {@link Integer} 请求数据条数
	 * @return {@link List}
	 */
	abstract List<byte[]> queryLinkExceptionData(long nodeCode,long nodeCodeTo, int dataType,
												 int size);

	abstract void delLinkExceptionData(long nodeCode,long nodeCodeTo, int dataType);
}

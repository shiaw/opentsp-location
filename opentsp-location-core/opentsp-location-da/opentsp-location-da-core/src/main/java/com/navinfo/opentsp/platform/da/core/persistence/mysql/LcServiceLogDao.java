package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceLogDBEntity;

import java.util.List;


public interface LcServiceLogDao extends BaseDao<LcServiceLogDBEntity>{


	/**
	 * 根据参数查询服务日志
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param logDistrict 服务区域编码
	 * @param logIp IP地址
	 * @param typeCode 服务状态
	 * @return
	 */
	public List<LcServiceLogDBEntity> queryServiceLogList(long start, long end,
														  int logDistrict, String logIp, int typeCode,int currentPage,int pageSize);

	public int queryServiceLogCount(long start, long end,
									int logDistrict, String logIp, int typeCode);
}

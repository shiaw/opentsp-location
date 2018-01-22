package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDataDBEntity;

import java.util.List;


public interface LcTerminalAreaDataDao extends BaseDao<LcTerminalAreaDataDBEntity> {
	/**
	 * 根据区域id查找
	 * @param taId
	 * @return
	 */
	public List<LcTerminalAreaDataDBEntity> getTerminalAreaData(int taId);

	/**
	 * 根据区域标识删除区域数据
	 * @param originalAreaId
	 */
	public void deleteByAreaId(long terminalId,int... originalAreaId);
}
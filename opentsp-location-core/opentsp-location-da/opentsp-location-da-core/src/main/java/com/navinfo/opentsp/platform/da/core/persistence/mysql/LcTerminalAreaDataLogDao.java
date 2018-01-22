package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDataLogDBEntity;

import java.util.List;


public interface LcTerminalAreaDataLogDao extends BaseDao<LcTerminalAreaDataLogDBEntity> {
	/**
	 * 根据区域id查找
	 * @param taId
	 * @return
	 */
	public List<LcTerminalAreaDataLogDBEntity> queryTerminalAreaDataList(int taId);

}

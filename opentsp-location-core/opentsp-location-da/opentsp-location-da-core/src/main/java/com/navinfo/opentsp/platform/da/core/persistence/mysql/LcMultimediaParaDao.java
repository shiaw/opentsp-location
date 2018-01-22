package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcMultimediaParaDBEntity;

import java.util.List;


public interface LcMultimediaParaDao extends BaseDao<LcMultimediaParaDBEntity>{
	/**
	 * 根据终端标识查找
	 * @param terminalId
	 * @return
	 */
	public List<LcMultimediaParaDBEntity> getMultimediaPara(long terminalId);

	/**
	 *
	 * @param terminalIds
	 * @param multimediaType
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	abstract List<LcMultimediaParaDBEntity> getMultimedia(long[] terminalIds,
														  int multimediaType, long beginTime, long endTime, int currentPage, int pageSize);

}

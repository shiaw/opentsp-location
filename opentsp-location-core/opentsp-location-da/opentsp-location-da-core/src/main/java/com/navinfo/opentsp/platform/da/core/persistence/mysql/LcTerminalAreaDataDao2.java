package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDataDBEntity;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaData.AreaData;

import java.util.List;



public interface LcTerminalAreaDataDao2 extends BaseDao<LcTerminalAreaDataDBEntity> {
	/**
	 * 根据区域id查找
	 * @param taId
	 * @return
	 */
	public List<LcTerminalAreaDataDBEntity> getTerminalAreaData(int taId)throws Exception;

	/**
	 * 根据区域标识删除区域数据
	 * @param originalAreaId
	 */
	public void deleteByAreaId(long terminalId,int... originalAreaId)  throws Exception ;

	/**
	 * @param areaDataList AreaData
	 * @param taId 区域Area 区域表主键
	 */
	public void saveAreaData(List<AreaData> areaDataList, int taId) throws Exception ;

	public void deleteByTaId(Integer taId) throws Exception ;


}

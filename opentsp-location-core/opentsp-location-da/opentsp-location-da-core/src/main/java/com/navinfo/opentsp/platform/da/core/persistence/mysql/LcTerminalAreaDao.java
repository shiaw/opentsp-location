package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaAndDataDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDBEntity;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo;

import java.util.List;


public interface LcTerminalAreaDao extends BaseDao<LcTerminalAreaDBEntity> {
	/**
	 * 添加区域信息
	 * @param areaInfo
	 * @return
	 */
	public boolean addAreaInfo(AreaInfo areaInfo);

	/**
	 * 根据终端标识查找
	 *
	 * @param terminalId
	 * @return
	 */
	public List<LcTerminalAreaDBEntity> getAreaInfo(long terminalId);
	/**
	 * 根据终端标识和区域标识查找
	 * @param terminalId
	 * @param areaId
	 * @return
	 */
	public LcTerminalAreaDBEntity getAreaInfo(long terminalId,int areaId);
	/**
	 * 根据服务区域和taIp获得区域信息
	 * @param districtCode
	 * @param node_code
	 * @return
	 */
	public List<LcTerminalAreaDBEntity> getAreaInfo(int districtCode,
													int... node_code);


	/**
	 * 根据区域类型和终端标识查找
	 * @param terminalId
	 * @param areaType
	 * @return
	 */
	public List<LcTerminalAreaDBEntity> getAreaInfoByType(long terminalId,int areaType);

	/**
	 * 根据终端标识和区域标识删除
	 * @param terminalId
	 * @param areaIdentify
	 * @return
	 */
	public void deleteAreaInfo(long terminalId, int... areaIdentify);
	/**
	 * 根据终端标识和区域类型删除
	 * @param terminalId
	 * @param areaIdentify
	 * @return
	 */
	public void deleteAreaInfo(long terminalId, int areaType);
	/**
	 * 根据参数查询区域
	 * @param terminalId 终端标识
	 * @param originalAreaId 区域标识
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return List< LcTerminalAreaDBEntity>
	 */
	public List<LcTerminalAreaDBEntity> queryTerminalAreaRes(
			long terminalId, int originalAreaId, long start, long end,int currentPage,int pageSize);

	public List<LcTerminalAreaAndDataDBEntity> getAreaInfoByTerminalId(long[] terminalIds);
}

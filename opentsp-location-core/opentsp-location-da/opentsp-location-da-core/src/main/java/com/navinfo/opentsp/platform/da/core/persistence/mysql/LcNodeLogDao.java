package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeLogDBEntity;

import java.util.List;


public interface LcNodeLogDao extends BaseDao<LcNodeLogDBEntity> {
	/**
	 *
	 * @param nodeCode
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	abstract List<LcNodeLogDBEntity> findNodeLogByCode(int nodeCode, long beginTime,
													   long endTime);

	/**
	 *
	 * @param nodeType
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	abstract List<LcNodeLogDBEntity> findNodeLogByType(int nodeType, long beginTime,
													   long endTime);

	/**
	 *
	 * @param nodeIp
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	abstract List<LcNodeLogDBEntity> findNodeLogByIp(String nodeIp, long beginTime,
													 long endTime);
	/**
	 * 根据参数节点日志
	 * @param nodeCode 节点标识
	 * @param logDistrict 服务区域
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return List< LcNodeLogDBEntity>
	 */
	public List<LcNodeLogDBEntity> queryNodeLogList(int nodeCode, long start,
													long end, int logDistrict,int currentPage,int pageSize);

	public int queryNodeLogCount(int nodeCode, long start,
								 long end, int logDistrict);
}

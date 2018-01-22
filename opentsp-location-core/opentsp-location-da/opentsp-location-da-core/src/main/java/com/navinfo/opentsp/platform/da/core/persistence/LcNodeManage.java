package com.navinfo.opentsp.platform.da.core.persistence;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceDistrictConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeConfigDBEntity;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;

import java.util.List;

public interface LcNodeManage {
	/**
	 * 查询服务配置数量
	 * @param userName
	 * @param ip
	 * @param district
	 * @param start
	 * @param end
	 * @return
	 */
	public int queryServiceConfigResCount(String userName,
										  String ip, int district, long start, long end,int flag);
	/**
	 * 获取服务区域编码下的节点信息
	 * @param districtCode 服务区域编码
	 * @return List<{@link LcNodeConfigDBEntity}> 服务区域下的所有节点配置信息
	 */
	public List<LcNodeConfigDBEntity> getNodeInfo(int districtCode);

	/**
	 * 根据节点类型获取节点列表
	 * @param type
	 * @return
	 */
	public List<LcNodeConfigDBEntity> getNodesByNodeType(int type);

	/**
	 * 获取服务配置列表
	 * @param userName
	 * @param ip
	 * @param district
	 * @param start
	 * @param end
	 * @return
	 */
	public List<LcServiceConfigDBEntity> queryServiceConfigRes(String userName,
															   String ip, int district, long start, long end,int flag,int currentPage,int pageSize);

	/**
	 * 获取节点配置列表
	 * @param district
	 * @param nodeCode
	 * @return
	 */
	public List<LcNodeConfigDBEntity> queryNodeConfigRes(int district,
														 int nodeCode,int nodeType,int currentPage,int pageSize,int nodetype);
	/**
	 * 获取节点配置列表记录数
	 * @param district
	 * @param nodeCode
	 * @return
	 */
	public int queryNodeConfigResCount(int district,
									   int nodeCode,int nodetype);
	/**
	 * 根据用户名删除服务配置
	 * @param authName
	 * @return
	 */
	public PlatformResponseResult deleteServiceConfigByAuthName(String authName);

	/**
	 * 根据节点标识删除节点配置
	 * @param nodeCode
	 * @return
	 */
	public PlatformResponseResult deleteNodeConfigByNodeCode(int nodeCode);
	/**
	 * 根据主键查找服务配置信息（这个可以修改成按用户名查找）
	 * @param sc_id
	 * @return
	 */
	public LcServiceConfigDBEntity queryServiceConfigById(int sc_id);
	/**
	 * 根据服务配置主键查找服务所在分区
	 * @param sc_id
	 * @return
	 */
	public List<LcServiceDistrictConfigDBEntity> queryServiceInDistrict(
			int sc_id);
	/**
	 * 根据主键查找结点信息（这个可以修改成按节点标识查找）
	 * @param nc_id
	 * @return
	 */
	public LcNodeConfigDBEntity queryNodeConfigById(int nc_id);

	/**
	 * 修改或者新增服务配置信息
	 * @param serviceConfig
	 * @param district
	 * @return
	 */
	public PlatformResponseResult saveOrUpdateServiceConfig(
			LcServiceConfigDBEntity serviceConfig, int[] district,String authName);

	/**
	 *修改或者新增节点配置信息
	 * @param nodeConfig
	 * @return
	 */
	public PlatformResponseResult saveOrUpdateNodeConfig(
			LcNodeConfigDBEntity nodeConfig);
	/**
	 * 删除节点配置信息（可以换成按节点标识删除的）
	 * @param primaryKeys
	 * @return
	 */
	public PlatformResponseResult deleteNodeConfig(int[] primaryKeys);

	/**
	 * 删除节点配置信息（可以换成按用户名删除的）
	 * @param primaryKeys
	 * @return
	 */
	public PlatformResponseResult deleteServiceConfig(int[] primaryKeys);
}

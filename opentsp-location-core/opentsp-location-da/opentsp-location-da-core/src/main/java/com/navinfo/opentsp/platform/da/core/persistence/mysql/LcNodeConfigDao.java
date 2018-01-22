package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeConfigDBEntity;

import java.util.List;


public interface LcNodeConfigDao extends BaseDao<LcNodeConfigDBEntity>{
	/**
	 * 根据节点编号和服务区域查找
	 * @param node_code
	 * @return
	 */
	public List<LcNodeConfigDBEntity> getbyNodeCode(int node_code,int district,int nodeType,int currentPage,int pageSize,int nodetype);
	/**
	 * 获取节点配置列表记录数
	 * @param node_code
	 * @param district
	 * @param nodeType
	 * @return
	 */
	public int queryNodeConfigResCount(int node_code,int district,int nodetype);

	/**
	 * 根据ip和服务区域查找
	 * @param ip_address
	 * @return
	 */
	public LcNodeConfigDBEntity getbyIp(String ip_address,int district);
	/**
	 * 根据节点类型和服务区域查找
	 * @param node_type
	 * @return
	 */
	public List<LcNodeConfigDBEntity> getbyNodeType(int node_type,int district);
	/**
	 * 根据服务区域查找
	 * @param district
	 * @return
	 */
	public List<LcNodeConfigDBEntity> getbyDistrict(int district);
	/**
	 * 根据修改用户查找，支持模糊查询
	 * @param userName
	 * @return
	 */
	public List<LcNodeConfigDBEntity> getbyUserName(String userName);

	/**
	 * 根据节点表示修改节点信息
	 * @param nodeConfig
	 * @return
	 */
	public int updateByNodeCode(LcNodeConfigDBEntity nodeConfig);

	/**
	 * 根据节点标识删除节点信息
	 * @param nodeCode
	 */
	public void deleteByNodeCode(int nodeCode);

	/**
	 * 根据服务区域查找
	 * @param district
	 * @return
	 */
	public List<LcNodeConfigDBEntity> getbyDistrictMM(int type);
}

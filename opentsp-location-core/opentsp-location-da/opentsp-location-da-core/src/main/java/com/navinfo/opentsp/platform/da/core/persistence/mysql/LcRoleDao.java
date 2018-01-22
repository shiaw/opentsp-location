package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcRoleDBEntity;

import java.util.List;


public interface LcRoleDao extends BaseDao<LcRoleDBEntity>{
	/**
	 * 根据角色名称查找，支持模糊查询
	 * @param role_name
	 * @return
	 */
	public LcRoleDBEntity getRoleInfo(String role_name);
	/**
	 * 查询角色信息
	 * @param role_name
	 * @return
	 */
	public List<LcRoleDBEntity> queryRoleList(String role_name,int currentPage,int pageSize);
	/**
	 * 查询角色数量
	 * @param role_name
	 * @return
	 */
	public int queryRoleCount(String role_name,int flag);

}

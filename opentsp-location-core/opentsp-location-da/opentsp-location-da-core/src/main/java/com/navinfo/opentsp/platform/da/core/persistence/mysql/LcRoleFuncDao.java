package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcRoleFuncDBEntity;

import java.util.List;


public interface LcRoleFuncDao extends BaseDao<LcRoleFuncDBEntity> {
	/**
	 * 根据角色id查找
	 * @param role_id
	 * @return
	 */
	public List<LcRoleFuncDBEntity> getRoleFuncByRoleId(int role_id);

	/**
	 * 根据功能id查找
	 * @param func_id
	 * @return
	 */
	public List<LcRoleFuncDBEntity> getRoleFuncByFuncId(int func_id);
	/**
	 * 根据角色ID删除映射表
	 * @param roleId
	 * @return
	 */
	public int  deleteRoleFuncByRoleId(int roleId);
	/**
	 * 根据功能删除映射表
	 * @param functId
	 * @return
	 */
	public int  deleteRoleFuncByFuncId(int funcId);

}

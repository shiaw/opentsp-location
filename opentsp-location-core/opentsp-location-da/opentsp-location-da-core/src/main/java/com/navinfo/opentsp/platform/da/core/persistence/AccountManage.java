package com.navinfo.opentsp.platform.da.core.persistence;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcFuncDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcRoleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcUserDBEntity;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebParam.Mode;




public interface AccountManage {
	/**
	 * 根据参数查询用户数量
	 * @param userName
	 * @param password
	 * @param roleId
	 */
	public int queryUserCount(String userName,String password,int roleId);
	/**
	 * 根据参数查询系统功能记录数
	 * @param funcName
	 * @return
	 */
	public int queryFuncCount(String funcName);
	/**
	 * 根据参数查询角色数量
	 * @param role_name
	 * @return
	 */
	public int queryRoleCount(String role_name,int flag);
	/**
	 * 根据参数查询用户基本信息
	 *
	 * @param userName
	 * @param roleId
	 * @return List<LcUserDBEntity>
	 */
	public List<LcUserDBEntity> querUserList(String userName,String password,int roleId,int currentPage,int pageSize);
	/**
	 * 根据主键Id查询用户基本信息
	 *
	 * @param userName
	 * @param roleId
	 * @return List<LcUserDBEntity>
	 */
	public LcUserDBEntity querUserById(int userId);
	/**
	 * 新增（修改）用户基本信息
	 *
	 * @param lcUserDBEntity
	 * @return
	 */
	public PlatformResponseResult saveOrUpdateUser(LcUserDBEntity lcUserDBEntity);

	/**
	 * (批量)删除用户基本信息
	 *
	 * @param userId
	 * @return
	 */
	public PlatformResponseResult deleteUser(int[] userId);

	/**
	 * 根据“功能名称”查询系统功能信息(参数为空时加载所有系统功能)
	 *
	 * @param funcName
	 * @return
	 */
	public List<LcFuncDBEntity> queryFuncList(String funcName,int currentPage,int pageSize);

	/**
	 * 新增（修改）系统功能
	 *
	 * @param lcUserDBEntity
	 * @return
	 */
	public PlatformResponseResult saveOrUpdateFunc(LcFuncDBEntity lcFuncDBEntity);

	/**
	 * (批量)删除系统功能
	 *
	 * @param userId
	 * @return
	 */
	public PlatformResponseResult deleteFunc(int[] funcId);

	/**
	 * 查询角色信息
	 *
	 * @param role_name
	 * @return
	 */
	public List<LcRoleDBEntity> queryRoleList(String role_name,int currentPage,int pageSize);
	/**
	 * 根据主键ID查询角色信息
	 *
	 * @param roleId
	 * @return
	 */
	public LcRoleDBEntity queryRoleById(int roleId);

	/**
	 * 新增（修改）角色信息
	 *
	 * @param lcUserDBEntity
	 * @return
	 */
	public PlatformResponseResult saveOrUpdateRole(LcRoleDBEntity lcRoleDBEntity);

	/**
	 * (批量)删除角色
	 *
	 * @param userId
	 * @return
	 */
	public PlatformResponseResult deleteRole(
			@WebParam(name = "roleId", mode = Mode.IN) int[] roleId);

	/**
	 * 根据角色查询功能列表
	 *
	 * @param role_name
	 * @return
	 */
	public List<LcFuncDBEntity> queryRoleFuncList(int roleId);

	/**
	 * 角色绑定功能
	 *
	 * @param userId
	 * @return
	 */
	public PlatformResponseResult bindRoleAndFun(int roleId,int[] funcId);
	/**
	 * 根据父级ID查询功能信息
	 * @return
	 */
	public List<LcFuncDBEntity> selectFuncByParent_func(int parent_func);
	/**
	 * 根据主键查询功能信息
	 */
	public LcFuncDBEntity selectFuncById(int func_id);
	public PlatformResponseResult updateUserPasswod(String userId,
													String password);

}

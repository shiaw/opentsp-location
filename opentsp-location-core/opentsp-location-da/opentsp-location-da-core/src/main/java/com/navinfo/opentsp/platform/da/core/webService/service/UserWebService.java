package com.navinfo.opentsp.platform.da.core.webService.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebParam.Mode;

import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcFuncDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcRoleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcUserDBEntity;


@WebService
public interface UserWebService {
	/**
	 * 根据参数查询用户数量
	 * @param userName
	 * @param password
	 * @param roleId
	 */
	public int queryUserCount(
			@WebParam(name = "userName", mode = Mode.IN) String userName,
			@WebParam(name = "password", mode = Mode.IN) String password,
			@WebParam(name = "roleId", mode = Mode.IN) int roleId
	);
	/**
	 * 根据参数查询系统功能总记录数
	 * @param funcName
	 * @return
	 */
	public int queryFuncCount(
			@WebParam(name = "funcName", mode = Mode.IN) String funcName);
	/**
	 * 查询角色信息记录数
	 *
	 * @param role_name
	 * @return
	 */
	public int queryRoleCount(
			@WebParam(name = "role_name", mode = Mode.IN) String role_name,
			@WebParam(name = "flag", mode = Mode.IN) int flag);
	/**
	 * 根据参数查询用户基本信息
	 *
	 * @param userName
	 * @param roleId
	 * @return List<LcUserDBEntity>
	 */
	public List<LcUserDBEntity> querUserList(
			@WebParam(name = "userName", mode = Mode.IN) String userName,
			@WebParam(name = "password", mode = Mode.IN) String password,
			@WebParam(name = "roleId", mode = Mode.IN) int roleId,
			@WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
			@WebParam(name = "pageSize", mode = Mode.IN) int pageSize);
	/**
	 * 根据主键Id查询用户基本信息
	 *
	 * @param userName
	 * @param roleId
	 * @return List<LcUserDBEntity>
	 */
	public LcUserDBEntity querUserById(
			@WebParam(name = "userId", mode = Mode.IN) int userId);
	/**
	 * 新增（修改）用户基本信息
	 *
	 * @param lcUserDBEntity
	 * @return
	 */
	public PlatformResponseResult saveOrUpdateUser(
			@WebParam(name = "lcUserDBEntity", mode = Mode.IN) LcUserDBEntity lcUserDBEntity);

	public PlatformResponseResult updateUserPasswod(
			@WebParam(name = "userId", mode = Mode.IN) String userId,
			@WebParam(name = "password", mode = Mode.IN)  String password);
	/**
	 * (批量)删除用户基本信息
	 *
	 * @param userId
	 * @return
	 */
	public PlatformResponseResult deleteUser(
			@WebParam(name = "userId", mode = Mode.IN) int[] userId);

	/**
	 * 根据“功能名称”查询系统功能信息(参数为空时加载所有系统功能)
	 *
	 * @param funcName
	 * @return
	 */
	public List<LcFuncDBEntity> queryFuncList(
			@WebParam(name = "funcName", mode = Mode.IN) String funcName,
			@WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
			@WebParam(name = "pageSize", mode = Mode.IN) int pageSize);

	/**
	 * 新增（修改）系统功能
	 *
	 * @param lcUserDBEntity
	 * @return
	 */
	public PlatformResponseResult saveOrUpdateFunc(
			@WebParam(name = "lcFuncDBEntity", mode = Mode.IN) LcFuncDBEntity lcFuncDBEntity);

	/**
	 * (批量)删除系统功能
	 *
	 * @param userId
	 * @return
	 */
	public PlatformResponseResult deleteFunc(
			@WebParam(name = "funcId", mode = Mode.IN) int[] funcId);

	/**
	 * 查询角色信息
	 *
	 * @param role_name
	 * @return
	 */
	public List<LcRoleDBEntity> queryRoleList(
			@WebParam(name = "role_name", mode = Mode.IN) String role_name,
			@WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
			@WebParam(name = "pageSize", mode = Mode.IN) int pageSize);
	/**
	 * 根据主键ID查询角色信息
	 *
	 * @param roleId
	 * @return
	 */
	public LcRoleDBEntity queryRoleById(@WebParam(name = "roleId", mode = Mode.IN)int roleId);

	/**
	 * 新增（修改）角色信息
	 *
	 * @param lcUserDBEntity
	 * @return
	 */
	public PlatformResponseResult saveOrUpdateRole(
			@WebParam(name = "lcRoleDBEntity", mode = Mode.IN) LcRoleDBEntity lcRoleDBEntity);

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
	public List<LcFuncDBEntity> queryRoleFuncList(
			@WebParam(name = "roleId", mode = Mode.IN) int roleId);

	/**
	 * 角色绑定功能
	 *
	 * @param userId
	 * @return
	 */
	public PlatformResponseResult bindRoleAndFun(
			@WebParam(name = "roleId", mode = Mode.IN) int roleId,
			@WebParam(name = "funcId", mode = Mode.IN) int[] funcId);
	/**
	 * 根据父级ID查询功能信息
	 * @return
	 */
	public List<LcFuncDBEntity> selectFuncByParent_func(@WebParam(name = "parent_func", mode = Mode.IN) int parent_func);
	/**
	 * 根据主键查询功能信息
	 */
	public LcFuncDBEntity selectFuncById(@WebParam(name = "func_id", mode = Mode.IN) int func_id);
}

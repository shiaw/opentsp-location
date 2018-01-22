package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcUserDBEntity;

import java.util.List;


public interface LcUserDao extends BaseDao<LcUserDBEntity> {
	/**
	 * 根据用户名和密码查找用户
	 * @param username {@link String} 用户名
	 * @param password {@link String} 密码
	 * @return {@link LcUserDBEntity} 用户实体
	 */
	public LcUserDBEntity getUserInfo(String username);
	/**
	 * 根据角色id获取用户信息
	 * @param role_id {@link Integer} 角色id
	 * @return {@link List<{@link LcUserDBEntity}> 用户集合
	 */
	public List<LcUserDBEntity> getUserInfo(int role_id);

	/**
	 * 根据用户状态查找
	 * @param data_status
	 * @return
	 */
	public List<LcUserDBEntity> getUserInfoByStatus(int data_status);
	/**
	 * 根据参数查询用户基本信息
	 * @param userName
	 * @param roleId
	 * @return
	 */
	public List<LcUserDBEntity> querUserList(String userName, String password, int roleId, int currentPage, int pageSize);
	/**
	 * 查询用户数量
	 * @param userName
	 * @param password
	 * @param roleId
	 * @return
	 */
	public int queryUserCount(String userName, String password, int roleId);
}

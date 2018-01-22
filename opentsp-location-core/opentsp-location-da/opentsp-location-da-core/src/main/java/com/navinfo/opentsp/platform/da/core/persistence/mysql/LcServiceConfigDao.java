package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceConfigDBEntity;

import java.util.List;


public interface LcServiceConfigDao extends BaseDao<LcServiceConfigDBEntity>{
	/**
	 * 根据用户名查找
	 * @param auth_name
	 * @return
	 */
	public LcServiceConfigDBEntity getServiceConfig(String auth_name);

	/**
	 * 根据ip查找
	 * @param ip
	 * @return
	 */
	public LcServiceConfigDBEntity getServiceConfigByIp(String ip);
	/**
	 * 根据修改用户查找
	 * @param username
	 * @return
	 */
	public List<LcServiceConfigDBEntity> getServiceConfigByUpdateUser(String username);
	/**
	 * 根据服务状态查找
	 * @param stats
	 * @return
	 */
	public List<LcServiceConfigDBEntity> getServiceConfig(int stats);

	/**
	 * 查询服务信息
	 * @param userName 用户名
	 * @param ip ip地址
	 * @param district 服务区域
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return
	 */
	public List<LcServiceConfigDBEntity> queryServiceConfigRes(String userName,
															   String ip, int district, long start, long end,int flag,int currentPage,int pageSize);
	/**
	 * 根据用scid修改服务信息(用于修改用户名)
	 * @param serviceConfig
	 * @return
	 */
	public int updatebyScid(LcServiceConfigDBEntity serviceConfig);
	/**
	 * 根据用户名修改服务信息
	 * @param serviceConfig
	 * @return
	 */
	public int updatebyAuthName(LcServiceConfigDBEntity serviceConfig);

	/**
	 * 根据用户名删除服务信息
	 * @param authName
	 * @return
	 */
	public void deleteByAuthName(String authName);
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

}

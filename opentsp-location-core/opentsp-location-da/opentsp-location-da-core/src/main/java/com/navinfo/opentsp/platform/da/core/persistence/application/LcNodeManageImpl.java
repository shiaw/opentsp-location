package com.navinfo.opentsp.platform.da.core.persistence.application;

import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcNodeConfigDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcServiceConfigDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.LcNodeManage;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceDistrictConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcServiceConfigDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcServiceDistrictConfigDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcNodeConfigDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcServiceDistrictConfigDaoImpl;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;

import java.util.ArrayList;
import java.util.List;


public class LcNodeManageImpl implements LcNodeManage {
	@Override
	public int queryServiceConfigResCount(String userName,
			String ip, int district, long start, long end,int flag){
		try {
			MySqlConnPoolUtil.startTransaction();
			LcServiceConfigDao serviceConfigDao = new LcServiceConfigDaoImpl();
			int count = serviceConfigDao.queryServiceConfigResCount(userName,
					ip, district, start, end,flag);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	/**
	 * 根据服务区域查找
	 * @param districtCode 服务区域编码
	 * @return
	 */
	@Override
	public List<LcNodeConfigDBEntity> getNodeInfo(int districtCode) {
		try {
			// 获取数据库连接
			MySqlConnPoolUtil.startTransaction();
			// 判断是否district是否为0
			if (districtCode == 0) {
				return null;
			} else {
				LcNodeConfigDao lcNodeConfigDao = new LcNodeConfigDaoImpl();
				return lcNodeConfigDao.getbyDistrict(districtCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcNodeConfigDBEntity> getNodesByNodeType(int type) {
		try {
			// 获取数据库连接
			MySqlConnPoolUtil.startTransaction();
			// 判断是否district是否为0
			if (type == 0) {
				return null;
			} else {
				LcNodeConfigDao lcNodeConfigDao = new LcNodeConfigDaoImpl();
				return lcNodeConfigDao.getbyDistrictMM(type);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcServiceConfigDBEntity> queryServiceConfigRes(String userName,
			String ip, int district, long start, long end,int flag,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcServiceConfigDao serviceConfigDao = new LcServiceConfigDaoImpl();
			List<LcServiceConfigDBEntity> serviceConfigs = new ArrayList<LcServiceConfigDBEntity>();
			serviceConfigs = serviceConfigDao.queryServiceConfigRes(userName,
					ip, district, start, end,flag,currentPage,pageSize);
			return serviceConfigs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public int queryNodeConfigResCount(int district,int nodeCode,int nodetype){
		try {
			MySqlConnPoolUtil.startTransaction();
			LcNodeConfigDao nodeConfigDao = new LcNodeConfigDaoImpl();
			int count = nodeConfigDao.queryNodeConfigResCount(district,
					nodeCode,nodetype);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcNodeConfigDBEntity> queryNodeConfigRes(int district,
			int nodeCode,int nodeType,int currentPage,int pageSize,int nodetype) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcNodeConfigDao nodeConfigDao = new LcNodeConfigDaoImpl();
			List<LcNodeConfigDBEntity> nodeConfigs = new ArrayList<LcNodeConfigDBEntity>();
			nodeConfigs = nodeConfigDao.getbyNodeCode(nodeCode, district,nodeType,currentPage,pageSize,nodetype);
			return nodeConfigs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public LCPlatformResponseResult.PlatformResponseResult deleteServiceConfigByAuthName(String authName) {

		try {
			MySqlConnPoolUtil.startTransaction();
			LcServiceConfigDao lcServiceConfigDao = new LcServiceConfigDaoImpl();
			lcServiceConfigDao.deleteByAuthName(authName);
			MySqlConnPoolUtil.commit();
			return LCPlatformResponseResult.PlatformResponseResult.success;
		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}

	}

	@Override
	public LCPlatformResponseResult.PlatformResponseResult deleteNodeConfigByNodeCode(int nodeCode) {

		try {
			MySqlConnPoolUtil.startTransaction();
			LcNodeConfigDao lcNodeConfigDao = new LcNodeConfigDaoImpl();
			lcNodeConfigDao.deleteByNodeCode(nodeCode);
			MySqlConnPoolUtil.commit();
			return LCPlatformResponseResult.PlatformResponseResult.success;
		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}

	}

	@Override
	public LcServiceConfigDBEntity queryServiceConfigById(int sc_id) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcServiceConfigDao serviceConfigDao = new LcServiceConfigDaoImpl();
			LcServiceConfigDBEntity serviceConfig = serviceConfigDao.findById(
					sc_id, LcServiceConfigDBEntity.class);
			return serviceConfig;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public List<LcServiceDistrictConfigDBEntity> queryServiceInDistrict(
			int sc_id) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcServiceDistrictConfigDao serviceDistrictConfigDao = new LcServiceDistrictConfigDaoImpl();
			List<LcServiceDistrictConfigDBEntity> result = serviceDistrictConfigDao
					.getByScId(sc_id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public LcNodeConfigDBEntity queryNodeConfigById(int nc_id) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcNodeConfigDao nodeConfigDao = new LcNodeConfigDaoImpl();
			return nodeConfigDao.findById(nc_id, LcNodeConfigDBEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public LCPlatformResponseResult.PlatformResponseResult saveOrUpdateServiceConfig(
			LcServiceConfigDBEntity serviceConfig, int[] district,String authName) {

		//总部新增时，则判断用户名是否存在，存在则修改，不存在则新增
		//修改时可分为2种情况：①分区已经存在用户名的数据，进行修改 ②分区为修改新分区，没有用户的数据，则添加一条数据。
		//由于总部和分区用的同一个方法，由此可简化为：先查询用户名是否存在，存在则修改，不存在则判断用户名是否修改，否则新增。
		try {
			MySqlConnPoolUtil.startTransaction();
			LcServiceConfigDao lcServiceConfigDao = new LcServiceConfigDaoImpl();
			LcServiceDistrictConfigDao lcServiceDistrictConfigDao = new LcServiceDistrictConfigDaoImpl();
			int result;
			List<LcServiceConfigDBEntity> list = lcServiceConfigDao
					.queryServiceConfigRes(authName,null,0,0,0,1,0,0);
			if(list!=null&&list.size()>0){
				serviceConfig.setSc_id(list.get(0).getSc_id());
				if(authName.equals(serviceConfig.getAuth_name())){
					result = lcServiceConfigDao.updatebyAuthName(serviceConfig);
				}else{
					result = lcServiceConfigDao.updatebyScid(serviceConfig);
				}
				lcServiceDistrictConfigDao.deleteByService(result);
				for (int i : district) {
					LcServiceDistrictConfigDBEntity lcServiceDistrictConfig = new LcServiceDistrictConfigDBEntity();
					lcServiceDistrictConfig.setSc_id(result);
					lcServiceDistrictConfig.setService_district(i);
					lcServiceDistrictConfigDao.add(lcServiceDistrictConfig);
				}
			}else{
				result = lcServiceConfigDao.add(serviceConfig);
				for (int i : district) {
					LcServiceDistrictConfigDBEntity lcServiceDistrictConfig = new LcServiceDistrictConfigDBEntity();
					lcServiceDistrictConfig.setSc_id(result);
					lcServiceDistrictConfig.setService_district(i);
					lcServiceDistrictConfigDao.add(lcServiceDistrictConfig);
				}
			}
			if (result != 0) {
				MySqlConnPoolUtil.commit();
				return LCPlatformResponseResult.PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return LCPlatformResponseResult.PlatformResponseResult.failure;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}

	}

	@Override
	public LCPlatformResponseResult.PlatformResponseResult saveOrUpdateNodeConfig(
			LcNodeConfigDBEntity nodeConfig) {
		try {

			MySqlConnPoolUtil.startTransaction();
			LcNodeConfigDao lcNodeConfigDao = new LcNodeConfigDaoImpl();
			int result = 0;
			//①节点信息保存时，只需把数据同步到对应分区库即可。add。
		    //②节点修改时，分两种情况。一、分区不改变时，只需修改。二、分区改变时，需把旧分区节点信息删除（旧分区逻辑操作删除），新分区库添加一条节点信息。
			List<LcNodeConfigDBEntity> list = lcNodeConfigDao.getbyNodeCode(nodeConfig.getNode_code(), 0, 1, 0, 0, 0);
			if(list!=null&&list.size()>0){
				result = lcNodeConfigDao.updateByNodeCode(nodeConfig);
			}else{
				result = lcNodeConfigDao.add(nodeConfig);
			}
			/*if (nodeConfig.getNc_id() == null || nodeConfig.getNc_id() == 0) {
				result = lcNodeConfigDao.add(nodeConfig);
			} else {
				result = lcNodeConfigDao.updateByNodeCode(nodeConfig);
			}*/
			if (LCPlatformResponseResult.PlatformResponseResult.success_VALUE == result) {
				MySqlConnPoolUtil.commit();
				return LCPlatformResponseResult.PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return LCPlatformResponseResult.PlatformResponseResult.failure;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public LCPlatformResponseResult.PlatformResponseResult deleteNodeConfig(int[] primaryKeys) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcNodeConfigDao lcNodeConfigDao = new LcNodeConfigDaoImpl();
			int result=lcNodeConfigDao.delete(LcNodeConfigDBEntity.class, primaryKeys);
			if(result== LCPlatformResponseResult.PlatformResponseResult.success_VALUE){
				MySqlConnPoolUtil.commit();
				return LCPlatformResponseResult.PlatformResponseResult.success;
			}else{
				MySqlConnPoolUtil.rollback();
				return LCPlatformResponseResult.PlatformResponseResult.failure;
			}
		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public LCPlatformResponseResult.PlatformResponseResult deleteServiceConfig(int[] primaryKeys) {
		try {
			MySqlConnPoolUtil.startTransaction();
			LcServiceConfigDao lcServiceConfigDao = new LcServiceConfigDaoImpl();
			// 删除服务配置表的信息时没有删除所在分区表是因为服务配置是逻辑删除
			int result=lcServiceConfigDao.delete(LcServiceConfigDBEntity.class,
					primaryKeys);
			if(result== LCPlatformResponseResult.PlatformResponseResult.success_VALUE){
				MySqlConnPoolUtil.commit();
				return LCPlatformResponseResult.PlatformResponseResult.success;
			}else{
				MySqlConnPoolUtil.rollback();
				return LCPlatformResponseResult.PlatformResponseResult.failure;
			}
		} catch (Exception e) {
			e.printStackTrace();
			MySqlConnPoolUtil.rollback();
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

}

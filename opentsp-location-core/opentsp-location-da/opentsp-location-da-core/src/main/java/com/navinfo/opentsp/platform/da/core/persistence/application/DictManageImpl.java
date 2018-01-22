package com.navinfo.opentsp.platform.da.core.persistence.application;

import com.navinfo.opentsp.platform.da.core.persistence.DictManage;
import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.DictData;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictTypeDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDistrictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalProtoMappingDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcDictDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcDictTypeDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcDistrictDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalProtoMappingDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcDictDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcDictTypeDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcDistrictDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcTerminalProtoMappingDaoImpl;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;

import java.util.List;


public class DictManageImpl implements DictManage {

	@Override
	public List<DictData> getDictDataRes(int dictType) {
		try {
			MySqlConnPoolUtil.startTransaction();
			if (dictType == 0) {
				return null;
			} else {
				LcDictDao lcDictDao = new LcDictDaoImpl();
				return lcDictDao.getDictDataRes(dictType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	public LCPlatformResponseResult.PlatformResponseResult saveOrUpdateDict(LcDictDBEntity lcDictDBEntity) {
		int result;
		try {
			MySqlConnPoolUtil.startTransaction();
			LcDictDao LcDictDao=new LcDictDaoImpl();
			//判断字典编码是否存在。存在则修改。
			List<LcDictDBEntity> list = LcDictDao.queryDictList("", lcDictDBEntity.getDict_code());
			if(list!=null&&list.size()>0){
				lcDictDBEntity.setDict_id(list.get(0).getDict_id());
				result=LcDictDao.update(lcDictDBEntity);
			}else{
				result= LcDictDao.add(lcDictDBEntity);
			}
			if (result == LCPlatformResponseResult.PlatformResponseResult.success_VALUE) {
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
	public List<LcDictDBEntity> queryDictList(String dtName, int dictId) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDictDaoImpl().queryDictList(dtName, dictId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcDictTypeDBEntity> queryDictTypeList(String dtName, int dictId) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDictDaoImpl().queryDictTypeList(dtName, dictId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcDictTypeDBEntity> queryDictTypeList(String dtName) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDictTypeDaoImpl().queryDictTypeList(dtName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}

	}
	@Override
	public List<LcTerminalProtoMappingDBEntity> getByLogicCode(int logic_code) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcTerminalProtoMappingDaoImpl()
					.getByLogicCode(logic_code);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}

	}
	@Override
	public List<LcDistrictDBEntity> queryDistrictList(String dictCodeName) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDistrictDaoImpl().queryDistrictList(dictCodeName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcDictDBEntity> getDictByType(int dictType,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDictDaoImpl().getDictByType(dictType,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcDistrictDBEntity> queryDistrictByParent_code(int parent_code,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDistrictDaoImpl().queryDistrictByParent_code(parent_code,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public int queryDistrictCount(int parent_code) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDistrictDaoImpl().queryDistrictCount(parent_code);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcTerminalProtoMappingDBEntity> getByGroupLogicCode(
			int logic_code) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcTerminalProtoMappingDaoImpl().getByGroupLogicCode(logic_code);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public LCPlatformResponseResult.PlatformResponseResult deleteDict(int[] dictCode) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDistrictDaoImpl().deleteBydictCode(dictCode);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public LCPlatformResponseResult.PlatformResponseResult saveOrUpdateDictType(
			LcDictTypeDBEntity lcDictTypeDBEntity) {
		try {
			int result=0;
			MySqlConnPoolUtil.startTransaction();
			LcDictTypeDao LcDictTypeDao = new LcDictTypeDaoImpl();
			result = LcDictTypeDao.add(lcDictTypeDBEntity);
			if (result == LCPlatformResponseResult.PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
				return LCPlatformResponseResult.PlatformResponseResult.success;
			}else{
				MySqlConnPoolUtil.rollback();
				return LCPlatformResponseResult.PlatformResponseResult.failure;
			}
		} catch (Exception e) {
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public LCPlatformResponseResult.PlatformResponseResult deleteDictType(int[] dtId) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDistrictDaoImpl().deleteDictType(dtId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public LCPlatformResponseResult.PlatformResponseResult bindProtoMapping(int logicCode, int[] dictCode) {
		try {

			MySqlConnPoolUtil.startTransaction();
			// 删除原有逻辑
			LcTerminalProtoMappingDao terminalProtoMappingDao = new LcTerminalProtoMappingDaoImpl();
			terminalProtoMappingDao.deleteProtoMapping(logicCode);
			// 添加新逻辑
			LcTerminalProtoMappingDBEntity entity = new LcTerminalProtoMappingDBEntity();
			for (int i : dictCode) {
				entity.setLogic_code(logicCode);
				entity.setDict_code(i);
				terminalProtoMappingDao.add(entity);
			}
			MySqlConnPoolUtil.commit();
			return LCPlatformResponseResult.PlatformResponseResult.success;
		} catch (Exception e) {
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public LCPlatformResponseResult.PlatformResponseResult bindDistrict(int parentCode, int[] dictCode) {
		try {
			MySqlConnPoolUtil.startTransaction();
			// 删除原有逻辑
			LcDistrictDao lcDistrictDao = new LcDistrictDaoImpl();
			lcDistrictDao.deleteByParentCode(parentCode);
			// 添加新逻辑
			LcDistrictDBEntity entity = new LcDistrictDBEntity();
			for (int i : dictCode) {
				entity.setDict_code(i);
				entity.setParent_code(parentCode);
				lcDistrictDao.add(entity);
			}
			MySqlConnPoolUtil.commit();
			return LCPlatformResponseResult.PlatformResponseResult.success;
		} catch (Exception e) {
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public LCPlatformResponseResult.PlatformResponseResult deleteDistrict(int[] districtId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public LCPlatformResponseResult.PlatformResponseResult deleteProtoMapping(int[] tplId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public LCPlatformResponseResult.PlatformResponseResult deleteProtoMappingByDictcode(int[] logic_code) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据参数查询终端指令协议映射信息
	 *
	 * @param dictCodeName
	 *            协议类型code
	 * @return 实体对象
	 */
	@Override
	public List<LcTerminalProtoMappingDBEntity> getTerminalProtoMapping(int logic_code,int dictCode,int messageCode,int currentPage,int pageSize){
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalProtoMappingDao protoMappingDao=new LcTerminalProtoMappingDaoImpl();
			return protoMappingDao.getTerminalProtoMapping(logic_code,dictCode,messageCode,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			// 回滚事务
			MySqlConnPoolUtil.rollback();
		} finally {
			MySqlConnPoolUtil.close();
		}
		return null;
	}
	@Override
	public int selectPMListCount(int logic_code,int dictCode,int messageCode){
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalProtoMappingDao protoMappingDao=new LcTerminalProtoMappingDaoImpl();
			return protoMappingDao.selectPMListCount(logic_code,dictCode,messageCode);
		} catch (Exception e) {
			e.printStackTrace();
			// 回滚事务
			MySqlConnPoolUtil.rollback();
		} finally {
			MySqlConnPoolUtil.close();
		}
		return 0;
	}
	/**
	 *
	 * @param lcTerminalProtoMappingDBEntity
	 * @return
	 */
	public LCPlatformResponseResult.PlatformResponseResult saveOrUpdateTerminalProtoMapping(
			LcTerminalProtoMappingDBEntity lcTerminalProtoMappingDBEntity){
		int result;
		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalProtoMappingDao protoMappingDao=new LcTerminalProtoMappingDaoImpl();
			if(lcTerminalProtoMappingDBEntity.getTpl_d() == null ||  lcTerminalProtoMappingDBEntity.getTpl_d() == 0  ){
				result= protoMappingDao.add(lcTerminalProtoMappingDBEntity);
			}else {
				result=protoMappingDao.update(lcTerminalProtoMappingDBEntity);
			}
			if (result == LCPlatformResponseResult.PlatformResponseResult.success_VALUE) {
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
	/**
	 * 删除字典（支持批量删除）暂时不做
	 *
	 * @param dictId
	 *            字典Id
	 * @return 枚举
	 */
	public LCPlatformResponseResult.PlatformResponseResult deleteTerminalProtoMapping(int[] tplId,int messageCode){

		try {
			MySqlConnPoolUtil.startTransaction();
			LcTerminalProtoMappingDao protoMappingDao=new LcTerminalProtoMappingDaoImpl();
			protoMappingDao.deleteProtoMapping(tplId,messageCode);
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
	/**
	 * 协议绑定更新
	 *
	 * @param dictId
	 *            字典Id
	 * @return 枚举
	 */
	public LCPlatformResponseResult.PlatformResponseResult updateProtocoMapping(List<LcTerminalProtoMappingDBEntity> lcTerminalProtoMappingDBEntity){
		if(lcTerminalProtoMappingDBEntity!=null&&lcTerminalProtoMappingDBEntity.size()>0){
			LCPlatformResponseResult.PlatformResponseResult result=deleteTerminalProtoMapping(null,lcTerminalProtoMappingDBEntity.get(0).getMessage_code());
			if(result.equals(LCPlatformResponseResult.PlatformResponseResult.success)){
				for(LcTerminalProtoMappingDBEntity entity:lcTerminalProtoMappingDBEntity){
					saveOrUpdateTerminalProtoMapping(entity);
				}
			}
			return result;
		}
        return LCPlatformResponseResult.PlatformResponseResult.failure;
	}
	@Override
	public LcDictDBEntity getDictByCode(int dictCode) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDictDaoImpl().getDictByCode(dictCode);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public int getDictDataResCount(int dictType) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDictDaoImpl().getDictDataResCount(dictType);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcDictTypeDBEntity> queryDictTypeList(String dtName,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDictTypeDaoImpl().queryDictTypeList(dtName,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}

	}
	@Override
	public int queryDictTypeListCount(String dtName){
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcDictTypeDaoImpl().queryDictTypeListCount(dtName);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
}

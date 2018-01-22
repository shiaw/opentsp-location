package com.navinfo.opentsp.platform.da.core.persistence.application;

import java.util.List;

import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.da.core.persistence.AccountManage;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcFuncDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcRoleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcRoleFuncDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcUserDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.BaseDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcFuncDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcRoleDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcRoleFuncDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcUserDaoImpl;

public class AccountManageImpl implements AccountManage {
	@Override
	public int queryUserCount(String userName,String password,int roleId){
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcUserDaoImpl().queryUserCount(userName,password, roleId);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public int queryFuncCount(String funcName){
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcFuncDaoImpl().queryFuncCount(funcName);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlConnPoolUtil.close();
		}	
	}
	@Override
	public int queryRoleCount(String role_name,int flag) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcRoleDaoImpl().queryRoleCount(role_name,flag);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			MySqlConnPoolUtil.close();
		}		
	}
	@Override
	public List<LcUserDBEntity> querUserList(String userName,String password,int roleId,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcUserDaoImpl().querUserList(userName,password, roleId,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public PlatformResponseResult saveOrUpdateUser(LcUserDBEntity lcUserDBEntity) {
		int result;
		MySqlConnPoolUtil.startTransaction();
		try {
			if( lcUserDBEntity.getUser_id()!=null && lcUserDBEntity.getUser_id()!=0  ){
				result= new BaseDaoImpl<LcUserDBEntity>().update(lcUserDBEntity);
			}else {
				result=new BaseDaoImpl<LcUserDBEntity>().add(lcUserDBEntity);
			}
			if (result ==PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
				return PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return PlatformResponseResult.failure;
			}
		} catch (Exception e) {
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}

	}
	@Override
	public PlatformResponseResult deleteUser(int[] userId) {
		MySqlConnPoolUtil.startTransaction();
		try {
			int result=new BaseDaoImpl<LcUserDBEntity>().delete(LcUserDBEntity.class, userId);
			if (result ==PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
				return PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return PlatformResponseResult.failure;
			}
		}catch (Exception e) {
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcFuncDBEntity> queryFuncList(String funcName,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcFuncDaoImpl().queryFuncList(funcName,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public PlatformResponseResult saveOrUpdateFunc(LcFuncDBEntity lcFuncDBEntity) {
		int result;
		MySqlConnPoolUtil.startTransaction();
		try {
			if(lcFuncDBEntity.getFunc_id()!=null  && lcFuncDBEntity.getFunc_id()!=0 ){
				result= new BaseDaoImpl<LcFuncDBEntity>().update(lcFuncDBEntity);
			}else {
				result=new BaseDaoImpl<LcFuncDBEntity>().add(lcFuncDBEntity);
			}
			if (result ==PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
				return PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return PlatformResponseResult.failure;
			}
		} catch (Exception e) {
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

	@Override
	public PlatformResponseResult deleteFunc(int[] funcId) {
		MySqlConnPoolUtil.startTransaction();
		for (int i = 0; i < funcId.length; i++) {
			new LcRoleFuncDaoImpl().deleteRoleFuncByFuncId(funcId[i]);
		}
		try {
			int result=new BaseDaoImpl<LcFuncDBEntity>().delete(LcFuncDBEntity.class, funcId);
			if (result ==PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
				return PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return PlatformResponseResult.failure;
			}
		}catch (Exception e) {
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcRoleDBEntity> queryRoleList(String role_name,int currentPage,int pageSize) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcRoleDaoImpl().queryRoleList(role_name,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public PlatformResponseResult saveOrUpdateRole(LcRoleDBEntity lcRoleDBEntity) {
		int result;
		MySqlConnPoolUtil.startTransaction();
		try {
			if( lcRoleDBEntity.getRole_id()!=null && lcRoleDBEntity.getRole_id()!=0 ){
				result= new BaseDaoImpl<LcRoleDBEntity>().update(lcRoleDBEntity);
			}else {
				result=new BaseDaoImpl<LcRoleDBEntity>().add(lcRoleDBEntity);
			}
			if (result ==PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
				return PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return PlatformResponseResult.failure;
			}
		} catch (Exception e) {
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public PlatformResponseResult deleteRole(int[] roleId) {
		MySqlConnPoolUtil.startTransaction();
		int result=0;
		for (int i = 0; i < roleId.length; i++) {
			result=new LcRoleFuncDaoImpl().deleteRoleFuncByRoleId(roleId[i]);
		}
		try {
			result=new BaseDaoImpl<LcRoleDBEntity>().delete(LcRoleDBEntity.class, roleId);
			if (result ==PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
				return PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return PlatformResponseResult.failure;
			}
		}catch (Exception e) {
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public List<LcFuncDBEntity> queryRoleFuncList(int roleId) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcFuncDaoImpl().queryRoleFuncList(roleId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public PlatformResponseResult bindRoleAndFun(int roleId, int[] funcId) {
		MySqlConnPoolUtil.startTransaction();
		int result=0;
		result=new LcRoleFuncDaoImpl().deleteRoleFuncByRoleId(roleId);
		try {
				for (int i = 0; i < funcId.length; i++) {
					LcRoleFuncDBEntity lcRoleFuncDBEntity=new LcRoleFuncDBEntity();
					lcRoleFuncDBEntity.setFunc_id(funcId[i]);
					lcRoleFuncDBEntity.setRole_id(roleId);
					result=new BaseDaoImpl<LcRoleFuncDBEntity>().add(lcRoleFuncDBEntity);
					if(result<=0){
						break;
					}
				}
			if (result ==PlatformResponseResult.success_VALUE) {
				MySqlConnPoolUtil.commit();
				return PlatformResponseResult.success;
			} else {
				MySqlConnPoolUtil.rollback();
				return PlatformResponseResult.failure;
			}
		}catch (Exception e) {
			// 回滚事务
			MySqlConnPoolUtil.rollback();
			return PlatformResponseResult.failure;
		} finally {
			MySqlConnPoolUtil.close();
		}	
	}
	@Override
	public LcUserDBEntity querUserById(int userId) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcUserDaoImpl().findById(userId, LcUserDBEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public LcRoleDBEntity queryRoleById(int roleId) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcRoleDaoImpl().findById(roleId, LcRoleDBEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
		
	}
	@Override
	public List<LcFuncDBEntity> selectFuncByParent_func(int parent_func) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcFuncDaoImpl().selectFuncByParent_func(parent_func);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public LcFuncDBEntity selectFuncById(int func_id) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcFuncDaoImpl().findById(func_id, LcFuncDBEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}
	@Override
	public PlatformResponseResult updateUserPasswod(String userId,
			String password) {
		try {
			MySqlConnPoolUtil.startTransaction();
			return new LcUserDaoImpl().updateUserPasswod(userId,password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			MySqlConnPoolUtil.close();
		}
	}

}

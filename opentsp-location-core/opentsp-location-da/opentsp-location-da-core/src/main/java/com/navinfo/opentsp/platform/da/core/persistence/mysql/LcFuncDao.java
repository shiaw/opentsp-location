package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcFuncDBEntity;

import java.util.List;



public interface LcFuncDao extends BaseDao<LcFuncDBEntity>{
 /**
  * 根据功能编码查找
  * @param func_code
  * @return
  */
 public LcFuncDBEntity getFuncInfo(String func_code);
 /**
  * 根据功能描述查找
  * @param func_name
  * @return
  */
 public List<LcFuncDBEntity> getFuncInfoByName(String func_name);
 /**
  * 根据“功能名称”查询系统功能信息(参数为空时加载所有系统功能)
  * @param funcName
  * @return
  */
 public List<LcFuncDBEntity> queryFuncList(String funcName,int currentPage,int pageSize);
 /**
  * 根据角色查询功能列表
  * @param role_name
  * @return
  */
 public List<LcFuncDBEntity> queryRoleFuncList(int roleId);
 /**
  * 根据父级ID查询功能列表
  * @param parent_func
  * @return
  */
 public List<LcFuncDBEntity> selectFuncByParent_func(int parent_func);
 /**
  * 查询系统功能 数量
  * @param funcName
  * @return
  */
 public int queryFuncCount(String funcName);


}
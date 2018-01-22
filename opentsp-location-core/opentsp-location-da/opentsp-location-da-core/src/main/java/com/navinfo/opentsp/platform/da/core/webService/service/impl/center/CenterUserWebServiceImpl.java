package com.navinfo.opentsp.platform.da.core.webService.service.impl.center;

import com.navinfo.opentsp.platform.da.core.persistence.AccountManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.AccountManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcFuncDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcRoleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcUserDBEntity;
import com.navinfo.opentsp.platform.da.core.webService.service.UserWebService;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;

import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "com.navinfo.opentsp.platform.da.core.webService.service.UserWebService")
public class CenterUserWebServiceImpl implements UserWebService {
    private AccountManage accountManage = new AccountManageImpl();
    @Override
    public int queryUserCount(String userName,String passweord, int roleId){
        int userCount = accountManage.queryUserCount(userName, passweord,roleId);
        return userCount;
    }
    @Override
    public int queryFuncCount(String funcName){
        int funcCount = accountManage.queryFuncCount(funcName);
        return funcCount;
    }
    @Override
    public int queryRoleCount(String role_name,int flag){
        int roleCount = accountManage.queryRoleCount(role_name,flag);
        return roleCount;
    }
    @Override
    public List<LcUserDBEntity> querUserList(String userName,String passweord, int roleId,int currentPage,int pageSize) {
        List<LcUserDBEntity> list = accountManage
                .querUserList(userName, passweord, roleId, currentPage, pageSize);
        return list;
    }

    @Override
    public PlatformResponseResult saveOrUpdateUser(LcUserDBEntity lcUserDBEntity) {
        PlatformResponseResult result = accountManage
                .saveOrUpdateUser(lcUserDBEntity);
        return result;
    }

    @Override
    public PlatformResponseResult deleteUser(int[] userId) {
        PlatformResponseResult result = accountManage.deleteUser(userId);
        return result;
    }

    @Override
    public List<LcFuncDBEntity> queryFuncList(String funcName,int currentPage,int pageSize) {
        List<LcFuncDBEntity> list = accountManage.queryFuncList(funcName,currentPage,pageSize);
        return list;
    }

    @Override
    public PlatformResponseResult saveOrUpdateFunc(LcFuncDBEntity lcFuncDBEntity) {
        PlatformResponseResult result = accountManage
                .saveOrUpdateFunc(lcFuncDBEntity);
        return result;
    }

    @Override
    public PlatformResponseResult deleteFunc(int[] funcId) {
        PlatformResponseResult result = accountManage.deleteFunc(funcId);
        return result;
    }

    @Override
    public List<LcRoleDBEntity> queryRoleList(String role_name,int currentPage,int pageSize) {
        List<LcRoleDBEntity> list = accountManage.queryRoleList(role_name,currentPage,pageSize);
        return list;
    }

    @Override
    public PlatformResponseResult saveOrUpdateRole(LcRoleDBEntity lcRoleDBEntity) {
        PlatformResponseResult result = accountManage
                .saveOrUpdateRole(lcRoleDBEntity);
        return result;
    }

    @Override
    public PlatformResponseResult deleteRole(int[] roleId) {
        PlatformResponseResult result = accountManage.deleteRole(roleId);
        return result;
    }

    @Override
    public List<LcFuncDBEntity> queryRoleFuncList(int roleId) {
        List<LcFuncDBEntity> list = accountManage.queryRoleFuncList(roleId);
        return list;
    }

    @Override
    public PlatformResponseResult bindRoleAndFun(int roleId, int[] funcId) {
        PlatformResponseResult result = accountManage.bindRoleAndFun(roleId,funcId);
        return result;
    }

    @Override
    public LcUserDBEntity querUserById(int userId) {
        LcUserDBEntity entity = accountManage.querUserById(userId);
        return entity;
    }

    @Override
    public LcRoleDBEntity queryRoleById(int roleId) {
        LcRoleDBEntity entity = accountManage.queryRoleById(roleId);
        return entity;

    }

    @Override
    public List<LcFuncDBEntity> selectFuncByParent_func(int parent_func) {
        List<LcFuncDBEntity> list = accountManage.selectFuncByParent_func(parent_func);
        return list;
    }

    @Override
    public LcFuncDBEntity selectFuncById(int func_id) {
        LcFuncDBEntity entity = accountManage.selectFuncById(func_id);
        return entity;
    }
    public static void main(String[] args) {
        CenterUserWebServiceImpl service=new CenterUserWebServiceImpl();
        List<LcUserDBEntity> querUserList = service.querUserList("", null, 0,1,10);
        System.out.println(querUserList.size());

    }

    @Override
    public PlatformResponseResult updateUserPasswod(String userId, String password) {
        PlatformResponseResult result = accountManage
                .updateUserPasswod(userId, password);
        return result;
    }
}

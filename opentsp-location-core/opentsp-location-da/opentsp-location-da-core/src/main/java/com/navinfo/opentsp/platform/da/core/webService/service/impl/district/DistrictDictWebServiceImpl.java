package com.navinfo.opentsp.platform.da.core.webService.service.impl.district;

import com.navinfo.opentsp.platform.da.core.persistence.DictManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.DictManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictTypeDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDistrictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalProtoMappingDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcTerminalProtoMappingDaoImpl;
import com.navinfo.opentsp.platform.da.core.webService.service.DictWebService;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;

import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "com.navinfo.opentsp.platform.da.core.webService.service.DictWebService")
public class DistrictDictWebServiceImpl implements DictWebService {
    private DictManage dictManage = new DictManageImpl();

    @Override
    public List<LcDictDBEntity> queryDictList(String dtName, int dictId) {
        List<LcDictDBEntity> list = dictManage.queryDictList(dtName, dictId);
        return list;
    }

    @Override
    public List<LcDictTypeDBEntity> queryDictTypeInfo(String dtName, int dictId) {
        List<LcDictTypeDBEntity> list = dictManage.queryDictTypeList(dtName, dictId);
        return list;
    }

    @Override
    public PlatformResponseResult saveOrUpdateDict(LcDictDBEntity lcDictDBEntity) {
        PlatformResponseResult responseResult = dictManage
                .saveOrUpdateDict(lcDictDBEntity);
        return responseResult;
    }

    @Override
    public PlatformResponseResult deleteDict(int[] dictCode) {
        // �ֵ�ɾ����ʱ�Ȳ���
        PlatformResponseResult result = dictManage.deleteDict(dictCode);
        return result;
    }

    @Override
    public PlatformResponseResult saveOrUpdateDictType(
            LcDictTypeDBEntity lcDictTypeDBEntity) {
        PlatformResponseResult responseResult = dictManage
                .saveOrUpdateDictType(lcDictTypeDBEntity);
        return responseResult;

    }

    @Override
    public PlatformResponseResult deleteDictType(int[] dtId) {
        // �ֵ�ɾ����ʱ�Ȳ���
        PlatformResponseResult result = dictManage.deleteDictType(dtId);
        return result;
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
    public PlatformResponseResult bindProtoMapping(int logicCode, int[] dictCode) {
        PlatformResponseResult responseResult = dictManage.bindProtoMapping(
                logicCode, dictCode);
        return responseResult;
    }

    @Override
    public List<LcDistrictDBEntity> queryDistrictList(String dictCodeName) {
        List<LcDistrictDBEntity> list = dictManage
                .queryDistrictList(dictCodeName);
        return list;
    }

    @Override
    public PlatformResponseResult bindDistrict(int parent_code, int[] dict_code) {
        // ��������������Ϣ����
        PlatformResponseResult responseResult = dictManage.bindDistrict(
                parent_code, dict_code);
        return responseResult;
    }

    @Override
    public PlatformResponseResult deleteDistrict(int[] districtId) {
        // ɾ����ʱ�Ȳ���
        PlatformResponseResult result = dictManage.deleteDistrict(districtId);
        return result;
    }

    @Override
    public List<LcDictDBEntity> getDictByType(int dictType, int currentPage, int pageSize) {
        List<LcDictDBEntity> list = dictManage.getDictByType(dictType, currentPage, pageSize);
        return list;
    }

    @Override
    public int getDictDataResCount(int dictType) {
        int count = dictManage.getDictDataResCount(dictType);
        return count;
    }

    @Override
    public List<LcDistrictDBEntity> queryDistrictByParent_code(int parent_code, int currentPage, int pageSize) {
        List<LcDistrictDBEntity> list = dictManage
                .queryDistrictByParent_code(parent_code, currentPage, pageSize);
        return list;
    }

    @Override
    public int queryDistrictCount(int parent_code) {
        int count = dictManage.queryDistrictCount(parent_code);
        return count;
    }

    @Override
    public PlatformResponseResult deleteProtoMapping(int[] tplId) {
        // ɾ����ʱ�Ȳ���
        PlatformResponseResult result = dictManage.deleteProtoMapping(tplId);
        return result;
    }

    @Override
    public List<LcTerminalProtoMappingDBEntity> getByGroupLogicCode(
            int logic_code) {
        List<LcTerminalProtoMappingDBEntity> list = dictManage
                .getByGroupLogicCode(logic_code);
        return list;
    }

    @Override
    public PlatformResponseResult deleteProtoMappingByDictcode(int[] logic_code) {
        // ɾ����ʱ�Ȳ���
        PlatformResponseResult result = dictManage
                .deleteProtoMappingByDictcode(logic_code);
        return result;

    }

    @Override
    public List<LcTerminalProtoMappingDBEntity> getTerminalProtoMapping(
            int logicCode, int dictCode, int messageCode, int currentPage, int pageSize) {
        List<LcTerminalProtoMappingDBEntity> terminalProtoMapping = dictManage.getTerminalProtoMapping(logicCode, dictCode, messageCode, currentPage, pageSize);
        return terminalProtoMapping;
    }

    @Override
    public int selectPMListCount(
            int logicCode, int dictCode, int messageCode) {
        int count = dictManage.selectPMListCount(logicCode, dictCode, messageCode);
        return count;
    }

    @Override
    public PlatformResponseResult saveOrUpdateTerminalProtoMapping(
            LcTerminalProtoMappingDBEntity lcTerminalProtoMapping) {
        PlatformResponseResult result = dictManage.saveOrUpdateTerminalProtoMapping(lcTerminalProtoMapping);
        return result;
    }

    @Override
    public PlatformResponseResult deleteTerminalProtoMapping(int[] tplId,
                                                             int messageCode) {
        PlatformResponseResult result = dictManage.deleteTerminalProtoMapping(tplId, messageCode);
        return result;
    }

    @Override
    public PlatformResponseResult updateProtocoMapping(
            List<LcTerminalProtoMappingDBEntity> lcTerminalProtoMappingDBEntity) {
        PlatformResponseResult result = dictManage.updateProtocoMapping(lcTerminalProtoMappingDBEntity);
        return result;

    }

    @Override
    public List<LcDictTypeDBEntity> queryDictTypeList(String dtName, int currentPage, int pageSize) {
        List<LcDictTypeDBEntity> list = dictManage.queryDictTypeList(dtName, currentPage, pageSize);
        return list;
    }

    @Override
    public int queryDictTypeListCount(String dtName) {
        int count = dictManage.queryDictTypeListCount(dtName);
        return count;
    }
}

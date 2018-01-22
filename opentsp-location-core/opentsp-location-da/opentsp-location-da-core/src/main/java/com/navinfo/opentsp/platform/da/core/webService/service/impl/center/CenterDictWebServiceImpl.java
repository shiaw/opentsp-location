package com.navinfo.opentsp.platform.da.core.webService.service.impl.center;

import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.DictManage;
import com.navinfo.opentsp.platform.da.core.persistence.SynchronousManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.DictManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.SynchronousManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.*;
import com.navinfo.opentsp.platform.da.core.webService.manage.SynchronousDataCache;
import com.navinfo.opentsp.platform.da.core.webService.manage.WebServiceTools;
import com.navinfo.opentsp.platform.da.core.webService.service.DictWebService;
import com.navinfo.opentsp.platform.location.kit.lang.ObjectUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "com.navinfo.opentsp.platform.da.core.webService.service.DictWebService")
public class CenterDictWebServiceImpl implements DictWebService {
    private static final Logger log = LoggerFactory
            .getLogger(CenterConfigWebServiceImpl.class);
    private DictManage dictManage = new DictManageImpl();
    private SynchronousManage synchronousManage = new SynchronousManageImpl();

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
        // 保存基本信息
        PlatformResponseResult responseResult = dictManage
                .saveOrUpdateDict(lcDictDBEntity);
        if (responseResult == PlatformResponseResult.success) {
            // 从数据库获得所有分区
            List<LcDictDBEntity> dictDatas = dictManage.getDictByType(13,0,0);
            for (LcDictDBEntity data : dictDatas) {
                LcSynchronizationLogDBEntity entity = new LcSynchronizationLogDBEntity();
                // 调用分区

                //暂时只有北京分区。增加判断。
                if(data.getDict_code().intValue() == 130100){
                    try {
                        DictWebService dictWebService = (DictWebService) WebServiceTools
                                .getOperationClass(data.getDict_code() + "",
                                        DictWebService.class);
                        if(dictWebService!=null){
                            PlatformResponseResult wsResult = dictWebService
                                    .saveOrUpdateDict(lcDictDBEntity);
                            if (wsResult == PlatformResponseResult.success) {
                                entity.setStatus(PlatformResponseResult.success_VALUE);
                            } else {
                                entity.setStatus(PlatformResponseResult.failure_VALUE);
                            }
                        }else {
                            entity.setStatus(PlatformResponseResult.failure_VALUE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        entity.setStatus(PlatformResponseResult.failure_VALUE);
                    }
                    // 组装同步数据
                    entity.setId(SynchronousDataCache.updateMaxSynchronizationLogId());
                    entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.save);
                    entity.setDistrict(data.getDict_code() + "");
                    entity.setModuleName(Constant.PropertiesKey.SynchronousDataParameters.DictWebService);
                    entity.setOperationDate(System.currentTimeMillis() / 1000);
                    entity.setOperationObject(ObjectUtils
                            .objectToBytes(lcDictDBEntity));
                    entity.setType(Constant.PropertiesKey.DBTableName.LC_DICT);
                    entity.setWebserviceClass(DictWebService.class.getName());
                    // 放缓存
                    if (entity.getStatus() == PlatformResponseResult.failure_VALUE) {
                        SynchronousDataCache.add(data.getDict_code(), entity);
                    }
                    // 保存同步数据日志
                    PlatformResponseResult result = synchronousManage
                            .saveSynchronousData(entity);
                    if (result == PlatformResponseResult.failure) {
                        log.error("同步数据日志表存储失败");
                    }
                }
            }
        }

        if (responseResult == PlatformResponseResult.success) {
            return responseResult;
        } else {
            return PlatformResponseResult.failure;
        }


    }

    @Override
    public PlatformResponseResult deleteDict(int[] dictCode) {
        // 字典删除时的同步暂时先不做***
        PlatformResponseResult result = dictManage.deleteDict(dictCode);
        if (result == PlatformResponseResult.success) {
            List<LcDictDBEntity> dictDatas = dictManage.getDictByType(13,0,0);
            for (LcDictDBEntity data : dictDatas) {

                LcSynchronizationLogDBEntity entity = new LcSynchronizationLogDBEntity();
                // 调用ws
                try {
                    DictWebService dictWebService = (DictWebService) WebServiceTools
                            .getOperationClass(data.getDict_code() + "",
                                    DictWebService.class);
                    if(dictWebService!=null){
                        PlatformResponseResult wsResult = dictWebService
                                .deleteDict(dictCode);
                        if (wsResult == PlatformResponseResult.success) {
                            entity.setStatus(PlatformResponseResult.success_VALUE);
                        } else {
                            entity.setStatus(PlatformResponseResult.failure_VALUE);
                        }
                    }else {
                        entity.setStatus(PlatformResponseResult.failure_VALUE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    entity.setStatus(PlatformResponseResult.failure_VALUE);
                }


            }

        }
        if (result == PlatformResponseResult.success) {
            return result;
        } else {
            return PlatformResponseResult.failure;
        }

    }

    @Override
    public PlatformResponseResult saveOrUpdateDictType(
            LcDictTypeDBEntity lcDictTypeDBEntity) {
        // 保存基本信息
        PlatformResponseResult responseResult = dictManage
                .saveOrUpdateDictType(lcDictTypeDBEntity);
        if (responseResult == PlatformResponseResult.success) {
            List<LcDictDBEntity> dictDatas = dictManage.getDictByType(13,0,0);
            for (LcDictDBEntity data : dictDatas) {
                //设备有限 ，暂时做北京分区的处理，其它分区不做处理。
                if(data.getDict_code() == 130100){
                    LcSynchronizationLogDBEntity entity = new LcSynchronizationLogDBEntity();
                    // 调用ws
                    try {
                        DictWebService dictWebService = (DictWebService) WebServiceTools
                                .getOperationClass(data.getDict_code() + "",
                                        DictWebService.class);

                        PlatformResponseResult wsResult = dictWebService
                                .saveOrUpdateDictType(lcDictTypeDBEntity);
                        if (wsResult == PlatformResponseResult.success) {
                            entity.setStatus(PlatformResponseResult.success_VALUE);
                        } else {
                            entity.setStatus(PlatformResponseResult.failure_VALUE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        entity.setStatus(PlatformResponseResult.failure_VALUE);
                    }
                    // 组装同步数据
                    entity.setId(SynchronousDataCache.updateMaxSynchronizationLogId());
                    entity.setDistrict(data.getDict_code() + "");
                    entity.setModuleName(Constant.PropertiesKey.SynchronousDataParameters.DictWebService);
                    entity.setOperationDate(System.currentTimeMillis() / 1000);
                    entity.setOperationObject(ObjectUtils
                            .objectToBytes(lcDictTypeDBEntity));
                    entity.setType(Constant.PropertiesKey.DBTableName.LC_DICT_TYPE);
                    entity.setWebserviceClass(DictWebService.class.getName());
                    entity.setOperationType(Constant.PropertiesKey.OperationTypeParameters.save);
                    // 放缓存
                    if (entity.getStatus() == PlatformResponseResult.failure_VALUE) {
                        SynchronousDataCache.add(data.getDict_code(), entity);
                    }
                    // 放数据库
                    PlatformResponseResult result = synchronousManage
                            .saveSynchronousData(entity);
                    if (result == PlatformResponseResult.failure) {
                        log.error("同步数据日志表存储失败");
                    }
                }
            }

        }
        if (responseResult == PlatformResponseResult.success) {
            return responseResult;
        } else {
            return PlatformResponseResult.failure;
        }
    }

    @Override
    public PlatformResponseResult deleteDictType(int[] dtId) {
        // 字典删除暂时先不做
        PlatformResponseResult result = dictManage.deleteDictType(dtId);
        if (result == PlatformResponseResult.success) {
            List<LcDictDBEntity> dictDatas = dictManage.getDictByType(13,0,0);
            for (LcDictDBEntity data : dictDatas) {

                LcSynchronizationLogDBEntity entity = new LcSynchronizationLogDBEntity();
                // 调用ws
                try {
                    DictWebService dictWebService = (DictWebService) WebServiceTools
                            .getOperationClass(data.getDict_code() + "",
                                    DictWebService.class);
                    PlatformResponseResult wsResult = dictWebService
                            .deleteDictType(dtId);
                    if (wsResult == PlatformResponseResult.success) {
                        entity.setStatus(PlatformResponseResult.success_VALUE);
                    } else {
                        entity.setStatus(PlatformResponseResult.failure_VALUE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    entity.setStatus(PlatformResponseResult.failure_VALUE);
                }


            }

        }
        if (result == PlatformResponseResult.success) {
            return result;
        } else {
            return PlatformResponseResult.failure;
        }
    }

    @Override
    public List<LcTerminalProtoMappingDBEntity> getByLogicCode(int logic_code) {
        List<LcTerminalProtoMappingDBEntity> list = dictManage
                .getByLogicCode(logic_code);
        return list;
    }

    @Override
    public PlatformResponseResult bindProtoMapping(int logicCode, int[] dictCode) {
        // 基本数据绑定
        PlatformResponseResult responseResult = dictManage.bindProtoMapping(
                logicCode, dictCode);
        if (responseResult == PlatformResponseResult.success) {
            List<LcDictDBEntity> dictDatas = dictManage.getDictByType(13,0,0);
            for (LcDictDBEntity dictData : dictDatas) {
                if(dictData.getDict_code().intValue() == 130100){
                    LcSynchronizationLogDBEntity data = new LcSynchronizationLogDBEntity();
                    LcTerminalProtoMappingDBEntity entity = new LcTerminalProtoMappingDBEntity();
                    try {
                        DictWebService dictWebService = (DictWebService) WebServiceTools
                                .getOperationClass(dictData.getDict_code() + "",
                                        DictWebService.class);
                        PlatformResponseResult wsResult = dictWebService
                                .bindProtoMapping(logicCode, dictCode);
                        if (wsResult == PlatformResponseResult.success) {
                            data.setStatus(PlatformResponseResult.success_VALUE);
                        } else {
                            data.setStatus(PlatformResponseResult.failure_VALUE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        data.setStatus(PlatformResponseResult.failure_VALUE);
                    }
                    entity.setLogic_code(logicCode);
                    entity.setDict_code(null);
                    entity.setTpl_d(null);
                    data.setId(SynchronousDataCache.updateMaxSynchronizationLogId());
                    data.setModuleName(Constant.PropertiesKey.SynchronousDataParameters.DictWebService);
                    data.setDistrict(dictData.getDict_code() + "");
                    data.setOperationDate(System.currentTimeMillis() / 1000);
                    data.setOperationObject(ObjectUtils.objectToBytes(entity));
                    data.setType(Constant.PropertiesKey.DBTableName.LC_TERMINAL_PROTO_MAPPING);
                    data.setOperationType(Constant.PropertiesKey.OperationTypeParameters.save);
                    String primaryKeys = "";
                    int count = 0;
                    for (int k : dictCode) {
                        if (count == 0) {
                            primaryKeys = "" + k;
                        } else {
                            primaryKeys += "," + k;
                        }
                        count++;
                    }
                    data.setPrimaryKeys(primaryKeys);
                    data.setWebserviceClass(DictWebService.class.getName());
                    // 放缓存
                    if (data.getStatus() == PlatformResponseResult.failure_VALUE) {
                        SynchronousDataCache.add(dictData.getDict_code(), data);
                    }
                    // 放数据库
                    PlatformResponseResult result = synchronousManage
                            .saveSynchronousData(data);
                    if (result == PlatformResponseResult.failure) {
                        log.error("同步数据日志表存储失败");
                    }
                }
            }
        }
        if (responseResult == PlatformResponseResult.success) {
            return responseResult;
        } else {
            return PlatformResponseResult.failure;
        }

    }

    @Override
    public List<LcDistrictDBEntity> queryDistrictList(String dictCodeName) {
        List<LcDistrictDBEntity> list = dictManage
                .queryDistrictList(dictCodeName);
        return list;
    }

    @Override
    public PlatformResponseResult bindDistrict(int parent_code, int[] dict_code) {
        // 行政区划基本信息保存
        PlatformResponseResult responseResult = dictManage.bindDistrict(
                parent_code, dict_code);
        if (responseResult == PlatformResponseResult.success) {
            List<LcDictDBEntity> dictDatas = dictManage.getDictByType(13,0,0);
            for (LcDictDBEntity dictData : dictDatas) {
                if(dictData.getDict_code() == 130100){
                    LcSynchronizationLogDBEntity data = new LcSynchronizationLogDBEntity();
                    LcDistrictDBEntity entity = new LcDistrictDBEntity();
                    // 调用ws
                    try {
                        DictWebService dictWebService = (DictWebService) WebServiceTools
                                .getOperationClass(dictData.getDict_code() + "",
                                        DictWebService.class);
                        PlatformResponseResult wsResult = dictWebService
                                .bindDistrict(parent_code, dict_code);
                        if (wsResult == PlatformResponseResult.success) {
                            data.setStatus(PlatformResponseResult.success_VALUE);
                        } else {
                            data.setStatus(PlatformResponseResult.failure_VALUE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        data.setStatus(PlatformResponseResult.failure_VALUE);
                    }
                    // 组装同步数据
                    entity.setDistrict_id(null);
                    entity.setDict_code(null);
                    entity.setParent_code(parent_code);
                    data.setId(SynchronousDataCache.updateMaxSynchronizationLogId());
                    data.setDistrict(dictData.getDict_code() + "");
                    data.setModuleName(Constant.PropertiesKey.SynchronousDataParameters.DictWebService);
                    data.setOperationDate(System.currentTimeMillis() / 1000);
                    data.setOperationObject(ObjectUtils.objectToBytes(entity));
                    data.setType(Constant.PropertiesKey.DBTableName.LC_DISTRICT);
                    data.setOperationType(Constant.PropertiesKey.OperationTypeParameters.save);
                    String primaryKeys = "";
                    int count = 0;
                    for (int k : dict_code) {
                        if (count == 0) {
                            primaryKeys = "" + k;
                        } else {
                            primaryKeys += "," + k;
                        }
                        count++;
                    }
                    data.setPrimaryKeys(primaryKeys);
                    data.setWebserviceClass(DictWebService.class.getName());
                    // 放缓存
                    if (data.getStatus() == 0) {
                        SynchronousDataCache.add(dictData.getDict_code(), data);
                    }
                    // 放数据库
                    PlatformResponseResult result = synchronousManage
                            .saveSynchronousData(data);
                    if (result == PlatformResponseResult.failure) {
                        log.error("同步数据日志表存储失败");
                    }
                }
            }
        }

        if (responseResult == PlatformResponseResult.success) {
            return responseResult;
        } else {
            return PlatformResponseResult.failure;
        }
    }

    @Override
    public PlatformResponseResult deleteDistrict(int[] districtId) {
        // 删除暂时先不做
        PlatformResponseResult result = dictManage.deleteDistrict(districtId);
        return result;
    }

    @Override
    public List<LcDictDBEntity> getDictByType(int dictType,int currentPage,int pageSize) {
        List<LcDictDBEntity> list = dictManage.getDictByType(dictType,currentPage,pageSize);
        return list;
    }

    @Override
    public List<LcDistrictDBEntity> queryDistrictByParent_code(int parent_code,int currentPage,int pageSize) {
        List<LcDistrictDBEntity> list = dictManage
                .queryDistrictByParent_code(parent_code, currentPage, pageSize);
        return list;
    }
    @Override
    public int queryDistrictCount(int parent_code){
        int count = dictManage.queryDistrictCount(parent_code);
        return count;
    }

    @Override
    public PlatformResponseResult deleteProtoMapping(int[] tplId) {
        // 删除暂时先不做
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
        // 删除暂时先不做
        PlatformResponseResult result = dictManage
                .deleteProtoMappingByDictcode(logic_code);
        return result;
    }

    @Override
    public List<LcTerminalProtoMappingDBEntity> getTerminalProtoMapping(
            int logicCode, int dictCode, int messageCode,int currentPage,int pageSize) {
        List<LcTerminalProtoMappingDBEntity> terminalProtoMapping = dictManage.getTerminalProtoMapping(logicCode, dictCode, messageCode,currentPage,pageSize);
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
        PlatformResponseResult result=dictManage.saveOrUpdateTerminalProtoMapping(lcTerminalProtoMapping);
        return result;
    }

    @Override
    public PlatformResponseResult deleteTerminalProtoMapping(int[] tplId,
                                                             int messageCode) {
        PlatformResponseResult result=dictManage.deleteTerminalProtoMapping(tplId, messageCode);
        return result;
    }

    @Override
    public PlatformResponseResult updateProtocoMapping(
            List<LcTerminalProtoMappingDBEntity> lcTerminalProtoMappingDBEntity) {
        PlatformResponseResult result=dictManage.updateProtocoMapping(lcTerminalProtoMappingDBEntity);
        return result;

    }

    @Override
    public int getDictDataResCount(int dictType) {
        int count = dictManage.getDictDataResCount(dictType);
        return count;
    }

    @Override
    public List<LcDictTypeDBEntity> queryDictTypeList(String dtName,int currentPage,int pageSize) {
        List<LcDictTypeDBEntity> list = dictManage.queryDictTypeList(dtName,currentPage,pageSize);
        return list;
    }
    @Override
    public int queryDictTypeListCount(String dtName){
        int count = dictManage.queryDictTypeListCount(dtName);
        return count;
    }
}

package com.navinfo.opentsp.platform.da.core.webService.manage;

import java.util.List;

import com.navinfo.opentsp.platform.location.kit.lang.ObjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.SynchronousManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.SynchronousManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictTypeDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDistrictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcSynchronizationLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalInfoDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalProtoMappingDBEntity;
import com.navinfo.opentsp.platform.da.core.webService.service.ConfigWebService;
import com.navinfo.opentsp.platform.da.core.webService.service.DictWebService;
import com.navinfo.opentsp.platform.da.core.webService.service.TerminalWebService;

/**
 * 同步线程，根据分区属性同步数据
 *
 * @author ss
 */
public class SynchronousThreed implements Runnable {
    private static final Logger log = LoggerFactory
            .getLogger(SynchronousThreed.class);
    /**
     * 分区编码
     */
    private int district;
    /**
     * 轮询等待时间
     */
    private int wait_time = 5000;

    public SynchronousThreed() {
    }

    public SynchronousThreed(int district) {
        this.district = district;
    }

    /**
     * 分区数据同步
     */
    @Override
    public void run() {
        try {
            while (true) {
                // 扫描缓存数据,同步
                List<LcSynchronizationLogDBEntity> synchronizationLog = SynchronousDataCache
                        .get(district);
                if (CollectionUtils.isNotEmpty(synchronizationLog)) {
                    synchronousData(synchronizationLog);
                }
                // 扫描一圈后，睡一会儿（设置睡得时间）。
                try {
                    Thread.sleep(wait_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 分区同步数据
     *
     * @param synchronizationLog
     */
    private void synchronousData(
            List<LcSynchronizationLogDBEntity> synchronizationLog) {
        try {
            SynchronousManage sydService = new SynchronousManageImpl();
            for (LcSynchronizationLogDBEntity dataModel : synchronizationLog) {
                int status = dataModel.getStatus();
                //2-处理中
                if (Constant.PropertiesKey.OperationTypeParameters.operating == status) {
                    continue;
                } else {
                    //设置当前记录为处理中
                    dataModel.setStatus(Constant.PropertiesKey.OperationTypeParameters.operating);
                }
                // 得到分区
                String district = dataModel.getDistrict();
                // 得到调用模块名称
                String module = dataModel.getModuleName();
                // 得到操作类型
                String opretionType = dataModel.getOperationType();
                /* 自身类型 */
                String type = dataModel.getType();
                // 调用webserviceTool
                switch (module) {
                    case Constant.PropertiesKey.SynchronousDataParameters.TerminalWebService:
                        // 终端信息同步type=LC_TERMINAL_INFO
                        terminalSynchronization(dataModel, district, opretionType,
                                type, sydService);
                        break;
                    case Constant.PropertiesKey.SynchronousDataParameters.DictWebService:
                        // 字典明细同步type=LC_DICT 没有做删除
                        dictSynchronization(dataModel, opretionType, type,
                                sydService);
                        // 字典类型同步type=LC_DICT_TYPE 没有做删除
                        dictTypeSynchronization(dataModel, opretionType, type,
                                sydService);
                        // 终端指令映射同步type=LC_TERMINAL_PROTO_MAPPING 没有做删除
                        protoMappinSynchronization(dataModel, opretionType, type,
                                sydService);
                        // 行政区划同步type=LC_DISTRICT 没有做删除
                        districtSynchronization(dataModel, opretionType, type,
                                sydService);
                        break;
                    case Constant.PropertiesKey.SynchronousDataParameters.ConfigWebService:
                        // 服务配置同步type=LC_SERVICE_CONFIG
                        serviceConfigSynchronization(dataModel, district,
                                opretionType, type, sydService);
                        // 节点配置同步type=LC_NODE_CONFIG
                        nodeConfigSynchronization(dataModel, district,
                                opretionType, type, sydService);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 行政区划令映射同步
     *
     * @param dataModel
     * @param opretionType
     * @param type
     * @param sydService
     */
    private void districtSynchronization(
            LcSynchronizationLogDBEntity dataModel, String opretionType,
            String type, SynchronousManage sydService) {
        PlatformResponseResult result = null;
        try {
            if (type.equals(Constant.PropertiesKey.DBTableName.LC_DISTRICT)) {
                DictWebService dictWebService = (DictWebService) WebServiceTools
                        .getOperationClass(dataModel.getDistrict(),
                                DictWebService.class);
                LcDistrictDBEntity district = (LcDistrictDBEntity) ObjectUtils
                        .bytesToObject(dataModel.getOperationObject());
                if (opretionType.equals(Constant.PropertiesKey.OperationTypeParameters.delete)) {
                    // 删除功能先不做
                    // configWebService.deleteNodeConfigByNodeCode(nodeConfig.getNode_code());
                } else {
                    result = dictWebService.bindDistrict(district.getParent_code(),
                            stringToArray(dataModel));
                }
                // 根据返回值进行判断，如果返回失败，则不做处理，成功则处理
                if (result == PlatformResponseResult.success) {
                    dataModel.setStatus(PlatformResponseResult.success_VALUE);
                    //删除缓存数据
                    SynchronousDataCache.remove(dataModel.getDistrict(), dataModel);

                } else {
                    dataModel.setStatus(PlatformResponseResult.failure_VALUE);
                }
                sydService.updateSynchronousData(dataModel);
            }
        } catch (Exception e) {
            log.info("行政区划同比失败");
            e.printStackTrace();
        }
    }

    /**
     * 终端指令映射同步
     *
     * @param dataModel
     * @param opretionType
     * @param type
     * @param sydService
     */
    private void protoMappinSynchronization(
            LcSynchronizationLogDBEntity dataModel, String opretionType,
            String type, SynchronousManage sydService) {
        PlatformResponseResult result = null;
        try {
            if (type.equals(Constant.PropertiesKey.DBTableName.LC_TERMINAL_PROTO_MAPPING)) {
                DictWebService dictWebService = (DictWebService) WebServiceTools
                        .getOperationClass(dataModel.getDistrict(),
                                DictWebService.class);
                LcTerminalProtoMappingDBEntity protoMapping = (LcTerminalProtoMappingDBEntity) ObjectUtils
                        .bytesToObject(dataModel.getOperationObject());
                if (opretionType.equals(Constant.PropertiesKey.OperationTypeParameters.delete)) {
                    // 删除功能先不做
                    // configWebService.deleteNodeConfigByNodeCode(nodeConfig.getNode_code());
                } else {
                    result = dictWebService.bindProtoMapping(
                            protoMapping.getLogic_code(),
                            stringToArray(dataModel));
                }
                // 根据返回值进行判断，如果返回失败，则不做处理，成功则处理
                if (result == PlatformResponseResult.success) {
                    dataModel.setStatus(PlatformResponseResult.success_VALUE);
                    //删除缓存数据
                    SynchronousDataCache.remove(dataModel.getDistrict(), dataModel);

                } else {
                    dataModel.setStatus(PlatformResponseResult.failure_VALUE);
                }
                sydService.updateSynchronousData(dataModel);
            }
        } catch (Exception e) {
            log.info("终端指令映射同步失败");
            e.printStackTrace();
        }
    }

    /**
     * 字典类型同步
     *
     * @param dataModel
     * @param opretionType
     * @param type
     * @param sydService
     */
    private void dictTypeSynchronization(
            LcSynchronizationLogDBEntity dataModel, String opretionType,
            String type, SynchronousManage sydService) {
        PlatformResponseResult result = null;
        try {
            if (type.equals(Constant.PropertiesKey.DBTableName.LC_DICT_TYPE)) {
                DictWebService dictWebService = (DictWebService) WebServiceTools
                        .getOperationClass(dataModel.getDistrict(),
                                DictWebService.class);
                LcDictTypeDBEntity dictType = (LcDictTypeDBEntity) ObjectUtils
                        .bytesToObject(dataModel.getOperationObject());
                if (opretionType.equals(Constant.PropertiesKey.OperationTypeParameters.delete)) {
                    // 删除功能先不做
                    // configWebService.deleteNodeConfigByNodeCode(nodeConfig.getNode_code());
                } else {
                    result = dictWebService.saveOrUpdateDictType(dictType);
                }
                // 根据返回值进行判断，如果返回失败，则不做处理，成功则处理

                if (result == PlatformResponseResult.success) {
                    dataModel.setStatus(PlatformResponseResult.success_VALUE);
                    //删除缓存数据
                    SynchronousDataCache.remove(dataModel.getDistrict(), dataModel);

                } else {
                    dataModel.setStatus(PlatformResponseResult.failure_VALUE);
                }
                sydService.updateSynchronousData(dataModel);
            }
        } catch (Exception e) {
            log.info("字典类型同步失败");
            e.printStackTrace();
        }
    }

    /**
     * 字典明细同步
     *
     * @param dataModel
     * @param opretionType
     * @param type
     * @param sydService
     */
    private void dictSynchronization(LcSynchronizationLogDBEntity dataModel,
                                     String opretionType, String type,
                                     SynchronousManage sydService) {
        PlatformResponseResult result = null;
        try {
            if (type.equals(Constant.PropertiesKey.DBTableName.LC_DICT)) {
                DictWebService dictWebService = (DictWebService) WebServiceTools
                        .getOperationClass(dataModel.getDistrict(),
                                DictWebService.class);
                LcDictDBEntity dict = (LcDictDBEntity) ObjectUtils
                        .bytesToObject(dataModel.getOperationObject());
                if (opretionType.equals(Constant.PropertiesKey.OperationTypeParameters.delete)) {
                    // 删除功能先不做
                    // configWebService.deleteNodeConfigByNodeCode(nodeConfig.getNode_code());
                } else {
                    result = dictWebService.saveOrUpdateDict(dict);
                }
                // 根据返回值进行判断，如果返回失败，则不做处理，成功则处理
                if (result == PlatformResponseResult.success) {
                    dataModel.setStatus(PlatformResponseResult.success_VALUE);
                    //删除缓存数据
                    SynchronousDataCache.remove(dataModel.getDistrict(), dataModel);

                } else {
                    dataModel.setStatus(PlatformResponseResult.failure_VALUE);
                }
                sydService.updateSynchronousData(dataModel);
            }
        } catch (Exception e) {
            log.info("字典明细同步失败");
            e.printStackTrace();
        }
    }

    /**
     * 节点配置同步
     *
     * @param dataModel
     * @param district
     * @param opretionType
     * @param type
     * @param sydService
     */
    private void nodeConfigSynchronization(
            LcSynchronizationLogDBEntity dataModel, String district,
            String opretionType, String type,
            SynchronousManage sydService) {
        PlatformResponseResult result;
        try {
            if (type.equals(Constant.PropertiesKey.DBTableName.LC_NODE_CONFIG)) {
                ConfigWebService configWebService = (ConfigWebService) WebServiceTools
                        .getOperationClass(district, ConfigWebService.class);
                LcNodeConfigDBEntity nodeConfig = (LcNodeConfigDBEntity) ObjectUtils
                        .bytesToObject(dataModel.getOperationObject());
                if (opretionType.equals(Constant.PropertiesKey.OperationTypeParameters.delete)) {
                    // 分区根据节点配置的节点标识删除
                    result = configWebService.deleteNodeConfigByNodeCode(nodeConfig
                            .getNode_code());
                } else {
                    // 分区根据节点配置的节点标识进行update
                    result = configWebService.saveOrUpdateNodeConfig(nodeConfig);
                }
                // 根据返回值进行判断，如果返回失败，则不做处理，成功则处理
                if (result == PlatformResponseResult.success) {
                    dataModel.setStatus(PlatformResponseResult.success_VALUE);
                    //删除缓存数据
                    SynchronousDataCache.remove(district, dataModel);

                } else {
                    dataModel.setStatus(PlatformResponseResult.failure_VALUE);
                }
                sydService.updateSynchronousData(dataModel);
            }
        } catch (Exception e) {
            log.info("节点配置失败");
            e.printStackTrace();
        }
    }

    /**
     * 服务配置同步
     *
     * @param dataModel
     * @param district
     * @param opretionType
     * @param type
     * @param sydService
     */
    private void serviceConfigSynchronization(
            LcSynchronizationLogDBEntity dataModel, String district,
            String opretionType, String type,
            SynchronousManage sydService) {
        PlatformResponseResult result = null;
        try {
            if (type.equals(Constant.PropertiesKey.DBTableName.LC_SERVICE_CONFIG)) {
                ConfigWebService configWebService = (ConfigWebService) WebServiceTools
                        .getOperationClass(district, ConfigWebService.class);
                LcServiceConfigDBEntity serviceConfig = (LcServiceConfigDBEntity) ObjectUtils
                        .bytesToObject(dataModel.getOperationObject());
                if (opretionType.equals(Constant.PropertiesKey.OperationTypeParameters.delete)) {
                    // 分区根据服务配置的用户名删除
                    result = configWebService
                            .deleteServiceConfigByAuthName(serviceConfig
                                    .getAuth_name());
                } else {
                    // 分区根据服务配置的用户名进行update
                    result = configWebService.saveOrUpdateServiceConfig(serviceConfig,
                            new int[]{Integer.parseInt(district)}, serviceConfig.getAuth_name());
                }
                // 根据返回值进行判断，如果返回失败，则不做处理，成功则处理
                if (result == PlatformResponseResult.success) {
                    dataModel.setStatus(PlatformResponseResult.success_VALUE);
                    //删除缓存数据
                    SynchronousDataCache.remove(district, dataModel);

                } else {
                    dataModel.setStatus(PlatformResponseResult.failure_VALUE);
                }
                sydService.updateSynchronousData(dataModel);
            }
        } catch (NumberFormatException e) {
            log.info("服务配置同步失败");
            e.printStackTrace();
        }
    }

    /**
     * string转int[]方法
     *
     * @param dataModel
     * @return
     */
    private int[] stringToArray(LcSynchronizationLogDBEntity dataModel) {
        String districts = dataModel.getPrimaryKeys();
        String[] array = districts.split("\\,");
        int[] dis = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            dis[i] = Integer.parseInt(array[i]);
        }
        return dis;
    }

    /**
     * 终端同步
     *
     * @param dataModel
     * @param district
     * @param opretionType
     * @param type
     * @param sydService
     */
    private void terminalSynchronization(
            LcSynchronizationLogDBEntity dataModel, String district,
            String opretionType, String type,
            SynchronousManage sydService) {
        PlatformResponseResult result;
        try {
            if (type.equals(Constant.PropertiesKey.DBTableName.LC_TERMINAL_INFO)) {
                TerminalWebService termianlWebService = (TerminalWebService) WebServiceTools
                        .getOperationClass(district, TerminalWebService.class);
                if (opretionType.equals(Constant.PropertiesKey.OperationTypeParameters.delete)) {
                    // 终端删除同步数据时分区根据终端标识删除
                    LcTerminalInfoDBEntity terminal = (LcTerminalInfoDBEntity) ObjectUtils
                            .bytesToObject(dataModel.getOperationObject());
                    result = termianlWebService.deleteTerminalInfoByTerminalId(terminal
                            .getTerminal_id());
                } else {
                    String isAddString = dataModel.getOperationType();
                    boolean isAdd = false;
                    if ((Constant.PropertiesKey.OperationTypeParameters.save).equals(isAddString)) {
                        isAdd = true;
                    }
                    // 分区根据终端标识进行update
                    result = termianlWebService.saveOrUpdateTerminalInfo2((LcTerminalInfoDBEntity) ObjectUtils
                            .bytesToObject(dataModel.getOperationObject()), isAdd);
                }
                // 根据返回值进行判断，如果返回失败，则不做处理，成功则处理
                if (result == PlatformResponseResult.success) {
                    dataModel.setStatus(PlatformResponseResult.success_VALUE);
                    //删除缓存数据
                    SynchronousDataCache.remove(district, dataModel);
                } else {
                    dataModel.setStatus(PlatformResponseResult.failure_VALUE);
                }
                sydService.updateSynchronousData(dataModel);
            }
        } catch (Exception e) {
            log.info("终端同步失败");
            e.printStackTrace();
        }
    }
}

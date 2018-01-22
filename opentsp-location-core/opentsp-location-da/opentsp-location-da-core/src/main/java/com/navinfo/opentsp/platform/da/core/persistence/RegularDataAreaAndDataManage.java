package com.navinfo.opentsp.platform.da.core.persistence;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.*;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave.RegularDataSave;

import java.util.List;
import java.util.Map;


public interface RegularDataAreaAndDataManage {

    /**
     * 查询终端参数
     *
     * @param terminalId
     * @param paramterCode {@link Integer}终端参数编码
     * @return {@link List}<{@link LcTerminalParaDBEntity}>
     */
    abstract List<LcTerminalParaDBEntity> getTerminalParameter(long terminalId,
                                                               int... paramterCode) throws Exception;

    /**
     * 根据编码，查询终端参数
     *
     * @param paramterCode
     * @return
     * @throws Exception
     */
    abstract List<LcTerminalParaDBEntity> queryTerminalByParaCode(int... paramterCode) throws Exception;

    /**
     * 据TA节点IP查询区域数据 <br>
     * 1、查询TA节点下的终端列表 <br>
     * 2、据1查询的终端列表查询区域
     *
     * @param districtCode
     * @return {@link List}<{@link Map}> Key<br>
     * topic=区域信息-->{@link LcTerminalAreaDBEntity}<br>
     * detailed=区域数据-->{@link LcTerminalAreaDataDBEntity}
     */
    abstract List<Map<String, Object>> queryAreaInfo(int districtCode,
                                                     long node_code) throws Exception;

    /**
     * 删除指定终端区域信息 <br>
     * 其区域对应的相关规则也需要进行删除
     *
     * @param terminalId
     * @param originalAreaId
     * @return {@link OptResult}
     */
    abstract OptResult deleteAreaInfo(long terminalId, int... originalAreaId) throws Exception;

    /**
     * 规则查询 <br>
     * 查询的方式同区域数据查询
     *
     * @param districtCode
     * @return {@link List}<{@link LcTerminalAreaDBEntity}>
     */
    abstract List<LcTerminalRuleDBEntity> queryRegularData(int districtCode,
            long node_code)throws Exception;

    /**
     * 规则数据存储
     *
     * @param ruleEntity
     * @return {@link OptResult}
     */
//	abstract OptResult saveRegular(List<RegularData> regularDatas);

    /**
     * 区域存储,同时要存储区域数据信息
     *
     * @param lcTerminalArea
     * @return
     */
//	abstract OptResult saveAreaInfo(List<AreaInfo> areaInfo);

    /**
     * 规则删除，删除与区域无关的规则
     *
     * @param terminalId
     * @param regularCode
     * @param regularIdentifyList
     * @return {@link OptResult}
     */
    abstract OptResult deleteRegular(long terminalId, int regularCode, List<Long> regularIdentifyList) throws Exception;

    /**
     * 规则删除<br>
     * 针对与区域相关规则的删除
     *
     * @param terminalId
     * @param originalAreaId
     * @param originalAreaId
     * @return {@link OptResult}
     */
    abstract OptResult deleteRegularByAreaId(long terminalId,
                                             int... originalAreaId) throws Exception;


    /**
     * 根据终端标识查询规则
     *
     * @param terminalId 终端标识
     * @return
     */
    public List<LcTerminalRuleDBEntity> queryTerminalRuleRes(long terminalId, int currentPage, int pageSize) throws Exception;

    /**
     * 根据参数查询区域
     *
     * @param terminalId 终端标识
     * @param areaId     区域标识
     * @param start      开始时间
     * @param end        结束时间
     * @return List< LcTerminalAreaDBEntity>
     */
    public List<LcTerminalAreaDBEntity> queryTerminalAreaRes(long terminalId, int areaId, long start, long end, int currentPage, int pageSize) throws Exception;

    /**
     * 根据规则编码查询所有规则
     *
     * @param code
     * @return
     */
    public List<LcTerminalRuleDBEntity> getRegularDatasByRegularCode(RegularCode code);

    /**
     * 根据区域ID查询区域数据
     *
     * @param terminalId 终端标识
     * @param taId       区域ID
     * @return
     */
    public List<LcTerminalAreaDataDBEntity> queryAreaDataRes(long terminalId, int taId) throws Exception;

    /**
     * 根据终端标识和规则id查询规则
     *
     * @param terminalId 终端标识
     * @return
     */
    public LcTerminalRuleDBEntity queryTerminalRuleById(long terminalId, int trId) throws Exception;

    /**
     * 根据终端标识和区域id查询规则
     *
     * @param terminalId 终端标识
     * @return
     */
    public List<LcTerminalRuleDBEntity> getRegularData(long terminalId, int areaId) throws Exception;

    /**
     * 根据终端标识和区域id列表查询规则
     *
     * @param terminalId 终端标识
     * @return
     */
    public List<LcTerminalRuleDBEntity> getRegularDataByAreaIds(
            long terminalId, int[] areaIds) throws Exception;

    public List<LcTerminalRuleDBEntity> getRegularDataByAreaIds(List<Long> areaIds) throws Exception;

    public List<LcTerminalAreaAndDataDBEntity> queryAreaInfoByTerminalId(long[] terminalIds) throws Exception;

    abstract List<LcTerminalRuleDBEntity> getRegularDataByTerminalIds(
            List<Long> terminalId) throws Exception;

    abstract OptResult saveRegularAndAreaInfo(RegularDataSave rds) throws Exception;
}

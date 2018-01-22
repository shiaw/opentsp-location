package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalInfoDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalInfoDBInfo;

import java.util.List;


public interface LcTerminalInfoDao extends BaseDao<LcTerminalInfoDBEntity> {


    /**
     * 获取所有终端      重构
     *
     * @return
     */
    public List<LcTerminalInfoDBEntity> getAllTerminal();

    /**
     * 根据终端标识查找
     *
     * @param terminalId
     * @return
     */
    public LcTerminalInfoDBEntity getByTerminalId(long terminalId);

    public LcTerminalInfoDBEntity getInfoByTerminalId(long terminalId);


    public LcTerminalInfoDBEntity getInfoByDeviceId(String deviceId);

    /**
     * 根据节点服务节点标识和服务区域查找
     *
     * @param node_code
     * @param district
     * @return
     */
    public List<LcTerminalInfoDBEntity> getByIpAndDistrict(long node_code, int district);

    /**
     * 根据协议和服务区域查找
     *
     * @param proto_code
     * @return
     */
    public List<LcTerminalInfoDBEntity> getByProto(int proto_code, int district);

    /**
     * 根据服务区域查找
     *
     * @param district
     * @return
     */
    public List<LcTerminalInfoDBEntity> getByDistrict(int district);

    /**
     * 根据数据状态和服务区域查找
     *
     * @param data_status
     * @return
     */
    public List<LcTerminalInfoDBEntity> getByStatus(short data_status, int district);

    /**
     * 更新终端所在TA节点信息
     *
     * @param terminalId
     * @param districtCode
     * @param node_code
     * @return
     */
    public int updateTerminalInfo(long terminalId, int districtCode,
                                  long node_code, long changeTId);

    /**
     * 根据终端标识，协议类型名称，开始时间，结束时间查询终端信息
     *
     * @param terminalId 终端标识
     * @param protoType  协议类型
     * @param start      开始时间
     * @param end        结束时间
     * @return
     */
    public List<LcTerminalInfoDBEntity> queryTerminalParaRes(long terminalId,
                                                             int protoType, String deviceId, long changeId, long start, long end, int flag, int currentPage, int pageSize);

    public int queryTerminalInfoResCount(long terminalId,
                                         int protoType, String deviceId, long changeId, long start, long end);

    /**
     * 根据终端标识标识删除终端信息
     */
    public int deleteByTerminalId(long terminalId);

    /**
     * 根据终端标识物理删除终端信息。
     *
     * @param terminalId
     * @return
     */
    public int deletePhysiscByTerminalId(long terminalId);

    /**
     * 根据终端标识修改终端信息
     *
     * @param terminalInfo 终端信息
     * @return
     */
    public int updateByTerminalId(LcTerminalInfoDBEntity terminalInfo);

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    public int batchAddTerminalInfo(List<LcTerminalInfoDBEntity> list);

    /**
     * 查询所有终端
     *
     * @param district 分区号
     * @return
     */
    public List<LcTerminalInfoDBInfo> getByNodeCodeAndDistrict(int district);

    public int updateByDeviceId(LcTerminalInfoDBEntity terminalInfo);

    public List<Long> getTerminalIds();
}

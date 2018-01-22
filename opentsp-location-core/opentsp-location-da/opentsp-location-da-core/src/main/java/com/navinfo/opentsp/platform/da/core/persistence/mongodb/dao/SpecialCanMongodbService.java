package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao;

import com.mongodb.WriteResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCFaultInfo;
import com.navinfo.opentsp.platform.location.protocol.common.LCTerminalStatisticData;

/**
 * @author: ChenJie
 * @Description:
 * @Date 2017/11/9
 * @Modified by:
 */
public interface SpecialCanMongodbService {
    /**
     * 保存统计信息0F38到mongodb
     * @param terminalId
     * @param statisticData
     * @return WriteResult
     * */
    WriteResult saveTerminalStatisticData(String terminalId, LCTerminalStatisticData.StatisticData statisticData);

    /**
     * 保存故障信息0F39到mongodb
     * @param terminalId
     * @param faultInfo
     * @return WriteResult
     * */
    WriteResult saveFaultData(String terminalId, LCFaultInfo.FaultInfo faultInfo);
}

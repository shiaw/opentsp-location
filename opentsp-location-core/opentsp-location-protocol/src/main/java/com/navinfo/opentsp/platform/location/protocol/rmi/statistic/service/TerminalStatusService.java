package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service;

import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa.DASTerminalStatus;

import java.util.List;



/****
 * 
 * 每天的统计计算后，每个终端状态的存储/读取，存储与Redis中
 * 
 *  定义一个远程接口，继承Remote接口，其中需要远程调用的方法必须抛出RemoteException异常 
 */ 

public interface TerminalStatusService {
	
    /**
     * 获取多个终端的最近一次统计计算状态集合
     * 
     * @param terminalIds
     * @return
     * @
     */
    public List<DASTerminalStatus> getTerminalStatusData(List<Long> terminalIds) ;
    

    /**
     * 批量存储多个终端统计计算状态 
     *  
     * @param terminalStatusList
     * @return
     * @
     */
    public PlatformResponseResult saveTerminalStatusData(List<DASTerminalStatus> terminalStatusList) ;
    
    /**
     * 单个终端统计计算状态存储
     * 
     * @param status
     * @return
     * @
     */
    public PlatformResponseResult saveTerminalStatusData(DASTerminalStatus status) ;

    public PlatformResponseResult removeTerminalStatusData(String[] tids)
			; 

	/**
	 * rmi服务可用性检测
	 * @return
	 * @
	 */
	public boolean isConnected() ;
}

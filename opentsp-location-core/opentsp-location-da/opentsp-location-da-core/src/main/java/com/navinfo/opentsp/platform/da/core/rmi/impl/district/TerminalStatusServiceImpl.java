package com.navinfo.opentsp.platform.da.core.rmi.impl.district;

import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.DASServiceImp;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa.DASTerminalStatus;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TerminalStatusService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DSA计算任务状态缓存相关服务
 * @author jin_s
 *
 */
@Service
public class TerminalStatusServiceImpl  implements TerminalStatusService {
	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 1L;

	public TerminalStatusServiceImpl() {
		super();

	}
	DASServiceImp dasService=new DASServiceImp();
	/**
	 * DSA计算任务状态缓存查询
	 *
	 */
	@Override
	public List<DASTerminalStatus> getTerminalStatusData(List<Long> terminalIds) {
		long[] terminalIdArray=new long[terminalIds.size()];
		for(int i=0;i<terminalIds.size();i++){
			terminalIdArray[i]=terminalIds.get(i);
		}
		return dasService.findTerminalStatus(terminalIdArray);
	}
	/**
	 * DSA计算任务状态缓存批量存储
	 *
	 */
	@Override
	public PlatformResponseResult saveTerminalStatusData(
			List<DASTerminalStatus> terminalStatusList) {
		return dasService.addTerminalStatus(terminalStatusList);
	}
	/**
	 * DSA计算任务状态缓存单笔存储
	 *
	 */
	@Override
	public PlatformResponseResult saveTerminalStatusData(
			DASTerminalStatus status)  {
		return dasService.addTerminalStatusSimple(status);
	}
	/**
	 * DSA计算任务状态缓存单笔删除
	 *
	 */
	@Override
	public PlatformResponseResult removeTerminalStatusData(
			String[] tids) {
		return dasService.removeTerminalStatusSimple(tids);
	}
	public static void main(String[] args) {
		DASTerminalStatus dasTerminalStatus = new DASTerminalStatus();
		dasTerminalStatus.setTerminalId(123456);
			(new TerminalStatusServiceImpl()).saveTerminalStatusData(dasTerminalStatus);
	}
	@Override
	public boolean isConnected() {
		return true;
	}
}

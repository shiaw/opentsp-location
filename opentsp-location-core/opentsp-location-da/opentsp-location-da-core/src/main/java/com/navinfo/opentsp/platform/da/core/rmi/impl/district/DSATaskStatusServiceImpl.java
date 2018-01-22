package com.navinfo.opentsp.platform.da.core.rmi.impl.district;

import com.navinfo.opentsp.platform.da.core.persistence.redis.service.TerminalStatisticStatusService;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.DASTaskStatusServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalStatisticStatusServiceImpl;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.DSATaskStatusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;



/**
 * DSA统计任务状态服务
 * @author jin_s
 *
 */
@Service
public class DSATaskStatusServiceImpl   implements DSATaskStatusService {
	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 1L;

	public DSATaskStatusServiceImpl() {
		super();

	}
	DASTaskStatusServiceImpl service=new DASTaskStatusServiceImpl();
	TerminalStatisticStatusService statusService = new TerminalStatisticStatusServiceImpl();
	/**
	 * 查询终端统计计算任务状态
	 */
	@Override
	public Map<Long,Integer> getDSATaskStatusData(List<Long> terminalIds) {
		return service.findTerminalStatus(terminalIds);
	}
	/**
	 * 存储查询的key
	 */
	@Override
	public void saveTerminalDSATaskStatus(long terminalId) {
		if(terminalId>0)
			service.saveDataComplete(String.valueOf(terminalId));
	}
	@Override
	public Set<Long> getStatisticByType(int type) {
		return statusService.getTerminalsByType(type);
	}

}

package com.navinfo.opentsp.platform.dp.core.rule.handler.single;

import com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver.RP_2151_DispatchMessage;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver.RP_2170_TerminalStatusControl;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.RmiConstant;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.handler.SendKafkaHandler;
import com.navinfo.opentsp.platform.dp.core.rmi.DaRmiService;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;


/********************
 * 报警规则处理
 * 
 * @author claus
 *
 */
public abstract class SingleRegularProcess {

	@Autowired
	private DaRmiService daRmiService;

	@Resource
	protected RP_2170_TerminalStatusControl writeToTermianl2170;

	@Resource
	protected RP_2151_DispatchMessage writeToTermianl2151;

	@Resource
	protected SendKafkaHandler sendKafkaHandler;

	//log
	public static Logger log = LoggerFactory.getLogger(SingleRegularProcess.class);

	public abstract GpsLocationDataEntity process(RuleEntity rule , GpsLocationDataEntity dataEntity);
	
	
	private SingleRegularProcess nextProcess;
	public SingleRegularProcess() {
		super();
	}

	public final void addNextProcess(SingleRegularProcess process){
		this.nextProcess = process;
	}
	
	public final void handler(RuleEntity rule ,GpsLocationDataEntity dataEntity) {
		this.process(rule,dataEntity);
		if (this.nextProcess != null) {
			this.nextProcess.handler(rule,dataEntity);
		}
	}

	/**
	 * 向DA节点发送数据
	 * @param packet
	 * @return
	 */
	protected int writeToDataAccess(Packet packet){
		daRmiService.callRmi(RmiConstant.RMI_SERVICE_ID,
				RmiConstant.RMI_INTERFACE_NAME,
				RmiConstant.RmiInterFaceEum.DaRmiInterface.getClassType(),
				packet);
		return 0;
	}
}

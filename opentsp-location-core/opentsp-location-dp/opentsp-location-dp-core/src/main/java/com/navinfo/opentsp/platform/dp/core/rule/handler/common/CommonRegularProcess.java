package com.navinfo.opentsp.platform.dp.core.rule.handler.common;

import com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver.RP_2151_DispatchMessage;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.common.RmiConstant;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.handler.SendKafkaHandler;
import com.navinfo.opentsp.platform.dp.core.rmi.DaRmiService;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * User: zhanhk
 * Date: 16/10/21
 * Time: 上午10:32
 */
public abstract class CommonRegularProcess {

	@Autowired
	protected SendKafkaHandler sendKafkaHandler;

	@Autowired
	private DaRmiService daRmiService;

	@Autowired
	protected RP_2151_DispatchMessage writeToTermianl;

    //log
    public static Logger log = LoggerFactory.getLogger(CommonRegularProcess.class);
    //
    public abstract GpsLocationDataEntity process(double distance, AreaEntity areaEntity , GpsLocationDataEntity dataEntity);

	private CommonRegularProcess nextProcess;

	public final void addNextProcess(CommonRegularProcess process){
		this.nextProcess = process;
	}

	public final void handler(double distance,AreaEntity areaEntity,GpsLocationDataEntity dataEntity) {
		this.process(distance,areaEntity,dataEntity);
		if (this.nextProcess != null) {
			this.nextProcess.handler(distance,areaEntity,dataEntity);
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

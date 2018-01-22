package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;


/*******************************
 * RP服务节点收到心跳汇报，统一回复通用应答
 * 
 * @author claus
 *
 */
@RPAnno(id="1099")
public class RP_1099_Heartbeat extends RPCommand {

	@Override
	public int processor(Packet packet) {
		log.info("收到来自[" + packet.getFrom() + "]节点心跳数据.");
		return super.commonResponses(packet.getProtocol(), packet.getFrom(), packet.getSerialNumber(), AllCommands.Platform.Heartbeat_VALUE, PlatformResponseResult.success_VALUE);
	}

}

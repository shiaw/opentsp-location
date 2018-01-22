package com.navinfo.opentsp.platform.da.core.connector.mm.procotol;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes.ServerCommonRes;
import  com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import  com.navinfo.opentsp.platform.da.core.connector.mm.session.MMMutualSession;
import  com.navinfo.opentsp.platform.da.core.connector.mm.session.MMMutualSessionManage;
import com.navinfo.opentsp.platform.location.kit.Command;
import com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import com.navinfo.opentsp.platform.location.kit.Packet;

public abstract class MMCommand extends Command {
	public static Logger logger = LoggerFactory.getLogger(MMCommand.class);

	/**
	 * 发送通用应答
	 *
	 * @param uniqueMark
	 * @param responsesSerialNumber
	 * @param responsesId
	 * @param result
	 * @return
	 */
	public int commonResponses(long to, int responsesSerialNumber,
			int responsesId, PlatformResponseResult result) {
		Packet packet = new Packet(true);
		ServerCommonRes.Builder builder = ServerCommonRes.newBuilder();
		builder.setSerialNumber(responsesSerialNumber);
		builder.setResponseId(responsesId);
		builder.setResults(result);

		packet.setCommand(AllCommands.Platform.ServerCommonRes_VALUE);
		packet.setProtocol(LCMessageType.PLATFORM);
		packet.setContent(builder.build().toByteArray());
		// 将唯一标识设置为当前节点标识
		packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		packet.setFrom(NodeHelper.getNodeCode());
		packet.setTo(to);
		return this.write(packet);
	}

	/**
	 * 往指定MM链路发送数据
	 */
	@Override
	public int write(Packet packet) {
		MMMutualSession mmMutualSession = MMMutualSessionManage.getInstance().getMMMutualSessionForCode(packet.getTo());
		if(mmMutualSession != null){
			IoSession ioSession = mmMutualSession.getIoSession();
			if(ioSession != null && ioSession.isConnected()){
				ioSession.write(packet);
			}else{
				logger.error("MM[ "+packet.getTo()+" ]链路已关闭");
				return -1;
			}
		}else{
			logger.error("未找到MM[ "+packet.getTo()+" ]链路信息");
			return -1;
		}
		return 0;
	}

	/**
	 * 向任意一个MM节点发送数据<br>
	 * 优先向Master节点发送;如果Master不可用,则向Slave发送
	 *
	 * @param packet
	 * @return
	 */
	public int writeMasterOrSlave(Packet packet) {
		MMMutualSession mmMutualSession = MMMutualSessionManage.getInstance().getMM();
		if(mmMutualSession != null){
			mmMutualSession.getIoSession().write(packet);
		}
		return 0;
	}

	/**
	 * 广播
	 * 
	 * @param packet
	 * @return
	 */
	public int broadcast(Packet packet) {
		Map<Long, MMMutualSession> map = MMMutualSessionManage.getInstance().get();
		if(map != null){
			for (Entry<Long, MMMutualSession> e : map.entrySet()) {
				MMMutualSession mutualSession = e.getValue();
				if(mutualSession != null){
					IoSession ioSession = mutualSession.getIoSession();
					if(ioSession != null && ioSession.isConnected()){
						ioSession.write(packet);
					}
				}
			}
		}
		return 0;
	}

}

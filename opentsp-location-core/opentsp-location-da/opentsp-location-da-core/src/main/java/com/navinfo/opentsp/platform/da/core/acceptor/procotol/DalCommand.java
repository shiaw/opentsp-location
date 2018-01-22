

package com.navinfo.opentsp.platform.da.core.acceptor.procotol;

import com.navinfo.opentsp.platform.da.core.acceptor.session.MutualSession;
import com.navinfo.opentsp.platform.da.core.acceptor.session.MutualSessionManage;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Command;
import com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes.ServerCommonRes;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class DalCommand extends Command {
	public static Logger logger = LoggerFactory.getLogger(DalCommand.class);

	/**
	 * 发送通用应答
	 *
	 * @param to
	 * @param responsesSerialNumber
	 * @param responsesId
	 * @param result
	 * @return
	 */
	public int commonResponsesForPlatform(long to, int responsesSerialNumber,
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

	@Override
	public int write(Packet packet) {
		MutualSession mutualSession = MutualSessionManage.getInstance()
				.getMutualSessionForNodeCode(packet.getTo());
		if (mutualSession != null) {
			IoSession ioSession = mutualSession.getIoSession();
			if (ioSession != null && !ioSession.isClosing()) {
				try{
					ioSession.write(packet);
				}catch(Exception e){
					e.printStackTrace();
				}
				return 1;
			} else {
				logger.error("节点编号[" + packet.getTo() + "]的链路已关闭.command:"+packet.getCommandForHex());
			}
		} else {
			logger.error("未找到连接节点编号[" + packet.getTo() + "]的链路信息.command:"+packet.getCommandForHex());
		}
		return 0;
	}
}

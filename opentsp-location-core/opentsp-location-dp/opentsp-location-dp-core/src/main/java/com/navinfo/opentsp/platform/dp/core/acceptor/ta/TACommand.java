package com.navinfo.opentsp.platform.dp.core.acceptor.ta;

import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes.ServerCommonRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType.PLATFORM;


public abstract class TACommand {
	public static Logger log = LoggerFactory.getLogger(TACommand.class);

	/**
	 * 发送通用应答(终端)
	 * @param to
	 * @param responsesSerialNumber
	 * @param responsesId
	 * @param result
     * @return
     */
	public int commonResponsesForTerminal(long to, int responsesSerialNumber,
			int responsesId, PlatformResponseResult result) {
		Packet packet = new Packet(true);
		ServerCommonRes.Builder builder = ServerCommonRes.newBuilder();
		builder.setSerialNumber(responsesSerialNumber);
		builder.setResponseId(responsesId);
		builder.setResults(result);

		// packet.setCommand(AllCommands.Platform.ServerCommonRes_VALUE);
		packet.setProtocol(PLATFORM);
		packet.setContent(builder.build().toByteArray());
		// 将唯一标识设置为当前节点标识
		packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		packet.setFrom(NodeHelper.getNodeCode());
		packet.setTo(to);
		return this.write(packet);
	}

	/**
	 * 发送通用应答(平台)
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
		packet.setProtocol(PLATFORM);
		packet.setContent(builder.build().toByteArray());
		// 将唯一标识设置为当前节点标识
		packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		packet.setFrom(NodeHelper.getNodeCode());
		packet.setTo(to);
		return this.writeToTaPlatform(packet);
	}

	int writeToTaPlatform(Packet packet) {
		// TODO: 16/8/24
//		TAMutualSession taMutualSession = TAMutualSessionManage.getInstance()
//				.getSessionForNodeCode(packet.getTo());
//		if (taMutualSession != null) {
//			IoSession ioSession = taMutualSession.getIoSession();
//			if (ioSession != null && ioSession.isConnected()) {
//				ioSession.write(packet);
//				return 1;
//			} else {
//				log.error("DP[" + NodeHelper.getNodeCode() + "]-TA["
//						+ packet.getTo() + "]链路关闭,发送数据失败.");
//				return 0;
//			}
//		}
//		log.error("DP[" + NodeHelper.getNodeCode() + "]-TA[" + packet.getTo()
//				+ "]链路未找到,发送数据失败.");
		return -1;
	}

	/**
	 * 向TA节点发送数据
	 */
	public int write(Packet packet) {
		// TODO: 16/8/24
//		TerminalEntity terminalEntity = TerminalCache.getInstance().getTerminal(packet.getTo());
//		if (terminalEntity != null) {
//			NodeLinkPo nodeLinkPo = RedisUtil.getRedis().getHashValue("NodeLink",packet.getUniqueMark(),NodeLinkPo.class);
//			if (nodeLinkPo != null) {
//				if(nodeLinkPo.isOnlineStatus()) {
//					//RP广播对应对应DP链路
//					if(NodeHelper.getNodeCode() == nodeLinkPo.getDpNode()) {
//						TAMutualSession mutualSession = TAMutualSessionManage.getInstance()
//								.getSessionForNodeCode(nodeLinkPo.getTaNode());
//						if (mutualSession != null) {
//							if (mutualSession.getIoSession() != null
//									&& !mutualSession.getIoSession().isClosing()) {
//								mutualSession.getIoSession().write(packet);
//							} else {
//								log.error("终端[ " + packet.getTo() + " ]链路关闭,发送数据失败.");
//							}
//						} else {
//							log.error("终端[ " + packet.getTo() + " ]链路未找到,发送数据失败.");
//						}
//					} else {
//						log.error("RP广播,终端DP链路不匹配.");
//					}
//				} else {
//					//TODO 不在线处理
//					log.error("终端不在线.");
//				}
//			} else {
//				log.error("终端[ " + packet.getTo() + " ]TA集群链路未找到,发送数据失败.");
//			}
//		} else {
//			log.error("终端[ " + packet.getTo() + " ]未找到,发送数据失败.");
//		}
		return 0;
	}

	public int writeToDataAccess(Packet packet) {
		// TODO: 16/8/24 to DA
//		DalMutualSession dalMutualSession = DalMutualSessionManage
//				.getInstance().getActiveSession();
//		if (dalMutualSession != null) {
//			IoSession ioSession = dalMutualSession.getIoSession();
//			if (ioSession != null && ioSession.isConnected()) {
//				ioSession.write(packet);
//				return 1;
//			} else {
//				log.error("DP>DA链路关闭,发送数据失败.");
//				return 0;
//			}
//		} else {
//			log.error("DP>DA链路未找到,发送数据失败.");
//			return 0;
//		}
		return 0;
	}

	public int writeToRequestProcessing(Packet packet) {
		// TODO: 16/8/24  需要通过一致性hash算法,将终端数据分布到上层RP节点
		//RPClusters.execute(packet);
		return 0;
	}

}

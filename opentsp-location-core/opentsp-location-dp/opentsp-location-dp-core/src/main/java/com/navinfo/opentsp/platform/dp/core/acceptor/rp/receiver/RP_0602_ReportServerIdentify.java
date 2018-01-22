package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCReportServerIdentify.ReportServerIdentify;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.entity.ConnectStatus;

/**
 * 节点汇报<br>
 * 
 * @author lgw
 * 
 */
@RPAnno(id="0602")
public class RP_0602_ReportServerIdentify extends RPCommand {

	@Override
	public int processor(Packet packet) {
		try {
			ReportServerIdentify reportServerIdentify = ReportServerIdentify
					.parseFrom(packet.getContent());
			log.error("收到RP层节点["+reportServerIdentify.getNodeCode()+"]汇报节点编号.");
			

			//RP节点连接DP节点汇报节点标识时，更新RPNodeList中对应RPNodeInfo.ConnectStatus=SUCCESS,连接成功
//			RPNodeList.updateRPNodeStatus(reportServerIdentify.getNodeCode(), ConnectStatus.SUCCESS);
			//绑定节点编号与Session
			long sessionId = Long.parseLong(packet.getParamterForString("sessionId"));
			// TODO: 16/9/5
			//RPMutualSessionManage.getInstance().bind(reportServerIdentify.getNodeCode(), sessionId);
			//设置Session属性
			// TODO: 16/9/5
			//RPMutualSessionManage.getInstance().getMutualSessionForSessionId(sessionId).getIoSession().setAttribute("nodeCode", reportServerIdentify.getNodeCode());
			super.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
			
			//添加新的RP节点到一致性哈希
			//Node node = new Node();
			//node.setNodeCode(reportServerIdentify.getNodeCode());
			//RPClusters.addNode(node);			
			
			/*Packet _out_packet = new Packet(true);
			_out_packet.setCommand(AllCommands.NodeCluster.DPNodeTerminalInfo_VALUE);
			_out_packet.setProtocol(LCMessageType.PLATFORM);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setFrom(NodeHelper.getNodeCode());
			
			DPNodeTerminalInfo.Builder builder = DPNodeTerminalInfo.newBuilder();
			Map<Long, TerminalEntity> terminals = TerminalCache.getInstance().get();
			for (Entry<Long, TerminalEntity> e : terminals.entrySet()) {
				Node rp = RPClusters.getNode(e.getKey());
				if(rp == null){
					continue;
				}
				TerminalEntity terminal = e.getValue();
				TerminalRPHashInfo.Builder hashBuilder = TerminalRPHashInfo.newBuilder();
				hashBuilder.setRpNodeCode(rp.getNodeCode());
				TerminalInfo.Builder terminalInfoBuilder = TerminalInfo.newBuilder();
				terminalInfoBuilder.setTerminalId(e.getKey());
				terminalInfoBuilder.setAuthCode(terminal.getAuthCode());
				terminalInfoBuilder.setNodeCode(terminal.getTaNodeCode());
				terminalInfoBuilder.setProtocolType(terminal.getTerminalProtocolCode());
				terminalInfoBuilder.setRegularInTerminal(terminal.isRegularInTerminal());
				hashBuilder.setInfo(terminalInfoBuilder);
				builder.addRpHashInfo(hashBuilder);
			}
			_out_packet.setContent(builder.build().toByteArray());
			RPClusters.broadcast(_out_packet);*/
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return 0;
	}

	
}

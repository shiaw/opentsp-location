package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCDataTransfer;

import java.util.List;

@DaRmiNo(id = "0734")
public class Mutual_0734_DataTransfer extends Dacommand {

	@Override
	public Packet processor(Packet packet) {
		try {
			LCDataTransfer.DataTransfer data = LCDataTransfer.DataTransfer.parseFrom(packet.getContent());
			logger.info("收到转存任务");
			//是否新任务
			boolean isNewTask=data.getIsNewTask();
			long beginDate = data.getBeginDate();
			long endDate = data.getEndDate();
			//终端列表
			List<Long> terminalIdList = data.getTerminalIdList();

			//数据转存消息回复平台通用应答（0x1100）
			super.commonResponsesForPlatform(packet.getFrom(),
					packet.getSerialNumber(), packet.getCommand(),
					LCPlatformResponseResult.PlatformResponseResult.success);
//			SaveGpsDataTask task=new SaveGpsDataTask(terminalIdList);
//			try {
//				task.run(isNewTask,beginDate,endDate);
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			return buildPacket(packet);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 构建packet
	 * @param packet
	 * @return
	 */
	private Packet buildPacket(Packet packet) {
		
		Packet _out_packet = new Packet(true);
		_out_packet.setCommand(LCAllCommands.AllCommands.NodeCluster.DataTransferCompletionNotice_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setTo(packet.getFrom());
		_out_packet.setFrom(NodeHelper.getNodeCode());
		
		return _out_packet;
	}

}

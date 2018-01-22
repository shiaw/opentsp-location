package com.navinfo.opentsp.platform.da.core.common.schedule.task;

import java.util.TimerTask;

import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import  com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.common.schedule.ITask;
import com.navinfo.opentsp.platform.da.core.common.schedule.TaskStatus;
import com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMMutualCommandFacotry;

/**
 * 心跳任务<br>
 * 负责所有链路心跳
 *
 * @author lgw
 *
 */
public class HeartbeatTask extends TimerTask implements ITask {

	@Override
	public void setExecuteCycle(long cycle) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getLastExecuteTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TaskStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		try {
			/*//构建心跳数据包
			Packet _out_packet = new Packet(true);
			_out_packet.setCommand(AllCommands.Platform.Heartbeat_VALUE);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setProtocol(LCMessageType.PLATFORM);
			//向MM节点发送心跳数据
			MMMutualCommandFacotry.processor(_out_packet);*/

//			log.info("执行心跳任务");
			//状态心跳包
			Packet _out_packet_status = new Packet(true);
			_out_packet_status.setCommand(AllCommands.NodeCluster.HeartBeatToMM_VALUE);
			_out_packet_status.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet_status.setProtocol(LCConstant.LCMessageType.PLATFORM);
			MMMutualCommandFacotry.processor(_out_packet_status);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

}

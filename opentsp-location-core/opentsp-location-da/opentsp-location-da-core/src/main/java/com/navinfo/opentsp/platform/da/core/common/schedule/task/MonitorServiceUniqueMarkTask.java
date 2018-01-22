package com.navinfo.opentsp.platform.da.core.common.schedule.task;

import java.util.List;
import java.util.TimerTask;

import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerStatus.ServerStatus;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCServerExpiredNotice.ServerExpiredNotice;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.MutualCommandFacotry;
import  com.navinfo.opentsp.platform.da.core.common.schedule.ITask;
import  com.navinfo.opentsp.platform.da.core.common.schedule.TaskStatus;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.entity.ServiceUniqueMark;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.service.IUpperService;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.UpperServiceImp;

public class MonitorServiceUniqueMarkTask extends TimerTask implements ITask {

	IUpperService upperService = new UpperServiceImp();
	private static long SERVEREXPIREDTIME = 5 * 60l;
	public static void main(String[] args) {
		MonitorServiceUniqueMarkTask markTask = new MonitorServiceUniqueMarkTask();
		markTask.run();
	}
	@Override
	public void run() {
		try{
		List<ServiceUniqueMark> serviceUniqueMarks = upperService
				.findAllServiceUniqueMark();
		log.info("执行服务标识（鉴权码）监控任务。");
		for (ServiceUniqueMark mark : serviceUniqueMarks) {
			// 判断断开状态的时间与当前时间的差大于5分钟的删除，并发送通知
			if (mark.getStatus() == ServerStatus.disconnect_VALUE) {
				if (System.currentTimeMillis() / 1000
						- mark.getDisconnectTime() >= SERVEREXPIREDTIME) {
					Packet _out_packet = new Packet(true);
					_out_packet.setCommand(AllCommands.Platform.ServerExpiredNotice_VALUE);
					_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
					ServerExpiredNotice.Builder builder = ServerExpiredNotice.newBuilder();
					if (mark.getType() == 0) {
						// 从删除，直接删除
						upperService.delServiceUniqueMark(mark.getUniqueMark());
						builder.addServerIdentifies(mark.getUniqueMark());
					} else {
						// 主从同时删除
						upperService.delServiceUniqueMark(mark.getUniqueMark());
						builder.setMainServerIdentify(mark.getUniqueMark());
						for (long key : mark.getChildren().keySet()) {
							upperService.delServiceUniqueMark(key);
							builder.addServerIdentifies(mark.getUniqueMark());
						}
					}
					log.info("删除无效的服务标识，并推送给MM");
					_out_packet.setContent(builder.build().toByteArray());
					MutualCommandFacotry.processor(_out_packet);
				}
			}
		}
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
	}

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

}

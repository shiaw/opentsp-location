package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.common.SpringContextUtil;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import  com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalOnlineSwitchInfoSave.TerminalOnlineSwitchInfoSave;
import  com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.TerminalOnOffLineStatus;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.StatisticsStoreService;
import  com.navinfo.opentsp.platform.da.core.persistence.application.StatisticsStoreServiceimpl;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalOnOffStatus;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalOnOffStatusServiceImpl;
import  com.navinfo.opentsp.platform.location.kit.Packet;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 终端上下线状态存储
 * @author jin_s
 *
 */
@DaRmiNo(id = "0984")
public class Mutual_0984_TerminalOnlineSwitchInfoSave extends Dacommand {
	/**
	 * 统计数据实时存储服务
	 */
	private StatisticsStoreService statisticsService;
	/**
	 *  终端上下线状态缓存
	 */
	private TerminalOnOffStatusServiceImpl  redisTerminalOnOffService;

	@Override
	public Packet processor(Packet packet){
		try {
			if(statisticsService == null) {
				statisticsService = (StatisticsStoreService) SpringContextUtil.getBean("StatisticsStoreService");
			}
			if(redisTerminalOnOffService == null) {
				redisTerminalOnOffService = (TerminalOnOffStatusServiceImpl) SpringContextUtil.getBean("TerminalOnOffStatusServiceImpl");
			}
			TerminalOnlineSwitchInfoSave switchInfoSave = TerminalOnlineSwitchInfoSave.parseFrom(packet.getContent());
			//查询redis缓存，获取当前终端的状态信息
			TerminalOnOffStatus terminalOnOffStatus = redisTerminalOnOffService.getTerminalOnOffStatus(switchInfoSave.getTerminalId());
			//终端上报的当前状态
			int status=switchInfoSave.getStatus()==true?1:0;
			//首次上线
			if(null==terminalOnOffStatus){
				terminalOnOffStatus=new TerminalOnOffStatus();
				terminalOnOffStatus.setTerminalId(switchInfoSave.getTerminalId());
				terminalOnOffStatus.setStatus(status);
				terminalOnOffStatus.setStartTime(switchInfoSave.getSwitchDate());
				//添加到缓存
				redisTerminalOnOffService.putData(String.valueOf(switchInfoSave.getTerminalId()), terminalOnOffStatus);
			}else{
				//终端上报的当前状态与缓存中的状态不一致
				if(terminalOnOffStatus.getStatus()!=status){

					TerminalOnOffLineStatus terminalOnOffLineStatus=new TerminalOnOffLineStatus();
					//时间
					terminalOnOffLineStatus.setBeginDate(terminalOnOffStatus.getStartTime());
					//持续时间
					terminalOnOffLineStatus.setContinuousTime((int) (switchInfoSave.getSwitchDate()-terminalOnOffLineStatus.getBeginDate()));
					terminalOnOffLineStatus.setOnlineStatus(terminalOnOffStatus.getStatus());
					terminalOnOffLineStatus.setEndDate(switchInfoSave.getSwitchDate());
					//终端标识
					terminalOnOffLineStatus.setTerminalId(switchInfoSave.getTerminalId());
					//存储数据到mongo
					statisticsService.saveTerminalOnOffLineStatusInfo(switchInfoSave.getTerminalId(), terminalOnOffLineStatus);

					terminalOnOffStatus.setStartTime(switchInfoSave.getSwitchDate());
					terminalOnOffStatus.setStatus(status);
					//更新缓存
					redisTerminalOnOffService.putData(String.valueOf(switchInfoSave.getTerminalId()), terminalOnOffStatus);
				}

			}


			super.commonResponsesForPlatform(packet.getFrom(),
					packet.getSerialNumber(), packet.getCommand(),
					PlatformResponseResult.success);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}

package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp;

import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.Map.Entry;

public abstract class DPCommand extends Command {
	public static Logger logger = LoggerFactory.getLogger(DPCommand.class);


    @Value("${opentsp.command.timeout.threshold:0}")
    protected  int ta_commandTimeoutThreshold;
//	/**
//	 * 向终端接入模块发送数据
//	 *
//	 * @param packet
//	 * @return
//	 */
//	public int forwardTermianl(Packet packet) {
//		MutualCommandFacotry.processor(packet);
//		return 1;
//	}
//	public int writeToTerminal(Packet packet){
//		TerminalInfo terminalInfo = TerminalManage.getInstance().getTerminal(packet.getUniqueMark());
//		packet.setTo(Long.parseLong(packet.getUniqueMark()));
//		if(terminalInfo == null){//终端未入网
//			Packet pk = TerminalResponse.response(0, packet.getTo(), ResponseResult.notRegister, packet.getSerialNumber());
//			return this.write(pk);
//		}
//
//		MutualSession mutualSession = terminalInfo.getMutualSession();
//		if(mutualSession != null){
//			if(mutualSession.getIoSession() != null && !mutualSession.getIoSession().isClosing()){
//				mutualSession.getIoSession().write(packet);
//				return 1;
//			}else{//终端不在线
//				Packet pk = TerminalResponse.response(0, packet.getTo(), ResponseResult.offline, packet.getSerialNumber());
//				this.write(pk);
//				logger.error("终端[ "+packet.getTo()+" ]链路关闭,发送数据失败.");
//				AnswerCommandCache.getInstance().remove("0"+packet.getTo(), packet.getSerialNumber());
//			}
//		}else{//终端不在线
//			Packet pk = TerminalResponse.response(0, packet.getTo(), ResponseResult.offline, packet.getSerialNumber());
//			this.write(pk);
//			logger.error("终端[ "+packet.getTo()+" ]链路未找到,发送数据失败.");
//			AnswerCommandCache.getInstance().remove("0"+packet.getTo(), packet.getSerialNumber());
//		}
//		return 0;
//	}
	

}

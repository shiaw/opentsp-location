package com.navinfo.opentsp.platform.dp.core.acceptor.rp;

import com.google.protobuf.ByteString;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.platform.dp.command.RefreshGroupDpCacheCommand;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.dp.core.common.RmiConstant;
import com.navinfo.opentsp.platform.dp.core.handler.SendKafkaHandler;
import com.navinfo.opentsp.platform.dp.core.rmi.DaRmiService;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTerminalOperateLogSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCDownCommonRes;
import com.navinfo.opentsp.platform.push.DeviceCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public abstract class RPCommand {

	@Autowired
	private DaRmiService daRmiService;

	@Autowired
	private MessageChannel messageChannel;

	@Autowired
	private SendKafkaHandler sendKafkaHandler;

	public static Logger log = LoggerFactory.getLogger(RPCommand.class);
	
	/**
	 * 通用应答
	 * @param type {@link Integer} 1=平台交互通用应答;3=终端上行通用应答;4=终端下行通用应答
	 * @param to {@link Long} 类型为终端=终端标识;类型为平台=节点标识
	 * @param responsesSerialNumber
	 * @param responsesId
	 * @param result
	 * @return
	 */
	public int commonResponses(int type ,long to, int responsesSerialNumber,
			int responsesId, int result){
		/**
		 * 1=平台交互通用应答;3=终端上行通用应答;4=终端下行通用应答
		 */
		switch (type) {
		case 1:
			this.commonResponsesForPlatform(to, responsesSerialNumber, responsesId, result);
			break;
		case 4:
			this.commonResponsesForTerminal(to, responsesSerialNumber, responsesId, result);
			break;
		default:
			break;
		}
		return 1;
	}
	/**
	 * 发送通用应答(终端)
	 * 
	 * @param responsesSerialNumber
	 * @param responsesId
	 * @param result
	 * @return
	 */
	private int commonResponsesForTerminal( long to, 
			                                int responsesSerialNumber,
			                                int responsesId, 
			                                int result) {
		log.error("[DP->RP] 返回通用下行应答. 0x3001");
		Packet packet = new Packet(true);
		LCDownCommonRes.DownCommonRes.Builder builder = LCDownCommonRes.DownCommonRes.newBuilder();
		builder.setSerialNumber(responsesSerialNumber);
		builder.setResponseId(responsesId);
		//主要处理平台处理规则设置、删除指令的应答
		builder.setResult( (LCPlatformResponseResult.PlatformResponseResult.success_VALUE == result) ? LCResponseResult.ResponseResult.success: LCResponseResult.ResponseResult.failure);
		packet.setCommand(LCAllCommands.AllCommands.Terminal.DownCommonRes_VALUE);
		packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		packet.setContent(builder.build().toByteArray());
		packet.setUniqueMark(Convert.terminalIdToUniqueMark(to));
		packet.setFrom(to); //to
		packet.setTo(to);
		return this.write(packet);
	}
	
	/**
	 * 发送通用应答(平台)
	 * @param to
	 * @param responsesSerialNumber
	 * @param responsesId
	 * @param result
	 * @return
	 */
	private int commonResponsesForPlatform( long to, 
			                                int responsesSerialNumber,
			                                int responsesId, 
			                                int result) {
		log.error("[DP->RP] 返回通用应答. 0x1100");
		Packet packet = new Packet(true);
		LCServerCommonRes.ServerCommonRes.Builder builder = LCServerCommonRes.ServerCommonRes.newBuilder();
		builder.setSerialNumber(responsesSerialNumber);
		builder.setResponseId(responsesId);
		builder.setResults(LCPlatformResponseResult.PlatformResponseResult.valueOf(result));

		packet.setCommand(LCAllCommands.AllCommands.Platform.ServerCommonRes_VALUE);
		packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		packet.setContent(builder.build().toByteArray());
		// 将唯一标识设置为当前节点标识
		packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		packet.setFrom(NodeHelper.getNodeCode());
		packet.setTo(to);
		return this.write(packet);
	}
	
	
	
	/**
	 * 向Rp节点发送数据
	 */
	public int write(Packet packet) {
		try {
			sendKafkaHandler.writeKafKaToRP(packet);
		}catch (Exception e) {
			log.error("send to rp error",e);
		}
		return 0;
	}
	
	
	/**
	 * 采用一致性hash向RP节点发送数据
	 * @param packet
	 * @return
	 */
	public int writeForHash(Packet packet){
		//// TODO: 16/9/5
//		Node node = RPClusters.getNode(packet.getFrom());
//		if(node != null){
//			packet.setTo(node.getNodeCode());
//			this.write(packet);
//		}
		return 0;
	}
	
	
	/**
	 * 向TA节点发送数据
	 * @param packet
	 * @return
	 */
	public int writeToTermianl(Packet packet){
		DeviceCommand deviceCommand ;
		String id = null;
		try {
			deviceCommand = new DeviceCommand();
			Map<String,Object> paramMap = new HashMap<>();
			id = packet.getUniqueMark() + "_" + packet.getCommand() + "_" + packet.getSerialNumber();
			paramMap.put("packetContent",Convert.bytesToHexString(packet.getContent()));
			paramMap.put("serialnumber", packet.getSerialNumber());
			deviceCommand.setId(id);
			deviceCommand.setArguments(paramMap);
			deviceCommand.setCommand(packet.getCommandForHex());
			deviceCommand.setQueueName(packet.getQueueName());
			deviceCommand.setDevice(packet.getUniqueMark());
			messageChannel.send(deviceCommand);
			log.error("DP-->TA 下发指令成功!{},id:{},queue:{}",packet.toString(),id,packet.getQueueName());
		}catch (Exception e) {
			log.error("DP-->TA 下发指令错误!{},id:{},queue:{}",packet.toString(),id,packet.getQueueName());
			return 1;
		}
		return 0;
	}
	
	
	/**
	 * 向DA节点发送数据
	 * @param packet
	 * @return
	 */
	public int writeToDataAccess(Packet packet){
		daRmiService.callRmi(RmiConstant.RMI_SERVICE_ID,
				RmiConstant.RMI_INTERFACE_NAME,
				RmiConstant.RmiInterFaceEum.DaRmiInterface.getClassType(),
				packet);
		return 0;
	}

	/**
	 * 终端操作日志
	 * 
	 * @param terminalId
	 * @param operationCMD
	 * @param logContent
	 */
	public void terminalOperateLog(long terminalId,
			int operationCMD, byte[] logContent) {
		Packet packet = new Packet(true);
		packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		packet.setCommand(LCAllCommands.AllCommands.DataAccess.TerminalOperateLogSave_VALUE);
		//TODO: 16/9/5  日志处理
		LCTerminalOperateLogSave.TerminalOperateLogSave.Builder builder = LCTerminalOperateLogSave.TerminalOperateLogSave
				.newBuilder();
		builder.setTerminalId(terminalId);
		builder.setFunctionCode(operationCMD);
		builder.setLogDate(System.currentTimeMillis() / 1000);
		builder.setLogContent(ByteString.copyFrom(logContent));
		builder.setResults(false);
		packet.setContent(builder.build().toByteArray());
		this.writeToDataAccess(packet);
	}
	
	public void CommonResToRP1100(Packet packet){
		//返回1100
		log.info("[DP->RP] 返回通用应答. 0x1100");
		Packet _CommonRes_packet = new Packet(true);
		LCServerCommonRes.ServerCommonRes.Builder _CommonRes_packetbuilder = LCServerCommonRes.ServerCommonRes.newBuilder();
		_CommonRes_packetbuilder.setSerialNumber(packet.getSerialNumber());
		_CommonRes_packetbuilder.setResponseId(LCAllCommands.AllCommands.Terminal.MessageBroadcastInArea_VALUE);
//		_CommonRes_packetbuilder.setResults(PlatformResponseResult.valueOf(result));
		_CommonRes_packetbuilder.setResults(LCPlatformResponseResult.PlatformResponseResult.success);


		_CommonRes_packet.setCommand(LCAllCommands.AllCommands.Platform.ServerCommonRes_VALUE);
		_CommonRes_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_CommonRes_packet.setContent(_CommonRes_packetbuilder.build().toByteArray());
		// 将唯一标识设置为当前节点标识
		_CommonRes_packet.setSerialNumber(packet.getSerialNumber());
		_CommonRes_packet.setUniqueMark(packet.getUniqueMark());
		_CommonRes_packet.setFrom(NodeHelper.getNodeCode());
		_CommonRes_packet.setTo(100310);
		write(_CommonRes_packet);
	}

	/**
	 * 包指令处理
	 * @param packet
	 * @return
     */
	public abstract int processor(Packet packet);

	/**
	 * 组播刷新缓存
	 * @param packet
	 * @param messageChannel
     */
	public void sendRefreshGroupDpCacheCommand(Packet packet , MessageChannel messageChannel ,String queueName) {
		try {
			RefreshGroupDpCacheCommand refreshGroupDpCacheCommand = new RefreshGroupDpCacheCommand();
			//组播刷新缓存,设置DP写DA为false
			packet.setDpProcess(false);
			//设置当前服务队列名称,组播时做判断是否执行
			packet.setQueueName(queueName);
			refreshGroupDpCacheCommand.setPacket(packet);
			messageChannel.send(refreshGroupDpCacheCommand);
		}catch (Exception e) {
			log.error("组播刷新缓存失败 !",e);
		}
	}
}

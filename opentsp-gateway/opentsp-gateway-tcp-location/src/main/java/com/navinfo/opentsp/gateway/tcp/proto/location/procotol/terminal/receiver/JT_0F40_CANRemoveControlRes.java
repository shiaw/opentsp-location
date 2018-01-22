package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalProtoVersionCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;

import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTerminalStatusControlRes;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 江淮终端和防拆盒 0F40临时兼容处理
 * @author Administrator
 *
 */
@LocationCommand(id = "0F40")
public class JT_0F40_CANRemoveControlRes extends TerminalCommand {


	@Autowired
	private TerminalProtoVersionCache terminalProtoVersionCache;

	static Map<Integer, ResponseResult> ResponseResultMapping = new ConcurrentHashMap<Integer, ResponseResult>();
	static{
		ResponseResultMapping.put(0, ResponseResult.success);
		ResponseResultMapping.put(1, ResponseResult.failure);
		ResponseResultMapping.put(2, ResponseResult.messageError);
		ResponseResultMapping.put(3, ResponseResult.notSupport);
		ResponseResultMapping.put(4, ResponseResult.alarmHandle);
		ResponseResultMapping.put(5, ResponseResult.offline);
		ResponseResultMapping.put(6, ResponseResult.notRegister);
		ResponseResultMapping.put(7, ResponseResult.ecuSuccess);
		ResponseResultMapping.put(8, ResponseResult.ecuFailure);
		ResponseResultMapping.put(9, ResponseResult.ecuNotSupport);
		ResponseResultMapping.put(10, ResponseResult.typeError);
		ResponseResultMapping.put(11, ResponseResult.limitError);
		ResponseResultMapping.put(12, ResponseResult.vehicleClose);
		ResponseResultMapping.put(13, ResponseResult.communicateError);

	}
	public static void main(String[] args) {
		Packet p = new Packet(6);
		p.appendContent(Convert.hexStringToBytes("FBB90001"));
		new JT_0F40_CANRemoveControlRes().processor(null,p);
	}

	@Override 
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		String protocolType=terminalProtoVersionCache.get(packet.getUniqueMark());
		LCTerminalStatusControlRes.TerminalStatusControlRes.Builder builder = LCTerminalStatusControlRes.TerminalStatusControlRes.newBuilder();
		byte[] content = packet.getContent();
		int responsesSerialNumber = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 2), 2);//应答流水号
		builder.setSerialNumber(responsesSerialNumber);
		int result = Convert.byte2Int(ArraysUtils.subarrays(content, 2, 1), 1);//结果 2-3字节
		if(result==4){ //F9 结果 ： 4 ECU成功
			builder.setResult(ResponseResultMapping.get(7));
		}else if(result==5){ //5 ECU失败
			builder.setResult(ResponseResultMapping.get(8));
		}else if(result==6){ //6 限制类型无效
			builder.setResult(ResponseResultMapping.get(11));
		}else if(result==7){ //7 指令解析错误
			builder.setResult(ResponseResultMapping.get(2));
		}else if(result==8){ //8操作类型无效
			builder.setResult(ResponseResultMapping.get(10));
		}else if(result==9){ //9车辆关机
			builder.setResult(ResponseResultMapping.get(12));
		}else if(result==10){ //10总线通讯异常
			builder.setResult(ResponseResultMapping.get(13));
		}else if(result == 3){
			if("200130".equals(protocolType)){
				builder.setResult(ResponseResultMapping.get(9));
			}else{
				builder.setResult(ResponseResultMapping.get(3));
			}
		}else{
			builder.setResult(ResponseResultMapping.get(result));
		}
		if("200110".equals(protocolType)){ //大运，江淮 有为终端
			if(content.length == 4){
				int types = Convert.byte2Int(ArraysUtils.subarrays(content, 3, 1), 1);//1：激活；2：关闭 3：锁车；4：解锁
				builder.setTypes(types);
			}else{
				int controlType = Convert.byte2Int(ArraysUtils.subarrays(content, 3, 1), 1);//1：潍柴CAN总线实现 2：终端自身实现(OBD)
				int types = Convert.byte2Int(ArraysUtils.subarrays(content, 4, 1), 1);//1：激活；2：关闭 3：锁车；4：解锁
				builder.setControlType(controlType);
				builder.setTypes(types);
			}
		}else if("200130".equals(protocolType)){ //F9 防拆盒
			int controlType = Convert.byte2Int(ArraysUtils.subarrays(content, 3, 1), 1);//1：潍柴CAN总线实现 2：终端自身实现(OBD)
			int types = Convert.byte2Int(ArraysUtils.subarrays(content, 4, 1), 1);//1：激活；2：关闭 3：锁车；4：解锁
			builder.setControlType(controlType);
			builder.setTypes(types);
		}
		/*if(content.length == 4) {//防拆盒
			int types = Convert.byte2Int(ArraysUtils.subarrays(content, 3, 1), 1);//1：激活；2：关闭 3：锁车；4：解锁
			builder.setTypes(types);
			log.info("UniqueMark:[" + packet.getUniqueMark()
					+ "] responsesSerialNumber:[" + responsesSerialNumber
					+ "],result[" + result + "], " +
					"types[" + types + "].");
		}else if(content.length == 5) {//有为的终端
			int controlType = Convert.byte2Int(ArraysUtils.subarrays(content, 3, 1), 1);//1：潍柴CAN总线实现 2：终端自身实现(OBD)
			int types = Convert.byte2Int(ArraysUtils.subarrays(content, 4, 1), 1);//1：激活；2：关闭 3：锁车；4：解锁
			builder.setControlType(controlType);
			builder.setTypes(types);

			log.info("UniqueMark:[" + packet.getUniqueMark()
					+ "] responsesSerialNumber:[" + responsesSerialNumber
					+ "],result[" + result + "], " +
							"controlType[" + controlType+"], " +
					"types[" + types + "].");
		}*/

		Packet _out_packet = new Packet();
		_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.TerminalStatusControlRes_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());
		super.sendCommandStatus(packet.getUniqueMark(),responsesSerialNumber,_out_packet);
		super.writeKafKaToDP(_out_packet, TopicConstants.POSRAW);
		return null;
	}
}

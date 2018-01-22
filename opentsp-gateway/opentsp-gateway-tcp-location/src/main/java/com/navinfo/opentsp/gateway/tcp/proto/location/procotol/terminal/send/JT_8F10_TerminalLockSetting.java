package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTerminalStatusControl;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTerminalStatusControlRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTerminalStatusControlRes.TerminalStatusControlRes;

import java.util.List;
//@LocationCommand(id = "8F10")
public class JT_8F10_TerminalLockSetting {

	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		PacketResult packetResult=new PacketResult();
			try {
			LCTerminalStatusControl.TerminalStatusControl  statusControl = LCTerminalStatusControl.TerminalStatusControl.parseFrom(packet.getContent());
			Packet outPacket = null;
			int controls = statusControl.getControls().getNumber();// 控制标识
			List<Integer> paras = statusControl.getParasList();
			switch (controls) {
				case 0x01:// 加锁标识，参数值：1锁车，0解锁
					byte[] vehicleLockData = {};
					if(paras.get(0)==1){//1锁车
						vehicleLockData = ArraysUtils.arraycopy(vehicleLockData,Convert.intTobytes(1,1));//锁车
					}else if(paras.get(0)==0) {//2解锁
						vehicleLockData = ArraysUtils.arraycopy(vehicleLockData, Convert.intTobytes(2,1));//解锁
					}
					vehicleLockData = ArraysUtils.arraycopy(vehicleLockData,Convert.intTobytes(paras.get(3),1));//1.限制转速锁车  2.扭矩限制锁车 3.喷油限制锁车 4:其他锁车方式(可扩展)
					
					vehicleLockData = ArraysUtils.arraycopy(vehicleLockData,Convert.intTobytes(0,2)); //锁车时间，默认0:立即锁车
					vehicleLockData = ArraysUtils.arraycopy(vehicleLockData,Convert.intTobytes(0,2));//锁车状态上报间隔；0：不主动发送
					vehicleLockData = ArraysUtils.arraycopy(vehicleLockData,Convert.intTobytes(paras.get(1),2));//限制参数
					//answerCommandCacheAddEntry(packet,Constant.JTProtocol.CANRemoveControl);
					outPacket = buildPacket(10, Constant.JTProtocol.TerminalLockSetting,packet,vehicleLockData);
					break;
				default:
					break;	
			}
			//int result = this.write(outPacket);
				packetResult.setTerminalPacket(outPacket);
			//特殊处理：华菱终端在下发成功之后，应答一条0F10的应答。
			//if(result == 1){
				TerminalStatusControlRes.Builder builder = TerminalStatusControlRes.newBuilder();
				builder.setSerialNumber(packet.getSerialNumber());//应答流水号
				if(paras.get(0)==1){
					builder.setTypes(3);//操作类型3：锁车  4：解锁
				}else if(paras.get(0)==0){
					builder.setTypes(4);//操作类型3：锁车  4：解锁
				}
				//int limitType = Convert.byte2Int(ArraysUtils.subarrays(content, 3, 1), 1);//限制类型
				builder.setControlType(1);
				builder.setResult(LCResponseResult.ResponseResult.messageError);
				
				Packet _out_packet = new Packet();
				_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.TerminalStatusControlRes_VALUE);
				_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
				_out_packet.setUniqueMark(packet.getUniqueMark());
				_out_packet.setSerialNumber(packet.getSerialNumber());
				_out_packet.setContent(builder.build().toByteArray());
			//	super.writeToDataProcessing(_out_packet);
			//}
				packetResult.setKafkaPacket(_out_packet);
				return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
	private Packet buildPacket(int capacity, int jTProtocol,Packet packet,byte[] data) {
		Packet outPacket = new Packet(capacity);
		outPacket.setCommand(jTProtocol);
		outPacket.setSerialNumber(packet.getSerialNumber());
		outPacket.setUniqueMark(packet.getUniqueMark());
		outPacket.setTo(Long.parseLong(packet.getUniqueMark()));
		outPacket.appendContent(data);
		return outPacket;
	}
}

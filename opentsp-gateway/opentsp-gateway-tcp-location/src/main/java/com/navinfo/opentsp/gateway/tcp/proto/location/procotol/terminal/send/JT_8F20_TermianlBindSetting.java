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
//@LocationCommand(id = "8F20")
public class JT_8F20_TermianlBindSetting {

	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		try {
			PacketResult packetResult=new PacketResult();
			LCTerminalStatusControl.TerminalStatusControl  statusControl = LCTerminalStatusControl.TerminalStatusControl.parseFrom(packet.getContent());
			Packet outPacket = null;
			int controls = statusControl.getControls().getNumber();// 控制标识
			List<Integer> paras = statusControl.getParasList();
			int type=0;
			switch (controls) {
				case 0x03:// 防拆开启或关闭
					byte[] removeData = {}; 
					if(paras.get(0).intValue()==0x15F4){//0x15F4 (绑定)
						type = 1;//激活
						removeData = ArraysUtils.arraycopy(removeData,Convert.intTobytes(1,1));//子命令
						removeData = ArraysUtils.arraycopy(removeData, Convert.hexStringToBytes("15F4"));
					}
					else if(paras.get(0).intValue()==0x4F51){//0x4F51 (解绑) 
						type = 2;//关闭
						removeData = ArraysUtils.arraycopy(removeData,Convert.intTobytes(2,1));//子命令
						removeData = ArraysUtils.arraycopy(removeData,Convert.hexStringToBytes("4F51"));
					}
					//answerCommandCacheAddEntry(packet,Constant.JTProtocol.CANRemoveControl);
					outPacket = buildPacket(10, Constant.JTProtocol.TerminalBindSetting,packet,removeData);
					break;
				default:
					break;	
			}
			//int result = this.write(outPacket);
			packetResult.setTerminalPacket(outPacket);

			//特殊处理：华菱终端在下发成功之后，应答一条0F20的应答。
			//if(result == 1){
				TerminalStatusControlRes.Builder builder = TerminalStatusControlRes.newBuilder();
				builder.setSerialNumber(packet.getSerialNumber());//应答流水号
				builder.setTypes(type);//操作类型1：激活  2：关闭
				builder.setControlType(1);
				builder.setResult(LCResponseResult.ResponseResult.messageError);
				
				Packet _out_packet = new Packet();
				_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.TerminalStatusControlRes_VALUE);
				_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
				_out_packet.setUniqueMark(packet.getUniqueMark());
				_out_packet.setSerialNumber(packet.getSerialNumber());
				_out_packet.setContent(builder.build().toByteArray());
				//super.writeToDataProcessing(_out_packet);
		//	}
			packetResult.setKafkaPacket(_out_packet);
			return  packetResult;
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

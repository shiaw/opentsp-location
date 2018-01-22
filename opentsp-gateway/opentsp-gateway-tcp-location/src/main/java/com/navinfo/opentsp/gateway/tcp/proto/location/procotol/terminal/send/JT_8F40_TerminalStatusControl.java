package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTerminalStatusControl;

import java.util.List;
//@LocationCommand(id = "8F40")
public class JT_8F40_TerminalStatusControl  {

	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		try {
			LCTerminalStatusControl.TerminalStatusControl  statusControl = LCTerminalStatusControl.TerminalStatusControl.parseFrom(packet.getContent());
			Packet outPacket = null;
			int controls = statusControl.getControls().getNumber();// 控制标识
			int controlType=statusControl.getControlType();	//1：潍柴CAN总线实现; 2：终端自身实现
			List<Integer> paras = statusControl.getParasList();
				switch (controls) {
				case 0x01:// 加锁标识，参数值：1锁车，0解锁
					byte[] vehicleLockData = {};
					vehicleLockData = ArraysUtils.arraycopy(vehicleLockData, Convert.intTobytes(controlType, 1));//1
					if(paras.get(0)==1){//1锁车
						vehicleLockData = ArraysUtils.arraycopy(vehicleLockData,Convert.intTobytes(3,1));//子命令	
					}else if(paras.get(0)==0) {//0解锁
						vehicleLockData = ArraysUtils.arraycopy(vehicleLockData,Convert.intTobytes(4,1));//子命令
					}
					if(paras.get(3) == 1){
						if(paras.get(1) == 0 || paras.get(1) > 100){
							//1.为了兼容之前程序版本，定义锁车指令如下：大于100或等于0:转速=0 时不能启动，其它转速为允许的最高转速，如果限速值小于怠速值，则默认为怠速值
							//2.若该字段为0或大于100，则为转速限制值，锁车参数值为0
							vehicleLockData = ArraysUtils.arraycopy(vehicleLockData,Convert.intTobytes(paras.get(1),2));
						}else{
							vehicleLockData = ArraysUtils.arraycopy(vehicleLockData,Convert.intTobytes(paras.get(1),2));
						}
					}else{
						vehicleLockData = ArraysUtils.arraycopy(vehicleLockData,Convert.intTobytes(paras.get(3),2));//限制类型
					}
					
					vehicleLockData = ArraysUtils.arraycopy(vehicleLockData,Convert.hexStringToBytes("FFFFFF"));
					vehicleLockData = ArraysUtils.arraycopy(vehicleLockData,Convert.intTobytes(paras.get(2),3));//GPSID
					if(paras.get(3) != 1){
						vehicleLockData = ArraysUtils.arraycopy(vehicleLockData, Convert.intTobytes(paras.get(1),2));  //锁车参数
					}else{
						if(packet.getProtocol() != 200110){
							vehicleLockData = ArraysUtils.arraycopy(vehicleLockData, Convert.intTobytes(0,2));
						}
					}
					
					//answerCommandCacheAddEntry(packet,Constant.JTProtocol.CANRemoveControl);
					outPacket = buildPacket(12, Constant.JTProtocol.CANRemoveControl,packet,vehicleLockData);// int jTProtocol,Packet packet,byte[] data) {
					break;
				case 0x02: // 车辆限速 该协议已废除
//					byte[] limitSpeedData = {}; 
//					limitSpeedData = ArraysUtils.arraycopy(limitSpeedData,Convert.hexStringToBytes("0D"));	
//					limitSpeedData = ArraysUtils.arraycopy(limitSpeedData, Convert.intTobytes(2, 1));
//					limitSpeedData = ArraysUtils.arraycopy(limitSpeedData, Convert.intTobytes(paras.get(0), 2));
//					answerCommandCacheAddEntry(packet,Constant.JTProtocol.TerminalStatusControl);
//					outPacket = buildPacket(4,Constant.JTProtocol.TerminalStatusControl,packet,limitSpeedData);
					break;
				case 0x03:// 防拆开启或关闭
					byte[] removeData = {}; 
					removeData = ArraysUtils.arraycopy(removeData,Convert.intTobytes(controlType, 1));//1
					//GPS 功能激活\关闭,按照需求设置，需保密
					/*if(paras.get(0).intValue()==0){
						removeData = ArraysUtils.arraycopy(removeData,Convert.intTobytes(1,1));//子命令
						removeData = ArraysUtils.arraycopy(removeData,Convert.hexStringToBytes("15F4"));
					}
					else if(paras.get(0).intValue()==1){
						removeData = ArraysUtils.arraycopy(removeData,Convert.intTobytes(2,1));//子命令
						removeData = ArraysUtils.arraycopy(removeData,Convert.hexStringToBytes("4F51"));
					}*/
					if(paras.get(0).intValue()==0x15F4){//0x15F4 激活
						removeData = ArraysUtils.arraycopy(removeData,Convert.intTobytes(1,1));//子命令
						removeData = ArraysUtils.arraycopy(removeData,Convert.hexStringToBytes("15F4"));
					}
					else if(paras.get(0).intValue()==0x4F51){//0x4F51关闭 
						removeData = ArraysUtils.arraycopy(removeData,Convert.intTobytes(2,1));//子命令
						removeData = ArraysUtils.arraycopy(removeData,Convert.hexStringToBytes("4F51"));
					}
					
					removeData = ArraysUtils.arraycopy(removeData, Convert.intTobytes(paras.get(1), 3));//GPSID
					removeData = ArraysUtils.arraycopy(removeData, Convert.intTobytes(paras.get(2), 3));//固定密钥
					//answerCommandCacheAddEntry(packet,Constant.JTProtocol.CANRemoveControl);
					outPacket = buildPacket(10,Constant.JTProtocol.CANRemoveControl,packet,removeData);
					break;
				default:
					break;	
			}
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outPacket);
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
	/**
	 *  缓存指令信息 
	 * @param jTProtocol  协议号
	 */
	private void answerCommandCacheAddEntry(Packet packet ,int jTProtocol) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry
.setInternalCommand(LCAllCommands.AllCommands.Terminal.TerminalStatusControl_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(jTProtocol);
		answerEntry.setTimeout(60*1000);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(),
				packet.getSerialNumber(), answerEntry);
	}
}

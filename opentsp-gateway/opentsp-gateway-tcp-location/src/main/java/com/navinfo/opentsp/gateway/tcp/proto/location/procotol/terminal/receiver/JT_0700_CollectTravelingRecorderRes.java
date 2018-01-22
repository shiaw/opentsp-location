package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands.Terminal;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCDownCommonRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JT_0700_CollectTravelingRecorderRes extends TerminalCommand {
	final static byte ERROR_CMD = (byte)0xFB;
	static Map<String, List<Packet>> cache = new ConcurrentHashMap<String, List<Packet>>();
	static Map<String, Integer> cmdMapping = new HashMap<String ,Integer>();
	
	
	static{
		cmdMapping.put("0", Terminal.TRVersionCollect_VALUE);
		cmdMapping.put("1", Terminal.TRCollectDriverInfo_VALUE);
		cmdMapping.put("2", Terminal.TRCollectRealTime_VALUE);
		cmdMapping.put("3", Terminal.TRCollectMileage_VALUE);
		cmdMapping.put("4", Terminal.TRPulseCollection_VALUE);
		cmdMapping.put("5", Terminal.TRCollectVehicleInfo_VALUE);
		cmdMapping.put("6", Terminal.TRCollectStatusSignal_VALUE);
		cmdMapping.put("7", Terminal.TRCollectOnlyCode_VALUE);
		cmdMapping.put("8", Terminal.TRSpeedCollect_VALUE);
		cmdMapping.put("9", Terminal.TRCollectLocationData_VALUE);
		cmdMapping.put("10", Terminal.TRDoubtCollect_VALUE);
		cmdMapping.put("11", Terminal.TRCollectOvertimeRecord_VALUE);
		cmdMapping.put("12", Terminal.TRCollectDriverIdentity_VALUE);
		cmdMapping.put("13", Terminal.TRCollectPowerRecord_VALUE);
		cmdMapping.put("14", Terminal.TRCollectParaModifyRecord_VALUE);
		cmdMapping.put("15", Terminal.TRCollectSpeedLog_VALUE);
		cmdMapping.put("82", Terminal.TRSetVehicleInfo_VALUE);
		cmdMapping.put("83", Terminal.TRSetInstallDate_VALUE);
		cmdMapping.put("84", Terminal.TRSetStatusSignal_VALUE);
		cmdMapping.put("C2", Terminal.TRSetCurrentDate_VALUE);
		cmdMapping.put("C3", Terminal.TRSetPulseValue_VALUE);
		cmdMapping.put("C4", Terminal.TRSetMileage_VALUE);
	}
	
	
	
	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		byte[] content  = null;
		super.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(), LCResultCode.JTTerminal.SUCCESS );
		
		if(packet.getPacketTotal() > 1){
			if(packet.getPacketSerial() == 1){
				byte[] tempContent = packet.getContent();
				int cmd = Convert.byte2Int(ArraysUtils.subarrays(tempContent, 2, 1), 1);
				
				//
				LCDownCommonRes.DownCommonRes.Builder builder = LCDownCommonRes.DownCommonRes.newBuilder();
				int answerSerialNumber = Convert.byte2Int(ArraysUtils.subarrays(tempContent, 0, 2), 2);
				builder.setSerialNumber(answerSerialNumber);
				builder.setResponseId(cmdMapping.get(String.valueOf(cmd)));
				
				builder.setResult(LCResponseResult.ResponseResult.success);
								
//				//转给上层业务系统，交给DP处理（0x3001）
				Packet outpacket = new Packet();
				outpacket.setCommand(Terminal.DownCommonRes_VALUE);
				outpacket.setSerialNumber(packet.getSerialNumber());
				outpacket.setUniqueMark(packet.getUniqueMark());
				outpacket.setContent(builder.build().toByteArray());
			//	super.writeToDataProcessing(outpacket);
                super.writeKafKaToDP(outpacket, TopicConstants.POSRAW);
                return null;
			}
			
			//组包
			List<Packet> packets = cache.get(packet.getUniqueMark());
			if(packets == null || packet.getSerialNumber() == 1){
				packets = new ArrayList<Packet>();
				cache.put(packet.getUniqueMark(), packets);
			}
			if(packet.getPacketSerial() < packet.getPacketTotal()){
				packets.add(packet);
				return null;//-1;
			} else {
				packets.add(packet);
				for (Packet packet2 : packets) {
					content = ArraysUtils.arraycopy(content, packet2.getContent());
				}
				//
				cache.remove(packet.getUniqueMark());
				packet.setContent(content);
			}
		} else {
			content = packet.getContent();
		}
		
		int cmd = Convert.byte2Int(ArraysUtils.subarrays(content, 2, 1), 1);
		if( cmd == ERROR_CMD){
			log.error("采集错误");
		}
		
		//
	//	TRFactory.processor(cmd, 2003, packet);

		return null;//0;
	}
}

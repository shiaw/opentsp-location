package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.jt2013;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver.JT_0104_ParameterQueryRes;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCParameterMessageNumber;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCQueryAppointPara;


import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
* 查询指定终端参数
* @author jin_s
*
*/
@LocationCommand(id = "2316")
public class DP_2316_QueryAppointPara extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.QueryAppointPara_VALUE);// ************
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.QueryAppointTerminalParameters);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			LCQueryAppointPara.QueryAppointPara queryAppointPara = LCQueryAppointPara.QueryAppointPara.parseFrom(packet.getContent());
			 List<LCParameterMessageNumber.ParameterMessageNumber> paraIdentifyList = queryAppointPara.getNumbersList();

			BytesArray bytesArray = new BytesArray();
			int paramNum=0;
			BytesArray bytesArrayParam = new BytesArray();
	        if(paraIdentifyList!=null&&paraIdentifyList.size()>0){



			  for( LCParameterMessageNumber.ParameterMessageNumber parameterMessageNumber:paraIdentifyList){

				  int messageNumber = parameterMessageNumber.getNumber();
				  Map<String, Integer> messageNumberMap = JT_0104_ParameterQueryRes.paramterInternlKeyMap.get(messageNumber);
				  if(messageNumberMap!=null&&!messageNumberMap.isEmpty()){
					  Collection<Integer> values = messageNumberMap.values();
					  paramNum+=values.size();
					  for (int parameterMessage:values){
						  bytesArrayParam.append(Convert.longTobytes(parameterMessage, 4));
					  }
				  }

			  }
		     }
	        bytesArray.append(Convert.intTobytes(paramNum, 1));
	        bytesArray.append(bytesArrayParam.get());
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.QueryAppointTerminalParameters);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}

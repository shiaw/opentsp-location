package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.terminalsetting;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCTakePictureControl;


import java.util.List;
@LocationCommand(id = "2265")
public class DP_2265_TakePictureControl extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.TakePictureControl_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
			try {
				LCTakePictureControl.TakePictureControl takePictureControl= LCTakePictureControl.TakePictureControl.parseFrom(packet.getContent());
				List<LCTakePictureControl.PassageStatus> passageStatusList=takePictureControl.getPassageStatusList();
		
				int[] status=new int[32];
				for (LCTakePictureControl.PassageStatus passageStatus : passageStatusList) {
					if(passageStatus.getPassageId() == 0){
						status[31]=passageStatus.getIsPhotoing()? 1 : 0;
						status[23]=passageStatus.getIsSaving()? 0 : 1;
					}else if(passageStatus.getPassageId() == 1){
						status[30]=passageStatus.getIsPhotoing()? 1 : 0;
						status[22]=passageStatus.getIsSaving()? 0 : 1;
					}else if(passageStatus.getPassageId() == 2){
						status[29]=passageStatus.getIsPhotoing()? 1 : 0;
						status[21]=passageStatus.getIsSaving()? 0 : 1;
					}else if(passageStatus.getPassageId() == 3){
						status[28]=passageStatus.getIsPhotoing()? 1 : 0;
						status[20]=passageStatus.getIsSaving()? 0 : 1;
					}else if(passageStatus.getPassageId() == 4){
						status[27]=passageStatus.getIsPhotoing()? 1 : 0;
						status[19]=passageStatus.getIsSaving()? 0: 1;
					}
				}
				
				int unit = takePictureControl.getUnit().getNumber();
				if(unit%2==1){
					status[15]	=0;
				}else{
					status[15]	=1;
				}
				String twoString = Integer.toBinaryString(takePictureControl.getTimingInterval());
				for (int i = 0; i < twoString.length(); i++) {
					status[15-twoString.length()+i]=Integer.parseInt(twoString.charAt(i)+"");
					
				}
			
				String str="";
				for(int j=0;j<status.length;j++){
				    str+=String.valueOf(status[j]);
				}
				System.err.println("-------------->str:"+str);
				BytesArray bytesArray = new BytesArray();
				bytesArray.append(Convert.intTobytes(1, 1));
				//true 定时  false 定距、
				if(takePictureControl.getStatus()){
					bytesArray.append(Convert.intTobytes(0x64, 4));
				}else{
					bytesArray.append(Convert.intTobytes(0x65, 4));
				}
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(binaryStringToLong(str), 4));
			
				//byte[] content = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
				Packet outpacket = new Packet();
				outpacket.setCommand(Constant.JTProtocol.TerminalSetting);
				outpacket.setUniqueMark(packet.getUniqueMark());
				outpacket.setSerialNumber(packet.getSerialNumber());
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
public static void main(String[] args) {
	String x="00000000001001100110010000000000";
	long s=0;
	for(int i=0;i<x.length();i++){
		int c=Integer.parseInt( (x.charAt(x.length()-i-1))+"");
		 int num=(int) Math.pow(2, i);
		 s=s|(num*c);
		 System.err.println(s);
	}
	System.err.println(s);
	//Convert.longTobytes(Long.parseLong(""), 4);
}

public long binaryStringToLong(String binaryStrig){
	long s=0;
	for(int i=0;i<binaryStrig.length();i++){
		int c=Integer.parseInt( (binaryStrig.charAt(binaryStrig.length()-i-1))+"");
		 int num=(int) Math.pow(2, i);
		 s=s|(num*c);
	}
	return s;
}
}

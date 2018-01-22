package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;


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
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxSetting;
@LocationCommand(id = "2271")
public class DP_2271_AntiTamperBoxSetting extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.AntiTamperBoxSetting_VALUE);//2271
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);//8103
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		
		try {
			LCAntiTamperBoxSetting.AntiTamperBoxSetting antiTamperBoxSetting = LCAntiTamperBoxSetting.AntiTamperBoxSetting.parseFrom(packet.getContent());
		
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.intTobytes(2, 1));//2个
			

			bytesArray.append(Convert.intTobytes(0XF003, 4));//锁车参数设置定义
			bytesArray.append(Convert.intTobytes(1, 1));
			bytesArray.append(Convert.intTobytes(antiTamperBoxSetting.getLockVehiclePar(),1));

			bytesArray.append(Convert.intTobytes(0XF00D, 4)); //设置北斗ID号，定义见部标808表7中终端ID
			bytesArray.append(Convert.intTobytes(7, 1));
			bytesArray.append(assembleByteArray(antiTamperBoxSetting.getDeviceId().getBytes(),7));
			
			
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.TerminalSetting);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());

			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	
	/**
	 * @param src 
	 * @return 7 个字节，由大写字母和数字组成，此终端ID 由制造商自行定义，位数不足时，后补“0X00”。
	 */
	private static byte[] assembleByteArray(byte[] src,int length) {
		if(src.length < length) {
			byte[] reArr = new byte[length];
			System.arraycopy(src, 0, reArr, 0, src.length);
			byte[] b = new byte[length - src.length];
			for(int i = 0;i<length - src.length;i++) {
				b[i] = 0x00;
			}
			System.arraycopy(b, 0, reArr, src.length, b.length);
			return reArr; 
		}
		return src;
	}
}

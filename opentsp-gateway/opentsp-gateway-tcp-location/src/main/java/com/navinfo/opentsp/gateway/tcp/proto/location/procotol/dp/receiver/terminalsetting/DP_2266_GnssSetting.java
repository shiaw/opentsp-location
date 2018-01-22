package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.terminalsetting;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCGnssSetting;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCGnssSetting.LocationMode;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCGnssSetting.UpLoadSetting;

import java.util.List;
@LocationCommand(id = "2266")
public class DP_2266_GnssSetting  extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.GnssSetting_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
			try {
				LCGnssSetting.GnssSetting gnssSetting= LCGnssSetting.GnssSetting.parseFrom(packet.getContent());
				//定位模式
			    LocationMode locationMode=gnssSetting.getLocationMode();
				//波特率
				LCGnssSetting.BaudRate baudRate=gnssSetting.getBaudRate();
				//定位数据输出频率，单位秒
				LCGnssSetting.LocationRate locationRate=gnssSetting.getLocationRate();
				//定位数据上传设置
				List<UpLoadSetting> upLoadSettingList=gnssSetting.getUploadSettingList();
				BytesArray bytesArray = new BytesArray();
				int pnumber = 0;
				if(gnssSetting.hasLocationMode()){
					bytesArray.append(Convert.longTobytes(0x90, 4));
					bytesArray.append(Convert.longTobytes(2, 1));
					bytesArray.append(getlocationModeByByte(locationMode));
					pnumber++;
				}
				if(gnssSetting.hasBaudRate()){
					bytesArray.append(Convert.longTobytes(0x91, 4));
					bytesArray.append(Convert.longTobytes(2, 1));
					bytesArray.append(Convert.intTobytes(baudRate.getNumber(), 2));
					pnumber++;
				}
				if(gnssSetting.hasLocationRate()){
					bytesArray.append(Convert.longTobytes(0x92, 4));
					bytesArray.append(Convert.longTobytes(2, 1));
					bytesArray.append(Convert.intTobytes(locationRate.getNumber(), 2));
					pnumber++;
				}
				if(gnssSetting.hasCollectRate()){
					bytesArray.append(Convert.longTobytes(0x93, 4));
					bytesArray.append(Convert.longTobytes(4, 1));
					bytesArray.append(Convert.intTobytes(gnssSetting.getCollectRate(), 4));
					pnumber++;
				}
				for (UpLoadSetting upLoadSetting : upLoadSettingList) {
						if(upLoadSetting.hasModes()){
							bytesArray.append(Convert.longTobytes(0x94, 4));
							bytesArray.append(Convert.longTobytes(2, 1));
							bytesArray.append(Convert.intTobytes(upLoadSetting.getModes().getNumber(), 2));
							pnumber++;
						}
						if(upLoadSetting.hasUploadInterval()){
							bytesArray.append(Convert.longTobytes(0x95, 4));
							bytesArray.append(Convert.longTobytes(4, 1));
							bytesArray.append(Convert.intTobytes(gnssSetting.getCollectRate(), 4));
							pnumber++;
						}
				}
				byte[] bytes = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
				Packet outpacket = new Packet();
				outpacket.setCommand(Constant.JTProtocol.TerminalSetting);
				outpacket.setUniqueMark(packet.getUniqueMark());
				outpacket.setSerialNumber(packet.getSerialNumber());
				outpacket.setContent(bytes);
				//return super.writeToTerminal(outpacket);
				PacketResult packetResult=new PacketResult();
				packetResult.setTerminalPacket(outpacket);
				return packetResult;
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
		
			return null;
	
	}
	/**
	 * 定位模式
	 * @param locationMode
	 * @return
	 */
	private byte[] getlocationModeByByte(LocationMode locationMode){
		String  locationModeString="";
		if(locationMode.getIsGps()){
			locationModeString+=1;
		}else {
			locationModeString+=0;
		}
		if(locationMode.getIsCompass()){
			locationModeString+=1;
		}else {
			locationModeString+=0;
		}
		if(locationMode.getIsGlonass()){
			locationModeString+=1;
		}else {
			locationModeString+=0;
		}
		if(locationMode.getIsGalileo()){
			locationModeString+=1;
		}else {
			locationModeString+=0;
		}
		return Convert.intTobytes(Integer.valueOf(locationModeString), 2);
	} 

}

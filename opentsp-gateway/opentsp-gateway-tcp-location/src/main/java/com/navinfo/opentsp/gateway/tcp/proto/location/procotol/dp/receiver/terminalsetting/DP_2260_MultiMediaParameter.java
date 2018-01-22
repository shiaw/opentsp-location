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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCMultiMediaParameter;

@LocationCommand(id = "2260")
public class DP_2260_MultiMediaParameter extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		try {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.MultiMediaParameter_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

			LCMultiMediaParameter.MultiMediaParameter mediaParameter = LCMultiMediaParameter.MultiMediaParameter
					.parseFrom(packet.getContent());
			int pnumber = 0;
			BytesArray bytesArray = new BytesArray();
			// pictureQuality 图像/视频质量，1~10,1最好
			if (mediaParameter.hasPictureQuality()) {
				bytesArray.append(Convert.longTobytes(0x70, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(
						mediaParameter.getPictureQuality(), 4));
				pnumber++;
			}
			// brightness 亮度，0~255
			if (mediaParameter.hasBrightness()) {
				bytesArray.append(Convert.longTobytes(0x71, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(mediaParameter.getBrightness(), 4));
				pnumber++;
			}
			// contrast 对比度，0~127
			if (mediaParameter.hasContrast()) {
				bytesArray.append(Convert.longTobytes(0x72, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(mediaParameter.getContrast(), 4));
				pnumber++;
			}
			// saturation 饱和度，0~127
			if (mediaParameter.hasSaturation()) {
				bytesArray.append(Convert.longTobytes(0x73, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(mediaParameter.getSaturation(), 4));
				pnumber++;
			}
			//chroma	色度，0~255
			if (mediaParameter.hasChroma()) {
				bytesArray.append(Convert.longTobytes(0x74, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(mediaParameter.getChroma(), 4));
				pnumber++;
			}
			byte[] bytes = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
			
			packet.setCommand(Constant.JTProtocol.TerminalSetting);
			packet.setContent(bytes);
			//return super.writeToTerminal(packet);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(packet);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
}

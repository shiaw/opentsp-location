package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.LCResultCode;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCEventCode.EventCode;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCFormatEncoding.FormatEncoding;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCMediaType;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCMediaEventInfoUpLoad.MediaEventInfoUpLoad;

/**
 * 多媒体事件信息上传
 * @author admin
 *
 */
@LocationCommand(id = "0800")
public class JT_0800_MultimediaEventUpload extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		String uniqueMark = packet.getUniqueMark();
		byte[] content = packet.getContent();
		long mediaId = Convert.byte2Long(ArraysUtils.subarrays(content, 0, 4),
                4);
		int type = Convert.byte2Int(ArraysUtils.subarrays(content, 4, 1), 1);
		int encode = Convert.byte2Int(ArraysUtils.subarrays(content, 5, 1), 1);
		int event = Convert.byte2Int(ArraysUtils.subarrays(content, 6, 1), 1);
		int channel = Convert.byte2Int(ArraysUtils.subarrays(content, 7, 1), 1);
		log.info("收到终端[" + uniqueMark + "]多媒体事件信息上传: 多媒体ID[" + mediaId
				+ "],多媒体类型[" + type + "],多媒体格式编码[" + encode + "],事件项编码["
				+ event + "],通道ID[" + channel + "].");
		
		MediaEventInfoUpLoad.Builder builder = MediaEventInfoUpLoad.newBuilder();
		builder.setMediaId(mediaId);
		switch (type) {
		case LCMediaType.MediaType.audio_VALUE:
			builder.setTypes(LCMediaType.MediaType.audio);
			break;
        case LCMediaType.MediaType.picture_VALUE:
        	builder.setTypes(LCMediaType.MediaType.picture);
			break;
        case LCMediaType.MediaType.video_VALUE:
        	builder.setTypes(LCMediaType.MediaType.video);
          break;
		default:
			break;
		}
		switch (encode) {
		case FormatEncoding.audio_mp3_VALUE:
			builder.setEncode(FormatEncoding.audio_mp3);
			break;
        case FormatEncoding.audio_wav_VALUE:
        	builder.setEncode(FormatEncoding.audio_wav);
			break;
        case FormatEncoding.picture_jpeg_VALUE:
        	builder.setEncode(FormatEncoding.picture_jpeg);
          break;
        case FormatEncoding.picture_tif_VALUE:
        	builder.setEncode(FormatEncoding.picture_tif);
          break;
        case FormatEncoding.video_wmv_VALUE:
        	builder.setEncode(FormatEncoding.video_wmv);
          break;
       
		default:
			break;
		}
		builder.setChannels(channel);
		switch (event) {
		case EventCode.collisionAlarm_VALUE:
			builder.setEvents(EventCode.collisionAlarm);
			break;
        case EventCode.distancePhoto_VALUE:
        	builder.setEvents(EventCode.distancePhoto);
			break;
        case EventCode.doorClosePhoto_VALUE:
        	builder.setEvents(EventCode.doorClosePhoto);
          break;
        case EventCode.doorOpenPhoto_VALUE:
        	builder.setEvents(EventCode.doorOpenPhoto);
          break;
        case EventCode.doorSwitch_VALUE:
        	builder.setEvents(EventCode.doorSwitch);
          break;
        case EventCode.platformSend_VALUE:
        	builder.setEvents(EventCode.platformSend);
          break;
        case EventCode.robberyAlarm_VALUE:
        	builder.setEvents(EventCode.robberyAlarm);
          break;
        case EventCode.timedAction_VALUE:

        	builder.setEvents(EventCode.timedAction);
          break;
		default:
			break;
		}
		Packet _out_packet = new Packet();
		_out_packet.setCommand(AllCommands.Terminal.MediaEventInfoUpLoad_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());;
		//super.writeToDataProcessing(_out_packet);
//        super.writeKafKaToDP(_out_packet, TopicConstants.JT_0800_MultimediaEventUpload);
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), 0x0800, LCResultCode.JTTerminal.SUCCESS));
		packetResult.setKafkaPacket(_out_packet);
		return  packetResult;
	}

}

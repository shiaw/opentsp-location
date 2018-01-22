package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver.jt2013;


import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes;
import org.springframework.stereotype.Component;

@LocationCommand(id = "0805",version = "200110")
@Component("JT_0805_TakePhotographyRes_200110")
public class JT_0805_TakePhotographyRes extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		byte[] content = packet.getContent();
		int serialNumber = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 2), 2);
		int result = Convert.byte2Int(ArraysUtils.subarrays(content, 2 , 1), 1);
		AnswerCommandCache.getInstance().remove(packet.getUniqueMark(), serialNumber);
	
		LCTakePhotographyRes.TakePhotographyRes.Builder builder = LCTakePhotographyRes.TakePhotographyRes.newBuilder();
		builder.setResult(ResponseResult.success);
		switch (result) {
		case LCPhotoResult.PhotoResult.success1_VALUE:
			builder.setResults(LCPhotoResult.PhotoResult.success1);
			
			break;
		case LCPhotoResult.PhotoResult.failed_VALUE:
			builder.setResults(LCPhotoResult.PhotoResult.failed);
			break;
		case LCPhotoResult.PhotoResult.channelNotSupport_VALUE:
			builder.setResults(LCPhotoResult.PhotoResult.channelNotSupport);
			break;
		default:
			break;
		}
		builder.setResults(LCPhotoResult.PhotoResult.valueOf(result));
		builder.setSerialNumber(serialNumber);
		if(result == 0){
			int mediaNumber = Convert.byte2Int(ArraysUtils.subarrays(content, 3, 2), 2);
			byte[] medias = ArraysUtils.subarrays(content, 5);
			for(int i = 0 ; i < mediaNumber ; i ++){
				long mediaId = Convert.byte2Int(ArraysUtils.subarrays(medias, i * 4, 4), 4);
				builder.addMediaIdentify(mediaId);
			}
		}
		
		Packet _out_packet = new Packet();
		_out_packet.setCommand(AllCommands.Terminal.TakePhotographyRes_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());
	//	super.writeToDataProcessing(_out_packet);
        super.writeKafKaToDP(_out_packet, TopicConstants.POSRAW);
		return null;
	}

}

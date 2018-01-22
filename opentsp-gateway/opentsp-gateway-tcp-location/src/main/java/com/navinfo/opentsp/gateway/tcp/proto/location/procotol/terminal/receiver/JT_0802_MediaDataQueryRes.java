package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;

import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCEventCode.EventCode;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCMediaType.MediaType;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCMediaDataQueryRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCMediaDataQueryRes.MediaDataQueryRes;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@LocationCommand(id = "0802",version = "200110")
public class JT_0802_MediaDataQueryRes extends TerminalCommand {
	
	private static int GPS_TIME_OUT = 300; 
	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		log.debug("收到终端[ "+packet.getUniqueMark()+" ]存储多媒体数据检索应答.");
		byte[] bytes = packet.getContent();
		int serialNumber = Convert.byte2Int(ArraysUtils.subarrays(bytes, 0, 2), 2);
		int dataNumber = Convert.byte2Int(ArraysUtils.subarrays(bytes, 2, 2), 2);
		int idx = 4;
	    MediaDataQueryRes.Builder builder = MediaDataQueryRes.newBuilder();
		
		builder.setSerialNumber(serialNumber);
		builder.setResult(LCResponseResult.ResponseResult.success);
	
		for(int i = 0; i < dataNumber; i++) {
			if(idx + 35 > bytes.length){
				break;
			}
			LCMediaDataQueryRes.MediaQueryData.Builder mediaQueryData= LCMediaDataQueryRes.MediaQueryData.newBuilder();
			// mediaId 多媒体数据标识
			long mediaId=Convert.byte2Long(ArraysUtils.subarrays(bytes, idx, 4),4);
			// types 多媒体类型,见附录，枚举
			int types=Convert.byte2Int(ArraysUtils.subarrays(bytes, idx+4, 1),1);
			// channels 通道(<32)
			int channels=Convert.byte2Int(ArraysUtils.subarrays(bytes, idx+5, 1),1);
			// events 事件编码,见附录，枚举
			int events=Convert.byte2Int(ArraysUtils.subarrays(bytes, idx+6, 1),1);
			// locationData 位置数据，见附录
			byte[] locationData = ArraysUtils.subarrays(bytes, idx+7, 28);//基本位置信息
			mediaQueryData.setMediaId(mediaId);
			switch (types) {
			case MediaType.audio_VALUE:
				mediaQueryData.setTypes(MediaType.audio);
				break;
            case MediaType.picture_VALUE:
            	mediaQueryData.setTypes(MediaType.picture);
				break;
            case MediaType.video_VALUE:
            	mediaQueryData.setTypes(MediaType.video);
	          break;
			default:
				break;
			}
			mediaQueryData.setChannels(channels);
			switch (events) {
			case EventCode.collisionAlarm_VALUE:
				mediaQueryData.setEvents(EventCode.collisionAlarm);
				break;
            case EventCode.distancePhoto_VALUE:
            	mediaQueryData.setEvents(EventCode.distancePhoto);
				break;
            case EventCode.doorClosePhoto_VALUE:
            	mediaQueryData.setEvents(EventCode.doorClosePhoto);
	          break;
            case EventCode.doorOpenPhoto_VALUE:
            	mediaQueryData.setEvents(EventCode.doorOpenPhoto);
	          break;
            case EventCode.doorSwitch_VALUE:
            	mediaQueryData.setEvents(EventCode.doorSwitch);
	          break;
            case EventCode.platformSend_VALUE:
            	mediaQueryData.setEvents(EventCode.platformSend);
	          break;
            case EventCode.robberyAlarm_VALUE:
            	mediaQueryData.setEvents(EventCode.robberyAlarm);
	          break;
            case EventCode.timedAction_VALUE:
            	mediaQueryData.setEvents(EventCode.timedAction);
	          break;
			default:
				break;
			}
			mediaQueryData.setLocationData(this.toLocationData(locationData));
			builder.addQueryData(mediaQueryData);
			idx+=35;
		}
		
	
		Packet _out_packet = new Packet();
		_out_packet.setCommand(AllCommands.Terminal.MediaDataQueryRes_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());;
        super.writeKafKaToDP(_out_packet, TopicConstants.JT_0802_MediaDataQueryRes);
		PacketResult packetResult=new PacketResult();
		packetResult.setKafkaPacket(_out_packet);
		return  packetResult;
	}
	
	     LocationData toLocationData(byte[] bytes){
		long alarm = Convert.byte2Long(ArraysUtils.subarrays(bytes, 0, 4), 4);
		byte[] subarrays = ArraysUtils.subarrays(bytes, 4, 4);
		String bytesToHexString = Convert.bytesToHexString(subarrays);
		System.err.println("bytesToHexString:"+bytesToHexString);
		long status = Convert.byte2Long(subarrays, 4);
		int latitude = Convert.byte2Int(ArraysUtils.subarrays(bytes, 8, 4), 4);
		int longitude = Convert.byte2Int(ArraysUtils.subarrays(bytes, 12, 4), 4);
		int height = Convert.byte2Int(ArraysUtils.subarrays(bytes, 16, 2), 2);
		int speed = Convert.byte2Int(ArraysUtils.subarrays(bytes, 18, 2), 2);
		int direction = Convert.byte2Int(ArraysUtils.subarrays(bytes, 20, 2), 2);
		long currentTime = System.currentTimeMillis()/1000;
		long gpsTime = currentTime;
		String timestr = "20"+Convert.bytesToHexString(ArraysUtils.subarrays(bytes, 22, 6));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		try {
			gpsTime = sdf.parse(timestr).getTime()/1000;
		} catch (ParseException e) {
			gpsTime = System.currentTimeMillis()/1000;
		}
		LocationData.Builder builder = LocationData.newBuilder();
		builder.setAlarm(alarm);
		builder.setStatus(status);
		builder.setOriginalLat(latitude);
		builder.setOriginalLng(longitude);
		builder.setLatitude(latitude);
		builder.setLongitude(longitude);
		builder.setHeight(height);
		builder.setSpeed(speed);
		builder.setDirection(direction);
		builder.setGpsDate(gpsTime);
		long serverTime = System.currentTimeMillis()/1000;
		builder.setReceiveDate(serverTime);
		builder.setIsPatch((serverTime - gpsTime) > GPS_TIME_OUT ? true : false);
		
		return builder.build();
	}
	public static void main(String[] args) {
		String s= "092400041C9CE8E200000000000000000C0003026BEFA5068C3CE20429000000191503190954421C9CF62100000000000020000C0001026BEFCC068C3C0C0401000000001503191051131C9CF70E00000000000020000C0001026BEFFC068C3C39040F000000151503191055101C9D3EE200000000000000000C0003026BED4D068C3C520470000000B3150319160138";
		byte[] bytes = Convert.hexStringToBytes(s);
		int serialNumber = Convert.byte2Int(ArraysUtils.subarrays(bytes, 0, 2), 2);
		int dataNumber = Convert.byte2Int(ArraysUtils.subarrays(bytes, 2, 2), 2);
		int idx = 4;
	    MediaDataQueryRes.Builder builder = MediaDataQueryRes.newBuilder();
		
		builder.setSerialNumber(serialNumber);
		builder.setResult(LCResponseResult.ResponseResult.success);
	
		for(int i = 0; i < dataNumber; i++) {
			if(idx + 35 > bytes.length){
				break;
			}
			LCMediaDataQueryRes.MediaQueryData.Builder mediaQueryData= LCMediaDataQueryRes.MediaQueryData.newBuilder();
			// mediaId 多媒体数据标识
			long mediaId=Convert.byte2Long(ArraysUtils.subarrays(bytes, idx, 4),4);
			// types 多媒体类型,见附录，枚举
			int types=Convert.byte2Int(ArraysUtils.subarrays(bytes, idx+4, 1),1);
			// channels 通道(<32)
			int channels=Convert.byte2Int(ArraysUtils.subarrays(bytes, idx+5, 1),1);
			// events 事件编码,见附录，枚举
			int events=Convert.byte2Int(ArraysUtils.subarrays(bytes, idx+6, 1),1);
			// locationData 位置数据，见附录
			byte[] locationData = ArraysUtils.subarrays(bytes, idx+7, 28);//基本位置信息
			mediaQueryData.setMediaId(mediaId);
			switch (types) {
			case MediaType.audio_VALUE:
				mediaQueryData.setTypes(MediaType.audio);
				break;
            case MediaType.picture_VALUE:
            	mediaQueryData.setTypes(MediaType.picture);
				break;
            case MediaType.video_VALUE:
            	mediaQueryData.setTypes(MediaType.video);
	          break;
			default:
				break;
			}
			mediaQueryData.setChannels(channels);
			switch (events) {
			case EventCode.collisionAlarm_VALUE:
				mediaQueryData.setEvents(EventCode.collisionAlarm);
				break;
            case EventCode.distancePhoto_VALUE:
            	mediaQueryData.setEvents(EventCode.distancePhoto);
				break;
            case EventCode.doorClosePhoto_VALUE:
            	mediaQueryData.setEvents(EventCode.doorClosePhoto);
	          break;
            case EventCode.doorOpenPhoto_VALUE:
            	mediaQueryData.setEvents(EventCode.doorOpenPhoto);
	          break;
            case EventCode.doorSwitch_VALUE:
            	mediaQueryData.setEvents(EventCode.doorSwitch);
	          break;
            case EventCode.platformSend_VALUE:
            	mediaQueryData.setEvents(EventCode.platformSend);
	          break;
            case EventCode.robberyAlarm_VALUE:
            	mediaQueryData.setEvents(EventCode.robberyAlarm);
	          break;
            case EventCode.timedAction_VALUE:
            	mediaQueryData.setEvents(EventCode.timedAction);
	          break;
			default:
				break;
			}
		//	mediaQueryData.setLocationData(toLocationData(locationData));
			builder.addQueryData(mediaQueryData);
			idx+=35;
		}
	}
}

package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;

import com.google.protobuf.ByteString;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.CacheKey;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.InternalCache;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.*;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCEventCode;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCFormatEncoding;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCMediaType;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCMediaType.MediaType;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCMultimediaUpload;
import org.springframework.beans.factory.annotation.Value;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCMultimediaUpload.*;

@LocationCommand(id = "0801")
public class JT_0801_MultimediaUpload extends TerminalCommand {
	private static int GPS_TIME_OUT = 300;

    @Value("${opentsp.command.picture.data.timeout.threshold:10}")
    private int ta_pictureDataTimeoutThreshold;
	/**
	 * 1、图片数据的分包上传<br>
	 * 2、图片数据的乱序上传<br>
	 * 3、图片数据丢包,需要向终端重新申请补传图片数据包<br>
	 * 4、图片的返回,应在下发时候缓存好
	 */
	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		int packetSerial = packet.getPacketSerial();
		int total = packet.getPacketTotal();
		log.error("包总数："+total+"当前包序号："+packetSerial);
		super.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(), LCResultCode.JTTerminal.SUCCESS);
		//System.err.println("收到拍照应答：共["+total+"]数据包,当前第["+packetSerial+"]包.");
		String uniqueMark = packet.getUniqueMark();
		//缓存key=唯一标识_多媒体类型_多媒体ID
		String cacheKey = CacheKey.multimediaKey(uniqueMark, 1, 1);
		Packet[] packetCache = (Packet[])InternalCache.getInstance().get(cacheKey);
		if(packetCache == null) {
			packetCache = new Packet[total];
		}
		packetCache[packetSerial-1] = packet;
		InternalCache.getInstance().add(cacheKey,ta_pictureDataTimeoutThreshold, packetCache);

		Packet frist_packet = packetCache[0];
		if(frist_packet == null){
			System.err.println("丢失首包,抛弃.");
			InternalCache.getInstance().delete(cacheKey);
			return null;
		}
		byte[] bytes = frist_packet.getContent();
		long mediaId = Convert.byte2Long(ArraysUtils.subarrays(bytes, 0, 4), 4);

		//每次接受一个多媒体数据包后都判断是否收到了所有的包
		//如果收到了所有的包，则进行分包处理
		if(this.isNull(packetCache)) {
			System.err.println("收到完整数据包.");
			InternalCache.getInstance().delete(cacheKey);
			int type = Convert.byte2Int(ArraysUtils.subarrays(bytes, 4, 1), 1);
			int encode = Convert.byte2Int(ArraysUtils.subarrays(bytes, 5, 1), 1);
			int event = Convert.byte2Int(ArraysUtils.subarrays(bytes, 6, 1), 1);
			int channel = Convert.byte2Int(ArraysUtils.subarrays(bytes, 7, 1), 1);
			System.err.println(type+" "+encode+" "+event+" "+channel);
			byte[] mediaData = new byte[0];
			MultimediaUpload.Builder builder =
					MultimediaUpload.newBuilder();
			for(int i = 0; i < packetCache.length; i++) {
				byte[] contentCache = packetCache[i].getContent();
				if(i==0){
					byte[] locationData = ArraysUtils.subarrays(contentCache, 8, 28);//基本位置信息
					builder.setLocationData(this.toLocationData(locationData));
					byte[] pieceData = ArraysUtils.subarrays(contentCache, 36, contentCache.length-36);
					mediaData = ArraysUtils.arraycopy(mediaData, pieceData);
				}else {
					mediaData = ArraysUtils.arraycopy(mediaData, contentCache);
				}
			}

			builder.setTypes(MediaType.valueOf(type));
			builder.setEncode(LCFormatEncoding.FormatEncoding.valueOf(encode));
			builder.setEvents(LCEventCode.EventCode.valueOf(event));
			builder.setChannels(channel);
			builder.setMediaId(mediaId);
			builder.setMediaData(ByteString.copyFrom(mediaData));
			log.error("多媒体ID:"+mediaId);
			//PhotoGaph gaph = new PhotoGaph();
			//gaph.write(1, "测试", mediaData, null, null, 1l, 1l);
			log.error("图片数据包："+Convert.bytesToHexString(mediaData));
			Packet _out_packet = new Packet();
			_out_packet.setCommand(AllCommands.Terminal.MultimediaUpload_VALUE);
			_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
			_out_packet.setUniqueMark(packet.getUniqueMark());
			_out_packet.setSerialNumber(packet.getSerialNumber());
			_out_packet.setContent(builder.build().toByteArray());
			//super.writeToDataProcessing(_out_packet);
            //super.writeKafKaToDP(_out_packet,TopicConstants.JT_0801_MultimediaUpload);

			//发送多媒体数据上传应答(不需要终端重传包)
			Packet respPacket = new Packet(true,5);
			respPacket.setCommand(Constant.JTProtocol.TerminalMultimediaDataReportedResponse);
			respPacket.setProtocol(LCConstant.LCMessageType.TERMINAL);
			respPacket.setUniqueMark(uniqueMark);
			respPacket.appendContent(Convert.longTobytes(mediaId, 4));
			respPacket.appendContent(Convert.intTobytes(0, 1));

			//super.write(respPacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(respPacket);
			packetResult.setKafkaPacket(_out_packet);
			return  packetResult;
		}
		return null;
	}

	boolean isNull(Packet[] packets){
		for (Packet packet : packets) {
			if(packet == null){
				return false;
			}
		}
		return true;
	}
	private LocationData toLocationData(byte[] bytes){
		long alarm = Convert.byte2Long(ArraysUtils.subarrays(bytes, 0, 4), 4);
		long status = Convert.byte2Long(ArraysUtils.subarrays(bytes, 4, 4), 4);
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
}

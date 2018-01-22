package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;//package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
//
//import com.google.protobuf.ByteString;
//import com.navinfo.opentsp.platform.location.kit.Packet;
//import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
//import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
//import com.navinfo.opentsp.platform.location.kit.Convert;
//import com.navinfo.opentsp.platform.location.kit.LCConstant;
//import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
//import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCEventCode;
//import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCFormatEncoding;
//import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCMediaType;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//
//public class JT_0801_MultimediaUpload_backup {
//	private static int GPS_TIME_OUT = 300;
//	/**
//	 * 1、图片数据的分包上传<br>
//	 * 2、图片数据的乱序上传<br>
//	 * 3、图片数据丢包,需要向终端重新申请补传图片数据包<br>
//	 * 4、图片的返回,应在下发时候缓存好
//	 */
//	public PacketResult processor(Packet packet) {
//		int packetSerial = packet.getPacketSerial();
//		int total = packet.getPacketTotal();
//		//System.err.println("收到拍照应答：共["+total+"]数据包,当前第["+packetSerial+"]包.");
//		String uniqueMark = packet.getUniqueMark();
//		//缓存key=唯一标识_多媒体类型_多媒体ID
//		String cacheKey = CacheKey.multimediaKey(uniqueMark, 1, 1);
//
////		int serialNum = packet.getSerialNumber();
//		byte[] content = packet.getContent();
////		long mediaId = Convert.byte2Long(ArraysUtils.subarrays(content, 0, 4), 4);
//
//		System.err.println("缓存key："+cacheKey);
//		Packet[] packetCache = (Packet[])InternalCache.getInstance().get(cacheKey);
//		if(packetCache == null) {
//			System.err.println("缓存中未存在此终端的拍照数据,创建缓存数据包.");
//			packetCache = new Packet[total];
//		}
//		packetCache[packetSerial-1] = packet;
//		InternalCache.getInstance().add(cacheKey,Configuration.getInt("ta_pictureDataTimeoutThreshold") , packetCache);
//
//
//		//如果收到的包序号等于包总数，则判断是否收到了所有的包
//		if(packetSerial == total) {
//			int retransmitNum = 0;
//			byte[] retransmitId = new byte[0];
//			for(int i = 0 , length = packetCache.length ; i < length; i++) {
//				if(packetCache[i] == null) {
//					retransmitNum++;
//					ArraysUtils.arraycopy(retransmitId, Convert.intTobytes(i + 1, 2));
//				}
//			}
//			//如果缺包则给终端发送多媒体数据上传应答，要求其重传缺少的包
//			if(retransmitNum != 0) {
//				Packet frist_packet = packetCache[0];
//				byte[] bytes = frist_packet.getContent();
//				long mediaId = Convert.byte2Long(ArraysUtils.subarrays(bytes, 0, 4), 4);
//				int capacity = 5 + retransmitId.length;
//				Packet respPacket = new Packet(true ,capacity);
//				respPacket.setCommand(Constant.JTProtocol.TerminalMultimediaDataReportedResponse);
//				respPacket.setUniqueMark(uniqueMark);
//				respPacket.appendContent(Convert.longTobytes(mediaId, 4));
//				respPacket.appendContent(Convert.intTobytes(retransmitNum, 1));
//				respPacket.appendContent(retransmitId);
//
//			//	super.write(respPacket);
//				return 0;
//			}
//		}
//		//每次接受一个多媒体数据包后都判断是否收到了所有的包
//		int idx = 0;
//		for(; idx < packetCache.length; idx++) {
//			if(packetCache[idx] == null)
//				break;
//		}
//		//如果收到了所有的包，则进行分包处理
//		if(idx == packetCache.length) {
//			InternalCache.getInstance().delete(cacheKey);
//			int type = Convert.byte2Int(ArraysUtils.subarrays(content, 4, 1), 1);
//			int encode = Convert.byte2Int(ArraysUtils.subarrays(content, 5, 1), 1);
//			int event = Convert.byte2Int(ArraysUtils.subarrays(content, 6, 1), 1);
//			int channel = Convert.byte2Int(ArraysUtils.subarrays(content, 7, 1), 1);
//			byte[] mediaData = new byte[0];
//			LCMultimediaUpload.MultimediaUpload.Builder builder =
//					LCMultimediaUpload.MultimediaUpload.newBuilder();
//			for(int i = 0; i < packetCache.length; i++) {
//				byte[] contentCache = packetCache[i].getContent();
//				if(i==0){
//					byte[] locationData = ArraysUtils.subarrays(contentCache, 8, 28);//基本位置信息
//					builder.setLocationData(this.toLocationData(locationData));
//					byte[] pieceData = ArraysUtils.subarrays(contentCache, 36, contentCache.length-36);
//					mediaData = ArraysUtils.arraycopy(mediaData, pieceData);
//				}else {
//					mediaData = ArraysUtils.arraycopy(mediaData, contentCache);
//				}
//			}
//
//			builder.setTypes(LCMediaType.MediaType.picture);
//			builder.setEncode(LCFormatEncoding.FormatEncoding.picture_jpeg);
//			builder.setEvents(LCEventCode.EventCode.platformSend);
//			builder.setChannels(channel);
//			builder.setMediaData(ByteString.copyFrom(mediaData));
//			PhotoGaph gaph = new PhotoGaph();
//			gaph.write(1, "测试", mediaData, null, null, 1l, 1l);
//			Packet _out_packet = new Packet();
//			_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.MultimediaUpload_VALUE);
//			_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
//			_out_packet.setUniqueMark(packet.getUniqueMark());
//			_out_packet.setSerialNumber(packet.getSerialNumber());
//			_out_packet.setContent(builder.build().toByteArray());
//			//super.writeToDataProcessing(_out_packet);
//
//			Packet frist_packet = packetCache[0];
//			byte[] bytes = frist_packet.getContent();
//			long mediaId = Convert.byte2Long(ArraysUtils.subarrays(bytes, 0, 4), 4);
//			//发送多媒体数据上传应答(不需要终端重传包)
//			Packet respPacket = new Packet(true,5);
//			respPacket.setCommand(Constant.JTProtocol.TerminalMultimediaDataReportedResponse);
//			respPacket.setProtocol(LCConstant.LCMessageType.TERMINAL);
//			respPacket.setUniqueMark(uniqueMark);
//			respPacket.appendContent(Convert.longTobytes(mediaId, 4));
//			respPacket.appendContent(Convert.intTobytes(0, 1));
//
//			//super.write(respPacket);
//		}
//		return 0;
//	}
//	private LocationData toLocationData(byte[] bytes){
//		long alarm = Convert.byte2Long(ArraysUtils.subarrays(bytes, 0, 4), 4);
//		long status = Convert.byte2Long(ArraysUtils.subarrays(bytes, 4, 4), 4);
//		int latitude = Convert.byte2Int(ArraysUtils.subarrays(bytes, 8, 4), 4);
//		int longitude = Convert.byte2Int(ArraysUtils.subarrays(bytes, 12, 4), 4);
//		int height = Convert.byte2Int(ArraysUtils.subarrays(bytes, 16, 2), 2);
//		int speed = Convert.byte2Int(ArraysUtils.subarrays(bytes, 18, 2), 2);
//		int direction = Convert.byte2Int(ArraysUtils.subarrays(bytes, 20, 2), 2);
//		long currentTime = System.currentTimeMillis()/1000;
//		long gpsTime = currentTime;
//		String timestr = "20"+Convert.bytesToHexString(ArraysUtils.subarrays(bytes, 22, 6));
//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//
//		try {
//			gpsTime = sdf.parse(timestr).getTime()/1000;
//		} catch (ParseException e) {
//			gpsTime = System.currentTimeMillis()/1000;
//		}
//		LocationData.Builder builder = LocationData.newBuilder();
//		builder.setAlarm(alarm);
//		builder.setStatus(status);
//		builder.setOriginalLat(latitude);
//		builder.setOriginalLng(longitude);
//		builder.setLatitude(latitude);
//		builder.setLongitude(longitude);
//		builder.setHeight(height);
//		builder.setSpeed(speed);
//		builder.setDirection(direction);
//		builder.setGpsDate(gpsTime);
//		long serverTime = System.currentTimeMillis()/1000;
//		builder.setReceiveDate(serverTime);
//		builder.setIsPatch((serverTime - gpsTime) > GPS_TIME_OUT ? true : false);
//
//		return builder.build();
//	}
//}

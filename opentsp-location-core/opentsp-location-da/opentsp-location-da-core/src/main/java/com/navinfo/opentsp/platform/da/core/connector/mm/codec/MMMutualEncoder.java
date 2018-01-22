package com.navinfo.opentsp.platform.da.core.connector.mm.codec;

import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.PacketProcessing;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;

public class MMMutualEncoder implements ProtocolEncoder {

	private final static Logger log = LoggerFactory.getLogger(MMMutualEncoder.class);
	@Override
	public void dispose(IoSession arg0) throws Exception {

	}

	@Override
	public void encode(IoSession arg0, Object arg1, ProtocolEncoderOutput arg2)
			throws Exception {
		Packet outpacket = (Packet) arg1;
		byte[] messageType = Convert.intTobytes(outpacket.getProtocol(), 1);
		byte[] uniqueMark = Convert.hexStringToBytes(outpacket.getUniqueMark());
		byte[] serialNumber = Convert.longTobytes(outpacket.getSerialNumber(), 2);
		byte[] content = outpacket.getContent();
		byte[] cmdId = Convert.longTobytes(outpacket.getCommand(), 2);
		
		if(content != null){
			if(content.length > LCConstant.PACKET_MAX_LENGTH){
				List<byte[]> list = PacketProcessing.dataSegmentation(content,LCConstant.PACKET_MAX_LENGTH);
				int packetCount = list.size();
				byte[] pkCount = Convert.longTobytes(packetCount, 2);
				byte[] blockId = Convert.intTobytes(PacketProcessing.getBlockId(), 2);
				for(int i = 0 ; i < packetCount ; i ++){
					byte[] pkNumber = Convert.longTobytes( i + 1, 2 );
					
					byte[] pkNode = ArraysUtils.arraycopy(pkCount, pkNumber);
					pkNode = ArraysUtils.arraycopy(pkNode, blockId);
					
					byte[] pkProperty = Convert.longTobytes(list.get(i).length + 8192, 2);//8192字节位表示当前数据分包

					this.write(messageType, cmdId, pkProperty, uniqueMark, serialNumber, pkNode, list.get(i), arg2);
				}
			}else{
				byte[] pkProperty = Convert.longTobytes(content.length , 2);
				this.write(messageType  , cmdId, pkProperty, uniqueMark, serialNumber, null, content,arg2);
			}
		}
	}

	private void write(byte[] messageType ,byte[] cmdId, byte[] pkProperty, byte[] uniqueMark,
			byte[] serialNumber, byte[] pkNode, byte[] pkContent,ProtocolEncoderOutput out) {
		//首标识位=1		消息类型=1 	消息头 ={消息ID=2		消息体属性=2		唯一标识=6		消息流水号=2		消息封包项=4}		消息体=N		校验码=1		尾标识位=1
		int contentLength = pkContent != null ? pkContent.length : 0;
		int pkLength = pkNode == null ? contentLength +  14 : contentLength + 20;
		byte[] data = new byte[pkLength];
		ArraysUtils.arrayappend(data, 0, messageType);//消息类型
		ArraysUtils.arrayappend(data, 1, cmdId);//指令号
		ArraysUtils.arrayappend(data, 3, pkProperty);//消息体属性
		ArraysUtils.arrayappend(data, 5, uniqueMark);//唯一标识
		ArraysUtils.arrayappend(data, 11, serialNumber);//流水号
		if (pkNode != null) {// 消息封装项
			ArraysUtils.arrayappend(data, 13, pkNode);
			ArraysUtils.arrayappend(data, 19, pkContent);
			ArraysUtils.arrayappend(data, data.length - 1 ,
					new byte[] { PacketProcessing.checkPackage(data, 0, data.length - 1) });
		}else{
			ArraysUtils.arrayappend(data, 13, pkContent);
			ArraysUtils.arrayappend(data, data.length - 1 , 
					new byte[] { PacketProcessing.checkPackage(data, 0, data.length - 1) });
		}
		
		byte[] bytes = PacketProcessing.escape(data, LCConstant.escapeByte, LCConstant.toEscapeByte);
		IoBuffer buffer = IoBuffer.allocate(bytes.length+2);
		buffer.put(0,LCConstant.pkBegin);
		int index = 1;
		for (byte b : bytes) {
			buffer.put(index , b);
			index++;
		}
		buffer.put(bytes.length+1,LCConstant.pkEnd);
		//log.info("[DA>MM]: "+buffer.getHexDump());
		out.write(buffer);
	}


}

package com.navinfo.opentsp.platform.da.core.acceptor.codec;

import java.util.List;

import com.navinfo.opentsp.platform.da.core.acceptor.session.MutualSession;
import com.navinfo.opentsp.platform.da.core.acceptor.session.MutualSessionManage;
import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.PacketProcessing;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MutualDecoder implements ProtocolDecoder {
	private static final Logger logger = LoggerFactory.getLogger(MutualDecoder.class);

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
//		logger.debug("[存储、检索：Other Node->DA]: " + in.getHexDump());
		int limit = in.limit();
		byte[] currentBytes = new byte[limit];
		in.get(currentBytes, 0, limit);
		MutualSession mutualSession = MutualSessionManage.getInstance().getMutualSessionForSessionId(session.getId());
		if(mutualSession != null){
			byte[] lastBytes = mutualSession.getStickPack();
			byte[] bytes = ArraysUtils.arraycopy(lastBytes, currentBytes);
			List<byte[]> packets = PacketProcessing.subpackage(bytes, LCConstant.pkBegin, LCConstant.pkEnd, 0);
			for (byte[] packet : packets) {
				if(packet != null){
					if(packet[0] == LCConstant.pkBegin && packet[packet.length -1] == LCConstant.pkEnd){
						this.build(packet, session,out);
					}else if(packet[0] == LCConstant.pkBegin){
						mutualSession.setStickPack(packet);
					}
				}
			}
		}else{
			session.close();
		}
	}

	private void build(byte[] packet, IoSession session,
			ProtocolDecoderOutput out) {
		//去包头包尾
		byte[] tempBytes = new byte[packet.length - 2];
		System.arraycopy(packet, 1, tempBytes, 0, tempBytes.length);
		//转义还原
		byte[] bytes = PacketProcessing.unEscape(tempBytes, LCConstant.toEscapeByte, LCConstant.escapeByte);
		//取出源数据检验码
		int checkCode = bytes[bytes.length - 1];
		//计算检验码
		int tempCode = PacketProcessing.checkPackage(bytes, 0, bytes.length - 2);
		if (checkCode != tempCode) {
			logger.error("Xor error , result[" + tempCode + "],source[" + checkCode
					+ "].source data :" + Convert.bytesToHexString(packet));
			return;
		}

		int msgType = Convert.byte2Int(ArraysUtils.subarrays(bytes, 0, 1), 2);
		int cmdId = Convert.byte2Int(ArraysUtils.subarrays(bytes, 1, 2), 2);
		int cmdProperty = Convert.byte2Int(ArraysUtils.subarrays(bytes, 3, 2), 2);
		String uniqueMark = Convert.bytesToHexString(ArraysUtils.subarrays(bytes, 5, 6));
		int serialNumber = Convert.byte2Int(ArraysUtils.subarrays(bytes, 11, 2), 2);

		Packet outpacket = new Packet();
		outpacket.setCommand(cmdId);
		outpacket.setSerialNumber(serialNumber);
		outpacket.setProtocol(msgType);
		outpacket.setOriginalPacket(packet);
		outpacket.setUniqueMark(uniqueMark);
		if(Long.parseLong(uniqueMark) > 1000000){
			logger.error("uniqueMark" + uniqueMark + "cmdId" + cmdId + "NodeCode" + MutualSessionManage.getNodeCodeBySessionId(session.getId()));
		}
		outpacket.setFrom(Long.parseLong(uniqueMark));
		//如果指令为节点汇报,添加SessionID到Packet包,提供给Session与节点编号绑定所用
		if(cmdId == LCAllCommands.AllCommands.NodeCluster.ReportServerIdentify_VALUE){
			outpacket.addParameter("sessionId", session.getId());
		}
		//判断是否分包
		if((cmdProperty & 8192) > 0){
			int total = Convert.byte2Int(ArraysUtils.subarrays(bytes, 13, 2), 2);// 消息包封装项,包总数
			int serial = Convert.byte2Int(ArraysUtils.subarrays(bytes, 15, 2), 2);;// 消息包封装项,包序号
			int blockId = Convert.byte2Int(ArraysUtils.subarrays(bytes, 17, 2), 2);//消息包封装项,包块ID
			byte[] content = ArraysUtils.subarrays(bytes, 19 , bytes.length - 20);
			outpacket.setPacketTotal(total);
			outpacket.setPacketSerial(serial);
			outpacket.setBlockId(blockId);
			outpacket.setContent(content);
			boolean isComplete = PacketProcessing.mergeBlock(outpacket);
			if( isComplete ){
				outpacket = PacketProcessing.getCompletePacket(PacketProcessing.getCacheBlockId(uniqueMark, blockId));
				out.write(outpacket);
			}
		}else{
			byte[] content = ArraysUtils.subarrays(bytes, 13, bytes.length - 14);
			outpacket.setContent(content);
			out.write(outpacket);
		}
	}



	@Override
	public void dispose(IoSession arg0) throws Exception {

	}

	@Override
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1)
			throws Exception {

	}
	/**
	 * 链路校验 内部节点暂时不做链路校验
	 * @param unqieMark
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean sessionCheck(String unqieMark , IoSession session){
		String key = String.valueOf(session.getAttribute(Constant.UniqueMark));
		if(unqieMark.equals(key)){
			return true;
		}else{
			return false;
		}
	}
}

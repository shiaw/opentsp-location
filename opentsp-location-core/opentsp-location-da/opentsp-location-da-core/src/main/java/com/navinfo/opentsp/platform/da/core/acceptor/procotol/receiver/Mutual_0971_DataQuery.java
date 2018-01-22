package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.util.List;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDataQuery.DataQuery;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDataQueryRes;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.TermianlDynamicManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.TermianlDynamicManageImpl;
import  com.navinfo.opentsp.platform.location.kit.LCConstant;
import  com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import  com.navinfo.opentsp.platform.location.kit.Packet;
@DaRmiNo(id = "0971")
public class Mutual_0971_DataQuery extends Dacommand {
	final static TermianlDynamicManage termianlDynamicManage = new TermianlDynamicManageImpl();
	final static int block_size = 100;

	@Override
	public Packet processor(Packet packet) {
		try {
			// 获取消息主体
			DataQuery dataQuery = DataQuery.parseFrom(packet.getContent());
			// 从数据库取数据
			List<byte[]> result = termianlDynamicManage.queryLinkExceptionData(
					dataQuery.getBlockNumber(), dataQuery.getNodeNumberTo(),
					dataQuery.getDataType(), dataQuery.getSerializedSize());
			int total = result.size();
			int totalBlock = (total % block_size == 0) ? total / block_size
					: total / block_size + 1;
			for (int i = 0; i < totalBlock; i++) {
				LCDataQueryRes.DataQueryRes.Builder builder = LCDataQueryRes.DataQueryRes
						.newBuilder();
				// 收集一次块数据
				for (int j = 0; j < block_size; j++) {
					byte[] bytes = result.get(i + (i * total));
					builder.setDataValue(j, ByteString.copyFrom(bytes));
				}
				builder.setDataType(dataQuery.getDataType());
				builder.setNodeNumberTo(dataQuery.getNodeNumberTo());
				builder.setBlockNumber(dataQuery.getBlockNumber());
				builder.setTotalBlock(totalBlock);
				
				Packet _out_packet = new Packet(true);
				_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
				_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
				_out_packet.setCommand(AllCommands.DataAccess.DataQueryRes_VALUE);
				_out_packet.setProtocol(LCMessageType.PLATFORM);
				_out_packet.setTo(packet.getFrom());
				_out_packet.setFrom(NodeHelper.getNodeCode());
				_out_packet.setContent(builder.build().toByteArray());
				return  _out_packet;
			}

		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}


}

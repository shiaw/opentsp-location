package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.DictManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.DictManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.DictData;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDictData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDictType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCGetDictData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCGetDictDataRes;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

@DaRmiNo(id = "0910")
public class Mutual_0910_GetDictData extends Dacommand {
	final static DictManage dictManage = new DictManageImpl();
	@Override
	public Packet processor(Packet packet) {
		try {
			LCGetDictData.GetDictData getDictData = LCGetDictData.GetDictData.parseFrom(packet.getContent());
			List<LCDictType.DictType> list = getDictData.getDictTypeList();
			
			LCGetDictDataRes.GetDictDataRes.Builder builder = LCGetDictDataRes.GetDictDataRes.newBuilder();
			if(CollectionUtils.isNotEmpty(list)){
				builder.setSerialNumber(packet.getSerialNumber());
				builder.setResults(LCPlatformResponseResult.PlatformResponseResult.success);
				for (LCDictType.DictType dictType : list) {
					List<DictData> temp = dictManage.getDictDataRes(dictType.getNumber());
					if(CollectionUtils.isNotEmpty(temp)){
						for (DictData dictData : temp) {
							LCDictData.DictData.Builder builder2 = LCDictData.DictData.newBuilder();
							builder2.setDataCode(dictData.getDataCode());
							if(null != dictData.getGbCode())builder2.setGbCode(dictData.getGbCode());
							builder2.setName(dictData.getName());
							if(null != dictData.getParentDataCode())builder2.setParentDataCode(dictData.getParentDataCode());
							builder2.setDictType(LCDictType.DictType.valueOf(dictData.getDictType()));
							if(null != dictData.getDictValue())builder2.setDictValue(dictData.getDictValue());
							builder.addDatas(builder2);
						}
					}
				}
			}
			
			Packet _out_packet = new Packet(true);
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setCommand(LCAllCommands.AllCommands.DataAccess.GetDictDataRes_VALUE);
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setTo(packet.getFrom());
			_out_packet.setFrom(NodeHelper.getNodeCode());
			_out_packet.setContent(builder.build().toByteArray());
			return  _out_packet;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
			return  null;
		}
	}

}

package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.RegularDataAreaAndDataManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.RegularDataAreaAndDataManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDataDBEntity;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCQueryAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCQueryAreaInfoRes;
import org.apache.commons.collections.CollectionUtils;

@DaRmiNo(id = "0930")
public class Mutual_0930_QueryAreaInfo extends Dacommand {
	final static RegularDataAreaAndDataManage manager = new RegularDataAreaAndDataManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			//获取消息主体
			LCQueryAreaInfo.QueryAreaInfo gti = LCQueryAreaInfo.QueryAreaInfo.parseFrom(packet.getContent());
			//获取参数
			int districtCode = gti.getDistrictCode().getNumber();
			long node_code = gti.getNodeCode();
			List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
			//从数据库取数据
				List<Map<String, Object>> query_list = manager.queryAreaInfo(districtCode,node_code);
				if(CollectionUtils.isNotEmpty(query_list)){
					result.addAll(query_list);
				}
			return buildPacket(packet ,result);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	/**
	 * 构建packet
	 * @param list
	 * @return
	 */
	private Packet buildPacket(Packet packet , List<Map<String, Object>> list) {
		LCQueryAreaInfoRes.QueryAreaInfoRes.Builder list_builder=LCQueryAreaInfoRes.QueryAreaInfoRes.newBuilder();
		list_builder.setSerialNumber(packet.getSerialNumber());
		list_builder.setResults(LCPlatformResponseResult.PlatformResponseResult.success);
		if(CollectionUtils.isNotEmpty(list)){
			for(Map<String, Object> obj:list){
				LCAreaInfo.AreaInfo.Builder area_builder=LCAreaInfo.AreaInfo.newBuilder();

				LcTerminalAreaDBEntity area=(LcTerminalAreaDBEntity)obj.get("topic");//区域信息
				area_builder.setTerminalId(area.getTerminal_id());
				area_builder.setAreaIdentify(area.getOriginal_area_id());
//				switch(area.getArea_type()){
//				case 140000 :
//					area_builder.setTypes(LCAreaType.AreaType.noType);
//					break;
//				case 140001 :
//					area_builder.setTypes(LCAreaType.AreaType.point);
//					break;
//				case 140002 :
//					area_builder.setTypes(LCAreaType.AreaType.circle);
//					break;
//				case 140003 :
//					area_builder.setTypes(LCAreaType.AreaType.rectangle);
//					break;
//				case 140004 :
//					area_builder.setTypes(LCAreaType.AreaType.polygon);
//					break;
//				case 140005 :
//					area_builder.setTypes(LCAreaType.AreaType.route);
//					break;
//				case 140006 :
//					area_builder.setTypes(LCAreaType.AreaType.segment);
//					break;
//				}
				area_builder.setTypes(LCAreaType.AreaType.valueOf(area.getArea_type()));
				area_builder.setCreateDate(area.getCreate_time());

				List<LcTerminalAreaDataDBEntity> datas = (List<LcTerminalAreaDataDBEntity>) obj.get("detailed");//区域数据
				for (LcTerminalAreaDataDBEntity lcTerminalAreaDataDBEntity : datas) {
					LCAreaData.AreaData.Builder data_builder=LCAreaData.AreaData.newBuilder();
					data_builder.setDataSN(lcTerminalAreaDataDBEntity.getData_sn());
					data_builder.setDataStatus(lcTerminalAreaDataDBEntity.getData_status());
					data_builder.setLongitude(lcTerminalAreaDataDBEntity.getLongitude());
					data_builder.setLatitude(lcTerminalAreaDataDBEntity.getLatitude());
					data_builder.setRadiusLength(lcTerminalAreaDataDBEntity.getRadius_len());
					area_builder.addDatas(data_builder.build());
				}
				list_builder.addInfos(area_builder);
			}
		}
		
		Packet _out_packet = new Packet(true);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setCommand(LCAllCommands.AllCommands.DataAccess.QueryAreaInfoRes_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setTo(packet.getFrom());
		_out_packet.setFrom(NodeHelper.getNodeCode());
		_out_packet.setContent(list_builder.build().toByteArray());
		return _out_packet;
	}

}

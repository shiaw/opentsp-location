package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCommonCache;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCDeleteRegularData;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCInOrOutAreaNotifySetDel.InOrOutAreaNotifySetDel;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RPAnno(id="2508")
public class RP_2508_InOrOutAreaNotifySetDel extends RPCommand {

	@Autowired
	private RuleCommonCache ruleCommonCache;

	@Autowired
	private AreaCommonCache areaCommonCache;

	@Override
	public int processor(Packet packet) {
		InOrOutAreaNotifySetDel inOrOutAreaNotifySetDel;
		try {
			inOrOutAreaNotifySetDel = InOrOutAreaNotifySetDel.parseFrom(packet.getContent());
			List<Long> areaIds = inOrOutAreaNotifySetDel.getAreaIdentifysList();
			LCDeleteRegularData.DeleteRegularData.Builder delBuilder = LCDeleteRegularData.DeleteRegularData.newBuilder();
			delBuilder.setTerminalId(0);
			delBuilder.setRegularCode(LCRegularCode.RegularCode.inOrOutAreaNotifySetPara);
			for(Long areaId : areaIds){
				//删除的是多个个区域
				areaCommonCache.removeAreaEntity(areaId);
				ruleCommonCache.removeRuleEntityByAreaId(170190, areaId);
				delBuilder.addRegularIdentify(areaId);
			}
			Packet _out_packet = new Packet(true);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setCommand(AllCommands.DataAccess.DeleteRegularData_VALUE);
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setContent(delBuilder.build().toByteArray());
			int result= super.writeToDataAccess(_out_packet);
			if(result==0){
				this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.failure_VALUE);
			}else{
				this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
			}
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return 0;
	}

}

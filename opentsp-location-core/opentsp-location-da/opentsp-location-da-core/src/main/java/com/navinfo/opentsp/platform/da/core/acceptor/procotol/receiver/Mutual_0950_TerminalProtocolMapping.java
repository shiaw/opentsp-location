package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.DictManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.DictManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalProtoMappingDBEntity;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalProtocolMapping;

import java.util.List;


@DaRmiNo(id = "0950")
public class Mutual_0950_TerminalProtocolMapping extends Dacommand {

	@Override
	public Packet processor(Packet packet) {
		LCTerminalProtocolMapping.TerminalProtocolMapping.Builder builder = LCTerminalProtocolMapping.TerminalProtocolMapping
				.newBuilder();
	
		DictManage dao = new DictManageImpl();
		List<LcTerminalProtoMappingDBEntity> terminalProtoMappings = dao
				.getByLogicCode(0);
		
		if (null != terminalProtoMappings) {
			builder.setSerialNumber(packet.getSerialNumber());
			builder.setResults(LCPlatformResponseResult.PlatformResponseResult.success);
			for (LcTerminalProtoMappingDBEntity entity : terminalProtoMappings) {
				LCTerminalProtocolMapping.TerminalProtocolMapping.MappingInfo.Builder mapping = LCTerminalProtocolMapping.TerminalProtocolMapping.MappingInfo.newBuilder();
				mapping.setInstructCode(entity.getDict_code());
				mapping.setMessageCode(entity.getMessage_code());
				mapping.setProtocolCode(entity.getLogic_code());
				builder.addInfos(mapping);
			}
			Packet _out_packet = new Packet(true);
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet
					.setCommand(LCAllCommands.AllCommands.DataAccess.TerminalProtocolMappingRes_VALUE);
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setTo(packet.getFrom());
			_out_packet.setFrom(NodeHelper.getNodeCode());
			_out_packet.setContent(builder.build().toByteArray());
			return  _out_packet;
		}

		return null;
	}

	public static void main(String[] args) {
		LCTerminalProtocolMapping.TerminalProtocolMapping.Builder builder = LCTerminalProtocolMapping.TerminalProtocolMapping
				.newBuilder();
		DictManage dao = new DictManageImpl();
		List<LcTerminalProtoMappingDBEntity> terminalProtoMappings = dao
				.getByLogicCode(0);
		if (null != terminalProtoMappings) {
			for (LcTerminalProtoMappingDBEntity entity : terminalProtoMappings) {
				LCTerminalProtocolMapping.TerminalProtocolMapping.MappingInfo.Builder mapping = LCTerminalProtocolMapping.TerminalProtocolMapping.MappingInfo.newBuilder();
				mapping.setInstructCode(entity.getDict_code());
				mapping.setMessageCode(entity.getMessage_code());
				mapping.setProtocolCode(entity.getLogic_code());
				builder.addInfos(mapping);
			}
	}
		System.out.println(builder.build());
	}
}

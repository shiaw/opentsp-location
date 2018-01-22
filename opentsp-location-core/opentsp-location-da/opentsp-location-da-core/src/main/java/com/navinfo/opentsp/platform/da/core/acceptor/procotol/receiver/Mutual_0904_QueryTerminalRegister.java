package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.TerminalInfoManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.TerminalInfoManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRegisterDBEntity;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCQueryTerminalRegister;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalRegisterSave;

@DaRmiNo(id = "0904")
public class Mutual_0904_QueryTerminalRegister extends Dacommand {
	final static TerminalInfoManage terminalInfoManage = new TerminalInfoManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			LCQueryTerminalRegister.QueryTerminalRegister qtr = LCQueryTerminalRegister.QueryTerminalRegister
					.parseFrom(packet.getContent());
			LcTerminalRegisterDBEntity terminalRegister = terminalInfoManage
					.queryTerminalRegister(qtr.getTerminalId());
			return buildPacket(packet , terminalRegister);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Packet buildPacket(Packet packet , LcTerminalRegisterDBEntity terminalRegister) {
		LCTerminalRegisterSave.TerminalRegisterSave.Builder builder = LCTerminalRegisterSave.TerminalRegisterSave
				.newBuilder();
		if(terminalRegister!=null){
			builder.setAuthCode(terminalRegister.getAuth_code());
			builder.setCityIdentify(terminalRegister.getCity());
			builder.setLicense(terminalRegister.getLicense());
			builder.setLicenseColorCode(terminalRegister.getLicense_color());
			builder.setProduceCoding(terminalRegister.getProduct());
			builder.setProvinceIdentify(terminalRegister.getProvince());
			builder.setTerminalId(terminalRegister.getTerminal_id());
			builder.setTerminalModel(terminalRegister.getTerminal_type());
			builder.setTerminalNumber(terminalRegister.getTerminal_sn());
		}

		Packet _out_packet = new Packet(true);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setCommand(LCAllCommands.AllCommands.DataAccess.QueryTerminalRegisterRes_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setTo(packet.getFrom());
		_out_packet.setFrom(NodeHelper.getNodeCode());
		_out_packet.setContent(builder.build().toByteArray());
		return _out_packet;
	}
}

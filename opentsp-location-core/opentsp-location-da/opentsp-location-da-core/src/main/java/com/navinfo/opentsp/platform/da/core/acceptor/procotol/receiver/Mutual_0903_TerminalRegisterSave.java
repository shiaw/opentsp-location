package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.persistence.OptResult;
import com.navinfo.opentsp.platform.da.core.persistence.TerminalInfoManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.TerminalInfoManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRegisterDBEntity;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalRegisterSave;

@DaRmiNo(id = "0903")
public class Mutual_0903_TerminalRegisterSave extends Dacommand {
	final static TerminalInfoManage terminalInfoManage = new TerminalInfoManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			LCTerminalRegisterSave.TerminalRegisterSave trs= LCTerminalRegisterSave.TerminalRegisterSave.parseFrom(packet.getContent());
			LcTerminalRegisterDBEntity terminalRegister=new LcTerminalRegisterDBEntity();
			terminalRegister.setAuth_code(trs.getAuthCode());
			terminalRegister.setCity(trs.getCityIdentify());
			terminalRegister.setLicense(trs.getLicense());
			terminalRegister.setLicense_color(trs.getLicenseColorCode());
			terminalRegister.setProduct(trs.getProduceCoding());
			terminalRegister.setProvince(trs.getProvinceIdentify());
			terminalRegister.setTerminal_id(trs.getTerminalId());
			terminalRegister.setTerminal_sn(trs.getTerminalNumber());
			terminalRegister.setTerminal_type(trs.getTerminalModel());
			OptResult or =terminalInfoManage.saveTerminalRegister(terminalRegister);
			if(or.getStatus()>0){
				super.commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.success);
			}else{
				super.commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.failure);
				
			}
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
}

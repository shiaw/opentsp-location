package com.navinfo.opentsp.platform.dp.core.acceptor.da.receiver;


import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.acceptor.da.DACommand;
import com.navinfo.opentsp.platform.dp.core.cache.TerminalCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.TerminalEntity;
import com.navinfo.opentsp.platform.dp.core.handler.DAAnno;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo;

import javax.annotation.Resource;
@Deprecated
@DAAnno(id="0902")
public class DA_0902_NewTerminalInfo extends DACommand {

	@Resource
	private TerminalCache terminalCache;

	@Override
	public int processor(Packet packet) {
		try {
			LCTerminalInfo.TerminalInfo terminalInfo = LCTerminalInfo.TerminalInfo.parseFrom(packet
					.getContent());
			//新终端，无规则数据，可不请求
			TerminalEntity entity = new TerminalEntity();
			entity.setTerminalId(terminalInfo.getTerminalId());
//			entity.setTerminalProtocolCode(terminalInfo.getProtocolType());
//			// TODO 未使用
////			entity.setTaNodeCode(terminalInfo.getNodeCode());
////			entity.setAuthCode(terminalInfo.getAuthCode());
//			entity.setRegularInTerminal(terminalInfo.getRegularInTerminal());
			terminalCache.addTerminal(entity);
			
//			super.commonResponses(NodeHelper.getNodeUniqueMark(),
//					packet.getSerialNumber(), packet.getCommand(),
//					LCPlatformResponseResult.PlatformResponseResult.success, packet.getFrom());
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return 0;
	}

}

//package com.navinfo.opentsp.platform.dp.core.acceptor.receiver;
//
//import com.google.protobuf.InvalidProtocolBufferException;
//import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
//import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalRegisterSave.TerminalRegisterSave;
//import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalRegister.TerminalRegister;
//import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalRegisterRes.TerminalRegisterRes;
//import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalRegisterRes.TerminalRegisterRes.RegisterResult;
//import com.lc.dp.acceptor.tal.protocol.TACommand;
//import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
//import com.navinfo.opentsp.platform.location.kit.Convert;
//import com.navinfo.opentsp.platform.location.kit.LCConstant;
//import com.navinfo.opentsp.platform.location.kit.Packet;
//import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
//import com.navinfo.opentsp.platform.location.kit.lang.DateUtils.DateFormat;
//
//import java.util.Date;
//
//public class TA_3003_TerminalRegister extends TACommand {
//
//	@Override
//	public int processor(Packet packet) {
//		try {
//			String authCode = authCode(packet.getFrom());
//			TerminalRegister terminalReg = TerminalRegister.parseFrom(packet.getContent());
//			TerminalRegister.Builder _regBuilder = terminalReg.toBuilder();
//			_regBuilder.setAuthCoding(authCode);
//			packet.setContent(_regBuilder.build().toByteArray());
//			super.writeToRequestProcessing(packet);
//
//			TerminalRegisterSave.Builder builder = TerminalRegisterSave.newBuilder();
//			builder.setTerminalId(Convert.uniqueMarkToLong(packet.getUniqueMark()));
//			builder.setAuthCode(authCode);
//			builder.setProvinceIdentify(terminalReg.getProvinceIdentify());
//			builder.setCityIdentify(terminalReg.getCityIdentify());
//			builder.setProduceCoding(terminalReg.getProduceCoding());
//			builder.setTerminalModel(terminalReg.getTerminalModel());
//			builder.setTerminalNumber(terminalReg.getTerminalIdentify());
//			builder.setLicenseColorCode(terminalReg.getLicenseColorCode());
//			builder.setLicense(terminalReg.getLicense());
//
//			Packet _out_packet = new Packet(true);
//			_out_packet.setCommand(AllCommands.DataAccess.TerminalRegisterSave_VALUE);
//			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
//			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
//			_out_packet.setContent(builder.build().toByteArray());
//			int result = super.writeToDataAccess(_out_packet);
//
//			if(result == 1){
//				TerminalRegisterRes.Builder regBuilder = TerminalRegisterRes.newBuilder();
//				regBuilder.setAuthCoding(authCode);
//				regBuilder.setResults(RegisterResult.success);
//				regBuilder.setSerialNumber(packet.getSerialNumber());
//				Packet _out_packet_terminal = new Packet(true);
//				_out_packet_terminal.setCommand(AllCommands.Terminal.TerminalRegisterRes_VALUE);
//				_out_packet_terminal.setProtocol(LCConstant.LCMessageType.TERMINAL);
//				_out_packet_terminal.setUniqueMark(packet.getUniqueMark());
//				_out_packet_terminal.setTo(packet.getFrom());
//				_out_packet_terminal.setContent(regBuilder.build().toByteArray());
//				super.write(_out_packet_terminal);
//			}else{
//				// TODO 回复注册失败应答
//				TerminalRegisterRes.Builder regBuilder = TerminalRegisterRes.newBuilder();
//				regBuilder.setResults(RegisterResult.vehicleNotRegister);
//				regBuilder.setSerialNumber(packet.getSerialNumber());
//				Packet _out_packet_terminal = new Packet(true);
//				_out_packet_terminal.setCommand(AllCommands.Terminal.TerminalRegisterRes_VALUE);
//				_out_packet_terminal.setProtocol(LCConstant.LCMessageType.TERMINAL);
//				_out_packet_terminal.setUniqueMark(packet.getUniqueMark());
//				_out_packet_terminal.setTo(packet.getFrom());
//				_out_packet_terminal.setContent(regBuilder.build().toByteArray());
//				super.write(_out_packet_terminal);
//			}
//		} catch (InvalidProtocolBufferException e) {
//			log.error(e.getMessage());
//			e.printStackTrace();
//		}
//		return 0;
//	}
//	static String authCode(long terminalId){
//		String time  = DateUtils.format(new Date(), DateFormat.YYYYMMDDHHMMSS).substring(6);
//		return time+""+terminalId;
//	}
//
//}

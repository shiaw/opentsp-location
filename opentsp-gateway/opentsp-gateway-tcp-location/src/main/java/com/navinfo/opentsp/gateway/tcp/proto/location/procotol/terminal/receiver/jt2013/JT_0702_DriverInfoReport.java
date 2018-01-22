package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver.jt2013;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.StringUtils;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCDriverInfoReportSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCDriverInfoReport;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@LocationCommand(id = "0702",version = "200110")
@Component("JT_0702_DriverInfoReport_200110")
public class JT_0702_DriverInfoReport extends TerminalCommand {
	
	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		
		
		byte[] content = packet.getContent();
		long status =  Convert.byte2Long(ArraysUtils.subarrays(content, 0, 1), 1);
		boolean cardStatus = status == 1 ? true : false;
		
		String timeStr = "20"+Convert.bytesToHexString(ArraysUtils.subarrays(content, 1, 6));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		long cardSwitchDate = System.currentTimeMillis()/1000;
		try {
			cardSwitchDate = sdf.parse(timeStr).getTime()/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int results = (int)Convert.byte2Long(ArraysUtils.subarrays(content, 7, 1), 1);
		int n = (int)Convert.byte2Long(ArraysUtils.subarrays(content, 8, 1),1); 
		String name = StringUtils.bytesToGbkString(ArraysUtils.subarrays(content, 9, n));
		String certificateCode = StringUtils.bytesToGbkString(ArraysUtils.subarrays(content, 9 + n, 20));
		int m = (int)Convert.byte2Long(ArraysUtils.subarrays(content, 29+n, 1), 1);
		String organizationName = StringUtils.bytesToGbkString(ArraysUtils.subarrays(content, 30+n, m));
		
		String dateStr = Convert.bytesToHexString(ArraysUtils.subarrays(content, 30+n+m, 4));
		long licenseValidDate = System.currentTimeMillis()/1000;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		try {
			licenseValidDate = sdf1.parse(dateStr).getTime()/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		LCDriverInfoReport.DriverInfoReport.Builder builder = 
				LCDriverInfoReport.DriverInfoReport.newBuilder();
		builder.setCardStatus(cardStatus);
		builder.setCardSwitchDate(cardSwitchDate);
		builder.setResults(LCDriverInfoReport.DriverInfoReport.ReadResult.valueOf(results));
		builder.setName(name);
		builder.setCertificateCode(certificateCode);
		builder.setOrganizationName(organizationName);
		builder.setLicenseValidDate(licenseValidDate);
		
		Packet _out_packet = new Packet();
		_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.DriverInfoReport_VALUE);
		int c=0;
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());
		//super.writeToDataProcessing(_out_packet);
		
		
		Packet _out_packet_da = new Packet(true);
		_out_packet_da.setCommand(LCAllCommands.AllCommands.DataAccess.DriverInfoReportSave_VALUE);
		_out_packet_da.setProtocol(LCConstant.LCMessageType.PLATFORM);
		//_out_packet_da.setUniqueMark(NodeHelper.getNodeUniqueMark());
		//_out_packet_da.setFrom(NodeHelper.getNodeCode());
		LCDriverInfoReportSave.DriverInfoReportSave.Builder builder2 = LCDriverInfoReportSave.DriverInfoReportSave.newBuilder();
		builder2.setTerminalId(packet.getFrom());
		builder2.setInfo(builder);
		_out_packet_da.setContent(builder2.build().toByteArray());
	//	super.writeToDataAccess(_out_packet_da);
		
		log.info("收到驾驶员身份信息：状态："+status+",时间："+cardSwitchDate+",读取结果："+results+
				",驾驶员："+name+",从业资格证编码："+certificateCode+",发证机构:"+organizationName+
				",有效期："+licenseValidDate);
		
		return null;
	}
	
	
}

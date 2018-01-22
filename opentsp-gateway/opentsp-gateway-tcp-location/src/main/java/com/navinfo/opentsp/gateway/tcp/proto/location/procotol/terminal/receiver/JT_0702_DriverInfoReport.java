package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCDriverInfoReport;

import java.io.ByteArrayOutputStream;

@LocationCommand(id = "0702",version = "200100")
public class JT_0702_DriverInfoReport extends TerminalCommand {
	
	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
//		byte[] content = packet.getContent();
		String content = Convert.bytesToHexString(packet.getContent());

		int nameLength = Integer.parseInt(content.substring(0, 2), 16) * 2;
		String name = "";
		if(nameLength>0){
			name = hexToGbk(content.substring(2, 2 + nameLength));
		}
		String idCard = hexToGbk(content.substring(2 + nameLength,
				2 + nameLength + 40));
		String practitionersCard = hexToGbk(content.substring(
				2 + nameLength + 40, 2 + nameLength + 40 + 80));
		int organizationLength = Integer.parseInt(
				content.substring(2 + nameLength + 40 + 80, 2 + nameLength + 40
						+ 80 + 2), 16);
		String organization = "306";
		if(organizationLength > 0){
//			organization = hexToGbk(content.substring(2 + 2
//					+ nameLength + 40 + 80, 2 + 2 + nameLength + 40 + 80
//					+ organizationLength));
		}
		
		log.info("收到终端【" + packet.getUniqueMark() + "】驾驶员身份信息上传信息：驾驶员姓名长度【" + nameLength
				+ "】,驾驶员姓名【" + name + "】,驾驶员身份证编码【" + idCard + "】,从业资格证编码【"
				+ practitionersCard + "】,发证机构名称长度【" + organizationLength
				+ "】,发证机构名称【" + organization + "】.");

//		int results = (int)Convert.byte2Long(ArraysUtils.subarrays(content, 7, 1), 1);
//		int n = (int)Convert.byte2Long(ArraysUtils.subarrays(content, 8, 1),1); 
//		String name = StringUtils.bytesToGbkString(ArraysUtils.subarrays(content, 9, n));
//		String certificateCode = StringUtils.bytesToGbkString(ArraysUtils.subarrays(content, 9+n, 20));
//		int m = (int)Convert.byte2Long(ArraysUtils.subarrays(content, 29+n, 1), 1);
//		String organizationName = StringUtils.bytesToGbkString(ArraysUtils.subarrays(content, 30+n, m));
//		
//		String dateStr = Convert.bytesToHexString(ArraysUtils.subarrays(content, 30+n+m, 4));
//		long licenseValidDate = System.currentTimeMillis()/1000;
//		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
//		try {
//			licenseValidDate = sdf1.parse(dateStr).getTime()/1000;
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		
		LCDriverInfoReport.DriverInfoReport.Builder builder =
				LCDriverInfoReport.DriverInfoReport.newBuilder();
		//builder.setCardStatus(cardStatus);
		//builder.setCardSwitchDate(cardSwitchDate);
		//builder.setResults(LCDriverInfoReport.DriverInfoReport.ReadResult.valueOf(results));
		builder.setName(name);
		builder.setCertificateCode(practitionersCard);
		builder.setOrganizationName(organization);
		//builder.setLicenseValidDate(licenseValidDate);
	
		Packet _out_packet = new Packet();
		_out_packet.setCommand(AllCommands.Terminal.DriverInfoReport_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());

	//	super.writeKafKaToDP(_out_packet, TopicConstants.JT_0702_DriverInfoReport);
//		
//		
//		Packet _out_packet_da = new Packet(true);
//		_out_packet_da.setCommand(AllCommands.DataAccess.DriverInfoReportSave_VALUE);
//		_out_packet_da.setProtocol(LCMessageType.PLATFORM);
//		_out_packet_da.setUniqueMark(NodeHelper.getNodeUniqueMark());
//		_out_packet_da.setFrom(NodeHelper.getNodeCode());
//		DriverInfoReportSave.Builder builder2 = DriverInfoReportSave.newBuilder();
//		builder2.setTerminalId(packet.getFrom());
//		builder2.setInfo(builder);
//		_out_packet_da.setContent(builder2.build().toByteArray());
//		super.writeToDataAccess(_out_packet_da);
//		
//		log.info("收到驾驶员身份信息：状态："+status+",时间："+cardSwitchDate+",读取结果："+results+
//				",驾驶员："+name+",从业资格证编码："+certificateCode+",发证机构:"+organizationName+
//				",有效期："+licenseValidDate);
	    PacketResult packetResult=new PacketResult();
		packetResult.setKafkaPacket(_out_packet);
		return  packetResult;//0;
	}
	
	public static void main(String[] args) {
		String content = "0CCFC4BFA5000000000000000033323132303131393930303432363030313500003132333435363738393031323334353637383900000000000000000000000000000000000000000036FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";

		int nameLength = Integer.parseInt(content.substring(0, 2), 16) * 2;
		String name = "";
		if(nameLength>0){
			name = hexToGbk("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF0000");
		}
		System.err.println("name:"+name);
		String idCard = hexToGbk(content.substring(2 + nameLength,
				2 + nameLength + 40));
		String practitionersCard = hexToGbk(content.substring(
				2 + nameLength + 40, 2 + nameLength + 40 + 80));
		int organizationLength = Integer.parseInt(
				content.substring(2 + nameLength + 40 + 80, 2 + nameLength + 40
						+ 80 + 2), 16);
		String organization = "";
		if(organizationLength > 0){
			organization = hexToGbk(content.substring(2 + 2
					+ nameLength + 40 + 80, 2 + 2 + nameLength + 40 + 80
					+ organizationLength));
		}
		//practitionersCard = Convert.fillZero(Long.parseLong(practitionersCard.substring(2),16), 10);

		log.info("收到终端,驾驶员身份信息上传信息：驾驶员姓名长度【" + nameLength
				+ "】,驾驶员姓名【" + name + "】,驾驶员身份证编码【" + idCard + "】,从业资格证编码【"
				+ practitionersCard + "】,发证机构名称长度【" + organizationLength
				+ "】,发证机构名称【" + organization + "】.");
	}
	/**
	 * 十六进制转GBK <br>
	 * mixHex2str
	 * 
	 * @param bytes
	 *            String 十六进制字符串
	 * @return String
	 */
	public static String hexToGbk(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		String opr = null;
		for (int i = 0; i < bytes.length(); i += 2) {
			if (bytes.charAt(i) == '0' && bytes.charAt(i + 1) == '0') {
                break;
            }
			baos.write(("0123456789ABCDEF".indexOf(bytes.charAt(i)) << 4 | "0123456789ABCDEF"
					.indexOf(bytes.charAt(i + 1))));
		}
		try {
			opr = new String(baos.toByteArray(), "GBK");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (bytes.charAt(0) == '0' && bytes.charAt(1) == '0') {
            opr = "";
        }
		return opr;
	}
}

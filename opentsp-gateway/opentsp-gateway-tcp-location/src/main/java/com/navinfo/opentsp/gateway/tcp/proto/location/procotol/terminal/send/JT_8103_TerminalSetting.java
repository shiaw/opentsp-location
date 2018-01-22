package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;


import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;

import java.util.HashMap;
import java.util.Map;
@LocationCommand(id = "8103")
public class JT_8103_TerminalSetting extends TerminalCommand {
	@Override
	public  PacketResult processor(NettyClientConnection connection,Packet packet) {
		//this.write(packet);
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(packet);
		return packetResult;
	}

	public int processor1(Packet packet) {
//		try {
//			lcterminal.TerminalSetting terminalSetting = LCTerminalSetting.TerminalSetting
//					.parseFrom(packet.getContent());
//			List<SettingObject> objects = terminalSetting.getSettingList();
//			// 因String类型的不可确定长度因素,不采用动态填充content内容
//			byte[] content = Convert.longTobytes(objects.size(), 1);
//			for (SettingObject setting : objects) {
//				byte[] bytes = this.object2byte(setting);
//				content = ArraysUtils.arraycopy(content, bytes);
//			}
//			Packet outpacket = new Packet();
//			outpacket.setCommand(Constant.JTProtocol.SetTerminalParameters);
//			outpacket.setSerialNumber(packet.getSerialNumber());
//			outpacket.setUniqueMark(packet.getUniqueMark());
//			outpacket.setContent(content);
//			this.write(outpacket);
//		} catch (InvalidProtocolBufferException e) {
//			e.printStackTrace();
//		}

		return 0;
	}

//	// 将SettingObject对象转换成字节数组
//	private byte[] object2byte(SettingObject object) {
//		byte[] id = Convert.longTobytes(object.getSetId().getNumber(), 4);
//		if (object.hasIntParaContent()) {
//			long value = object.getIntParaContent();
//			byte[] valueByte = Convert.longTobytes(value,
//					paramterType.get(object.getSetId().getNumber()));
//			byte[] valueLenghtByte = Convert.longTobytes(
//					paramterType.get(object.getSetId().getNumber()), 1);
//			byte[] result = new byte[4 + 1 + paramterType.get(object.getSetId()
//					.getNumber())];
//			ArraysUtils.arrayappend(result, 0, id);
//			ArraysUtils.arrayappend(result, 4, valueLenghtByte);
//			ArraysUtils.arrayappend(result, 5, valueByte);
//			return result;
//		} else if (object.hasStrParaContent()) {
//			String value = object.getStrParaContent();
//			try {
//				byte[] valueByte = value.getBytes("GBK");
//				byte[] valueLengthByte = Convert.longTobytes(valueByte.length,
//						1);
//				byte[] result = new byte[4 + 1 + paramterType.get(object
//						.getSetId().getNumber())];
//				ArraysUtils.arrayappend(result, 0, id);
//				ArraysUtils.arrayappend(result, 4, valueLengthByte);
//				ArraysUtils.arrayappend(result, 5, valueByte);
//				return result;
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}

	// 参数类型
	private static Map<Integer, Integer> paramterType = new HashMap<Integer, Integer>();
	static {
		paramterType.put(0x0001, 4);
		paramterType.put(0x0002, 4);
		paramterType.put(0x0003, 4);
		paramterType.put(0x0004, 4);
		paramterType.put(0x0005, 4);
		paramterType.put(0x0006, 4);
		paramterType.put(0x0007, 4);

		// paramterType.put(0x0008,4);
		// paramterType.put(0x0009,4);
		// paramterType.put(0x000A,4);
		// paramterType.put(0x000B,4);
		// paramterType.put(0x000C,4);
		// paramterType.put(0x000D,4);
		// paramterType.put(0x000E,4);
		// paramterType.put(0x000F,4);

		// paramterType.put(0x0010,4);
		// paramterType.put(0x0011,4);
		// paramterType.put(0x0012,4);
		// paramterType.put(0x0013,4);
		// paramterType.put(0x0014,4);
		// paramterType.put(0x0015,4);
		// paramterType.put(0x0016,4);
		// paramterType.put(0x0017,4);

		paramterType.put(0x0018, 4);
		paramterType.put(0x0019, 4);
		// paramterType.put(0x001A,4);
		// paramterType.put(0x001B,4);
		// paramterType.put(0x001C,4);
		// paramterType.put(0x001D,4);
		// paramterType.put(0x001E,4);
		// paramterType.put(0x001F,4);
		paramterType.put(0x0020, 4);
		paramterType.put(0x0021, 4);
		paramterType.put(0x0022, 4);
		// paramterType.put(0x0023,4);
		// paramterType.put(0x0024,4);
		// paramterType.put(0x0025,4);
		// paramterType.put(0x0026,4);
		paramterType.put(0x0027, 4);
		paramterType.put(0x0028, 4);
		paramterType.put(0x0029, 4);
		// paramterType.put(0x002A,4);
		// paramterType.put(0x002B,4);
		paramterType.put(0x002C, 4);
		paramterType.put(0x002D, 4);
		paramterType.put(0x002E, 4);
		paramterType.put(0x002F, 4);
		paramterType.put(0x0030, 4);
		// paramterType.put(0x0031,4);
		// paramterType.put(0x0032,4);
		// paramterType.put(0x0033,4);
		// paramterType.put(0x0034,4);
		// paramterType.put(0x0035,4);
		// paramterType.put(0x0036,4);
		// paramterType.put(0x0037,4);
		// paramterType.put(0x0038,4);
		// paramterType.put(0x0039,4);
		// paramterType.put(0x003A,4);
		// paramterType.put(0x003B,4);
		// paramterType.put(0x003C,4);
		// paramterType.put(0x003D,4);
		// paramterType.put(0x003E,4);
		// paramterType.put(0x003F,4);
		// paramterType.put(0x0040,4);
		// paramterType.put(0x0041,4);
		// paramterType.put(0x0042,4);
		// paramterType.put(0x0043,4);
		// paramterType.put(0x0044,4);
		paramterType.put(0x0045, 4);
		paramterType.put(0x0046, 4);
		paramterType.put(0x0047, 4);
		// paramterType.put(0x0048,4);
		// paramterType.put(0x0049,4);
		// paramterType.put(0x004A,4);
		// paramterType.put(0x004B,4);
		// paramterType.put(0x004C,4);
		// paramterType.put(0x004D,4);
		// paramterType.put(0x004E,4);
		// paramterType.put(0x004F,4);
		paramterType.put(0x0050, 4);
		paramterType.put(0x0051, 4);
		paramterType.put(0x0052, 4);
		paramterType.put(0x0053, 4);
		paramterType.put(0x0054, 4);
		paramterType.put(0x0055, 4);
		paramterType.put(0x0056, 4);
		paramterType.put(0x0057, 4);
		paramterType.put(0x0058, 4);
		paramterType.put(0x0059, 4);
		paramterType.put(0x005A, 4);
		// paramterType.put(0x005B,4);
		// paramterType.put(0x005C,4);
		// paramterType.put(0x005D,4);
		// paramterType.put(0x005E,4);
		// paramterType.put(0x005F,4);
		// paramterType.put(0x0060,4);
		// paramterType.put(0x0061,4);
		// paramterType.put(0x0062,4);
		// paramterType.put(0x0063,4);
		// paramterType.put(0x0064,4);
		// paramterType.put(0x0065,4);
		// paramterType.put(0x0066,4);
		// paramterType.put(0x0067,4);
		// paramterType.put(0x0068,4);
		// paramterType.put(0x0069,4);
		// paramterType.put(0x006A,4);
		// paramterType.put(0x006B,4);
		// paramterType.put(0x006C,4);
		// paramterType.put(0x006D,4);
		// paramterType.put(0x006E,4);
		// paramterType.put(0x006F,4);
		paramterType.put(0x0070, 4);
		paramterType.put(0x0071, 4);
		paramterType.put(0x0072, 4);
		paramterType.put(0x0073, 4);
		paramterType.put(0x0074, 4);
		// paramterType.put(0x0075,4);
		// paramterType.put(0x0076,4);
		// paramterType.put(0x0077,4);
		// paramterType.put(0x0078,4);
		// paramterType.put(0x0079,4);
		// paramterType.put(0x007A,4);
		// paramterType.put(0x007B,4);
		// paramterType.put(0x007C,4);
		// paramterType.put(0x007D,4);
		// paramterType.put(0x007E,4);
		// paramterType.put(0x007F,4);
		paramterType.put(0x0080, 4);
		paramterType.put(0x0081, 2);
		paramterType.put(0x0082, 2);
		// paramterType.put(0x0083,4);
		paramterType.put(0x0084, 1);
	}
}

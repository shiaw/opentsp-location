package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit;

/**
 * 位置云平台常量
 * 
 * @author lgw
 * 
 */
public final class LCConstant {
	/** 消息类型 */
	public class LCMessageType {
		/** 终端交互 */
		public final static int TERMINAL = 0x02;
	}

	public static final int PACKET_MAX_LENGTH = 1023;//Integer.MAX_VALUE;
	public static byte pkBegin = 0x7E;
	public static byte pkEnd = 0x7B;
	public static final byte[] ESCAPE_BYTE = new byte[] { 0x7E, 0x7D, 0x7B, 0x7A };
	public static final byte[][] TO_ESCAPE_BYTE = new byte[][] { { 0x7D, 0x02 },
			{ 0x7D, 0x01 }, { 0x7A, 0x02 }, { 0x7A, 0x01 } };
	
	public static final int TERMINAL_PACKET_MAX_LENGTH_2011 = 1023;
	public static byte TERMINAL_PK_END_2011 = 0x7E;
	
	public static byte[] TERMINAL_ESCAPE_2011 =new byte[] { 0x7e, 0x7d};
	public static byte[][] TERMINAL_TO_ESCAPE_2011 = new byte[][] { { 0x7D, 0x02 },
		{ 0x7D, 0x01 }};
	
}

package com.navinfo.opentsp.platform.location.kit;

/**
 * 位置云平台常量
 * 
 * @author lgw
 * 
 */
public final class LCConstant {
	public final static class ServerStatusCode{
		/***/
		public final static int create = 0x01;
		/***/
		public final static int open = 0x01;
		/***/
		public final static int close = 0x01;
		/***/
		public final static int success = 0x01;
		/***/
		public final static int failure = 0x01;
		/***/
		public static final int ready = 1;
		/***/
		public static final int work = 2;
		
	}
	/** 消息类型 */
	public class LCMessageType {
		/** 平台交互 */
		public final static int PLATFORM = 0x01;
		/** 终端交互 */
		public final static int TERMINAL = 0x02;
		/** 原始数据 */
		public final static int ORIGINAL = 0x03;
		/** 统计数据 */
		public final static int STATISTICS = 0x04;
	}
	/**
	 * 终端协议类型
	 * @author lgw
	 *
	 */
	public class TerminalProtocolType{
		public final static int JT_2011 = 200100;
		public final static int JT_2013 = 200110;
		public final static int XW = 200200;
		public final static int YD = 200300;
	}
	/**
	 * 约定值 
	 * @author Administrator
	 *
	 */
	public class AgreedValue{
		/** 最小优化距离 : 位置数据汇报频率为30S/条，最长应该不会超过10分钟，所以设定车辆距离服务站<20KM时，不在优化范围内。*/
		public final static  double minDistance=20000;//	单位 M
		/** 平均速度：设定为80KM/H。*/
		public final static  double averageSpeed = 22;//平均速度：设定为80KM/H ,即 22m/s
	}
	/**
	 * 流量方向
	 * @author lgw
	 *
	 */
	public static enum FlowDirection{
		Up,Down
	}
	public static enum MasterSlave {
		Slave, Master
	}
	public static final int PACKET_MAX_LENGTH = 1023;//Integer.MAX_VALUE;
	public static byte pkBegin = 0x7E;
	public static byte pkEnd = 0x7B;
	public static final byte[] escapeByte = new byte[] { 0x7E, 0x7D, 0x7B, 0x7A };
	public static final byte[][] toEscapeByte = new byte[][] { { 0x7D, 0x02 },
			{ 0x7D, 0x01 }, { 0x7A, 0x02 }, { 0x7A, 0x01 } };
	
	public static final int TERMINAL_PACKET_MAX_LENGTH_2011 = 1023;
	public static byte TERMINAL_PK_BEGIN_2011 = 0x7E;
	public static byte TERMINAL_PK_END_2011 = 0x7E;
	
	public static byte[] TERMINAL_ESCAPE_2011 =new byte[] { 0x7e, 0x7d};
	public static byte[][] TERMINAL_TO_ESCAPE_2011 = new byte[][] { { 0x7D, 0x02 },
		{ 0x7D, 0x01 }};
	
}

package com.navinfo.opentsp.platform.location.kit.servicemonitor.mode;

import java.text.NumberFormat;


public class MonitorUtil {
	/**
	 * 设置浮点数的精度.<br>
	 * 
	 * retainPrecision(5.12345678, 3) --> "5.123"<br>
	 * retainPrecision(5.12345678, 4) --> "5.1235"<br>
	 * retainPrecision(5, 2) --> "5.00"<br>
	 * 
	 * @param d
	 *            浮点数.
	 * @param b
	 *            需要保留的小数部分位数.
	 * @return 截取(或补全)后的字符串表示.
	 */
	public static String retainPrecision(double d, int b) {
		// 注意setMaximumFractionDigits(int)有一个小bug(由于Java基本类型自身的精度导致),不是真正的四舍五入.
		// 比如 retainPrecision(5.12345, 4) --> "5.1234";
		// 而 retainPrecision(5.123451, 4) --> "5.1235".
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumFractionDigits(2); // 最少也给2位小数吧
		nf.setMaximumFractionDigits(b);
		return nf.format(d);
	}

	/**
	 * 用于格式化频率(保留2位小数),大于1GHz的话使用GHz为单位(1GHz=1000MHz),否则使用MHz为单位.<br>
	 * 
	 * 988MHz --> "988MHz"<br>
	 * 2885MHz --> "2.88GHz"<br>
	 * 3006MHz --> "3.01GHz"
	 * 
	 * @param mhz
	 *            CPU频率,以MHz为单位.
	 * @return 格式化后的CPU频率.
	 */
	public static String getCpuHz(int mhz) {
		if (mhz >= 1000) {
			return retainPrecision(mhz / 1000.0, 2) + "GHz";
		} else
			return Integer.toString(mhz) + "MHz";
	}

	/**
	 * 用于格式化内存(保留2位小数),大于1G的话使用G为单位(1G=1024M),否则使用M为单位.<br>
	 * 
	 * @param size
	 *            内存大小,以byte为单位.将除以1024<sup>2</sup>,byte->KB->MB
	 * @return 格式化后的内存大小.
	 */
	public static String getMemSize(long size) {
		long mb = size >> 20; // 除以1024的平方
		if (mb >= 1024) {
			return retainPrecision(mb / 1024.0, 2) + "G";
		} else
			return Long.toString(mb) + "M";
	}

	/**
	 * 用于格式化显示硬盘分区空间(保留2位小数),大于1G的话使用GB为单位(1GB=1024MB),否则使用MB为单位.<br>
	 * 
	 * @param size
	 *            分区大小,以byte为单位.将除以1024<sup>2</sup>,byte->KB->MB
	 * @return 格式化后的分区大小.
	 */
	public static String getDiskSize(long size) {
		long mb = size >> 20; // 除以1024的平方
		if (mb >= 1024) {
			return retainPrecision(mb / 1024.0, 2) + "GB";
		} else
			return Long.toString(mb) + "MB";
	}

	/**
	 * 用于格式化显示硬盘分区空间(保留2位小数),大于1G的话使用GB为单位(1GB=1024MB),否则使用MB为单位.<br>
	 * 
	 * @param size
	 *            分区大小,以byte为单位.将除以1024,KB->MB
	 * @return 格式化后的分区大小.
	 */
	public static String getDiskSizeKB(long kbsize) {
		long mb = kbsize >> 10; // 除以1024
		if (mb >= 1024) {
			return retainPrecision(mb / 1024.0, 2) + "GB";
		} else
			return Long.toString(mb) + "MB";
	}
	
	/**
	 * 用以给多核CPU设置通俗名称.<br>
	 * 单核: Single-Core<br>
	 * 双核: Dual-Core<br>
	 * 四核: Quad-Core<br>
	 * 八核及以上: Multi[<i>n</i>]-Core
	 * 
	 * @param i
	 *            物理CPU数
	 * @return
	 */
	public static String coreConvension(int i) {
		String flag = "";
		switch (i) {
		case 1:
			flag = "Single-Core";
			break;
		case 2:
			flag = "Dual-Core";
			break;
		case 4:
			flag = "Quad-Core";
			break;
		default:
			flag = "Multi[" + Integer.toString(i) + "]-Core";
			break;
		}
		return flag;
	}
}

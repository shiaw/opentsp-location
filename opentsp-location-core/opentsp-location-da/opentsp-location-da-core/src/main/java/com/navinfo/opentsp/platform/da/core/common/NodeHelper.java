package com.navinfo.opentsp.platform.da.core.common;


import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCNodeStatusReport;

public class NodeHelper {
	private static int nodeCode = Configuration.getInt("NODE.CODE");
	private static String nodeUniqueMark = Convert.fillZero(Configuration.getConfig("NODE.CODE"), 12);
	public static LCNodeStatusReport.NodeStatusReport.NodeStatus nodeStatus = LCNodeStatusReport.NodeStatusReport.NodeStatus.failed;
	/**
	 * 获取自身节点标识
	 *
	 * @return
	 */
	public static String getNodeUniqueMark() {
		return nodeUniqueMark;
	}
	/**
	 * 获取自身节点编号
	 * @return
	 */
	public static int getNodeCode(){
		return nodeCode;
	}
}

package com.navinfo.opentsp.platform.dp.core.common;

import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCNodeStatusReport.NodeStatusReport.NodeStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NodeHelper {

	@Value("${dp.node.code:1000}")
	private static int nodeCode;

	private static String nodeUniqueMark = Convert.fillZero(nodeCode, 12);
	public static NodeStatus nodeStatus = NodeStatus.failed;
	private static long _system_current_time = System.currentTimeMillis() / 1000;
	
	public static int TA3002Num = 0;
	public static int RP3002Num = 0;
	
	static{
		Thread updateTime = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					_system_current_time = System.currentTimeMillis() / 1000;
					
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		updateTime.setName("update system time");
		updateTime.start();
		
	}
	/**
	 * 获取节点标识
	 * 
	 * @return
	 */
	public static String getNodeUniqueMark() {
		return nodeUniqueMark;
	}
	public static int getNodeCode(){
		return nodeCode;
	}
	public static long getCurrentTime(){
		return _system_current_time;
	}
}

package com.navinfo.opentsp.platform.da.core.connector.mm;

import com.navinfo.opentsp.platform.da.core.cache.InstanceCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 不断偿试重连MM节点,以ReconnectDelayInterval秒倍数延迟间隔.直到重连成功,结束循环
 *
 * @author HK
 *
 */
public class MMReconnect {
	private static final Logger log = LoggerFactory.getLogger(MMReconnect.class);
	public static synchronized void reconnect(final int nodeCode) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 从缓存当中获取服务实例
				MMMutualClient mmMutualClient = (MMMutualClient) InstanceCache.getInstance(nodeCode);
				while(true){
					int number = mmMutualClient.reconnector();
					if (number == 0) {
						log.info("da connecting mm successful.");
						return;
					}else{
						try {
							log.info("da connecting mm failed, connecting [ " + number + " ] times");
							Thread.sleep(mmMutualClient.getReconnectDelayInterval() * 1000);
						} catch (InterruptedException e) {
							log.info("da connecting mm sleep exception" + e.getMessage());
						}
					}
				}
			}
		}).start();
	}
}

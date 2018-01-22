package com.navinfo.opentsp.platform.da.core.connector.mm.procotol.send;


import com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;

public class MM_1735_DataTransferCompletionNotice extends MMCommand {
	@Override
	public int processor(Packet packet) {
		int result = -1;
		while(true){
			result = this.write(packet);
			if(0 == result){
				logger.info("发送[转存成功通知]成功");
				break;
			}else{
				logger.error("发送[转存成功通知]失败，5s后重发");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

}

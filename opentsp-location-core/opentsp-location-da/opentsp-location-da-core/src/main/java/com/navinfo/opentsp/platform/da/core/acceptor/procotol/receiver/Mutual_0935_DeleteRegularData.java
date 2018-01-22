package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.persistence.RegularDataAreaAndDataManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.RegularDataAreaAndDataManageImpl;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCDeleteRegularData;

import java.util.List;



/**
 * 删除规则，删除规则的时候会判断是否删除相应区域
 * @author Administrator
 *
 */
@DaRmiNo(id = "0935")
public class Mutual_0935_DeleteRegularData extends Dacommand {
	final static RegularDataAreaAndDataManage manager = new RegularDataAreaAndDataManageImpl();


	@Override
	public Packet processor(Packet packet) {
		try {
			LCDeleteRegularData.DeleteRegularData drd=LCDeleteRegularData.DeleteRegularData.parseFrom(packet.getContent());
			long terminalId=drd.getTerminalId();//终端标识
			int regularCode=drd.getRegularCode().getNumber();//规则类型编码
			//规则数据识别
			List<Long> regularIdentifyList= drd.getRegularIdentifyList();//标识指的是区域、路线、禁驾等标识字段，主要有上层业务系统提供。
			manager.deleteRegular(terminalId, regularCode,regularIdentifyList);
			super.commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.success);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			super.commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.failure);
		}
		return null;
	}
}

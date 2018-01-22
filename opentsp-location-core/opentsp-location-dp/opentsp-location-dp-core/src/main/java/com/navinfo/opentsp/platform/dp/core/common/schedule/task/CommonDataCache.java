package com.navinfo.opentsp.platform.dp.core.common.schedule.task;

import com.navinfo.opentsp.platform.dp.core.acceptor.da.receiver.DA_0910_GetDictDataRes;
import com.navinfo.opentsp.platform.dp.core.common.RmiConstant;
import com.navinfo.opentsp.platform.dp.core.rmi.DaRmiService;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDictType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCGetDictData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Configurable
@EnableScheduling
public class CommonDataCache{

	@Autowired
	private DaRmiService daRmiService;// rmi调用通用类

	@Autowired
	private DA_0910_GetDictDataRes da_1910_GetDictDataRes;// 区域检索

	public static Logger logger = LoggerFactory.getLogger(CommonDataCache.class);

	@Scheduled(cron = "0 0/5 * * * ?")
	public void run() {
		try {
			
			//字典数据
			LCGetDictData.GetDictData.Builder getDictDataBuilder = LCGetDictData.GetDictData.newBuilder();
			getDictDataBuilder.addDictType(LCDictType.DictType.commonRuleCode);

			Packet _0910_GetDic_packet_ = new Packet(true);
			_0910_GetDic_packet_.setCommand(LCAllCommands.AllCommands.DataAccess.GetDictData_VALUE);
//			_0910_GetDic_packet_.setUniqueMark(NodeHelper.getNodeUniqueMark());
//			_0910_GetDic_packet_.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_0910_GetDic_packet_.setContent(getDictDataBuilder.build().toByteArray());

			Packet packet =
					daRmiService.callRmi(RmiConstant.RMI_SERVICE_ID,
							RmiConstant.RMI_INTERFACE_NAME,
							RmiConstant.RmiInterFaceEum.DaRmiInterface.getClassType(),
							_0910_GetDic_packet_);

			if (packet != null)
			{
				// 初始化缓存
				da_1910_GetDictDataRes.processor(packet);

			}
			else
			{
				logger.info("定时任务无法从DA获取数据字典");
			}
			//DAMutualCommandFacotry.processor(_0910_GetDic_packet_);
//			System.out.println("end   0910 字典数据获取任务  CommonDataCache ===========================================");
		} catch (Exception e) {
			logger.error("获取数据字典定时任务出错！");
		}
	}

}

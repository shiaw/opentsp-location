package com.navinfo.opentsp.platform.da.core.common.schedule.task;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import  com.navinfo.opentsp.platform.da.core.common.Constant;
import  com.navinfo.opentsp.platform.da.core.webService.manage.DynamicPassword;
import  com.navinfo.opentsp.platform.da.core.webService.manage.DynamicPasswordCache;

/**
 * 处理队列数据
 *
 * @author ss
 *
 */
public class DynamicPasswordManageTask extends TimerTask {
	@Override
	public void run() {
		//动态口令中的数据，根据动态口令的活动时间来判断删除，大于30分钟的删除，小于30分钟的保留
		Map<String,DynamicPassword> cache=DynamicPasswordCache.getDynamicPasswordCache();
		//遍历cache
		if(cache!=null&&!cache.isEmpty()){
			List<DynamicPassword> list=(List<DynamicPassword>) cache.values();
			for(DynamicPassword dp:list){
				if(System.currentTimeMillis()/10-dp.getActionTime()>Constant.PropertiesKey.DynamicPasswordConstant.OUTOFTIME){
					DynamicPasswordCache.removeInstance(dp.getSessionId());
				}
			}
		}
		
	}

	
}

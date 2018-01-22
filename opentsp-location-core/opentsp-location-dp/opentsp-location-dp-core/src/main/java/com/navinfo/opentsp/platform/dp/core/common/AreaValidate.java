package com.navinfo.opentsp.platform.dp.core.common;

import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AreaValidate {
	public static Logger log = LoggerFactory.getLogger(AreaValidate.class);
	//多边形暂不做
	public  boolean areaValidate(LCAreaInfo.AreaInfo areaInfo){
		if( areaInfo.getTypes().getNumber() == 140002){ //区域类型为圆形时，圆心点只有一个。
			if(areaInfo.getDatasCount()!=1){
				log.error("AreaValidate-->AreaCache:区域id："+areaInfo.getAreaIdentify()+"   区域类型：  "
						+areaInfo.getTypes()+"  区域数据size：  "+areaInfo.getDatasCount());
				return false;
			}
		} 
		if(areaInfo.getTypes().getNumber() == 140003){ //区域类型为矩形时，只能有两个点
			if(areaInfo.getDatasCount()!=2){
				log.error("AreaValidate-->AreaCache:区域id："+areaInfo.getAreaIdentify()+"   区域类型：  "
						+areaInfo.getTypes()+"  区域数据size：  "+areaInfo.getDatasCount());
				return false;
			}
		}
		return true;
	}
}

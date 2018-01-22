package com.navinfo.opentsp.platform.da.core.common.schedule.task;

import java.util.List;

import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictDBEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.mysql.LcDictDao;
import  com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import  com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcDictDaoImpl;
import  com.navinfo.opentsp.platform.da.core.webService.manage.SynchronousThreed;

public class SynchronousAdmin {
	/**
	 * 内部标准编    13-服务区域编码
	 */
	private static int DISTRICT_CODE=13;

	public  static void manage(){
		List<LcDictDBEntity> dictDatas=null;
		//提供一个根据类型查找字典明细的方法
		try {
			MySqlConnPoolUtil.startTransaction();

		  //首先数据初始化
		  LcDictDao lcDictDao = new LcDictDaoImpl();
		 dictDatas = lcDictDao.getDictByType(DISTRICT_CODE,0,0);
		} catch (Exception e) {
			// 回滚事务
			MySqlConnPoolUtil.rollback();
		} finally {
			MySqlConnPoolUtil.close();
		}
		//从数据库查出分区，然后根据new出同步线程并启动。添加判空操作。
		for(LcDictDBEntity entity:dictDatas){
			int district=entity.getDict_code();
			SynchronousThreed synchronousthread=new SynchronousThreed(district);
			Thread thread=new Thread(synchronousthread);
			thread.start();
		}
	}
	public static void main(String[] args) {
		
		SynchronousAdmin.manage();
	}
}

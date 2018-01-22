package com.navinfo.tasktracker.nilocation.service;

import com.navinfo.tasktracker.nilocation.util.RedisDaoTem;
import com.navinfo.tasktracker.nilocation.mapper.TerminalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * 内存中维护区域信息
 *@author zhangyue
 */
@Service
public class GpsDataSegementThisDay {

	protected static final Logger logger = LoggerFactory.getLogger(GpsDataSegementThisDay.class);

	public static final int NUM = 3;

	@Autowired
	private RedisDaoTem redisDaoTem;

	@Autowired
	private TerminalMapper terminalMapper;

	public void delGpsDataSegementThisDay() {
		Calendar cal = Calendar.getInstance();
		long millis = cal.getTimeInMillis();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long today = cal.getTimeInMillis();
		long seg = (millis - today) / (300 * 1000);
		seg = seg - NUM;
		if (seg <= 0) {
			seg = seg + 287;
			cal.add(Calendar.MINUTE, -5);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String format = sdf.format(cal.getTime());
		long s = System.currentTimeMillis();
		List list = new ArrayList();

		List<Long> terminalIds = getTerminalIds();
		try {
			//清除终端分段历史数据
			int size = terminalIds.size();
			String[] keyArr = new String[size];
			if(seg >= NUM) {
				for(int i = (int) (seg - NUM); i<= seg; i++) {
					int j = 0;
					for(long tid :terminalIds) {
						keyArr[j] = tid +"_" + format + "_" + i;
						j++;
					}
					redisDaoTem.del(keyArr);
				}
			}
			logger.info("删除当日终端分段数据:{},终端数量:{},耗时:{}ms",format+"_"+seg,
					terminalIds.size(),
					System.currentTimeMillis()-s);
		}catch (NullPointerException e) {
			logger.error("删除分段数据错误",e);
		}
	}

	public List<Long> getTerminalIds() {
		List<Long> list = terminalMapper.selectTerminals();
		return list;
//		try {
//			MySqlConnPoolUtil.startTransaction();
//			long startTime = System.currentTimeMillis();
//			LcTerminalInfoDao lcTerminalInfoDao = new LcTerminalInfoDaoImpl();
//			List<Long> terminalIds = lcTerminalInfoDao.getTerminalIds();
//			if (terminalIds != null) {
//				TerminalIdCache.getInstance().add(terminalIds);
//				log.error("定时获取终端ID列表,获取{}条，耗时{}",terminalIds.size(),System.currentTimeMillis()-startTime);
//			}
//		}catch (Exception e){
//			log.error("定时获取终端ID列表{}",e.getMessage());
//		}finally {
//			MySqlConnPoolUtil.close();
//		}
	}

}

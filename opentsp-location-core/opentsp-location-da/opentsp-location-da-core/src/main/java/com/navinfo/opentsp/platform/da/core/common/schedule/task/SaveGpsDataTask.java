package com.navinfo.opentsp.platform.da.core.common.schedule.task;

import com.navinfo.opentsp.platform.da.core.common.schedule.ITask;
import com.navinfo.opentsp.platform.da.core.common.schedule.TaskStatus;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDataEntityDB;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDetailedEntityDB;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.CanDataReportEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DACanDataDetail;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoManager;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.CanDataMongodbService;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.LocationMongodbService;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.CanDataMongodbServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.LocationMongodbServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.ILocationRedisService;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.DATransferStatusServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.LocationRedisServiceImpl;
import com.navinfo.opentsp.platform.da.core.rmi.impl.district.TermianlRuleAndParaServiceImpl;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils.DateFormat;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlRuleAndParaService;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataReport.CANBUSDataReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.*;

/**
 * 存储Gps数据任务
 *
 * @author lgw
 *
 */
public class SaveGpsDataTask extends TimerTask implements ITask {

	static List<String> successful = new ArrayList<String>();
	static List<String> failure = new ArrayList<String>();
    ILocationRedisService locationRedisService = new LocationRedisServiceImpl();
    LocationMongodbService locationMongodbService = new LocationMongodbServiceImpl();
    DATransferStatusServiceImpl transferStatusService = new DATransferStatusServiceImpl();
    CanDataMongodbService canDataMongodbService = new CanDataMongodbServiceImpl();
    private Logger log = LoggerFactory.getLogger(SaveGpsDataTask.class);
    /**
     * 转存任务终端列表
     */
    private List<Long> terminalIds = new ArrayList<Long>();

    public SaveGpsDataTask() {
     }

    @Override
    public void setExecuteCycle(long cycle) {
    }

    @Override
    public long getLastExecuteTime() {
        return 0;
    }

    @Override
    public TaskStatus getStatus() {
        return null;
    }

    public void transferLocations(boolean isNewTask, long beginDate, long endDate) throws UnknownHostException {
        successful.clear();
		failure.clear();
		Calendar beginTaskDate = DateUtils.calendar(beginDate);
		String beginTaskDateString = DateUtils.format(beginTaskDate.getTime(), DateUtils.DateFormat.YYYYMMDD);

		log.info("开始转存[" +beginTaskDateString + "]位置数据...");
		if(terminalIds!=null){
			log.info("------>本次转存[" +beginTaskDateString +"]:终端数量为["+terminalIds.size()+"]");
		}
		MongoManager.start(LCMongo.DB.LC_GPS_LOCATION,
			LCMongo.Collection.LC_GPS_DATA_ENTITY+ (DateUtils.format(beginTaskDate.getTime(), DateUtils.DateFormat.YYMM)));
		try {
			if(null!=terminalIds){
				//在redis中存储终端terminalIds开始转存
				if(isNewTask){
//                    transferStatusService.initTerminalStatus(terminalIds);
                    for (long terminalId : terminalIds) {
                        String dataKey = String.valueOf(terminalId) + "_" + beginTaskDateString;
                        boolean result = this.saveGpsData(dataKey);
						//mongodb保存成功
						if(result){
							successful.add(dataKey);
							//转存完成，在静态redis中存储完成标识
//							transferStatusService.saveDataComplete(terminalId);
                            //删除redis中的位置数据
							locationRedisService.deleteLocationData(dataKey);
						}else{
							log.error("转存失败："+dataKey);
							failure.add(dataKey);
						}
					}
				}else{
					//重新下发的任务
					for(long terminalId:terminalIds) {
						String dataKey = String.valueOf(terminalId)+"_"+beginTaskDateString;
						int terminalStatus = transferStatusService.findTerminalStatus(terminalId);
						//判断该终端的位置数据是否已经转存成功，没有转存成功则进行转存
						if(1 != terminalStatus){
							boolean result = this.saveGpsData(dataKey);
							//mongodb保存成功
							if(result){
								successful.add(dataKey);
								///转存完成，在静态redis中存储完成标识
								transferStatusService.saveDataComplete(terminalId);
								//删除redis中的位置数据
								locationRedisService.deleteLocationData(dataKey);
							}else{
								log.error("转存失败："+dataKey);
								failure.add(dataKey);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("[" +beginTaskDateString + "]位置数据转存任务异常.",e);
		} finally {
			MongoManager.close();
			log.error("本次转存[" +beginTaskDateString + "]位置数据状态：");
			log.error("共成功转存[ "+successful.size()+" ]条记录.");
			log.error("共失败转存[ "+failure.size()+" ]条记录.");
			if( failure.size() > 0 ){
				log.error("失败记录如下：\n");
				StringBuffer buffer = new StringBuffer();
				for(int i = 0 , length = failure.size() ; i < length ; i ++){
					String key = failure.get(i);
					buffer.append(key);
					if((i + 1) % 5 == 0){
						buffer.append("\n");
					}else{
						buffer.append(",");
					}
				}
				log.error("失败记录： "+buffer.toString());
			}
		}
	}

	public void transferCanDatas(boolean isNewTask,long beginDate,long endDate) throws UnknownHostException {
		successful.clear();
		failure.clear();
		Calendar beginTaskDate = DateUtils.calendar(beginDate);
		String beginTaskDateString = DateUtils.format(beginTaskDate.getTime(), DateFormat.YYYYMMDD);

		log.info("开始转存[" +beginTaskDateString + "]的CAN数据...");
		if(terminalIds!=null){
			log.info("------>本次转存CAN[" +beginTaskDateString +"]:终端数量为["+terminalIds.size()+"]");
		}
		MongoManager.start(LCMongo.DB.LC_GPS_CANDATA,
				LCMongo.Collection.LC_CANDATA_REPORT+ (DateUtils.format(beginTaskDate.getTime(),DateFormat.YYMM)));
		try {
			if(null!=terminalIds){
				//在redis中存储终端terminalIds开始转存
				if(isNewTask){
//					transferStatusService.initTerminalStatus(terminalIds);
                    for(long terminalId:terminalIds) {
						String dataKey = String.valueOf(terminalId)+"_can_"+beginTaskDateString;
						boolean result = this.saveCANData(dataKey);
						//mongodb保存成功
						if(result){
							successful.add(dataKey);
							//转存完成，在静态redis中存储完成标识
//							transferStatusService.saveDataComplete(terminalId);
                            //删除redis中的位置数据
							locationRedisService.deleteLocationData(dataKey);
						}else{
							log.error("转存CAN失败："+dataKey);
							failure.add(dataKey);
						}
					}
				}else{
					//重新下发的任务
					Map<Long, Integer> canStatus = transferStatusService.findCANStatus(terminalIds);
					for(long terminalId:terminalIds) {
						String dataKey = String.valueOf(terminalId)+"_can_"+beginTaskDateString;
						int terminalStatus = canStatus.get(terminalId);
						//判断该终端的位置数据是否已经转存成功，没有转存成功则进行转存
						if(1 != terminalStatus){
							boolean result = this.saveCANData(dataKey);
							//mongodb保存成功
							if(result){
								successful.add(dataKey);
								///转存完成，在静态redis中存储完成标识
								transferStatusService.saveCANDataComplete(terminalId);
								//删除redis中的位置数据
								locationRedisService.deleteLocationData(dataKey);
							}else{
								log.error("转存CAN失败："+dataKey);
								failure.add(dataKey);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("[" +beginTaskDateString + "]位置数据转存任务异常.",e);
		} finally {
			MongoManager.close();
			log.error("共成功转存CAN[ "+successful.size()+" ]条记录.");
			log.error("共失败转存CAN[ "+failure.size()+" ]条记录.");
			if( failure.size() > 0 ){
				log.error("CAN失败记录如下：\n");
				StringBuffer buffer = new StringBuffer();
				for(int i = 0 , length = failure.size() ; i < length ; i ++){
					String key = failure.get(i);
					buffer.append(key);
					if((i + 1) % 5 == 0){
						buffer.append("\n");
					}else{
						buffer.append(",");
					}
				}
				log.error("CAN失败记录： "+buffer.toString());
			}
		}
	}
	@Override
	public void run() {
		try {
			boolean isNewTask = true;
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);

			long endDate = (cal.getTimeInMillis() - 1) / 1000;
			cal.add(Calendar.DATE, -1);
			long beginDate = cal.getTimeInMillis() / 1000;
			log.info("转存任务开始加载终端信息...");
			terminalIds.clear();
			TermianlRuleAndParaService tr = new TermianlRuleAndParaServiceImpl();
			List<LCTerminalInfo.TerminalInfo> allTerminalInfo = tr.getAllTerminalInfo();
			for (LCTerminalInfo.TerminalInfo terminal : allTerminalInfo) {
				terminalIds.add(terminal.getTerminalId());
			}
            terminalIds.add(18010480002L);
			log.info("转存任务结束加载终端信息...");
			//合并位置数据
			transferLocations(isNewTask, beginDate, endDate);
			transferCanDatas(isNewTask, beginDate, endDate);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 将redis中key为dataKey的数据转存到mongo中
	 * @param dataKey
	 */
	private boolean saveGpsData(String dataKey) {
		boolean saveResult = true;
		try {
			//从redis中获取位置数据
			List<LocationData> list = locationRedisService.findAllLocationDatas(dataKey);
			if (null != list && list.size() > 0) {
				//位置数据根据gpsDate从小到大排序
				Collections.sort(list, new Comparator<LocationData>() {
					@Override
					public int compare(LocationData o1, LocationData o2) {
						//return o1.getGpsDate() > o2.getGpsDate()?1:-1;
						return String.valueOf(o1.getGpsDate()).compareTo(String.valueOf(o2.getGpsDate()));
					}
				});
				GpsDataEntityDB dataEntity = new GpsDataEntityDB();
				dataEntity.settId(Long.parseLong(dataKey.split("_")[0]));
				dataEntity.setDay(DateUtils.format(list.get(0).getGpsDate(),
						DateFormat.YYYYMMDD));
				GpsDetailedEntityDB detailedEntity = null;
				for (LocationData locationData : list) {
					detailedEntity = new GpsDetailedEntityDB();
					detailedEntity.setData(locationData.toByteArray());
					detailedEntity.setGpsTime(locationData.getGpsDate());
					dataEntity.addGpsDetailed(detailedEntity);
				}
				saveResult = locationMongodbService.saveGpsData(dataEntity);
				if(saveResult){
					log.info("完成终端[ "+dataKey+" ] 的位置数据转存任务,共计[ "+dataEntity.getDataList().size()+" ]条数据.");
				}else{
					log.info("终端[ "+dataKey+" ] 的位置数据转存任务出错啦,共计[ "+dataEntity.getDataList().size()+" ]条数据.请赶快检查。");
				}
			}
		} catch (Exception e) {
			saveResult = false;
			log.error("转存终端[ "+dataKey+" ] 的位置数据异常.",e);
		}
		return saveResult;
	}
	private boolean saveCANData(String dataKey) {
		boolean saveResult = true;
		try {
			//从redis中获取位置数据
			List<CANBUSDataReport> list = locationRedisService.findCANDatas(dataKey);
			if (null != list && list.size() > 0) {
				//位置数据根据gpsDate从小到大排序
				Collections.sort(list, new Comparator<CANBUSDataReport>() {
					@Override
					public int compare(CANBUSDataReport o1, CANBUSDataReport o2) {
						return o1.getReportDate() > o2.getReportDate()?1:-1;
					}
				});
				CanDataReportEntity dataEntity = new CanDataReportEntity();
				dataEntity.settId(Long.parseLong(dataKey.split("_can_")[0]));
				dataEntity.setDay(DateUtils.format(list.get(0).getReportDate(),
						DateFormat.YYYYMMDD));
				DACanDataDetail detailedEntity = null;
				for (CANBUSDataReport report : list) {
					detailedEntity = new DACanDataDetail();
					detailedEntity.setTime(report.getReportDate());
					detailedEntity.setData(report.toByteArray());
					dataEntity.addDataList(detailedEntity);
				}
				saveResult = canDataMongodbService.saveReportCanData(dataEntity);

				if(saveResult){
					log.info("完成终端[ "+dataKey+" ] 的CAN数据转存任务,共计[ "+dataEntity.getDataList().size()+" ]条数据.");
				}else{
					log.info("终端[ "+dataKey+" ] 的CAN数据转存任务出错啦,共计[ "+dataEntity.getDataList().size()+" ]条数据.请赶快检查。");
				}
			}
		} catch (Exception e) {
			saveResult = false;
			log.error("转存终端[ "+dataKey+" ] 的CAN数据异常.",e);
		}
		return saveResult;
	}
}

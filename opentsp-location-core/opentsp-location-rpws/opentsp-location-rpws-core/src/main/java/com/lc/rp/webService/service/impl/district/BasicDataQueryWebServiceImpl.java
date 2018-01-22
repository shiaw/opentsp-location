package com.lc.rp.webService.service.impl.district;


import com.google.protobuf.InvalidProtocolBufferException;
import com.lc.rp.common.RequestUtil;
import com.lc.rp.webService.service.BasicDataQueryWebService;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaSpeeding.AreaSpeeding;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDelayOvertimePark.DelayOvertimePark;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDoorOpenOutArea.DoorOpenOutArea;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDriverNotCard.DriverNotCard;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCInOutArea.InOutArea;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCKeyPointFence.KeyPointFence;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCMessageBroadcast.MessageBroadcast;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOverTimePark.OverTimePark;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRouteDriverTime.RouteDriverTime;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTerminalMessageSwitch.TerminalMessageSwitch;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCVehiclePassTimes.VehiclePassTimes;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCLastestLocationDataRes.LastestLocationDataRes;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa.LcTerminalRuleEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.Point;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.StatisticsQueryService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlDataService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlRuleAndParaService;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBusDataRecords.CANBusDataRecords;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalFirstRecieveDateRes.GetTerminalFirstRecieveDateRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalInAreaRes.GetTerminalInAreaRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalInDistrictRes.GetTerminalInDistrictRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalLocationDataRes.GetTerminalLocationDataRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCLastestLocationDataByAlarmFilterRess.LastestLocationDataByAlarmFilterRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCLastestTerminalStatusRes.LastestTerminalStatusRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCLatestParkRecoreds;
import com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCQueryRegularDataRes.QueryRegularDataRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCTerminalTrackRes.TerminalTrackRes;
import com.navinfo.opentsp.platform.rpws.core.configuration.RMIConnctorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import javax.xml.soap.SOAPException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


/*****************
 * 查询基础数据服务接口
 *
 *
 * @author claus
 *
 */
@WebService(endpointInterface = "com.lc.rp.webService.service.BasicDataQueryWebService", portName = "BasicDataQueryPort", serviceName="BasicDQWS")
public class BasicDataQueryWebServiceImpl implements BasicDataQueryWebService {

	//日志
	private Logger log = LoggerFactory.getLogger(BasicDataQueryWebServiceImpl.class);

	@Override
	public byte[] getTerminalRegular(List<Long> terminalIds, List<Long>  areaIds, boolean type, long accessTocken) {
		QueryRegularDataRes.Builder ruleDataRes = QueryRegularDataRes.newBuilder();
		try{
			TermianlRuleAndParaService client = RMIConnctorManager.getInstance().getTermianlRuleAndParaService();
			List<LcTerminalRuleEntity> areaRules = client.queryTerminalRule(terminalIds, areaIds, type);
			for(LcTerminalRuleEntity ruleEntity : areaRules){
				if(ruleEntity.getBusiness_type() == RegularCode.speeding_VALUE){
					//区域，路段超速规则
					RegularData.Builder ruleItem = RegularData.newBuilder();
					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
					ruleItem.setRegularCode(RegularCode.speeding);
//					ruleItem.setIsGeneral(ruleEntity.getRule_type()==1 ? false : true);
					ruleItem.setType(RegularType.valueOf(ruleEntity.getRule_type()));
					AreaSpeeding speed = AreaSpeeding.parseFrom(ruleEntity.getRule_content());
					ruleItem.setSpeeding(speed);
					//
					ruleDataRes.addDatas(ruleItem);
//					log.error("Terminal Regular Query: [ Type: speeding, Date:"+ruleItem.getLastModifyDate()+"]");
				} else if(ruleEntity.getBusiness_type() == RegularCode.inOutArea_VALUE){
					//进出区域，路线规则
					RegularData.Builder ruleItem = RegularData.newBuilder();
					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
					ruleItem.setRegularCode(RegularCode.inOutArea);
//					ruleItem.setIsGeneral(ruleEntity.getRule_type()==1 ? false : true);
					ruleItem.setType(RegularType.valueOf(ruleEntity.getRule_type()));
					InOutArea iout = InOutArea.parseFrom(ruleEntity.getRule_content());
					ruleItem.setInOutArea(iout);
					//
					ruleDataRes.addDatas(ruleItem);
//					log.error("Terminal Regular Query: [ Type: inOutArea, Date:"+ruleItem.getLastModifyDate()+"]");
				} else if(ruleEntity.getBusiness_type() == RegularCode.routeDriverTime_VALUE){
					//路段超时
					RegularData.Builder ruleItem = RegularData.newBuilder();
					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
					ruleItem.setRegularCode(RegularCode.routeDriverTime);
//					ruleItem.setIsGeneral(ruleEntity.getRule_type()==1 ? false : true);
					ruleItem.setType(RegularType.valueOf(ruleEntity.getRule_type()));
					RouteDriverTime drt = RouteDriverTime.parseFrom(ruleEntity.getRule_content());
					ruleItem.setDriverTime(drt);
					//
					ruleDataRes.addDatas(ruleItem);
//					log.error("Terminal Regular Query: [ Type: routeDriverTime, Date:"+ruleItem.getLastModifyDate()+"]");
				} else if(ruleEntity.getBusiness_type() == RegularCode.driverNotCard_VALUE){
					//无证驾驶
					RegularData.Builder ruleItem = RegularData.newBuilder();
					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
					ruleItem.setRegularCode(RegularCode.driverNotCard);
//					ruleItem.setIsGeneral(ruleEntity.getRule_type()==1 ? false : true);
					ruleItem.setType(RegularType.valueOf(ruleEntity.getRule_type()));
					DriverNotCard driveNCard = DriverNotCard.parseFrom(ruleEntity.getRule_content());
					ruleItem.setDriverNotCard(driveNCard);
					//
					ruleDataRes.addDatas(ruleItem);
//					log.error("Terminal Regular Query: [ Type: noCard, Date:"+ruleItem.getLastModifyDate()+"]");
				} else if(ruleEntity.getBusiness_type() == RegularCode.doorOpenOutArea_VALUE){
					//区域外开门
					RegularData.Builder ruleItem = RegularData.newBuilder();
					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
					ruleItem.setRegularCode(RegularCode.doorOpenOutArea);
//					ruleItem.setIsGeneral(ruleEntity.getRule_type()==1 ? false : true);
					ruleItem.setType(RegularType.valueOf(ruleEntity.getRule_type()));
					DoorOpenOutArea doorOpen = DoorOpenOutArea.parseFrom(ruleEntity.getRule_content());
					ruleItem.setDoorOpenOutArea(doorOpen);
					//
					ruleDataRes.addDatas(ruleItem);
//					log.error("Terminal Regular Query: [ Type: doorOpenOutArea, Date:"+doorOpen.getEndDate()+"]");

				} else if(ruleEntity.getBusiness_type() == RegularCode.drivingBan_VALUE){
					//禁止驾驶
//					RegularData.Builder ruleItem = RegularData.newBuilder();
//					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
//					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
//					ruleItem.setRegularCode(RegularCode.drivingBan);
//
//					//DrivingBan driBan = DrivingBan.parseFrom(ruleEntity.getRule_content());
//					ruleItem.setDrivingBan(driBan);
//
//					//
//					ruleDataRes.addDatas(ruleItem);
					//log.error("Terminal Regular Query: [ Type: drivingBan, Date:"+ruleItem.getLastModifyDate()+"]");
				}
				else if(ruleEntity.getBusiness_type() == RegularCode.keyPointFence_VALUE){
					//
					RegularData.Builder ruleItem = RegularData.newBuilder();
					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
					ruleItem.setRegularCode(RegularCode.keyPointFence);
//					ruleItem.setIsGeneral(ruleEntity.getRule_type()==1 ? false : true);
					ruleItem.setType(RegularType.valueOf(ruleEntity.getRule_type()));
					KeyPointFence keyPointFence = KeyPointFence.parseFrom(ruleEntity.getRule_content());
					ruleItem.setKeyPointFence(keyPointFence);

					ruleDataRes.addDatas(ruleItem);
//					log.error("Terminal Regular Query: [ Type: doorOpenOutArea, Date:"+keyPointFence.getEndDate()+"]");

				}else if(ruleEntity.getBusiness_type() == RegularCode.messageBroadcast_VALUE){
					RegularData.Builder ruleItem = RegularData.newBuilder();
					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
//					ruleItem.setIsGeneral(ruleEntity.getRule_type()==1 ? false : true);
					ruleItem.setType(RegularType.valueOf(ruleEntity.getRule_type()));
					ruleItem.setRegularCode(RegularCode.messageBroadcast);
					MessageBroadcast messageBroadcast = MessageBroadcast.parseFrom(ruleEntity.getRule_content());
					ruleItem.setMessageBroadcast(messageBroadcast);
					ruleDataRes.addDatas(ruleItem);
//					log.error("Terminal Regular Query: [ Type: messageBroadcast, Date:"+ruleItem.getLastModifyDate()+"]");
				}else if(ruleEntity.getBusiness_type() == RegularCode.overtimePark_VALUE){
					RegularData.Builder ruleItem = RegularData.newBuilder();
					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
//					ruleItem.setIsGeneral(ruleEntity.getRule_type()==1 ? false : true);
					ruleItem.setType(RegularType.valueOf(ruleEntity.getRule_type()));
					ruleItem.setRegularCode(RegularCode.overtimePark);
					OverTimePark overTimePark = OverTimePark.parseFrom(ruleEntity.getRule_content());
					ruleItem.setOvertimePark(overTimePark);
					ruleDataRes.addDatas(ruleItem);
//					log.error("Terminal Regular Query: [ Type: overtimePark, Date:"+ruleItem.getLastModifyDate()+"]");
				}else if(ruleEntity.getBusiness_type() == RegularCode.outregionToLSpeed_VALUE){
					RegularData.Builder ruleItem = RegularData.newBuilder();
					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
//					ruleItem.setIsGeneral(ruleEntity.getRule_type()==1 ? false : true);
					ruleItem.setType(RegularType.valueOf(ruleEntity.getRule_type()));
					ruleItem.setRegularCode(RegularCode.outregionToLSpeed);
					OutRegionToLSpeed outRegionToLSpeed = OutRegionToLSpeed.parseFrom(ruleEntity.getRule_content());
					ruleItem.setOutregionToLSpeed(outRegionToLSpeed);
					ruleDataRes.addDatas(ruleItem);
//					log.error("Terminal Regular Query: [ Type: outregionToLSpeed, Date:"+ruleItem.getLastModifyDate()+"]");
				}else if(ruleEntity.getBusiness_type() == RegularCode.terminalBroadcastSwitch_VALUE) {
					RegularData.Builder ruleItem = RegularData.newBuilder();
					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
					ruleItem.setType(RegularType.valueOf(ruleEntity.getRule_type()));
					ruleItem.setRegularCode(RegularCode.terminalBroadcastSwitch);
					TerminalMessageSwitch m = TerminalMessageSwitch.parseFrom(ruleEntity.getRule_content());
					ruleItem.setTerminalMessage(m);
					ruleDataRes.addDatas(ruleItem);
				}else if(ruleEntity.getBusiness_type() == RegularCode.delayOvertimePark_VALUE) {
					RegularData.Builder ruleItem = RegularData.newBuilder();
					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
					ruleItem.setType(RegularType.valueOf(ruleEntity.getRule_type()));
					ruleItem.setRegularCode(RegularCode.delayOvertimePark);
					DelayOvertimePark m = DelayOvertimePark.parseFrom(ruleEntity.getRule_content());
					ruleItem.setDelayPark(m);
					ruleDataRes.addDatas(ruleItem);
				} else if(ruleEntity.getBusiness_type() == RegularCode.vehiclePassStatistic_VALUE) {
					RegularData.Builder ruleItem = RegularData.newBuilder();
					ruleItem.setTerminalId(ruleEntity.getTerminal_id());
					ruleItem.setLastModifyDate(ruleEntity.getLast_update_time());
					ruleItem.setType(RegularType.valueOf(ruleEntity.getRule_type()));
					ruleItem.setRegularCode(RegularCode.vehiclePassStatistic);
					VehiclePassTimes m = VehiclePassTimes.parseFrom(ruleEntity.getRule_content());
					ruleItem.setPassTimes(m);
					ruleDataRes.addDatas(ruleItem);
				}
			}
			return ruleDataRes.build().toByteArray();
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage(),e);
			return null;
		} catch (NumberFormatException e) {
			log.error(e.getMessage(),e);
			return null;
		}  catch (RemoteException e) {
			log.error(e.getMessage(),e);
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return null;
		}
	}


//	@Override
//	public String getHelloworld() {
//		return "helloworld";
//	}

//	/**
//	 * 检索车辆在指定的时间段是否经过区域，检索后返回车辆轨迹的第一个点和最后一个点
//	 * @param terminalIds
//	 * @param beginTime
//	 * @param endTime
//	 * @param leftLongitude
//	 * @param rightLongitude
//	 * @param topLatitude
//	 * @param bottomLatitude
//	 * @return
//	 * @throws RemoteException
//	 */
//	@Override
//	public byte[] getCrossAreaTrack(List<Long>  terminalIds, long beginDate,
//			long endDate, int leftLongitude, int rightLongitude,
//			int topLatitude, int bottomLatitude, long accessTocken) {
//
//		try {
//			CrossAreaTracks.Builder terTrack = CrossAreaTracks.newBuilder();
//			TermianlDataService server = RMIConnctorManager.getInstance().getTermianlDataService();
//			Map<Long, GpsDataEntity> gpsData = server.getCrossAreaTrack(terminalIds, beginDate, endDate, leftLongitude, rightLongitude, topLatitude, bottomLatitude);
//			//
//			if(gpsData == null){
//				return null;
//			}
//			for(Entry<?, ?> entry:gpsData.entrySet()){
//				TerminalTracking.Builder tracking=TerminalTracking.newBuilder();
//			    long terminalId=(Long) entry.getKey();
//			    tracking.setTerminalId(terminalId);
//			    GpsDataEntity gpsDataEntity=(GpsDataEntity) entry.getValue();
//			    List<GpsDetailedEntity> dataList = gpsDataEntity.getDataList();
//			    for(GpsDetailedEntity datail:dataList){
//			    	LocationData datas = LocationData.parseFrom(datail.getData());
//			    	tracking.addLocationData(datas);
//			    	log.error("Terminal Track Query: [ GPS date: "+datas.getGpsDate() +"]");
//			    }
//			    terTrack.addTracks(tracking);
//			}
//
//			log.error("Terminal Track Query: [ Result "+terTrack.build().toString()+"]");
//			return terTrack.build().toByteArray();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("Terminal Track Query: [ Result "+e.getMessage()+"]");
//			return null;
//		}
//	}

	@Override
	public byte[] getTerminalTrack(long terminalId,long startDate, long endDate,
								   boolean isFilterBreakdown,long breakdownCode,boolean isThin, int level,int isAll,CommonParameter commonParameter) {
		// 2016.06.03新增——王景康
		try {
			StatisticsQueryService service = RMIConnctorManager.getInstance().getStaticsQueryService();
			long startTime = System.currentTimeMillis();
			log.error("获取轨迹回放数据getTerminalTrack,开始时间(ms)：   " + RequestUtil.ChangeDate(startTime / 1000));
			TerminalTrackRes track = service.getTerminalTrack(terminalId, startDate, endDate, isFilterBreakdown,
					breakdownCode,isThin,level,isAll, commonParameter);
			long endTime = System.currentTimeMillis();
			log.error("获取轨迹回放数据,结束时间(ms)：   " + RequestUtil.ChangeDate(endTime / 1000));
			log.error("获取轨迹回放数据耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			if (null != track) {
				return track.toByteArray();
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Point com.lc.rp.webService.paramEntity.Point
	 */
//	@Override
//	public byte[] getCrossAreaCounts(List<Long> terminalIds, long beginDate,
//			long endDate, int areaType, List<Point> points, long accessTocken) {
//		try {
//			//com.lc.core.protocol.rmi.statistic.entity.Point
//			List<com.lc.core.protocol.rmi.statistic.entity.Point> convertPoints = null;
//			if(points != null && points.size() >0) {
//				convertPoints = new ArrayList<com.lc.core.protocol.rmi.statistic.entity.Point>();
//				for(Point p :points) {
//					com.lc.core.protocol.rmi.statistic.entity.Point convertPoint = new com.lc.core.protocol.rmi.statistic.entity.Point();
//					convertPoint.setLatitude(p.getLatitude());
//					convertPoint.setLongitude(p.getLongitude());
//					convertPoints.add(convertPoint);
//				}
//			}
//			TermianlDataService server = RMIConnctorManager.getInstance().getTermianlDataService();
//			return server.getCrossAreaCounts(terminalIds, beginDate, endDate, areaType, convertPoints);
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("Terminal GetCrossAreaCounts: [ Result "+e.getMessage()+"]");
//			return null;
//		}
//	}
//	@Override
//	public byte[] getCrossAreaCounts2(List<Long> terminalIds, long beginDate,
//			long endDate, int areaType, List<Point> points,int width, long accessTocken) {
//		try {
//			//com.lc.core.protocol.rmi.statistic.entity.Point
//			List<com.lc.core.protocol.rmi.statistic.entity.Point> convertPoints = null;
//			if(points != null && points.size() >0) {
//				convertPoints = new ArrayList<com.lc.core.protocol.rmi.statistic.entity.Point>();
//				for(Point p :points) {
//					com.lc.core.protocol.rmi.statistic.entity.Point convertPoint = new com.lc.core.protocol.rmi.statistic.entity.Point();
//					convertPoint.setLatitude(p.getLatitude());
//					convertPoint.setLongitude(p.getLongitude());
//					convertPoints.add(convertPoint);
//				}
//			}
//			TermianlDataService server = RMIConnctorManager.getInstance().getTermianlDataService();
//			return server.getCrossAreaCounts2(terminalIds, beginDate, endDate, areaType, convertPoints,width);
//		} catch (Exception e) {
//			log.error("Terminal GetCrossAreaCounts: [ Result "+e.getMessage()+"]",e);
//			return null;
//		}
//	}

//	@Override
//	public byte[] getOutRegionLimitSpeed(List<Long> terminalIds,
//			long beginDate, long endDate, CommonParameter commonParameter) {
//		try {
//			//获取到MM动态下发的DA节点
//			TermianlRuleAndParaService daServer = (TermianlRuleAndParaService)RMIConnctorManager.getInstance().getTermianlRuleAndParaService();
//			log.error("ST: "+beginDate+" ,ET:"+endDate+", tocken: "+commonParameter.getAccessTocken());
//
//			OutRegionLimitSpeedQuery logs = daServer.queryOutRegionLimitSpeedLogList(terminalIds, beginDate, endDate, commonParameter);
//			if(null!=logs){
//				OutRegionLimitSpeed.Builder builder=OutRegionLimitSpeed.newBuilder();
//				builder.setStatusCode(logs.getStatusCode());
//				builder.setTotalRecords(logs.getTotalRords());
//				List<LcOutRegionLimitSpeedLogEntity> dataList=logs.getDataList();
//				LimitSpeedRecord.Builder record = LimitSpeedRecord.newBuilder();
//				for(LcOutRegionLimitSpeedLogEntity log:dataList){
//					record.setTerminalId(log.getTerminal_id());
//					record.setAreaIdentify(log.getOriginal_area_id());
//					record.setLimitDate(log.getLimit_date());
//					record.setLimitSpeed(log.getLimit_speed());
//					record.setSign(log.getSign());
//					builder.addRecord(record);
//				}
//				return builder.build().toByteArray();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}


	@Override
	public byte[] getCANBusData(List<Long> terminalIds, int type,
								long beginDate, long endDate, CommonParameter commonParameter) {
		try {
			TermianlDataService client = RMIConnctorManager.getInstance().getTermianlDataService();
			log.error("type:"+type+" ,ST: "+beginDate+" ,ET:"+endDate+", tid size: "+terminalIds.size());
			CANBusDataRecords canBusData = client.getCANBusData(terminalIds, type, beginDate, endDate, commonParameter);
			return canBusData.toByteArray();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 里程油量数据检索
	 */
//	@Override
//	public byte[] getMileageAndOilData(List<Long> terminalIds) {
//		try {
//			TermianlDataService server = RMIConnctorManager.getInstance().getTermianlDataService();
//			List<MileageAndOilData> mileAndOilDataList = server.getMileageAndOilData(terminalIds);
//			MileageAndOilDataRes.Builder builder = MileageAndOilDataRes.newBuilder();
//			if(mileAndOilDataList!=null){
//				for(MileageAndOilData data : mileAndOilDataList){
//					builder.addRecords(data);
//				}
//			}
//			return builder.build().toByteArray();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("Mileage Oil Query: [ Result "+e.getMessage()+"]");
//			return null;
//		}
//	}

	/**
	 * 最新位置数据检索
	 * @throws SOAPException
	 */
	@Override
	public byte[] getLastestLocationData(List<Long> terminalIds,String mileageRange,int type,CommonParameter commonParameter) throws Exception {
		if (!mileageRange.equals("")&&mileageRange.contains("-")) {
			throw new SOAPException("查询里程范围，不能为负数");
		}
		long startTime = System.currentTimeMillis();
		try {
			log.error("terminalIds:"+terminalIds.size()+" ,mileageRange:"+mileageRange+" ,type : "+type+" ,pageIndex : "+commonParameter.getPageIndex()+
					" ,pageSize : "+commonParameter.getPageSize()+",isMultipage: "+commonParameter.isMultipage());
			TermianlDataService server = RMIConnctorManager.getInstance().getTermianlDataService();
			LastestLocationDataRes dataRes = server.getLastestLocationData(terminalIds,mileageRange,type,commonParameter);
			long endTime = System.currentTimeMillis();
			log.error("最新位置数据检索-getLastestLocationData,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			if(dataRes != null){
				return dataRes.toByteArray();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("LastestLocation Query: [ Result "+e.getMessage()+"]");
			return null;
		}
	}

	//新增接口--3.2	位置数据检索   hxw
	@Override
	public byte[] getTerminalLocationData(long terminalId, long queryDate,
										  int index, long accessTocken) {
		long startTime = System.currentTimeMillis();
		try {
			log.error("terminalId:"+terminalId+" ,queryDate: "+queryDate+" ,index:"+index);
			TermianlDataService server = RMIConnctorManager.getInstance().getTermianlDataService();
			GetTerminalLocationDataRes dataRes = server.getTerminalLocationData(terminalId,queryDate,index,accessTocken);
			long endTime = System.currentTimeMillis();
			log.error("最新位置数据检索-getTerminalLocationData,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			return dataRes.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public byte[] getTerminalInArea(List<Long> terminalIds,int type, List<Point> points, int radius,
									long accessTocken) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		try {
			log.error("区域类型:"+type+" ,points : "+points.size()+" ,radius:"+radius);
			List<Point> convertPoints = null;
			if(points != null && points.size() >0) {
				convertPoints = new ArrayList<>();
				for(Point p :points) {
					Point convertPoint = new Point();
					convertPoint.setLatitude(p.getLatitude());
					convertPoint.setLongitude(p.getLongitude());
					convertPoints.add(convertPoint);
				}
			}
			TermianlDataService server = RMIConnctorManager.getInstance().getTermianlDataService();
			GetTerminalInAreaRes dataRes = server.getTerminalInArea(terminalIds,type,convertPoints,radius);
			long endTime = System.currentTimeMillis();
			log.error("区域查车-getTerminalInArea,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			return dataRes.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public byte[] GetTerminalInDistrict(List<Long> terminalIds, int districtCode, long accessTocken) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		try {
			log.error("terminalIds:"+terminalIds.size()+" ,districtCode : "+districtCode);

			TermianlDataService server = RMIConnctorManager.getInstance().getTermianlDataService();
			GetTerminalInDistrictRes dataRes = server.GetTerminalInDistrict(terminalIds, districtCode);
			long endTime = System.currentTimeMillis();
			log.error("区域查车-GetTerminalInDistrict,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			return dataRes.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public byte[] getLastestLocationDataByAlarmFilter(List<Long> terminalIds,
													  boolean isFilter, long alarmType, int isCancel, int type,
													  CommonParameter commonParameter) {
		long startTime = System.currentTimeMillis();
		try {
			log.error("terminalIds:"+terminalIds.size()+" ,isFilter : "+isFilter+" ,alarmType : "+alarmType+
					" ,isCancel : "+isCancel+" ,type : "+type+" ,CommonParameter.pageIndex : "+commonParameter.getPageIndex()+
					" ,CommonParameter.pageSize : "+commonParameter.getPageSize());

			TermianlDataService server = RMIConnctorManager.getInstance().getTermianlDataService();
			LastestLocationDataByAlarmFilterRes data = server.getLastestLocationDataByAlarmFilter(terminalIds,isFilter,alarmType,isCancel,type,commonParameter);
			long endTime = System.currentTimeMillis();
			log.error("报警撤销筛选最新位置,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			if(data!=null){
				return data.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public byte[] GetLastestTerminalStatus(List<Long> terminalIds, long accessTocken) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		try {
			if (terminalIds==null){
				terminalIds=new ArrayList<>();
			}
			log.error("terminalIds:"+terminalIds.size());

			TermianlDataService server = RMIConnctorManager.getInstance().getTermianlDataService();
			LastestTerminalStatusRes data = server.GetLastestTerminalStatus(terminalIds);
			long endTime = System.currentTimeMillis();
			log.error("获取终端状态,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			if(data!=null){
				return data.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public byte[] GetTerminalFirstRecieveDate(List<Long> terminalIds) {
		long startTime = System.currentTimeMillis();
		try {
			//log.error("terminalIds:"+terminalIds.size());

			TermianlDataService server = RMIConnctorManager.getInstance().getTermianlDataService();
			GetTerminalFirstRecieveDateRes data = server.GetTerminalFirstRecieveDate(terminalIds);
			long endTime = System.currentTimeMillis();
			log.error("获取终端首次接入时间,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			if(data!=null){
				return data.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 4.8车辆末次进入服务站时间
	 *
	 * @param terminalIds
	 * @param areaIds
	 * @param commonParameter @return
	 */
	@Override
	public byte[] GetLatestParkRecords(List<Long> terminalIds, List<Long> areaIds, CommonParameter commonParameter) {
		long startTime = System.currentTimeMillis();
		try {
			//log.error("terminalIds:"+terminalIds.size());

			TermianlDataService server = RMIConnctorManager.getInstance().getTermianlDataService();
			LCLatestParkRecoreds.LatestParkRecoreds data = server.GetLatestParkRecordsRes(terminalIds,areaIds);
			long endTime = System.currentTimeMillis();
			log.error("获取车辆末次进入服务站时间,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			if(data!=null){
				return data.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

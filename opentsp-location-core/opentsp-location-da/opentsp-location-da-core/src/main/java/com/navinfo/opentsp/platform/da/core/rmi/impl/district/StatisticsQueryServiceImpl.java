package com.navinfo.opentsp.platform.da.core.rmi.impl.district;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.navinfo.opentsp.platform.da.core.cache.TerminalMilOilTypeCache;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.*;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerMilOilTypeDBEntity;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMileageAndOilDataRes.MileageAndOilData;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.*;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCCalculateMileageConsumptionRes.CalculateMileageConsumptionRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCMileageConsumptionRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCStaytimeParkRecoreds;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCStaytimePark;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCVehiclePassDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMileageAndOilDataRes.MileageAndOilDataRes;
import  com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.StatisticsQueryService;
import  com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetLastestVehiclePassInAreaRes.GetLastestVehiclePassInAreaRes;
import  com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetStagnationTimeoutRes.GetStagnationTimeoutRes;
import  com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCMileageConsumptionRecords.MileageConsumptionRecords;
import  com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCMileageConsumption.MileageConsumption;
import  com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCStagnationTimeout.StagnationTimeout;
import  com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCDelOvertimeParkResult.DelOvertimeParkResult;
import  com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCTerminalTrackRes.TerminalTrackRes;
import  com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCFaultCodeRecords.FaultCodeRecords;
import  com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCOvertimeParkRecoreds.OvertimeParkRecoreds;
import  com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCStaytimeParkRecoreds.StaytimeParkRecoreds;
import  com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCVehiclePassInAreaRecords.VehiclePassInAreaRecords;
import  com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCVehiclePassTimesRecords.VehiclePassTimesRecords;
import  com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCFaultCode.FaultCode;
import  com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOvertimePark;
import  com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCStaytimePark;
import  com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCVehiclePassInArea.VehiclePassInArea;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.ReportQueryService;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.VehicleDrivingNumber;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.ReportQueryServiceImpl;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.VehicleDrivingNumberImpl;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsData;
import org.springframework.stereotype.Service;

/**
 * 统计查询接口（2016年5月20日添加）
 *
 * @author 王景康
 *
 */
@Service
public class StatisticsQueryServiceImpl implements StatisticsQueryService {

	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(StatisticsQueryServiceImpl.class);

	private VehicleDrivingNumber vehicleDrivingNumber = new VehicleDrivingNumberImpl();

	@Override
	public boolean isConnected()  {
		return true;
	}

	@Override
	public TerminalTrackRes getTerminalTrack(long terminalId, long beginDate, long endDate, boolean isFilterBreakdown,
											 long breakdownCode, boolean isThin, int level,int isAll, CommonParameter commonParameter) {
		ReportQueryService service = new ReportQueryServiceImpl();
		return service.getTerminalTrack(terminalId, beginDate, endDate, isFilterBreakdown, breakdownCode, isThin, level,isAll,
				commonParameter);
	}

	@Override
	public MileageAndOilDataRes getMileageAndOilData(List<Long> terminalIds, CommonParameter commonParameter)
			 {
		Map<Long, MileageConsumption> allConsumptions = TempGpsData.getAllMileageAndOilDataFromRedis();

		MileageAndOilDataRes.Builder builder = MileageAndOilDataRes.newBuilder();

		long todayBegin = this.getTodayBegin();
		long todayEnd = todayBegin + 24 * 3600 - 1;
		if (allConsumptions != null) {
			List<Long> targetTerminalIds = this.getTargetTerminalIds(terminalIds, commonParameter);
			for (Long terminalId : targetTerminalIds) {
				MileageConsumption con = allConsumptions.get(terminalId);
				if (con != null) {
					if (con.getEndDate() >= todayBegin && con.getEndDate() <= todayEnd) {
						MileageAndOilData.Builder b = MileageAndOilData.newBuilder();
						b.setTerminalId(con.getTerminalID());
						b.setMileage(con.getMileage());
						b.setOil(con.getOilConsumption());
						b.setCurrentDate(con.getEndDate());
						//b.setTerminalMileage(con.getTerminalMileage());
						//BigDecimal bd = BigDecimal.valueOf(con.getElectricConsumption());
						//b.setBatteryPower(bd.setScale(2, RoundingMode.HALF_UP).floatValue());

						builder.addRecords(b.build());
					}
				}
			}
		}

		return builder.build();
	}

	private long getTodayBegin() {
		long now = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return (calendar.getTimeInMillis() / 1000);
	}

	@Override
	public MileageConsumptionRecords getMileageConsumptionRecords(List<Long> terminalIds, long startDate, long endDate,
																  CommonParameter commonParameter)  {
		MileageConsumptionRecords.Builder builder = MileageConsumptionRecords.newBuilder();
		if (endDate > startDate) {
			List<Long> targetTerminalIds = this.getTargetTerminalIds(terminalIds, commonParameter);
			builder.setStatusCode(1);
			builder.setTotalRecords(terminalIds.size());
			long todayBegin = this.getTodayBegin();
			long todayEnd = todayBegin + 24 * 3600 - 1;

			Map<Long, MileageConsumption> beforeConsumptions = new HashMap<>();
			if (startDate < todayBegin) {
				// 查询历史数据
				ReportQueryService service = new ReportQueryServiceImpl();
				List<DAMilages> allMilages = service.getMilageRecords(targetTerminalIds, startDate, Math.min(todayBegin - 1, endDate));
				List<DAMilages> result = this.combineByDayUnit(targetTerminalIds, allMilages);

				for (DAMilages dam : result) {
					MileageConsumption.Builder mcb = MileageConsumption.newBuilder();
					mcb.setTerminalID(dam.getTerminalId());
					mcb.setMileage(dam.getCanMileage());
					mcb.setStaticDate(dam.getStaticDate());
					mcb.setOilConsumption(dam.getOilConsumption());
					mcb.setElectricConsumption(dam.getElectricConsumption());
					mcb.setBeginMileage(dam.getBeginMileage());
					mcb.setEndMileage(dam.getEndMileage());
					mcb.setStartDate(dam.getStartDate());
					mcb.setEndDate(dam.getEndDate());
					mcb.setBeginLat(dam.getBeginLat());
					mcb.setBeginLng(dam.getBeginLng());
					mcb.setEndLat(dam.getEndLat());
					mcb.setEndLng(dam.getEndLng());
					mcb.setTerminalMileage(dam.getTerminalMileage());
					mcb.setMeterMileage(dam.getMeterMileage());
					mcb.setFuelOil(dam.getFuelOil());
					mcb.setOilValue(dam.getOilValue());
					mcb.setBeginMeMileage(dam.getBeginMeMileage());
					mcb.setEndMeMileage(dam.getEndMeMileage());
					mcb.setBeginGpsMileage(dam.getBeginGpsMileage());
					mcb.setEndGpsMileage(dam.getEndGpsMileage());
					beforeConsumptions.put(dam.getTerminalId(), mcb.build());
				}
			}
			boolean flag = false;
			if (endDate > todayBegin) {
				// 如果查询时间段覆盖了当天则从Redis中补充查询，由于查询的时间粒度为天，所以只需要endDate大于当天开始时间即可认为涵盖了当天
				Map<Long, MileageConsumption> allConsumptions = TempGpsData.getAllMileageAndOilDataFromRedis();
				Map<Long, MileageConsumption> todayConsumptions = new HashMap<>();
				if (allConsumptions != null) {
					for (Long terminalId : targetTerminalIds) {
						MileageConsumption con = allConsumptions.get(terminalId);
						if (con != null) {
							// Redis中可能存在更早的数据，因此还需要按时间过滤
							if (con.getEndDate() >= todayBegin && con.getEndDate() <= todayEnd) {
								todayConsumptions.put(terminalId, con);
							}
						}
					}
				}
				Map<Long, MileageConsumption> allCombined = this.combine(targetTerminalIds, beforeConsumptions,
						todayConsumptions);
				for (Long terminalId : targetTerminalIds) {
					MileageConsumption con = allCombined.get(terminalId);
					if (con != null) {
						LCMileageConsumptionRes.MileageConsumptionRes.Builder res = LCMileageConsumptionRes.MileageConsumptionRes.newBuilder();
						LcTerMilOilTypeDBEntity entity = TerminalMilOilTypeCache.getInstance().getValue(con.getTerminalID());
						if (entity!=null){
							if (entity.getMileage_type() == 1){
								res.setMileage(con.getMeterMileage());
								res.setBeginMileage(con.getBeginMeMileage());
								res.setEndMileage(con.getEndMeMileage());
							}else if (entity.getMileage_type() == 2){
								res.setMileage(con.getMileage());
								res.setBeginMileage(con.getBeginMileage());
								res.setEndMileage(con.getEndMileage());
							}else if (entity.getMileage_type() == 3 || entity.getMileage_type() == 4){
								res.setMileage(con.getTerminalMileage());
								res.setBeginMileage(con.getBeginGpsMileage());
								res.setEndMileage(con.getEndGpsMileage());
							}
							if (entity.getOil_type() == 1){
								res.setOilConsumption(con.getOilConsumption());
							}else if (entity.getOil_type() == 2){
								res.setOilConsumption(con.getFuelOil());
							}
						}
						res.setTerminalID(con.getTerminalID());
						res.setStaticDate(con.getStaticDate());
						//res.setBeginMileage(con.getBeginMileage());
						//res.setEndMileage(con.getEndMileage());
						res.setStartDate(con.getStartDate());
						res.setEndDate(con.getEndDate());
						res.setBeginLat(con.getBeginLat());
						res.setBeginLng(con.getBeginLng());
						res.setEndLat(con.getEndLat());
						res.setEndLng(con.getEndLng());
						res.setOilValue(con.getOilValue());
						builder.addDataList(res.build());
					}
				}
				flag = true;
			}
			if (!flag) {
				for (Long terminalId : targetTerminalIds) {
					MileageConsumption con = beforeConsumptions.get(terminalId);
					if (con != null) {
						LCMileageConsumptionRes.MileageConsumptionRes.Builder res = LCMileageConsumptionRes.MileageConsumptionRes.newBuilder();
						LcTerMilOilTypeDBEntity entity = TerminalMilOilTypeCache.getInstance().getValue(con.getTerminalID());
						if (entity!=null){
							if (entity.getMileage_type() == 1){
								res.setMileage(con.getMeterMileage());
								res.setBeginMileage(con.getBeginMeMileage());
								res.setEndMileage(con.getEndMeMileage());
							}else if (entity.getMileage_type() == 2){
								res.setMileage(con.getMileage());
								res.setBeginMileage(con.getBeginMileage());
								res.setEndMileage(con.getEndMileage());
							}else if (entity.getMileage_type() == 3 || entity.getMileage_type() == 4){
								res.setMileage(con.getTerminalMileage());
								res.setBeginMileage(con.getBeginGpsMileage());
								res.setEndMileage(con.getEndGpsMileage());
							}
							if (entity.getOil_type() == 1){
								res.setOilConsumption(con.getOilConsumption());
							}else if (entity.getOil_type() == 2){
								res.setOilConsumption(con.getFuelOil());
							}
						}
						res.setTerminalID(con.getTerminalID());
						res.setStaticDate(con.getStaticDate());
						//res.setBeginMileage(con.getBeginMileage());
						//res.setEndMileage(con.getEndMileage());
						res.setStartDate(con.getStartDate());
						res.setEndDate(con.getEndDate());
						res.setBeginLat(con.getBeginLat());
						res.setBeginLng(con.getBeginLng());
						res.setEndLat(con.getEndLat());
						res.setEndLng(con.getEndLng());
						res.setOilValue(con.getOilValue());
						builder.addDataList(res.build());
					}
				}
			}
		} else {
			builder.setStatusCode(1);
			builder.setTotalRecords(0);
		}
		return builder.build();
	}

	/**
	 * 合并两组油耗数据，要求入参都非空，第一个油耗数据的统计时间较早，第二个油耗数据的统计时间较晚
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	private Map<Long, MileageConsumption> combine(List<Long> terminalIds, Map<Long, MileageConsumption> a,
												  Map<Long, MileageConsumption> b) {
		Map<Long, MileageConsumption> result = new HashMap<>();
		for (Long terminalId : terminalIds) {
			MileageConsumption ma = a.get(terminalId);
			MileageConsumption mb = b.get(terminalId);
			MileageConsumption.Builder mcb = MileageConsumption.newBuilder();
			mcb.setTerminalID(terminalId);
			if (ma != null && mb != null) {
				mcb.setTerminalMileage(ma.getTerminalMileage() + mb.getTerminalMileage());
				mcb.setMileage(ma.getMileage() + mb.getMileage());
				mcb.setMeterMileage(ma.getMeterMileage()+mb.getMeterMileage());
				mcb.setFuelOil(ma.getFuelOil()+mb.getFuelOil());
				mcb.setOilValue(ma.getOilValue()+mb.getOilValue());
				mcb.setStaticDate(ma.getStaticDate() + mb.getStaticDate());
				mcb.setOilConsumption(ma.getOilConsumption() + mb.getOilConsumption());
				mcb.setElectricConsumption(ma.getElectricConsumption() + mb.getElectricConsumption());
				mcb.setBeginMileage(Math.min(ma.getBeginMileage(), mb.getBeginMileage()));
				mcb.setEndMileage(Math.max(ma.getEndMileage(), mb.getEndMileage()));
				mcb.setBeginMeMileage(Math.min(ma.getBeginMeMileage(), mb.getBeginMeMileage()));
				mcb.setEndMeMileage(Math.max(ma.getEndMeMileage(), mb.getEndMeMileage()));
				mcb.setBeginGpsMileage(Math.min(ma.getBeginGpsMileage(), mb.getBeginGpsMileage()));
				mcb.setEndGpsMileage(Math.max(ma.getEndGpsMileage(), mb.getEndGpsMileage()));
				if (ma.getStartDate() == 0) {
					mcb.setStartDate(mb.getStartDate());
					mcb.setBeginLat(mb.getBeginLat());
					mcb.setBeginLng(mb.getBeginLng());
					mcb.setEndLat(mb.getEndLat());
					mcb.setEndLng(mb.getEndLng());
				} else if (mb.getStartDate() == 0) {
					mcb.setStartDate(ma.getStartDate());
					mcb.setBeginLat(ma.getBeginLat());
					mcb.setBeginLng(ma.getBeginLng());
					mcb.setEndLat(ma.getEndLat());
					mcb.setEndLng(ma.getEndLng());
				} else {
					mcb.setStartDate(Math.min(ma.getStartDate(), mb.getStartDate()));
					if (ma.getStartDate() < mb.getStartDate()) {
						mcb.setBeginLat(ma.getBeginLat());
						mcb.setBeginLng(ma.getBeginLng());
						mcb.setEndLat(mb.getEndLat());
						mcb.setEndLng(mb.getEndLng());
					} else {
						mcb.setBeginLat(mb.getBeginLat());
						mcb.setBeginLng(mb.getBeginLng());
						mcb.setEndLat(ma.getEndLat());
						mcb.setEndLng(ma.getEndLng());
					}
				}
				mcb.setEndDate(Math.max(ma.getEndDate(), mb.getEndDate()));
			} else if (ma == null && mb != null) {
				mcb.setTerminalMileage(mb.getTerminalMileage());
				mcb.setMileage(mb.getMileage());
				mcb.setMeterMileage(mb.getMeterMileage());
				mcb.setFuelOil(mb.getFuelOil());
				mcb.setOilValue(mb.getOilValue());
				mcb.setStaticDate(mb.getStaticDate());
				mcb.setOilConsumption(mb.getOilConsumption());
				mcb.setElectricConsumption(mb.getElectricConsumption());
				mcb.setBeginMileage(mb.getBeginMileage());
				mcb.setEndMileage(mb.getEndMileage());
				mcb.setBeginMeMileage(mb.getBeginMeMileage());
				mcb.setEndMeMileage(mb.getEndMeMileage());
				mcb.setBeginGpsMileage(mb.getBeginGpsMileage());
				mcb.setEndGpsMileage(mb.getEndGpsMileage());
				mcb.setStartDate(mb.getStartDate());
				mcb.setEndDate(mb.getEndDate());
				mcb.setBeginLat(mb.getBeginLat());
				mcb.setBeginLng(mb.getBeginLng());
				mcb.setEndLat(mb.getEndLat());
				mcb.setEndLng(mb.getEndLng());
			} else if (ma != null && mb == null) {
				mcb.setTerminalMileage(ma.getTerminalMileage());
				mcb.setMileage(ma.getMileage());
				mcb.setMeterMileage(ma.getMeterMileage());
				mcb.setFuelOil(ma.getFuelOil());
				mcb.setOilValue(ma.getOilValue());
				mcb.setStaticDate(ma.getStaticDate());
				mcb.setOilConsumption(ma.getOilConsumption());
				mcb.setElectricConsumption(ma.getElectricConsumption());
				mcb.setBeginMileage(ma.getBeginMileage());
				mcb.setEndMileage(ma.getEndMileage());
				mcb.setBeginMeMileage(ma.getBeginMeMileage());
				mcb.setEndMeMileage(ma.getEndMeMileage());
				mcb.setBeginGpsMileage(ma.getBeginGpsMileage());
				mcb.setEndGpsMileage(ma.getEndGpsMileage());
				mcb.setStartDate(ma.getStartDate());
				mcb.setEndDate(ma.getEndDate());
				mcb.setBeginLat(ma.getBeginLat());
				mcb.setBeginLng(ma.getBeginLng());
				mcb.setEndLat(ma.getEndLat());
				mcb.setEndLng(ma.getEndLng());
			}
			result.put(terminalId, mcb.build());
		}
		return result;
	}

	private List<DAMilages> combineByDayUnit(List<Long> targetTerminalIds, List<DAMilages> allMilages) {
		Map<Long, DAMilages> temp = new HashMap<>();
		if (allMilages != null) {
			for (DAMilages m : allMilages) {
				// System.out.println("m.getTerminalId() is " + m.getTerminalId());
				// System.out.println("m.getMileage() is " + m.getMileage());
				// System.out.println("m.getStaticDate() is " + m.getStaticDate());
				DAMilages current = temp.get(m.getTerminalId());
				if (current == null) {
					current = m;
					temp.put(m.getTerminalId(), m);
				} else {
					// 开始里程、结束里程
					current.setBeginMileage(Math.min(current.getBeginMileage(), m.getBeginMileage()));
					current.setEndMileage(Math.max(current.getEndMileage(), m.getEndMileage()));
					current.setBeginGpsMileage(Math.min(current.getBeginGpsMileage(), m.getBeginGpsMileage()));
					current.setEndGpsMileage(Math.max(current.getEndGpsMileage(), m.getEndGpsMileage()));
					current.setBeginMeMileage(Math.min(current.getBeginMeMileage(), m.getBeginMeMileage()));
					current.setEndMeMileage(Math.max(current.getEndMeMileage(), m.getEndMeMileage()));
					// 里程数
					current.setCanMileage(current.getCanMileage() + m.getCanMileage());
					// 开始时间、结束时间
					current.setStartDate(Math.min(current.getStartDate(), m.getStartDate()));
					current.setEndDate(Math.max(current.getEndDate(), m.getEndDate()));
					// 开始经度、纬度与结束经度、纬度
					if (current.getStartDate() > m.getStartDate()) {
						current.setBeginLng(m.getBeginLng());
						current.setBeginLat(m.getBeginLat());
					} else {
						current.setEndLng(m.getEndLng());
						current.setEndLat(m.getEndLat());
					}
					// 统计时长
					current.setStaticDate(current.getStaticDate() + m.getStaticDate());
					// 燃油、电量消耗
					current.setElectricConsumption(current.getElectricConsumption() + m.getElectricConsumption());
					current.setOilConsumption(current.getOilConsumption() + m.getOilConsumption());
					//终端里程数
					current.setTerminalMileage(current.getTerminalMileage() + m.getTerminalMileage());

					//仪表里程数
					current.setMeterMileage(current.getMeterMileage()+m.getMeterMileage());
					//积分总油耗
					current.setFuelOil(current.getFuelOil()+m.getFuelOil());
					//剩余油量统计
					current.setOilValue(current.getOilValue()+m.getOilValue());
				}
			}
		}

		List<DAMilages> result = new ArrayList<>(targetTerminalIds.size());
		for (Long terminalId : targetTerminalIds) {
			if (temp.containsKey(terminalId)) {
				result.add(temp.get(terminalId));
			} else {
				// 统计结果为0
				DAMilages emptyMilage = new DAMilages();
				emptyMilage.setTerminalId(terminalId);
				result.add(emptyMilage);
			}
		}
		return result;
	}

	private List<Long> getTargetTerminalIds(List<Long> terminalIds, CommonParameter commonParameter) {
		List<Long> targetTerminalIds = new ArrayList<>();
		if (terminalIds != null && terminalIds.size() > 0) {
			if (commonParameter.isMultipage()) {
				int begin = commonParameter.getPageSize() * commonParameter.getPageIndex();
				int end = begin + commonParameter.getPageSize();
				begin = Math.max(0, begin);
				end = Math.min(end, terminalIds.size());
				if (begin < end) {
					for (int i = begin; i < end; i++) {
						targetTerminalIds.add(terminalIds.get(i));
					}
				}
			} else {
				targetTerminalIds.addAll(terminalIds);
			}
		}
		return targetTerminalIds;
	}

	@Override
	public OvertimeParkRecoreds getOvertimeParkRecords(List<Long> terminalIds, List<Long> areaIds, long startDate,
													   long endDate, CommonParameter commonParameter)  {
		OvertimeParkRecoreds.Builder builder = OvertimeParkRecoreds.newBuilder();

		if (endDate > startDate) {
			ReportQueryService service = new ReportQueryServiceImpl();
			List<DAOvertimeParkAlarm> alarms = service.getOvertimeParkAlarmRecords(terminalIds, areaIds, startDate,
					endDate, commonParameter);
			int total = (int) service.getOvertimeParkAlarmRecordsCount(terminalIds, areaIds, startDate, endDate);
			int size = (alarms != null) ? alarms.size() : -1;

			builder.setStatusCode(1);
			builder.setTotalRecords(total);
			if (size > 0) {
				for (DAOvertimeParkAlarm alarm : alarms) {
					LCOvertimePark.OvertimePark.Builder fault = LCOvertimePark.OvertimePark.newBuilder();
					fault.setId(alarm.get_id());
					fault.setBeginDate(alarm.getBeginDate());
					fault.setBeginLat(alarm.getBeginLat());
					fault.setBeginLng(alarm.getBeginLng());
					fault.setContinuousTime(alarm.getContinuousTime());
					fault.setEndDate(alarm.getEndDate());
					fault.setEndLat(alarm.getEndLat());
					fault.setEndLng(alarm.getEndLng());
					fault.setTerminalId(alarm.getTerminalId());
					fault.setLimitParking(alarm.getLimitParking());
					fault.setAreaId(alarm.getAreaId());
					builder.addDataList(fault.build());
				}
			}
		} else {
			builder.setStatusCode(1);
			builder.setTotalRecords(0);
		}

		return builder.build();
	}

	@Override
	public LCStaytimeParkRecoreds.StaytimeParkRecoreds getStaytimeParkRecords(List<Long> terminalIds, List<Long> areaIds, long startDate,
																			  long endDate, CommonParameter commonParameter)  {
		StaytimeParkRecoreds.Builder builder = StaytimeParkRecoreds.newBuilder();

		if (endDate > startDate) {
			ReportQueryService service = new ReportQueryServiceImpl();
			List<DAStaytimeParkAlarm> alarms = service.getStaytimeParkAlarmRecords(terminalIds, areaIds, startDate,
					endDate, commonParameter);
			int total = (int) service.getStaytimeParkAlarmRecordsCount(terminalIds, areaIds, startDate, endDate);
			int size = (alarms != null) ? alarms.size() : -1;

			builder.setStatusCode(1);
			builder.setTotalRecords(total);
			if (size > 0) {
				for (DAStaytimeParkAlarm alarm : alarms) {
					LCStaytimePark.StaytimePark.Builder fault = LCStaytimePark.StaytimePark.newBuilder();
					fault.setId(alarm.get_id());
					fault.setBeginDate(alarm.getBeginDate());
					fault.setBeginLat(alarm.getBeginLat());
					fault.setBeginLng(alarm.getBeginLng());
					fault.setContinuousTime(alarm.getContinuousTime());
					fault.setEndDate(alarm.getEndDate());
					fault.setEndLat(alarm.getEndLat());
					fault.setEndLng(alarm.getEndLng());
					fault.setTerminalId(alarm.getTerminalId());
					fault.setAreaId(alarm.getAreaId());
					builder.addDataList(fault.build());
				}
			}
		} else {
			builder.setStatusCode(1);
			builder.setTotalRecords(0);
		}

		return builder.build();
	}

	@Override
	public DelOvertimeParkResult delOvertimeParkRecords(String id, long recordTime)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FaultCodeRecords getFaultCodeRecords(List<Long> terminalIds, int spn, int fmi, long startDate, long endDate,
												CommonParameter commonParameter)  {
		FaultCodeRecords.Builder builder = FaultCodeRecords.newBuilder();

		if (endDate > startDate) {
			ReportQueryService service = new ReportQueryServiceImpl();
			long time = System.currentTimeMillis();
			List<DAFaultCodeAlarm> alarms = service.getFaultCodeAlarmRecords(terminalIds, spn, fmi, startDate, endDate,
					commonParameter);
			int total = (int) service.getFaultCodeAlarmRecordsCount(terminalIds, spn, fmi, startDate, endDate);
			int size = (alarms != null) ? alarms.size() : -1;
			long cost = System.currentTimeMillis() - time;
			boolean log = true;
			if(log) {
				System.out.println("故障码查询总耗时(ms): " + cost);
			}

			builder.setStatusCode(1);
			builder.setTotalRecords(total);
			if (size > 0) {
				for (DAFaultCodeAlarm alarm : alarms) {
					FaultCode.Builder fault = FaultCode.newBuilder();
					fault.setTerminalId(alarm.getTerminalId());
					fault.setSpn(alarm.getSpn());
					fault.setFmi(alarm.getFmi());
					fault.setBeginDate(alarm.getBeginDate());
					fault.setContinuousTime(alarm.getContinuousTime());
					fault.setBeginLat(alarm.getBeginLat());
					fault.setBeginLon(alarm.getBeginLng());
					fault.setEndLat(alarm.getEndLat());
					fault.setEndLon(alarm.getEndLng());
					builder.addDataList(fault.build());
				}
			}
		} else {
			builder.setStatusCode(1);
			builder.setTotalRecords(0);
		}
		return builder.build();
	}

	@Override
	public VehiclePassTimesRecords getVehiclePassTimesRecords(int districtCode, long startDate, long endDate)
			 {
		VehiclePassTimesRecords records = vehicleDrivingNumber.getVehiclePassTimesRecords(districtCode, startDate, endDate);
		return records;
	}

	@Override
	public VehiclePassTimesRecords getVehiclePassTimesBytileId(List<Long> tileIds, long startDate, long endDate)
			 {
		VehiclePassTimesRecords records = vehicleDrivingNumber.getVehiclePassTimesByTileId(tileIds, startDate, endDate);
		return records;
	}

	@Override
	public VehiclePassInAreaRecords getVehiclePassInArea(List<Integer> districtCodes, int type, long startDate,
														 long endDate)  {
		VehiclePassInAreaRecords.Builder builder = VehiclePassInAreaRecords.newBuilder();

		if (endDate > startDate) {
			ResVehiclePassInAreaRecordsQuery records = vehicleDrivingNumber.queryVehiclePassInAreaRecords(districtCodes,
					type, startDate, endDate);

			builder.setStatusCode(records.getStatusCode());
			builder.setTotalRecords(records.getTotalRecords());

			List<VehiclePassInAreaEntity> list = records.getDataList();
			if (list != null && list.size() > 0) {
				for (VehiclePassInAreaEntity entity : list) {
					builder.addDataList(this.translate2VehiclePassInArea(entity));
				}
			}
		} else {
			builder.setStatusCode(1);
			builder.setTotalRecords(0);
		}

		return builder.build();
	}

	private VehiclePassInArea translate2VehiclePassInArea(VehiclePassInAreaEntity entity) {
		VehiclePassInArea.Builder builder = VehiclePassInArea.newBuilder();
		builder.setId(entity.get_id());
		builder.setTimes(entity.getTimes());

		if (entity.getDataList().size() > 0) {
			for (VehiclePassDetail detail : entity.getDataList()) {
				LCVehiclePassDetail.VehiclePassDetail.Builder detailBuilder = LCVehiclePassDetail.VehiclePassDetail
						.newBuilder();
				detailBuilder.setTimes(detail.getTime());
				detailBuilder.setTid(detail.getTid());
				detailBuilder.setMileage(detail.getMileage());
				detailBuilder.setRunTime(detail.getRunTime());
				detailBuilder.setHasFaultCode(detail.isHasFaultCode());
				builder.addDataList(detailBuilder.build());
			}
		}

		return builder.build();
	}

	@Override
	public GetLastestVehiclePassInAreaRes getLastestVehiclePassInArea(List<Integer> districtCodes)
			 {
		try {
			return TempGpsData.findVehiclePassInAreaInfo(districtCodes);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public GetStagnationTimeoutRes getStagnationTimeoutRecords(
			List<Long> terminalID, long startDate, long endDate,
			CommonParameter commonParameter)  {
		GetStagnationTimeoutRes.Builder builder = GetStagnationTimeoutRes.newBuilder();

		if (endDate > startDate) {
			ReportQueryService service = new ReportQueryServiceImpl();
			List<DAStagnationTimeoutAlarm> alarms = service.getStagnationTimeoutRecords(terminalID,startDate,
					endDate, commonParameter);
			int total = (int) service.getStagnationTimeoutRecordsCount(terminalID,startDate, endDate);
			int size = (alarms != null) ? alarms.size() : -1;

			builder.setStatusCode(1);
			builder.setTotalRecords(total);
			if (size > 0) {
				for (DAStagnationTimeoutAlarm alarm : alarms) {
					StagnationTimeout.Builder Stagnation = StagnationTimeout.newBuilder();
					Stagnation.setId(alarm.get_id());
					Stagnation.setTerminalId(alarm.getTerminalId());
					Stagnation.setBeginDate(alarm.getBeginDate());
					Stagnation.setEndDate(alarm.getEndDate());
					Stagnation.setContinuousTime(alarm.getContinuousTime());
					Stagnation.setLimitParking(alarm.getLimitParking());
					Stagnation.setBeginLat(alarm.getBeginLat());
					Stagnation.setBeginLng(alarm.getBeginLng());
					Stagnation.setStatus(alarm.isStatus());
					builder.addDataList(Stagnation.build());
				}
			}
		} else {
			builder.setStatusCode(1);
			builder.setTotalRecords(0);
		}

		return builder.build();
	}

	/**
	 * 里程能耗实时计算
	 */
	@Override
	public CalculateMileageConsumptionRes calculateMileageConsumption(
			long terminalId, long startDate, long endDate, long accessTocken){
		// TODO Auto-generated method stub
		CalculateMileageConsumptionRes.Builder builder = CalculateMileageConsumptionRes.newBuilder();
		if(endDate > startDate){
			ReportQueryService service = new ReportQueryServiceImpl();
			Mileages da = service.calculateMileageConsumption(terminalId, startDate, endDate, accessTocken);
			LCMileageConsumptionRes.MileageConsumptionRes.Builder mBuilder = LCMileageConsumptionRes.MileageConsumptionRes.newBuilder();
			builder.setStatusCode(1);
			if(da!=null){
				LcTerMilOilTypeDBEntity entity = TerminalMilOilTypeCache.getInstance().getValue(da.getTerminalID());
				if (entity!=null) {
					if (entity.getMileage_type() == 1) {
						mBuilder.setMileage(da.getMeterMileage());
						mBuilder.setBeginMileage(da.getBeginMeMileage());
						mBuilder.setEndMileage(da.getEndMeMileage());
					} else if (entity.getMileage_type() == 2) {
						mBuilder.setMileage(da.getCanMileage());
						mBuilder.setBeginMileage(da.getBeginMileage());
						mBuilder.setEndMileage(da.getEndMileage());
					} else if (entity.getMileage_type() == 3 || entity.getMileage_type() == 4) {
						mBuilder.setMileage(da.getGpsMileage());
						mBuilder.setBeginMileage(da.getBeginGpsMileage());
						mBuilder.setEndMileage(da.getEndGpsMileage());
					}
					if (entity.getOil_type() == 1) {
						mBuilder.setOilConsumption(da.getOilConsumption());
					} else if (entity.getOil_type() == 2) {
						mBuilder.setOilConsumption(da.getFuelOil());
					}
				}
				mBuilder.setTerminalID(da.getTerminalID());
				mBuilder.setStaticDate(da.getStaticDate());
				//mBuilder.setBeginMileage(da.getBeginMileage());
				//mBuilder.setEndMileage(da.getEndMileage());
				mBuilder.setStartDate(da.getStartDate());
				mBuilder.setEndDate(da.getEndDate());
				mBuilder.setBeginLat(da.getBeginLat());
				mBuilder.setBeginLng(da.getBeginLng());
				mBuilder.setEndLat(da.getEndLat());
				mBuilder.setEndLng(da.getEndLng());
				mBuilder.setOilValue(da.getOilValue());
				builder.setData(mBuilder.build());
			}
		} else {
			builder.setStatusCode(1);
		}
		return builder.build();
	}

}

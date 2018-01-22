package com.lc.rp.webService.service.impl.district;

import ch.qos.logback.classic.Logger;
import com.lc.rp.common.RequestUtil;
import com.lc.rp.webService.common.ConvertKit;
import com.lc.rp.webService.service.DataAnalysisWebService;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMileageAndOilDataRes.MileageAndOilData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMileageAndOilDataRes.MileageAndOilDataRes;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.ResVehiclePassTimesRecordsQuery;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsQueryService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.RpQueryKeyService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.StatisticsQueryService;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCCalculateMileageConsumptionRes.CalculateMileageConsumptionRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetLastestVehiclePassInAreaRes.GetLastestVehiclePassInAreaRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetStagnationTimeoutRes.GetStagnationTimeoutRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCMileageConsumptionRecords.MileageConsumptionRecords;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCMileageConsumption.MileageConsumption;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCStagnationTimeoutCancelOrNotResult.StagnationTimeoutCancelOrNotResult;
import com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCDelOvertimeParkResult.DelOvertimeParkResult;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCCrossGridCounts.CrossGridCounts;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCFaultCodeRecords.FaultCodeRecords;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCOvertimeParkRecoreds.OvertimeParkRecoreds;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCStaytimeParkRecoreds.StaytimeParkRecoreds;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCVehiclePassInAreaRecords.VehiclePassInAreaRecords;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCVehiclePassTimesRecords.VehiclePassTimesRecords;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOvertimePark.OvertimePark;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCStaytimePark.StaytimePark;
import com.navinfo.opentsp.platform.rpws.core.configuration.RMIConnctorManager;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import java.util.List;
import java.util.Map;

@WebService(endpointInterface = "com.lc.rp.webService.service.DataAnalysisWebService", portName = "AnalysisPort", serviceName = "DAnalysisWS")
public class DataAnalysisWebServiceImpl implements DataAnalysisWebService {

	// 休眠时间
	public static int SLEEP_SECONDS = 100;

	// 日志
	public static Logger log = (Logger) LoggerFactory.getLogger(DataAnalysisWebServiceImpl.class);

	// @Override
	// public byte[] getOverspeedAlarmRecords(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResOverspeedAlarmRecoredsQuery Recoreds =
	// client.overspeedRecordsQuery(terminalId, startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<OverspeedAlarm> dataList = Recoreds.getCommResultList();
	// ResOverspeedAlarmRecoreds.Builder builder =
	// ResOverspeedAlarmRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// OverspeedData.Builder item = OverspeedData.newBuilder();
	// for (OverspeedAlarm spd : dataList) {
	// item.setStartDate(spd.getBeginDate());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setLimitSpeed(spd.getLimitSpeed());
	// item.setMaxSpeed(spd.getMaxSpeed());
	// item.setMinSpeed(spd.getMinSpeed());
	// item.setTerminalID(spd.getTerminalId());
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getOverspeedAlarmSummary(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	//
	// AlarmSummaryQuery summary = client.overspeedSummaryQuery(terminalId,
	// startDate, endDate, commonParameter,
	// commonParameter.getQueryKey());
	// if (null != summary) {
	// ResOverspeedAlarmSummary.Builder builder =
	// ResOverspeedAlarmSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<CommonSummaryEntity> dataList = summary.getCommResultList();
	// CommonSummary.Builder resCommonSummary = CommonSummary.newBuilder();
	// for (CommonSummaryEntity commonSummary : dataList) {
	// resCommonSummary.setCalculatedAT(commonSummary.getCalculatedAT());
	// resCommonSummary.setEndTime(commonSummary.getEndTime());
	// resCommonSummary.setRecordsTotal(commonSummary.getRecordsTotal());
	// resCommonSummary.setStartTime(commonSummary.getStartTime());
	// resCommonSummary.setTerminalID(commonSummary.getTerminalID());
	// builder.addDataList(resCommonSummary);
	// }
	// log.error(new String(builder.build().toByteArray()));
	// return builder.build().toByteArray();
	// }
	//
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getAreaINOUTAlarmRecords(List<Long> terminalId, long
	// startDate, long endDate, int type,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResAreaINOUTAlarmRecoredsQuery Recoreds =
	// client.areaINOUTAlarmRecoredsQuery(terminalId, type, startDate,
	// endDate, commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<AreaINOUTAlarm> dataList = Recoreds.getCommResultList();
	// ResAreaINOUTAlarmRecoreds.Builder builder =
	// ResAreaINOUTAlarmRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// AreaInOutData.Builder item = AreaInOutData.newBuilder();
	// for (AreaINOUTAlarm spd : dataList) {
	// item.setStartDate(spd.getBeginDate());
	// item.setAreaID(spd.getAreaId());
	// item.setType(spd.getType());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setTerminalID(spd.getTerminalId());
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	//
	// return null;
	// }

	// @Override
	// public byte[] getAreaINOUTAlarmSummary(List<Long> terminalId, long
	// startDate, long endDate, int type,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// AlarmSummaryQuery summary = client.areaINOUTAlarmSummaryQuery(terminalId,
	// type, startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != summary) {
	// ResAreaINOUTAlarmSummary.Builder builder =
	// ResAreaINOUTAlarmSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<CommonSummaryEntity> dataList = summary.getCommResultList();
	// CommonSummary.Builder resCommonSummary = CommonSummary.newBuilder();
	// for (CommonSummaryEntity commonSummary : dataList) {
	// resCommonSummary.setCalculatedAT(commonSummary.getCalculatedAT());
	// resCommonSummary.setEndTime(commonSummary.getEndTime());
	// resCommonSummary.setRecordsTotal(commonSummary.getRecordsTotal());
	// resCommonSummary.setStartTime(commonSummary.getStartTime());
	// resCommonSummary.setTerminalID(commonSummary.getTerminalID());
	// builder.addDataList(resCommonSummary);
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getAreaOverspeedAlarmRecords(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResAreaOverspeedAlarmRecoredsQuery Recoreds =
	// client.areaOverspeedRecordsQuery(terminalId, startDate,
	// endDate, commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<AreaOverspeedAlarm> dataList = Recoreds.getCommResultList();
	// ResAreaOverspeedAlarmRecoreds.Builder builder =
	// ResAreaOverspeedAlarmRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// AreaOverspeedData.Builder item = AreaOverspeedData.newBuilder();
	// for (AreaOverspeedAlarm spd : dataList) {
	// item.setStartDate(spd.getBeginDate());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setTerminalID(spd.getTerminalId());
	// item.setAreaID(spd.getAreaId());
	// item.setType(spd.getType());
	// item.setLimitSpeed(spd.getLimitSpeed());
	// item.setMaxSpeed(spd.getMaxSpeed());
	// item.setMinSpeed(spd.getMinSpeed());
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	//
	// return null;
	// }

	// @Override
	// public byte[] getAreaOverspeedAlarmSummary(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// AlarmSummaryQuery summary = client.areaOverspeedSummaryQuery(terminalId,
	// startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != summary) {
	// ResAreaOverspeedAlarmSummary.Builder builder =
	// ResAreaOverspeedAlarmSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<CommonSummaryEntity> dataList = summary.getCommResultList();
	// CommonSummary.Builder resCommonSummary = CommonSummary.newBuilder();
	// for (CommonSummaryEntity commonSummary : dataList) {
	// resCommonSummary.setCalculatedAT(commonSummary.getCalculatedAT());
	// resCommonSummary.setEndTime(commonSummary.getEndTime());
	// resCommonSummary.setRecordsTotal(commonSummary.getRecordsTotal());
	// resCommonSummary.setStartTime(commonSummary.getStartTime());
	// resCommonSummary.setTerminalID(commonSummary.getTerminalID());
	// builder.addDataList(resCommonSummary);
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getRouteINOUTAlarmRecords(List<Long> terminalId, long
	// startDate, long endDate, int type,
	// CommonParameter commonParameter) {
	// long start = System.currentTimeMillis();
	// log.error("开启WS时间： " + DateUtils.format(start,
	// DateFormat.YY_YY_MM_DD_HH_MM_SS));
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResRouteINOUTAlarmRecoredsQuery Recoreds =
	// client.routeLineINOUTAlarmRecoredsQuery(terminalId, type,
	// startDate, endDate, commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<RouteINOUTAlarm> dataList = Recoreds.getCommResultList();
	// ResRouteINOUTAlarmRecoreds.Builder builder =
	// ResRouteINOUTAlarmRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// RouteInOutData.Builder item = RouteInOutData.newBuilder();
	// for (RouteINOUTAlarm spd : dataList) {
	// item.setAreaID(spd.getAreaId());
	// item.setType(spd.getType());
	// item.setTriggerContinuousTimes(spd.getTriggerContinuousTime());
	// item.setTriggerDate((int) spd.getTriggerDate());
	// item.setTriggerLat(spd.getTriggerLat());
	// item.setTriggerLng(spd.getTriggerLng());
	// item.setStartDate(spd.getBeginDate());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setTerminalID(spd.getTerminalId());
	// builder.addDataList(item);
	// }
	// log.error("完成WS时间： " + (System.currentTimeMillis() - start));
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getRouteINOUTAlarmSummary(List<Long> terminalId, long
	// startDate, long endDate, int type,
	// CommonParameter commonParameter) {
	// long start = System.currentTimeMillis();
	// log.error("开启WS时间： " + DateUtils.format(start,
	// DateFormat.YY_YY_MM_DD_HH_MM_SS));
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// AlarmSummaryQuery summary =
	// client.routeINOUTAlarmSummaryQuery(terminalId, type, startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != summary) {
	// ResRouteINOUTAlarmSummary.Builder builder =
	// ResRouteINOUTAlarmSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<CommonSummaryEntity> dataList = summary.getCommResultList();
	// CommonSummary.Builder resCommonSummary = CommonSummary.newBuilder();
	// for (CommonSummaryEntity commonSummary : dataList) {
	// resCommonSummary.setCalculatedAT(commonSummary.getCalculatedAT());
	// resCommonSummary.setEndTime(commonSummary.getEndTime());
	// resCommonSummary.setRecordsTotal(commonSummary.getRecordsTotal());
	// resCommonSummary.setStartTime(commonSummary.getStartTime());
	// resCommonSummary.setTerminalID(commonSummary.getTerminalID());
	// builder.addDataList(resCommonSummary);
	// }
	//
	// log.error("完成WS时间： " + (System.currentTimeMillis() - start));
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getRLineOverspeedAlarmRecords(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResRLineOverspeedAlarmRecoredsQuery Recoreds =
	// client.routeLineOverspeedAlarmRecoredsQuery(terminalId,
	// startDate, endDate, commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<RLineOverspeedAlarm> dataList = Recoreds.getCommResultList();
	// ResRLineOverspeedAlarmRecoreds.Builder builder =
	// ResRLineOverspeedAlarmRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// RLineOverspeedData.Builder item = RLineOverspeedData.newBuilder();
	// for (RLineOverspeedAlarm spd : dataList) {
	// item.setAreaID(spd.getAreaId());
	// item.setSegmentID(spd.getSegmentId());
	// item.setLimitSpeed(spd.getLimitSpeed());
	// item.setMaxSpeed(spd.getMaxSpeed());
	// item.setMinSpeed(spd.getMinSpeed());
	// item.setStartDate(spd.getBeginDate());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setTerminalID(spd.getTerminalId());
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getRLineOverspeedAlarmSummary(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// AlarmSummaryQuery summary =
	// client.routeLineOverspeedAlarmSummaryQuery(terminalId, startDate,
	// endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != summary) {
	// ResRLineOverspeedAlarmSummary.Builder builder =
	// ResRLineOverspeedAlarmSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<CommonSummaryEntity> dataList = summary.getCommResultList();
	// CommonSummary.Builder resCommonSummary = CommonSummary.newBuilder();
	// for (CommonSummaryEntity commonSummary : dataList) {
	// resCommonSummary.setCalculatedAT(commonSummary.getCalculatedAT());
	// resCommonSummary.setEndTime(commonSummary.getEndTime());
	// resCommonSummary.setRecordsTotal(commonSummary.getRecordsTotal());
	// resCommonSummary.setStartTime(commonSummary.getStartTime());
	// resCommonSummary.setTerminalID(commonSummary.getTerminalID());
	// builder.addDataList(resCommonSummary);
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getRLineOvertimeAlarmRecords(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResRLineOvertimeAlarmRecoredsQuery Recoreds =
	// client.routeLineOvertimeAlarmRecoredsQuery(terminalId,
	// startDate, endDate, commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<RLineOvertimeAlarm> dataList = Recoreds.getCommResultList();
	// ResRLineOvertimeAlarmRecoreds.Builder builder =
	// ResRLineOvertimeAlarmRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// RLineOvertimeData.Builder item = RLineOvertimeData.newBuilder();
	// for (RLineOvertimeAlarm spd : dataList) {
	// item.setAreaID(spd.getAreaId());
	// item.setSegmentID(spd.getSegmentId());
	// item.setType(spd.getType());
	// item.setMaxLimitTime(spd.getMaxLimitTime());
	// item.setMinLimitTime(spd.getMinLimitTime());
	// item.setStartDate(spd.getBeginDate());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setTerminalID(spd.getTerminalId());
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getRLineOvertimeAlarmSummary(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// AlarmSummaryQuery summary =
	// client.routeLineOvertimeAlarmSummaryQuery(terminalId, startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != summary) {
	// ResRLineOvertimeAlarmSummary.Builder builder =
	// ResRLineOvertimeAlarmSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<CommonSummaryEntity> dataList = summary.getCommResultList();
	// CommonSummary.Builder resCommonSummary = CommonSummary.newBuilder();
	// for (CommonSummaryEntity commonSummary : dataList) {
	// resCommonSummary.setCalculatedAT(commonSummary.getCalculatedAT());
	// resCommonSummary.setEndTime(commonSummary.getEndTime());
	// resCommonSummary.setRecordsTotal(commonSummary.getRecordsTotal());
	// resCommonSummary.setStartTime(commonSummary.getStartTime());
	// resCommonSummary.setTerminalID(commonSummary.getTerminalID());
	// builder.addDataList(resCommonSummary);
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getOvertimeParkingAlarmRecords(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResOvertimeParkingAlarmRecoredsQuery Recoreds =
	// client.overtimeParkingAlarmRecoredsQuery(terminalId,
	// startDate, endDate, commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<OvertimeParkingAlarm> dataList = Recoreds.getCommResultList();
	// ResOvertimeParkingAlarmRecoreds.Builder builder =
	// ResOvertimeParkingAlarmRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// OvertimeParkingData.Builder item = OvertimeParkingData.newBuilder();
	// for (OvertimeParkingAlarm spd : dataList) {
	// item.setStartDate(spd.getBeginDate());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setTerminalID(spd.getTerminalId());
	// // item.setLimitParking((long)spd.getLimitParking());
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getOvertimeParkingAlarmSummary(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// AlarmSummaryQuery summary =
	// client.overtimeParkingAlarmSummaryQuery(terminalId, startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != summary) {
	// ResOvertimeParkingAlarmSummary.Builder builder =
	// ResOvertimeParkingAlarmSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<CommonSummaryEntity> dataList = summary.getCommResultList();
	// CommonSummary.Builder resCommonSummary = CommonSummary.newBuilder();
	// for (CommonSummaryEntity commonSummary : dataList) {
	// resCommonSummary.setCalculatedAT(commonSummary.getCalculatedAT());
	// resCommonSummary.setEndTime(commonSummary.getEndTime());
	// resCommonSummary.setRecordsTotal(commonSummary.getRecordsTotal());
	// resCommonSummary.setStartTime(commonSummary.getStartTime());
	// resCommonSummary.setTerminalID(commonSummary.getTerminalID());
	// builder.addDataList(resCommonSummary);
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getFatigueAlarmRecords(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResFatigueAlarmRecoredsQuery Recoreds =
	// client.fatigueAlarmRecoredsQuery(terminalId, startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<FatigueAlarm> dataList = Recoreds.getCommResultList();
	// ResFatigueAlarmRecoreds.Builder builder =
	// ResFatigueAlarmRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// FatigueData.Builder item = FatigueData.newBuilder();
	// for (FatigueAlarm spd : dataList) {
	// item.setLimitDayDriving(spd.getLimitDayDriving());
	// item.setLimitDriving(spd.getLimitDriving());
	// item.setLimitRest(spd.getLimitRest());
	// item.setStartDate(spd.getBeginDate());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setTerminalID(spd.getTerminalId());
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getFatigueAlarmSummary(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// AlarmSummaryQuery summary = client.fatigueAlarmSummaryQuery(terminalId,
	// startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != summary) {
	// ResFatigueAlarmSummary.Builder builder =
	// ResFatigueAlarmSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<CommonSummaryEntity> dataList = summary.getCommResultList();
	// CommonSummary.Builder resCommonSummary = CommonSummary.newBuilder();
	// for (CommonSummaryEntity commonSummary : dataList) {
	// resCommonSummary.setCalculatedAT(commonSummary.getCalculatedAT());
	// resCommonSummary.setEndTime(commonSummary.getEndTime());
	// resCommonSummary.setRecordsTotal(commonSummary.getRecordsTotal());
	// resCommonSummary.setStartTime(commonSummary.getStartTime());
	// resCommonSummary.setTerminalID(commonSummary.getTerminalID());
	// builder.addDataList(resCommonSummary);
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getEmergencyAlarmRecords(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResEmergencyAlarmRecoredsQuery Recoreds =
	// client.emergencyAlarmRecoredsQuery(terminalId, startDate,
	// endDate, commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<EmergencyAlarm> dataList = Recoreds.getCommResultList();
	// ResEmergencyAlarmRecoreds.Builder builder =
	// ResEmergencyAlarmRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// for (EmergencyAlarm spd : dataList) {
	// EmergencyData.Builder item = EmergencyData.newBuilder();
	// item.setTerminalID(spd.getTerminalId());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setStartDate(spd.getBeginDate());
	// builder.addDataList(item);
	// }
	//
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getEmergencyAlarmSummary(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// AlarmSummaryQuery summary = client.emergencyAlarmSummaryQuery(terminalId,
	// startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != summary) {
	// ResEmergencyAlarmSummary.Builder builder =
	// ResEmergencyAlarmSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<CommonSummaryEntity> dataList = summary.getCommResultList();
	// CommonSummary.Builder resCommonSummary = CommonSummary.newBuilder();
	// for (CommonSummaryEntity commonSummary : dataList) {
	// resCommonSummary.setCalculatedAT(commonSummary.getCalculatedAT());
	// resCommonSummary.setEndTime(commonSummary.getEndTime());
	// resCommonSummary.setRecordsTotal(commonSummary.getRecordsTotal());
	// resCommonSummary.setStartTime(commonSummary.getStartTime());
	// resCommonSummary.setTerminalID(commonSummary.getTerminalID());
	// builder.addDataList(resCommonSummary);
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getPowerStayInLowerAlarmRecords(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResPowerStayInLowerAlarmRecoredsQuery Recoreds =
	// client.powerStayInLowerAlarmRecoredsQuery(terminalId,
	// startDate, endDate, commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<PowerStayInLowerAlarm> dataList = Recoreds.getCommResultList();
	// ResPowerStayInLowerAlarmRecoreds.Builder builder =
	// ResPowerStayInLowerAlarmRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// PowerStayInLowerData.Builder item = PowerStayInLowerData.newBuilder();
	// for (PowerStayInLowerAlarm spd : dataList) {
	// item.setStartDate(spd.getBeginDate());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setTerminalID(spd.getTerminalId());
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getPowerStayInLowerAlarmSummary(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	//
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// AlarmSummaryQuery summary =
	// client.powerStayInLowerAlarmSummaryQuery(terminalId, startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != summary) {
	// ResPowerStayInLowerAlarmSummary.Builder builder =
	// ResPowerStayInLowerAlarmSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<CommonSummaryEntity> dataList = summary.getCommResultList();
	// CommonSummary.Builder resCommonSummary = CommonSummary.newBuilder();
	// for (CommonSummaryEntity commonSummary : dataList) {
	// resCommonSummary.setCalculatedAT(commonSummary.getCalculatedAT());
	// resCommonSummary.setEndTime(commonSummary.getEndTime());
	// resCommonSummary.setRecordsTotal(commonSummary.getRecordsTotal());
	// resCommonSummary.setStartTime(commonSummary.getStartTime());
	// resCommonSummary.setTerminalID(commonSummary.getTerminalID());
	// builder.addDataList(resCommonSummary);
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getElectricPowerOffAlarmRecords(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResElectricPowerOffAlarmRecoredsQuery Recoreds =
	// client.electricPowerOffAlarmRecoredsQuery(terminalId,
	// startDate, endDate, commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<ElectricPowerOffAlarm> dataList = Recoreds.getCommResultList();
	// ResElectricPowerOffAlarmRecoreds.Builder builder =
	// ResElectricPowerOffAlarmRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// ElectricPowerOffData.Builder item = ElectricPowerOffData.newBuilder();
	// for (ElectricPowerOffAlarm spd : dataList) {
	// item.setStartDate(spd.getBeginDate());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setTerminalID(spd.getTerminalId());
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }
	//
	// @Override
	// public byte[] getElectricPowerOffAlarmSummary(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// AlarmSummaryQuery summary =
	// client.electricPowerOffAlarmSummaryQuery(terminalId, startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != summary) {
	// ResElectricPowerOffAlarmSummary.Builder builder =
	// ResElectricPowerOffAlarmSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<CommonSummaryEntity> dataList = summary.getCommResultList();
	// CommonSummary.Builder resCommonSummary = CommonSummary.newBuilder();
	// for (CommonSummaryEntity commonSummary : dataList) {
	// resCommonSummary.setCalculatedAT(commonSummary.getCalculatedAT());
	// resCommonSummary.setEndTime(commonSummary.getEndTime());
	// resCommonSummary.setRecordsTotal(commonSummary.getRecordsTotal());
	// resCommonSummary.setStartTime(commonSummary.getStartTime());
	// resCommonSummary.setTerminalID(commonSummary.getTerminalID());
	// builder.addDataList(resCommonSummary);
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getACCStatusAlarmRecords(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResACCStatusAlarmRecoredsQuery Recoreds =
	// client.accStatusAlarmRecoredsQuery(terminalId, startDate,
	// endDate, commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<TerACCStatus> dataList = Recoreds.getCommResultList();
	// ResACCStatusAlarmRecoreds.Builder builder =
	// ResACCStatusAlarmRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// ACCStatusData.Builder item = ACCStatusData.newBuilder();
	// for (TerACCStatus spd : dataList) {
	// item.setStartDate(spd.getBeginDate());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setAccStatus(spd.getAccStatus());
	// item.setTerminalID(spd.getTerminalId());
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }
	//
	// @Override
	// public byte[] getACCStatusAlarmSummary(List<Long> terminalId, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// AlarmSummaryQuery summary = client.accStatusAlarmSummaryQuery(terminalId,
	// startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != summary) {
	// ResACCStatusSummary.Builder builder = ResACCStatusSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<CommonSummaryEntity> dataList = summary.getCommResultList();
	// CommonSummary.Builder resCommonSummary = CommonSummary.newBuilder();
	// for (CommonSummaryEntity commonSummary : dataList) {
	// resCommonSummary.setCalculatedAT(commonSummary.getCalculatedAT());
	// resCommonSummary.setEndTime(commonSummary.getEndTime());
	// resCommonSummary.setRecordsTotal(commonSummary.getRecordsTotal());
	// resCommonSummary.setStartTime(commonSummary.getStartTime());
	// resCommonSummary.setTerminalID(commonSummary.getTerminalID());
	// builder.addDataList(resCommonSummary);
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }
	//
	// @Override
	// public byte[] getMileagesRecords(List<Long> terminalId, long startDate,
	// long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	//
	// ResMileagesRecoredsQuery Recoreds =
	// client.mileagesRecoredsQuery(terminalId, startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<Mileages> dataList = Recoreds.getCommResultList();
	// ResMileagesRecoreds.Builder builder = ResMileagesRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// MileagesData.Builder item = MileagesData.newBuilder();
	// for (Mileages spd : dataList) {
	// item.setTerminalID(spd.getTerminalID());
	// item.setMileage((int) spd.getMileage());
	// item.setStartDate(spd.getStartDate());
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setEndDate(spd.getEndDate());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLng());
	// item.setBeginMileage((int) spd.getBeginMileage());
	// item.setEndMileage((int) spd.getEndMileage());
	// item.setStaticDate((int) (spd.getEndDate() - spd.getStartDate()));
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	@Override
	public byte[] getMileageAndOilData(List<Long> terminalId) {
		try {
			// 获取到MM动态下发的DSA节点
			long startTime = System.currentTimeMillis();
			log.error("最新里程能耗数据检索-getMileageAndOilData,开始时间(ms)：   " + RequestUtil.ChangeDate(startTime / 1000));
			AlarmStatisticsQueryService client = RMIConnctorManager.getInstance().getAlarmStatisticsQueryService();
			Map<Long, MileageAndOilData> Recoreds = client.mileagesRecoredsQuery(terminalId);
			if (null != Recoreds) {
				if (Recoreds.size() > 0) {
					MileageAndOilDataRes.Builder builder = MileageAndOilDataRes.newBuilder();
					// builder.setStatusCode(Recoreds.getStatusCode());
					// builder.setTotalRecords(Recoreds.size());
					for (MileageAndOilData data : Recoreds.values()) {
						/*MileageAndOilData.Builder item = MileageAndOilData.newBuilder();
						MileageConsumption spd = Recoreds.get(tid);
						//item.setBatteryPower(spd.getElectricConsumption());
						item.setCurrentDate(spd.getEndDate());
						item.setMileage(spd.getMileage());
						item.setOil(spd.getOilConsumption());
						item.setTerminalId(spd.getTerminalID());
						//item.setTerminalMileage(spd.getTerminalMileage());
						//item.setMeterMileage(spd.getMeterMileage());
						//item.setFuelOil(spd.getFuelOil());
						item.setOilValue(spd.getOilValue());*/
						builder.addRecords(data);
					}
					long endTime = System.currentTimeMillis();
					log.error("最新里程能耗数据检索-getMileageAndOilData,结束时间(ms)：   " + RequestUtil.ChangeDate(endTime / 1000));
					log.error("最新里程能耗数据检索-getMileageAndOilData,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
					return builder.build().toByteArray();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public byte[] getMileageConsumptionRecords(List<Long> terminalID, long startDate, long endDate,
											   CommonParameter commonParameter) {
		return this.getMileagesSummary(terminalID, startDate, endDate, commonParameter);
	}

	public byte[] getMileagesSummary(List<Long> terminalId, long startDate, long endDate,
									 CommonParameter commonParameter) {
		// 2016.05.26修改为直接调用DA接口——王景康
		try {
			long startTime = System.currentTimeMillis();
			log.error("里程能耗数据检索-getMileagesSummary,开始时间(ms)：   " + RequestUtil.ChangeDate(startTime / 1000));
			StatisticsQueryService service = RMIConnctorManager.getInstance().getStaticsQueryService();
			// 获取到MM动态下发的DSA节点
			// StatementQueryServer client =
			// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(commonParameter.getCode());
			log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " + commonParameter.getAccessTocken()+",isPage:"+commonParameter.isMultipage()+",pageIndex:"+commonParameter.getPageIndex()+",pageSize:"+commonParameter.getPageSize());

			// ResMileagesSummaryQuery
			// summary=client.mileagesSummaryQuery(terminalId, startDate,
			// endDate, commonParameter, commonParameter.getQueryKey());
			MileageConsumptionRecords consumptionRecords = service.getMileageConsumptionRecords(terminalId, startDate,
					endDate, commonParameter);
			long endTime = System.currentTimeMillis();
			log.error("里程能耗数据检索-getMileagesSummary,结束时间(ms)：   " + RequestUtil.ChangeDate(endTime / 1000));
			log.error("里程能耗数据检索-getMileagesSummary,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			if (consumptionRecords != null) {
				if (consumptionRecords.getStatusCode() == 1) {
					return consumptionRecords.toByteArray();
				} else {
					log.info("里程能耗数据检索发生错误，状态码为：" + consumptionRecords.getStatusCode());
				}
			} else {
				log.info("里程能耗数据检索查询为空");
			}
			// log.error(""+builder.build().toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	// @Override
	// public byte[] getDriverLoginRecords(List<Long> terminalId, String
	// driverId, long startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResDriverLoginRecordsQuery Recoreds =
	// client.driverLoginRecordsQuery(terminalId, driverId, startDate,
	// endDate, commonParameter, commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<DriverLogin> dataList = Recoreds.getCommResultList();
	// ResDriverloginRecoreds.Builder builder =
	// ResDriverloginRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// DriverLoginData.Builder item = DriverLoginData.newBuilder();
	// for (DriverLogin spd : dataList) {
	// item.setBeginLat(spd.getBeginLat());
	// item.setBeginLng(spd.getBeginLng());
	// item.setEndLat(spd.getEndLat());
	// item.setEndLng(spd.getEndLNG());
	// item.setTerminalID(spd.getTerminalId());
	// if (null != spd.getDriverName()) {
	// item.setDriverName(spd.getDriverName());
	// }
	// if (null != spd.getDriverIdCode()) {
	// item.setDriverIDCode(spd.getDriverIdCode());
	// }
	// if (null != spd.getDriverCertificate()) {
	// item.setDriverCertificate(spd.getDriverCertificate());
	// }
	// if (null != spd.getAgencyName()) {
	// item.setAgencyName(spd.getAgencyName());
	// }
	// if (null != spd.getCertificateValid()) {
	// item.setCertificateExpiredDate(spd.getCertificateValid());
	// }
	// item.setCheckInDate(spd.getCreditDate());
	// item.setCheckOutDate(spd.getStubbsCard());
	// item.setLogintimes(spd.getDriverTime());
	// if (null != spd.getFailedReason()) {
	// item.setFailedReasons(spd.getFailedReason());
	// }
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	// log.error(ExceptionUtils.getFullStackTrace(e));
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getTerminalOnOffLineStatusRecords(List<Long> terminalId,
	// long startDate, long endDate, int type,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResTerminalOnOffLineStatusRecordsQuery Recoreds =
	// client.terminalOnOffLineStatusRecordsQuery(terminalId,
	// type, startDate, endDate, commonParameter,
	// commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<TerminalOnOffLineStatus> dataList = Recoreds.getCommResultList();
	// ResTerminalOnOffLineStatusRecoreds.Builder builder =
	// ResTerminalOnOffLineStatusRecoreds
	// .newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// TerOnOffStatusData.Builder item = TerOnOffStatusData.newBuilder();
	// for (TerminalOnOffLineStatus spd : dataList) {
	// item.setStartDate(spd.getBeginDate());
	// item.setTerminalID(spd.getTerminalId());
	// item.setOnlineStatus(spd.getOnlineStatus());
	// item.setContinuousTimes(spd.getContinuousTime());
	// item.setEndDate(spd.getEndDate());
	//
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// @Override
	// public byte[] getWFlowRecords(List<Long> terminalId, long startDate, long
	// endDate, CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// ResWFlowRecoredsQuery Recoreds = client.wflowRecoredsQuery(terminalId,
	// startDate, endDate, commonParameter,
	// commonParameter.getQueryKey());
	// if (null != Recoreds) {
	// if (Recoreds.getStatusCode() == 1) {
	// List<WFlow> dataList = Recoreds.getCommResultList();
	// ResWFlowRecoreds.Builder builder = ResWFlowRecoreds.newBuilder();
	// builder.setStatusCode(Recoreds.getStatusCode());
	// builder.setTotalRecords(Recoreds.getTotalRords());
	// WFlowData.Builder item = WFlowData.newBuilder();
	// for (WFlow spd : dataList) {
	// item.setTerminalID(spd.getTerminalId());
	// item.setBeginDate(spd.getBeginDate());
	// item.setEndDate(spd.getEndDate());
	// item.setUpFlow(spd.getUpFlow());
	// item.setDownFlow(spd.getDownFlow());
	// builder.addDataList(item);
	// }
	// return builder.build().toByteArray();
	// }
	// }
	// } catch (Exception e) {
	//
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }
	//
	// @Override
	// public byte[] getWFlowSummary(List<Long> terminalId, long startDate, long
	// endDate, CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	//
	// ResWFlowSummaryQuery summary = client.wflowSummaryQuery(terminalId,
	// startDate, endDate, commonParameter,
	// commonParameter.getQueryKey());
	// if (null != summary) {
	// ResWFlowSummary.Builder builder = ResWFlowSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<WFSummary> dataList = summary.getCommResultList();
	// WFlowData.Builder resCommonSummary = WFlowData.newBuilder();
	// for (WFSummary wf : dataList) {
	// resCommonSummary.setBeginDate(wf.getBeginDate());
	// resCommonSummary.setEndDate(wf.getEndDate());
	// resCommonSummary.setTerminalID(wf.getTerminalId());
	// resCommonSummary.setUpFlow(wf.getUpFlow());
	// resCommonSummary.setDownFlow(wf.getDownFlow());
	// builder.addDataList(resCommonSummary);
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }
	//
	// @Override
	// public byte[] getAllAlarmSummary(List<Long> terminalId, long startDate,
	// long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	//
	// ResAllAlarmSummaryQuery summary = client.allAlarmSummaryQuery(terminalId,
	// startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// log.error("ST: " + startDate + ", ET:" + endDate + ", Types:[ ]");
	// if (null != summary) {
	// ResAllAlarmSummary.Builder builder = ResAllAlarmSummary.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRords());
	// List<com.lc.core.protocol.rmi.statistic.alarm.AllAlarmData> dataList =
	// summary.getCommResultList();
	// for (com.lc.core.protocol.rmi.statistic.alarm.AllAlarmData allAlarmData :
	// dataList) {
	// AlarmData.Builder item = AlarmData.newBuilder();
	// item.setAreaInOut(allAlarmData.getAreaInOut());
	// item.setAreaOS(allAlarmData.getAreaOS());
	// item.setElectricPOff(allAlarmData.getElectricPOff());
	// item.setEmergecy(allAlarmData.getEmergecy());
	// item.setFatigue(allAlarmData.getFatigue());
	// item.setOffRLine(allAlarmData.getOffRLine());
	// item.setOverSpd(allAlarmData.getOverSpd());
	// item.setOverTP(allAlarmData.getOverTP());
	// item.setPowerSIW(allAlarmData.getPowerSIW());
	// item.setRLineOSpd(allAlarmData.getrLineOSpd());
	// item.setRLineOT(allAlarmData.getrLineOT());
	// item.setRouteIO(allAlarmData.getRouteIO());
	// item.setTerminalID(allAlarmData.getTerminalID());
	//
	// builder.addDataList(item.build());
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }
	//
	// @Override
	// public byte[] getTerminalOnlinePercentage(List<Long> terminalIds, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// // 获取到MM动态下发的DSA节点
	// try {
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken());
	// TerminalOnlineRecordsQuery terminalOnlineRecordsQuery =
	// client.getTerminalOnlinePercentage(terminalIds,
	// startDate, endDate, commonParameter, commonParameter.getQueryKey());
	// if (terminalOnlineRecordsQuery != null) {
	// // 创建返回protobuf对象
	// TerminalOnlineRecords.Builder builder =
	// TerminalOnlineRecords.newBuilder();
	// builder.setStatusCode(terminalOnlineRecordsQuery.getStatusCode());
	// builder.setTotalRecords(terminalOnlineRecordsQuery.getTotalRords());
	// for (TerminalOnlinePercentage terminalOnlinePercentage :
	// terminalOnlineRecordsQuery.getDataList()) {
	// OnlinePercentage.Builder onlinePercentageBuilder =
	// OnlinePercentage.newBuilder();
	// onlinePercentageBuilder.setTerminalId(terminalOnlinePercentage.getTerminalId());
	// onlinePercentageBuilder.setStatisticDay(terminalOnlinePercentage.getStatisticDay());
	// onlinePercentageBuilder.setOnlineDay(terminalOnlinePercentage.getOnlineDay());
	// onlinePercentageBuilder.setOnlinePercentage(terminalOnlinePercentage.getOnlinePercentage());
	// builder.addPercentages(onlinePercentageBuilder.build());
	// }
	// return builder.build().toByteArray();
	// }
	//
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }
	//
	// @Override
	// public byte[] getManageBoardRecords(List<Long> terminalIds, long
	// startDate, long endDate,
	// CommonParameter commonParameter) {
	// try {
	// // 获取到MM动态下发的DSA节点
	// StatementQueryServer client =
	// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(
	// commonParameter.getCode());
	// log.error("ST: " + startDate + " ,ET:" + endDate + ", tocken: " +
	// commonParameter.getAccessTocken()
	// + ",dsa:" + commonParameter.getCode());
	//
	// ResManageBoardRecoredsEntity summary =
	// client.getManageBoardRecords(terminalIds, startDate, endDate,
	// commonParameter, commonParameter.getQueryKey());
	// if (null != summary) {
	// ResManageBoardRecoreds.Builder builder =
	// ResManageBoardRecoreds.newBuilder();
	// builder.setStatusCode(summary.getStatusCode());
	// builder.setTotalRecords(summary.getTotalRecords());
	// List<ManageBoardEntity> dataList = summary.getDataList();
	// for (ManageBoardEntity entity : dataList) {
	// ManageBoard.Builder item = ManageBoard.newBuilder();
	// item.setTerminalID(entity.getTerminal_id());
	// item.setAccTime(entity.getAccTime());
	// item.setOverSpeedTimes(entity.getOverSpeedTimes());
	// item.setMileage(entity.getMileage());
	// item.setFatigueTimes(entity.getFatigueTimes());
	// item.setOilConsumption(entity.getOilConsumption());
	// builder.addDataList(item.build());
	// }
	// return builder.build().toByteArray();
	// }
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// return null;
	// }

	@Override
	public byte[] getOvertimeParkRecords(List<Long> terminalID, List<Long> areaIds, long startDate, long endDate,
										 CommonParameter commonParameter) {
		// 2016.05.26修改为直接调用DA接口——王景康
		try {
			long startTime = System.currentTimeMillis();
			log.error("滞留超时检索-getOvertimeParkRecords,开始时间(ms)：   " + RequestUtil.ChangeDate(startTime / 1000));
			StatisticsQueryService service = RMIConnctorManager.getInstance().getStaticsQueryService();
			// 获取到MM动态下发的DSA节点
			// StatementQueryServer client =
			// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(commonParameter.getCode());
			log.error("ST: " + startDate + " ,ET:" + endDate + ", areaIds size: " + ((areaIds != null) ? areaIds.size() : -1)
					+ ", terminalID size: " + terminalID.size()+",isPage:"+commonParameter.isMultipage()+",pageIndex:"+commonParameter.getPageIndex()+",pageSize:"+commonParameter.getPageSize());
			OvertimeParkRecoreds recoreds = service.getOvertimeParkRecords(terminalID, areaIds, startDate, endDate,
					commonParameter);
			if (null != recoreds) {
				if (recoreds.getStatusCode() == 1) {
					List<OvertimePark> dataList = recoreds.getDataListList();
					OvertimeParkRecoreds.Builder builder = OvertimeParkRecoreds.newBuilder();
					builder.setStatusCode(recoreds.getStatusCode());
					if (null != dataList) {
						builder.setTotalRecords(recoreds.getTotalRecords());
					} else {
						builder.setTotalRecords(0);
					}
					for (OvertimePark spd : dataList) {
						OvertimePark.Builder item = OvertimePark.newBuilder();
						if (null == spd.getId()) {
							item.setId("");
						} else {
							item.setId(spd.getId());
						}
						item.setTerminalId(spd.getTerminalId());
						item.setAreaId(spd.getAreaId());
						item.setContinuousTime(spd.getContinuousTime());
						item.setLimitParking(spd.getLimitParking());
						item.setBeginDate(spd.getBeginDate());
						item.setBeginLat(spd.getBeginLat());
						item.setBeginLng(spd.getBeginLng());
						item.setEndDate(spd.getEndDate());
						item.setEndLat(spd.getEndLat());
						item.setEndLng(spd.getEndLng());
						builder.addDataList(item);
					}
					log.info("滞留超时报表查询的结果个数为：" + builder.getDataListCount());
					long endTime = System.currentTimeMillis();
					log.error("滞留超时检索-getOvertimeParkRecords,结束时间(ms)：   " + RequestUtil.ChangeDate(endTime / 1000));
					log.error("滞留超时检索-getOvertimeParkRecords,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
					return builder.build().toByteArray();
				} else {
					log.info("滞留超时报表发生错误，状态码为：" + recoreds.getStatusCode());
				}
			} else {
				log.info("滞留超时报表查询为空：" + terminalID.toString() + "areaId:" + areaIds);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public byte[] getStaytimeParkRecords(List<Long> terminalID, List<Long> areaIds, long startDate, long endDate,
										 CommonParameter commonParameter) {
		// 2016.05.26修改为直接调用DA接口——王景康
		try {
			long startTime = System.currentTimeMillis();
			log.error("区域停留时长检索-getStaytimeParkRecords,开始时间(ms)：   " + RequestUtil.ChangeDate(startTime / 1000));
			StatisticsQueryService service = RMIConnctorManager.getInstance().getStaticsQueryService();
			// 获取到MM动态下发的DSA节点
			// StatementQueryServer client =
			// RMIConnctorManager.getInstance().getStatementQueryServerByDsacode(commonParameter.getCode());
			log.error("ST: " + startDate + " ,ET:" + endDate + ", areaIds size: " + ((areaIds != null) ? areaIds.size() : -1)
					+ ", terminalID size: " + terminalID.size()+",isPage:"+commonParameter.isMultipage()+",pageIndex:"+commonParameter.getPageIndex()+",pageSize:"+commonParameter.getPageSize());
			StaytimeParkRecoreds recoreds = service.getStaytimeParkRecords(terminalID, areaIds, startDate, endDate,
					commonParameter);
			if (null != recoreds) {
				if (recoreds.getStatusCode() == 1) {
					List<StaytimePark> dataList = recoreds.getDataListList();
					StaytimeParkRecoreds.Builder builder = StaytimeParkRecoreds.newBuilder();
					builder.setStatusCode(recoreds.getStatusCode());
					if (null != dataList) {
						builder.setTotalRecords(recoreds.getTotalRecords());
					} else {
						builder.setTotalRecords(0);
					}
					for (StaytimePark spd : dataList) {
						StaytimePark.Builder item = StaytimePark.newBuilder();
						if (null == spd.getId()) {
							item.setId("");
						} else {
							item.setId(spd.getId());
						}
						item.setTerminalId(spd.getTerminalId());
						item.setAreaId(spd.getAreaId());
						item.setContinuousTime(spd.getContinuousTime());
						item.setBeginDate(spd.getBeginDate());
						item.setBeginLat(spd.getBeginLat());
						item.setBeginLng(spd.getBeginLng());
						item.setEndDate(spd.getEndDate());
						item.setEndLat(spd.getEndLat());
						item.setEndLng(spd.getEndLng());
						builder.addDataList(item);
					}
					log.info("区域停留时长报表查询的结果个数为：" + builder.getDataListCount());
					long endTime = System.currentTimeMillis();
					log.error("区域停留时长检索-getStaytimeParkRecords,结束时间(ms)：   " + RequestUtil.ChangeDate(endTime / 1000));
					log.error("区域停留时长检索-getStaytimeParkRecords,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
					return builder.build().toByteArray();
				} else {
					log.info("区域停留时长报表发生错误，状态码为：" + recoreds.getStatusCode());
				}
			} else {
				log.info("区域停留时长报表查询为空：" + terminalID.toString() + "areaId:" + areaIds);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public byte[] delOvertimeParkRecords(long accessTocken, String _id, long recordDate) {
		// 获取到MM动态下发的DA节点
		// AlarmStatisticsStoreService来自DA， 不必修改！——王景康，2016.05.26
		try {
			AlarmStatisticsStoreService daServer = (AlarmStatisticsStoreService) RMIConnctorManager.getInstance()
					.getAlarmStatisticsStoreService();
			log.error("删除滞留超时报表，key: " + _id);
			PlatformResponseResult result = daServer.delOvertimeParkStatisticInfo(_id, recordDate);
			DelOvertimeParkResult.Builder del = DelOvertimeParkResult.newBuilder();
			del.setStatusCode(result.getNumber());
			return del.build().toByteArray();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public byte[] getVehiclePassTimesRecords(int districtCode, long startDate, long endDate, long accessTocken) {
		// 2016.05.26修改为直接调用DA接口——王景康
		try {
			long startTime = System.currentTimeMillis();
			log.error("网格车次检索-getVehiclePassTimesRecords,开始时间(ms)：   " + RequestUtil.ChangeDate(startTime / 1000));
			StatisticsQueryService service = RMIConnctorManager.getInstance().getStaticsQueryService();
			// StatementQueryServer client =
			// RMIConnctorManager.getInstance().getDSAStatementQueryServer();
			log.error("ST: " + startDate + " ,ET:" + endDate + ", districtCode: " + districtCode);

			VehiclePassTimesRecords records = service.getVehiclePassTimesRecords(districtCode, startDate, endDate);
			long endTime = System.currentTimeMillis();
			log.error("网格车次检索-getVehiclePassTimesRecords,结束时间(ms)：   " + RequestUtil.ChangeDate(endTime / 1000));
			log.error("网格车次检索-getVehiclePassTimesRecords,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			return records.toByteArray();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public byte[] getVehiclePassInArea(List<Integer> districtCodes, int type, long startDate, long endDate,
									   long accessTocken) {
		// 2016.05.26修改为直接调用DA接口——王景康
		try {
			long startTime = System.currentTimeMillis();
			log.error("区域车次检索-getVehiclePassInArea,开始时间(ms)：   " + RequestUtil.ChangeDate(startTime / 1000));
			StatisticsQueryService service = RMIConnctorManager.getInstance().getStaticsQueryService();
			// StatementQueryServer client =
			// RMIConnctorManager.getInstance().getDSAStatementQueryServer();
			log.error("ST: " + startDate + " ,ET:" + endDate + ", districtCode: " + districtCodes + ",type:" + type);

			VehiclePassInAreaRecords records = service.getVehiclePassInArea(districtCodes, type, startDate, endDate);
			long endTime = System.currentTimeMillis();
			log.error("区域车次检索-getVehiclePassInArea,结束时间(ms)：   " + RequestUtil.ChangeDate(endTime / 1000));
			log.error("区域车次检索-getVehiclePassInArea,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			if (null != records) {
				if (records.getStatusCode() == 1) {
					return records.toByteArray();
				} else {
					log.info("区域车次检索发生错误，状态码为：" + records.getStatusCode());
				}
			} else {
				log.info("区域车次检索查询为空");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public byte[] getGridCrossCounts(List<Long> terminalIds, List<Long> tileId, long startDate, long endDate,
									 long accessTocken) {
		// RpQueryKeyService来自DA，不必修改——王景康，2016.05.26
		try {
			RpQueryKeyService daService = RMIConnctorManager.getInstance().getRpQueryKeyService();
			long startTime = System.currentTimeMillis();
			log.error("区域车次检索-getGridCrossCounts,开始时间(ms)：   " + RequestUtil.ChangeDate(startTime / 1000));
			CrossGridCounts gridCrossCounts = daService.getGridCrossCounts(terminalIds, tileId, startDate, endDate,
					accessTocken);
			long endTime = System.currentTimeMillis();
			log.error("区域车次检索-getGridCrossCounts,,结束时间(ms)：   " + RequestUtil.ChangeDate(endTime / 1000));
			log.error("区域车次检索-getGridCrossCounts,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			return gridCrossCounts.toByteArray();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public byte[] getVehiclePassTimesBytileId(List<Long> tileIds, long startDate, long endDate, long accessTocken) {
		// AlarmStatisticsStoreService来自DA， 不必修改！——王景康，2016.05.26
		try {
			if (null != tileIds && tileIds.size() > 0) {
				AlarmStatisticsQueryService daService = RMIConnctorManager.getInstance()
						.getAlarmStatisticsQueryService();
				ResVehiclePassTimesRecordsQuery recoreds = daService.getVehiclePassTimesBytileId(tileIds, startDate,
						endDate);
				byte[] proto = ConvertKit.convertEntity2Proto(recoreds);
				return proto;
			} else {
				log.info("根据瓦片ID查询车次：传入的瓦片ID列表为空或size为0.");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public byte[] getFaultCodeRecords(List<Long> terminalID, int spn, int fmi, long startDate, long endDate,
									  CommonParameter commonParameter) {
		// 2016.05.26修改为直接调用DA接口——王景康
		try {
			long startTime = System.currentTimeMillis();
			log.error("故障码检索-getFaultCodeRecords,开始时间(ms)：   " + RequestUtil.ChangeDate(startTime / 1000));
			StatisticsQueryService service = RMIConnctorManager.getInstance().getStaticsQueryService();
			// StatementQueryServer client =
			// RMIConnctorManager.getInstance().getDSAStatementQueryServer();
			log.error("ST: " + startDate + " ,ET:" + endDate + ", terminalID: " + terminalID.size() + ",spn:" + spn
					+ ",fmi:" + fmi+",isPage:"+commonParameter.isMultipage()+",pageIndex:"+
					commonParameter.getPageIndex()+",pageSize:"+commonParameter.getPageSize());

			FaultCodeRecords records = service.getFaultCodeRecords(terminalID, spn, fmi, startDate, endDate,
					commonParameter);
			long endTime = System.currentTimeMillis();
			log.error("故障码检索-getFaultCodeRecords,结束时间(ms)：   " + RequestUtil.ChangeDate(endTime / 1000));
			log.error("故障码检索-getFaultCodeRecords,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
			if (records != null) {
				if (records.getStatusCode() == 1) {
					return records.toByteArray();
				} else {
					log.info("故障码检索发生错误，状态码为：" + records.getStatusCode());
				}
			} else {
				log.info("故障码检索查询为空");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public byte[] getLastestVehiclePassInArea(List<Integer> districtCodes, long accessTocken) {
		// 4.3.4 区域最新车辆检索 hxw
		try {
			StatisticsQueryService service = RMIConnctorManager.getInstance().getStaticsQueryService();
			log.error("DistrictCodes:" + districtCodes.size());
			GetLastestVehiclePassInAreaRes records = service.getLastestVehiclePassInArea(districtCodes);
			if (records != null) {
				if (records.getStatusCode() == 1) {
					return records.toByteArray();
				} else {
					log.info("区域最新车辆检索发生错误，状态码为：" + records.getStatusCode());
				}
			} else {
				log.info("区域最新车辆检索查询为空");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public byte[] getStagnationTimeoutRecords(List<Long> terminalID,
											  long startDate, long endDate, CommonParameter commonParameter) {
		long startTime = System.currentTimeMillis();
		try {
			StatisticsQueryService service = RMIConnctorManager.getInstance().getStaticsQueryService();
			log.error("terminalID: " + terminalID.size() + " ,startDate:" + startDate + ", endDate: " + endDate +
					",isPage:"+commonParameter.isMultipage()+",pageIndex:"+commonParameter.getPageIndex()+",pageSize:"+commonParameter.getPageSize());
			GetStagnationTimeoutRes data = service.getStagnationTimeoutRecords(terminalID,startDate,endDate,commonParameter);
			if(data != null){
				long endTime = System.currentTimeMillis();
				log.error("停滞超时检索-getStagnationTimeoutRecords,耗时：" + (endTime - startTime) / 1000.0 + " 秒");
				return data.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public byte[] stagnationTimeoutCancelOrNot(String _id, boolean isCancel,
											   long recordDate, long accessTocken) {
		try {
			AlarmStatisticsStoreService daServer = (AlarmStatisticsStoreService) RMIConnctorManager.getInstance()
					.getAlarmStatisticsStoreService();
			log.error("停滞超时撤销和恢复,记录标识 key: " + _id);
			PlatformResponseResult result = daServer.stagnationTimeoutCancelOrNot(_id, isCancel,recordDate);
			StagnationTimeoutCancelOrNotResult.Builder del = StagnationTimeoutCancelOrNotResult.newBuilder();
			del.setStatusCode(result.getNumber());
			return del.build().toByteArray();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public byte[] GetHistoryParking(List<Long> terminalIDs, long startDate, long endDate, int parkLimit,
									CommonParameter commonParameter) {
		// TODO Auto-generated method stub
		/*try {
			StatisticsQueryService service = RMIConnctorManager.getInstance().getStaticsQueryService();
			log.error("terminalID: " + terminalIDs.size() + " ,startDate:" + startDate + ", endDate: " + endDate +",parkLimit"+parkLimit+
					",isPage:"+commonParameter.isMultipage()+",pageIndex:"+commonParameter.getPageIndex()+",pageSize:"+commonParameter.getPageSize());
//			PlatformResponseResult result = daServer.stagnationTimeoutCancelOrNot(_id, isCancel,recordDate);
			GetHistoryParkingRes parkingRes = service.getHistoryParkingResRecords(terminalIDs, startDate, endDate,
					parkLimit, commonParameter);
			if (parkingRes != null) {
				return parkingRes.toByteArray();}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}*/
		return null;
	}

	/**
	 * 里程能耗实时计算
	 */
	@Override
	public byte[] calculateMileageConsumption(long terminalId, long startDate,
											  long endDate, long accessTocken) {
		// TODO Auto-generated method stub
		try {
			log.error("terminalId: "+terminalId +" ,startDate: "+startDate+" , endDate: "+endDate);
			StatisticsQueryService service = RMIConnctorManager.getInstance().getStaticsQueryService();
			CalculateMileageConsumptionRes calRes = service.calculateMileageConsumption(terminalId,startDate,endDate,accessTocken);
			if(calRes != null){
				log.error(terminalId+"-->实时计算出来的里程能耗值为： 里程="+calRes.getData().getMileage()+"  ,燃油消耗="+calRes.getData().getOilConsumption()
						+",仪表油量消耗="+calRes.getData().getOilValue());
				return calRes.toByteArray();
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
		}
		return null;
	}
}

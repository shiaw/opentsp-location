package com.lc.rp.webService.service;


import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.Point;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;
import java.util.List;

/*****************
 *
 *
 * 对应ws接口文档里面的基础数据查询
 *
 */
@WebService
public interface BasicDataQueryWebService {

//	@WebMethod
//	public String getHelloworld();
	/**
	 * 根据终端编号，区域编号，类型，鉴权码获取终端规则数据
	 * @param terminalIds
	 * @param areaIds
	 * @param type
	 * @param accessTocken 鉴权码 
	 * @return byte[] 字节数组
	 */
	@WebMethod
	public byte[] getTerminalRegular(
			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
			@WebParam(name = "areaIds", mode = Mode.IN) List<Long> areaIds,
			@WebParam(name = "type", mode = Mode.IN) boolean type,
			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken
	);

	/**
	 * 根据终端标识和时间段检索车辆轨迹数据：
	 * @param terminalId
	 * @param startDate
	 * @param endDate
	 * @param isFilterAlarm
	 * @param isAllAlarm
	 * @param alarms
	 * @param commonParameter 通用参数
	 * @return
	 */
	@WebMethod
	public byte[] getTerminalTrack(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalIds,
			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
			@WebParam(name = "isFilterBreakdown", mode = Mode.IN) boolean isFilterBreakdown,
			@WebParam(name = "breakdownCode", mode = Mode.IN) long breakdownCode,
			@WebParam(name = "isThin",mode = Mode.IN) boolean isThin,
			@WebParam(name = "level",mode = Mode.IN) int level,
			@WebParam(name = "isAll",mode = Mode.IN) int isAll,
			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter
	);

	/**
	 * 检索车辆在指定的时间段是否经过区域，检索后返回车辆轨迹的第一个点和最后一个点。
	 * @param terminalIds
	 * @param beginDate
	 * @param endDate
	 * @param leftLongitude
	 * @param rightLongitude
	 * @param topLatitude
	 * @param bottomLatitude
	 * @param accessTocken
	 * @return
	 */
//	@WebMethod
//	public byte[] getCrossAreaTrack(
//			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
//			@WebParam(name = "beginDate", mode = Mode.IN) long beginDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "leftLongitude", mode = Mode.IN) int leftLongitude,
//			@WebParam(name = "rightLongitude", mode = Mode.IN) int rightLongitude,
//			@WebParam(name = "topLatitude", mode = Mode.IN) int topLatitude,
//			@WebParam(name = "bottomLatitude", mode = Mode.IN) int bottomLatitude,
//			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken
//			);

	/**
	 * 检索车辆经过区域的次数
	 * @param terminalIds
	 * @param beginDate
	 * @param endDate
	 * @param areaType
	 * @param points
	 * @param accessTocken
	 * @return
	 */
//	@WebMethod
//	public byte[] getCrossAreaCounts(
//			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
//			@WebParam(name = "beginDate", mode = Mode.IN) long beginDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "areaType", mode = Mode.IN) int areaType,
//			@WebParam(name = "points", mode = Mode.IN) List<Point> points,
//			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken
//	);
//	@WebMethod
//	public byte[] getCrossAreaCounts2(
//			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
//			@WebParam(name = "beginDate", mode = Mode.IN) long beginDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "areaType", mode = Mode.IN) int areaType,
//			@WebParam(name = "points", mode = Mode.IN) List<Point> points,
//			@WebParam(name = "width", mode = Mode.IN) int width,
//			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken
//			);

	/**
	 * 出区域限速检索
	 * @param terminalIds
	 * @param beginDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 */
//	@WebMethod
//	public byte[] getOutRegionLimitSpeed(
//			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
//			@WebParam(name = "beginDate", mode = Mode.IN) long beginDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter
//			);
	/**
	 * 3.6	CAN数据检索
	 * @param terminalIds
	 * @param type 1：主动汇报CAN原始数据 2：CAN数据检索历史数据
	 * @param beginDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 */
	@WebMethod
	public byte[] getCANBusData(
			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
			@WebParam(name = "type", mode = Mode.IN) int type,
			@WebParam(name = "beginDate", mode = Mode.IN) long beginDate,
			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter
	);
	/**
	 * 3.8里程油量数据检索
	 * @param terminalIds
	 * @return
	 */
//	@WebMethod
//	public byte[] getMileageAndOilData(
//			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds
//			);
	/**
	 * 3.7	最新位置数据检索
	 * @param terminalIds
	 * @return
	 */
	@WebMethod
	public byte[] getLastestLocationData(
			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
			@WebParam(name = "mileageRange", mode = Mode.IN) String  mileageRange,
			@WebParam(name = "type", mode = Mode.IN) int type,
			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter
	)throws Exception;
	/**
	 * 3.2	位置数据检索
	 * @param terminalIds
	 * @return
	 */
	@WebMethod
	public byte[] getTerminalLocationData(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
			@WebParam(name = "queryDate", mode = Mode.IN) long queryDate,
			@WebParam(name = "index", mode = Mode.IN) int index,
			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken
	);
	/**
	 * 3.4	区域查车
	 * @param terminalIds
	 * @return
	 */
	@WebMethod
	public byte[] getTerminalInArea(
			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
			@WebParam(name = "type", mode = Mode.IN) int type,
			@WebParam(name = "points", mode = Mode.IN) List<Point> points,
			@WebParam(name = "radius", mode = Mode.IN) int radius,
			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken
	);
	/**
	 * 3.5	行政区域查车
	 * @param terminalIds
	 * @return
	 */
	@WebMethod
	public byte[] GetTerminalInDistrict(
			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
			@WebParam(name = "type", mode = Mode.IN) int districtCode,
			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken
	);
	/**
	 * 3.9	根据报警撤销筛选最新位置
	 * @param terminalIds
	 * @param isFilter
	 * @param alarmFilter
	 * @param type
	 * @param commonParameter
	 * @return
	 */
	@WebMethod
	public byte[] getLastestLocationDataByAlarmFilter(
			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
			@WebParam(name = "isFilter", mode = Mode.IN) boolean isFilter,
			@WebParam(name = "alarmType", mode = Mode.IN) long alarmType,
			@WebParam(name = "isCancel", mode = Mode.IN) int isCancel,
			@WebParam(name = "type", mode = Mode.IN) int type,
			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter
	);
	/**
	 * 3.10	终端在线状态
	 * @param terminalIds
	 * @param isFilter
	 * @param alarmFilter
	 * @param type
	 * @param commonParameter
	 * @return
	 */
	@WebMethod
	public byte[] GetLastestTerminalStatus(
			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken
	);

	@WebMethod
	public byte[]GetTerminalFirstRecieveDate(
			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds);

	/**
	 * 4.8车辆末次进入服务站时间
	 * @param terminalIds
	 * @param isFilter
	 * @param alarmFilter
	 * @param type
	 * @param commonParameter
	 * @return
	 */
	@WebMethod
	public byte[] GetLatestParkRecords(
			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
			@WebParam(name = "areaIds", mode = Mode.IN) List<Long> areaIds,
			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter
	);
}

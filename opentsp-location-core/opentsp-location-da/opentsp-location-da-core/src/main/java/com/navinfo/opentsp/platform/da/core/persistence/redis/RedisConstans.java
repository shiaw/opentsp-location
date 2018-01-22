package com.navinfo.opentsp.platform.da.core.persistence.redis;

public class RedisConstans {
    public static final int LIST_PART_MAX_SIZE = Integer.MAX_VALUE;


    public enum RedisKey {
        /** 链路异常 */
        EXCEPTION_LINK_DATA,
        /** 位置数据 */
        GPS_DATA,
        /** 位置数据名称 */
        GPS_DATA_TITLE,
        /** 服务标识 */
        SERVICE_UNIQUEMARK,
        /** 最新数据 */
        LATEST_GPS_DATA,
        /** 终端状态 */
        TERMINAL_STATUS	,
        /**流量*/
        TERMINAL_FLOW,
        /**终端数据统计状态*/
        DAS_STATUS,
        /**数据统计查询缓存，DA关于数据检索的缓存（暂时）--hk*/
        DAS_QUERY_KEY,
        /**数据统计任务状态*/
        DAS_TASK_STATUS_KEY,
        /**DA数据在转存状态缓存*/
        DA_TRANSFER_STATUS_KEY,
        /**终端上下线状态缓存*/
        DA_TERMINAL_ON_OFF_STATUS,
        /** 终端数据同步动态口令缓存 **/
        DYNAMIC_PASSWORD,
        /**CAN数据转存状态缓存*/
        DA_TRANSFER_CAN_STATUS_KEY,
        /**当天是否有滞留超时报警缓存*/
        STATISTIC_STATUS_OVERTIMEPARKINAREA,
        /**CAN数据有效*/
        LASTEST_LOCATION_DATA,
        /**经纬度有效*/
        LASTEST_LOCATION_DATA_LATLNG,
        /**所有最新位置数据*/
        LASTEST_LOCATION_DATA_ALL,
        /**针对车况报表录入有效数据*/
        LASTEST_COMBINATION_LOCATION_DATA,
        /**当日里程能耗有效*/
        DAY_MILEAGE_OIL_VALUE,
        /**区域内最新车辆信息*/
        LASTEST_VEHICLE_PASS_AREA_TIMES,
        /**行政区域内最新车辆信息 MAP<DISTRICT_CODE,LIST<TID>>*/
        LASTEST_VEHICLE_PASS_IN_DISTRICT,
        /**报警撤销缓存规则*/
        ALARM_CANCEL_REGULAR,
        /**终端首次接入时间*/
        DA_TERMINAL_FIRST_RECIEVE,
        /**历史停车*/
        DA_HISTORY_PARKING,
        /**区域停车*/
        STAYTIME_PARK_ALARM,

        /**车辆最后进入服务站时间*/
        LATEST_PARK_DATA
    }

}

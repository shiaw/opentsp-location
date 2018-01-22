package com.lc.rp.webService.service;

import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import java.util.List;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;

@WebService
public abstract interface DataAnalysisWebService
{
    public abstract byte[] getOvertimeParkRecords(@WebParam(name="terminalID", mode=WebParam.Mode.IN) List<Long> paramList1, @WebParam(name="areaIds", mode=WebParam.Mode.IN) List<Long> paramList2, @WebParam(name="startDate", mode=WebParam.Mode.IN) long paramLong1, @WebParam(name="endDate", mode=WebParam.Mode.IN) long paramLong2, @WebParam(name="commonParameter", mode=WebParam.Mode.IN) CommonParameter paramCommonParameter);

    public abstract byte[] getStaytimeParkRecords(@WebParam(name="terminalID", mode=WebParam.Mode.IN) List<Long> paramList1, @WebParam(name="areaIds", mode=WebParam.Mode.IN) List<Long> paramList2, @WebParam(name="startDate", mode=WebParam.Mode.IN) long paramLong1, @WebParam(name="endDate", mode=WebParam.Mode.IN) long paramLong2, @WebParam(name="commonParameter", mode=WebParam.Mode.IN) CommonParameter paramCommonParameter);

    public abstract byte[] delOvertimeParkRecords(@WebParam(name="accessTocken", mode=WebParam.Mode.IN) long paramLong1, @WebParam(name="_id", mode=WebParam.Mode.IN) String paramString, @WebParam(name="recordDate", mode=WebParam.Mode.IN) long paramLong2);

    public abstract byte[] getVehiclePassTimesRecords(@WebParam(name="districtCode", mode=WebParam.Mode.IN) int paramInt, @WebParam(name="startDate", mode=WebParam.Mode.IN) long paramLong1, @WebParam(name="endDate", mode=WebParam.Mode.IN) long paramLong2, @WebParam(name="accessTocken", mode=WebParam.Mode.IN) long paramLong3);

    public abstract byte[] getVehiclePassInArea(@WebParam(name="districtCodes", mode=WebParam.Mode.IN) List<Integer> paramList, @WebParam(name="type", mode=WebParam.Mode.IN) int paramInt, @WebParam(name="startDate", mode=WebParam.Mode.IN) long paramLong1, @WebParam(name="endDate", mode=WebParam.Mode.IN) long paramLong2, @WebParam(name="accessTocken", mode=WebParam.Mode.IN) long paramLong3);

    public abstract byte[] getLastestVehiclePassInArea(@WebParam(name="districtCodes", mode=WebParam.Mode.IN) List<Integer> paramList, @WebParam(name="accessTocken", mode=WebParam.Mode.IN) long paramLong);

    public abstract byte[] getGridCrossCounts(@WebParam(name="terminalIds", mode=WebParam.Mode.IN) List<Long> paramList1, @WebParam(name="tileId", mode=WebParam.Mode.IN) List<Long> paramList2, @WebParam(name="startDate", mode=WebParam.Mode.IN) long paramLong1, @WebParam(name="endDate", mode=WebParam.Mode.IN) long paramLong2, @WebParam(name="accessTocken", mode=WebParam.Mode.IN) long paramLong3);

    public abstract byte[] getVehiclePassTimesBytileId(@WebParam(name="tileIds", mode=WebParam.Mode.IN) List<Long> paramList, @WebParam(name="startDate", mode=WebParam.Mode.IN) long paramLong1, @WebParam(name="endDate", mode=WebParam.Mode.IN) long paramLong2, @WebParam(name="accessTocken", mode=WebParam.Mode.IN) long paramLong3);

    public abstract byte[] getFaultCodeRecords(@WebParam(name="terminalID", mode=WebParam.Mode.IN) List<Long> paramList, @WebParam(name="spn", mode=WebParam.Mode.IN) int paramInt1, @WebParam(name="fmi", mode=WebParam.Mode.IN) int paramInt2, @WebParam(name="startDate", mode=WebParam.Mode.IN) long paramLong1, @WebParam(name="endDate", mode=WebParam.Mode.IN) long paramLong2, @WebParam(name="commonParameter", mode=WebParam.Mode.IN) CommonParameter paramCommonParameter);

    public abstract byte[] getStagnationTimeoutRecords(@WebParam(name="terminalID", mode=WebParam.Mode.IN) List<Long> paramList, @WebParam(name="startDate", mode=WebParam.Mode.IN) long paramLong1, @WebParam(name="endDate", mode=WebParam.Mode.IN) long paramLong2, @WebParam(name="commonParameter", mode=WebParam.Mode.IN) CommonParameter paramCommonParameter);

    public abstract byte[] stagnationTimeoutCancelOrNot(@WebParam(name="_id", mode=WebParam.Mode.IN) String paramString, @WebParam(name="isCancel", mode=WebParam.Mode.IN) boolean paramBoolean, @WebParam(name="recordDate", mode=WebParam.Mode.IN) long paramLong1, @WebParam(name="accessTocken", mode=WebParam.Mode.IN) long paramLong2);

    public abstract byte[] GetHistoryParking(@WebParam(name="terminalIDs", mode=WebParam.Mode.IN) List<Long> paramList, @WebParam(name="startDate", mode=WebParam.Mode.IN) long paramLong1, @WebParam(name="endDate", mode=WebParam.Mode.IN) long paramLong2, @WebParam(name="parkLimit", mode=WebParam.Mode.IN) int paramInt, @WebParam(name="commonParameter", mode=WebParam.Mode.IN) CommonParameter paramCommonParameter);

    public abstract byte[] calculateMileageConsumption(@WebParam(name="terminalID", mode=WebParam.Mode.IN) long paramLong1, @WebParam(name="startDate", mode=WebParam.Mode.IN) long paramLong2, @WebParam(name="endDate", mode=WebParam.Mode.IN) long paramLong3, @WebParam(name="accessTocken", mode=WebParam.Mode.IN) long paramLong4);
}
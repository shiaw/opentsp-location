/**
 * 
 */
package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 区域车次检索
 * @author zyl
 * @date 2015年10月28日
 */
public class VehiclePassInAreaEntity implements Serializable{
	private static final long serialVersionUID = 7158379400006513868L;
	private long _id;
	private int times;
	private List<VehiclePassDetail> dataList = new ArrayList<VehiclePassDetail>();
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public List<VehiclePassDetail> getDataList() {
		return dataList;
	}
	public void setDataList(List<VehiclePassDetail> dataList) {
		this.dataList = dataList;
	}
}

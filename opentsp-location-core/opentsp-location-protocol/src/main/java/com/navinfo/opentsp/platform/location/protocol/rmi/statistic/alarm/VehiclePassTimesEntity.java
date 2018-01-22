/**
 * 
 */
package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 4.16.1	网格车次检索
 * @author zyl
 * @date 2015年10月28日
 */
public class VehiclePassTimesEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private long _id;
	private int times;
	private List<SonAreaTimesEntity> sonAreaTimes = new ArrayList<SonAreaTimesEntity>();
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
	public List<SonAreaTimesEntity> getSonAreaTimes() {
		return sonAreaTimes;
	}
	public void setSonAreaTimes(List<SonAreaTimesEntity> sonAreaTimes) {
		this.sonAreaTimes = sonAreaTimes;
	}
	
}

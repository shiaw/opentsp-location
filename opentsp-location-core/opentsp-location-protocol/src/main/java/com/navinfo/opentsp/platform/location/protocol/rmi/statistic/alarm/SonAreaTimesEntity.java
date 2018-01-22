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
public class SonAreaTimesEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private long _id;
	private int times;
	private List<GrandSonAreaTimesEntity> grandSonAreaTimes = new ArrayList<GrandSonAreaTimesEntity>();
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
	public List<GrandSonAreaTimesEntity> getGrandSonAreaTimes() {
		return grandSonAreaTimes;
	}
	public void setGrandSonAreaTimes(List<GrandSonAreaTimesEntity> grandSonAreaTimes) {
		this.grandSonAreaTimes = grandSonAreaTimes;
	}
	
}

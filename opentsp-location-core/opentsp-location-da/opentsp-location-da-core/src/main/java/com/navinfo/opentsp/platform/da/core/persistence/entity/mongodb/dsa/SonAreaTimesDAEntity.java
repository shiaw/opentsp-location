/**
 * 
 */
package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 4.16.1	网格车次检索
 * @author zyl
 * @date 2015年10月28日
 */
public class SonAreaTimesDAEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private long tile;
	private int times;
	private List<GrandSonAreaTimesDAEntity> grandSonAreaTimes = new ArrayList<GrandSonAreaTimesDAEntity>();
	public long getTile() {
		return tile;
	}
	public void setTile(long tile) {
		this.tile = tile;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public List<GrandSonAreaTimesDAEntity> getGrandSonAreaTimes() {
		return grandSonAreaTimes;
	}
	public void setGrandSonAreaTimes(
			List<GrandSonAreaTimesDAEntity> grandSonAreaTimes) {
		this.grandSonAreaTimes = grandSonAreaTimes;
	}
	public DBObject toDbObject(){
		DBObject topic = new BasicDBObject();
		topic.put("tile", this.getTile());
		topic.put("times", this.getTimes());
		List<DBObject> dataList = new ArrayList<DBObject>(this.getGrandSonAreaTimes().size());
		for (GrandSonAreaTimesDAEntity d : this.grandSonAreaTimes) {
			dataList.add(d.toDbObject());
		}
		topic.put("dl", dataList);
		return topic;
	}
	public void dbObjectToBean(DBObject db){
		this.tile = (long) db.get("tile");
		this.times = (int) db.get("times");
		List<DBObject> dataList = (List<DBObject>) db.get("dl");
		if (dataList != null && dataList.size() > 0) {
			this.grandSonAreaTimes = new ArrayList<GrandSonAreaTimesDAEntity>(4);
			for (DBObject d : dataList) {
				GrandSonAreaTimesDAEntity entity = new GrandSonAreaTimesDAEntity();
				entity.dbObjectToBean(d);
				this.grandSonAreaTimes.add(entity);
			}
		}
	}
	@Override
	public String toString() {
		return "SonAreaTimesDAEntity [tile=" + tile + ", times=" + times
				+ ", grandSonAreaTimes=" + grandSonAreaTimes.toString() + "]";
	}
}

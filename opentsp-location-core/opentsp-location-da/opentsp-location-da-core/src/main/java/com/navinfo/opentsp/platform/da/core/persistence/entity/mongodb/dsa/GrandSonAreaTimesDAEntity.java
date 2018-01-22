/**
 * 
 */
package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 4.16.1	网格车次检索
 * @author zyl
 * @date 2015年10月28日
 */
public class GrandSonAreaTimesDAEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private long tile;
	private int times;
	private Set<AreaTerminalTimesDAEntity> tids = new HashSet<AreaTerminalTimesDAEntity>();
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
	public Set<AreaTerminalTimesDAEntity> getTids() {
		return tids;
	}
	public void setTids(Set<AreaTerminalTimesDAEntity> tids) {
		this.tids = tids;
	}
	public DBObject toDbObject(){
		DBObject topic = new BasicDBObject();
		topic.put("tile", this.getTile());
		topic.put("times", this.getTimes());
		List<DBObject> dblist = new ArrayList<DBObject>(tids.size());
		for(AreaTerminalTimesDAEntity entity : tids){
			dblist.add(entity.toDbObject());
		}
		topic.put("dl", dblist);
		return topic;
	}
	public void dbObjectToBean(DBObject db){
		this.tile = (long) db.get("tile");
		this.times = (int) db.get("times");
		List<DBObject> dataList = (List<DBObject>) db.get("dl");
		if (dataList != null && dataList.size() > 0) {
			for (DBObject d : dataList) {
				AreaTerminalTimesDAEntity entity = new AreaTerminalTimesDAEntity();
				entity.dbObjectToBean(d);
				this.getTids().add(entity);
			}
		}
	}

	@Override
	public String toString() {
		return "GrandSonAreaTimesDAEntity [tile=" + tile + ", times=" + times
				+ "]";
	}
}

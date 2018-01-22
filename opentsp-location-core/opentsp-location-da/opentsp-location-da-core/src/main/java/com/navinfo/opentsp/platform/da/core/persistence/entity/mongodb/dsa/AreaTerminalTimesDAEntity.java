/**
 * 
 */
package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa;

import java.io.Serializable;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 4.16.1	网格车次检索
 * @author zyl
 * @date 2015年10月28日
 */
public class AreaTerminalTimesDAEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private long tid;
	private int day;
	public long getTid() {
		return tid;
	}
	public void setTid(long tid) {
		this.tid = tid;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public DBObject toDbObject(){
		DBObject topic = new BasicDBObject();
		topic.put("tid", this.getTid());
		topic.put("d", this.getDay());
		return topic;
	}
	public void dbObjectToBean(DBObject db){
		this.tid = (long) db.get("tid");
		this.day = (int) db.get("d");
	}

	@Override
	public String toString() {
		return "AreaTerminalTimesDAEntity [tid=" + tid + ", day=" + day
				+ "]";
	}
}

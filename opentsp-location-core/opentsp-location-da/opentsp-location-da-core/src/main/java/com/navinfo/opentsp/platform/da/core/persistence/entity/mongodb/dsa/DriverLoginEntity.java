package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbIndex;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbName;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DADriverLogin;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;

@LCTable(name="DriverLogin")
//@LCDbName(name="DataStatisticAnalysis")
@LCDbName(name= LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS)
@LCDbIndex(name={"AA","KA","HA"})
public class DriverLoginEntity  extends BaseMongoEntity implements Serializable{
	private long _id;
	private long terminal_id;
	private long day;
	public int counts;
	public int getCounts() {
		return counts;
	}
	public void setCounts(int counts) {
		this.counts = counts;
	}
	private List<DADriverLogin> dataList;

	/**
	 * 转换为Bson
	 */
	public List<DBObject> toBatchDBObject(){
		List<DBObject> dataList = new ArrayList<DBObject>();
		if(this.dataList!=null&&this.dataList.size()!=0){
			for (DADriverLogin d : this.dataList) {
				dataList.add(d.toDBObject());
			}
		}

		return dataList;
	}
	public DBObject toDBObject(){
		DBObject topic = new BasicDBObject();
		topic.put("AA", this.getTerminal_id());//终端标识�?
		topic.put("DA", this.getDay());//日期
		List<DBObject> dataList = new ArrayList<DBObject>(this.getDataList().size());
		for (DADriverLogin d : this.dataList) {
			dataList.add(d.toDBObject());
		}
		topic.put("dataList", dataList);
		return topic;
	}
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public long getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(long terminal_id) {
		this.terminal_id = terminal_id;
	}
	public long getDay() {
		return day;
	}
	public void setDay(long day) {
		this.day = day;
	}
	public List<DADriverLogin> getDataList() {
		return dataList;
	}
	public void setDataList(List<DADriverLogin> dataList) {
		this.dataList = dataList;
	}
	public DriverLoginEntity() {
		super();
	}
}

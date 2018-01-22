package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbIndex;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbName;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAOvertimeParkAlarm;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;

/**
 * 3.15	滞留超时
 * @author zyl
 * @date 2015年10月22日
 */
@LCTable(name="OvertimeParkingInArea")
//@LCDbName(name="DataStatisticAnalysis")
@LCDbName(name= LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS)
@LCDbIndex(name={"AA","BA","KA","HA"})
public class OvertimeParkAlarmEntity  extends BaseMongoEntity implements Serializable{
	private static final long serialVersionUID = 1L;
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
	private List<DAOvertimeParkAlarm> dataList;

	/**
	 * 转换为Bson
	 */
	public List<DBObject> toBatchDBObject(){
		List<DBObject> dataList = new ArrayList<DBObject>();
		if(this.dataList!=null&&this.dataList.size()!=0){
			for (DAOvertimeParkAlarm d : this.dataList) {
				dataList.add(d.toDBObject());
			}
		}

		return dataList;
	}
	public DBObject toDBObject(){
		DBObject topic = new BasicDBObject();
		topic.put("AA", this.getTerminal_id());//终端标识
		topic.put("DA", this.getDay());// 日期
		List<DBObject> dataList = new ArrayList<DBObject>(this.getDataList()
				.size());
		for (DAOvertimeParkAlarm d : this.dataList) {
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
	public List<DAOvertimeParkAlarm> getDataList() {
		return dataList;
	}
	public void setDataList(List<DAOvertimeParkAlarm> dataList) {
		this.dataList = dataList;
	}
	public OvertimeParkAlarmEntity() {
		super();
	}
}

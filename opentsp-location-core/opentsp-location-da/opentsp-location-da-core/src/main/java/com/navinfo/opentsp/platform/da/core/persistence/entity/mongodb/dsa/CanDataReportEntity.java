package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbIndex;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbName;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DACanDataDetail;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;

/**
 * 3.15	滞留超时
 * @author zyl
 * @date 2015年10月22日
 */
@LCTable(name="CANDataEntityForReport")
//@LCDbName(name="GpsCANData")
@LCDbName(name= LCMongo.DB.LC_GPS_CANDATA)
@LCDbIndex(name={"tId","time"})
public class CanDataReportEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private long _id;
	private long tId;
	private String day;
	private List<DACanDataDetail> dataList;

	/**
	 * 转换为Bson
	 */
	/*public List<DBObject> toBatchDBObject(){
		List<DBObject> dataList = new ArrayList<DBObject>();
		if(this.dataList!=null&&this.dataList.size()!=0){
		for (DACanDataDetail d : this.dataList) {
			dataList.add(d.toDBObject());
		  }
		}

		return dataList;
	}*/
	public DBObject toDBObject(){
		DBObject topic = new BasicDBObject();
		topic.put("tId", this.gettId());//终端标识
		topic.put("day", this.getDay());//日期
		List<DBObject> dataList = new ArrayList<DBObject>(this.getDataList().size());
		for (DACanDataDetail d : this.dataList) {
			dataList.add(d.toDBObject());
		}
		topic.put("dataList", dataList);
		return topic;
	}
	public CanDataReportEntity dbObjectToBean(DBObject dbObject){
		CanDataReportEntity entity = new CanDataReportEntity();
		entity.settId((long)dbObject.get("tId"));
		entity.setDay((String)dbObject.get("day"));
		List<DACanDataDetail> list = new ArrayList<DACanDataDetail>();
		List<DBObject> dataList = (List<DBObject>) dbObject.get("dataList");
		for(DBObject object : dataList){
			list.add(new DACanDataDetail(object));
		}
		entity.setDataList(list);
		return entity;
	}
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public long gettId() {
		return tId;
	}
	public void settId(long tId) {
		this.tId = tId;
	}

	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public List<DACanDataDetail> getDataList() {
		return dataList;
	}
	public void setDataList(List<DACanDataDetail> dataList) {
		this.dataList = dataList;
	}
	public void addDataList(DACanDataDetail detail) {
		if(null == this.dataList){
			this.dataList = new ArrayList<DACanDataDetail>(100);
		}
		this.dataList.add(detail);
	}

}

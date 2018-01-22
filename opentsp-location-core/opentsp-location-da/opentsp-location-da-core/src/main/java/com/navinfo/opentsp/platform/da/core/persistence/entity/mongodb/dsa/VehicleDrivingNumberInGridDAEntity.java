/**
 * 
 */
package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import  com.navinfo.opentsp.platform.da.core.persistence.common.LCDbIndex;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCDbName;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;

/**
 * 4.16.1	网格车次检索
 * @author zyl
 * @date 2015年10月28日
 */
@LCTable(name="VehicleDrivingNumberInGrid")
//@LCDbName(name="DataStatisticAnalysis")
@LCDbName(name= LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS)
@LCDbIndex(name={"tile","district"})
public class VehicleDrivingNumberInGridDAEntity extends BaseMongoEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String _id;//mongo主键
	private long tile;
	private int times;
	private int month;
	private int district;
	private List<SonAreaTimesDAEntity> sonAreaTimes = new ArrayList<SonAreaTimesDAEntity>();
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
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
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDistrict() {
		return district;
	}
	public void setDistrict(int district) {
		this.district = district;
	}
	public List<SonAreaTimesDAEntity> getSonAreaTimes() {
		return sonAreaTimes;
	}
	public void setSonAreaTimes(List<SonAreaTimesDAEntity> sonAreaTimes) {
		this.sonAreaTimes = sonAreaTimes;
	}
	public void dbObjectToBean(DBObject db){
		this._id = db.get("_id").toString();
		this.tile = (long) db.get("tile");
		this.times = (int) db.get("times");
		this.month = (int) db.get("month");
		this.district = (int) db.get("district");
		List<DBObject> dataList = (List<DBObject>) db.get("dl");
		if (dataList != null && dataList.size() > 0) {
			this.sonAreaTimes = new ArrayList<SonAreaTimesDAEntity>(4);
			for (DBObject d : dataList) {
				SonAreaTimesDAEntity entity = new SonAreaTimesDAEntity();
				entity.dbObjectToBean(d);
				this.sonAreaTimes.add(entity);
			}
		}
	}
	@Override
	public DBObject toDBObject() {
		DBObject topic = new BasicDBObject();
		topic.put("tile", this.getTile());
		topic.put("times", this.getTimes());
		topic.put("month", this.getMonth());
		topic.put("district", this.getDistrict());
		List<DBObject> dataList = new ArrayList<DBObject>(this.getSonAreaTimes().size());
		for (SonAreaTimesDAEntity d : this.sonAreaTimes) {
			dataList.add(d.toDbObject());
		}
		topic.put("dl", dataList);
		return topic;
	}
	@Override
	public long getDay() {
		return 0;
	}
	@Override
	public void setDay(long day) {
	}
	@Override
	public List<DBObject> toBatchDBObject() {
		return null;
	}
	@Override
	public String toString() {
		return "VehicleDrivingNumberInGridDAEntity [tile=" + tile + ", times="
				+ times + ", month=" + month + ", district=" + district
				+ ", sonAreaTimes=" + sonAreaTimes.toString() + "]";
	}
	
}

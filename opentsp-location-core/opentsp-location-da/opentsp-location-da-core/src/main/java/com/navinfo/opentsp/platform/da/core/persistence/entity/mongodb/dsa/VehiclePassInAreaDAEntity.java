/**
 * 
 */
package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbIndex;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbName;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAVehicleNumInAreaAlarm;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;

/**
 * 区域车次检索
 * @author zyl
 * @date 2015年10月28日
 */
@LCTable(name="VehicleDrivingNumberInArea")
//@LCDbName(name="DataStatisticAnalysis")
@LCDbName(name= LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS)
@LCDbIndex(name={"district"})
public class VehiclePassInAreaDAEntity extends BaseMongoEntity implements Serializable{
	private static final long serialVersionUID = 7158379400006513868L;
	private String _id;
	private int district;
	private int type;
	private int times;
	private int day;

	private long lat;
	private long lng;
	private  int districtCode;

	public long getLat() {
		return lat;
	}

	public void setLat(long lat) {
		this.lat = lat;
	}

	public long getLng() {
		return lng;
	}

	public void setLng(long lng) {
		this.lng = lng;
	}

	public int getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(int districtCode) {
		this.districtCode = districtCode;
	}

	private List<DAVehicleNumInAreaAlarm> dataList = new ArrayList<DAVehicleNumInAreaAlarm>();

	public List<DAVehicleNumInAreaAlarm> getDataList() {
		return dataList;
	}
	public void setDataList(List<DAVehicleNumInAreaAlarm> dataList) {
		this.dataList = dataList;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getDistrict() {
		return district;
	}
	public void setDistrict(int district) {
		this.district = district;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int getThisDay() {
		return day;
	}
	public void setThisDay(int day) {
		this.day = day;
	}
	public DBObject toDbObject(){
		DBObject topic = new BasicDBObject();
		topic.put("district", this.getDistrict());
		topic.put("type", this.getType());
		topic.put("times", this.getTimes());
		topic.put("day", this.getThisDay());
		return topic;
	}
	public void dbObjectToBean(DBObject db){
		this._id = db.get("_id").toString();
		this.district = (int) db.get("district");
		this.type = (int) db.get("type");
		this.times = (int) db.get("times");
		this.day = (int) db.get("day");
		this.districtCode=db.get("districtCode")==null?0:(int)db.get("districtCode");
		this.lng=db.get("lng")==null?0:(long)db.get("lng");
		this.lat=db.get("lat")==null?0:(long)db.get("lat");
		List<DBObject> dataList = (List<DBObject>) db
				.get("dl");
		if (dataList != null && dataList.size() > 0) {
			this.dataList = new ArrayList<DAVehicleNumInAreaAlarm>();
			for (DBObject d : dataList) {
				DAVehicleNumInAreaAlarm vehicleNumInAreaAlarm = new DAVehicleNumInAreaAlarm();
				vehicleNumInAreaAlarm.dbObjectToBean(d);
				this.dataList.add(vehicleNumInAreaAlarm);
			}

		}
	}
	@Override
	public DBObject toDBObject() {
		DBObject topic = new BasicDBObject();
		topic.put("district", this.getDistrict());
		topic.put("type", this.getType());
		topic.put("times", this.getTimes());
		topic.put("day", this.getThisDay());
		topic.put("lat", this.getLat());
		topic.put("lng", this.getLng());
		topic.put("districtCode", this.getDistrictCode());
		//目前需求，只有type为服务站时，才有新增dl.
		List<DBObject> dataList = new ArrayList<DBObject>(this.getDataList()
				.size());
		for (DAVehicleNumInAreaAlarm d : this.dataList) {
			dataList.add(d.toDBObject());
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
		return "VehiclePassInAreaDAEntity [district=" + district + ", type="
				+ type + ", times=" + times + ", day=" + day + "]";
	}

}

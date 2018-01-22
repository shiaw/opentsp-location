/**
 * 
 */
package com.navinfo.opentsp.platform.rprest.entity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 区域车次检索
 * @author zyl
 * @date 2015年10月28日
 */
public class VehiclePassInAreaDAEntity  implements Serializable{
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

	}
	public DBObject toDBObject() {
		DBObject topic = new BasicDBObject();
		topic.put("district", this.getDistrict());
		topic.put("type", this.getType());
		topic.put("times", this.getTimes());
		topic.put("day", this.getThisDay());
		topic.put("lat", this.getLat());
		topic.put("lng", this.getLng());
		topic.put("districtCode", this.getDistrictCode());

		return topic;
	}
	public long getDay() {
		return 0;
	}
	public void setDay(long day) {

	}
	public List<DBObject> toBatchDBObject() {
		return null;
	}
	@Override
	public String toString() {
		return "VehiclePassInAreaDAEntity [district=" + district + ", type="
				+ type + ", times=" + times + ", day=" + day + "]";
	}

}

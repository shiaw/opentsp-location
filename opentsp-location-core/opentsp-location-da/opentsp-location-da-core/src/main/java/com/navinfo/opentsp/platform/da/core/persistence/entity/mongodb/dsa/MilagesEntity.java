package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbName;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAMilages;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;

@LCTable(name = "MileageConsumption")
//@LCDbName(name = "DataStatisticAnalysis")
@LCDbName(name= LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS)
public class MilagesEntity extends BaseMongoEntity implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private long _id;
	// private long terminal_id;
	private long day;
	// private int oil;// 当日油耗 单位毫升
	public int counts;

	private List<DAMilages> dataList;

	public void dbObjectToBean(DBObject dbObjectDetail) {
		// this.terminal_id = (long) dbObjectDetail.get("AA");
		this.day = (long) dbObjectDetail.get("DA");
		// this.oil = (int) dbObjectDetail.get("UB");
		@SuppressWarnings("unchecked")
		List<DBObject> dataList = (List<DBObject>) dbObjectDetail.get("dataList");
		if (dataList != null && dataList.size() > 0) {
			this.dataList = new ArrayList<DAMilages>();
			for (DBObject d : dataList) {
				DAMilages milages = new DAMilages();
				milages.dbObjectToBean(d);
				this.dataList.add(milages);
			}

		}

	}

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}

	/**
	 * 转换为Bson
	 */
	public List<DBObject> toBatchDBObject() {
		List<DBObject> dataList = new ArrayList<DBObject>();
		if (this.dataList != null && this.dataList.size() != 0) {
			for (DAMilages d : this.dataList) {
				dataList.add(d.toDBObject());
			}
		}

		return dataList;
	}

	public DBObject toDBObject() {
		DBObject topic = new BasicDBObject();
//		topic.put("AA", this.getTerminal_id());// 终端标识
		topic.put("DA", this.getDay());// 日期
		// topic.put("UB", this.getOil());//油耗
		List<DBObject> dataList = new ArrayList<DBObject>(this.getDataList().size());
		for (DAMilages d : this.dataList) {
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

	// public long getTerminal_id() {
	// return terminal_id;
	// }
	//
	// public void setTerminal_id(long terminal_id) {
	// this.terminal_id = terminal_id;
	// }

	public long getDay() {
		return day;
	}

	public void setDay(long day) {
		this.day = day;
	}

	// public int getOil() {
	// return oil;
	// }
	//
	// public void setOil(int oil) {
	// this.oil = oil;
	// }

	public List<DAMilages> getDataList() {
		return dataList;
	}

	public void setDataList(List<DAMilages> dataList) {
		this.dataList = dataList;
	}

	public MilagesEntity() {
		super();
	}
}

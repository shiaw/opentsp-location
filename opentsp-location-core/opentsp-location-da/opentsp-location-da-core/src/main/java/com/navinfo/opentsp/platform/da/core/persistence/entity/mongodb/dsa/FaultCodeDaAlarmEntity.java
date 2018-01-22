package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbIndex;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbName;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAFaultCodeAlarm;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DBObject;

/**
 * 3.15 滞留超时
 *
 * @author zyl
 * @date 2015年10月22日
 */
@LCTable(name = "FaultCode")
//@LCDbName(name = "DataStatisticAnalysis")
@LCDbName(name= LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS)
@LCDbIndex(name = { "terminalId", "spn", "fmi", "beginDate" })
public class FaultCodeDaAlarmEntity extends BaseMongoEntity implements
		Serializable {
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(FaultCodeDaAlarmEntity.class);
	private long _id;
	private long terminal_id;
	private long day;
	private List<DAFaultCodeAlarm> dataList;

	/**
	 * 转换为Bson
	 */
	public List<DBObject> toBatchDBObject() {
		List<DBObject> dataList = new ArrayList<DBObject>();
		if (this.dataList != null && this.dataList.size() != 0) {
			for (DAFaultCodeAlarm d : this.dataList) {
				dataList.add(d.toDBObject());
			}
		}
		return dataList;
	}

	public DBObject toDBObject() {
		logger.error("调用了方法：toDBObject");
		return null;
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

	public List<DAFaultCodeAlarm> getDataList() {
		return dataList;
	}

	public void setDataList(List<DAFaultCodeAlarm> dataList) {
		this.dataList = dataList;
	}

	public FaultCodeDaAlarmEntity() {
		super();
	}
}

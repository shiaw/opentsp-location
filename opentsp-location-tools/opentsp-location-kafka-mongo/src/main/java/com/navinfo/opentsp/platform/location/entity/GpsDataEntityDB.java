package com.navinfo.opentsp.platform.location.entity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.location.kit.Convert;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class GpsDataEntityDB implements Serializable {
    private static final long serialVersionUID = 1L;
    private long _id;
    private long tId;
    private String day;
    private List<GpsDetailedEntityDB> dataList = new ArrayList<GpsDetailedEntityDB>();

    public GpsDataEntityDB() {
        super();
    }

    public void addGpsDetailed(GpsDetailedEntityDB detailedEntity) {
        if (this.dataList == null) {
            this.dataList = new ArrayList<GpsDetailedEntityDB>();
        }
        this.dataList.add(detailedEntity);
    }

    public GpsDetailedEntityDB newGpsDetailedEntity() {
        return new GpsDetailedEntityDB();
    }

    public DBObject toDBObject() {
        DBObject topic = new BasicDBObject();
        topic.put("tId", this.gettId());
        topic.put("day", this.getDay());
        List<DBObject> dataList = new ArrayList<DBObject>(this.getDataList().size());
        for (GpsDetailedEntityDB d : this.dataList) {
            dataList.add(d.toDBObject());
        }
        topic.put("dataList", dataList);
        return topic;
    }

    public long get_id() {
        return _id;
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

    public List<GpsDetailedEntityDB> getDataList() {
        return dataList;
    }

    public void setDataList(List<GpsDetailedEntityDB> dataList) {
        this.dataList = dataList;
    }

}

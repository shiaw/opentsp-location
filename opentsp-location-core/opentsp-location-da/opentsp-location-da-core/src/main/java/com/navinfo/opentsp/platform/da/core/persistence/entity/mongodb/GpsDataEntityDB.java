package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.common.Configuration;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoManager;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.location.kit.Convert;

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

    public static void main(String[] args) {
        Configuration.addConfig("MONGODB.IP", "172.16.1.4");
        Configuration.addConfig("MONGODB.PORT", "27017");
        DBCollection collection = null;
        try {
            collection = MongoManager.start("test", "Users");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            List<DBObject> list = (List<DBObject>) dbObject.get("data");
            for (DBObject dbObject2 : list) {
                Object object = dbObject2.get("b");
                if (object != null) {
                    System.err.println(Convert.bytesToHexString((byte[]) dbObject2.get("b")));
                } else {
                    System.err.println(Convert.bytesToHexString((byte[]) dbObject2.get("c")));
                }
            }
        }
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

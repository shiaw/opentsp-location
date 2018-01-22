package com.navinfo.opentsp.platform.da.core.persistence.client.mongo;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoDaoImp;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoManager;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class test extends MongoDaoImp{

	public static void main(String[] args) {
//		DBObject query = new BasicDBObject();
//		query.put("AA", 2014003);
//		//query.put("HA", new BasicDBObject("$gte", st).append("$lte", et));
//		// BasicDBList list = new BasicDBList();
//		// list.add(new BasicDBObject("status",new BasicDBObject("$gte",0)));
//		// list.add(new BasicDBObject("conType", new BasicDBObject("$gt",0)));
//		// query.put("$or",list);

	DBCollection collection = null;
	try {
		collection = MongoManager.start("GpsLocationData", "GpsDataEntity_1410");
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	DBObject query = new BasicDBObject();
	query.put("tId", 18210355950l);
	DBCursor find = collection.find(query).limit(1);
	while (find.hasNext()){
		DBObject next = find.next();
		List<DBObject> datas = (List<DBObject>) next
				.get("dataList");
		System.out.println(datas.size());
		for(DBObject o:datas){
			long gpsTime = Long.parseLong(String.valueOf(o
					.get("gpsTime")));
			System.out.println("gpsTime-->"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(gpsTime*1000)));
		}
	}
//	
//		System.out.println(collection.find().skip(1440*1).limit(10).toArray());

	}

}

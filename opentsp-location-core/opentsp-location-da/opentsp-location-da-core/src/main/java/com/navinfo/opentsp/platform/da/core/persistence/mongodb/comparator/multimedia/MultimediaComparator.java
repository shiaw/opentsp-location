package com.navinfo.opentsp.platform.da.core.persistence.mongodb.comparator.multimedia;

import java.util.Comparator;

import com.mongodb.DBObject;


public class MultimediaComparator implements Comparator<DBObject>{

	@Override
	public int compare(DBObject o1, DBObject o2) {
		int flag=((String) o1.get("date")).compareTo((String)o2.get("date"));
		return flag; 
	}
}

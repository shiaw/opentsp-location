package com.navinfo.opentsp.platform.da.core.persistence.application;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAWFlow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class FlowCacheEntity  implements Serializable{
       /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String date;
       
       List<DAWFlow> dataList;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<DAWFlow> getDataList() {
		return dataList;
	}

	public void setDataList(List<DAWFlow> dataList) {
		this.dataList = dataList;
	}
	
	public void addFlowData(DAWFlow wFlow) {
		if(this.dataList==null){
			dataList=new ArrayList<DAWFlow>();
		}
		dataList.add(wFlow);
	}

	public void clearFloeDate() {
		dataList.clear();
		
	}
       
       
       
}

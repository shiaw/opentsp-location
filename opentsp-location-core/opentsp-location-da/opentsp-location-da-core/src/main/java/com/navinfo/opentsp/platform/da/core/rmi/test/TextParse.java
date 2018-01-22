package com.navinfo.opentsp.platform.da.core.rmi.test;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCResOverspeedAlarmSummary;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCCommonSummary;

public class TextParse {

	public static void main(String[] args) throws InvalidProtocolBufferException {
		byte[] c={8, 1, 16, 9, 26, 23, 8, -72, -10, 122, 16, -128, -48, -23, -98, 5, 24, -3, -104, -85, -95, 5, 32, -64, 22, 40, -128, -31, 1, 26, 23, 8, -74, -10, 122, 16, -128, -48, -23, -98, 5, 24, -3, -104, -85, -95, 5, 32, -64, 22, 40, -128, -31, 1, 26, 23, 8, -75, -10, 122, 16, -128, -48, -23, -98, 5, 24, -3, -104, -85, -95, 5, 32, -64, 22, 40, -128, -31, 1, 26, 23, 8, -76, -10, 122, 16, -128, -48, -23, -98, 5, 24, -3, -104, -85, -95, 5, 32, -64, 22, 40, -128, -31, 1, 26, 23, 8, -73, -10, 122, 16, -128, -48, -23, -98, 5, 24, -3, -104, -85, -95, 5, 32, -64, 22, 40, -128, -31, 1, 26, 23, 8, -77, -10, 122, 16, -128, -48, -23, -98, 5, 24, -3, -104, -85, -95, 5, 32, -64, 22, 40, -128, -31, 1, 26, 23, 8, -78, -10, 122, 16, -128, -48, -23, -98, 5, 24, -3, -104, -85, -95, 5, 32, -64, 22, 40, -128, -31, 1, 26, 23, 8, -79, -10, 122, 16, -128, -48, -23, -98, 5, 24, -3, -104, -85, -95, 5, 32, -64, 22, 40, -128, -31, 1};
		LCResOverspeedAlarmSummary.ResOverspeedAlarmSummary query= LCResOverspeedAlarmSummary.ResOverspeedAlarmSummary.parseFrom(c);
		System.out.println(query.getStatusCode());
		System.out.println(query.getTotalRecords());
		System.out.println(query.getDataListCount());
		List<LCCommonSummary.CommonSummary> dataListList = query.getDataListList();
		for (LCCommonSummary.CommonSummary d:dataListList){
			System.out.println(d.getTerminalID()+":"+d.getRecordsTotal());
		}
	}

}

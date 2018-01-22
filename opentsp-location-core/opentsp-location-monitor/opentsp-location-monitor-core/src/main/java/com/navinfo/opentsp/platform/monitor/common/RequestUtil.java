/**
 * 
 */
package com.navinfo.opentsp.platform.monitor.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tfj
 * 2014-8-22
 */
public class RequestUtil {
	 //ʱ��ת��(long--->string)
    public static String ChangeDate(Long date){
    Date d = new Date(date * 1000L);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(d);
    }
    
    public static String getDoubleData(Double data){
    	java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#0.0");
        try {
			String d = data == null ? "0" : df.format(data) + "";//������̣�Km��
			return d;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
        return "0";
    }
    //ʱ��ת��(string--->long)
	public static long ChangeDateString(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date aa = null;
		try {
			aa = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return aa.getTime() / 1000;
	}
	
	
}

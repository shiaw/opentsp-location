package com.navinfo.opentsp.platform.da.core.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 获取经纬度
 * api:http://developer.baidu.com/map/index.php?title=webapi/route-matrix-api#.E6.8E.A5.E5.8F.A3.E7.A4.BA.E4.BE.8B
 */
public class BaiduAPI {
	private static final Logger log = LoggerFactory.getLogger(BaiduAPI.class);
	private static String ak = "1lOECWxM8OzzWdKzw5THP0y9";
	private static String api = "http://api.map.baidu.com/direction/v1/routematrix";


	/**
	 * 获得两点之间的距离
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 * @throws Exception
	 */
	public static Long getPointAndPointDistance(double lat1,double lng1,double lat2,double lng2) {
		URLConnection httpsConn = null;
		InputStreamReader insr = null;
		BufferedReader br = null;
		Long distanceValue = 0l;
		try {
			String url = String.format(api+"?output=json&origins=%f,%f&destinations=%f,%f&ak=%s",lat1,lng1,lat2,lng2,ak);
			URL myURL = new URL(url);
			httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
			insr = new InputStreamReader(httpsConn.getInputStream(),"UTF-8");
			br = new BufferedReader(insr);
			String data = null;
			StringBuilder json = new StringBuilder();
			while ((data = br.readLine()) != null) {
				json.append(data);
			}
//			System.out.println(json);
			JSONObject obj = JSON.parseObject(json.toString());
			Integer status = obj.getInteger("status");
			if(status.equals(0)) {
				JSONObject result = obj.getJSONObject("result");
				JSONArray elements = result.getJSONArray("elements");
				JSONObject distance = elements.getJSONObject(0).getJSONObject("distance");
				distanceValue = distance.getLongValue("value");
			} else {
				log.error("异常，status="+status);
			}
			return distanceValue;
		}catch(Exception e) {
			log.error("",e);
			return 0l;
		} finally {
			if (insr != null) {
				try {
					insr.close();
					br.close();
				} catch (Exception e) {
					log.error("",e);
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		long o;
		try {
			o = BaiduAPI.getPointAndPointDistance(39.915285,116.403857,39.915285,116.493857);
			System.out.println(o);// 经度
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
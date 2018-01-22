package com.navinfo.tasktracker.rprest.util;



import com.navinfo.tasktracker.rprest.entity.AreaDataEntity;

import java.util.ArrayList;
import java.util.List;

public class GeometricCalculation {
	private static int ESP = (int) 1e-5;


	/**
	 * 判断一个点是否在矩形内
	 *
	 * @param longitude
	 * @param latitude
	 * @param p1
	 *            左上角经纬度
	 * @param p2
	 *            右下角经纬度
	 * @return 在矩形内则返回true，否则返回false
	 */
	public static boolean insideRectangle(int longitude, int latitude,
										  AreaDataEntity p1, AreaDataEntity p2) {
		if (longitude >= p1.getLongitude() && longitude <= p2.getLongitude()
				&& latitude <= p1.getLatitude() && latitude >= p2.getLatitude()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断一个点是否在圆内
	 *
	 * @param longitude
	 * @param latitude
	 * @param o
	 *            圆心经纬度
	 * @param r
	 *            半径
	 * @return 在圆内则返回true，否则返回false
	 */
	public static boolean insideCircle(int longitude, int latitude, AreaDataEntity o,  int r) {
		double d = getDistance(latitude / 1000000D, longitude / 1000000D, o.getLatitude() / 1000000D, o.getLongitude() / 1000000D);
		if (d > r) return false;
		return true;
	}

	/**
	 * 判断一个点是否在圆内
	 *
	 * @param longitude
	 * @param latitude
	 * @param o
	 *            圆心经纬度
	 * @param r
	 *            半径
	 * @return 在圆内则返回true，否则返回false
	 */
	public static boolean insideCircle_1(int longitude, int latitude, AreaDataEntity o,  int r) {
		double d = Math.sqrt( Math.pow(Math.abs(o.getLongitude()-longitude), 2) + Math.pow(Math.abs(o.getLatitude()-latitude), 2) );
		if (d > r) return false;
		return true;
	}

	/**
	 * 判断一个点是否在多边形内，采用角度和判定方法。
	 * @param longitude
	 * @param latitude
	 * @param polygon 多边形各个点的链表
	 * @param p 待测点
	 * @return 在多边形内则返回true，否则返回false
	 */
	public static boolean insidePolygon1(int longitude, int latitude, List<AreaDataEntity> polygon) {
		//System.err.println("Gps点：longitude = "+longitude+"; latitude = "+latitude+".");
		//System.err.println("多边形：");
//		for(int i = 0 ; i < polygon.size() ; i ++){
		//System.err.println(polygon.get(i));
//		}
		if(polygon.size() < 1) return false;
		double angle = 0;
		int x1 = polygon.get(0).getLongitude() - longitude;
		int y1 = polygon.get(0).getLatitude() - latitude;
		int x2 = polygon.get(polygon.size()-1).getLongitude() - longitude;
		int y2 = polygon.get(polygon.size()-1).getLatitude() - latitude;
		//根据向量的数量积(叉积、内积)公式求待测点与多边形相邻两个点之间连线的角度
		angle += Math.acos((double)(x1*x2+y1*y2)/Math.sqrt((x1*x1+y1*y1)*(x2*x2+y2*y2)));

		for(int i = 0; i < polygon.size()-1; i++) {
			if(isOnLine(longitude , latitude , polygon.get(i),polygon.get(i+1)))
				return true;
			x1 = polygon.get(i).getLongitude() - longitude;
			y1 = polygon.get(i).getLatitude() - latitude;
			x2 = polygon.get(i+1).getLongitude() - longitude;
			y2 = polygon.get(i+1).getLatitude() - latitude;
			//根据向量的数量积(叉积、内积)公式求待测点与多边形相邻两个点之间连线的角度
			angle += Math.acos((double)(x1*x2+y1*y2)/Math.sqrt((x1*x1+y1*y1)*(x2*x2+y2*y2)));
		}

		if(Math.abs(angle-Math.PI*2) <= ESP)
			return true;
		return false;
	}


	/************************************
	 *  int    polySides  =  how many corners the polygon has
	 double  polyX[]   =  horizontal coordinates of corners
	 double  polyY[]   =  vertical coordinates of corners
	 double  x,y       =  point to be tested
	 *
	 * @param longitude
	 * @param latitude
	 * @param polygons
	 * @return
	 */
	public static boolean insidePoloygonAlgorithm(int longitude, int latitude, List<AreaDataEntity> polygons){
		int i,j = polygons.size()-1 ;
		boolean oddNodes = false;

		float x = longitude;
		float y = latitude;
		List<Float> polyX = new ArrayList();
		List<Float> polyY = new ArrayList();

		for(AreaDataEntity point : polygons){
			polyX.add((float)point.getLongitude());
			polyY.add((float)point.getLatitude());
		}
		for (i=0; i <polygons.size(); i++) {
			if(polyY.get(i) < y && polyY.get(j) >= y ||  polyY.get(j) < y && polyY.get(i) >= y) {
				if(polyX.get(i) + (y-polyY.get(i)) / (polyY.get(j)-polyY.get(i)) * (polyX.get(j)-polyX.get(i)) < x) {
					oddNodes = !oddNodes;
				}
			}
			j=i;
		}

		return oddNodes;
	}


	public static boolean insidePolygon(int longitude, int latitude, List<AreaDataEntity> polygons){
		int nCross = 0;
		int len=polygons.size()-1;
		for (int i = 0; i < len; i++) {
			AreaDataEntity p1 = polygons.get(i);
			AreaDataEntity p2 = polygons.get(( i + 1 ) % polygons.size());

			// 求解 y=p.y 与 p1p2 的交点 , p1p2 与 y=p0.y平行
			if (p1.getLatitude() == p2.getLatitude()) continue;
			// 交点在p1p2水平方向延长线上
			if (latitude < Math.min(p1.getLatitude(), p2.getLatitude())) continue;
			if (latitude >= Math.max(p1.getLatitude(), p2.getLatitude())) continue;

			// 与P1P2 求交点的 X 坐标
			double x = (double) (latitude - p1.getLatitude())* (double) (p2.getLongitude() - p1.getLongitude()) / (double) (p2.getLatitude() - p1.getLatitude()) + p1.getLongitude();
			if (x > longitude)   nCross++; // 只统计单边交点
		}

		//最后一条边， 末端点与首位点连接
		AreaDataEntity p1 = polygons.get(polygons.size()-1);
		AreaDataEntity p2 = polygons.get(0);
		// 与P1P2 求交点的 X 坐标
		double x = (double) (latitude - p1.getLatitude())* (double) (p2.getLongitude() - p1.getLongitude()) / (double) (p2.getLatitude() - p1.getLatitude()) + p1.getLongitude();

		if (x > longitude)    nCross++; // 只统计单边交点

		// 单边X方向交点为偶数，点在多边形之外, 否则点在多边形内
		return (nCross % 2 == 1)?true:false;
	}


	/*******************
	 * 判断一个点是否在线段上
	 *
	 * @param longitude
	 * @param latitude
	 * @param p1  起点经纬度
	 * @param p2 终点经纬度
	 * @return 在线段上则返回true，否则返回false
	 */
	public static boolean isOnLine(int longitude, int latitude , AreaDataEntity p1, AreaDataEntity p2) {
		//如果p1p3与p2p3平行并且p3的横坐标和纵坐标都在p1和p2之内说明p3在线段p1p2上
		int a = (p1.getLongitude()-longitude)*(p2.getLatitude()-latitude)-(p2.getLongitude()-longitude)*(p1.getLatitude()-latitude);
		if( a == 0 && (p1.getLongitude()-longitude)*(p2.getLongitude()-longitude) <=0 && (p1.getLatitude()-latitude)*(p2.getLatitude()-latitude) <= 0 ) {
			return true;
		}
		return false;
	}



	/**********************
	 * 计算线段缓冲区多边形的四个角点
	 *
	 * @param sLongitude
	 * @param sLatitude
	 * @param eLongitude
	 * @param eLatitude
	 * @param width
	 * @return
	 */
	public static List<AreaDataEntity> getPolygonPoints(int sLongitude, int sLatitude, int eLongitude, int eLatitude, int width){
		List<AreaDataEntity> cornerPoints = new ArrayList();

		//求两点的直线方程L: a*x+b*y+c=0
		double x1 = sLongitude;
		double y1 = sLatitude;
		double x2 = eLongitude;
		double y2 = eLatitude;
		double a = y2-y1;
		double b = x1-x2;
		double c = x2*y1-x1*y2;

		//求出与直线L平行并且距离为width的直线方程L1, L2:a*x+b*y+c1=0，L2:a*x+b*y+c2=0
		double c1 = (double) (c-Math.sqrt(a*a+b*b)*width);
		double c2 = (double) (c+Math.sqrt(a*a+b*b)*width);

		//求出过第一个点并且与L垂直的直线方程#L1:b*x-a*y+_c=0;
		double _c = a*y1-b*x1;

		//求出路线上第一条线段所在的四边形的头两个点，即L1,L2与#L1的交点 P1, P2
		AreaDataEntity p1 = new AreaDataEntity();
		AreaDataEntity p2 = new AreaDataEntity();
		p1.setLongitude((int)Math.round((_c*b+c1*a)/(-a*a-b*b)));
		p1.setLatitude((int)Math.round((_c*a-c1*b)/(b*b+a*a)));
		p2.setLongitude((int)Math.round((_c*b+c2*a)/(-a*a-b*b)));
		p2.setLatitude((int)Math.round((_c*a-c2*b)/(b*b+a*a)));


		//求出过末尾点并且与L垂直的直线方程#L2:b*x-a*y+_c2=0;
		//int len = (int) Math.sqrt( Math.pow( x2-x1, 2) + Math.pow( y2-y1, 2));
		double _c2 = a*y2-b*x2;
		AreaDataEntity p3 = new AreaDataEntity();
		AreaDataEntity p4 = new AreaDataEntity();

		//求出路线上第一条线段所在的四边形的尾部两个点，即L1,L2与#L2的交点 P3, P4
		p4.setLongitude((int)Math.round((_c2*b+c1*a)/(-a*a-b*b)));
		p4.setLatitude((int)Math.round((_c2*a-c1*b)/(b*b+a*a)));
		p3.setLongitude((int)Math.round((_c2*b+c2*a)/(-a*a-b*b)));
		p3.setLatitude((int)Math.round((_c2*a-c2*b)/(b*b+a*a)));

		cornerPoints.add(p1);
		cornerPoints.add(p2);
		cornerPoints.add(p3);
		cornerPoints.add(p4);
		return cornerPoints;
	}



	/********************************
	 * 判断一个点是否在指定的路线上，采用分段判定
	 *
	 * @param longitude
	 * @param latitude
	 * @param route 路线上各个点的经纬度
	 * @param width 路线的宽度
	 * @return 在路线上则返回true，否则返回false
	 */
	public static boolean insideRoute(int longitude, int latitude , List<AreaDataEntity> route) {
		if(route.size() < 2)  return false;
		for(int i = 1; i < route.size(); i++) {
			//判断待测点是否在四边形内
			AreaDataEntity startPt = route.get(i-1);
			AreaDataEntity endPt = route.get(i);
			int width = startPt.getRadiusLength();
			if(insidePoloygonAlgorithm(longitude ,latitude ,getPolygonPoints(startPt.getLongitude(), startPt.getLatitude(), endPt.getLongitude(), endPt.getLatitude(), width))){
				return true;
			}
		}
		return false;
	}

	private static double EARTH_RADIUS = 6378.137;// 地球半径 6378.137km

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 点到圆心的距离
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return 距离 单位:米
	 */
	public static double getDistance(double lat1, double lng1, double lat2,
									 double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = Math.abs(radLat1 - radLat2);
		double b = Math.abs(rad(lng1) - rad(lng2));

		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		return Math.round(s * 1000);
	}

	/**
	 * 已知圆形的圆心为（lng，lat），圆形的半径为r，求圆形外切正方形的左上和右下点坐标，范围比较大，考虑地球的曲率上
	 * @param lon
	 * @param lat
	 * @param r 单位：米
	 * @return 左上角：result[0]，右下角：result[1] result[i][0]经度 result[i][1]纬度
	 */
	public static double[][] enclosingRectangle(double lon, double lat, double r) {
		r = r / 6371004D;
		double a = Math.asin(Math.sin(r / 2) / Math.cos(lat / 180D * Math.PI)) * 2;
		a = a * 180D / Math.PI;
		double b = r * 180D / Math.PI;
		double[][] result = new double[2][2];
		result[0][0] = lon - a;
		result[1][0] = lon + a;
		result[0][1] = lat + b;
		result[1][1] = lat - b;
		return result;
	}


	public static void main(String[] args) {
		double[][] rec = enclosingRectangle(118d, 30d, 50000);
		System.out.println("Left Top Point:" + rec[0][0] + "," + rec[0][1]);
		System.out.println("Right Bottom Point:" + rec[1][0] + "," + rec[1][1]);
		System.out.println(getDistance(rec[0][1], rec[0][0], rec[0][1], rec[1][0]));
//		AreaDataEntity o = new AreaDataEntity();
//		o.setLatitude(39987086);
//		o.setLongitude(115749482);
//		o.setRadiusLength(1181);
//		System.err.println(insideCircle(115762931, 39985776, o, 1181));;

		//List<AreaDataEntity> entities = new ArrayList<AreaDataEntity>();
		//多边形
//		entities.add(new AreaDataEntity(116388819,39969305));
//		entities.add(new AreaDataEntity(116390654,39969186));
//		entities.add(new AreaDataEntity(116391201,39968125));
//		entities.add(new AreaDataEntity(116390659,39967895));
//		entities.add(new AreaDataEntity(116390986,39967438));
//		entities.add(new AreaDataEntity(116389710,39967229));
//		entities.add(new AreaDataEntity(116389146,39966871));
//		entities.add(new AreaDataEntity(116387617,39967763));
//		entities.add(new AreaDataEntity(116386523,39967401));
//		entities.add(new AreaDataEntity(116386722,39968709));

		//A (116387842,39967748)  True
		//B (116387654,39967475)  False
		//C (116389161,39966953)  True
		//D (116390728,39967524)  True
		//E (116390926,39967849)  False
//		//System.err.println(insidePoloygonAlgorithm(116390926,39967849, entities));

		//单个路线
//		entities.add(new AreaDataEntity(2, 15));
//		entities.add(new AreaDataEntity(12, 5));
//		//System.err.println(isOnLine(3, 14, new AreaDataEntity(2, 15), new AreaDataEntity(12, 5)));


		/***************  路线集合 **************/
//		entities.add(new AreaDataEntity(116385155, 39966871));  //116385155, 39966871
//		entities.add(new AreaDataEntity(116385133, 39968059));  //116385133, 39968059
//		entities.add(new AreaDataEntity(116388137, 39968137));  //116388137, 39968137
//		entities.add(new AreaDataEntity(116388148, 39966702));  //116388148, 39966702
//		entities.add(new AreaDataEntity(116390149, 39966723));  //116390149, 39966723
//		entities.add(new AreaDataEntity(116390058, 39968174));  //116390058, 39968174
//		entities.add(new AreaDataEntity(116388604, 39968149));  //116388604, 39968149

		//路线上 116385230,39967992
		////System.err.println(insideRoute(116385230,39967992,entities,70));
		//链路拐角外
		////System.err.println(insideRoute(116384951,39968153, entities, 20));
		//链路拐角 内点
		////System.err.println(insideRoute(116385230,39967992, entities, 100));
		//链路拐角 外点
		////System.err.println(insideRoute(116385860,39967708, entities, 20));


		//路线集合
//		entities.add(new AreaDataEntity(2, 2));
//		entities.add(new AreaDataEntity(6, 6));
//		entities.add(new AreaDataEntity(12, 4));

		//路线上
//		//System.err.println(insideRoute(7, 6, entities, 2));

		//
//		//System.err.println(insideCircle(7, 7, new AreaDataEntity(8, 8), 2));
	}
}

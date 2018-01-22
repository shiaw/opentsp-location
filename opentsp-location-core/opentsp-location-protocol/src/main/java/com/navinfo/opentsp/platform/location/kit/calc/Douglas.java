package com.navinfo.opentsp.platform.location.kit.calc;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class Douglas {
	/***存储采样点数据的链表*/
	public List<DouglasPoint> points = new ArrayList<DouglasPoint>();
	/*** 控制数据压缩精度的极差 */
	private static  double D = 1;
	private WKTReader reader;

	/*** 构造Geometry
	 * @param str
	 * @return 
	 * */
	public Geometry buildGeo(String str) {
		try {
			if (reader == null) {
				reader = new WKTReader();
			}
			return reader.read(str);
		} catch (ParseException e) {
			throw new RuntimeException("buildGeometryError", e);
		}
	}

	/*** 读取采样点
	 * 
	 *  */
	/*public void readDouglasPoint() {
		Geometry g = buildGeo("LINESTRING(1 4,2 3,4 2,6 6,7 7,8 6,9 5,10 10)");
		Coordinate[] coords = g.getCoordinates();
		for (int i = 0; i < coords.length; i++) {
			DouglasPoint p = new DouglasPoint(coords[i].x, coords[i].y, i);
			points.add(p);
		}
	}*/

	/************
	 * 采集点读取
	 * @param pointStr 格式：1 4,2 3,4 2 (x坐标y坐标以空格相连，多点以逗号相连)
	 */
	public void readDouglasPoint(String pointStr) {
		Geometry g = buildGeo("LINESTRING("+pointStr+")");
		Coordinate[] coords = g.getCoordinates();
		for (int i = 0; i < coords.length; i++) {
			DouglasPoint p = new DouglasPoint(coords[i].x, coords[i].y, i);
			points.add(p);
		}
	}
	
	/*** 对矢量曲线进行压缩
	 * **@paramfrom 曲线的起始点
	 * *@paramto 曲线的终止点 
	 * */
	public void compress(DouglasPoint from, DouglasPoint to) {
		/*** 压缩算法的开关量 */
		boolean switchvalue = false;
		/*** 由起始点和终止点构成的直线方程一般式的系数 */
//		System.out.println(from.getY());
//		System.out.println(to.getY());
		double A = (from.getY() - to.getY())
				/ Math.sqrt(Math.pow((from.getY() - to.getY()), 2)
						+ Math.pow((from.getX() - to.getX()), 2));
		/*** 由起始点和终止点构成的直线方程一般式的系数 */
		double B = (to.getX() - from.getX())
				/ Math.sqrt(Math.pow((from.getY() - to.getY()), 2)
						+ Math.pow((from.getX() - to.getX()), 2));
		/*** 由起始点和终止点构成的直线方程一般式的系数 */
		double C = (from.getX() * to.getY() - to.getX() * from.getY())
				/ Math.sqrt(Math.pow((from.getY() - to.getY()), 2)
						+ Math.pow((from.getX() - to.getX()), 2));
		double d = 0;
		double dmax = 0;
		int m = points.indexOf(from);
		int n = points.indexOf(to);
		if (n == m + 1)
			return;
		DouglasPoint middle = null;
		List<Double> distance = new ArrayList<Double>();
		for (int i = m + 1; i < n; i++) {
			d = Math.abs(A * (points.get(i).getX()) + B
					* (points.get(i).getY()) + C)
					/ Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
			distance.add(d);
		}
		dmax = distance.get(0);
		for (int j = 1; j < distance.size(); j++) {
			if (distance.get(j) > dmax)
				dmax = distance.get(j);
		}
		if (dmax > D)
			switchvalue = true;
		else
			switchvalue = false;
		if (!switchvalue) {
			// 删除DouglasPoints(m,n)内的坐标
			for (int i = m + 1; i < n; i++)
				points.get(i).setIndex(-1);
		} else {
			for (int i = m + 1; i < n; i++) {
				if ((Math.abs(A * (points.get(i).getX()) + B
						* (points.get(i).getY()) + C)
						/ Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2)) == dmax))
					middle = points.get(i);
			}
			compress(from, middle);
			compress(middle, to);
		}
	}

	/************
	 * 对矢量曲线进行压缩
	 * @param from  曲线的起始点
	 * @param to	曲线的终止点
	 * @param precision 抽稀精度
	 */
	public void compress(DouglasPoint from, DouglasPoint to,double precision) {
		/****抽稀精度设置******/
		D=precision;
		
		/*** 压缩算法的开关量 */
		boolean switchvalue = false;
		/*** 由起始点和终止点构成的直线方程一般式的系数 */
//		System.out.println(from.getY());
//		System.out.println(to.getY());
		double A = (from.getY() - to.getY())
				/ Math.sqrt(Math.pow((from.getY() - to.getY()), 2)
						+ Math.pow((from.getX() - to.getX()), 2));
		/*** 由起始点和终止点构成的直线方程一般式的系数 */
		double B = (to.getX() - from.getX())
				/ Math.sqrt(Math.pow((from.getY() - to.getY()), 2)
						+ Math.pow((from.getX() - to.getX()), 2));
		/*** 由起始点和终止点构成的直线方程一般式的系数 */
		double C = (from.getX() * to.getY() - to.getX() * from.getY())
				/ Math.sqrt(Math.pow((from.getY() - to.getY()), 2)
						+ Math.pow((from.getX() - to.getX()), 2));
		double d = 0;
		double dmax = 0;
		int m = points.indexOf(from);
		int n = points.indexOf(to);
		if (n == m + 1)
			return;
		DouglasPoint middle = null;
		List<Double> distance = new ArrayList<Double>();
		for (int i = m + 1; i < n; i++) {
			d = Math.abs(A * (points.get(i).getX()) + B
					* (points.get(i).getY()) + C)
					/ Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
			distance.add(d);
		}
		dmax = distance.get(0);
		for (int j = 1; j < distance.size(); j++) {
			if (distance.get(j) > dmax)
				dmax = distance.get(j);
		}
		if (dmax > D)
			switchvalue = true;
		else
			switchvalue = false;
		if (!switchvalue) {
			// 删除DouglasPoints(m,n)内的坐标
			for (int i = m + 1; i < n; i++)
				points.get(i).setIndex(-1);
		} else {
			for (int i = m + 1; i < n; i++) {
				if ((Math.abs(A * (points.get(i).getX()) + B
						* (points.get(i).getY()) + C)
						/ Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2)) == dmax))
					middle = points.get(i);
			}
			compress(from, middle);
			compress(middle, to);
		}
	}
	
	public static void main(String[] args) {
		Douglas d = new Douglas();
		/*d.readDouglasPoint();
		d.compress(d.points.get(0), d.points.get(d.points.size() - 1));
		for (int i = 0; i < d.points.size(); i++) {
			DouglasPoint p = d.points.get(i);
			if (p.getIndex() > -1) {
				System.out.print(p.getX() + "——" + p.getY() + ",");
			}
		}*/
		System.out.println();
		System.out.println("---------------");
		d.points.clear();
		//1 4,2 3,4 2,6 6,7 7,8 6,9 5,10 10
		d.readDouglasPoint("1 4,2 3,4 2,6 6,7 7,8 6,9 5,10 10");
		d.compress(d.points.get(0), d.points.get(d.points.size() - 1),0.001);
		for (int i = 0; i < d.points.size(); i++) {
			DouglasPoint p = d.points.get(i);
			if (p.getIndex() > -1) {
				System.out.print(p.getX() + "——" + p.getY() + ",");
			}
		}
	}
}


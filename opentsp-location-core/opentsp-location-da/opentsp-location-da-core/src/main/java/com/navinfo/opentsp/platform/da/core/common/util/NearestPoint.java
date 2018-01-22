package com.navinfo.opentsp.platform.da.core.common.util;

/**
 * 点到线的最近的点
 * @author zyl
 * @date 2015年10月16日
 */
public class NearestPoint {

	public static void main(String[] args) {
//		NearestPointTest nearestPoint = new NearestPointTest();
		long a = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
//			float ok = nearestPoint.calculateDistance();
			double[] ok = NearestPoint.getNearestPoint(100, 0, 5000, 0, i, 1);
			System.out.println("the nearest distance is :" + ok[0]+" "+ ok[1]);
		}
		System.out.println(System.currentTimeMillis()-a);
	}

	/**
	 * 获得点(x0,y0)到线段[(x1,y1),(x2,y2)]最近的点的坐标
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x0
	 * @param y0
	 * @return
	 */
	public static double[] getNearestPoint(double x1, double y1, double x2, double y2, double x0,double y0){
		double a, b, c;
		a = lineSpace(x1, y1, x2, y2);// 线段的长度
		b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离
		c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离
		//点到线段的最短距离
		double s = pointToLine(a, b, c);
		double[] result = new double[2];
		if(b == s){
			result[0] = x1;
			result[1] = y1;
			return result;
		}else if(c == s){
			result[0] = x2;
			result[1] = y2;
			return result;
		}else if(0 == s){
			result[0] = 0;
			result[1] = 0;
			return result;
		}else{
			double d1 = Math.sqrt(b*b-s*s);
			double x = (d1*(x2-x1))/a + x1;
			double y = (d1*(y2-y1))/a + y1;
			result[0] = x;
			result[1] = y;
			return result;
		}
	}

	/**
	 * abc为三角形三条边的长度，求ab交点到bc边的高度
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	private static double pointToLine(double a,double b,double c) {
		double space = 0;
		if (c <= 0.000001 || b <= 0.000001) {
			space = 0;
			return space;
		}
		if (a <= 0.000001) {
			space = b;
			return space;
		}
		if (c * c >= a * a + b * b) {
			space = b;
			return space;
		}
		if (b * b >= a * a + c * c) {
			space = c;
			return space;
		}
		double p = (a + b + c) / 2;// 半周长
		double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
		space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
		return space;
	}
	/**
	 * 计算两点之间的距离
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private static double lineSpace(double x1, double y1, double x2, double y2) {
		double lineLength = 0;
		lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)* (y1 - y2));
		return lineLength;
	}

}

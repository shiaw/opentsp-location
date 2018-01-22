package com.navinfo.opentsp.platform.dp.core.rule.tools;

public class Test {
	public static void main(String[] args) {
//		Point p1 = new Point(1,4);
//		Point p2 = new Point(4,7);
//		Point p3 = new Point(8,5);
//		Point p4 = new Point(7,2);
//		Point p5 = new Point(3,1);
//		
//		Point p = new Point(4,4);
//		
//		Point[] pp = new Point[]{p1,p2,p3,p4,p5};
//		System.err.println(checkInPolygon(pp, p));
		
		
//		Point p1 = new Point(9,17);
//		Point p2 = new Point(15,19);
//		Point p3 = new Point(18,15);
//		Point p4 = new Point(16,11);
//		Point p5 = new Point(18,8);
//		Point p6 = new Point(15,5);
//		Point p7 = new Point(11,8);
//		Point p8 = new Point(7,7);
//		Point p9 = new Point(10,12);
//		
//		Point p = new Point(16, 7);
//		
//		Point[] pp = new Point[]{p1,p2,p3,p4,p5,p6,p7,p8,p9};
//		System.err.println(checkInPolygon(pp, p));
		
		
		Point p1 = new Point(116388819,39969305);
		Point p2 = new Point(116390654,39969186);
		Point p3 = new Point(116391201,39968125);
		Point p4 = new Point(116390659,39967895);
		Point p5 = new Point(116390986,39967438);
		Point p6 = new Point(116389710,39967229);
		Point p7 = new Point(116389146,39966871);
		Point p8 = new Point(116387617,39967763);
		Point p9 = new Point(116386523,39967401);
		Point p10 = new Point(116386722,39968709);
		
		Point p = new Point(116387606, 39967158);
		
		Point[] pp = new Point[]{p1,p2,p3,p4,p5,p6,p7,p8,p9,p10};
		System.err.println(checkInPolygon(pp, p));
	}
	/**
	 * 功能：判断点是否在多边形内 // 方法：求解通过该点的水平线与多边形各边的交点 // 结论：单边交点为奇数，成立!
	 * 参考：http://blog.csdn.net/okvee/article/details/5643407
	 * 13611346980249110116308150399371490001002000000000200000000011116314242399384271169
	 * @author shillyXiao E_mail:xiaoleyuan@foxmail.com 多边形的各个顶点坐标（首末点可以不一致）
	 * @param ptPolygon
	 * @param p
	 * @return
	 */
	static boolean checkInPolygon( Point[] ptPolygon,Point p) {

		int nCross = 0;
		int len=ptPolygon.length-1;
		for (int i = 0; i < len; i++) {
			Point p1 = ptPolygon[i];
			Point p2 = ptPolygon[(i + 1) % ptPolygon.length];

			// 求解 y=p.y 与 p1p2 的交点
			if (p1.getY() == p2.getY()) // p1p2 与 y=p0.y平行
				continue;

			if (p.getY() < Math.min(p1.getY(), p2.getY())) // 交点在p1p2延长线上
				continue;
			if (p.getY() >= Math.max(p1.getY(), p2.getY())) // 交点在p1p2延长线上
				continue;

			// 求交点的 X 坐标
			double x = (double) (p.getY() - p1.getY())
					* (double) (p2.getX() - p1.getX())
					/ (double) (p2.getY() - p1.getY()) + p1.getX();

			if (x > p.getX())
				nCross++; // 只统计单边交点
		}
		// System.out.println("nCross :"+nCross);
		// 单边交点为偶数，点在多边形之外 ---
		return (nCross % 2 == 1)?true:false;
	}
}

package com.navinfo.opentsp.platform.dp.core.rule.tools;

/**
 * 经X纬Y,以度为单位
 * @author aeroZH
 *
 */
public class Point {
	private double X;
	private double Y;

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("点信息：");
		buffer.append("X:" + this.X);
		buffer.append("Y:" + this.Y);
		return buffer.toString();
	}

	public Point() {
	}

	public Point(double x, double y) {
		this.X = x;
		this.Y = y;
	}

	public double getX() {
		return X;
	}

	public void setX(Double x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(double y) {
		Y = y;
	}
}

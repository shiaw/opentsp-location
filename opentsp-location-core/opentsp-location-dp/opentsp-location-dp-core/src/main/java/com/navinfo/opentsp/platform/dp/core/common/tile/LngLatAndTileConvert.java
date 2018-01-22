package com.navinfo.opentsp.platform.dp.core.common.tile;

public class LngLatAndTileConvert {
	public static long xyzToTileId(int z, int x, int y){
		long tileId = 0;
		tileId = (long)(z*Math.pow(10, 12))+(long)(x*Math.pow(10, 6))+y;
		return tileId;
	}
	
	public static int[] getTileNumber(long latitude, long longitude, int zoom) {
		double lat = ((double)latitude)/Math.pow(10, 6);
		double lon = ((double)longitude)/Math.pow(10, 6);
		int[] zxy = new int[3];
		zxy[0] = zoom;
		zxy[1] = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
		zxy[2] = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat))
				+ 1 / Math.cos(Math.toRadians(lat)))
				/ Math.PI)
				/ 2 * (1 << zoom));
		if (zxy[1] < 0)
			zxy[1] = 0;
		if (zxy[1] >= (1 << zoom))
			zxy[1] = ((1 << zoom) - 1);
		if (zxy[2] < 0)
			zxy[2] = 0;
		if (zxy[2] >= (1 << zoom))
			zxy[2] = ((1 << zoom) - 1);
		return zxy;
	}
}

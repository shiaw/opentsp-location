package com.navinfo.opentsp.platform.dp.core.common.tile;

import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaDataEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AreaToTile {

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

	//private static Map<Long, List<Long>> areaTiles = new HashMap<>();
	
	public static List<Long> areaToTiles(AreaEntity areaEntity, int zoom){
		List<Long> tiles = new ArrayList<>();
		//140000 无特定区域;140001 点；140002	圆形;140003 矩形;	140004 多边形;140005 路线;140006 路段
		switch(areaEntity.getAreaType()){
		case 140002:
			circleToTiles(areaEntity.getDatas().get(0), tiles, zoom);
			break;
		case 140003:
			rectangleToTiles(areaEntity.getDatas(), tiles, zoom);
			break;
		}
		return tiles;
	}
	
	public static void circleToTiles(AreaDataEntity circle, List<Long> list, int zoom){
		double[][] rect = enclosingRectangle(circle.getLongitude()/1000000d, circle.getLatitude()/1000000d, circle.getRadiusLength());
		int[] leftTop = LngLatAndTileConvert.getTileNumber((long)(rect[0][1]*1000000), (long)(rect[0][0]*1000000), zoom);
		int[] rightBottom = LngLatAndTileConvert.getTileNumber((long)(rect[1][1]*1000000), (long)(rect[1][0]*1000000), zoom);
		for (int i = leftTop[1]; i <= rightBottom[1] ; i++) {
			for (int j = leftTop[2]; j <= rightBottom[2]; j++) {
				list.add(LngLatAndTileConvert.xyzToTileId(zoom, i, j));
			}
		}
	}
	
	public static void rectangleToTiles(List<AreaDataEntity> rects, List<Long> list, int zoom){
		AreaDataEntity leftTopAreaData = null;
		AreaDataEntity rightBottomAreaData = null;
		for(AreaDataEntity entity : rects){
			if(leftTopAreaData == null){
				leftTopAreaData = entity;
			}else if(leftTopAreaData.getDataSn() > entity.getDataSn()){
				rightBottomAreaData = leftTopAreaData;
				leftTopAreaData = entity;
			}else{
				rightBottomAreaData = entity;
			}
		}
		int[] leftTop = LngLatAndTileConvert.getTileNumber(leftTopAreaData.getLatitude(), leftTopAreaData.getLongitude(), zoom);
		int[] rightBottom = LngLatAndTileConvert.getTileNumber(rightBottomAreaData.getLatitude(), rightBottomAreaData.getLongitude(), zoom);
		for (int i = leftTop[1]; i <= rightBottom[1] ; i++) {
			for (int j = leftTop[2]; j <= rightBottom[2]; j++) {
				list.add(LngLatAndTileConvert.xyzToTileId(zoom, i, j));
			}
		}
	}

	/*public static void main(String[] args) {
		//140000 无特定区域;140001 点；140002	圆形;140003 矩形;	140004 多边形;140005 路线;140006 路段
		AreaEntity area = new AreaEntity();
		area.setAreaId(100);
		area.setCreateDate(System.currentTimeMillis() / 1000);
		area.setAreaType(140002);
		AreaDataEntity areaData = new AreaDataEntity();
		areaData.setDataSn(1);
		areaData.setLatitude(38000000);
		areaData.setLongitude(118000000);
		areaData.setRadiusLength(50000);
		area.addAreaDataEntity(areaData);
		List<Long> list = areaToTiles(area, 12);
		for (Long tileId : list) {
			List<Long> areaIds = areaTiles.get(tileId);
			if (areaIds == null) {
				areaIds = new ArrayList<>();
				areaTiles.put(tileId, areaIds);
			}
			areaIds.add(area.getAreaId());
		}
	}*/
}

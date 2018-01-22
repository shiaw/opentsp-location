package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

import java.util.Random;

/**
 * 
 * @author HK
 *
 */
public class VehiclePassTimesQuadtree {
	private VehiclePassTimesTreeNode root = null;
	private int depth;
	
	public VehiclePassTimesQuadtree(int depth){
		this.depth = depth;
	}
	
	public VehiclePassTimesTreeNode getRoot(){
		return root;
	}
	
	public VehiclePassTimesTreeNode creatRoot(int z,int x, int y,int times, int month, int district){
		root = new VehiclePassTimesTreeNode(xyzToTileId(z,x,y), times, month, district);
		root.setCurrentDepth(1);
		return root;
	}
	
	public void createChildrenByDepth(VehiclePassTimesTreeNode node){
		if(node.getCurrentDepth()>=depth){
			return ;
		}
		int[] zxy = tileIdTozxy(node.getTileId());
		long tileIdLt = xyzToTileId(zxy[0]+1, 2*zxy[1], 2*zxy[2]);
		VehiclePassTimesTreeNode lt = new VehiclePassTimesTreeNode(tileIdLt, 0, node);
		node.addChildren(lt);
		createChildrenByDepth(lt);
		long tileIdRt = xyzToTileId(zxy[0]+1, 2*zxy[1]+1, 2*zxy[2]);
		VehiclePassTimesTreeNode rt = new VehiclePassTimesTreeNode(tileIdRt, 0, node);
		node.addChildren(rt);
		createChildrenByDepth(rt);
		long tileIdLb = xyzToTileId(zxy[0]+1, 2*zxy[1], 2*zxy[2]+1);
		VehiclePassTimesTreeNode lb = new VehiclePassTimesTreeNode(tileIdLb, 0, node);
		node.addChildren(lb);
		createChildrenByDepth(lb);
		long tileIdRb = xyzToTileId(zxy[0]+1, 2*zxy[1]+1, 2*zxy[2]+1);
		VehiclePassTimesTreeNode rb = new VehiclePassTimesTreeNode(tileIdRb, 0, node);
		node.addChildren(rb);
		createChildrenByDepth(rb);
	}
	
	public static long xyzToTileId(int z, int x, int y){
		long tileId = 0;
		tileId = (long)(z*Math.pow(10, 10))+(long)(x*Math.pow(10, 5))+y;
		return tileId;
	}
	
	public static int[] tileIdTozxy(long tileId){
		int[] zxy = new int[3];
		zxy[0] = (int)(tileId / Math.pow(10, 10));
		zxy[1] = (int)(tileId / Math.pow(10, 5))%((int)Math.pow(10, 5));
		zxy[2] = (int)(tileId % Math.pow(10, 5));
		return zxy;
	}
	
	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		Random random = new Random();
		for(int i=0;i<10000000;i++){
			getTileNumber(25925208+random.nextInt(100000), 101397824+random.nextInt(100000), 10);
		}
		System.err.println("计算10000000次耗时为(ms)"+(System.currentTimeMillis()-begin));
	}
	
	public static long getTileNumber(long latitude, long longitude, int zoom) {
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
		return xyzToTileId(zxy[0], zxy[1], zxy[2]);
	}
}

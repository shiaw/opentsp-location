package com.navinfo.tasktracker.rprest.util;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.navinfo.tasktracker.rprest.entity.VehiclePassTimesTreeNode;

public class VehiclePassTimesQuadtree {
    private VehiclePassTimesTreeNode root = null;
    private int depth;

    public VehiclePassTimesQuadtree(int depth) {
        this.depth = depth;
    }

    public VehiclePassTimesTreeNode getRoot() {
        return this.root;
    }

    public VehiclePassTimesTreeNode creatRoot(int z, int x, int y, int times, int month, int district) {
        this.root = new VehiclePassTimesTreeNode(xyzToTileId(z, x, y), times, month, district);
        this.root.setCurrentDepth(1);
        return this.root;
    }

    public void createChildrenByDepth(VehiclePassTimesTreeNode node) {
        if(node.getCurrentDepth() < this.depth) {
            int[] zxy = tileIdTozxy(node.getTileId());
            long tileIdLt = xyzToTileId(zxy[0] + 1, 2 * zxy[1], 2 * zxy[2]);
            VehiclePassTimesTreeNode lt = new VehiclePassTimesTreeNode(tileIdLt, 0, node);
            node.addChildren(lt);
            this.createChildrenByDepth(lt);
            long tileIdRt = xyzToTileId(zxy[0] + 1, 2 * zxy[1] + 1, 2 * zxy[2]);
            VehiclePassTimesTreeNode rt = new VehiclePassTimesTreeNode(tileIdRt, 0, node);
            node.addChildren(rt);
            this.createChildrenByDepth(rt);
            long tileIdLb = xyzToTileId(zxy[0] + 1, 2 * zxy[1], 2 * zxy[2] + 1);
            VehiclePassTimesTreeNode lb = new VehiclePassTimesTreeNode(tileIdLb, 0, node);
            node.addChildren(lb);
            this.createChildrenByDepth(lb);
            long tileIdRb = xyzToTileId(zxy[0] + 1, 2 * zxy[1] + 1, 2 * zxy[2] + 1);
            VehiclePassTimesTreeNode rb = new VehiclePassTimesTreeNode(tileIdRb, 0, node);
            node.addChildren(rb);
            this.createChildrenByDepth(rb);
        }
    }

    public static long xyzToTileId(int z, int x, int y) {
        long tileId = 0L;
        tileId = (long)((double)z * Math.pow(10.0D, 10.0D)) + (long)((double)x * Math.pow(10.0D, 5.0D)) + (long)y;
        return tileId;
    }

    public static int[] tileIdTozxy(long tileId) {
        int[] zxy = new int[]{(int)((double)tileId / Math.pow(10.0D, 10.0D)), (int)((double)tileId / Math.pow(10.0D, 5.0D)) % (int)Math.pow(10.0D, 5.0D), (int)((double)tileId % Math.pow(10.0D, 5.0D))};
        return zxy;
    }

    public static void main(String[] args) {
//        long begin = System.currentTimeMillis();
//        Random random = new Random();
//        for(int i = 0; i < 10000000; ++i) {
//            getTileNumber((long)(25925208 + random.nextInt(100000)), (long)(101397824 + random.nextInt(100000)), 10);
//        }

        long t = getTileNumber(41891628,123386673,15);
        int[]zxy = tileIdTozxy(t);
        long t3 = xyzToTileId(14,zxy[1],zxy[2]);
        long t4 = xyzToTileId(15,zxy[1],zxy[2]);
        long t1 = getTileNumber(41891628,123386673,14);
        long t2 = getTileNumber(41891628,123386674,15);
        System.out.println(t);
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
        System.out.println(t4);
//        int[]zxy = tileIdTozxy(152308712501L);

//        System.out.println(zxy);
//        xyzToTileId(15,23088, 12501);
//        System.err.println("璁＄畻10000000娆¤?楁椂涓?(ms)" + (System.currentTimeMillis() - begin));
    }

    public static long getTileNumber(long latitude, long longitude, int zoom) {
        double lat = (double)latitude / Math.pow(10.0D, 6.0D);
        double lon = (double)longitude / Math.pow(10.0D, 6.0D);
        int[] zxy = new int[]{zoom, (int)Math.floor((lon + 180.0D) / 360.0D * (double)(1 << zoom)), (int)Math.floor((1.0D - Math.log(Math.tan(Math.toRadians(lat)) + 1.0D / Math.cos(Math.toRadians(lat))) / 3.141592653589793D) / 2.0D * (double)(1 << zoom))};
        if(zxy[1] < 0) {
            zxy[1] = 0;
        }

        if(zxy[1] >= 1 << zoom) {
            zxy[1] = (1 << zoom) - 1;
        }

        if(zxy[2] < 0) {
            zxy[2] = 0;
        }

        if(zxy[2] >= 1 << zoom) {
            zxy[2] = (1 << zoom) - 1;
        }

        return xyzToTileId(zxy[0], zxy[1], zxy[2]);
    }

    public static int[] calculateParentTileIds(int x, int y, int z){
        int x1,y1,z1;
        if(x % 2 == 0){
            x1 = x/2;
        }else{
            x1 = (x-1)/2;
        }
        if(y % 2 == 0){
            y1 = y/2;
        }else{
            y1 = (y-1)/2;
        }
        z1 = z -1;
        int[] xyz = new int[]{x1, y1, z1};
        return  xyz;
    }

    public static long getParentTileId(long key){
        int[] zxy = VehiclePassTimesQuadtree.tileIdTozxy(key);
        int[] xyz = VehiclePassTimesQuadtree.calculateParentTileIds(zxy[1], zxy[2], zxy[0]);
        Long tile = VehiclePassTimesQuadtree.xyzToTileId(xyz[2], xyz[0], xyz[1]);
        return tile;
    }
}

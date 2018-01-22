package com.navinfo.opentsp.platform.da.core.common;

import com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCTerminalLocationData;

import java.util.*;

/**
 * Created by 修伟 on 2017/7/31 0031.
 * @User: yinchong
 * @Date: 2009-11-09
 * @Time: 21:15:21 To
 * @经纬度抽稀
 */

public class ThinningPoint {
    private int numLevels = 4;
    private int zoomFactor = 16;
    private double verySmall = 0.00003;
    private boolean forceEndPoints = true;
    private double[] zoomLevelBreaks; // 存放个级别的倍率值ֵ

    /**
     * 初始化加密线路类的构造函数
     *
     * @param numLevels
     *            抽稀级别,对应地图四个级别的跨度
     * @param zoomFactor
     *            用于计算zoomLevelBreaks各个级别倍率变化值，对应地图的4个级别的跨度
     * @param verySmall
     *            最小的显示比例级别，对应地图15级别的比例尺。
     * @param forceEndPoints
     *            是否显示起点终点
     */
    public ThinningPoint(int numLevels, int zoomFactor, double verySmall,
                           boolean forceEndPoints) {

        this.numLevels = numLevels;
        this.zoomFactor = zoomFactor;
        this.verySmall = verySmall;
        this.forceEndPoints = forceEndPoints;
        this.zoomLevelBreaks = new double[numLevels];

        for (int i = 0; i < numLevels; i++) {
            double dNum = verySmall
                    * Math.pow(this.zoomFactor, this.numLevels - i - 1);
            this.zoomLevelBreaks[i] = dNum;
        }
    }

    /**
     * 默认构造函数，根据各属性的默认值计算zoomLevelBreaks
     */
    public ThinningPoint() {
        this.zoomLevelBreaks = new double[numLevels];
        for (int i = 0; i < numLevels; i++) {
            double dNum = verySmall
                    * Math.pow(this.zoomFactor, this.numLevels - i - 1);
            this.zoomLevelBreaks[i] = dNum;
        }
    }

    /**
     * This computes the appropriate zoom level of a point in terms of it's
     * distance from the relevant segment in the DP algorithm. Could be done in
     * terms of a logarithm, but this approach makes it a bit easier to ensure
     * that the level is not too large.
     */
    private int computeLevel(double absMaxDist) {
        int level = 0;
        if (absMaxDist > this.verySmall) {//最大距离大有距离因子
            level = 0;
            while (absMaxDist < this.zoomLevelBreaks[level]) {//最大距离小于level的级别，则增加一个级别
                level++;
            }
            return level;
        }
        return level;
    }

    /**
     * 字符替换函数
     *
     * @param strOriginal
     *            需要处理的原始字符串
     * @param strOld
     *            被替换的老字符
     * @param strNew
     *            替换的新字符
     * @return 替换后的字符
     */
    public static String replace(String strOriginal, String strOld,
                                 String strNew) {
        if (strOriginal.equals("")) {
            return strOriginal;
        }
        String res = "";
        int i = strOriginal.indexOf(strOld, 0);
        int lastpos = 0;
        while (i != -1) {
            res += strOriginal.substring(lastpos, i) + strNew;
            lastpos = i + strOld.length();
            i = strOriginal.indexOf(strOld, lastpos);
        }
        res += strOriginal.substring(lastpos);
        return res;
    }

    /**
     * 编码经纬度在数组中的下标
     *
     * @param indexList
     *            下标集合
     * @return 编码后的数组中的下标串
     *//*
    private String encodeIndex(List<Integer> indexList) {
        StringBuffer strEncodeIndex = new StringBuffer();
        int indexLen = (indexList == null ? 0 : indexList.size());
        int pIndex = 0;
        for (int i = 0; i < indexLen; i++) {
            if (i == 0) {
                pIndex = indexList.get(i);
                strEncodeIndex.append(NumeralCoder.encodeNumber(pIndex));
                continue;
            }
            int currIndex = indexList.get(i);
            int diffIndex = (currIndex - pIndex);
            strEncodeIndex.append(NumeralCoder.encodeNumber(diffIndex));
            pIndex = currIndex;
        }
        return strEncodeIndex.toString();
    }*/

    /**
     * 编码经纬度在数组中的下标(非加密)
     *
     * @param indexList
     *            下标集合
     * @return 编码后的数组中的下标串
     *//*
    private String deCodeIndex(List<Integer> indexList) {
        StringBuffer strEncodeIndex = new StringBuffer();
        int indexLen = (indexList == null ? 0 : indexList.size());
        int pIndex = 0;
        for (int i = 0; i < indexLen; i++) {
            if (i == 0) {
                pIndex = indexList.get(i);
                strEncodeIndex.append("[").append(pIndex);
                continue;
            }
            int currIndex = indexList.get(i);
            int diffIndex = (currIndex - pIndex);
            strEncodeIndex.append(",").append(diffIndex);
            pIndex = currIndex;
        }
        strEncodeIndex.append("]");
        return strEncodeIndex.toString();
    }*/


    //增加按最小地图级别抽稀位置点的方法（返回结果不加密类型为list，有告警的位置点数据不抽稀，告警点只保留起点，连续的告警点不保留） songzb 20170216 start
    /**
     * Douglas-Peucker(道格拉斯 普克) algorithm 进行经纬度抽稀
     *
     * @param polyline
     * @param zoom
     * @param flag
     *            "temp" 时根据zoom返回数据
     * @return List<DYVehicleData>
     */
    public List<LCTerminalLocationData.TerminalLocationData> dpComputeToListByDaYun(List<LCTerminalLocationData.TerminalLocationData> polyline, int zoom, String flag, Map<String,Integer> holdPointMap, boolean withSpAndT) {
        List<LCTerminalLocationData.TerminalLocationData> resultList = new ArrayList<LCTerminalLocationData.TerminalLocationData>();
        int countPoints = polyline.size();
        Stack<int[]> stack = new Stack<int[]>();
        double[] dists = new double[countPoints];// 存储抽稀后的经纬度下标ꡣ
        double maxDist = 0;// 最大距离
        double absMaxDist = 0.0;// 绝对最大距离
        double currDist = 0.0;// 当前点距离
        int[] current;// 当前经纬度数组
        int maxLoc = 0;// 最大距离的经纬度数组下标

        if (countPoints > 2) {// 如果大于两个点，进行抽稀

            int[] stackVal = new int[] { 0, (countPoints - 1) };
            stack.push(stackVal);
            while (stack.size() > 0) {

                // 移除stack顶部对象并返回赋值给current
                current = stack.pop();
                maxDist = 0;
                // current[0]当前起始点，current[1]当前point数量

                for (int i = current[0] + 1; i < current[1]; i++) {
                    LCTerminalLocationData.TerminalLocationData point = polyline.get(i);
                    // 计算当前点，第一个点到最后一个点的距离.
                    currDist = this.distance(point, polyline.get(current[0]), polyline.get(current[1]));

                    if (currDist > maxDist) {// 如果当前距离大于最大距离,或者是拐点
                        maxDist = currDist;// 最大距离替换为当前的距离
                        maxLoc = i;
                        if (maxDist > absMaxDist) {// 如果最大距离大于绝对最大距离
                            absMaxDist = maxDist;
                        }
                    }
                }
                // 如果当前距离大于制定距离因子
                if (maxDist > this.verySmall) {
                    dists[maxLoc] = maxDist; // 更新dists数组总的maxLoc下表的最大值.
                    int[] stackValCurMax = { current[0], maxLoc };// 第一个经纬度到maxLoc下标经纬度创建一个stackValCurMax
                    stack.push(stackValCurMax);// 被递归计算
                    int[] stackValMaxCur = { maxLoc, current[1] };// 从maxLoc下标到最后一个经纬度创建一个stackValMaxCur
                    stack.push(stackValMaxCur);// 被递归计算
                }
            }
        }

        resultList = thinningPointByDaYun(polyline, dists, absMaxDist, zoom,
                flag, holdPointMap);
        return resultList;

    }



    /**
     * 以地图最小级别过滤抽稀后的经纬度，有告警的位置点不抽稀
     *
     * @param points
     *            抽稀前的原始经纬度集合
     * @param dists
     *            抽稀后的经纬度标识集合
     * @param absMaxDist
     *            最大距离
     * @return List<DYVehicleData 抽稀后的点集合
	 */
    private List<LCTerminalLocationData.TerminalLocationData> thinningPointByDaYun(List<LCTerminalLocationData.TerminalLocationData> points,
                                                     double[] dists, double absMaxDist, int zoom, String flag, Map<String,Integer> holdPointMap) {
        List<LCTerminalLocationData.TerminalLocationData> resultList = new ArrayList<>();

        //存儲event事件map
        Map<Integer, Integer> eventMap = new HashMap<Integer, Integer>();
        //存儲故障map
        Map<String, Integer> faultMap = new HashMap<String, Integer>();

        int pLen = points.size();

        // 根据传入zoom判断需要显示的级别
        int [] sZoom = {
                3,3,3,3,3,2,2,2,1,1,1,0,0,0,0,0,0,0,0
        };

        for (int i = 0; i < pLen; i++) {
            LCTerminalLocationData.TerminalLocationData point = points.get(i);
            if (point.getLocationData().getLongitude() < 0.00001 && point.getLocationData().getLatitude() < 0.00001){
                continue;
            }
            String pointStr = (point.getLocationData().getLongitude()+","+point.getLocationData().getLatitude());

            int pointLevel;// 判斷點所處的地圖級別
            if (i == 0 || i == (pLen - 1)) {
                // 起點或終點
                if (this.forceEndPoints) {
                    pointLevel = (this.numLevels - 1);
                } else {
                    pointLevel = (this.numLevels - computeLevel(absMaxDist) - 1);
                }
            } else {
                pointLevel = (this.numLevels - computeLevel(dists[i]) - 1);// 臨界點
            }

            //添加點標誌
            boolean addPointFlag = false;

            //更新事件及故障码标志
            //告警事件列表遍历
            Map<Integer, Integer> eventFlagMap = new HashMap<Integer, Integer>();//存储该轨迹点信息中包含的event list中的标志
            //event列表遍历
            if (point.getLocationData().getAdditionAlarm() != null && !point.getLocationData().getAdditionAlarm().isEmpty()){

                addPointFlag = true;//设置插入标识
                pointLevel = (this.numLevels - 1);//确定要插入事件点，则重置level。
            }

//			故障码列表遍历(本次不做抽稀时保留故障点的处理，如需要则打开该段代码)
            Map<String, Integer> faultFlagMap = new HashMap<String, Integer>();//存储该轨迹点信息中包含的故障码列表中的标志
            if (point.getLocationData().getBreakdownAddition() != null && !point.getLocationData().getBreakdownAddition().getBreakdownList().isEmpty()){

                addPointFlag = true;//设置插入标识
                pointLevel = (this.numLevels - 1);//确定要插入点，则重置level。
            }

            // 如果是 起點或終點或保持不抽稀的点，将此点的索引位置放到map中的value
            if (dists[i] != 0 || i == 0 || i == (pLen - 1) || (holdPointMap.get(pointStr)!=null)) {

                // 非临时线，原逻辑处理
                if (!"temp".equals(flag)) {
                    addPointFlag = true;
                    // 临时线，只处理与显示级别相同的点
                } else {
                    //showZoom
                    if (pointLevel >= sZoom[zoom]) {
                        addPointFlag = true;
                    }
                }
                //不抽稀的點
                if (i == 0 || i == (pLen - 1) || holdPointMap.get(pointStr)!=null){
                    addPointFlag = true;
                }
            }

            if (addPointFlag){
                resultList.add(point);// 存储抽稀的经纬度对象
            }
        }

        return resultList;
    }

    /**
     * distance(p0, p1, p2) computes the distance between the point p0 and the
     * segment [p1,p2]. This could probably be replaced with something that is a
     * bit more numerically stable.
     *
     * @param p0
     * @param p1
     * @param p2
     * @return
     */
    private double distance(LCTerminalLocationData.TerminalLocationData p0, LCTerminalLocationData.TerminalLocationData p1, LCTerminalLocationData.TerminalLocationData p2) {
        double u, out = 0.0;

        if (p1.getLocationData().getLatitude() == p2.getLocationData().getLatitude() && p1.getLocationData().getLatitude() == p2.getLocationData().getLatitude()) {
            out = Math.sqrt(Math.pow(p2.getLocationData().getLatitude() - p0.getLocationData().getLatitude(), 2)
                    + Math.pow(p2.getLocationData().getLongitude() - p0.getLocationData().getLongitude(), 2));
        } else {
            u = ((p0.getLocationData().getLatitude() - p1.getLocationData().getLatitude()) * (p2.getLocationData().getLatitude() - p1.getLocationData().getLatitude()) + (p0.getLocationData()
                    .getLongitude() - p1.getLocationData().getLongitude())
                    * (p2.getLocationData().getLongitude() - p1.getLocationData().getLongitude()))
                    / (Math.pow(p2.getLocationData().getLatitude() - p1.getLocationData().getLatitude(), 2) + Math.pow(p2.getLocationData()
                    .getLongitude()
                    - p1.getLocationData().getLongitude(), 2));

            if (u <= 0) {
                out = Math.sqrt(Math.pow(p0.getLocationData().getLatitude() - p1.getLocationData().getLatitude(), 2)
                        + Math.pow(p0.getLocationData().getLongitude() - p1.getLocationData().getLongitude(), 2));
            }
            if (u >= 1) {
                out = Math.sqrt(Math.pow(p0.getLocationData().getLatitude() - p2.getLocationData().getLatitude(), 2)
                        + Math.pow(p0.getLocationData().getLongitude() - p2.getLocationData().getLongitude(), 2));
            }
            if (0 < u && u < 1) {
                out = Math.sqrt(Math.pow(p0.getLocationData().getLatitude() - p1.getLocationData().getLatitude() - u
                        * (p2.getLocationData().getLatitude() - p1.getLocationData().getLatitude()), 2)
                        + Math.pow(p0.getLocationData().getLongitude() - p1.getLocationData().getLongitude() - u
                        * (p2.getLocationData().getLongitude() - p1.getLocationData().getLongitude()), 2));
            }
        }
        return out;
    }
}

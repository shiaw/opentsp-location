package com.navinfo.opentsp.platform.location.kit;

import java.io.Serializable;

/**
 * Created by machi1 on 2017/6/14.
 */
public class LocationMonitor implements Serializable {

    //模块+IP+端口
    private String moduleName;
    //模块名称
    private String appName;
    //实时时间
    private long currentTime;
    //进TA数据量
    private int inTaCount;
    //发送KAFKA数据量
    private int outTaCount;
    //回写终端数据量
    private int backTerminalCount;
    //进DP数据量
    private int inDpCount;
    //出RP数据量
    private int outRpCount;
    //出DA数据量
    private int outDaCount;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public int getInTaCount() {
        return inTaCount;
    }

    public void setInTaCount(int inTaCount) {
        this.inTaCount = inTaCount;
    }

    public int getOutTaCount() {
        return outTaCount;
    }

    public void setOutTaCount(int outTaCount) {
        this.outTaCount = outTaCount;
    }

    public int getBackTerminalCount() {
        return backTerminalCount;
    }

    public void setBackTerminalCount(int backTerminalCount) {
        this.backTerminalCount = backTerminalCount;
    }

    public int getInDpCount() {
        return inDpCount;
    }

    public void setInDpCount(int inDpCount) {
        this.inDpCount = inDpCount;
    }

    public int getOutRpCount() {
        return outRpCount;
    }

    public void setOutRpCount(int outRpCount) {
        this.outRpCount = outRpCount;
    }

    public int getOutDaCount() {
        return outDaCount;
    }

    public void setOutDaCount(int outDaCount) {
        this.outDaCount = outDaCount;
    }
}

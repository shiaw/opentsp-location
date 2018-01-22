package com.navinfo.opentsp.platform.rprest.dto;

/**
 * Created by wanliang on 2017/5/22.
 */
public class GetTotalMileageAndPackageDto {

   private long mileage;
    private long packageNum;
    private int onlineNum;
    private long driveNum;

    public long getMileage() {
        return mileage;
    }

    public void setMileage(long mileage) {
        this.mileage = mileage;
    }

    public long getPackageNum() {
        return packageNum;
    }

    public void setPackageNum(long packageNum) {
        this.packageNum = packageNum;
    }

    public int getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(int onlineNum) {
        this.onlineNum = onlineNum;
    }

    public long getDriveNum() {
        return driveNum;
    }

    public void setDriveNum(long driveNum) {
        this.driveNum = driveNum;
    }
}

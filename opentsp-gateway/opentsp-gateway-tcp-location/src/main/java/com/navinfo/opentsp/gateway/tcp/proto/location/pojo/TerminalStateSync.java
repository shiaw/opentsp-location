package com.navinfo.opentsp.gateway.tcp.proto.location.pojo;

import java.io.Serializable;

/**
 * @author 修伟 on 2017/9/21 0021.
 */
public class TerminalStateSync implements Serializable{

    public TerminalStateSync() {
    }
    /**时间：时间戳*/
    private long gpsDate;
    /**gps里程  (m)*/
    private long  gpsMileage;
    /**积分里程  (Km) */
    private long  integralMileage;
    /**积分油耗 L */
    private long  integralConsumption;

    public long getGpsDate() {
        return gpsDate;
    }

    public void setGpsDate(long gpsDate) {
        this.gpsDate = gpsDate;
    }

    public long getGpsMileage() {
        return gpsMileage;
    }

    public void setGpsMileage(long gpsMileage) {
        this.gpsMileage = gpsMileage;
    }

    public long getIntegralMileage() {
        return integralMileage;
    }

    public void setIntegralMileage(long integralMileage) {
        this.integralMileage = integralMileage;
    }

    public long getIntegralConsumption() {
        return integralConsumption;
    }

    public void setIntegralConsumption(long integralConsumption) {
        this.integralConsumption = integralConsumption;
    }

    @Override
    public String toString() {
        return "TerminalStateSync{" +
                "gpsDate=" + gpsDate +
                ", gpsMileage=" + gpsMileage +
                ", integralMileage=" + integralMileage +
                ", integralConsumption=" + integralConsumption +
                '}';
    }
}

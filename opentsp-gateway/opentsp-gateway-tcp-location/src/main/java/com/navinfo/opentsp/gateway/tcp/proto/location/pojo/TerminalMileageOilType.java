package com.navinfo.opentsp.gateway.tcp.proto.location.pojo;
/**
 * @author:qingqi
 * 将里程、油耗合并到标准值上
 * 里程有效性规则由高到低为：1 仪表里程km(mileageDD)、2 CAN里程（ECU） km(mileage)、3、积分里程km(differentialMileage)、4 终端里程(m)(mileage)
 * 油耗有效性规则由高到低为：1 CAN燃油总消耗L(totalFuelConsumption)、2 积分油耗L(integralFuelConsumption)
 * 速度有效性规则由高到低为：1 仪表速度Km/h(tachographVehicleSpeed)、2 车轮速度Km/h(wheelBasedVehicleSpd)、3 GPS速度Km/h(speed)
 * */
public class TerminalMileageOilType {
    private Long terminalId;

    private Integer mileageType;

    private Integer oilType;

    private Integer speedType;

    private Integer createTime;

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public Integer getMileageType() {
        return mileageType;
    }

    public void setMileageType(Integer mileageType) {
        this.mileageType = mileageType;
    }

    public Integer getOilType() {
        return oilType;
    }

    public void setOilType(Integer oilType) {
        this.oilType = oilType;
    }

    public Integer getSpeedType() {
        return speedType;
    }

    public void setSpeedType(Integer speedType) {
        this.speedType = speedType;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
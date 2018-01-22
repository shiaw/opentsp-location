package com.navinfo.opentsp.platform.rprest.dto;

/**
 * Created by wanliang on 2017/5/19.
 */
public class CarsInDistrictLastestDaysNumDto {

   private Integer district;
   private Integer[]    number;

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public Integer[] getNumber() {
        return number;
    }

    public void setNumber(Integer[] number) {
        this.number = number;
    }
}

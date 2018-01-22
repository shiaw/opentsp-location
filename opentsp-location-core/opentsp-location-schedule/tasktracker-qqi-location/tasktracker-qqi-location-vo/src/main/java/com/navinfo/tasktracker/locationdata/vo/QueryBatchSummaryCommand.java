package com.navinfo.tasktracker.locationdata.vo;


import javax.validation.constraints.Pattern;

/**
 * 批量查询按天统计信息并固化至mysql
 * create by wangshuai
 * @author
 */
public class QueryBatchSummaryCommand{

    @Pattern(message = "summaryDate格式不合法，应如：20170330", regexp = "^((((19){1}|(20){1})\\d{2})|\\d{2})[0,1]?\\d{1}[0-3]?\\d{1}$")
    private String summaryDate;

    public String carIds;

    private Boolean isExact = true;

    public String getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(String summaryDate) {
        this.summaryDate = summaryDate;
    }

    public String getCarIds() {
        return carIds;
    }

    public void setCarIds(String carIds) {
        this.carIds = carIds;
    }

    public Boolean isExact() {
        return isExact;
    }

    public void setIsExact(Boolean isExact) {
        this.isExact = isExact;
    }


    @Override
    public String toString() {
        return "QuerySummaryInfoCommand{" +
                "summaryDate='" + summaryDate + '\'' +
                ", isExact='" + isExact + '\'' +
                ", carIds='" + carIds + '\'' +
                '}';
    }
}
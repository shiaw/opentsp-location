package com.navinfo.opentsp.gateway.web.limiting;

/**
 * Result of request to ip-limiting service
 */
public class ApiGrantResult {
    private Integer status;
    private Boolean isSuccess;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSucess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Override
    public String toString() {
        return "ApiGrantResult{" +
                "status=" + status + ", isSucess=" + isSuccess + '}';
    }
}

package com.navinfo.opentsp.platform.rprest.cache;


/**
 * Registration entry of UserDevice, we suppose that any user can connect only through device (web browser is work on
 * device too), so any message can be send only to online device, but not to user.
 */
public final class DeviceRegistration {
    private String device;
    private String returnAddress;
    private long ttl;
    private DeviceStatus status;
    // private  onlineTime;termid、SIM、tcpGatewayQueue、offlineTime、onOffstatus、offReason

    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * Any device interact with system only through concrete gateway, therefore we need to know which gateway is used for send messages to device.
     *
     * @param returnAddress
     */
    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String getDevice() {
        return device;
    }

    /**
     * Any device interact with system only through concrete gateway, therefore we need to know which gateway is used for send messages to device.
     *
     * @return
     */
    public String getReturnAddress() {
        return returnAddress;
    }

    public long getTtl() {
        return ttl;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }
}

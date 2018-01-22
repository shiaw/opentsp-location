package com.navinfo.opentsp.platform.push;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/3
 * @modify
 * @copyright opentsp
 */
public enum DownCommandState {
    /**
     * 不在线：进行push之前(push里判断)
     * 0:进入push（命令初始化）                下发中
     * 1:tcpgateway write                      已发送
     * -1:sendandreceive超时(tcpgateway超时)   网关响应超时
     * 2: 终端已收到
     * -2：没收到终端已执行的回复（超时）
     * 3：终端开始执行
     * -3：没收到终端执行成功的回复（超时）
     */
    INIT(100000, "下发中"),
    SEND(100001, "已发送"),
    W_TIMEOUT(100019, "网关响应超时"),
    RECEIVE(100002, "终端已收到"),
    T_TIMEOUT(100029, "超时"),
    T_EXECUTE(100003, "执行成功");
    private final int value;
    private final String message;

    DownCommandState(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static DownCommandState adapt(int state) {
        DownCommandState[] allStatus = values();
        for (DownCommandState commandState : allStatus) {
            if (state == commandState.getValue()) {
                return commandState;
            }
        }
        return null;
    }
}

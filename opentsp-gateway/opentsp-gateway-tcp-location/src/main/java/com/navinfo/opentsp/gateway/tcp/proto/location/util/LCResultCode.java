package com.navinfo.opentsp.gateway.tcp.proto.location.util;

/**
 * 结果集代码
 *
 * @author aerozh-lgw
 */
public final class LCResultCode {
    /**
     * 终端-平台通用答应结果集
     *
     * @author aerozh-lgw
     */
    public static class JTTerminal {
        /**
         * 成功
         */
        public static final byte SUCCESS = 0x00;
        /**
         * 失败
         */
        public static final byte FAILURE = 0x01;
        /**
         * 消息错误
         */
        public static final byte MSG_ERROR = 0x02;
        /**
         * 不支持
         */
        public static final byte NOT_SUPPORT = 0x03;
        /**
         * 报警处理
         */
        public static final byte ALARM_PROCESS = 0x04;
        /**
         * 终端不在线
         */
        public static final byte NOT_ONLINE = 0x00;
        /**
         * 系统未找到此车辆
         */
        public static final byte NOT_FOUND_TERMINAL = 0x00;

    }

}

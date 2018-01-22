package com.navinfo.tasktracker.nilocation.entity;

import com.navinfo.tasktracker.nilocation.calculate.InterfaceResultCode;

/**
 * @author zhangyue
 */
public class  ResultCode implements InterfaceResultCode
{
    public static final int SUCCESS = 200;
    public static final int SERVER_ERROR = 506;
    public static final int BUSINESS_ERROR = 507;
    /**多次登录提示*/

    public static final int LOGIN_CODE_SEVENTEEN = 508;
    public static final int LOGIN_FAIL = 509;
    public static final int NO_MORE_DATA = 510;
    /**无法获取当前车辆位置信息提示*/

    public static final int NOT_CAR_POSITION = 511;
    public static final int EMAIL_SEND = 530;

    @Override public int code()
    {
        return 0;
    }

    @Override public String message()
    {
        return null;
    }
}

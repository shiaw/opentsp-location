package com.navinfo.opentsp.gateway.tcp.proto.location.util;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/9/7
 * @modify
 * @copyright opentsp
 */
public class IdGenerateUtil {

    private static int id=0;
    public synchronized static int getId(){
        id++;
        if(id>=Integer.MAX_VALUE){
            id=0;
        }
        return id;
    }
}

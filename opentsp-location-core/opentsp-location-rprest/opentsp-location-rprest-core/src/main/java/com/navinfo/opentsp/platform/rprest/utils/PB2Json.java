package com.navinfo.opentsp.platform.rprest.utils;

import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;

/**
 * Created by Administrator on 2017/10/9.
 */
public class PB2Json {

    public static Object format(Object o, boolean protoFormat){
        if(!(o instanceof Message)){
            return o;
        }
        if(!protoFormat){
            return Base64.encode(((Message) o).toByteArray());
        }
        return JsonFormat.printToString((Message)o);
    }
}

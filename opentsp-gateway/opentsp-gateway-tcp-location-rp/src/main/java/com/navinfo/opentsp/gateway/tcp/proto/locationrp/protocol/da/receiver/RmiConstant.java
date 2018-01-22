package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.da.receiver;

import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.da.DaRmiInterface;

public class RmiConstant
{
    /** 服务ID */
    public static final String RMI_SERVICE_ID = "opentsp-location-da";
    
    /** 接口名 */
    public static final String RMI_INTERFACE_NAME = "DaRmiInterface";
    
    /**
     * rmi接口类枚举
     */
    public enum RmiInterFaceEum
    {
        // rmi接口类
        DaRmiInterface(DaRmiInterface.class);
        
        private final Class classType;
        
        RmiInterFaceEum(Class classType)
        {
            this.classType = classType;
        }
        
        public Class getClassType()
        {
            return classType;
        }
    }
}

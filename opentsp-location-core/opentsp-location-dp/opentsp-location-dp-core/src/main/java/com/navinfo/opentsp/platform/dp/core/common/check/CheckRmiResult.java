package com.navinfo.opentsp.platform.dp.core.common.check;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes;

/**
 * Created by zhangyu on 2016/9/21.
 */
public class CheckRmiResult
{
    
    /**
     * 校验rmi返回数据是否正确
     * 
     * @param packet
     * @return
     * @throws InvalidProtocolBufferException
     */
    public static boolean checkResult(Packet packet)
        throws InvalidProtocolBufferException
    {
        
        LCServerCommonRes.ServerCommonRes commonRes = LCServerCommonRes.ServerCommonRes.parseFrom(packet.getContent());
        
        if (commonRes.getResponseId() == LCAllCommands.AllCommands.NodeCluster.ReportServerIdentify_VALUE
            && commonRes.getResults().getNumber() == LCPlatformResponseResult.PlatformResponseResult.success_VALUE)
        {
            return true;// 成功
        }
        else
        {
            return false;// 失败
        }
    }
    
}

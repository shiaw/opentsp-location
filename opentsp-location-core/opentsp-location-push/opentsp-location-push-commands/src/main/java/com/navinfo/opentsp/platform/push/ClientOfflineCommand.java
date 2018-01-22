package com.navinfo.opentsp.platform.push;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;
import org.springframework.security.access.annotation.Secured;

/**
 * 终端下线command
 * User: zhanhk
 * Date: 16/7/26
 * Time: 上午9:15
 */
@Secured("ROLE_USER")
@MessageGroup(PushModuleConstants.QUEUE)
public class ClientOfflineCommand extends AbstractCommand<CommandResult> {

    /**
     * 设备标识
     */
    private String deveiceId;

    /**
     * 下线时间
     */
    private long time = System.currentTimeMillis();

    /**
     * tcpgateway服务名称
     */
    private String tcpGatewayName;

    /**
     * 来源队列名称
     */
    private String queueName;

    public String getTcpGatewayName() {
        return tcpGatewayName;
    }

    public void setTcpGatewayName(String tcpGatewayName) {
        this.tcpGatewayName = tcpGatewayName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDeveiceId() {
        return deveiceId;
    }

    public void setDeveiceId(String deveiceId) {
        this.deveiceId = deveiceId;
    }

    @Override
    public Class<? extends CommandResult> getResultType() {
        return CommandResult.class;
    }

    @Override
    public String toString() {
        return "ClientOfflineCommand{" +
                "deveiceId='" + deveiceId + '\'' +
                ", time=" + time +
                ", tcpGatewayName='" + tcpGatewayName + '\'' +
                ", queueName='" + queueName + '\'' +
                '}';
    }
}

package com.navinfo.opentsp.platform.da.core.connector.mm;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import  com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import  com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMMutualCommandFacotry;
import  com.navinfo.opentsp.platform.da.core.connector.mm.session.MMMutualSession;
import  com.navinfo.opentsp.platform.da.core.connector.mm.session.MMMutualSessionManage;
import  com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import  com.navinfo.opentsp.platform.location.kit.LCConstant.MasterSlave;
import  com.navinfo.opentsp.platform.location.kit.Packet;


public class MMMutualHandler implements IoHandler {
    private final static Logger logger = LoggerFactory.getLogger(MMMutualHandler.class);

    private MasterSlave masterSlave;
    private int nodeCode;

    public MMMutualHandler(MasterSlave masterSlave, int nodeCode) {
        super();
        this.masterSlave = masterSlave;
        this.nodeCode = nodeCode;
    }

    @Override
    public void exceptionCaught(IoSession arg0, Throwable arg1)
            throws Exception {
        logger.error("链路[" + arg0.getAttribute("remoteIp") + ":" + arg0.getAttribute("remotePort") + "]发生异常.", arg1);
    }

    @Override
    public void messageReceived(IoSession arg0, Object arg1) throws Exception {
        MMMutualCommandFacotry.processor((Packet) arg1);
    }

    @Override
    public void messageSent(IoSession arg0, Object arg1) throws Exception {

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        //链路断开,获取节点编号,启动重连操作线程
        Object object = session.getAttribute("MasterSlave");
        Object nodeCode = session.getAttribute("nodeCode");
        if (object != null) {
            MMReconnect.reconnect(Integer.parseInt(String.valueOf(nodeCode)));
            final String remoteIp = session.getAttribute("remoteIp").toString();
            final String remotePort = session.getAttribute("remotePort").toString();
            logger.error("MM client connect [" + remoteIp + ":" + remotePort + "] is closed .");
        }
    }

    @Override
    public void sessionCreated(IoSession arg0) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {

    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        String address = session.getRemoteAddress().toString();
        //收集链路列表
        MMMutualSession mutualSession = new MMMutualSession();
        mutualSession.setId(session.getId());
        mutualSession.setFirstConnectTime(System.currentTimeMillis() / 1000);
        mutualSession.setLastMutualTime(System.currentTimeMillis() / 1000);
        mutualSession.setIoSession(session);
        MMMutualSessionManage.getInstance().addMutualSession(
                mutualSession.getId(), mutualSession);
        //设置链路属性
        //节点编号主要用在于重连使用
        String[] ip_port = address.split(":");
        session.setAttribute("remoteIp", ip_port[0].substring(1));
        session.setAttribute("remotePort", ip_port[1]);
        session.setAttribute("MasterSlave", this.masterSlave.name());
        session.setAttribute("nodeCode", this.nodeCode);
        //绑定主从链路
        MMMutualSessionManage.getInstance().masterOrSlaveBindSession(this.masterSlave, session.getId());
        MMMutualSessionManage.getInstance().nodeCodeSessionBind(this.nodeCode, session.getId());
        //发送节点标识汇报
        Packet reportServerIdentify = new Packet(true);
        reportServerIdentify.setProtocol(LCMessageType.PLATFORM);
        reportServerIdentify.setCommand(AllCommands.NodeCluster.ReportServerIdentify_VALUE);
        reportServerIdentify.setUniqueMark(NodeHelper.getNodeUniqueMark());
        reportServerIdentify.setTo(this.nodeCode);
        MMMutualCommandFacotry.processor(reportServerIdentify);
    }

}

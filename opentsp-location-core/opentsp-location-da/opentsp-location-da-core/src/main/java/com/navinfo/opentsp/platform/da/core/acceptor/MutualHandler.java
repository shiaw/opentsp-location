package com.navinfo.opentsp.platform.da.core.acceptor;

import com.navinfo.opentsp.platform.da.core.acceptor.procotol.MutualCommandFacotry;
import com.navinfo.opentsp.platform.da.core.acceptor.session.MutualSession;
import com.navinfo.opentsp.platform.da.core.acceptor.session.MutualSessionManage;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutualHandler implements IoHandler {
	private static final Logger logger = LoggerFactory.getLogger(MutualHandler.class);

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1) throws Exception {
		logger.error("链路["+arg0.getAttribute("remoteIp")+":"+arg0.getAttribute("remotePort")+"]发生异常.",arg1);
		Object object = arg0.getAttribute("nodeCode");
		logger.error("关闭链路"+object.toString()+"...");
	}

	@Override
	public void messageReceived(IoSession arg0, Object arg1) throws Exception {
		MutualCommandFacotry.processor((Packet) arg1);
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {

	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception {
		logger.error("mutual server disconnect from " + arg0.toString());
		/*Object object = arg0.getAttribute("nodeCode");
		if(object != null){
			MutualSessionManage.getInstance().unbind(Long.parseLong(String.valueOf(object)));
		}*/
		MutualSessionManage.getInstance().delMutualSession(arg0.getId());
		arg0.closeOnFlush();
	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
		logger.info("mutual server accepted from " + arg0.toString() + " created .");
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		logger.info("session " + arg0.toString() + " idle.");
		this.sessionClosed(arg0);
	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
		logger.info("mutual server accepted from " + arg0.toString() + " open .");
		MutualSession mutualSession = new MutualSession();
		mutualSession.setId(arg0.getId());
		mutualSession.setFirstConnectTime(System.currentTimeMillis() / 1000);
		mutualSession.setLastMutualTime(System.currentTimeMillis() / 1000);
		mutualSession.setIoSession(arg0);
		logger.info("bind session to sessionManage:"+mutualSession.getId());
		MutualSessionManage.getInstance().addMutualSession(mutualSession.getId(), mutualSession);
	}

}

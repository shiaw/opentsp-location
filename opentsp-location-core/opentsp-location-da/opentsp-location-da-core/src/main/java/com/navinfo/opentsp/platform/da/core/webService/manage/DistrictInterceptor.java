package com.navinfo.opentsp.platform.da.core.webService.manage;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  com.navinfo.opentsp.platform.da.core.common.Configuration;
import  com.navinfo.opentsp.platform.da.core.common.Constant.ConfigKey;
public class DistrictInterceptor extends AbstractPhaseInterceptor<Message> {

	private Logger logger = LoggerFactory.getLogger(DistrictInterceptor.class);
	public DistrictInterceptor(String phase) {
		super(phase);
	}

	@Override
	public void handleMessage(Message arg0) throws Fault {
		HttpServletRequest request = (HttpServletRequest) arg0
				.get(AbstractHTTPDestination.HTTP_REQUEST);
		// 取客户端IP地址
		String ipAddress = request.getRemoteAddr();
		String ws_center_ip = Configuration.getString(ConfigKey.WS_CENTERIP);
		if(!ipAddress.equals(ws_center_ip)){
			logger.error(ipAddress + "是非法ip");
			throw new Fault(new IllegalAccessException(ipAddress + "是非法ip"));
		}
	}

}

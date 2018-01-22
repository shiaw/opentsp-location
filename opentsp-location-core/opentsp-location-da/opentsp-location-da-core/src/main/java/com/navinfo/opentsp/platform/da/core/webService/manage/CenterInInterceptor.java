package com.navinfo.opentsp.platform.da.core.webService.manage;

import java.util.UUID;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;


public class CenterInInterceptor extends AbstractSoapInterceptor {

	private Logger logger = LoggerFactory.getLogger(CenterInInterceptor.class);
	/* 管理系统的sessionId */
	private String sessionId;

	public CenterInInterceptor() {
		super(Phase.PRE_INVOKE);
	}

	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		QName qnameCredentials = new QName("DynamicPassword");
		if (message.hasHeader(qnameCredentials)) {
			Header header = message.getHeader(qnameCredentials);
			Element elementOrderCredential = (Element) header.getObject();
			Node sessionid = elementOrderCredential.getFirstChild();
			Node type = elementOrderCredential.getLastChild();
			// 根据type进行判断，如果是登录则，直接根据sessionid创建动态口令，并放到东动态口令缓存，如果不是登录，则根据sessionid判断动态口令是否正确，正确则修改动态口令时间，如果不存在则返回异常
			// 所以对类型的处理是login或者other
//			logger.info(type.getTextContent());
			if ("login".equals(type.getTextContent())) {
				DynamicPassword dynamicPassword = new DynamicPassword();
				dynamicPassword.setActionTime(System.currentTimeMillis() / 1000);
				dynamicPassword.setDynamicPassword(UUID.randomUUID().toString());
				DynamicPasswordCache.addInstance(sessionid.getTextContent(),
						dynamicPassword);
			} else {
				DynamicPassword dynamicPassword = DynamicPasswordCache
						.getInstance(sessionid.getTextContent());
				if (dynamicPassword != null) {
					if (StringUtils.isBlank(dynamicPassword
							.getDynamicPassword())) {
						throw new RuntimeException("session过期");
					} else {
						//维护动态口令时间
						dynamicPassword.setActionTime(System
								.currentTimeMillis() / 1000);
						DynamicPasswordCache.addInstance(sessionid.getTextContent(),
								dynamicPassword);
					}
				} else {
					//throw new RuntimeException("session过期");
				}
			}

		} else {
			// 返回消息体错误
			throw new RuntimeException("消息体异常");
		}

	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}

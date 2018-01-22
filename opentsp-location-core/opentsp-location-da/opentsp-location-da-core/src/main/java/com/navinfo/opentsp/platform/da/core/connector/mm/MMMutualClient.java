package com.navinfo.opentsp.platform.da.core.connector.mm;

import java.net.InetSocketAddress;
import java.util.concurrent.AbstractExecutorService;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  com.navinfo.opentsp.platform.da.core.cache.InstanceCache;
import  com.navinfo.opentsp.platform.da.core.common.Configuration;
import  com.navinfo.opentsp.platform.da.core.connector.mm.codec.MMMutualCodecFactory;
import  com.navinfo.opentsp.platform.location.kit.LCConstant.MasterSlave;

/**
 * DA连接MM做状态监控和调度处理工作，二者始终保持链路，链路断开保持循环尝试直到成功。
 *
 * @author HK
 *
 */

public class MMMutualClient {
	private static final Logger log = LoggerFactory
			.getLogger(MMMutualClient.class);
	/** 连接对象 */
	private IoConnector connector = null;
	/** 是否启动 */
	private boolean isStart = false;
	/** 每次读取最大字节 */
	private int READ_SIZE = 1024 * 5;
	/** 链路最大空闲时间 */
	private int WRITE_TIME_OUT = 300;
	/**当前连接IP*/
	private String connectorIp;
	/**当前连接Port*/
	private int connectorPort;
	/**服务当前已重连次数*/
	private int _reconnector_number = 0;
	/**重连延迟间隔(秒)*/
	private int reconnectDelayInterval = 5;
	private MMMutualCodecFactory codecFactory = new MMMutualCodecFactory();
	private AbstractExecutorService executor = new OrderedThreadPoolExecutor(100);
	private IoHandler handler = null;
	private MasterSlave masterSlave;
	private int nodeCode =0;

	public int start(MasterSlave masterSlave){
		this.masterSlave = masterSlave;
		if(masterSlave.ordinal() == MasterSlave.Master.ordinal()){//Master
			this.connectorIp = Configuration.getString("MM.MASTER.IP");
			this.connectorPort = Configuration.getInt("MM.MASTER.PORT");
			this.nodeCode = Configuration.getInt("MM.MASTER.CODE");
			this.connect();
		}else if(masterSlave.ordinal() == MasterSlave.Slave.ordinal()){//Slaver
			this.connectorIp = Configuration.getString("MM.SLAVE.IP");
			this.connectorPort = Configuration.getInt("MM.SLAVE.PORT");
			this.nodeCode = Configuration.getInt("MM.SLAVE.CODE");
			this.connect();
		}
		return 0;
	}

	/**
	 * 启动服务,1：成功;-1：失败
	 * @return
	 */
	private int connect(){
		this._reconnector_number = 0;
		connector = new NioSocketConnector();
		connector.getFilterChain().addFirst("codec", new ProtocolCodecFilter(codecFactory));
		connector.getFilterChain().addLast("threadPool", new ExecutorFilter(executor));
		 handler = new MMMutualHandler(this.masterSlave , this.nodeCode);
		connector.setHandler(handler);
		connector.getSessionConfig().setMaxReadBufferSize(READ_SIZE);
		connector.getSessionConfig().setWriterIdleTime(WRITE_TIME_OUT);
		try {
			ConnectFuture connectFuture = connector
					.connect(new InetSocketAddress( this.connectorIp , this.connectorPort ));
			connectFuture.awaitUninterruptibly();
			isStart = connectFuture.isConnected();
			InstanceCache.addInstance(nodeCode, this);
			if(isStart){
				log.info("DA节点连接MM节点成功. IP：" + "[" +  this.connectorIp + "]" + " PORT：" + "["
						+  this.connectorPort + "] .");
				return 1;
			}else{
				log.info("DA节点连接MM节点失败. IP：" + "[" +  this.connectorIp + "]" + " PORT：" + "["
						+  this.connectorPort  + "] .");
				MMReconnect.reconnect(this.nodeCode);
				return -1;
			}
		} catch (Exception e) {
			log.info("DA节点连接MM节点失败. IP：" + "[" +  this.connectorIp + "]" + " PORT：" + "["
					+  this.connectorPort  + "] .", e);
			return -1;
		}
	}

	public void stop(){

	}
	public void restart(){

	}

	/**
	 * 重连服务,返回已重连次数<br>
	 * 0为重连成功
	 * @return
	 */
	public int reconnector(){
		if(this.connector.isActive()){
			return 0;
		}
		ConnectFuture connectFuture = this.connector.connect(new InetSocketAddress(this.connectorIp, this.connectorPort));
		connectFuture.awaitUninterruptibly();
		if(connectFuture.isConnected()){
			log.info("DA节点重连MM节点成功. IP：" + "[" +  this.connectorIp + "]" + " PORT：" + "["
					+  this.connectorPort + "] .");
			this._reconnector_number = 0;
		}else{
			log.info("DA节点重连MM节点失败. IP：" + "[" +  this.connectorIp + "]" + " PORT：" + "["
					+  this.connectorPort + "] .");
			this._reconnector_number++;
		}
		return _reconnector_number;
	}
	
	public int getReconnectDelayInterval() {
		return reconnectDelayInterval;
	}
	public void setReconnectDelayInterval(int reconnectDelayInterval) {
		this.reconnectDelayInterval = reconnectDelayInterval;
	}
	public String getConnectorIp() {
		return connectorIp;
	}
	public void setConnectorIp(String connectorIp) {
		this.connectorIp = connectorIp;
	}
	public int getConnectorPort() {
		return connectorPort;
	}
	public void setConnectorPort(int connectorPort) {
		this.connectorPort = connectorPort;
	}

}

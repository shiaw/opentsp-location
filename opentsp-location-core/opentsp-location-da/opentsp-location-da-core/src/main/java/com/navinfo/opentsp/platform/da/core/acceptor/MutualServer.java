

package com.navinfo.opentsp.platform.da.core.acceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.navinfo.opentsp.platform.da.core.acceptor.codec.MutualCodecFactory;
import com.navinfo.opentsp.platform.da.core.cache.InstanceCache;
import com.navinfo.opentsp.platform.da.core.common.Configuration;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MutualServer {
	private IoAcceptor acceptor = null;
	private final Logger log = LoggerFactory.getLogger(MutualServer.class);
	private final int _reader_idle_time = 30000;
	private final int _max_read = 1024;

	private ProtocolCodecFactory codecFactory = new MutualCodecFactory();
	private IoHandler handler = new MutualHandler();

	public int start() {
		acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
		acceptor.getFilterChain().addFirst("threadPool", new ExecutorFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(codecFactory));
		acceptor.setHandler(handler);
		acceptor.getSessionConfig().setReaderIdleTime(this._reader_idle_time);
		acceptor.getSessionConfig().setMaxReadBufferSize(this._max_read);
		//缓存服务实例
		InstanceCache.addInstance(Configuration.getInt("NODE.CODE"), this);
		try {
			acceptor.bind(new InetSocketAddress(Configuration.getString("DATA.ACCESS.IP"), Configuration.getInt("DATA.ACCESS.PORT")));
			log.info("DA节点数据访问服务启动完成,IP：" + "[" + Configuration.getString("DATA.ACCESS.IP") + "]" + ",PORT：" + "["
					+  Configuration.getInt("DATA.ACCESS.PORT") + "] .");
			return 1;
		} catch (IOException e) {
			log.info("DA节点数据访问服务启动失败,IP：" + "[" + Configuration.getString("DATA.ACCESS.IP") + "]" + ",PORT：" + "["
					+  Configuration.getInt("DATA.ACCESS.PORT")  + "] .", e);
			return -1;
		}
	}

	public void stop() {

	}

	public void restart() {

	}
}

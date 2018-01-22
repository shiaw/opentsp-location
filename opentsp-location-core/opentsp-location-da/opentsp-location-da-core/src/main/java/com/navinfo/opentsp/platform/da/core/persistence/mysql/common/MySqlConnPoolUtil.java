package com.navinfo.opentsp.platform.da.core.persistence.mysql.common;

import java.beans.PropertyVetoException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.navinfo.opentsp.platform.da.core.common.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ss
 * 
 */
public class MySqlConnPoolUtil {

	private static Logger logger = LoggerFactory.getLogger(MySqlConnPoolUtil.class);
	private static ComboPooledDataSource MASTER_POOL = null;
	private static ComboPooledDataSource SLAVER_POOL = null;
	/**
	 * 5 声明线程共享变量
	 */
	public static ThreadLocal<Connection> container = new ThreadLocal<Connection>();
	/* 初始化连接池参数 */
	static {
		try {
			MASTER_POOL = createPool(1);
			SLAVER_POOL = createPool(2);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}


	}

	private static ComboPooledDataSource createPool(int masterOrSlave) throws UnsupportedEncodingException {
		String mark = "";
		if (masterOrSlave == 1) {
			mark = "master";
		} else {
			mark = "slave";
		}
		ComboPooledDataSource pool = new ComboPooledDataSource();
		String jdbc="jdbc:mysql://"
		+ Configuration.getString("mysql." + mark + ".ip") + ":"
		+ Configuration.getString("mysql." + mark + ".port") + "/"
		+ Configuration.getString("mysql." + mark + ".db")+"?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&failOverReadOnly=false";
		try {
			pool.setDriverClass(Configuration.getString("mysql.driver"));
			pool.setJdbcUrl(jdbc);
			pool.setUser(Configuration.getString("mysql." + mark + ".user"));
			pool.setPassword(Configuration.getString("mysql." + mark
					+ ".password"));
			pool.setMaxPoolSize(Integer.parseInt(Configuration
					.getString("mysql.maxPoolSize")));
			pool.setMinPoolSize(Integer.parseInt(Configuration
					.getString("mysql.minPoolSize")));
			pool.setMaxIdleTime(Integer.parseInt(Configuration
					.getString("mysql.maxIdleTime")));
			pool.setInitialPoolSize(Integer.parseInt(Configuration
					.getString("mysql.initialPoolSize")));
			pool.setAcquireIncrement(Integer.parseInt(Configuration
					.getString("mysql.acquireIncrement")));
			pool.setAcquireRetryAttempts(Integer.parseInt(Configuration
					.getString("mysql.acquireRetryAttempts")));
			pool.setAcquireRetryDelay(Integer.parseInt(Configuration
					.getString("mysql.acquireRetryDelay")));
			pool.setMaxStatements(Integer.parseInt(Configuration
					.getString("mysql.maxStatements")));
			pool.setIdleConnectionTestPeriod(Integer.parseInt(Configuration
					.getString("mysql.idleConnectionTestPeriod")));
			pool.setCheckoutTimeout(Integer.parseInt(Configuration
					.getString("mysql.checkoutTimeout")));
			pool.setTestConnectionOnCheckin(Boolean.parseBoolean(Configuration
					.getString("mysql.testConnectionOnCheckin")));
			pool.setTestConnectionOnCheckout(Boolean.parseBoolean(Configuration
					.getString("mysql.testConnectionOnCheckout")));
			pool.setAutomaticTestTable(Configuration
					.getString("mysql.automaticTestTable"));
			return pool;
		} catch (PropertyVetoException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 获取共享变量
	 */
	public static ThreadLocal<Connection> getContainer() {
		return container;
	}

	/**
	 * 获取当前线程上的连接 开启事务
	 */
	public static void startTransaction() {
		Connection conn = container.get();// 首先获取当前线程的连接
		if (conn == null) {// 如果连接为空
			conn = getConnection(); // 从连接池中获取连接
			container.set(conn);// 将此连接放在当前线程上
		}
		try {
			conn.setAutoCommit(false);// 开启事务
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
		}
//		try {
//			logger.error("BusyConnections..." + MASTER_POOL.getNumBusyConnections());
//			logger.error("IdleConnections..." + MASTER_POOL.getNumIdleConnections());
//			logger.error("TotalConnections..." + MASTER_POOL.getNumConnections());
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
	}

	/**
	 * 提交事务
	 */
	public static void commit() {
		Connection conn = container.get();// 从当前线程上获取连接
		if (conn != null) {// 如果连接为空，则不做处理
			try {
				conn.commit();// 提交事务
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	/**
	 * 回滚事务
	 */
	public static void rollback() {
		Connection conn = container.get();// 检查当前线程是否存在连接
		if (conn != null) {
			try {
				conn.rollback();// 回滚事务
				// container.remove();//如果回滚了，就移除这个连接
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	/**
	 * 关闭连接
	 */
	public static void close() {
		Connection conn = container.get();
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				container.remove();// 从当前线程移除连接 切记
			}
		}
	}

	/**
	 * 获取数据库连接<br>
	 * 优先从master获取连接,再从slaver获取连接
	 * 
	 * @return
	 */
	public synchronized static Connection getConnection() {
		try {
			return getMaster();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return getSlaver();
			} catch (SQLException e1) {
				logger.error(e1.getMessage(),e1);
				return null;
			}
		}
	}

	public synchronized static Connection getMaster() throws SQLException {
		return MASTER_POOL.getConnection();
	}

	public synchronized static Connection getSlaver() throws SQLException {
		return SLAVER_POOL.getConnection();

	}

}

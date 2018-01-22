package com.navinfo.opentsp.platform.da.core.persistence.redis;

public class RedisException extends Exception {

	private static final long serialVersionUID = 1L;

	public RedisException() {
		super();
	}

	public RedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super();
	}

	public RedisException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisException(String message) {
		super(message);
	}

	public RedisException(Throwable cause) {
		super(cause);
	}
}

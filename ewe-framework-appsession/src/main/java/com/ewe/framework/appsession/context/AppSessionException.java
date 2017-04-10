package com.ewe.framework.appsession.context;

public class AppSessionException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppSessionException() {
		super();
	}

	public AppSessionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AppSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppSessionException(String message) {
		super(message);
	}

	public AppSessionException(Throwable cause) {
		super(cause);
	}

}

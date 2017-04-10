package com.ewe.exec.context;

public class ExecutException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void createAndThrow(String msg){
		throw new ExecutException(msg);
	}

	public ExecutException() {
		super();
	}

	public ExecutException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExecutException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExecutException(String message) {
		super(message);
	}

	public ExecutException(Throwable cause) {
		super(cause);
	}
}

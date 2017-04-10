package com.ewe.framework.exception;

import com.ewe.framework.context.MsgConfig;

/**
 * the server base exception class<p>
 * notice that the base interceptor noly handle this class<p>
 * so if make the specific exception must extends this class<p>
 * @author Lee-yo
 * 2015-6-2
 */
public class BaseException extends RuntimeException{
	
	private String msg;
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private Integer msgCode;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BaseException() {
		super();
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(Integer msgCode) {
		this.msgCode = msgCode;
		this.msg = MsgConfig.getMsg(msgCode+"");
	}
	
	public BaseException(Integer msgCode, Object ... params){
		super(MsgConfig.getMsg(msgCode+"", params));
		this.msgCode = msgCode;
		this.msg = MsgConfig.getMsg(msgCode+"", params);
	}

	public Integer getCode() {
		if(this.msgCode!=null)
			return this.msgCode;
		return null;
	}
	
}

package com.ewe.framework.exception.utils;

import com.ewe.framework.exception.BaseException;

/**
 * 
 * @author Yo-Lee:catreekk@sohu.com
 * @version 2014年11月21日下午5:32:27
 */
public class ExceptionUtils {

	public static void throwSimpleEx(int code)throws BaseException{
		throw new BaseException(code);
	}
	
	public static void throwSimpleEx(int code, Object ... params)throws BaseException{
		throw new BaseException(code, params);
	}
	
	public static void throwError() throws BaseException{
		throw new BaseException(9000);
	}
	
}

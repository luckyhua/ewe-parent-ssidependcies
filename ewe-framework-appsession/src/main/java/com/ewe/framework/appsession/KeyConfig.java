package com.ewe.framework.appsession;

import org.apache.commons.lang.StringUtils;

public class KeyConfig {
	public static final String Cache_Key = "cache:";
	
	/*
	 ----------------those constant used is session api------------------
	 ----------------session api redis key start with session------------
	 */
	
	/**
	 * used in store in redis tokens map key
	 */
	public static final String Tokens_Key = "session:tokens_map:userid_token";
	/**
	 * used in store in redis tokens map userid:token mapping key
	 */
	private static final String Userid_Token_Key = "session:userid_token:";
	/**
	 * used in store in redis token:session mapping key
	 */
	private static final String Token_Session_Key = "session:token_session:";
	
	/**
	 * used in store in redis vcodes map  key
	 */
//	public static final String Vcodes_Key = "cache:vcodes_map:mobile_token";
	
	/**
	 * in redis mobile:vcode  mapping key
	 */
	private static final String Mobile_VCode_Key = "cache:mobile_vcode:";
	
	/**
	 * used to make session key by token
	 * @param token
	 * @return session key
	 */
	public static String getSessionKey(String token){
		if(StringUtils.isBlank(token))
			return "";
		return Token_Session_Key+token;
	}
	
	/**
	 * used to make tokenKey by userId
	 * @param userId
	 * @return tokenKey
	 */
	public static String getTokenKey(String userId){
		if(StringUtils.isBlank(userId))
			return "";
		return Userid_Token_Key+userId;
	}
	
	
	
	public static String getVcodeKey(String mobile){
		if(StringUtils.isBlank(mobile))
			return null;
		return Mobile_VCode_Key+mobile;
	}
	
}

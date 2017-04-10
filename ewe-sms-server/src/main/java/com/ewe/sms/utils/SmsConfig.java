package com.ewe.sms.utils;

public class SmsConfig {

	/** ---------- 创世华信 ---------- **/
	public static final String CSHX_SN = "jkwl079";
	public static final String CSHX_PWD = "jkwl07911";
	public static final String CSHX_URL = "http://sh2.ipyy.com/sms.aspx";

	/** ---------- 云片 ---------- **/
	public static final String YP_APIKEY = "38a7853893ef124c3fa463123b71186f";
	// 查账户信息的http地址
	public static final String URI_GET_USER_INFO = "http://yunpian.com/v1/user/get.json";
	// 通用发送接口的http地址
	public static final String URI_SEND_SMS = "http://yunpian.com/v1/sms/send.json";
	// 模板发送接口的http地址
	public static final String URI_TPL_SEND_SMS = "http://yunpian.com/v1/sms/tpl_send.json";
	// 发送语音验证码接口的http地址
	public static final String URI_SEND_VOICE = "http://yunpian.com/v1/voice/send.json";
	public static final long  YP_TPL_ID_1  = 898495;
	
}

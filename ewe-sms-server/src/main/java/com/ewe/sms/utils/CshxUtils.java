package com.ewe.sms.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * ClassName:CshxUtils
 * Description:创世华信短信发送工具类
 *
 * @author luckyhua
 * @Date 2015年6月18日下午4:05:01
 * @version 
 *
 */
public class CshxUtils {

	private static String reqUrl; //请求地址
	private static Map<String, String> mapParams = new HashMap<String, String>();

	static {
		reqUrl = SmsConfig.CSHX_URL;
		mapParams.put("userid", "");
		mapParams.put("account", SmsConfig.CSHX_SN);
		mapParams.put("password", SmsConfig.CSHX_PWD);
	}

	//查询余额
	public static String SelSum() {
		mapParams.put("action", "overage");
		return HttpUtils.getRequestResult(mapParams, reqUrl);
	}
	
	//短信发送
	public static String SendMessage(String mobile, String content) {
		mapParams.put("action", "send");
		mapParams.put("mobile", mobile); //发信发送的目的号码.多个号码之间用半角逗号隔开
		mapParams.put("content", content);
		mapParams.put("sendTime", ""); //定时发送，为空表示立即发送，定时发送格式2010-10-24 09:08:10
		mapParams.put("extno", ""); //请先询问配置的通道是否支持扩展子号，如果不支持，请填空。子号只能为数字，且最多5位数
		return HttpUtils.getRequestResult(mapParams, reqUrl);
	} 
	
}

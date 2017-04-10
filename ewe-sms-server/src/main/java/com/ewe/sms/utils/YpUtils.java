package com.ewe.sms.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author   E-mail:liuchou.ewedata.com
 * @date 创建时间：2015年7月21日 下午3:41:42 
 * @Description 
 * @version 1.0 
 * @since  
 *  
 */
public class YpUtils {
    //编码格式。发送编码格式统一用UTF-8
//    private static String ENCODING = "UTF-8";
    private static String apikey = SmsConfig.YP_APIKEY;
	
	 /**
     * 取账户信息
     *
     * @return json格式字符串
     * @throws java.io.IOException
     */
    public static String getUserInfo() throws IOException, URISyntaxException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        return HttpUtils.postRequestResult(params, SmsConfig.URI_GET_USER_INFO);
    }

    /**
     * 通用接口发短信
     *
     * @param text   　短信内容
     * @param mobile 　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String sendSms( String text, String mobile) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("text", text);
        params.put("mobile", mobile);
        return HttpUtils.postRequestResult(params, SmsConfig.URI_SEND_SMS);
    }

    /**
     * 通过模板发送短信
     *
     * @param tpl_id    　模板id
     * @param tpl_value 　模板变量值
     * @param mobile    　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String tplSendSms(long tpl_id, String tpl_value, String mobile) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("tpl_id", String.valueOf(tpl_id));
        params.put("tpl_value", tpl_value);
        params.put("mobile", mobile);
        return HttpUtils.postRequestResult(params, SmsConfig.URI_TPL_SEND_SMS);
    }

    /**
     * 通过接口发送语音验证码
     * @param mobile 接收的手机号
     * @param code   验证码
     * @return
     */
    public static String sendVoice( String mobile, String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("mobile", mobile);
        params.put("code", code);
        return HttpUtils.postRequestResult(params, SmsConfig.URI_SEND_VOICE);
    }

}

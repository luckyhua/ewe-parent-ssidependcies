package com.ewe.sms.sender;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Sender {
	
	public static final Logger log = LoggerFactory.getLogger(Sender.class);

	public static final String SEND_SUCCESS ="1";
	public static final String SEND_FAILED = "2";
	
	public static ResourceBundle bundle = ResourceBundle.getBundle("config/sms/template");
	
	private String operator; //运营商
	private String templateKey; //模板健
	private String businessType; //业务类型
	
	/**
	 * 
	  * @Title: getContent
	  * @Description: 获取模板内容
	  * @param templateKey 模板key
	  * @param param 参数
	  * @return    
	  * @return String   
	  *
	 */
	public String getContent(String templateKey, Object... param) {
		return MessageFormat.format(bundle.getString(templateKey), param);
	}
	
	/**
	 * 
	  * @Title: sendMsg
	  * @Description: 短信发送内容
	  * @param mobile 手机号
	  * @param content 发送内容
	  * @return    
	  * @return String   
	  *
	 */
	public abstract String sendMsg(String mobile, String content);
	
	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}

	public String getTemplateKey() {
		return templateKey;
	}

	public void setTemplateKey(String operator, String businessType) {
		this.templateKey = operator + "_" + businessType;
	}
	
	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

}

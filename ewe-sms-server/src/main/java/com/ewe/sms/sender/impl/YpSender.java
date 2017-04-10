/**
 * 
 */
package com.ewe.sms.sender.impl;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.ewe.sms.sender.Sender;
import com.ewe.sms.utils.Operator;
import com.ewe.sms.utils.YpUtils;

/**
 * @author E-mail:liuchou.ewedata.com
 * @date 创建时间：2015年7月21日 下午3:37:28
 * @Description 云片短信发送器
 * @version 1.0
 * @since
 * 
 */
public class YpSender extends Sender {

	public YpSender() {
		setOperator(Operator.YUN_PIAN.getName());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ewe.qb.v1.sms.service.SmsSender#sendMsg(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String sendMsg(String mobile, String content) {
		// 实现短信发送接口
		String resultStr;
		try {
			resultStr = YpUtils.sendSms(content, mobile);
			
			if (log.isDebugEnabled()) 
				log.debug("send yunpian sms request return :{}", resultStr);

			if (StringUtils.isNotBlank(resultStr) && resultStr.contains(":0,")) 
				return SEND_SUCCESS;

		} catch (IOException e) {
			return SEND_FAILED;
		}
		return SEND_FAILED;
	}

}

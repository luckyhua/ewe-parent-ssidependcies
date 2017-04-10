package com.ewe.sms.sender.impl;

import com.ewe.sms.sender.Sender;
import com.ewe.sms.utils.CshxUtils;
import com.ewe.sms.utils.Operator;

public class CshxSender extends Sender{
	
	public CshxSender() {
		setOperator(Operator.CSHX.getName());
	}
	
	@Override
	public String sendMsg(String mobile, String content) {
		//实现短信发送接口
		String resultStr = CshxUtils.SendMessage(mobile, content);
		if(resultStr.contains("操作成功"))
			return SEND_SUCCESS;
		return SEND_FAILED;
	}
	
}

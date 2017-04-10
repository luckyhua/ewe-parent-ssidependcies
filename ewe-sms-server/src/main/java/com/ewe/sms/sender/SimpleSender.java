package com.ewe.sms.sender;

import java.util.Date;

import com.ewe.sms.dao.SmsDao;
import com.ewe.sms.domain.SmsLog;

public class SimpleSender {
	
	private static Sender sender = SenderFactory.createSender();
	
	private static SimpleSender instance = new SimpleSender();
	
	private SimpleSender(){}
	
	public static SimpleSender getInstance(){
		return instance;
	}
	
	/**
	 * 通过模板发送短信
	 * @param type 业务类型
	 * @param mobile 手机号码
	 * @param params 模板参数
	 *
	 */
	public void sendByTemp(String businessType, String mobile, Object... params) {
		sender.setTemplateKey(sender.getOperator(), businessType);
		sendByMes(businessType, mobile, sender.getContent(sender.getTemplateKey(), params));
	}

	/**
	 * 直接发送内容短信
	 * @param type 业务类型
	 * @param mobile 手机号码
	 * @param content 发送内容 
	 *
	 */
	public void sendByMes(String businessType, String mobile, String content) {
		String status = sender.sendMsg(mobile, content);
		insertSmslog(businessType, mobile, content, status);
	}
	
	private void insertSmslog(String businessType, String mobile, String content, String status){
		SmsLog smslog = new SmsLog();
		smslog.setBusinessType(businessType);
		smslog.setContent(content);
		smslog.setMobile(mobile);
		smslog.setOperator(sender.getOperator());
		smslog.setStatus(status); 
		smslog.setCreateTime(new Date());
		smslog.setSendTime(new Date());
		SmsDao.getInstance().insert(smslog);
	}
	
}

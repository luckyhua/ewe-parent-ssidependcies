package com.ewe.sms.sender;

import java.util.ResourceBundle;

import com.ewe.sms.sender.impl.CshxSender;
import com.ewe.sms.sender.impl.YpSender;

public class SenderFactory {
	
	private static ResourceBundle bundle = ResourceBundle.getBundle("config/sms/sms");
	
	private SenderFactory(){}
	
	public static Sender createSender(){
		String onOperator = bundle.getString("on_operator");
		switch (onOperator) {
			case "cshx" :
				return new CshxSender();
			case "yunpian" :
				return new YpSender();
			default:
				return new YpSender();
		}
	}
	
}

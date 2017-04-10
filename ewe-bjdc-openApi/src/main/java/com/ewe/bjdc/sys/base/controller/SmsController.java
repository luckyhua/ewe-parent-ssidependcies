package com.ewe.bjdc.sys.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewe.framework.appsession.SessionDataUtils;
import com.ewe.framework.exception.BaseException;
import com.ewe.framework.exception.utils.ExceptionUtils;
import com.ewe.framework.result.ResultUtils;
import com.ewe.framework.result.json.JsonResult;
import com.ewe.framework.utils.AssertUtils;
import com.ewe.sms.sender.SimpleSender;

@Controller
@RequestMapping("sys/base/sms")
public class SmsController {
	
	public final static String Login = "login";
	public final static String User_Grant = "grant";
	public final static String User_Notice = "notice";
	
	private final static Integer Sms_Valid_Time = 5;

	@RequestMapping("/send")
	@ResponseBody
	public JsonResult send(String businessType, String mobile) throws BaseException{
		
		AssertUtils.notNull(9005, businessType, mobile);
		
		AssertUtils.isMobile(9002, mobile);
		
		//根据手机号获取redis中某时间内发送的次数，判断不能超过多少次
		int vcodeTimes = SessionDataUtils.getVcodeTimes(businessType, mobile);
		if(vcodeTimes > 5){
			ExceptionUtils.throwSimpleEx(9010);	
		}
		
		//把验证码放入到redis中，及发送次数，根据key来区别，如：type：mobile:vcode:count
		String vcode = SessionDataUtils.setVcode(businessType, mobile);
		AssertUtils.notBlank(9007, vcode);
		
		
		//do send message
		SimpleSender.getInstance().sendByTemp(businessType, mobile, vcode, Sms_Valid_Time);
		
		JsonResult resJson = ResultUtils.getSuccessResult();
//		resJson.putData("vcode", vcode);
		return resJson;
	}
	
}

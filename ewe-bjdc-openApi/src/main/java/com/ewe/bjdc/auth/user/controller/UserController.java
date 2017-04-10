package com.ewe.bjdc.auth.user.controller;

import com.ewe.bjdc.auth.user.service.UserService;
import com.ewe.bjdc.domain.auth.user.User;
import com.ewe.bjdc.sys.base.controller.SmsController;
import com.ewe.framework.appsession.AppSession;
import com.ewe.framework.appsession.SessionDataUtils;
import com.ewe.framework.appsession.SessionUtils;
import com.ewe.framework.exception.utils.ExceptionUtils;
import com.ewe.framework.result.ResultUtils;
import com.ewe.framework.result.json.JsonResult;
import com.ewe.framework.utils.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller("userController")
@RequestMapping("/auth/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Resource(name="userService")
	private UserService userService;
	
	@ResponseBody
	@RequestMapping("/login")
	public JsonResult login(String phone, String vcode){
		log.info("ZGH00051: phone={}, vcode={}", phone, vcode);
		AssertUtils.notNull(1000, phone);
		AssertUtils.notNull(9007, vcode);
		AssertUtils.isMobile(9002, phone);
		
		String vcode2 = SessionDataUtils.getVcode(SmsController.Login, phone);
		AssertUtils.isEquals(9008, vcode, vcode2);
		SessionDataUtils.delVcode(SmsController.Login, phone);
		
		User user = userService.findByPhone(phone);
		if(user == null)
			ExceptionUtils.throwSimpleEx(1005);
		
		JsonResult resJson = ResultUtils.getSuccessResult();
		resJson.putData("user", user);

		AppSession session = SessionUtils.createSession(user);
		resJson.putData("token", session.getToken());
		
		return resJson;
		
	}

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.stream().map(String::toUpperCase).forEach(System.out::print);
	}

	@ResponseBody
	@RequestMapping("/regists")
	public JsonResult regists(User user){

		AssertUtils.notNull(1000, user.getPhone());
		AssertUtils.isMobile(9002, user.getPhone());
		AssertUtils.notNull(1002, user.getName());
		AssertUtils.notNull(1003, user.getCid());
		
		user = userService.regists(user);
		if(user == null)
			ExceptionUtils.throwSimpleEx(1005);
		
		JsonResult resJson = ResultUtils.getSuccessResult();
		resJson.putData("user", user);
		
		AppSession session = SessionUtils.createSession(user);
		resJson.putData("token", session.getToken());
		
		return resJson;
	}
	
}

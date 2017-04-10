package com.ewe.bjdc.record.user.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewe.bjdc.record.user.service.UserRecordService;
import com.ewe.framework.appsession.SessionUtils;
import com.ewe.framework.result.ResultUtils;
import com.ewe.framework.result.json.JsonResult;
import com.ewe.framework.utils.AssertUtils;

@Controller("userRecordController")
@RequestMapping("/record/user")
public class UserRecordController {

	@Resource(name="userRecordService")
	private UserRecordService userRecordService;
	
	@RequestMapping("/grant")
	@ResponseBody
	public JsonResult grant(String token, Integer authStatus, Integer agentApplyRecordId){
		
		AssertUtils.notNull(9005, authStatus, "authStatus");
		AssertUtils.notNull(9005, agentApplyRecordId, "agentApplyRecordId");
		
		AssertUtils.notEquals(3001, authStatus, 0);
		
		Integer userId = SessionUtils.getUserId(token);
		
		userRecordService.grant(userId, authStatus, agentApplyRecordId);
		
		JsonResult resJson = ResultUtils.getSuccessResult();
		
		return resJson;
		
	}
	
}

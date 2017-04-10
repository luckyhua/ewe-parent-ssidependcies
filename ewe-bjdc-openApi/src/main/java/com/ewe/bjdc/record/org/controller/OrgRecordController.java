package com.ewe.bjdc.record.org.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewe.bjdc.domain.record.org.OrgApplyRecord;
import com.ewe.bjdc.record.org.service.OrgRecordService;
import com.ewe.framework.appsession.SessionUtils;
import com.ewe.framework.exception.utils.ExceptionUtils;
import com.ewe.framework.result.ResultUtils;
import com.ewe.framework.result.json.JsonResult;
import com.ewe.framework.utils.AssertUtils;

@Controller("orgRecordController")
@RequestMapping("/record/org")
public class OrgRecordController {

	@Resource(name="orgRecordService")
	private OrgRecordService orgRecordService;
	
	@ResponseBody
	@RequestMapping("/apply")
	public JsonResult apply(OrgApplyRecord orgApplyRecord, HttpServletRequest request){
		
		AssertUtils.notNull(1000, orgApplyRecord.getPhone());
		AssertUtils.isMobile(1001, orgApplyRecord.getPhone());
		AssertUtils.notNull(1002, orgApplyRecord.getName());
		AssertUtils.notNull(1003, orgApplyRecord.getCid());
		AssertUtils.notNull(1004, orgApplyRecord.getOrgName());
		AssertUtils.notNull(1005, orgApplyRecord.getCallbackUrl());
		AssertUtils.notNull(1006, orgApplyRecord.getServiceNoList());
		
		Map<String, String[]> params = request.getParameterMap();
		String queryParams = "";  
        for (String key : params.keySet()) {  
            String[] values = params.get(key);  
            for (int i = 0; i < values.length; i++) {
                String value = values[i];  
                queryParams += key + "=" + value + "&";  
            }  
        }
        queryParams = queryParams.substring(0, queryParams.length() - 1);  
        
        orgApplyRecord.setQueryParams(queryParams);
        orgRecordService.apply(orgApplyRecord);
		
		JsonResult resJson = ResultUtils.getSuccessResult();
		
		return resJson;
	}
	
	@ResponseBody
	@RequestMapping("/list")
	public JsonResult list(Integer authStatus, String token){
		
		Integer userId = SessionUtils.getUserId(token);
		List<OrgApplyRecord> orgApplyRecords = orgRecordService.findByAuthStatus(authStatus, userId);
		
		JsonResult resJson = ResultUtils.getSuccessResult();
		resJson.putListData("orgApplyRecords", orgApplyRecords);
		
		return resJson;
	}
	
	@ResponseBody
	@RequestMapping("/details")
	public JsonResult details(String token, Integer id){
		
		AssertUtils.notNull(9005, id, "id");
		
		Integer userId = SessionUtils.getUserId(token);
		OrgApplyRecord orgApplyRecord = orgRecordService.findById(id);
		if(orgApplyRecord == null)
			ExceptionUtils.throwSimpleEx(3000);
		
		AssertUtils.isEquals(3000, orgApplyRecord.getAuthStatus(), 1);
		
		JsonResult resJson = ResultUtils.getSuccessResult();
		resJson.putBeanData("orgApplyRecord", orgApplyRecord);
		
		return resJson;
	}
	
	@RequestMapping("/applys")
	public JsonResult applys(){
		
		return null;
	}
	
}

package com.ewe.bjdc.record.user.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import mapper.mysql.bjdc.record.user.UserGrantRecordMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewe.bjdc.auth.user.service.UserService;
import com.ewe.bjdc.domain.auth.user.User;
import com.ewe.bjdc.domain.record.org.OrgApplyRecord;
import com.ewe.bjdc.domain.record.user.UserGrantRecord;
import com.ewe.bjdc.record.org.service.OrgRecordService;
import com.ewe.bjdc.record.user.service.UserRecordService;
import com.ewe.framework.exception.utils.ExceptionUtils;
import com.ewe.framework.utils.AssertUtils;
import com.ewe.rest.context.SenderFactory;
import com.ewe.rest.req.EwePost;
import com.ewe.rest.resp.EweResponse;
import com.ewe.rest.sender.HttpSender;

@Service("userRecordService")
public class UserRecordServiceImpl implements UserRecordService{
	
	private final static String Complex_Report = "https://www.ewedata.cn/ucf-webapp/api/ew_complex_report_api/";

	@Resource(name="userGrantRecordMapper")
	private UserGrantRecordMapper userGrantRecordMapper;
	
	@Resource(name="orgRecordService")
	private OrgRecordService orgRecordService;
	
	@Resource(name="userService")
	private UserService userService;
	
	@Override
	@Transactional(readOnly=false)
	public void grant(Integer userId, Integer authStatus,
			Integer orgApplyRecordId) {
		
		OrgApplyRecord orgApplyRecord = orgRecordService.findById(orgApplyRecordId);
		if(orgApplyRecord == null)
			ExceptionUtils.throwSimpleEx(3000);
		
		AssertUtils.isEquals(3002, orgApplyRecord.getAuthStatus(), 0);
		
		User user = userService.findByCid(orgApplyRecord.getCid());
		AssertUtils.isEquals(3003, userId, user.getId());
		
		UserGrantRecord userGrantRecord = new UserGrantRecord();
		userGrantRecord.setUserId(userId);
		userGrantRecord.setCid(orgApplyRecord.getCid());
		userGrantRecord.setPhone(orgApplyRecord.getPhone());
		userGrantRecord.setName(orgApplyRecord.getName());
		userGrantRecord.setOrgName(orgApplyRecord.getOrgName());
		userGrantRecord.setAuthStatus(authStatus);
		userGrantRecord.setServiceNoList(orgApplyRecord.getServiceNoList());
		if(authStatus == 1)
			userGrantRecord.setAuthTerm(orgApplyRecord.getServiceNoList());
		userGrantRecord.setAuthTime(new Date());
		userGrantRecord.setCreateTime(new Date());
		userGrantRecord.setStatus(1);
		
		// update orgApplyRecord
		orgApplyRecord.setAuthStatus(authStatus);
		orgApplyRecord.setAuthTime(new Date());
		if(authStatus == 1){
			
			orgApplyRecord.setAuthTerm(orgApplyRecord.getServiceNoList());
			orgApplyRecord.setSuccessTerm(orgApplyRecord.getServiceNoList());
			
			// apply credit interface
			String jsonData = complexReport(orgApplyRecord);
			if(jsonData == null)
				ExceptionUtils.throwSimpleEx(9001);
			orgApplyRecord.setJsonData(jsonData);
			
			// callback Org url
			callbackOrg(orgApplyRecord.getCallbackUrl(), jsonData);
			
		}
		
		userGrantRecordMapper.insertSelective(userGrantRecord);
		orgRecordService.save(orgApplyRecord);
		
	}

	private HttpSender<String> sender = SenderFactory.createSimple(String.class);
	
	private String complexReport(OrgApplyRecord orgApplyRecord){
		
		EwePost<String> applyUrl = sender.createPost(Complex_Report)
				.addParam("loginName", "Internaltest")
				.addParam("password", "a12345678");
		
		if(orgApplyRecord.getQueryParams() != null){
			String[] queryParams = orgApplyRecord.getQueryParams().split("&");
			for(String queryParam : queryParams){
				applyUrl.addParam(queryParam.split("=")[0], queryParam.split("=")[1]);
			}
		}
		
		EweResponse<String> retData = applyUrl.send();
		if(retData != null)
			return retData.getContentData();
		
		return null;
		
	}
	
	private void callbackOrg(String callbackUrl , String jsonData){
		
		EweResponse<String> retData = sender.createPost(callbackUrl)
				.addParam(jsonData.getBytes()).send();
				
	}
	
}

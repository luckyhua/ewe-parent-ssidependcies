package com.ewe.bjdc.record.org.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import mapper.mysql.bjdc.record.org.OrgApplyRecordMapper;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewe.bjdc.auth.user.domain.EweJsonResult;
import com.ewe.bjdc.auth.user.domain.NameCidPhoneCheck;
import com.ewe.bjdc.auth.user.service.UserService;
import com.ewe.bjdc.domain.auth.user.User;
import com.ewe.bjdc.domain.record.org.OrgApplyRecord;
import com.ewe.bjdc.domain.record.org.OrgApplyRecordExample;
import com.ewe.bjdc.record.org.service.OrgRecordService;
import com.ewe.bjdc.record.user.service.UserRecordChecker;
import com.ewe.bjdc.sys.base.controller.SmsController;
import com.ewe.framework.context.Logable;
import com.ewe.framework.exception.utils.ExceptionUtils;
import com.ewe.rest.context.SenderFactory;
import com.ewe.rest.resp.EweResponse;
import com.ewe.rest.sender.HttpSender;
import com.ewe.sms.sender.SimpleSender;

@Service("orgRecordService")
public class OrgRecordServiceImpl implements OrgRecordService,Logable {
	
	private final static String NameCidPhone_Check = "https://www.ewedata.cn/ucf-webapp/api/nameCidPhone_check_api/${name}/${cid}/${phone}/Internaltest/a12345678";
	
	@Resource(name="orgApplyRecordMapper")
	private OrgApplyRecordMapper orgApplyRecordMapper;
	
	@Resource(name="userService")
	private UserService userService;
	
	@Override
	@Transactional(readOnly=false)
	public void apply(OrgApplyRecord orgApplyRecord) {

		//check name、cid、phone
		NameCidPhoneCheck nameCidPhoneCheck = NameCidPhoneCheck(orgApplyRecord.getName(), orgApplyRecord.getCid(), orgApplyRecord.getPhone());
		if(nameCidPhoneCheck == null)
			ExceptionUtils.throwSimpleEx(2000);
		
		//insert agentApplyRecord
		orgApplyRecord.setCreateTime(new Date());
		orgApplyRecord.setStatus(1);
		orgApplyRecordMapper.insertSelective(orgApplyRecord);
		
		//make a user
		User user = new User();
		user.setCid(orgApplyRecord.getCid());
		user.setName(orgApplyRecord.getName());
		user.setPhone(orgApplyRecord.getPhone());
		user.setStatus(1);
		if(!userService.isExist(user)){
			user.setRegisterFrom("2");
			userService.save(user);
		}
		UserRecordChecker.addToCheck(orgApplyRecord);
		//send sms user
		SimpleSender.getInstance().sendByTemp(SmsController.User_Grant, orgApplyRecord.getPhone());
		
	}
	
	private HttpSender<EweJsonResult<NameCidPhoneCheck>> sender = SenderFactory.createSimple(new TypeReference<EweJsonResult<NameCidPhoneCheck>>() {});

	private NameCidPhoneCheck NameCidPhoneCheck(String name, String cid, String phone){
		
		EweResponse<EweJsonResult<NameCidPhoneCheck>> retData = sender.createGet(NameCidPhone_Check)
				.addUrlParam("name", name)
				.addUrlParam("cid", cid)
				.addUrlParam("phone", phone).send();
		
		if(retData != null && retData.getContentData() != null){
			
			NameCidPhoneCheck nameCidPhoneCheck = retData.getContentData().getData();
			if(log.isDebugEnabled())
				log.info(nameCidPhoneCheck.getResult());
			if (nameCidPhoneCheck == null || nameCidPhoneCheck.getResult() == null) {
				return null;
			}
			if("1".equals(nameCidPhoneCheck.getResult()))
				return nameCidPhoneCheck;
			
		}
		
		return null;
		
	}

	@Override
	public List<OrgApplyRecord> findByAuthStatus(Integer authStatus,
			Integer userId) {
		
		if(userId == null)
			return null;
		
		User user = userService.findById(userId);
		if(user == null)
			return null;
		
		OrgApplyRecordExample example = new OrgApplyRecordExample();
		example.createCriteria().andNameEqualTo(user.getName())
		.andCidEqualTo(user.getCid()).andPhoneEqualTo(user.getPhone()).andAuthStatusEqualTo(authStatus);
		
		List<OrgApplyRecord> orgApplyRecords = orgApplyRecordMapper.selectByExample(example);
		
		return orgApplyRecords;
	}

	@Override
	public OrgApplyRecord findById(Integer id) {
		
		if(id == null)
			return null;
		return orgApplyRecordMapper.selectByPrimaryKey(id);
	}

	@Override
	public void save(OrgApplyRecord orgApplyRecord) {
		
		if(orgApplyRecord != null && orgApplyRecord.getId() != null){
			orgApplyRecord.setUpdateTime(new Date());
			orgApplyRecordMapper.updateByPrimaryKeySelective(orgApplyRecord);
		}else{
			orgApplyRecord.setCreateTime(new Date());
			orgApplyRecordMapper.insertSelective(orgApplyRecord);
		}
		
	}

}

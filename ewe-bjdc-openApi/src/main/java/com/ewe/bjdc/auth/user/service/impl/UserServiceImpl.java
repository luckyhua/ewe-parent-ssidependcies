package com.ewe.bjdc.auth.user.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import mapper.mysql.bjdc.auth.user.UserMapper;

import com.ewe.bjdc.auth.user.domain.EweJsonResult;
import com.ewe.bjdc.auth.user.domain.NameCidPhoneCheck;
import com.ewe.bjdc.auth.user.service.UserService;
import com.ewe.bjdc.domain.auth.user.User;
import com.ewe.bjdc.domain.auth.user.UserExample;
import com.ewe.framework.exception.utils.ExceptionUtils;
import com.ewe.rest.context.SenderFactory;
import com.ewe.rest.resp.EweResponse;
import com.ewe.rest.sender.HttpSender;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	private final static String NameCidPhone_Check = "https://www.ewedata.cn/ucf-webapp/api/nameCidPhone_check_api/${name}/${cid}/${phone}/Internaltest/a12345678";
	
	@Resource(name="userMapper")
	private UserMapper userMapper;
	
	@Override
	public User findByPhone(String phone) {
		
		UserExample userExample = new UserExample();
		userExample.createCriteria().andPhoneEqualTo(phone).andStatusEqualTo(1);
		List<User> users = userMapper.selectByExample(userExample);
		if(users.size() == 1)
			return users.get(0);
		return null;
	}

	
	@Override
	public User regists(User user) {
		
		NameCidPhoneCheck nameCidPhoneCheck = NameCidPhoneCheck(user.getName(), user.getCid(), user.getPhone());
		if(nameCidPhoneCheck == null)
			ExceptionUtils.throwSimpleEx(2000);
		if(isExist(user))
			ExceptionUtils.throwSimpleEx(1007);
			
		user.setRegisterFrom("1");
		user.setStatus(1);
		
		this.save(user);
		
		return user;
	}
	
	private HttpSender<EweJsonResult<NameCidPhoneCheck>> sender = SenderFactory.createSimple(new TypeReference<EweJsonResult<NameCidPhoneCheck>>() {});

	private NameCidPhoneCheck NameCidPhoneCheck(String name, String cid, String phone){
		
		EweResponse<EweJsonResult<NameCidPhoneCheck>> retData = sender.createGet(NameCidPhone_Check)
				.addUrlParam("name", name)
				.addUrlParam("cid", cid)
				.addUrlParam("phone", phone).send();
		
		if(retData != null && retData.getContentData() != null){
			
			NameCidPhoneCheck nameCidPhoneCheck = retData.getContentData().getData();
			if("1".equals(nameCidPhoneCheck.getResult()))
				return nameCidPhoneCheck;
			
		}
		
		return null;
		
	}

	@Override
	public User findById(Integer userId) {
		
		if(userId == null)
			return null;
		
		return userMapper.selectByPrimaryKey(userId);
	}
	
	@Override
	public Boolean isExist(User user){
		
		UserExample userExample = new UserExample();
		userExample.createCriteria().andPhoneEqualTo(user.getPhone())
		.andCidEqualTo(user.getCid()).andNameEqualTo(user.getName()).andStatusEqualTo(1);
		List<User> users = userMapper.selectByExample(userExample);
		if(users.size() == 1)
			return true;
		return false;
		
	}

	@Override
	public void save(User user) {
		
		if(user != null && user.getId() != null){
			user.setUpdateTime(new Date());
			userMapper.updateByPrimaryKeySelective(user);
		}else{
			user.setCreateTime(new Date());
			userMapper.insertSelective(user);
		}
	}


	@Override
	public User findByCid(String cid) {
		
		UserExample userExample = new UserExample();
		userExample.createCriteria().andCidEqualTo(cid).andStatusEqualTo(1);
		List<User> users = userMapper.selectByExample(userExample);
		if(users.size() == 1)
			return users.get(0);
		return null;
		
	}

}

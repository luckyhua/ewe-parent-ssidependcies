package com.ewe.bjdc.auth.user.service;

import com.ewe.bjdc.domain.auth.user.User;

public interface UserService {

	public User findByPhone(String phone);
	
	public User findByCid(String cid);

	public User regists(User user);

	public User findById(Integer userId);

	public void save(User user);
	
	public Boolean isExist(User user);

}

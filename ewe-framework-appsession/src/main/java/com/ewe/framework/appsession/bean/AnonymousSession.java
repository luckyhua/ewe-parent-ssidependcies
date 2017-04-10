package com.ewe.framework.appsession.bean;

import com.ewe.framework.appsession.subject.UserSubject;

public class AnonymousSession extends DefaultAppSession{

	public AnonymousSession(String token){
		super(token);
	}
	
	@Override
	public String getToken() {
		return super.getToken();
	}

	@Override
	public boolean isOnline() {
		return false;
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public UserSubject getUserSubject() {
		return UserSubject.Null_User;
	}

	@Override
	public void setUserSubject(UserSubject userSubject) {
	}

	@Override
	public String toString() {
		return "AnonymousSession [getToken()=" + getToken() + ", isOnline()="
				+ isOnline() + ", isActive()=" + isActive() + ", getAllData()="
				+ getAllData() + "]";
	}
}

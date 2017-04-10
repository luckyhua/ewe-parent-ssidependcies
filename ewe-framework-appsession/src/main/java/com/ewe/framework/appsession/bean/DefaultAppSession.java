package com.ewe.framework.appsession.bean;

import java.util.Collections;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

import com.ewe.framework.appsession.AppSession;
import com.ewe.framework.appsession.subject.UserSubject;

/**
 * this class is the default implements of the AppSession<p>
 * if your system may only used the method in AppSession<p>
 * you can used this class<p>
 * @see AppSession
 * @author Lee-Yo
 * 2015年7月8日
 */
public class DefaultAppSession extends Observable implements AppSession{

	private String token;
	
	private boolean isOnline = false;
	
	private boolean isActive = true;
	
	private UserSubject currentUser = UserSubject.Null_User;
	
	private Map<String, Object> data = new ConcurrentHashMap<>();
	
	public DefaultAppSession(String token){
		this.token = token;
	}
	
	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public boolean isOnline() {
		return this.isOnline;
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

	@Override
	public UserSubject getUserSubject() {
		return this.currentUser;
	}

	@Override
	public Object getData(String key) {
		return this.data.get(key);
	}

	@Override
	public void addData(String key, Object data) {
		
		if(key==null||data==null)
			return;
		
		this.setChange(true);
		
		this.data.put(key, data);
		
		this.notifyObservers();
	}

	@Override
	public Map<String, Object> getAllData() {
		return Collections.unmodifiableMap(this.data);
	}

	@Override
	public void setUserSubject(UserSubject userSubject) {
		if(userSubject != null){
			this.setChange(true);
			this.currentUser = userSubject;
			this.data.put("userId", userSubject.getId());
			this.notifyObservers();
		}
	}
	
	@Override
	public void setChange(boolean isChange) {
		if(isChange)
			super.setChanged();
	}

	@Override
	public String toString() {
		return "DefaultAppSession [token=" + token + ", isOnline=" + isOnline
				+ ", isActive=" + isActive + ", currentUser=" + currentUser
				+ ", data=" + data  + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultAppSession other = (DefaultAppSession) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	

}

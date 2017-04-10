package com.ewe.framework.appsession.generator;

import java.util.HashMap;
import java.util.Map;

import com.ewe.framework.appsession.AppSession;
import com.ewe.framework.appsession.context.SessionGenerator;

public abstract class AbstractSessionGenerator implements SessionGenerator{

	@Override
	public AppSession createSession(Map<String, String> sessionMap) {
		
		if(sessionMap==null||sessionMap.isEmpty())
			return null;
		if(sessionMap.get("sessionType")==null||!sessionMap.get("sessionType").equals(this.getSupportType()))
			return null;
		
		return this.doCreateSession(sessionMap);
	}

	protected abstract AppSession doCreateSession(Map<String, String> sessionMap);
	
	protected abstract Map<String, String> doCreateSessionMap(Map<String, String> existsMap,AppSession session);
	
	@Override
	public Map<String, String> createSessionMap(AppSession session) {
		if(session.getClass() != this.getSupportClass()){
			return null;
		}
		Map<String, String> existsMap = new HashMap<String, String>();
		
		for(String key:session.getAllData().keySet()){
			existsMap.put(key, session.getData(key).toString());
		}
		
		Map<String, String> sessionMap = this.doCreateSessionMap(existsMap,session);
		
		sessionMap.put("sessionType", this.getSupportType());
		sessionMap.put("token", session.getToken());
		return sessionMap;
	}

	public abstract String getSupportType();
	
	public abstract Class<?> getSupportClass();
}

package com.ewe.framework.appsession.generator;

import java.util.Map;

import com.ewe.framework.appsession.AppSession;
import com.ewe.framework.appsession.bean.AnonymousSession;

public class AnonymousSessionGenerator extends AbstractSessionGenerator {

	public static final String Support_Type = "anonymouse";
	
	
	@Override
	protected AppSession doCreateSession(Map<String, String> sessionMap) {
		AppSession session = new AnonymousSession(sessionMap.get("token"));
		
		sessionMap.remove("token");
		sessionMap.remove("sessionType");
		
		for(String key:sessionMap.keySet()){
			session.addData(key, sessionMap.get(key));
		}
		
		return session;
	}

	@Override
	protected Map<String, String> doCreateSessionMap(Map<String, String> existsMap,AppSession session) {
		return existsMap;
	}

	@Override
	public String getSupportType() {
		return Support_Type;
	}

	@Override
	public Class<?> getSupportClass() {
		return AnonymousSession.class;
	}

}

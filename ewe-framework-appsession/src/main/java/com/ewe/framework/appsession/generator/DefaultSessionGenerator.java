package com.ewe.framework.appsession.generator;

import java.util.Map;

import com.ewe.common.utils.JsonUtils;
import com.ewe.framework.appsession.AppSession;
import com.ewe.framework.appsession.bean.DefaultAppSession;
import com.ewe.framework.appsession.subject.UserSubject;

public class DefaultSessionGenerator extends AbstractSessionGenerator{
	public static final String Support_Type = "default";

	@Override
	protected AppSession doCreateSession(Map<String, String> sessionMap) {
		
		String token = sessionMap.get("token");
		
		AppSession session = new DefaultAppSession(token);
		
		for(String key : sessionMap.keySet()){
			if(key.equals("currentUser")){
				UserSubject userSubject = JsonUtils.jsonToObject(sessionMap.get(key), UserSubject.class);
				session.setUserSubject(userSubject);
				continue;
			}
			session.addData(key, sessionMap.get(key));
		}
		
		return session;
	}

	@Override
	protected Map<String, String> doCreateSessionMap(Map<String, String> existsMap,AppSession session) {
		
		if(session.getData("userId")!=null){
			existsMap.put("userId", session.getData("userId").toString());
			return existsMap;
		}
		existsMap.put("userId", session.getUserSubject().getId()+"");
		
		return existsMap;
	}

	@Override
	public String getSupportType() {
		return Support_Type;
	}

	@Override
	public Class<?> getSupportClass() {
		return DefaultAppSession.class;
	}

}

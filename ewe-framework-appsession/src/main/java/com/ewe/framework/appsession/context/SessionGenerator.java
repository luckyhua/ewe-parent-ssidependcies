package com.ewe.framework.appsession.context;

import java.util.Map;

import com.ewe.framework.appsession.AppSession;

public interface SessionGenerator {

	public AppSession createSession(Map<String, String> sessionMap);
	
	public Map<String, String> createSessionMap(AppSession session);
}

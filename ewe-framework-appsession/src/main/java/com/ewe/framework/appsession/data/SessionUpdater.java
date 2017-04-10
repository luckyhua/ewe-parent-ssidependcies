package com.ewe.framework.appsession.data;

import com.ewe.framework.appsession.AppSession;

public interface SessionUpdater {

	public void doUpdate(AppSession session);
	
	public void setSessionManager(SessionUpdatable sessionManager);
	
}

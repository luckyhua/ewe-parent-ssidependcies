package com.ewe.framework.appsession.data;

import com.ewe.framework.appsession.AppSession;

public class SessionUpdateTask implements Runnable{

	private AppSession session;
	
	private SessionUpdatable manager;
	
	@Override
	public void run() {
		manager.updateSession(session);
	}

	public static SessionUpdateTask create(AppSession session,SessionUpdatable manager){
		SessionUpdateTask task = new SessionUpdateTask();
		task.session = session;
		task.manager = manager;
		return task;
	}
	
	public AppSession getAppSession(){
		return this.session;
	}
	
	public SessionUpdatable getSessionManager(){
		return this.manager;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((session == null) ? 0 : session.hashCode());
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
		SessionUpdateTask other = (SessionUpdateTask) obj;
		if (session == null) {
			if (other.session != null)
				return false;
		} else if (!session.equals(other.session))
			return false;
		return true;
	}
}

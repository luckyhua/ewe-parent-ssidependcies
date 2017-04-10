package com.ewe.framework.appsession;

import org.apache.commons.lang.math.NumberUtils;

import com.ewe.framework.appsession.context.AppSessionContext;
import com.ewe.framework.appsession.context.SessionManager;
import com.ewe.framework.appsession.subject.UserSubject;

public class SessionUtils {

	private static AppSessionContext context = new AppSessionContext();

	private static ThreadLocal<AppSession> sessions = new ThreadLocal<AppSession>();

	private static boolean isCache = false;

	private static SessionManager manager = context.getSessionManager();

	public static Integer getUserId(String token) {
		AppSession session = getSession(token);
		Object data = session.getData("userId")==null?0:session.getData("userId");
		int id = NumberUtils.toInt(data.toString(),0);
		return id;
	}

	public static AppSession getSession(String token) {
		if (!isCache)
			return manager.getSession(token);

		if (sessions.get() != null)
			return sessions.get();

		AppSession session = manager.getSession(token);
		sessions.set(session);

		return session;
	}

	public static AppSession createSession() {
		AppSession session = manager.createAnonymouseSession();
		if (!isCache)
			return session;

		sessions.set(session);
		return session;
	}

	public static AppSession createSession(UserSubject user) {
		AppSession session = manager.createActiveSession(user);
		if (!isCache)
			return session;

		sessions.set(session);
		return session;
	}

	public static AppSession createSession(String token, UserSubject user) {
		AppSession session = manager.createActiveSession(token, user);
		if (!isCache)
			return session;

		sessions.set(session);
		return session;
	}

	public static void updateSession(AppSession session) {
		manager.updateSessionLazy(session);
		if (!isCache) {
			return;
		}
		sessions.remove();
		sessions.set(session);
	}

	public static AppSession clearSession(String token) {
		AppSession anon = manager.clearSession(token);
		if (!isCache)
			return anon;

		sessions.remove();
		sessions.set(anon);
		return anon;
	}

}
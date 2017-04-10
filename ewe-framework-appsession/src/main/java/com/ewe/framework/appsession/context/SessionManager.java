package com.ewe.framework.appsession.context;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.ewe.common.utils.CodeUtils;
import com.ewe.framework.appsession.AppSession;
import com.ewe.framework.appsession.KeyConfig;
import com.ewe.framework.appsession.SessionFactory;
import com.ewe.framework.appsession.bean.AnonymousSession;
import com.ewe.framework.appsession.bean.DefaultAppSession;
import com.ewe.framework.appsession.data.SessionUpdatable;
import com.ewe.framework.appsession.data.SessionUpdater;
import com.ewe.framework.appsession.subject.UserSubject;
import com.ewe.framework.redis.RedisUtils;

public class SessionManager implements Observer , SessionUpdatable{

	private static final Logger log = LoggerFactory
			.getLogger(SessionManager.class);
	
	private boolean IsLeaveAnonSession = false;

	private SessionUpdater sessionUpdater;

	public SessionManager() {
		
	}
	
	public SessionUpdater getSessionUpdater() {
		return sessionUpdater;
	}

	public void setSessionUpdater(SessionUpdater sessionUpdater) {
		this.sessionUpdater = sessionUpdater;
	}



	public AppSession getSession(String token) {

		if (token == null || token.isEmpty()) {
			if (log.isInfoEnabled())
				log.info("try find session in redis but the token is {},return anonymousSession",
						token);

			return this.getNullOrAnonySession(token);
		}

		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();

			Map<String, String> sessionMap = jedis.hgetAll(KeyConfig
					.getSessionKey(token));

			if (log.isDebugEnabled())
				log.debug("find one map:{} from redis used token:{}",
						sessionMap, token);

			AppSession session = SessionFactory.createSession(sessionMap);

			session.addObserver(this);
			
			return session;

		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.error("get session error!token:" + token, e);

			log.error("find session error!token:{},e:{}", token, e.getMessage());
		}finally{
			RedisUtils.closeJedis(jedis);
		}

		return this.getNullOrAnonySession(token);
	}

	public AppSession createAnonymouseSession() {

		String token = CodeUtils.getToken();

		AppSession session = this.getNullOrAnonySession(token);
		
		if (log.isDebugEnabled()) {
			log.debug("create one session:{}", session);
		}

		this.saveSession(session);

		return session;
	}
	
	public AppSession createActiveSession(UserSubject userSubject){
		return createActiveSession(CodeUtils.getToken(), userSubject);
	}
	
	public AppSession createActiveSession(String anonyMouseToken,UserSubject userSubject){
		if(userSubject==null){
			log.warn("try to create active session but userSubject is null,token:{},subject:{}",anonyMouseToken,userSubject);
			return this.getSession(anonyMouseToken);
		}
		
		AppSession anonySession = this.getSession(anonyMouseToken);
		
		AppSession session = this.copySessionData(anonySession);
		
		session.setUserSubject(userSubject);
		
		AppSession existsSession = this.getUserSession(session);
		this.copySessionDate(existsSession, session);
		this.clearSession(existsSession.getToken());
		
		this.saveSession(session);
		
		return session;
	}
	
	public void updateSessionLazy(AppSession session){
		if(sessionUpdater==null){
			//do default
			return;
		}
		sessionUpdater.doUpdate(session);
	}

	public void updateSession(AppSession session) {
		if (session == null)
			return;
		Jedis jedis = null;
		try {
			if (log.isDebugEnabled()) {
				log.debug("try update session!token:{},session:{}",
						session.getToken(), session);
			}

			jedis = RedisUtils.getJedis();
			
			jedis.del(KeyConfig.getSessionKey(session.getToken()));
			if(!session.getUserSubject().getId().equals(0))
				jedis.del(KeyConfig.getTokenKey(session.getUserSubject().getId()+""));
			
			this.saveSession(session);

			if (log.isInfoEnabled())
				log.info("session update success token:{}", session.getToken());

		} catch (Exception e) {

			if (log.isDebugEnabled())
				log.debug("session update error!", e);

			log.error("session update error,token:{},error:{}",
					session.getToken(), e.getMessage());

		} finally {
			RedisUtils.closeJedis(jedis);
		}
	}

	public AppSession clearSession(String token) {
		if (token == null || token.isEmpty())
			return null;

		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			
			jedis.del(KeyConfig.getSessionKey(token));
			
			if(log.isDebugEnabled())
				log.debug("clear one session token:{}",token);
			
			if(IsLeaveAnonSession){
				AppSession session = this.getNullOrAnonySession(token);
				
				if(log.isDebugEnabled())
					log.debug("save another anonous session used token:{}",token);
				
				this.saveSession(session);
			}
			
		} catch (Exception e) {
			log.error("clear session error!token:{},error:{}",token,e.getMessage());
			
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		
		return this.getNullOrAnonySession(token);
	}

	
	@Override
	public void update(Observable o, Object arg) {
		
		AppSession session = (AppSession) o;
		
		//TODO
		if(session instanceof AnonymousSession)
			return;
		
		if(sessionUpdater==null){
			//do default
			return;
		}
		
		sessionUpdater.doUpdate(session);
	}
	
	private AppSession getNullOrAnonySession(String token){
		AnonymousSession session = new AnonymousSession(token);
		session.addObserver(this);
		return session;
	}
	
	private AppSession copySessionData(AppSession anonySession){
		DefaultAppSession session = new DefaultAppSession(CodeUtils.getToken());
		
		session.addObserver(this);
		
		Map<String, Object> allData = anonySession.getAllData();
		for(String key:allData.keySet()){
			session.addData(key, allData.get(key));
		}
		
		return session;
	}
	
	private void copySessionDate(AppSession copyFrom,AppSession copyTo){
		if(copyFrom==null||copyTo==null)
			return;
		Map<String, Object> allData = copyFrom.getAllData();
		for(String key:allData.keySet()){
			copyTo.addData(key, allData.get(key));
		}
	}
	
	private AppSession getUserSession(AppSession session){
		if(session.getUserSubject().getId()==null||session.getUserSubject().getId()==0)
			return this.getNullOrAnonySession(session.getToken());
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			
			String token = jedis.get(KeyConfig.getTokenKey(session.getUserSubject().getId()+""));
			if(token==null||token.isEmpty())
				return this.getNullOrAnonySession(session.getToken());
			
			AppSession existsSession = this.getSession(token);
			if(log.isDebugEnabled())
				log.debug("find one session by userid:{},session:{}",session.getUserSubject().getId(),existsSession);
			
			if(log.isInfoEnabled())
				log.info("find one session by userId:{},token:{}",session.getUserSubject().getId(),session.getToken());
			
			return existsSession;
		} catch (Exception e) {
			log.error("get session by user id error!userid:{},error:{}",session.getUserSubject().getId(),e.getMessage());
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		return this.getNullOrAnonySession(session.getToken());
	}
	
	private AppSession saveSession(AppSession session){
		//TODO
		if(session instanceof AnonymousSession)
			return session;
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			jedis.hmset(KeyConfig.getSessionKey(session.getToken()),
					SessionFactory.createSessionMap(session));

			if (log.isDebugEnabled())
				log.debug("save session success!token:{},session:{}", session.getToken(),
						session);

			session.setChange(false);
			if(session.getUserSubject().getId().equals(0))
				return session;
			
			//add user id token mapping
			jedis.set(KeyConfig.getTokenKey(session.getUserSubject().getId()+""), session.getToken());
			
			if(log.isInfoEnabled()){
				log.info("save user id and token mapping!userId:{},token:{}",session.getUserSubject().getId(),session.getToken());
			}
			
		} catch (Exception e) {
			log.error("save session:{} to redis error:{}", session,
					e.getMessage());

			if (log.isDebugEnabled()) {
				log.debug("save session error! session:" + session, e);
			}

		} finally {
			RedisUtils.closeJedis(jedis);
		}
		
		return session;
	}

}

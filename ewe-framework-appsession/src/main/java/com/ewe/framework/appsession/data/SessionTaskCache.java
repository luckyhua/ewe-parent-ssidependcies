package com.ewe.framework.appsession.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionTaskCache {

	private static final Logger log = LoggerFactory.getLogger(SessionTaskCache.class);
	
	private static SessionTaskCache instance;
	
	private int cacheSize;
	
	private Set<SessionUpdateTask> sessions;
	
	private SessionTaskCache(){
		
	}
	
	private SessionTaskCache(int cacheSize){
		if(log.isInfoEnabled())
			log.info("session task cache init use size:{}",cacheSize);
		
		this.cacheSize = cacheSize;
		this.sessions = Collections.synchronizedSet(new HashSet<SessionUpdateTask>(cacheSize*2,0.5f));
		
	}
	
	
	public static SessionTaskCache create(int cacheSize){
		
		if(instance!=null)
			return instance;
		
		synchronized (SessionTaskCache.class) {
			if(instance!=null)
				return instance;
			instance = new SessionTaskCache(cacheSize);
		}
		
		return instance;
	}
	
	public void addSession(SessionUpdateTask session){
		if(session == null)
			return;
		
		if(this.sessions.size()>this.cacheSize){
			log.warn("session cache is larger than size!cache size:{},now size:{}",this.cacheSize,this.sessions.size());
		}
		
		if(log.isInfoEnabled())
			log.info("add one session to cache,token:{},now size:{}",session.getAppSession().getToken(),this.sessions.size());
		
		
		this.sessions.add(session);
	}
	
	public Iterator<SessionUpdateTask> iterator(){
		return this.sessions.iterator();
	}
	
	public int getSize(){
		return this.sessions.size();
	}
}

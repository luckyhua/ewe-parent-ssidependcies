package com.ewe.framework.appsession.data.syn;

import java.util.Iterator;

import com.ewe.framework.appsession.data.SessionTaskCache;
import com.ewe.framework.appsession.data.SessionUpdateDespatcher;
import com.ewe.framework.appsession.data.SessionUpdateTask;
import com.ewe.framework.appsession.data.UpdateStrategy;

public class LazyUpdateStrategy implements UpdateStrategy{

	private final String Code = "LazyUpdateStrategy";
	
	private SessionTaskCache sessionCache;
	
	private SessionUpdateDespatcher despatcher = SessionUpdateDespatcher.create();
	
	private int cacheSize;
	
	public LazyUpdateStrategy(int cacheSize){
		this.cacheSize = cacheSize;
		this.sessionCache = SessionTaskCache.create(cacheSize);
		new Thread(new SessionCachePoller()).start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(sessionCache.getSize() == 0)
					return;
				Iterator<SessionUpdateTask> sessions = sessionCache.iterator();
				
				if(log.isInfoEnabled())
					log.info("try to clear all cache!");
				
				while (sessions.hasNext()) {
					SessionUpdateTask task = sessions.next();
					despatcher.addUpdateTask(task);
					sessions.remove();
				}
			}
		}));
	}
	
	@Override
	public void onUpdateSession(SessionUpdateTask task) {
		if(log.isInfoEnabled())
			log.info("add session to cache used to update!token:{}",task.getAppSession().getToken());
		
		if(log.isDebugEnabled())
			log.debug("add session to cache used to update!session:{}",task.getAppSession());
		
		this.sessionCache.addSession(task);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Code == null) ? 0 : Code.hashCode());
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
		LazyUpdateStrategy other = (LazyUpdateStrategy) obj;
		if (Code == null) {
			if (other.Code != null)
				return false;
		} else if (!Code.equals(other.Code))
			return false;
		return true;
	}




	private class SessionCachePoller implements Runnable {

		private boolean isShutdown = false;

		private long pollerPeriods = 2000;

		@Override
		public void run() {

			if (log.isInfoEnabled())
				log.info("session cache poller run!");

			int count = 0;
			
			while (this.isShutdown) {
				count++;
				
				try {
					Thread.sleep(pollerPeriods);
				} catch (InterruptedException e) {
				}

				if (LazyUpdateStrategy.this.sessionCache.getSize() < LazyUpdateStrategy.this.cacheSize) {
					
					if(LazyUpdateStrategy.log.isDebugEnabled() && (count%10 == 0))
						LazyUpdateStrategy.log.debug("session cached size:{},not yet synchronized!",LazyUpdateStrategy.this.sessionCache.getSize());
					
					continue;
				}
				
				Iterator<SessionUpdateTask> sessions = LazyUpdateStrategy.this.sessionCache.iterator();
						
				
				while (sessions.hasNext()) {
					LazyUpdateStrategy.this.despatcher.addUpdateTask(sessions.next());
					sessions.remove();
				}

			}

		}

	}

}

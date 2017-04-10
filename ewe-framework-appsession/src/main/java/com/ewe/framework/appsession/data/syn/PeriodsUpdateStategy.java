package com.ewe.framework.appsession.data.syn;

import java.util.Iterator;

import com.ewe.framework.appsession.data.SessionTaskCache;
import com.ewe.framework.appsession.data.SessionUpdateDespatcher;
import com.ewe.framework.appsession.data.SessionUpdateTask;
import com.ewe.framework.appsession.data.UpdateStrategy;

public class PeriodsUpdateStategy implements UpdateStrategy{
	
	private final String Code = "PeriodsUpdateStategy";
	
	public static final long Default_Periods = 1000*2;
	
	private SessionTaskCache sessionCache;
	
	private SessionUpdateDespatcher despatcher = SessionUpdateDespatcher.create();
	
	private static boolean needLog = false;
	
	private long pollPeriods = Default_Periods;
	
	public PeriodsUpdateStategy(long pollPeriods,int cacheSize){
		if(pollPeriods>100)
			this.pollPeriods = pollPeriods;
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
			log.info("add one task to cache!token:{}",task.getAppSession().getToken());
		
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
		PeriodsUpdateStategy other = (PeriodsUpdateStategy) obj;
		if (Code == null) {
			if (other.Code != null)
				return false;
		} else if (!Code.equals(other.Code))
			return false;
		return true;
	}



	private class SessionCachePoller implements Runnable {

		private boolean isShutdown = false;

		private long pollerPeriods = PeriodsUpdateStategy.this.pollPeriods;

		@Override
		public void run() {
			
			int count = 0;
			
			while (!isShutdown) {
				
				try {
					Thread.sleep(pollerPeriods);
				} catch (Exception e) {
				}
				
				if(PeriodsUpdateStategy.this.sessionCache.getSize() == 0){
					if(!needLog)
						continue;
					
					if(log.isDebugEnabled())
						log.debug("periods updater run! no sessions need to update!");
					
					if(log.isInfoEnabled()&& (count%10 == 0)){
						log.info("periods updater run! no sessions need to update!");
					}
					
					continue;
				}
				
				Iterator<SessionUpdateTask> sessions = PeriodsUpdateStategy.this.sessionCache.iterator();
				int size = PeriodsUpdateStategy.this.sessionCache.getSize();
				while (sessions.hasNext()) {
					SessionUpdateTask next = sessions.next();
					PeriodsUpdateStategy.this.despatcher.addUpdateTask(next);
					sessions.remove();
					
					if(log.isDebugEnabled())
						log.debug("synchronized one session in periods,session:{}",next);
					
				}
				
				if(log.isInfoEnabled()){
					log.info("session synchronized completed!update sessions:{}",size);
				}
				
			}
			
		}

	}
}

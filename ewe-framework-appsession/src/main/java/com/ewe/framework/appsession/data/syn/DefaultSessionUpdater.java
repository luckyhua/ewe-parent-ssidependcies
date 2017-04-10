package com.ewe.framework.appsession.data.syn;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewe.framework.appsession.AppSession;
import com.ewe.framework.appsession.config.ManagerConfig;
import com.ewe.framework.appsession.context.SessionManager;
import com.ewe.framework.appsession.data.SessionUpdatable;
import com.ewe.framework.appsession.data.SessionUpdateTask;
import com.ewe.framework.appsession.data.SessionUpdater;
import com.ewe.framework.appsession.data.UpdateStrategy;

public class DefaultSessionUpdater implements SessionUpdater{

	private SessionUpdatable manager;
	
	private Set<UpdateStrategy> strategies;
	
	private static final Logger log = LoggerFactory.getLogger(DefaultSessionUpdater.class);
	
	private DefaultSessionUpdater(){
		
	}
	
	public static SessionUpdater create(SessionManager manager,ManagerConfig managerConfig){
		DefaultSessionUpdater updater = new DefaultSessionUpdater();
		updater.strategies = new HashSet<UpdateStrategy>(5,0.5f);
		//check mutual
		updater.manager = manager;
		//build all strategy
		//check manual first
		boolean manulUpdate = managerConfig.isManulUpdate();
		if(manulUpdate){
			if(log.isInfoEnabled())
				log.info("update strategy set to manual!remove other all strategy!");
			if(managerConfig.isAsynchronousUpdate()){
				if(log.isInfoEnabled())
					log.info("update strategy set to asynchronouse!");
				updater.strategies.add(new AsynchronousUpdateStrategy());
			}else {
				if(log.isInfoEnabled())
					log.info("update strategy set to manual!");
				updater.strategies.add(new ManualUpdateStrategy());
			}
			return updater;
		}
		
		boolean lazyUpdate = managerConfig.isLazyUpdate();
		if(lazyUpdate){
			//set lazy update strategy
			if(log.isInfoEnabled())
				log.info("add update strategy!use cacheSize:{}",managerConfig.getCacheSize());
			updater.strategies.add(new LazyUpdateStrategy(managerConfig.getCacheSize()));
		}
		
		boolean periodsUpdate = managerConfig.isPeriodsUpdate();
		if(periodsUpdate){
			if(log.isInfoEnabled())
				log.info("add update strategy periods update!use periods:{}",managerConfig.getUpdatePeriods());
			updater.strategies.add(new PeriodsUpdateStategy(managerConfig.getUpdatePeriods(), managerConfig.getCacheSize()));
				
		}
		
		return updater;
	}
	
	public static SessionUpdater create(SessionManager manager){
		if(manager == null)
			throw new RuntimeException();
		DefaultSessionUpdater updater = new DefaultSessionUpdater();
		updater.setSessionManager(manager);
		return updater;
	}
	
	@Override
	public void doUpdate(AppSession session) {
		if(session==null)
			return;
		
		if(log.isInfoEnabled())
			log.info("try to update session,token:{}",session.getToken());
		
		for(UpdateStrategy strategy : strategies){
			
			if(log.isDebugEnabled())
				log.debug("do update strategy,strategy name:{}",strategy.getClass().getSimpleName());
			
			strategy.onUpdateSession(SessionUpdateTask.create(session, manager));
		}
	}

	@Override
	public void setSessionManager(SessionUpdatable sessionManager) {
		this.manager = sessionManager;
	}

}

package com.ewe.framework.appsession.context;

import java.util.ArrayList;
import java.util.List;

import com.ewe.framework.appsession.SessionFactory;
import com.ewe.framework.appsession.config.ManagerConfig;
import com.ewe.framework.appsession.config.loader.ConfigLoader;
import com.ewe.framework.appsession.config.loader.PropertiesConfigLoader;
import com.ewe.framework.appsession.data.SessionUpdater;
import com.ewe.framework.appsession.data.syn.DefaultSessionUpdater;



public class AppSessionContext {

	private List<String> generatorClasses = new ArrayList<>();
	
	private ConfigLoader configLoader = new PropertiesConfigLoader();
	
	private String propertiesName;
	
	private SessionManager manager;
	
	public AppSessionContext(String propertiesName){
		this.propertiesName = propertiesName;
		this.init();
	}
	
	public SessionManager getSessionManager(){
		return this.manager;
	}
	
	public AppSessionContext(){
		this.init();
	}
	
	@Deprecated
	public static AppSessionContext build(String...generatorClasses){
		AppSessionContext ctx = new AppSessionContext();
		for (String generatorClass : generatorClasses) {
			ctx.generatorClasses.add(generatorClass);
			try {
				SessionGenerator generator = (SessionGenerator) Class.forName(generatorClass).newInstance();
				SessionFactory.registsGenarator(generator);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return ctx;
	}
	
	private void init(){
		//genger config
		ManagerConfig config = configLoader.loadConfig(this.propertiesName);
		//build generator
		SessionFactory.buildGenerators(config);
		//create manager
		SessionManager manager = new SessionManager();
		//create updater
		SessionUpdater sessionUpdater = DefaultSessionUpdater.create(manager,config);
		//add relation
		manager.setSessionUpdater(sessionUpdater);
		
		this.manager = manager;
	}
}

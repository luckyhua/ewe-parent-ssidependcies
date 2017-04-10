package com.ewe.framework.appsession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.ewe.framework.appsession.bean.AnonymousSession;
import com.ewe.framework.appsession.config.ManagerConfig;
import com.ewe.framework.appsession.context.SessionGenerator;

public class SessionFactory {

	private static List<SessionGenerator> generators = new ArrayList<>();
	
	private static final Logger log = LoggerFactory.getLogger(SessionFactory.class);
	
	public static void registsGenarator(SessionGenerator generator){
		Jedis jedis = null;
		jedis.isConnected();
		if(generator!=null)
			generators.add(generator);
	}
	
	public static void buildGenerators(ManagerConfig config){
		List<String> generatorClasses = config.getGeneratorClasses();
		
		for (String clz : generatorClasses) {
			try {
				SessionGenerator generator = (SessionGenerator) Class.forName(clz).newInstance();
				if(generator!=null){
					generators.add(generator);
					
					if(log.isInfoEnabled())
						log.info("build one generator!name:{}",generator.getClass().getSimpleName());
				}
				
			} catch (Exception e) {
				log.error("could not init gengerator class:{},error:{}",clz,e.getMessage());
				if(log.isDebugEnabled())
					log.error("could not int generator class!",e);
			}
		}
	}
	
	public static AppSession createSession(Map<String, String> sessionMap){
		for (SessionGenerator generator : generators) {
			AppSession session = generator.createSession(sessionMap);
			if(session!=null)
				return session;
		}
		return new AnonymousSession("");
	}
	
	public static Map<String, String> createSessionMap(AppSession session){
		for (SessionGenerator generator : generators) {
			Map<String, String> sessionMap = generator.createSessionMap(session);
			if(sessionMap!=null)
				return sessionMap;
		}
		return new HashMap<String, String>();
	}
}

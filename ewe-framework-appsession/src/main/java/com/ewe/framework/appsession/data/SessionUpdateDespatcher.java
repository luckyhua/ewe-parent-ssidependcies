package com.ewe.framework.appsession.data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionUpdateDespatcher {

	private ExecutorService Update_Despathcer = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*4);
	
	private SessionUpdateDespatcher(){
		
	}
	
	public void addUpdateTask(SessionUpdateTask task){
		
		if(task == null)
			return;
		Update_Despathcer.execute(task);
	}
	
	public void destroy(){
		Update_Despathcer.shutdown();
	}
	
	public static SessionUpdateDespatcher create(){
		return Instance.instance;
	}
	
	private static class Instance{
		public static SessionUpdateDespatcher instance = new SessionUpdateDespatcher();
	}
}

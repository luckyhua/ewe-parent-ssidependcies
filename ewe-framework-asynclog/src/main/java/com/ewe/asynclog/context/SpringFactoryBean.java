package com.ewe.asynclog.context;

import com.ewe.asynclog.log.AsyncLog;
import com.ewe.asynclog.log.SimpleLog;
import com.ewe.exec.context.ExecutException;
import com.ewe.exec.executor.DataExecutor;

public class SpringFactoryBean {

	public static <T> AsyncLog<T> createLog(DataExecutor<T> executor){
		if(executor==null)
			ExecutException.createAndThrow("could not build log executor is null!");
		AsyncLog<T> log = SimpleLog.create(executor, "test");
		
		return log;
	}
}

package com.ewe.framework.appsession.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface UpdateStrategy {

	public static final Logger log = LoggerFactory.getLogger(UpdateStrategy.class);
	
	public void onUpdateSession(SessionUpdateTask task);
}

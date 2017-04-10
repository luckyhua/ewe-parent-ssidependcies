package com.ewe.framework.appsession.config.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewe.framework.appsession.config.ManagerConfig;

public interface ConfigLoader {

	public static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);
	
	public ManagerConfig loadConfig(String file);
}

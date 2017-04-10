package com.ewe.framework.appsession.config.loader;

import java.util.Arrays;
import java.util.ResourceBundle;

import org.apache.commons.lang.math.NumberUtils;

import com.ewe.framework.appsession.config.ManagerConfig;

public class PropertiesConfigLoader implements ConfigLoader {

	public static final String Default_Props_Name = "session";
	
	@Override
	public ManagerConfig loadConfig(String file) {
		
		if(file==null)
			log.warn("file name is null! used default file name:{}",Default_Props_Name);
		
		ManagerConfig managerConfig = new ManagerConfig();
		
		String propsName = file==null?Default_Props_Name:file;
		
		try {
			ResourceBundle config = ResourceBundle.getBundle(propsName);
			
			String manualUpdate = config.getString("manager.manualUpdate");
			Boolean isManualUpdate = Boolean.valueOf(manualUpdate);
			if(log.isInfoEnabled())
				log.info("init config!config manulaUpdate:{},used isManualUpdate:{}",manualUpdate,isManualUpdate);
			if(isManualUpdate){
				managerConfig.setManulUpdate(isManualUpdate);
				return managerConfig;
			}
			
			String lazyUpdate = config.getString("manager.lazyUpdate");
			Boolean isLazyUpdate = Boolean.valueOf(lazyUpdate);
			if(log.isInfoEnabled()){
				log.info("init config!config lazyaUpdate:{},used isLazyUpdate:{}",lazyUpdate,isLazyUpdate);
			}
			managerConfig.setLazyUpdate(isLazyUpdate);
			
			String cacheSize = config.getString("manager.cacheSize");
			Integer int_cacheSize = NumberUtils.toInt(cacheSize, ManagerConfig.Default_CacheSize);
			if(log.isInfoEnabled()){
				log.info("init config!config cache size:{},used cacheSize:{}",cacheSize,int_cacheSize);
			}
			managerConfig.setCacheSize(int_cacheSize);
			
			String periodsUpdate = config.getString("manager.periodsUpdate");
			Boolean isPeriodsUpdate = Boolean.valueOf(periodsUpdate);
			if(log.isInfoEnabled()){
				log.info("init config!config periodsUpdate:{},used periodsUpdate:{}",periodsUpdate,isPeriodsUpdate);
			}				
			managerConfig.setPeriodsUpdate(isPeriodsUpdate);
			
			String updatePeriods = config.getString("manager.updatePeriods");
			Long lon_updatePeriods = NumberUtils.toLong(updatePeriods, ManagerConfig.Default_UpdatePeriods);
			if(log.isInfoEnabled()){
				log.info("init config!config updatePeriods:{},used lon_updatePeriods:{}",updatePeriods,lon_updatePeriods);
			}			
			managerConfig.setUpdatePeriods(lon_updatePeriods);
			
			String[] generators = config.getString("session.generators").split(",");
			if(log.isInfoEnabled()){
				log.info("init config!config generators:{},used generators:{}",generators,ManagerConfig.Default_GeneratorClasses);
			}		
			if(generators!=null && generators.length!=0)
				managerConfig.setGeneratorClasses(Arrays.asList(generators));
			
		} catch (Exception e) {
			if(log.isDebugEnabled())
				log.error("load config error!",e);;
			
			log.error("load config error!file:{},error:{}",file,e.getMessage());
			
		}
		
		return managerConfig;
	}

}

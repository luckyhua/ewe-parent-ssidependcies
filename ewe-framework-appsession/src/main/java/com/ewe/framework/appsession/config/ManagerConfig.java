package com.ewe.framework.appsession.config;

import java.util.Arrays;
import java.util.List;

public class ManagerConfig {

	public static final boolean Default_ManulUpdate = false;

	public static final boolean Default_LazyUpdate = false;

	public static final int Default_CacheSize = 20;

	public static final boolean Default_PeriodsUpdate = true;

	public static final long Default_UpdatePeriods = 1000 * 2;
	
	public static final boolean Default_AsynchronousUpdate = true;

	public static final String[] Default_GeneratorClasses = {
			"com.ewe.framework.appsession.generator.DefaultSessionGenerator",
			"com.ewe.framework.appsession.generator.AnonymousSessionGenerator" };

	private boolean manulUpdate = Default_ManulUpdate;

	private boolean lazyUpdate = Default_LazyUpdate;
	
	private boolean asynchronousUpdate = true;

	private Integer cacheSize = Default_CacheSize;

	private boolean periodsUpdate = Default_PeriodsUpdate;

	private Long updatePeriods = Default_UpdatePeriods;

	private List<String> generatorClasses = Arrays.asList(Default_GeneratorClasses);
	
	public boolean isManulUpdate() {
		return manulUpdate;
	}

	public void setManulUpdate(boolean manulUpdate) {
		this.manulUpdate = manulUpdate;
	}

	public boolean isLazyUpdate() {
		return lazyUpdate;
	}

	public void setLazyUpdate(boolean lazyUpdate) {
		this.lazyUpdate = lazyUpdate;
	}

	public Integer getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(Integer cacheSize) {
		this.cacheSize = cacheSize;
	}

	public boolean isPeriodsUpdate() {
		return periodsUpdate;
	}

	public void setPeriodsUpdate(boolean periodsUpdate) {
		this.periodsUpdate = periodsUpdate;
	}

	public Long getUpdatePeriods() {
		return updatePeriods;
	}

	public void setUpdatePeriods(Long updatePeriods) {
		this.updatePeriods = updatePeriods;
	}

	public List<String> getGeneratorClasses() {
		return generatorClasses;
	}

	public void setGeneratorClasses(List<String> generatorClasses) {
		this.generatorClasses = generatorClasses;
	}

	public boolean isAsynchronousUpdate() {
		return asynchronousUpdate;
	}

	public void setAsynchronousUpdate(boolean asynchronousUpdate) {
		this.asynchronousUpdate = asynchronousUpdate;
	}
}

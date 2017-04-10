package com.ewe.framework.context;

import java.util.List;

import com.ewe.framework.context.model.PageInfo;

public interface BaseQueryService <T, Q>{

	public List<T> findByDetails(PageInfo pageInfo, Q queryVo);
	
}

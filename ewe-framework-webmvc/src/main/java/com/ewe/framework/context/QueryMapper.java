package com.ewe.framework.context;

import java.util.List;

import com.ewe.framework.data.QueryDetails;

public  interface QueryMapper<T,Q> {

	public List<T> selectByDetails(QueryDetails<Q> details);
	
	public Integer countByDetails(QueryDetails<Q> details); 
	
}

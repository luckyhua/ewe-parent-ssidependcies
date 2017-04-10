package com.ewe.framework.context;

import java.util.ArrayList;
import java.util.List;

import com.ewe.framework.context.model.PageInfo;
import com.ewe.framework.data.QueryDetails;

public abstract class AbstractQueryService<T, Q> implements BaseQueryService<T,Q>, Logable{

	public List<T> findByDetails(PageInfo pageInfo, Q queryVo) {
		QueryDetails<Q> details = QueryDetails.create(queryVo);

		Integer count = getMapper().countByDetails(details);
		if (log.isInfoEnabled())
			log.info("find by: {} size:{}",queryVo.getClass().getSimpleName(), count + "");
		
		if(count==0)
			return new ArrayList<>();

		pageInfo.setPageParams(count);
		details.setPageInfo(pageInfo);

		List<T> data = getMapper().selectByDetails(details);

		return data;

	}

	@SuppressWarnings("rawtypes")
	public abstract QueryMapper getMapper();
}

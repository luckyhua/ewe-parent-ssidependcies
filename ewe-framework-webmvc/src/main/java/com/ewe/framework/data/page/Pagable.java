package com.ewe.framework.data.page;

import com.ewe.framework.context.model.PageInfo;

public interface Pagable {

	public PageInfo getPageInfo();
	
	public void setPageInfo(PageInfo pageInfo);
}

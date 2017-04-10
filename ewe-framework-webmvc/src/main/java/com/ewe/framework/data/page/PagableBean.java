package com.ewe.framework.data.page;

import com.ewe.framework.context.model.PageInfo;

public abstract class PagableBean implements Pagable{

	protected PageInfo pageInfo = new PageInfo();

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		if(pageInfo==null)
			return;
		
		this.pageInfo = pageInfo;
	}
	
	
}

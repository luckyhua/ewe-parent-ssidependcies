package com.ewe.framework.context.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class SortInfo {
	
	private final static String ORDERBY_STR = "order by id desc"; 
	private final static String ORDERBY_PRI = "order by ";
	
	private String orderBy;
	
	public String getOrderBy() {
		return getOrderByColumn();
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	private List<String> orderByColumnList = new ArrayList<>();
	
	public void addOrderByColumn(String column, String sort){
		this.orderByColumnList.add(column + " " + sort);
	}
	
	public String getOrderByColumn(){
		if(orderByColumnList == null || orderByColumnList.size() == 0)
			return ORDERBY_STR;
		String orderByStr = "";
		for (String orderByColumn : orderByColumnList) {
			orderByStr += orderByColumn + ",";
		}
		return ORDERBY_PRI + orderByStr.substring(0, orderByStr.length()-1);
	}
	
}

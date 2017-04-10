package com.ewe.common.utils;

import java.util.List;

public class ValidateUtils {

	public static boolean isEmptyRedisArray(List<String> list){
		if(list==null||list.isEmpty()||list.get(0)==null)
			return true;
		return false;
	}
	
	public static boolean notEmptyRedisArray(List<String> list){
		return !isEmptyRedisArray(list);
	}
}

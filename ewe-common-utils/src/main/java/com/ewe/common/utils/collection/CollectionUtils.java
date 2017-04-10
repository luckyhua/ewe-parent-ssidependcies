package com.ewe.common.utils.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CollectionUtils {

	public static <K,V> Map<K,V> newHashMap(){
		return new HashMap<K,V>();
	}
	public static <T> List<T> newArrayList(){
		return new ArrayList<T>();
	}

	public interface Filter<T>{
		boolean filter(T t);
	}
	
	public static <T> T getOneFromCollection(Collection<T> collection,Filter<T> filter){
		if(null != collection){
			for(T t : collection){
				if(filter.filter(t)){
					return t;
				}
			}
		}
		return null;
	}
}

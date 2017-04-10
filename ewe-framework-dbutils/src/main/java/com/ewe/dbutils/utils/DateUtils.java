package com.ewe.dbutils.utils;

import java.util.Calendar;

public class DateUtils {

	public static int getDaysInYear(){
		Calendar cd = Calendar.getInstance();   
		int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);//获得当天是一年中的第几天   
		return yearOfNumber;
	}
	
}

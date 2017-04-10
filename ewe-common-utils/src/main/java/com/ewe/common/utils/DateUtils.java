package com.ewe.common.utils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	private static SimpleDateFormat default_sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * return now date string pattern : yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getNowTimeString() {
		return default_sdf.format(new Date());
	}
	
	public static Integer getNowYear(){
		Calendar c = Calendar.getInstance();
		c.get(Calendar.YEAR);
		return c.get(Calendar.YEAR);
	}
	
	public static Integer getDaysInYear(){
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DAY_OF_YEAR);
	}
	
	// @Test
	// public void testTime(){
	// int secondes = (int) (this.getRemainTime()/1000);
	// System.out.println(13*60*60);
	// System.out.println(secondes);
	// }
	/**
	 * 获得到当天晚上12所剩余的毫秒数
	 * 
	 * @return 时间的毫秒值
	 */
	public static long getRemainTime() {
		Calendar calendar = Calendar.getInstance();

		/*** 定制每日00:00时间 ***/

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Date date = calendar.getTime(); // 第一次执行定时任务的时间

		// 如果第一次执行定时任务的时间 小于 当前的时间
		// 此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
		if (date.before(new Date())) {
			date = addDay(date, 1);
		}

		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		// System.out.println("12 times:"+sdf.format(date));
		// System.out.println("now times:"+sdf.format(new Date()));

		return date.getTime() - new Date().getTime();
	}

	/**
	 * 给日期增加天数
	 * 
	 * @param date
	 *            日期 new Date()代表当期日期
	 * @param num
	 *            需要增加的天数
	 * @return 增加后的日期
	 */
	public static Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}

	public static Date getMouthDate(int nextMouths, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_MONTH, days);
		calendar.add(Calendar.MONTH, nextMouths);

		return calendar.getTime();
	}
	
	public static String nowString() {
		return getDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 指定时间格式转字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateString(Date date, String format) {
		Date tmpDate = null == date ? new Date() : date;

		String tmpFormat = "yyyy-MM-dd HH:mm:ss";
		if (null != format || "".equals(format)) {
			tmpFormat = format;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(tmpFormat);
		return sdf.format(tmpDate);
	}

	/**
	 * 
	 * @param text
	 * @param format
	 * @return
	 */
	public static Date parsedate(String text, String format) {
		try {
			return new SimpleDateFormat(format).parse(text);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static DatePeriods getYestodayPeriods() {
		Calendar calendar = Calendar.getInstance();

		/*** 定制每日00:00时间 ***/

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Date date = calendar.getTime();

		// 此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
		// if (date.before(new Date())) {
		// date = addDay(date, -1);
		// }

		Date yestoday = addDay(date, -1);

		long begin = yestoday.getTime();
		long end = yestoday.getTime() + 60 * 60 * 24 * 1000;

		return new DatePeriods(begin, end);
	}

	/*
	 * @Test public void testDatePeriods(){ DatePeriods y1 =
	 * getYestodayPeriods(); DatePeriods y2 = getYestodayPeriods();
	 * TestCase.assertEquals(y1, y2); System.out.println(y1);
	 * System.out.println(y2); System.out.println(System.currentTimeMillis()); }
	 * 
	 * @Test public void testNewDate(){ DatePeriods y1 = getDayPeriods(-1);
	 * DatePeriods y2 = getYestodayPeriods(); TestCase.assertEquals(y1, y2); }
	 */

	public static class DatePeriods implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private long beginTime;
		private long endTime;

		public DatePeriods(long beginTime, long endTime) {
			super();
			this.beginTime = beginTime;
			this.endTime = endTime;
		}

		public long getBeginTime() {
			return beginTime;
		}

		public void setBeginTime(long beginTime) {
			this.beginTime = beginTime;
		}

		public long getEndTime() {
			return endTime;
		}

		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (beginTime ^ (beginTime >>> 32));
			result = prime * result + (int) (endTime ^ (endTime >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DatePeriods other = (DatePeriods) obj;
			if (beginTime != other.beginTime)
				return false;
			if (endTime != other.endTime)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "DatePeriods:yestoday [beginTime=" + beginTime
					+ ", endTime=" + endTime + "]";
		}

	}

	public static int getDaysBetweenDates(Date d1,Date d2){
		long t1 = d1.getTime();
		long t2 = d2.getTime();
		long between_days= Math.abs((t1-t2)/(1000*3600*24));
		
		return Integer.parseInt(String.valueOf(between_days)); 
	}
	
	public static DatePeriods getDayPeriods(int day) {
		Calendar calendar = Calendar.getInstance();

		/*** 定制每日00:00时间 ***/

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Date date = calendar.getTime();

		// 此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
		// if (date.before(new Date())) {
		// date = addDay(date, -1);
		// }

		Date newDay = addDay(date, day);

		long begin = newDay.getTime();
		long end = newDay.getTime() + 60 * 60 * 24 * 1000;

		return new DatePeriods(begin, end);
	}
}

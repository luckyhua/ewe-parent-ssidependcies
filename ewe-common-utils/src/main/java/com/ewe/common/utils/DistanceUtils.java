package com.ewe.common.utils;

/**
 * 
 * @author Yo-Lee
 * 2015年3月9日上午10:38:27
 */
public class DistanceUtils {
	private static final double EARTH_RADIUS = 6378137;

	private static final String Dis_KM = "km";
	private static final String Dis_M = "m";
	private static final String Dis_Non = "未知";
	
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
	 * 
	 * @param lng1	first point lon
	 * @param lat1  first point lon
	 * @param lng2	second point lon
	 * @param lat2	second point lat
	 * @return
	 */
	public static double GetDistance(double lng1, double lat1, double lng2,
			double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	/**
	 * calculated distance string by tow points<p>
	 * if the distance between two points greater than one kilometer<p>
	 * will return two num concat km<p>
	 * if the dis less than kilometer will return integer concat m<p>
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static String getDisString(Double lng1, Double lat1, Double lng2,
			Double lat2){
		try {
			if(lng1 == null || lat1==null || lng2==null || lat2==null)
				return Dis_Non;
			if(lng1 == 0d || lat1 == 0d || lng2==0d || lat2==0d)
				return Dis_Non;
			
			double dis = GetDistance(lng1, lat1, lng2, lat2);
			if(dis > 1000){
				String retDis = dis/1000+"";
				if(retDis.length() > 3){
					return retDis.split("[.]")[0]+"."+retDis.split("[.]")[1].substring(0,1)+Dis_KM;
//					return retDis.substring(0, 3)+Dis_KM;
				}
				return dis/1000+Dis_KM;
			}
			Double d = new Double(dis);
			int intValue = d.intValue();
			return intValue+Dis_M;
		} catch (Exception e) {
			return Dis_Non;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根
		double distance = GetDistance(121.491909, 31.233234, 121.411994,
				31.206134);
		System.out.println("Distance is:" + getDisString(121.491909, 31.233234, 121.491994,
				31.233234));
		System.out.println("Distance is:" + getDisString(161.491909, 31.233234, 121.491994,
				31.233234));
		System.out.println("Distance is:" + getDisString(null, 31.233234, 121.411994,
				31.206134));
		System.out.println("Distance is:" + getDisString(0d, 31.233234, 121.411994,
				31.206134));
	}
}

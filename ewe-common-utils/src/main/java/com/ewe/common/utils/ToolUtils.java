package com.ewe.common.utils;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * ClassName:ToolUtils
 * Description:工具类
 *
 * @author luckyhua
 * @Date 2015年6月14日下午4:22:52
 * @version 
 *
 */
public class ToolUtils {
	
	private final static double EARTH_RADIUS = 6378.137; //地球半径

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 
      * @Title: GetDistance
      * @Description: 获得距离
      * @param lat1 
      * @param lng1
      * @param lat2
      * @param lng2
      * @return    
      * @return double   
      *
     */
    public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
        if ((lat2 == 0 && lng2 == 0) || (lng1 == 0 && lat1 == 0))
            return 0.0;
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = (s * 10000) / 10000;
        return s;
    }
    
    /**
     * 
      * @Title: getIpAddr
      * @Description: 获取Ip地址
      * @param request
      * @return    
      * @return String   
      *
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * 
      * @Title: generateRandom
      * @Description: 生成随机数
      * @param num
      * @return    
      * @return String   
      *
     */
    public static String generateRandom(int num) {
    	char[] codeSequence = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    	// 生成随机数  
        Random random = new Random();
        // randomCode记录随机产生的验证码  
        StringBuffer randomCode = new StringBuffer();  
        // 随机产生codeCount个字符的验证码。  
        for (int i = 0; i < num; i++) {  
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);  
            // 将产生的四个随机数组合在一起。  
            randomCode.append(strRand);  
        }  
        return randomCode.toString();
    }
    
}



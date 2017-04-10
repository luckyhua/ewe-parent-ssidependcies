package com.ewe.common.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 订单工具
 * @author jade
 *
 */
public class OrdersUtils {

	/**
	 * 加法
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double add(double d1,double d2){
		return  BigDecimal.valueOf(d1).add(BigDecimal.valueOf(d2)).doubleValue();
	}
	
	/**
	 * 减法
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double minus(double d1,double d2){
		return  BigDecimal.valueOf(d1).subtract(BigDecimal.valueOf(d2)).doubleValue();
	}
	
	/**
	 * 乘法
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double multiply(double d1,double d2){
		return BigDecimal.valueOf(d1).multiply(BigDecimal.valueOf(d2)).doubleValue();
	}
	
	/**
	 * 除法，3位小数，遵循四舍五入规则
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double divide(double d1,double d2){
		return BigDecimal.valueOf(d1).divide(BigDecimal.valueOf(d2),3,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 生成订单号
	 * 订单意义:用户 + 时间
	 * @param userId
	 * @return
	 * @throws BaseException
	 */
	public static String generateOrderCode(Integer userId) {
		String userIdStr = generatePKey(userId, 16);
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
		return sdf.format(new Date())+userIdStr;
	}

	/**
	 * 兑换码
	 * 时间(Long) + 其他主键信息
	 * @throws BaseException 
	 * @retur
	 */
	public static String generateExchangeCode(Integer ... params) {
		StringBuffer sb = new StringBuffer();
		sb.append(Long.toString(new Date().getTime() - 1000000000000L,32));
		String encodeKey = null;
		if(null != params){
			for(Integer param : params){
				encodeKey = generatePKey(param,32);
				if(StringUtils.isNotEmpty(encodeKey)){
					sb.append("-").append(encodeKey);
				}
			}
		}
		
		return sb.toString().toUpperCase();
	}
	
	/**
	 * 整型主键编码转换
	 * @param primaryKey
	 * @param radix
	 * @return
	 */
	private static String generatePKey(Integer primaryKey,int radix) {
		if(null == primaryKey) return StringUtils.EMPTY;
		
		String userIdHex = Integer.toString(primaryKey, radix);
		String userIdStr = StringUtils.reverse(StringUtils.substring((userIdHex + "0000"),0,4)).toUpperCase();
		return userIdStr;
	}
	
}

/**
 * 
 */
package com.ewe.common.utils.eweweb;

/**
 * @author   E-mail:liuchou.ewedata.com
 * @date 创建时间：2015年7月21日 上午9:26:57 
 * @Description  认证结果
 * @version 1.0 
 * @since  
 *  
 */
public class ValidateResult {

	/**
	 * 是否成功
	 */
	private  Boolean result;
	/**	 * 认证结果类型）
	 */
	private String rspContent;
	
	/**
	 * @return the result
	 */
	public Boolean getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(Boolean result) {
		this.result = result;
	}
	/**
	 * @return the rspContent
	 */
	public String getRspContent() {
		return rspContent;
	}
	/**
	 * @param rspContent the rspContent to set
	 */
	public void setRspContent(String rspContent) {
		this.rspContent = rspContent;
	}
	/**
	 * @param result
	 * @param rspCodeType
	 */
	public ValidateResult() {
		super();
	}
	/**
	 * @param result
	 * @param rspCodeType
	 */
	public ValidateResult(Boolean result, String rspContent) {
		super();
		this.result = result;
		this.rspContent = rspContent;
	}
	
}

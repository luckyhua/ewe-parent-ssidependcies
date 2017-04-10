/**
 * 
 */
package com.ewe.common.utils.eweweb;

/**
 * @author   E-mail:liuchou.ewedata.com
 * @date 创建时间：2015年7月20日 下午7:40:13 
 * @Description 
 * @version 1.0 
 * @since  
 *  
 */
public enum HanxinNameCertRspCode {
	C0000	("0000",	"验证成功"),
	C9999	("9999",	"未知错误"),
	C9901	("9901",	"报文格式错误"),
	C9902	("9902",	"解密错误"),
	C9903	("9903",	"查无此应用"),
	C9904	("9904",	"报文域不完整"),
	C9921	("9921",	"报文域格式验证错误"),
	C9922	("9922",	"身份验证错误"),
	C9923	("9923",	"卡号实名认证失败/对公实名认证失败"),
	C9924	("9924",	"不支持该银行卡实名验证"),
	C9925	("9925",	"银行卡号和手机号不匹配"),
	C9926	("9926",	"实人认证错误"),
	C9927	("9927",	"该银行卡不支持手机号验证"),
	C9929	("9929",	"渠道订单号重复"),
	C9930	("9930",	"发生异常"),
	C9931	("9931",	"卡号与身份证号不匹配"),
	C9932	("9932",	"卡号与身份证号不支持验证");

	// 成员变量
    private String respCode;
    private String content;
	/**
	 * @param name
	 * @param ordinal
	 */
	private  HanxinNameCertRspCode(String code, String str) {
		this.respCode = code;
		this.content = str;
	}
	/**
	 * @return the respCode
	 */
	public String getRespCode() {
		return respCode;
	}
	/**
	 * @param respCode the respCode to set
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public static HanxinNameCertRspCode formcode(final String code){
		for(final HanxinNameCertRspCode hanxinNameCertRspCode :values()){
			if(hanxinNameCertRspCode.respCode.equals(code)){
				return hanxinNameCertRspCode;
			}
		}
		throw new IllegalArgumentException(HanxinNameCertRspCode.class.getName() + "dont't containt the rspCode" + code);
	}
	
	
}

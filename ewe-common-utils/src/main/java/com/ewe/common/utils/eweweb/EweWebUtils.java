/**
 * 
 */
package com.ewe.common.utils.eweweb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.ewe.common.utils.HttpsUtil;

/**
 * @author E-mail:liuchou.ewedata.com
 * @date 创建时间：2015年7月20日 下午3:48:42
 * @Description 个人身份验证
 * @version 1.0
 * @since
 * 
 */
public class EweWebUtils {
	private static final String EWE_WEB_URL = "https://ewedata.cn/ucf-webapp/api/";
	private static final String HAN_XIN_NAME_CERT_API = "hanxin_name_cert_check";
	private static final String HANXIN_NAME_CARD_API = "hanxin_name_card_check";
	private static final String HANXIN_NAME_CARD_CERT_PHONE_CHECK = "hanxin_name_card_cert_phone_check";
	private static final String USER_NAME = "ewexfjr";
	private static final String PASSOWRD = "1qaz2wsx";

	private static final String SUCCESS_CODE = "0";

	/**
	 * 
	 * @param name the user's real name
	 * @param card the user's bank card
	 * @param cert the user's identity card number
	 * @param phone the bank registed phone number
	 * @return
	 */
	public static ValidateResult queryHanxinNameCardCertPhoneApi(String name,
			String card, String cert, String phone) {
		StringBuffer url = new StringBuffer(EWE_WEB_URL);

		if(StringUtils.isBlank(name)||StringUtils.isBlank(card)||
				StringUtils.isBlank(cert)||StringUtils.isBlank(phone)){
			return new ValidateResult(false, "请求参数不能为空");
		}
		
		try {
			name = URLEncoder.encode(name, "utf-8");
			card = URLEncoder.encode(card, "utf-8");
			cert = URLEncoder.encode(cert, "utf-8");
			phone = URLEncoder.encode(phone, "utf-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		url.append(HANXIN_NAME_CARD_CERT_PHONE_CHECK).append("/")
				.append(name).append("/")
				.append(card).append("/")
				.append(cert).append("/")
				.append(phone).append("/")
				.append(USER_NAME).append("/")
				.append(PASSOWRD);

		return getResponse(url);
	}

	/**
	 * 韩鑫个人身份验证(姓名和身份证)
	 * 
	 * @param name
	 *            姓名
	 * @param cert
	 *            身份证
	 * @return
	 */
	public static ValidateResult queryHanxinNameCertApi(String name, String cert) {

		StringBuffer url = new StringBuffer(EWE_WEB_URL);
		if(StringUtils.isBlank(name)||StringUtils.isBlank(cert)){
			return new ValidateResult(false, "请求参数不能为空");
		}
		try {
			name = URLEncoder.encode(name, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		url.append(HAN_XIN_NAME_CERT_API).append("/").append(name).append("/")
				.append(cert).append("/").append(USER_NAME).append("/")
				.append(PASSOWRD);

		return getResponse(url);
	}

	/**
	 * 韩鑫个人身份验证(姓名和银行卡)
	 * 
	 * @param name
	 *            姓名
	 * @param card
	 *            银行卡
	 * @return
	 */
	public static ValidateResult queryHanxinNameCardApi(String name, String card) {

		StringBuffer url = new StringBuffer(EWE_WEB_URL);
		if(StringUtils.isBlank(name)||StringUtils.isBlank(card)){
			return new ValidateResult(false, "请求参数不能为空");
		}
		
		try {
			name = URLEncoder.encode(name, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		url.append(HANXIN_NAME_CARD_API).append("/").append(name).append("/")
				.append(card).append("/").append(USER_NAME).append("/")
				.append(PASSOWRD);

		return getResponse(url);
	}

	/**
	 * 获取认证信息
	 * 
	 * @param url
	 * @return
	 */
	private static ValidateResult getResponse(StringBuffer url) {
		String result = HttpsUtil.getMethod(url.toString());

		JSONObject json = null;
		if (StringUtils.isNotBlank(result)) {
			json = JSONObject.fromObject(result);
		}

		String respCode = "";
		if (json != null) {
			if (SUCCESS_CODE.equals(json.getString("code"))) {
				JSONObject jobj = json.getJSONObject("data");
				respCode = jobj.getString("respCode");

				ValidateResult validateResult = new ValidateResult();

				if (HanxinNameCertRspCode.C0000.getRespCode().equals(respCode)) {
					validateResult.setResult(true);
					validateResult.setRspContent(HanxinNameCertRspCode.C0000
							.getContent());
					return validateResult;
				}

				validateResult.setResult(false);
				validateResult.setRspContent(HanxinNameCertRspCode.formcode(
						respCode).getContent());
				return validateResult;
			}
		}

		return new ValidateResult(false, "请求异常");

	}
}

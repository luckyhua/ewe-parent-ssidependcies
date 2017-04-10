package com.ewe.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class HttpsUtil {
	private static Log log = LogFactory.getLog(HttpsUtil.class);
	/**
	 * 忽视证书HostName
	 */
	private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
		public boolean verify(String s, SSLSession sslsession) {
			log.warn("WARNING: Hostname is not matched for cert.");
			return true;
		}
	};

	/**
	 * Ignore Certification
	 */
	private static TrustManager ignoreCertificationTrustManger = new X509TrustManager() {

		private X509Certificate[] certificates;

		@Override
		public void checkClientTrusted(X509Certificate certificates[],
				String authType) throws CertificateException {
			if (this.certificates == null) {
				this.certificates = certificates;
			}

		}

		@Override
		public void checkServerTrusted(X509Certificate[] ax509certificate,
				String s) throws CertificateException {
			if (this.certificates == null) {
				this.certificates = ax509certificate;
			}
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}

	};

	public static String getMethod(String urlString) {

		String output = "";
		try {

			URL url = new URL(urlString);

			/*
			 * use ignore host name verifier
			 */
			HttpsURLConnection
					.setDefaultHostnameVerifier(ignoreHostnameVerifier);
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();

			connection.setRequestProperty("charset", "utf-8");
			// Prepare SSL Context
			TrustManager[] tm = { ignoreCertificationTrustManger };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			connection.setSSLSocketFactory(ssf);
	
			InputStream reader = connection.getInputStream();
			BufferedReader responseBuffer = new BufferedReader(
					new InputStreamReader(reader,
							"UTF-8"));

			
			String temp = "";
			while((temp = responseBuffer.readLine()) != null)
			{
				output = output + temp;
			}
			System.out.println(output);
			reader.close();
			responseBuffer.close();

			connection.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		return output;
	}
}
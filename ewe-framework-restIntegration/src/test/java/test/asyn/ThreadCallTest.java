package test.asyn;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;

public class ThreadCallTest {

	public static void main(String[] args) {
		
		SocketConfig sc = SocketConfig.custom().setSoTimeout(5).build();
		
		RequestConfig config = RequestConfig.custom().
				setConnectionRequestTimeout(1000)
				.setSocketTimeout(3000)
				.setConnectTimeout(5000)
				.build();
		final HttpClient client = HttpClientBuilder.create()
				.setMaxConnPerRoute(20)
				.setMaxConnTotal(20)
				.setDefaultRequestConfig(config)
				.setDefaultSocketConfig(sc)
				.build();

		for (int i = 0; i < 20; i++) {
			final int a = i;
			new Thread(new Runnable() {

				@Override
				public void run() {
					HttpGet get = new HttpGet(
							"http://192.168.2.101:8888/eweAuth/v1/sdkapi/auth/authPhoto");
					try {
						HttpResponse response = client.execute(get);
						System.out.println("thread:" + a);
//						System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}).start();
		}

	}

	@Test
	public void testAsync() {

	}

	private static HttpClient getClient(boolean isSSL) {

		HttpClient httpClient = new DefaultHttpClient();
		if (isSSL) {
			X509TrustManager xtm = new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {

				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {

				}
			};

			try {
				SSLContext ctx = SSLContext.getInstance("TLS");

				ctx.init(null, new TrustManager[] { xtm }, null);

				// SSLConnectionSocketFactory socketFactory =
				// SSLConnectionSocketFactory.getSocketFactory();

				return HttpClientBuilder.create().setSslcontext(ctx).build();

				// httpClient.getConnectionManager().getSchemeRegistry()
				// .register(new Scheme("https", 443, socketFactory));

			} catch (Exception e) {
				throw new RuntimeException();
			}
		}

		return HttpClientBuilder.create().setMaxConnPerRoute(20).build();
		// return httpClient;
	}

	public HttpClient create() {
		SocketConfig sc = SocketConfig.custom().setSoTimeout(5).build();
		ConnectionConfig defaultConnectionConfig = ConnectionConfig.custom()
				.build();
		
		
		int max = 20;

		HttpClientConnectionManager connManager = null;
		LayeredConnectionSocketFactory sslSocketFactory = null;
		final String[] supportedProtocols = { "80" };
		final String[] supportedCipherSuites = { "443" };
		X509HostnameVerifier hostnameVerifier = null;
		if (hostnameVerifier == null) {
			hostnameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
		}
		sslSocketFactory = new SSLConnectionSocketFactory(
				SSLContexts.createDefault(), hostnameVerifier);
		@SuppressWarnings("resource")
		final PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(
				RegistryBuilder
						.<ConnectionSocketFactory> create()
						.register("http",
								PlainConnectionSocketFactory.getSocketFactory())
						.register("https", sslSocketFactory).build());

		poolingmgr.setDefaultSocketConfig(sc);
		poolingmgr.setDefaultConnectionConfig(defaultConnectionConfig);

		poolingmgr.setDefaultMaxPerRoute(max);
		poolingmgr.setMaxTotal(2 * max);
		
		
		
		
		connManager = poolingmgr;
		
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(1000).setSocketTimeout(3000).setConnectTimeout(5000).build();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

		return null;
	}
}

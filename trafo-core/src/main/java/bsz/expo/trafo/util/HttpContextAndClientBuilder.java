package bsz.expo.trafo.util;

import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.SSLContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * * Utility zu Erzeugung eines {@link CloseableHttpClient} und des zugehörigen
 * {@link HttpClientContext}, * der SSL-Verbindungen und BasicAuthentication
 * unterstützt, wenn username and password gesetzt sind.
 */
public class HttpContextAndClientBuilder {

	private Logger log = LoggerFactory.getLogger(getClass());
	private String server;
	private String username;
	private String password;
	private CloseableHttpClient httpClient;
	private HttpClientContext httpClientContext;

	public static HttpContextAndClientBuilder createInstance() {
		return new HttpContextAndClientBuilder();
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public HttpClientContext getHttpClientContext() {
		return httpClientContext;
	}

	public HttpContextAndClientBuilder setServer(String server) {
		this.server = server;
		return this;
	}

	public HttpContextAndClientBuilder setUsername(String username) {
		this.username = username;
		return this;
	}

	public HttpContextAndClientBuilder setPassword(String password) {
		this.password = password;
		return this;
	}
	
	public HttpContextAndClientBuilder build() throws Exception {
		log.debug("Started building httpClient...");
		
		final URL serverURL;
		try {
			serverURL = new URL(server);
		} catch (MalformedURLException e) {
			throw new Exception("server is not a valid URL: " + server, e);
		}
		
        httpClientContext = HttpClientContext.create();
        final HttpClientBuilder httpClientBuilder = HttpClients.custom();
		final HttpHost targetHost = new HttpHost(serverURL.getHost(), serverURL.getPort(), serverURL.getProtocol());

		if ("https".equals(serverURL.getProtocol())) {
			log.debug("Building SSLContext...");
			final SSLContext sslContext;
			try {
				sslContext = new SSLContextBuilder().loadTrustMaterial(null, (credential, zwei) -> true).build();
			} catch (Exception e) {
				throw new RuntimeException("Error building SSLContext!", e);
			}
        
			httpClientBuilder
				.setSSLContext(sslContext)
			    .setSSLHostnameVerifier(new NoopHostnameVerifier());
		}
        
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
        	log.debug("Adding Credentials...");
			
        	final CredentialsProvider credsProvider = new BasicCredentialsProvider();
	        credsProvider.setCredentials(
	        		new AuthScope(targetHost.getHostName(), targetHost.getPort()),
	                new UsernamePasswordCredentials(username, password));
	        
	        // POST requires Preemptive authentication:
	        // Create AuthCache instance
	        final AuthCache authCache = new BasicAuthCache();
	        // Generate BASIC scheme object and add it to the local auth cache
	        final BasicScheme basicAuth = new BasicScheme();
	        authCache.put(targetHost, basicAuth);
	
	        // Add AuthCache to the execution context
	        
	        httpClientContext.setCredentialsProvider(credsProvider);
	        httpClientContext.setAuthCache(authCache);
	        
	        httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
        }
        
        httpClient = httpClientBuilder.build();
        log.debug("Finished building httpClient!");
        
        return this;
	}
}

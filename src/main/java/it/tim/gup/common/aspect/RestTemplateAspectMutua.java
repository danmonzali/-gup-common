package it.tim.gup.common.aspect;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Aspect
@Component
@Order(1)
public class RestTemplateAspectMutua {

	@Autowired
	private BeanMutua beanMutua;

	@Value("${https.truststore.instance:JKS}")
	private String truststoreInstance;

	@Value("${https.keystore.instance:PKCS12}")
	private String keystoreInstance;

	@Value("${https.mutual-auth.keystore.path:/path/keystore}")
	private String keystorePath;

	@Value("${https.mutual-auth.keystore.password:keystore_password}")
	private String keystorePassword;

	@Value("${https.mutual-auth.truststore.path:/path/trustore}")
	private String truststorePath;

	@Value("${https.mutual-auth.truststore.password:trustore_password}")
	private String truststorePassword;

	@Autowired
	private ApplicationContext applicationContext;

	private static final Logger logger = LogManager.getLogger(RestTemplateAspectMutua.class);

	@Around("execution(* org.springframework.web.client.RestTemplate.exchange(..))")
	public Object aroundExchange(ProceedingJoinPoint joinPoint) throws Throwable {
		// Prima di chiamare il metodo exchange
		long startTime = System.currentTimeMillis();
		boolean makeMutua = false;
		Object[] args = joinPoint.getArgs();

		String url = String.valueOf(args[0]);
		HttpMethod method = args.length > 1 && args[1] instanceof HttpMethod ? (HttpMethod) args[1] : null;
		Object requestBody = null;

		if (args.length > 2 && args[2] instanceof HttpEntity<?> httpEntity) {
			requestBody = httpEntity.getBody();
		}

		HttpComponentsClientHttpRequestFactory requestFactory = ((HttpComponentsClientHttpRequestFactory) ((RestTemplate) joinPoint
				.getThis()).getRequestFactory());

		if (beanMutua.getUrl() != null) {
			for (int i = 0; i < beanMutua.getUrl().length; i++) {
				String s = beanMutua.getUrl()[i];
				if (url.contains(s) && beanMutua.getAccept()[i] == true) {
					makeMutua = true;

					break;
				}
			}
		}
		if (!makeMutua && url.toUpperCase().startsWith("HTTPS")) {
			((RestTemplate) joinPoint.getThis()).setRequestFactory(makeCallNonMutua(requestFactory, url));
		} else if (!makeMutua) {
			RestTemplate rtDefault = (RestTemplate) applicationContext.getBean("restTemplateDefault");
			((RestTemplate) joinPoint.getThis()).setRequestFactory(rtDefault.getRequestFactory());
		} else {
			((RestTemplate) joinPoint.getThis()).setRequestFactory(makeCallMutua(requestFactory));
		}
		// Esegui il metodo exchange()
		Object result = joinPoint.proceed();
		long duration = System.currentTimeMillis() - startTime;

		Object responseBody = null;
		Integer status = null;

		if (result instanceof ResponseEntity<?> responseEntity) {
			responseBody = responseEntity.getBody();
			status = responseEntity.getStatusCode().value();
		}

		logger.info("HTTP_CALL method={} url={} status={} timeMs={} req={} res={}", method, url, status, duration,
				requestBody, responseBody);

		return result;
	}

	public HttpComponentsClientHttpRequestFactory makeCallNonMutua(
			HttpComponentsClientHttpRequestFactory requestFactory, String url) {
		// HttpComponentsClientHttpRequestFactory requestFactory = new
		// HttpComponentsClientHttpRequestFactory();

		try {
			SSLContext sslContext = SSLContextBuilder.create().build();

			HostnameVerifier host = new NoopHostnameVerifier();

			DefaultClientTlsStrategy tlsStrategy = new DefaultClientTlsStrategy(sslContext, host);

			Registry<TlsSocketStrategy> socketFactoryRegistry = RegistryBuilder.<TlsSocketStrategy>create()
					.register("https", tlsStrategy) // Gestisce connessioni HTTPS
					.build();

			var connManager = PoolingHttpClientConnectionManagerBuilder.create()
					.setTlsSocketStrategy(socketFactoryRegistry.lookup("https")).build();

			CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();

			requestFactory.setHttpClient(httpClient);

		} catch (Throwable t) {
			logger.warn("ATTENZIONE qualcosa è andato storto: ", t);
		}

		return requestFactory;
	}

	public HttpComponentsClientHttpRequestFactory makeCallMutua(HttpComponentsClientHttpRequestFactory requestFactory) {

		// HttpComponentsClientHttpRequestFactory requestFactory = new
		// HttpComponentsClientHttpRequestFactory();
		try {

			KeyStore keyStore = KeyStore.getInstance(keystoreInstance);
			try (var keyStoreStream = new FileInputStream(new File(keystorePath))) {
				keyStore.load(keyStoreStream, keystorePassword.toCharArray());
			}

			// 🔹 2. Caricare il Truststore (verifica il server)
			KeyStore trustStore = KeyStore.getInstance(truststoreInstance);
			try (var trustStoreStream = new FileInputStream(new File(truststorePath))) {
				trustStore.load(trustStoreStream, truststorePassword.toCharArray());
			}

			// 🔹 3. Creare SSLContext con Keystore e Truststore
			SSLContext sslContext = SSLContextBuilder.create().loadKeyMaterial(keyStore, keystorePassword.toCharArray()) // Certificato
																															// client
					.loadTrustMaterial(trustStore, null) // Certificati attendibili
					.build();

			// 🔹 4. Creare il TLS Strategy con SSLContext personalizzato
			DefaultClientTlsStrategy tlsStrategy = new DefaultClientTlsStrategy(sslContext);

			// 🔹 5. Creare il Connection Manager (gestisce HTTP e HTTPS con mTLS)
			var connManager = PoolingHttpClientConnectionManagerBuilder.create().setTlsSocketStrategy(tlsStrategy)
					.build();

			// 🔹 6. Creare HttpClient con il Connection Manager
			CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();

			requestFactory.setHttpClient(httpClient);

			return requestFactory;
		} catch (Throwable t) {
			logger.warn("ATTENZIONE non è stato possibile abilitare la mutua: ", t);
		}

		return null;
	}
}
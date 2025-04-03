package it.tim.gup.common.aspect;


import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
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
		boolean makeMutua = false;
		String url = (String) joinPoint.getArgs()[0];

		HttpComponentsClientHttpRequestFactory requestFactory  = ((HttpComponentsClientHttpRequestFactory)((RestTemplate) joinPoint.getThis()).getRequestFactory());

		if (beanMutua.getUrl()!=null) {
			for (int i = 0; i < beanMutua.getUrl().length; i++) {
				String s = beanMutua.getUrl()[i];
				if (url.contains(s) && beanMutua.getAccept()[i] == true) {
					makeMutua = true;
					((RestTemplate) joinPoint.getThis()).setRequestFactory(makeCallMutua(requestFactory));
					break;
				}
			}
		}
		if (!makeMutua && url.toUpperCase().startsWith("HTTPS")) {
			((RestTemplate) joinPoint.getThis()).setRequestFactory(makeCallNonMutua(requestFactory,url));
		} else if(!makeMutua){
			RestTemplate rtDefault = (RestTemplate) applicationContext.getBean("restTemplateDefault");
			((RestTemplate) joinPoint.getThis()).setRequestFactory(rtDefault.getRequestFactory());
		}
		// Esegui il metodo exchange()
		Object result = joinPoint.proceed();

		//		// Dopo la chiamata exchange
		//		if (result instanceof ResponseEntity) {
		//			ResponseEntity<?> response = (ResponseEntity<?>) result;
		//			System.out.println("Risposta ricevuta: " + response.getStatusCode());
		//		}

		return result;
	}

	public HttpComponentsClientHttpRequestFactory makeCallNonMutua(HttpComponentsClientHttpRequestFactory requestFactory,String url) {
		//		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		try {
		SSLContext sslContext = SSLContextBuilder.create().build();
		SSLConnectionSocketFactory sslsf = 
		        new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		
		
		Registry<ConnectionSocketFactory> socketFactoryRegistry = 
		        RegistryBuilder.<ConnectionSocketFactory> create()
		        .register("https", sslsf)
		        .register("http", new PlainConnectionSocketFactory())
		        .build();
		
		BasicHttpClientConnectionManager connectionManager =
		        new BasicHttpClientConnectionManager(socketFactoryRegistry);
		
		CloseableHttpClient httpClient = HttpClients.custom()
		        .setConnectionManager(connectionManager)
		        .build();


		requestFactory.setHttpClient(httpClient);

	    } catch (Throwable t) {
			logger.warn("ATTENZIONE qualcosa è andato storto: ", t);
		}

		return requestFactory;
	}

	public HttpComponentsClientHttpRequestFactory makeCallMutua(HttpComponentsClientHttpRequestFactory requestFactory) {

		//		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		try {
			// Carica il keystore (certificato client e chiave privata)
			KeyStore keyStore = KeyStore.getInstance(keystoreInstance);
			keyStore.load(new FileInputStream(new File(keystorePath)), keystorePassword.toCharArray());

			// Carica il truststore (certificati server attendibili)
			KeyStore trustStore = KeyStore.getInstance(truststoreInstance);
			trustStore.load(new FileInputStream(new File(truststorePath)), truststorePassword.toCharArray());

			// Configura il contesto SSL con il keystore e il truststore
			SSLContext sslContext = SSLContextBuilder.create().loadKeyMaterial(keyStore, keystorePassword.toCharArray()) // client
					// cert
					.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()) // server cert
					.build();

			
			SSLConnectionSocketFactory sslsf = 
			        new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
			
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry = 
			        RegistryBuilder.<ConnectionSocketFactory> create()
			        .register("https", sslsf)
			        .register("http", new PlainConnectionSocketFactory())
			        .build();
			
			BasicHttpClientConnectionManager connectionManager =
			        new BasicHttpClientConnectionManager(socketFactoryRegistry);

			
			
			CloseableHttpClient httpClient = HttpClients.custom()
			        .setConnectionManager(connectionManager)
			        .build();
			
			requestFactory.setHttpClient(httpClient);

			return requestFactory;
		} catch (Throwable t) {
			logger.warn("ATTENZIONE non è stato possibile abilitare la mutua: ", t);
		}

		return null;
	}
}
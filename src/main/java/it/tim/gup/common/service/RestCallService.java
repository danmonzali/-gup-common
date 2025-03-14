package it.tim.gup.common.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import it.tim.gup.common.config.GupApplicationListener;
import it.tim.gup.common.exception.GupException;
import it.tim.gup.common.exception.GupUnprocessableEntity;

@Configuration
public abstract class RestCallService {

	@Value("${https.mutual-auth.keystore.path:/path/keystore}")
	private String keystorePath;

	@Value("${https.mutual-auth.keystore.password:keystore_password}")
	private String keystorePassword;

	@Value("${https.mutual-auth.truststore.path:/path/trustore}")
	private String truststorePath;

	@Value("${https.mutual-auth.truststore.password:truststore_password}")
	private String truststorePassword;

	@Value("${https.truststore.instance:PKCS12}")
	private String truststoreInstance;

	@Value("${https.keystore.instance:JKS}")
	private String keystoreInstance;

	@Autowired
	private ApplicationContext applicationContext;

	private static final String HMAC_ALGORITHM = "HmacSHA256";

	@Bean(name = "restTemplateDefault")
	RestTemplate getRestTemplateDefault() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		return new RestTemplate(requestFactory);
	}

	@Bean(name = "restTemplate")
	RestTemplate getRestTemplate() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		return new RestTemplate(requestFactory);
	}

	public String getHttpHeaderDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(new Date());
	}

	public String getHttpHeaderHost() {
		String host = "locahost:8080";
		try {
			host = InetAddress.getLocalHost().getHostName() + ":" + GupApplicationListener.SERVER_PORT;
		} catch (UnknownHostException e) {
			getLogger().error("getHttpHeaderHost - errore UnknownHostException, imposto come host = locahost:8080 "
					+ e.getMessage().replaceAll("[\n\r]", ""));
		}
		return host;
	}

	public String getHttpHeaderAuthorization(String date, String host, HttpMethod httpMethod, String methodPath,
			String merId, String digest) throws GupException {
		String authorization = null;
		if (httpMethod.equals(HttpMethod.GET)) {
			authorization = getHttpHeaderAuthorizationGet(date, host, methodPath, merId);
		} else if (httpMethod.equals(HttpMethod.POST) || httpMethod.equals(HttpMethod.PUT)) {
			authorization = getHttpHeaderAuthorizationWithDigest(date, host, methodPath, merId, digest, httpMethod);
		}
		return authorization;
	}

	private String getHttpHeaderAuthorizationWithDigest(String date, String host, String methodPath, String merId,
			String digest, HttpMethod httpMethod) throws GupException {

		host = sanitizeHeaderValue(host);
		date = sanitizeHeaderValue(date);
		methodPath = sanitizeHeaderValue(methodPath);
		digest = sanitizeHeaderValue(digest);

		//String header = "host: " + host + "\ndate: " + date + "\n(request-target): "
		//		+ httpMethod.toString().toLowerCase() + " " + methodPath + "\ndigest: " + digest;

		String header = """
				host: %s
				date: %s
				(request-target): %s %s
				digest: %s
				""".formatted(host, date, httpMethod.toString().toLowerCase(), methodPath, digest).strip();

//		String authorization = "Signature keyId=\"" + merId
//				+ "\",algorithm=\"hmac-sha256\",headers=\"host date (request-target) digest\",signature=\"";

        String keyId = System.getenv("NETS_KEYID_" + merId);
        
        // Se la chiave specifica non è trovata, uso la chiave di default
        if (keyId == null) {
            keyId = System.getenv("NETS_KEYID_DEFAULT");

            // Se non c'è nemmeno la chiave di default, lancio un'eccezione
            if (keyId == null) {
                getLogger().error("Chiave API non trovata per merId: " + merId + " né chiave di default.");
                return "";
            }else {
                getLogger().warn("Usata chiave di default per merId: " + merId);
            }
        }
        
		try {

			Mac hasher = Mac.getInstance(HMAC_ALGORITHM);

			hasher.init(new SecretKeySpec(keyId.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
			byte[] hash = hasher.doFinal(header.getBytes(StandardCharsets.UTF_8));
			String signature = Base64.getEncoder().encodeToString(hash);

			return """
					Signature keyId="%s",algorithm="hmac-sha256",headers="host date (request-target) digest",signature="%s"
					""".formatted(merId, signature).strip();

		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			getLogger().error("Errore nella generazione della firma HMAC.");
			throw new GupException("Errore crittografico.");
		}
	}

	private String sanitizeHeaderValue(String value) {
		return Objects.requireNonNull(value).replaceAll("[\\r\\n]", "").strip();
	}

	@SuppressWarnings("unused")
	@Deprecated
	private String getHttpHeaderAuthorizationPost(String date, String host, String methodPath, String merId,
			String digest) throws GupException {
		String header = "host: " + host + "\ndate: " + date + "\n(request-target): post " + methodPath + "\ndigest: "
				+ digest;
		String authorization = "Signature keyId=\"" + merId
				+ "\",algorithm=\"hmac-sha256\",headers=\"host date (request-target) digest\",signature=\"";

		String keyId = System.getenv("NETS_KEYID_" + merId);
		if (keyId == null) {
			getLogger().error("getHttpHeaderAuthorizationPost - ERRORE: variabile d'ambiente \"NETS_KEYID_" + merId
					+ "\" non trovata!");
			throw new GupUnprocessableEntity("Mer Id " + merId + " non configurato");
		}

		Mac hasher = null;
		try {
			hasher = Mac.getInstance("HmacSHA256");
			hasher.init(new SecretKeySpec(keyId.getBytes(), "HmacSHA256"));
		} catch (NoSuchAlgorithmException e) {
			getLogger().error("getHttpHeaderAuthorizationPost - errore NoSuchAlgorithmException"
					+ e.getMessage().replaceAll("[\n\r]", ""));
			throw new GupException("ERRORE: NoSuchAlgorithmException");
		} catch (InvalidKeyException e) {
			getLogger().error("getHttpHeaderAuthorizationPost - errore InvalidKeyException"
					+ e.getMessage().replaceAll("[\n\r]", ""));
			throw new GupException("ERRORE: InvalidKeyException");
		}

		byte[] hash = hasher.doFinal(header.getBytes());
		authorization += Base64.getEncoder().encodeToString(hash) + "\"";
		return authorization;
	}

	public String getHttpHeaderAuthorizationGet(String date, String host, String methodPath, String merId)
            throws GupException {
        // Sanitizzazione input per prevenire attacchi di manipolazione
        host = sanitizeHeaderValue(host);
        date = sanitizeHeaderValue(date);
        methodPath = sanitizeHeaderValue(methodPath);

        // Costruzione header sicura con blocchi di testo
        String header = """
                host: %s
                date: %s
                (request-target): get %s
                """.formatted(host, date, methodPath).strip();

        // Recupero chiave in modo sicuro
        String keyId = System.getenv("NETS_KEYID_" + merId);
        
        // Se la chiave specifica non è trovata, uso la chiave di default
        if (keyId == null) {
            keyId = System.getenv("NETS_KEYID_DEFAULT");
            // Se non c'è nemmeno la chiave di default, lancio un'eccezione
            if (keyId == null) {
                getLogger().error("Chiave API non trovata per merId: " + merId + " né chiave di default.");
                return "";
            }else {
            	// Log di sicurezza (senza esporre il valore della chiave)
                getLogger().warn("Usata chiave di default per merId: " + merId);
            }     
        }

        // Generazione firma HMAC
        try {
            Mac hasher = Mac.getInstance(HMAC_ALGORITHM);
            hasher.init(new SecretKeySpec(keyId.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            byte[] hash = hasher.doFinal(header.getBytes(StandardCharsets.UTF_8));
            String signature = Base64.getEncoder().encodeToString(hash);

            return """
                    Signature keyId="%s",algorithm="hmac-sha256",headers="host date (request-target)",signature="%s"
                    """.formatted(merId, signature).strip();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            getLogger().error("Errore nella generazione della firma HMAC.");
            throw new GupException("Errore crittografico.");
        }
    }


	public String getHttpHeaderDigest(String body) throws GupException {
		return "SHA-256=" + sha256(body);
	}

	private String sha256(String text) throws GupException {
		String encoded = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
			encoded = Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			getLogger().error("sha256 - errore NoSuchAlgorithmException" + e.getMessage().replaceAll("[\n\r]", ""));
			throw new GupException("ERRORE: NoSuchAlgorithmException");
		}
		return encoded;
	}

	public RestTemplate getRestTemplate(String protocol, int timeout) {
		RestTemplate rt = (RestTemplate) applicationContext.getBean("restTemplate");
		((HttpComponentsClientHttpRequestFactory)rt.getRequestFactory()).setConnectTimeout(timeout);
		((HttpComponentsClientHttpRequestFactory)rt.getRequestFactory()).setConnectTimeout(timeout);



		RestTemplate rtDefault = (RestTemplate) applicationContext.getBean("restTemplateDefault");
		((HttpComponentsClientHttpRequestFactory)rtDefault.getRequestFactory()).setConnectTimeout(timeout);
		((HttpComponentsClientHttpRequestFactory)rtDefault.getRequestFactory()).setConnectTimeout(timeout);
		return rt;

	}

	protected abstract void putLogThreadContextDestination();

	protected abstract void removeLogThreadContextDestination();

	protected abstract Logger getLogger();

}


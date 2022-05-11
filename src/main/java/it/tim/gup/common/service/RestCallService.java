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
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import it.tim.gup.common.config.GupApplicationListener;
import it.tim.gup.common.exception.GupException;
import it.tim.gup.common.exception.GupUnprocessableEntity;

public abstract class RestCallService {

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
			getLogger().error("getHttpHeaderHost - errore UnknownHostException, imposto come host = locahost:8080 " + e.getMessage().replaceAll("[\n\r]", ""));
		}
		return host;
	}

	public String getHttpHeaderAuthorization(String date, String host, HttpMethod httpMethod, String methodPath, String merId, String digest) throws GupException {
		String authorization = null;
		if (httpMethod.equals(HttpMethod.GET) ) {
			authorization = getHttpHeaderAuthorizationGet(date, host, methodPath, merId);
		} else if (httpMethod.equals(HttpMethod.POST) || httpMethod.equals(HttpMethod.PUT)) {
			authorization = getHttpHeaderAuthorizationWithDigest(date, host, methodPath, merId, digest, httpMethod);
		}
		return authorization;
	}

	private String getHttpHeaderAuthorizationWithDigest(String date, String host, String methodPath, String merId, String digest, HttpMethod httpMethod) throws GupException {
		String header = "host: " + host + "\ndate: " + date + "\n(request-target): " + httpMethod.toString().toLowerCase() + " " + methodPath + "\ndigest: " + digest;
		String authorization = "Signature keyId=\"" + merId + "\",algorithm=\"hmac-sha256\",headers=\"host date (request-target) digest\",signature=\"";
		
		String keyId =  System.getenv("NETS_KEYID_" + merId) != null ? System.getenv("NETS_KEYID_" + merId): System.getenv("NETS_KEYID_DEFAULT");
	
		if (keyId == null) {
			getLogger().error("getHttpHeaderAuthorizationPost - ERRORE: variabile d'ambiente \"NETS_KEYID_" + merId + "\" non trovata!");
			return "";
		}
		
		Mac hasher = null;
		try {
			hasher = Mac.getInstance("HmacSHA256");
			hasher.init(new SecretKeySpec(keyId.getBytes(), "HmacSHA256"));
		} catch (NoSuchAlgorithmException e) {
			getLogger().error("getHttpHeaderAuthorizationPost - errore NoSuchAlgorithmException"+ e.getMessage().replaceAll("[\n\r]", ""));
			throw new GupException("ERRORE: NoSuchAlgorithmException");
		} catch (InvalidKeyException e) {
			getLogger().error("getHttpHeaderAuthorizationPost - errore InvalidKeyException"+ e.getMessage().replaceAll("[\n\r]", ""));
			throw new GupException("ERRORE: InvalidKeyException");
		}

		byte[] hash = hasher.doFinal(header.getBytes());
		authorization += Base64.getEncoder().encodeToString(hash) + "\"";
		return authorization;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private String getHttpHeaderAuthorizationPost(String date, String host, String methodPath, String merId, String digest) throws GupException {
		String header = "host: " + host + "\ndate: " + date + "\n(request-target): post " + methodPath + "\ndigest: " + digest;
		String authorization = "Signature keyId=\"" + merId + "\",algorithm=\"hmac-sha256\",headers=\"host date (request-target) digest\",signature=\"";
		
		String keyId = System.getenv("NETS_KEYID_" + merId);
		if (keyId == null) {
			getLogger().error("getHttpHeaderAuthorizationPost - ERRORE: variabile d'ambiente \"NETS_KEYID_" + merId + "\" non trovata!");
			throw new GupUnprocessableEntity("Mer Id " + merId + " non configurato");
		}
		
		Mac hasher = null;
		try {
			hasher = Mac.getInstance("HmacSHA256");
			hasher.init(new SecretKeySpec(keyId.getBytes(), "HmacSHA256"));
		} catch (NoSuchAlgorithmException e) {
			getLogger().error("getHttpHeaderAuthorizationPost - errore NoSuchAlgorithmException"+ e.getMessage().replaceAll("[\n\r]", ""));
			throw new GupException("ERRORE: NoSuchAlgorithmException");
		} catch (InvalidKeyException e) {
			getLogger().error("getHttpHeaderAuthorizationPost - errore InvalidKeyException"+ e.getMessage().replaceAll("[\n\r]", ""));
			throw new GupException("ERRORE: InvalidKeyException");
		}

		byte[] hash = hasher.doFinal(header.getBytes());
		authorization += Base64.getEncoder().encodeToString(hash) + "\"";
		return authorization;
	}
	
	
	private String getHttpHeaderAuthorizationGet(String date, String host, String methodPath, String merId) throws GupException {
		String header = "host: " + host + "\ndate: " + date + "\n(request-target): get " + methodPath;
		String authorization = "Signature keyId=\"" + merId + "\",algorithm=\"hmac-sha256\",headers=\"host date (request-target)\",signature=\"";
		
		String keyId =  System.getenv("NETS_KEYID_" + merId) != null ? System.getenv("NETS_KEYID_" + merId): System.getenv("NETS_KEYID_DEFAULT");

		if (keyId == null) {
			getLogger().error("getHttpHeaderAuthorizationGet - ERRORE: variabile d'ambiente \"NETS_KEYID_" + merId + "\" non trovata!");
			return "";
		}
		
		Mac hasher = null;
		try {
			hasher = Mac.getInstance("HmacSHA256");
			hasher.init(new SecretKeySpec(keyId.getBytes(), "HmacSHA256"));
		} catch (NoSuchAlgorithmException e) {
			getLogger().error("getHttpHeaderAuthorizationGet - errore NoSuchAlgorithmException"+ e.getMessage().replaceAll("[\n\r]", ""));
			throw new GupException("ERRORE: NoSuchAlgorithmException");
		} catch (InvalidKeyException e) {
			getLogger().error("getHttpHeaderAuthorizationGet - errore InvalidKeyException"+ e.getMessage().replaceAll("[\n\r]", ""));
			throw new GupException("ERRORE: InvalidKeyException");
		}

		byte[] hash = hasher.doFinal(header.getBytes());
		authorization += Base64.getEncoder().encodeToString(hash) + "\"";
		return authorization;
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
			getLogger().error("sha256 - errore NoSuchAlgorithmException"+ e.getMessage().replaceAll("[\n\r]", ""));
			throw new GupException("ERRORE: NoSuchAlgorithmException");
		}
		return encoded;
	}
	
	public RestTemplate getRestTemplate(String protocol, int timeout) {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		if (protocol.toUpperCase().equals("HTTPS")) {
			CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
			requestFactory.setHttpClient(httpClient);
		}
		timeout = timeout == 0 ? 1 : timeout; 
		requestFactory.setConnectTimeout(timeout);
		requestFactory.setReadTimeout(timeout);
		return new RestTemplate(requestFactory);
	}
	
	protected abstract void putLogThreadContextDestination();
	
	protected abstract void removeLogThreadContextDestination();
	
	protected abstract Logger getLogger();
	
}

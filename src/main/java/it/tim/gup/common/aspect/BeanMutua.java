package it.tim.gup.common.aspect;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mutua")
public class BeanMutua {

	String url[];
	boolean accept[];

	public String[] getUrl() {
		return url;
	}

	public void setUrl(String[] url) {
		this.url = url;
	}

	public boolean[] getAccept() {
		return accept;
	}

	public void setAccept(boolean[] accept) {
		this.accept = accept;
	}


}

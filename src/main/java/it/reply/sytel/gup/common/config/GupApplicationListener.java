package it.reply.sytel.gup.common.config;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

public class GupApplicationListener implements ApplicationListener<WebServerInitializedEvent> {

	public static int SERVER_PORT;
	
	@Override
	public void onApplicationEvent(WebServerInitializedEvent event) {
		SERVER_PORT = event.getWebServer().getPort();
	}
	
}

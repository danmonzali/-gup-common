package it.tim.gup.common.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

@Component
public class PropertyLogger {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyLogger.class);

	public List<HxxProperties> elencoHxxProperties = new ArrayList<HxxProperties>();

	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) {
		final Environment env = event.getApplicationContext().getEnvironment();
		LOGGER.info("====== Environment and configuration ======");
		LOGGER.info("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));
		final MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();
		StreamSupport.stream(sources.spliterator(), false).filter(ps -> ps instanceof EnumerablePropertySource)
				.map(ps -> ((EnumerablePropertySource) ps).getPropertyNames()).flatMap(Arrays::stream).distinct()
				.filter(prop -> !(prop.contains("credentials") || prop.contains("password")))
				.forEach(prop -> add(prop, env.getProperty(prop))
				);
		LOGGER.info("===========================================");
	}
	
	public void add(String key, String value) {
		if (key.startsWith("hxx"))
		elencoHxxProperties.add(new HxxProperties(key, value));
	}

	public List<HxxProperties> getElencoHxxProperties() {
		return elencoHxxProperties;
	}
	
}

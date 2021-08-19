package it.reply.sytel.gup.common.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

@Configuration
public class GupObjectMapperConfig {

	@Bean()
	public ObjectMapper modelMapper() {
		ObjectMapper modelMapper = new ObjectMapper();
		JavaTimeModule module = new JavaTimeModule();
		LocalDateTimeDeserializer localDateTimeDeserializer =  new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
		modelMapper.registerModule(module);
		modelMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return modelMapper;
	} 
}

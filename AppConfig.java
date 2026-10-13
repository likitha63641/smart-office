package com.smartoffice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public String companyName() {
		return "Smart Office";
	}
}

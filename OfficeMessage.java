package com.smartoffice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OfficeMessage {
	
	@Value("${spring.application.name}")
	private String applicationName;
	
 public String getMessage() {
	 return "Welcome to "+ applicationName + "Maintenance Platform!";
 }
}

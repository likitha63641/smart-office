package com.smartoffice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartoffice.util.OfficeMessage;

@RestController
public class HomeController {
	
	private final OfficeMessage officeMessage;
	
	public HomeController(OfficeMessage officeMessage) {
		this.officeMessage = officeMessage;
	}
	
	@GetMapping("/")
	public String home() {
		return "Smart office Maintenance Platform is Running!";
	}
	
	
	
	@GetMapping("/welcome") 
	public String welcome() {
		return officeMessage.getMessage();
	}

}

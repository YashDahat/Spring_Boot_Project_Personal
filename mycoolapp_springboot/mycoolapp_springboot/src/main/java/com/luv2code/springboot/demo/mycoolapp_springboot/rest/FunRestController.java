package com.luv2code.springboot.demo.mycoolapp_springboot.rest;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunRestController {
	//expose "/" that return "Hello world"
	
	@GetMapping("/")
	public String sayHello() {
		return "Hello World! Time on server is:"+LocalDateTime.now();
	}
}

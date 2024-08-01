package com.acsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class EmailMailhogSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailMailhogSpringBootApplication.class, args);
	}

	@GetMapping
	public String hello(){
		return "hello, this is a test!";
	}

}


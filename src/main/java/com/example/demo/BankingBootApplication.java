package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BankingBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingBootApplication.class, args);
	}

}





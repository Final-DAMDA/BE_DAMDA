package com.damda.back;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class BackApplication {


	public static void main(String[] args) {

		SpringApplication.run(BackApplication.class, args);
	}

}

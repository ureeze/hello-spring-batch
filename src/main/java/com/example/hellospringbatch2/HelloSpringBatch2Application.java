package com.example.hellospringbatch2;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class HelloSpringBatch2Application {

	public static void main(String[] args) {
		SpringApplication.run(HelloSpringBatch2Application.class, args);
	}

}

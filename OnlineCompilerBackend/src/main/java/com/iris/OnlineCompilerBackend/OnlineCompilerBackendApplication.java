package com.iris.OnlineCompilerBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OnlineCompilerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineCompilerBackendApplication.class, args);
	}

}

package com.apus.drones.apusdronesbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class ApusDronesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApusDronesBackendApplication.class, args);
	}

}

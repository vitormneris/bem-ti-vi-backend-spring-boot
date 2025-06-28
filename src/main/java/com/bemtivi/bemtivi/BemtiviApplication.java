package com.bemtivi.bemtivi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.bemtivi.bemtivi.persistence.repositories.mongo")
@EnableJpaRepositories(basePackages = "com.bemtivi.bemtivi.persistence.repositories.jpa")
public class BemtiviApplication {

	public static void main(String[] args) {
		SpringApplication.run(BemtiviApplication.class, args);
	}

}

package com.bemtivi.bemtivi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BemtiviApplication {

	public static void main(String[] args) {
		SpringApplication.run(BemtiviApplication.class, args);
	}

}

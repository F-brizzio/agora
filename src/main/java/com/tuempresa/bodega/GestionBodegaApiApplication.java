package com.tuempresa.bodega;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GestionBodegaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionBodegaApiApplication.class, args);
	}

}

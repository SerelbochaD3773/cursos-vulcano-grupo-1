package com.grupo1.cursosvulcano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CursosvulcanoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CursosvulcanoApplication.class, args);
	}

}

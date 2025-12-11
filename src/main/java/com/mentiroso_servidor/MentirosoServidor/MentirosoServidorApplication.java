package com.mentiroso_servidor.MentirosoServidor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MentirosoServidorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MentirosoServidorApplication.class, args);

	}

	@GetMapping("/prueba")
	public String prueba() {
		return "Esto es una prueba";
	}

}

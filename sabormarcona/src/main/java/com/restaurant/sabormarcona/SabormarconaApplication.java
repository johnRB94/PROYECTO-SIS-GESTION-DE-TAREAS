package com.restaurant.sabormarcona;

import com.restaurant.sabormarcona.service.UsuarioInitializationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SabormarconaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SabormarconaApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializePasswords(UsuarioInitializationService usuarioInitializationService) {
		return args -> usuarioInitializationService.initializePasswords();
	}
}

package com.example.RoomRadar;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RoomRadarApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().load();

		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		System.setProperty("EMAIL_USERNAME", dotenv.get("EMAIL_USERNAME"));
		System.setProperty("EMAIL_PASSWORD", dotenv.get("EMAIL_PASSWORD"));

		SpringApplication.run(RoomRadarApplication.class, args);
	}
}

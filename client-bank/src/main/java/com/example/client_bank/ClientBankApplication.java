package com.example.client_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ClientBankApplication {

	public static void main(String[] args) {
        SpringApplication.run(ClientBankApplication.class, args);
	}
}

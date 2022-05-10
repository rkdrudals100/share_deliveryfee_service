package com.toyproject.share_deliveryfee_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShareDeliveryfeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareDeliveryfeeServiceApplication.class, args);
	}

}

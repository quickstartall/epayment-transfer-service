package com.raquibul.bank.transfer.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Application class for the Spring boot application for module Rest-API
 * @see SpringBootApplication
 * @author Raquibul Hasan 
 *
 */
@SpringBootApplication
public class TransferRestApplication extends SpringBootServletInitializer {
	private static Class<TransferRestApplication> applicationClass = TransferRestApplication.class;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}
	public static void main(String... args) {
       SpringApplication.run(TransferRestApplication.class, args);
    }
}

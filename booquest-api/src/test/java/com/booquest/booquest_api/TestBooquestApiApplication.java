package com.booquest.booquest_api;

import org.springframework.boot.SpringApplication;

public class TestBooquestApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(BooquestApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

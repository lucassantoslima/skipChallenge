package com.skipthedishes;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SkipChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkipChallengeApplication.class, args);
	}
	
	@Bean
	public ModelMapper buildModelMapper() {
		return new ModelMapper();
	}
	
}

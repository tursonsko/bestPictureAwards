package com.backbase.bestPictureAwards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class BestPictureAwardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BestPictureAwardsApplication.class, args);
	}

}

package com.backbase.bestPictureAwards.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestCommonConfig {

    @Bean
    public RestTemplate restTemplateWithConnectionAndReadTimeout() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(60_000))
                .setReadTimeout(Duration.ofMillis(60_000))
                .build();
    }

    @Bean
    public Gson gsonWithPrettyPrinting() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }
}
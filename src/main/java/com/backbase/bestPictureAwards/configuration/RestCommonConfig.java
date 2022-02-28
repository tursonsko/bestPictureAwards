package com.backbase.bestPictureAwards.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.List;

@Configuration
public class RestCommonConfig {

    private final ConfigProperties configProperties;

    public RestCommonConfig(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

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

    @Bean
    public HttpEntity<?> httpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.ALL));
        return new HttpEntity<>(headers);
    }

    @Bean
    public String urlTemplate() {
        return UriComponentsBuilder.fromHttpUrl(configProperties.getOmdbApiUrl())
                .queryParam(configProperties.getOmdbApiTitleParamName(),
                        "{" + configProperties.getOmdbApiTitleParamName() + "}")
                .queryParam(configProperties.getOmdbApiKeyParamName(),
                        "{" + configProperties.getOmdbApiKeyParamName() + "}")
                .build()
                .toUriString();
    }
}
package com.backbase.bestPictureAwards.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "consts")
@Getter
@Setter
public class ConfigProperties {
    private String awarded_type;
    private String category;
}

package com.grjus.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;

@NoArgsConstructor
public class AppConfig {
    public static final String appConfig = "appConfig.json";

    public ConfigDto getConfig() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File configFile = new File(appConfig);
            if (!configFile.exists()) {
                throw new IllegalArgumentException("Config file not found: " + appConfig);
            }
            return mapper.readValue(configFile, ConfigDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config file", e);
        }
    }
}
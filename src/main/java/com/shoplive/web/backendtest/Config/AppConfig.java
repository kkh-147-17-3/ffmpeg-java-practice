package com.shoplive.web.backendtest.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shoplive.web.backendtest.Util.StorageProperties;

@Configuration
public class AppConfig {
    
    @Bean
    public StorageProperties storageProperties() {
        return new StorageProperties(); 
    }
}

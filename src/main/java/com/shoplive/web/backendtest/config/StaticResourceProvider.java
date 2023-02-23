package com.shoplive.web.backendtest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceProvider implements WebMvcConfigurer {

    @Value("${video.video-url}")
    private String videoUrl;
    @Value("${video.thumbnail-url}")
    private String thumbnailUrl;
    
    @Value("${video.convert-save-path}")
    private String videoPath;
    
    @Value("${video.thumbnail-save-path}")
    private String thumbnailPath;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(videoUrl+"/**")
                .addResourceLocations("file:" + videoPath);

        registry
                .addResourceHandler(thumbnailUrl+"/**")
                .addResourceLocations("file:" + thumbnailPath);
    }
}
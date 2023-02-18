package com.shoplive.web.backendtest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class StaticResourceProvider implements WebMvcConfigurer {

    @Value("${resource.video-url}")
    private String videoUrl;
    @Value("${resource.thumbnail-url}")
    private String thumbnailUrl;
    
    @Value("${ffmpeg.convert-save-path}")
    private String videoPath;
    
    @Value("${ffmpeg.thumbnail-save-path}")
    private String thumbnailPath;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println(videoPath);
        System.out.println(thumbnailPath);
        System.out.println(videoUrl);
        System.out.println(thumbnailUrl);
        registry
                .addResourceHandler(videoUrl+"/**")
                .addResourceLocations("file:///" + videoPath +"/");

        registry
                .addResourceHandler(thumbnailUrl+"/**")
                .addResourceLocations("file:///" + thumbnailPath +"/");
    }
}
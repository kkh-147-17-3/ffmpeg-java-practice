package com.shoplive.web.backendtest.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shoplive.web.backendtest.helper.StorageProperties;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;

@Configuration
public class AppConfig {
    
    @Value("${ffmpeg.bin-path}")
    private String ffmpegBinPath;

    @Bean
    public StorageProperties storageProperties() {
        return new StorageProperties(); 
    }

    @Bean 
    FFmpeg ffMpeg() throws IOException{
        return new FFmpeg(ffmpegBinPath+"ffmpeg");
    }

    @Bean
    FFprobe ffProbe() throws IOException{
        return new FFprobe(ffmpegBinPath+"ffprobe");

    }
}

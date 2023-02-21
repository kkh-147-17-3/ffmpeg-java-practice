package com.shoplive.web.backendtest.helper;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * StorageProperties
 */
@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String videoUploadDir;
    private String thumbnailUploadDir;
}
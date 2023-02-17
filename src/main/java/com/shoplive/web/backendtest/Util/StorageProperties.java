package com.shoplive.web.backendtest.Util;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * StorageProperties
 */
@ConfigurationProperties(prefix = "file")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
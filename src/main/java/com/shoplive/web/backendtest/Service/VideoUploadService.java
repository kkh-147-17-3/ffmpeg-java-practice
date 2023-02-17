package com.shoplive.web.backendtest.Service;

import org.springframework.web.multipart.MultipartFile;

public interface VideoUploadService {

    void init();

	String create(MultipartFile videoFile);
    
}

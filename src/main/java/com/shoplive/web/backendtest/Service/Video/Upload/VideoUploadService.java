package com.shoplive.web.backendtest.service.Video.Upload;

import org.springframework.web.multipart.MultipartFile;

import com.shoplive.web.backendtest.dto.VideoUploadRequestDto;

public interface VideoUploadService {

    void init();
	String create(MultipartFile videoFile);
}

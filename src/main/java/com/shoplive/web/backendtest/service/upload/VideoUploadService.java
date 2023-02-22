package com.shoplive.web.backendtest.service.upload;

import org.springframework.web.multipart.MultipartFile;

import com.shoplive.web.backendtest.request.VideoUploadRequest;
import com.shoplive.web.backendtest.response.VideoUploadResponse;

public interface VideoUploadService {

    void init();
    VideoUploadResponse upload(MultipartFile videoFile, VideoUploadRequest uploadRequest);
	Long create(MultipartFile videoFile, VideoUploadRequest uploadRequest);
    void createResized(Long videoId, String fileName);
    void createThumbnail(Long videoId, String fileName);
}

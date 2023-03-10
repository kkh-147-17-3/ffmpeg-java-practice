package com.shoplive.web.backendtest.service.thumbnail;

import java.io.IOException;

import com.shoplive.web.backendtest.response.VideoProgressResponse;

public interface VideoThumbnailService {

    String createThumbnail(String fileName) throws IOException;
    
    VideoProgressResponse getProgress(Long id);
}

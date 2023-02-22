package com.shoplive.web.backendtest.service.resize;

import com.shoplive.web.backendtest.response.VideoProgressResponse;

public interface VideoResizeService {
    public String createResized(String fileName);
    public VideoProgressResponse getProgress(Long id);
}

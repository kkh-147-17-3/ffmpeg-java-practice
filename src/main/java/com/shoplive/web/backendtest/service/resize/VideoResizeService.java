package com.shoplive.web.backendtest.service.resize;

import com.shoplive.web.backendtest.Response.VideoProgressResponse;

public interface VideoResizeService {
    public String createResized(String fileName);
    public VideoProgressResponse getProgress(Long id);
}

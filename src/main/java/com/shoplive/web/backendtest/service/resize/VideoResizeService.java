package com.shoplive.web.backendtest.service.resize;

import java.util.concurrent.CompletableFuture;

import com.shoplive.web.backendtest.response.VideoProgressResponse;

public interface VideoResizeService {
    public CompletableFuture<String> createResized(String fileName);
    public VideoProgressResponse getProgress(Long id);
}

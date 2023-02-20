package com.shoplive.web.backendtest.service.resize;

import com.shoplive.web.backendtest.dto.VideoProgressResponse;

public interface VideoResizeService {
    public String create(Long videoId, String fileName);
    public VideoProgressResponse getProgress(Long id);
}

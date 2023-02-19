package com.shoplive.web.backendtest.service.resize;

public interface VideoResizeService {
    public String create(Long videoId, String fileName);
    public int getProgress(Long id);
}

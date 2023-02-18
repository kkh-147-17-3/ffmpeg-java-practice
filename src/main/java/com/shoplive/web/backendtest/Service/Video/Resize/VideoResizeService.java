package com.shoplive.web.backendtest.service.Video.Resize;

public interface VideoResizeService {
    public String create(Long videoId, String fileName);
    public int getProgress(Long id);
}

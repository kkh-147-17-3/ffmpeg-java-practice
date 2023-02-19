package com.shoplive.web.backendtest.service;

import com.shoplive.web.backendtest.dto.VideoUploadRequestDto;
import com.shoplive.web.backendtest.entity.Video;

public interface VideoService {

    Video insert(String originalFileName, VideoUploadRequestDto dto);
    public int updateResizedInfo(Long videoId, String resizedFileName);
    public int updateThumbnailUrl(Long videoId, String thumbnailUrl);
}

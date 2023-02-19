package com.shoplive.web.backendtest.service;

import com.shoplive.web.backendtest.dto.VideoDetailsResponse;
import com.shoplive.web.backendtest.dto.VideoUploadRequest;
import com.shoplive.web.backendtest.entity.Video;

public interface VideoService {

    Video insert(String originalFileName, VideoUploadRequest dto);
    public int updateResizedInfo(Long videoId, String resizedFileName);
    public int updateThumbnailUrl(Long videoId, String thumbnailUrl);
    VideoDetailsResponse getDetails(Long id);
}

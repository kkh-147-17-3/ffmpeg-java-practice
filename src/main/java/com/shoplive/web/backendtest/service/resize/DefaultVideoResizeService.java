package com.shoplive.web.backendtest.service.resize;

import org.springframework.stereotype.Service;

import com.shoplive.web.backendtest.Response.VideoProgressResponse;
import com.shoplive.web.backendtest.dao.VideoDao;
import com.shoplive.web.backendtest.entity.Video;
import com.shoplive.web.backendtest.exception.VideoUploadException;
import com.shoplive.web.backendtest.helper.VideoUploadHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultVideoResizeService implements VideoResizeService {

    private final VideoUploadHelper videoUploadHelper;
    private final int resizedWidth = 360;

    private final VideoDao videoDao;

    @Override
    public String createResized(String originalFileName) {
        
        return videoUploadHelper.resizeWidth(originalFileName, resizedWidth);

    }

    @Override
    public VideoProgressResponse getProgress(Long videoId) {
        Integer progress = null;
        Video video = videoDao.getById(videoId);
        if (video == null){
            throw new VideoUploadException("존재하지 않는 ID 입니다.");
        }

        if (video.getResizedFilesize() != 0){
            progress = 100;           
        }
        else{
            String videoPath = videoUploadHelper.getSavedPathFromUrl(video.getOriginalVideoUrl());
            progress = videoUploadHelper.getResizeProgress(videoPath);
        }

        String progressWithPercent = String.valueOf(progress) + "%";
    
        return  VideoProgressResponse.builder()
                                    .id(videoId)
                                    .progress(progressWithPercent)
                                    .build();
    }
}
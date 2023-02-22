package com.shoplive.web.backendtest.service.thumbnail;

import org.springframework.stereotype.Service;

import com.shoplive.web.backendtest.Response.VideoProgressResponse;
import com.shoplive.web.backendtest.dao.VideoDao;
import com.shoplive.web.backendtest.entity.Video;
import com.shoplive.web.backendtest.exception.VideoUploadException;
import com.shoplive.web.backendtest.helper.VideoUploadHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultVideoThumbnailService implements VideoThumbnailService {

    private final VideoUploadHelper videoUploadHelper;
    private final VideoDao videoDao;

    @Override
    public String createThumbnail(String fileName){

        return videoUploadHelper.createThumbnail(fileName);
        
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
            String thumbnailPath = videoUploadHelper.getSavedPathFromUrl(video.getOriginalVideoUrl());
            progress = videoUploadHelper.getThumbnailProgress(thumbnailPath);
        }

        String progressWithPercent = String.valueOf(progress) + "%";
    
        return  VideoProgressResponse.builder()
                                    .id(videoId)
                                    .progress(progressWithPercent)
                                    .build();
    }
    
}

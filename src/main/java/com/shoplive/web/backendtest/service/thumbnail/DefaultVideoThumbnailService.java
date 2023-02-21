package com.shoplive.web.backendtest.service.thumbnail;

import org.springframework.stereotype.Service;

import com.shoplive.web.backendtest.helper.VideoUploadHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultVideoThumbnailService implements VideoThumbnailService {

    private final VideoUploadHelper videoUploadHelper;

    @Override
    public String createThumbnail(String fileName){

        return videoUploadHelper.createThumbnail(fileName);
        
    }
    
}

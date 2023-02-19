package com.shoplive.web.backendtest.service.resize;

import org.springframework.stereotype.Service;

import com.shoplive.web.backendtest.util.VideoUploadUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultVideoResizeService implements VideoResizeService {

    private final VideoUploadUtil videoUploadUtil;
    private final int resizedWidth = 360;

    @Override
    public String create(Long videoId, String fileName) {

        return videoUploadUtil.resizeByWidth(videoId, fileName, resizedWidth);

    }

    @Override
    public int getProgress(Long id) {
        Integer progress =  videoUploadUtil.getProgressMap().get(id);
        if (progress == null) return -1;
        else return progress;
    }
}
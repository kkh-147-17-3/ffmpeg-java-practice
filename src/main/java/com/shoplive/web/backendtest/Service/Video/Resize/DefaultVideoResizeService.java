package com.shoplive.web.backendtest.service.Video.Resize;

import org.springframework.stereotype.Service;

import com.shoplive.web.backendtest.Util.VideoUploadUtil;
import com.shoplive.web.backendtest.dao.VideoDao;
import com.shoplive.web.backendtest.entity.Video;

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
        return videoUploadUtil.getProgressMap().get(id);
    }
}